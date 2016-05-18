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
package org.quark.engine.media.openal.audio;

import org.quark.engine.resource.AssetDescriptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * <code>Audio</code> encapsulate the data for any sound.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Audio {
    private final Data mData;
    private final AudioFormat mFormat;
    private final int mRate;
    private final int mDuration;

    /**
     * <p>Constructor</p>
     */
    public Audio(Data provider, AudioFormat format, int rate, int duration) {
        mData = provider;
        mFormat = format;
        mRate = rate;
        mDuration = duration;
    }

    /**
     * <p>Get the data of the audio</p>
     *
     * @return the data of the audio
     */
    public Data getData() {
        return mData;
    }

    /**
     * <p>Get the format of the audio</p>
     *
     * @return the format of the audio
     */
    public AudioFormat getFormat() {
        return mFormat;
    }

    /**
     * <p>Get the sample rate of the audio</p>
     *
     * @return the sample rate of the audio
     */
    public int getRate() {
        return mRate;
    }

    /**
     * <p>Get the duration (in milliseconds) of the audio</p>
     *
     * @return the duration (in milliseconds) of the audio
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * <code>Provider</code> encapsulate the data for {@link Audio}.
     */
    public interface Data {
        /**
         * <p>Handle when the audio requested to reset the stream</p>
         */
        void onReset();

        /**
         * <p>Handle when the audio requested data from the stream</p>
         *
         * @param buffer the audio's buffer
         * @param offset the audio's offset
         * @param length the audio's length
         *
         * @return the number of byte(s) read from the stream
         */
        int onRead(byte[] buffer, int offset, int length);

        /**
         * <p>Handle when the audio requested to close the stream</p>
         */
        void onClose();
    }

    /**
     * <code>BufferProvider</code> represent a {@link Data} backend by {@link ByteBuffer}.
     */
    public final static class BufferData implements Data {
        private final ByteBuffer mBuffer;

        /**
         * <p>Constructor</p>
         */
        public BufferData(ByteBuffer buffer) {
            mBuffer = buffer;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onReset() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int onRead(byte[] buffer, int offset, int length) {
            final int position = mBuffer.position();

            mBuffer.get(buffer, offset, length);

            return mBuffer.position() - position;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClose() {
        }
    }

    /**
     * <code>StreamProvider</code> represent a {@link Data} backend by {@link InputStream}.
     */
    public final static class StreamData implements Data {
        private final InputStream mStream;

        /**
         * <p>Constructor</p>
         */
        public StreamData(InputStream stream, int position) {
            mStream = stream.markSupported() ? stream : new BufferedInputStream(stream);
            mStream.mark(position);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onReset() {
            try {
                mStream.reset();
            } catch (IOException ignored) {
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int onRead(byte[] buffer, int offset, int length) {
            try {
                return mStream.read(buffer, offset, length);
            } catch (IOException ignored) {
            }
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClose() {
            try {
                mStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link Audio}.
     */
    public final static class Descriptor extends AssetDescriptor {
        /**
         * <p>Default constructor</p>
         */
        public Descriptor(boolean streaming) {
            super(true, !streaming);
        }
    }
}
