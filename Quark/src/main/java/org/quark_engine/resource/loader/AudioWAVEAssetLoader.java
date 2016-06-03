/*
 * This file is part of Quark Engine, licensed under the APACHE License.
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
package org.quark_engine.resource.loader;

import org.quark_engine.audio.Audio;
import org.quark_engine.audio.AudioFormat;
import org.quark_engine.resource.AssetKey;
import org.quark_engine.resource.AssetLoader;
import org.quark_engine.resource.AssetManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Encapsulate an {@link AssetLoader} for loading WAVE audio(s).
 * <p>
 * {@link org.quark_engine.audio.Audio}
 * {@link org.quark_engine.audio.AudioFormat#MONO_8}
 * {@link org.quark_engine.audio.AudioFormat#MONO_16}
 * {@link org.quark_engine.audio.AudioFormat#STEREO_8}
 * {@link org.quark_engine.audio.AudioFormat#STEREO_16}
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AudioWAVEAssetLoader implements AssetLoader<Audio, Audio.Descriptor> {
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
    public AssetKey<Audio, Audio.Descriptor> load(AssetManager manager, InputStream input,
            Audio.Descriptor descriptor) throws IOException {
        return new AssetKey<>(readAudio(descriptor, new DataInputStream(input)), descriptor);
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
            throw new IOException("Trying to read an invalid <WAVE> sound.");
        }

        //!
        //! Skip the size of the audio.
        //!
        input.skipBytes(4);

        if (readIntLittleEndian(input) != 0x45564157) {
            throw new IOException("Trying to read an empty <WAVE> sound.");
        }

        //!
        //! The header of the audio suppose to be the first chunk.
        //!
        AudioHeader header = null;

        int position = 0;
        do {
            final int type = readIntLittleEndian(input);
            final int length = readIntLittleEndian(input);
            position += 4;

            switch (type) {
                case 0x20746D66:
                    header = readHeader(input);

                    position += length;
                    break;
                case 0x61746164:
                    if (header == null) {
                        throw new IOException("Missing <WAVE> header.");
                    }
                    header.mAudioDuration = (length / header.mAudioBlock);

                    if (descriptor.isCloseable()) {
                        final ByteBuffer content
                                = ByteBuffer.allocateDirect(length).order(ByteOrder.nativeOrder());

                        final byte[] read = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = input.read(read)) > 0) {
                            content.put(read, 0,
                                    Math.min(bytesRead, content.remaining()));
                        }
                        content.flip();

                        return new Audio(new Audio.StaticFactory(content), getUncompressedFormat(header),
                                header.mAudioRate,
                                header.mAudioDuration);
                    } else {

                        return new Audio(new Audio.DynamicFactory(input, position), getUncompressedFormat(header),
                                header.mAudioRate,
                                header.mAudioDuration);
                    }
                default:
                    if (input.skipBytes(length) <= 0) {
                        throw new IOException("Failed to read the <WAVE> sound.");
                    }
                    position += length;
                    break;
            }
        } while (true);
    }


    /**
     * <p>Read audio header from the {@link DataInputStream} given</p>
     *
     * @param input the input that contain(s) the header
     *
     * @throws IOException indicates if the audio has invalid header
     */
    private AudioHeader readHeader(DataInputStream input) throws IOException {
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
     * <b>Read a little endian integer (Required for WAVE format)</b>
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int int1 = in.readByte();
        final int int2 = in.readByte();
        final int int3 = in.readByte();
        final int int4 = in.readByte();

        return (int4 << 24 | (int3 & 0xFF) << 16 | (int2 & 0xFF) << 8 | (int1 & 0xFF));
    }

    /**
     * <b>Read a little endian short (Required for WAVE format)</b>
     */
    private int readShortLittleEndian(DataInputStream in) throws IOException {
        final int int1 = in.readByte();
        final int int2 = in.readByte();

        return ((int2 & 0xFF) << 8 | (int1 & 0xFF));
    }
}
