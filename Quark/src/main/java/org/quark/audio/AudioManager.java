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

import org.quark.audio.factory.FactoryAudioStatic;
import org.quark.audio.factory.FactoryAudioStreaming;

/**
 * <code>AudioManager</code> encapsulate a service for managing {@link Audio}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface AudioManager {
    /**
     * <code>AL</code> encapsulate all feature(s) supported by OpenAL 1.0.
     */
    interface AL {
        int AL_MONO_8 = 0x1100;
        int AL_MONO_16 = 0x1101;
        int AL_STEREO_8 = 0x1102;
        int AL_STEREO_16 = 0x1103;
    }

    /**
     * <p>Play the given {@link AudioSource}</p>
     *
     * @param source the source
     */
    void play(AudioSource source);

    /**
     * <p>Pause the given {@link AudioSource}</p>
     *
     * @param source the source
     */
    void pause(AudioSource source);

    /**
     * <p>Pause all {@link AudioSource}</p>
     */
    void pause();

    /**
     * <p>Resume the given {@link AudioSource}</p>
     *
     * @param source the source
     */
    void resume(AudioSource source);

    /**
     * <p>Resume all {@link AudioSource}</p>
     */
    void resume();

    /**
     * <p>Stop the given {@link AudioSource}</p>
     *
     * @param source the source
     */
    void stop(AudioSource source);

    /**
     * <p>Stop all {@link AudioSource}</p>
     */
    void stop();

    /**
     * <p>Update the listener of the manager</p>
     *
     * @param listener the listener
     */
    void update(AudioListener listener);

    /**
     * <p>Delete the given {@link FactoryAudioStatic}</p>
     *
     * @param audio the audio
     */
    void delete(FactoryAudioStatic audio);

    /**
     * <p>Delete the given {@link FactoryAudioStreaming}</p>
     *
     * @param audio the audio
     */
    void delete(FactoryAudioStreaming audio);
}
