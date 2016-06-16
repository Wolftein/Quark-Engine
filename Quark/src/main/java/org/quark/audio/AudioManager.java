/*
 * This file is part of Quark Framework, licensed under the APACHE License.
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

import org.quark.audio.factory.FactoryStaticAudio;
import org.quark.audio.factory.FactoryStreamingAudio;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.Int8Array;

/**
 * <code>AudioManager</code> encapsulate a service for managing {@link Audio}.
 */
public interface AudioManager {
    /**
     * <code>ALES10</code> encapsulate all feature(s) supported by OpenAL 1.0.
     */
    interface ALES10 {
        int AL_BUFFER = 0x1009;
        int AL_BUFFERS_PROCESSED = 0x1016;
        int AL_CONE_INNER_ANGLE = 0x1001;
        int AL_CONE_OUTER_ANGLE = 0x1002;
        int AL_CONE_OUTER_GAIN = 0x1022;
        int AL_DIRECTION = 0x1005;
        int AL_FALSE = 0x0000;
        int AL_GAIN = 0x100A;
        int AL_LOOPING = 0x1007;
        int AL_MAX_DISTANCE = 0x1023;
        int AL_MONO_16 = 0x1101;
        int AL_MONO_8 = 0x1100;
        int AL_NONE = 0x0000;
        int AL_ORIENTATION = 0x100F;
        int AL_PAUSED = 0x1013;
        int AL_PITCH = 0x1003;
        int AL_PLAYING = 0x1012;
        int AL_POSITION = 0x1004;
        int AL_REFERENCE_DISTANCE = 0x1020;
        int AL_SOURCE_RELATIVE = 0x0202;
        int AL_SOURCE_STATE = 0x1010;
        int AL_STEREO_16 = 0x1103;
        int AL_STEREO_8 = 0x1102;
        int AL_STOPPED = 0x1014;
        int AL_TRUE = 0x0001;
        int AL_VELOCITY = 0x1006;

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        boolean alcCreateContext();

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alcDestroyContext();

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        int alGenBuffers();

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        int alGenSources();

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alDeleteBuffers(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alDeleteSources(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourcePause(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourcePlay(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourceStop(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourcei(int name, int type, int value);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourcef(int name, int type, float value);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourcef(int name, int type, float value1, float value2, float value3);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alListenerf(int type, float value);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alListenerf(int type, float value1, float value2, float value3);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alListenerf(int type, Float32Array value);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        int alGetSourcei(int name, int type);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        int alSourceUnqueueBuffers(int name);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alSourceQueueBuffers(int name, int id);

        /**
         * @see <a href="https://www.openal.org/documentation/OpenAL_Programmers_Guide.pdf">Link</a>
         */
        void alBufferData(int name, int format, Int8Array data, int rate);
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
     * <p>Delete the given {@link FactoryStaticAudio}</p>
     *
     * @param audio the audio
     */
    void delete(FactoryStaticAudio audio);

    /**
     * <p>Delete the given {@link FactoryStreamingAudio}</p>
     *
     * @param audio the audio
     */
    void delete(FactoryStreamingAudio audio);
}
