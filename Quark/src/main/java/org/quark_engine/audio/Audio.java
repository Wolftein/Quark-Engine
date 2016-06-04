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
package org.quark_engine.audio;

import org.quark_engine.system.utility.Disposable;
import org.quark_engine.system.utility.Manageable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.quark_engine.Quark.QkAudioManager;

/**
 * <code>Audio</code> encapsulate the data of a sound.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Audio extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);

    private final Data mData;
    private final int mDuration;
    private final int mRate;
    private final AudioFormat mFormat;


    /**
     * <p>Constructor</p>
     */
    public Audio(Data data, AudioFormat format, int duration, int rate) {
        mData = data;
        mFormat = format;
        mDuration = duration;
        mRate = rate;

        setUpdate(CONCEPT_DATA);
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
     * <p>Get the duration (in millisecond) of the audio</p>
     *
     * @return the duration (in millisecond) of the audio
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * <p>Get the sampler rate (in MHz) of the audio</p>
     *
     * @return the sampler rate (in MHz) of the audio
     */
    public int getRate() {
        return mRate;
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
     * <p>Check if the audio have mono channel</p>
     *
     * @return <code>true</code> if the audio have mono channel, <code>false</code> otherwise
     *
     * @see AudioFormat#MONO_8
     * @see AudioFormat#MONO_16
     */
    public boolean isMono() {
        return mFormat.eChannel == 0x01;
    }

    /**
     * <p>Check if the audio have stereo channel</p>
     *
     * @return <code>true</code> if the audio have stereo channel, <code>false</code> otherwise
     *
     * @see AudioFormat#STEREO_8
     * @see AudioFormat#STEREO_16
     */
    public boolean isStereo() {
        return mFormat.eChannel == 0x02;
    }

    /**
     * <p>Check if the audio is being streaming</p>
     *
     * @return <code>true</code> if the audio is being streaming, <code>false</code> otherwise
     */
    public boolean isStreaming() {
        return mData instanceof DynamicData;
    }

    /**
     * @see AudioManager#dispose(Manageable)
     */
    @Override
    public void dispose() {
        QkAudioManager.dispose(this);
    }

    /**
     * <code>Data</code> encapsulate the definition of a buffer within an <code>Audio</code>.
     */
    public interface Data {
        ByteBuffer direct();

        int read(byte[] buffer, int offset, int length);

        void reset();

        void close();
    }

    /**
     * <code>StaticData</code> encapsulate a {@link Data} which contain(s) the data in a static container.
     */
    public final static class StaticData implements Data {
        private final ByteBuffer mData;

        /**
         * <p>Constructor</p>
         */
        public StaticData(ByteBuffer data) {
            mData = data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuffer direct() {
            return mData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(byte[] buffer, int offset, int length) {
            mData.position(offset);
            mData.get(buffer, 0, length);

            return mData.position() - offset;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() {
            mData.rewind();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {
            mData.clear();
        }
    }

    /**
     * <code>DynamicData</code> encapsulate a {@link Data} which contain(s) the data in a dynamic container.
     */
    public final static class DynamicData implements Data {
        private final InputStream mData;

        /**
         * <p>Constructor</p>
         */
        public DynamicData(InputStream stream, int position) {
            mData = stream.markSupported() ? stream : new BufferedInputStream(stream);
            mData.mark(position);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuffer direct() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(byte[] buffer, int offset, int length) {
            int bytes = 0;

            try {
                bytes = mData.read(buffer, offset, length);
            } catch (IOException ignored) {
            }
            return bytes;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() {
            try {
                mData.reset();
            } catch (IOException ignored) {
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {
            try {
                mData.close();
            } catch (IOException ignored) {
            }
        }
    }
}
