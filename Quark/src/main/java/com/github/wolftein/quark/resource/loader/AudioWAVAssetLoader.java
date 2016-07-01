/*
 * This file is part of Quark Framework, licensed under the APACHE License.
 *
 * Copyright (c) 2014-2016 Agustin L. Alvarez <wolftein1@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wolftein.quark.resource.loader;

import com.github.wolftein.quark.audio.factory.FactoryStaticAudio;
import com.github.wolftein.quark.audio.factory.FactoryStreamingAudio;
import com.github.wolftein.quark.resource.AssetKey;
import com.github.wolftein.quark.resource.AssetLoader;
import com.github.wolftein.quark.resource.AssetManager;
import com.github.wolftein.quark.system.utility.array.Int8Array;
import com.github.wolftein.quark.audio.Audio;
import com.github.wolftein.quark.audio.AudioFormat;
import com.github.wolftein.quark.system.utility.array.ArrayFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <code>AudioWAVEAssetLoader</code> encapsulate an {@link AssetLoader} for loading WAVE audio(s).
 */
public final class AudioWAVAssetLoader implements AssetLoader<Audio, Audio.Descriptor> {
    /**
     * <code>AudioHeader</code> represent the file format of a WAVE sound.
     */
    private final static class AudioHeader {
        public int mAudioBlock;
        public int mAudioBit;
        public int mAudioChannel;
        public int mAudioDuration;
        public int mAudioRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(AssetManager manager,
            AssetKey<Audio, Audio.Descriptor> key, InputStream input) throws IOException {
        key.setAsset(readAudio(key.getDescriptor(), new DataInputStream(input)));
    }

    /**
     * <p>Read an {@link Audio} from the {@link InputStream} given</p>
     *
     * @param descriptor the texture descriptor
     * @param input      the input-stream that contain(s) the audio
     *
     * @return the audio
     *
     * @throws IOException indicates failing loading the audio
     */
    private Audio readAudio(Audio.Descriptor descriptor, DataInputStream input) throws IOException {
        if (readIntLittleEndian(input) != 0x46464952) {
            throw new IOException("Trying to read an invalid <WAV> sound.");
        }

        //!
        //! Skip the size of the audio.
        //!
        input.skipBytes(0x04);

        if (readIntLittleEndian(input) != 0x45564157) {
            throw new IOException("Trying to read an empty <WAV> sound.");
        }

        //!
        //! The header of the audio suppose to be the first chunk.
        //!
        AudioHeader header = null;

        do {
            final int type = readIntLittleEndian(input);
            final int length = readIntLittleEndian(input);

            switch (type) {
                case 0x20746D66:
                    header = readHeader(input, length);

                    break;
                case 0x61746164:
                    if (header == null) {
                        throw new IOException("Missing <WAV> header.");
                    }
                    header.mAudioDuration = (int) (((float) length / header.mAudioBlock) * 1000);

                    if (descriptor.isCloseable()) {
                        final Int8Array content = ArrayFactory.allocateInt8Array(length);

                        final byte[] read = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = input.read(read)) > 0) {
                            content.write(read, 0, Math.min(bytesRead, content.remaining()));
                        }
                        content.flip();

                        return new FactoryStaticAudio(content, getUncompressedFormat(header),
                                header.mAudioDuration,
                                header.mAudioRate);
                    } else {

                        return new FactoryStreamingAudio(input, getUncompressedFormat(header),
                                header.mAudioDuration,
                                header.mAudioRate);
                    }
                default:

                    if (input.skipBytes(length) <= 0) {
                        throw new IOException("Failed to read the <WAV> sound.");
                    }

                    break;
            }
        } while (true);
    }

    /**
     * <p>Read audio header from the {@link DataInputStream} given</p>
     *
     * @param input  the input that contain(s) the header
     * @param length the length of the header
     *
     * @throws IOException indicates if the audio has invalid header
     */
    private AudioHeader readHeader(DataInputStream input, int length) throws IOException {
        //!
        //! Check if we support the compression of the sound
        //!
        if (readShortLittleEndian(input) != 0x01) {
            throw new IOException("Only PCM compression is supported.");
        }

        final int hChannel = readShortLittleEndian(input);
        final int hRate = readIntLittleEndian(input);
        final int hBlock = readIntLittleEndian(input);
        final int hBlockAlign = readShortLittleEndian(input);
        final int hBit = readShortLittleEndian(input);

        //!
        //! Check if the header is valid
        //!
        final int expected = (hBit * hChannel * hRate) / 8;
        if (expected != hBlock) {
            throw new IOException(
                    "Expected " + expected + " bytes per second, instead got " + hBlock);
        }

        if (hBit != 8 && hBit != 16) {
            throw new IOException("Only 8 and 16 bits per sample are supported.");
        }
        if ((hBit / 8) * hChannel != hBlockAlign) {
            throw new IOException("Invalid bytes per sample value");
        }
        if (hBlockAlign * hRate != hBlock) {
            throw new IOException("Invalid bytes per second value");
        }

        //!
        //! Some header are different.
        //!
        final int skip = length - 16;
        if (skip > 0) {
            input.skipBytes(skip);
        }

        final AudioHeader header = new AudioHeader();
        header.mAudioBit = hBit;
        header.mAudioBlock = hBlock;
        header.mAudioChannel = hChannel;
        header.mAudioRate = hRate;

        return header;
    }

    /**
     * <p>Get the uncompressed format of the audio</p>
     *
     * @param header the header of the audio
     *
     * @return the uncompressed format of the audio
     *
     * @throws IOException indicates if the format is unsupported
     */
    private AudioFormat getUncompressedFormat(AudioHeader header) throws IOException {
        switch (header.mAudioChannel) {
            case 0x01:
                switch (header.mAudioBit) {
                    case 8:
                        return AudioFormat.MONO_8;
                    case 16:
                        return AudioFormat.MONO_16;
                    default:
                        break;
                }
                break;
            case 0x02:
                switch (header.mAudioBit) {
                    case 8:
                        return AudioFormat.STEREO_8;
                    case 16:
                        return AudioFormat.STEREO_16;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        throw new IOException("Uncompressed format not supported.");
    }

    /**
     * <b>Read a little endian integer (Required for WAV format)</b>
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int b0 = in.readInt();
        final int b1 = (b0 >> 0) & 0xFF;
        final int b2 = (b0 >> 8) & 0xFF;
        final int b3 = (b0 >> 16) & 0xFF;
        final int b4 = (b0 >> 24) & 0xFF;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    /**
     * <b>Read a little endian short (Required for WAV format)</b>
     */
    private int readShortLittleEndian(DataInputStream in) throws IOException {
        final int b0 = in.readShort();
        final int b1 = (b0 & 0xFF);
        final int b2 = (b0 >> 8) & 0xFF;

        return (short) (b1 << 8 | b2 << 0);
    }
}
