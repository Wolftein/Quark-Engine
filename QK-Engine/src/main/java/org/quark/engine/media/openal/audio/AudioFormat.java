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

/**
 * <code>AudioFormat</code> enumerates the format of an {@link Audio}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum AudioFormat {
    /**
     * Represent 8-Bit mono audio.
     * <p>
     * {@since OpenAL 1.0}
     */
    MONO_8(0x1100, 8, 1),

    /**
     * Represent 16-Bit mono audio.
     * <p>
     * {@since OpenAL 1.0}
     */
    MONO_16(0x1101, 16, 1),

    /**
     * Represent 8-Bit stereo audio.
     * <p>
     * {@since OpenAL 1.0}
     */
    STEREO_8(0x1102, 8, 2),

    /**
     * Represent 16-Bit stereo audio.
     * <p>
     * {@since OpenAL 1.0}
     */
    STEREO_16(0x1103, 16, 2);

    public final int eValue;
    public final int eComponent;
    public final int eChannel;

    /**
     * <p>Constructor</p>
     */
    AudioFormat(int value, int component, int channel) {
        eValue = value;
        eComponent = component;
        eChannel = channel;
    }
}
