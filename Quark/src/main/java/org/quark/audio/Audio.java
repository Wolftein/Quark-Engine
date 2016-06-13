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
package org.quark.audio;

import org.quark.resource.AssetDescriptor;
import org.quark.system.utility.Disposable;
import org.quark.system.utility.Manageable;

/**
 * <code>Audio</code> encapsulate the data of a sound.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public abstract class Audio extends Manageable implements Disposable {
    private final int mDuration;
    private final int mRate;
    private final AudioFormat mFormat;

    /**
     * <p>Constructor</p>
     */
    public Audio(AudioFormat format, int duration, int rate) {
        mDuration = duration;
        mRate = rate;
        mFormat = format;
    }

    /**
     * <p>Get the duration (in millisecond) of the audio</p>
     *
     * @return the duration (in millisecond) of the audio
     */
    public final int getDuration() {
        return mDuration;
    }

    /**
     * <p>Get the sampler rate (in KHz) of the audio</p>
     *
     * @return the sampler rate (in KHz) of the audio
     */
    public final int getRate() {
        return mRate;
    }

    /**
     * <p>Get the format of the audio</p>
     *
     * @return the format of the audio
     */
    public final AudioFormat getFormat() {
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
    public final boolean isMono() {
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
    public final boolean isStereo() {
        return mFormat.eChannel == 0x02;
    }

    /**
     * <p>Check if the audio is being streaming</p>
     *
     * @return <code>true</code> if the audio is being streaming, <code>false</code> otherwise
     */
    public abstract boolean isStreaming();

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
