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
 * <code>AudioFormat</code> enumerates the format of an {@link Audio}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum AudioFormat {
    /**
     * Represent 8-Bit mono audio.
     */
    MONO_8(8),

    /**
     * Represent 16-Bit mono audio.
     */
    MONO_16(16),

    /**
     * Represent 8-Bit stereo audio.
     */
    STEREO_8(8),

    /**
     * Represent 16-Bit stereo audio.
     */
    STEREO_16(16);

    public final int eComponent;

    /**
     * <p>Constructor</p>
     */
    AudioFormat(int component) {
        eComponent = component;
    }
}
