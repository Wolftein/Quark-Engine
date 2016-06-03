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

import org.quark_engine.resource.AssetDescriptor;
import org.quark_engine.system.utility.Disposable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * <code>Audio</code> encapsulate the data of a sound.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Audio implements Disposable {
    private final Factory mFactory;
    private final int mDuration;
    private final int mRate;
    private final AudioFormat mFormat;

    /**
     * <p>Constructor</p>
     */
    public Audio(Factory factory, AudioFormat format, int duration, int rate) {
        mFactory = factory;
        mFormat = format;
        mDuration = duration;
        mRate = rate;
    }

    /**
     * <p>Get the factory of the audio (which serve the audio's data)</p>
     *
     * @return the factory of the audio (which serve the audio's data)
     */
    public Factory getFactory() {
        return mFactory;
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
        return mFormat.eComponent == 0x08;
    }

    /**
     * <p>Check if the audio have stereo channel</p>
     * s
     *
     * @return <code>true</code> if the audio have stereo channel, <code>false</code> otherwise
     *
     * @see AudioFormat#STEREO_8
     * @see AudioFormat#STEREO_16
     */
    public boolean isStereo() {
        return mFormat.eComponent == 0x10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        mFactory.close();
    }

    /**
     * <code>Factory</code> encapsulate the definition of a buffer within an <code>Audio</code>.
     */
    public interface Factory {
        int read(byte[] buffer, int offset, int length);

        void reset();

        void close();
    }

    /**
     * <code>StaticFactory</code> encapsulate a {@link Factory} which contain(s) the data in a static container.
     */
    public final static class StaticFactory implements Factory {
        private final ByteBuffer mData;

        /**
         * <p>Constructor</p>
         */
        public StaticFactory(ByteBuffer data) {
            mData = data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read(byte[] buffer, int offset, int length) {
            final int position = mData.position();

            //!
            //! Read the data from the given offset.
            //!
            mData.get(buffer, offset, length);

            return mData.position() - position;
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
     * <code>DynamicFactory</code> encapsulate a {@link Factory} which contain(s) the data in a dynamic container.
     */
    public final static class DynamicFactory implements Factory {
        private final InputStream mData;

        /**
         * <p>Constructor</p>
         */
        public DynamicFactory(InputStream stream, int position) {
            mData = stream.markSupported() ? stream : new BufferedInputStream(stream);
            mData.mark(position);
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

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link Audio}.
     */
    public final static class Descriptor extends AssetDescriptor {
        /**
         * <p>constructor</p>
         */
        public Descriptor(boolean streaming) {
            super(true, !streaming);
        }
    }
}
