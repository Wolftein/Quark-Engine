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

import de.jarnbjo.ogg.*;
import de.jarnbjo.vorbis.IdentificationHeader;
import de.jarnbjo.vorbis.VorbisStream;
import org.quark_engine.audio.Audio;
import org.quark_engine.audio.AudioFormat;
import org.quark_engine.audio.factory.FactoryAudioStatic;
import org.quark_engine.audio.factory.FactoryAudioStreaming;
import org.quark_engine.resource.AssetKey;
import org.quark_engine.resource.AssetLoader;
import org.quark_engine.resource.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Encapsulate an {@link AssetLoader} for loading OGG audio(s).
 * <p>
 * {@link Audio}
 * {@link AudioFormat#MONO_16}
 * {@link AudioFormat#STEREO_16}
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AudioOGGAssetLoader implements AssetLoader<Audio, Audio.Descriptor> {
    /**
     * <code>AudioHeader</code> represent the file format of a OGG sound.
     */
    private final static class AudioHeader {
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
        return new AssetKey<>(readAudio(descriptor, input), descriptor);
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
    private Audio readAudio(Audio.Descriptor descriptor, InputStream input) throws IOException {
        //!
        //! Create a static or dynamic stream.
        //!
        final ExtendedOggStream stream = descriptor.isCloseable()
                ? new UncachedStream(input)
                : new CachedStream(input);

        //!
        //! Get the logical stream.
        //!
        final LogicalOggStream logical = stream.getLogicalStream();

        if (descriptor.isCloseable()) {
            return readAudio(stream, logical, new VorbisStream(logical));
        }
        return readAudioStreaming(stream, logical, new VorbisStream(logical));
    }

    /**
     * <p>Read an {@link FactoryAudioStatic} from the {@link ExtendedOggStream} given</p>
     *
     * @throws IOException indicates failing loading the audio
     */
    private FactoryAudioStatic readAudio(ExtendedOggStream stream, LogicalOggStream logical,
            VorbisStream vorbis) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        //!
        //! Read everything into a byte array.
        //!
        final byte[] buffer = new byte[1024];
        try {
            int read;

            while ((read = vorbis.readPcm(buffer, 0, buffer.length)) > 0) {
                output.write(swap(buffer, 0, read), 0, read);
            }
        } catch (EndOfOggStreamException ignored) {
        }
        final byte[] bytes = output.toByteArray();

        //!
        //! Get the header.
        //!
        final IdentificationHeader header = vorbis.getIdentificationHeader();

        //!
        //! Calculate the length of the data.
        //!
        final int length
                = (int) (stream.getAbsoluteGranulePosition() * header.getChannels() * 2);
        final int duration
                = (int) ((float) length / (2 * header.getChannels() * header.getSampleRate())) * 1000;

        //!
        //! Create the buffer for the audio and fill it with all the PCM data.
        //!
        final ByteBuffer data = ByteBuffer.allocateDirect(length).order(ByteOrder.nativeOrder());
        data.put(bytes, 0, length).flip();

        //!
        //! Close everything.
        //!
        vorbis.close();

        return new FactoryAudioStatic(data, getUncompressedFormat(header), duration, header.getSampleRate());
    }

    /**
     * <p>Read an {@link FactoryAudioStreaming} from the {@link ExtendedOggStream} given</p>
     *
     * @throws IOException indicates failing loading the audio
     */
    private FactoryAudioStreaming readAudioStreaming(ExtendedOggStream stream, LogicalOggStream logical,
            VorbisStream vorbis) throws IOException {
        //!
        //! Get the header.
        //!
        final IdentificationHeader header = vorbis.getIdentificationHeader();

        //!
        //! Calculate the length of the data.
        //!
        final int length
                = (int) (stream.getAbsoluteGranulePosition() * header.getChannels() * 2);
        final int duration
                = (int) ((float) length / (2 * header.getChannels() * header.getSampleRate())) * 1000;

        return new FactoryAudioStreaming(new OGGInputStream((CachedStream) stream, logical, vorbis),
                getUncompressedFormat(header), duration, header.getSampleRate());
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
    private AudioFormat getUncompressedFormat(IdentificationHeader header) throws IOException {
        switch (header.getChannels()) {
            case 0x01:
                return AudioFormat.MONO_16;
            case 0x02:
                return AudioFormat.STEREO_16;
            default:
                break;
        }
        throw new IOException("Uncompressed format not supported.");
    }

    /**
     * <code>OGGInputStream</code> encapsulate an {@link InputStream} for {@link FactoryAudioStreaming}.
     */
    private final static class OGGInputStream extends InputStream {
        private final CachedStream mStream;
        private LogicalOggStream mLogical;
        private VorbisStream mVorbis;

        /**
         * Hold number of byte(s) available.
         */
        private final int mTotal;
        private int mCurrent = 0;

        /**
         * Hold whenever the input is already at the end of the stream.
         */
        private boolean mInputEof;

        /**
         * <p>Constructor</p>
         */
        public OGGInputStream(CachedStream stream, LogicalOggStream logical, VorbisStream vorbis) {
            mStream = stream;
            mLogical = logical;
            mVorbis = vorbis;

            mTotal = (int) (stream.getAbsoluteGranulePosition() * vorbis.getIdentificationHeader().getChannels() * 2);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read() throws IOException {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(byte[] buffer) throws IOException {
            return read(buffer, 0, buffer.length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            if (mInputEof) {
                return -1;
            }

            int read = 0, count;

            while (read < length) {
                try {
                    final int remainingInStream = mTotal - mCurrent;
                    final int remainingToBuffer = length - read;

                    final int toRead = Math.min(remainingInStream, remainingToBuffer);

                    if ((count = mVorbis.readPcm(buffer, offset + read, toRead)) <= 0
                            || mCurrent >= mTotal) {
                        mInputEof = true;
                        break;
                    }
                } catch (EndOfOggStreamException exception) {
                    mInputEof = true;
                    break;
                }
                read += count;
            }
            if (read > 0) {
                swap(buffer, 0, read);
            }
            return (read > 0) ? read : -1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            mVorbis.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() throws IOException {
            mLogical = mStream.reload();
            mVorbis  = new VorbisStream(mLogical);

            mInputEof = false;
            mCurrent = 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean markSupported() {
            return true;
        }
    }

    /**
     * <code>ExtendedOggStream</code> encapsulate an extended mode for {@link PhysicalOggStream}.
     */
    private interface ExtendedOggStream extends PhysicalOggStream {
        long getAbsoluteGranulePosition();

        LogicalOggStream getLogicalStream();
    }

    /**
     * <code>UncachedStream</code> encapsulate a {@link ExtendedOggStream} that won't cache anything.
     */
    private final static class UncachedStream implements ExtendedOggStream {
        /**
         * Hold all page(s).
         */
        private final LinkedList<OggPage> mPages = new LinkedList<>();

        /**
         * Hold all logical stream(s).
         */
        private final Map<Integer, LogicalOggStream> mLogical = new HashMap<>();

        /**
         * Hold the input.
         */
        private final InputStream mInput;

        /**
         * Hold state(s).
         */
        private boolean mInputClose;
        private boolean mInputEof;
        private boolean mInputBos;

        /**
         * Hold the absolute granule position.
         */
        private long mAbsoluteGranulePosition;

        /**
         * <p>Constructor</p>
         */
        public UncachedStream(InputStream input) throws IOException {
            mInput = input;

            do {
                //!
                //! Read until there is no more page(s) to read.
                //!
                read();
            } while (!mInputBos);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<LogicalOggStream> getLogicalStreams() {
            return mLogical.values();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public OggPage getOggPage(int index) throws IOException {
            if (mPages.size() == 0 && !mInputEof) {
                read();
            }
            return mPages.removeFirst();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isOpen() {
            return !mInputClose;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            mInputClose = true;
            mInput.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTime(long granulePosition) throws IOException {
            //!
            //! NOT_REQUIRED
            //!
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isSeekable() {
            return false;
        }

        /**
         * <p>Read a new {@link OggPage}</p>
         */
        private void read() throws IOException {
            final OggPage page = OggPage.create(mInput);

            if (page.isBos()) {
                mInputBos = true;
            }
            if (page.isEos()) {
                mInputEof = true;
                mAbsoluteGranulePosition = page.getAbsoluteGranulePosition();
            }

            LogicalOggStreamImpl stream = (LogicalOggStreamImpl) mLogical.get(page.getStreamSerialNumber());
            if (stream == null) {
                stream = new LogicalOggStreamImpl(this, page.getStreamSerialNumber());
                stream.checkFormat(page);

                mLogical.put(page.getStreamSerialNumber(), stream);
            }
            mPages.add(page);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getAbsoluteGranulePosition() {
            return mAbsoluteGranulePosition;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LogicalOggStream getLogicalStream() {
            return mLogical.values().iterator().next();
        }
    }

    /**
     * <code>CachedStream</code> encapsulate a {@link ExtendedOggStream} that caches.
     */
    private final static class CachedStream implements ExtendedOggStream {
        /**
         * Hold all page(s).
         */
        private final Map<Integer, OggPage> mPages = new HashMap<>();

        /**
         * Hold all logical stream(s).
         */
        private final Map<Integer, LogicalOggStream> mLogical = new HashMap<>();

        /**
         * Hold the input.
         */
        private final InputStream mInput;

        /**
         * Hold state(s).
         */
        private boolean mInputClose;
        private boolean mInputEof;
        private boolean mInputBos;
        private int mSerial;

        /**
         * Hold the absolute granule position.
         */
        private long mAbsoluteGranulePosition;

        /**
         * <p>Constructor</p>
         */
        public CachedStream(InputStream input) throws IOException {
            mInput = input;

            do {
                //!
                //! Read until there is no more page(s) to read.
                //!
                read();
            } while (!mInputEof);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<LogicalOggStream> getLogicalStreams() {
            return mLogical.values();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public OggPage getOggPage(int index) throws IOException {
            return mPages.get(index);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isOpen() {
            return !mInputClose;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            mInputClose = true;
            mInput.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setTime(long granulePosition) throws IOException {
            for (final LogicalOggStream stream : mLogical.values()) {
                stream.setTime(granulePosition);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isSeekable() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getAbsoluteGranulePosition() {
            return mAbsoluteGranulePosition;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LogicalOggStream getLogicalStream() {
            return mLogical.values().iterator().next();
        }

        /**
         * <p>Reload the {@link LogicalOggStream}</p>
         */
        public LogicalOggStream reload() {
            mLogical.clear();

            final LogicalOggStreamImpl logical = new LogicalOggStreamImpl(this, mSerial);
            mLogical.put(mSerial, logical);

            for (final Map.Entry<Integer, OggPage> entry : mPages.entrySet()) {
                logical.addPageNumberMapping(entry.getKey());
                logical.addGranulePosition(entry.getValue().getAbsoluteGranulePosition());
            }
            return logical;
        }

        /**
         * <p>Read a new {@link OggPage}</p>
         */
        private void read() throws IOException {
            final OggPage page = OggPage.create(mInput);

            if (page.isBos()) {
                mInputBos = true;
            }
            if (page.isEos()) {
                mInputEof = true;
                mAbsoluteGranulePosition = page.getAbsoluteGranulePosition();
            }

            LogicalOggStreamImpl stream = (LogicalOggStreamImpl) mLogical.get(page.getStreamSerialNumber());
            if (stream == null) {
                stream = new LogicalOggStreamImpl(this, page.getStreamSerialNumber());
                stream.checkFormat(page);

                mSerial = page.getStreamSerialNumber();

                mLogical.put(page.getStreamSerialNumber(), stream);
            }

            final int index = mPages.size();

            stream.addPageNumberMapping(index);
            stream.addGranulePosition(page.getAbsoluteGranulePosition());

            mPages.put(index, page);
        }
    }

    /**
     * <b>Swap every short byte in the given byte-array</b>
     */
    private static byte[] swap(byte[] data, int offset, int length) throws IOException {
        byte temp;

        for (int i = offset, j = offset + length; i < j; i += 2) {
            temp = data[i];
            data[i] = data[i + 1];
            data[i + 1] = temp;
        }
        return data;
    }
}
