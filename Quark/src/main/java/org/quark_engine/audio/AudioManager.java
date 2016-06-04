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

import org.quark_engine.system.utility.ManageableManager;

import java.util.List;

/**
 * <code>AudioManager</code> encapsulate a service for managing {@link Audio}(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface AudioManager extends ManageableManager {
    /**
     * <p>Change the default device</p>
     *
     * @param name     the device
     * @param playback <code>true</code> if should change playback device, <code>false</code> if record device
     */
    void setDevice(String name, boolean playback);

    /**
     * <p>Get default device</p>
     *
     * @param playback <code>true</code> if should return playback device, <code>false</code> if record device
     *
     * @return the device queried
     */
    String getDevice(boolean playback);

    /**
     * <p>Get all available device(s)</p>
     *
     * @param playback <code>true</code> if should return playback device, <code>false</code> if record device
     *
     * @return a collection that contain(s) all device(s) queried
     */
    List<String> getDevices(boolean playback);

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
}
