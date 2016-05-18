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
package org.quark.engine.resource.loader;

import org.quark.engine.media.openal.audio.Audio;
import org.quark.engine.media.openal.audio.AudioFormat;
import org.quark.engine.resource.AssetKey;
import org.quark.engine.resource.AssetLoader;
import org.quark.engine.resource.AssetManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Encapsulate an {@link AssetLoader} for loading WAV sound(s).
 * <p>
 * {@link org.quark.engine.media.openal.audio.Audio}
 * {@link org.quark.engine.media.openal.audio.AudioFormat#MONO_8}
 * {@link org.quark.engine.media.openal.audio.AudioFormat#MONO_16}
 * {@link org.quark.engine.media.openal.audio.AudioFormat#STEREO_8}
 * {@link org.quark.engine.media.openal.audio.AudioFormat#STEREO_16}
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AudioWAVEAssetLoader implements AssetLoader<Audio, Audio.Descriptor> {
    /**
     * Encapsulate the audio structure for any WAVE audio.
     */
    private final static class WAVEHeader {
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
    public AssetKey<Audio, Audio.Descriptor> load(AssetManager context, Audio.Descriptor descriptor,
            InputStream in) throws IOException {
        return new AssetKey<>(parseAudio(new DataInputStream(in), descriptor), descriptor);
    }

    /**
     * <p>Parse the audio from {@link DataInputStream}</p>
     *
     * @param in         the input      of the audio expressed as <code>DataInputStream</code>
     * @param descriptor the descriptor of the audio expressed as <code>Audio.Descriptor</code>
     *
     * @return a reference to the audio
     *
     * @throws IOException indicates if the audio is invalid
     */
    private Audio parseAudio(DataInputStream in, Audio.Descriptor descriptor) throws IOException {
        if (readIntLittleEndian(in) != 0x46464952) {
            throw new IOException("Trying to read an invalid WAVE sound.");
        }

        //!
        //! Skip the size of the audio
        //!
        in.skipBytes(4);

        if (readIntLittleEndian(in) != 0x45564157) {
            throw new IOException("Trying to read an empty WAVE sound.");
        }

        //!
        //! The header of the audio suppose to be the first chunk
        //!
        WAVEHeader header = null;

        int position = 0;
        do {
            final int type = readIntLittleEndian(in);
            final int length = readIntLittleEndian(in);
            position += 4;

            switch (type) {
                case 0x20746D66:
                    header = parseHeader(in);

                    position += length;
                    break;
                case 0x61746164:
                    if (header == null) {
                        throw new IOException("Missing WAVE header.");
                    }
                    header.mAudioDuration = (length / header.mAudioBlock);

                    if (descriptor.isCloseable()) {
                        final ByteBuffer content
                                = ByteBuffer.allocateDirect(length).order(ByteOrder.nativeOrder());

                        final byte[] read = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(read)) > 0) {
                            content.put(read, 0,
                                    Math.min(bytesRead, content.remaining()));
                        }
                        content.flip();

                        return new Audio(new Audio.BufferData(content), getUncompressedFormat(header),
                                header.mAudioRate,
                                header.mAudioDuration);
                    } else {

                        return new Audio(new Audio.StreamData(in, position), getUncompressedFormat(header),
                                header.mAudioRate,
                                header.mAudioDuration);
                    }
                default:
                    if (in.skipBytes(length) <= 0) {
                        throw new IOException("Failed to read the WAVE sound.");
                    }
                    position += length;
                    break;
            }
        } while (true);
    }

    /**
     * <p>Parse {@link WAVEHeader} from {@link DataInputStream}</p>
     *
     * @param in the input of the audio expressed as <code>DataInputStream</code>
     *
     * @return a reference to the header of the audio
     *
     * @throws IOException indicates if the audio has invalid header
     */
    private WAVEHeader parseHeader(DataInputStream in) throws IOException {
        //!
        //! Check if we support the compression of the sound
        //!
        if (readShortLittleEndian(in) != 0x01) {
            throw new IOException("Only PCM compression is supported.");
        }

        final int hChannel = readShortLittleEndian(in);
        final int hRate = readIntLittleEndian(in);
        final int hBlock = readIntLittleEndian(in);
        final int hBlockAlign = readShortLittleEndian(in);
        final int hBit = readShortLittleEndian(in);

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

        final WAVEHeader header = new WAVEHeader();
        header.mAudioBit = hBit;
        header.mAudioBlock = hBlock;
        header.mAudioChannel = hChannel;
        header.mAudioRate = hRate;
        return header;
    }

    /**
     * <p>Get the uncompressed format</p>
     *
     * @param header the header of the audio expressed as <code>WAVEHeader</code>
     *
     * @return the uncompressed format of the audio
     *
     * @throws IOException indicates if the format is unsupported
     */
    private AudioFormat getUncompressedFormat(WAVEHeader header) throws IOException {
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
        throw new IOException("Unsupported audio format");
    }

    /**
     * (NON-DOCUMENTED)
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int int1 = in.readByte();
        final int int2 = in.readByte();
        final int int3 = in.readByte();
        final int int4 = in.readByte();

        return (int4 << 24 | (int3 & 0xFF) << 16 | (int2 & 0xFF) << 8 | (int1 & 0xFF));
    }

    /**
     * (NON-DOCUMENTED)
     */
    private int readShortLittleEndian(DataInputStream in) throws IOException {
        final int int1 = in.readByte();
        final int int2 = in.readByte();

        return ((int2 & 0xFF) << 8 | (int1 & 0xFF));
    }
}
