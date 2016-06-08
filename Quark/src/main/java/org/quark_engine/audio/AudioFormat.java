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

/**
 * <code>AudioFormat</code> enumerate the format of an {@link Audio}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum AudioFormat {
    /**
     * Represent 8-Bit mono audio.
     */
    MONO_8(AudioManager.AL.AL_MONO_8, 0x01, 0x08),

    /**
     * Represent 16-Bit mono audio.
     */
    MONO_16(AudioManager.AL.AL_MONO_16, 0x01, 0x10),

    /**
     * Represent 8-Bit stereo audio.
     */
    STEREO_8(AudioManager.AL.AL_STEREO_8, 0x02, 0x08),

    /**
     * Represent 16-Bit stereo audio.
     */
    STEREO_16(AudioManager.AL.AL_STEREO_16, 0x02, 0x10);

    public final int eType;
    public final int eChannel;
    public final int eComponent;

    /**
     * <p>Constructor</p>
     */
    AudioFormat(int type, int channel, int component) {
        eType = type;
        eChannel = channel;
        eComponent = component;
    }
}