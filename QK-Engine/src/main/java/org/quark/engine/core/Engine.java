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
package org.quark.engine.core;

import org.quark.engine.media.Display;
import org.quark.engine.media.input.InputManager;
import org.quark.engine.media.openal.AL;
import org.quark.engine.media.opengl.GL;
import org.quark.engine.resource.AssetManager;

/**
 * <code>Engine</code> encapsulate the engine of the game.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface Engine {
    /**
     * <p>Get the name of the engine</p>
     *
     * @return the name of the engine
     */
    String getName();

    /**
     * <p>Get the version of the engine</p>
     *
     * @return the version of the engine
     */
    String getVersion();

    /**
     * <p>Get the {@link AssetManager} attached to the application</p>
     *
     * @return the asset manager attached to the application
     */
    AssetManager getAssetManager();

    /**
     * <p>Get the {@link InputManager} attached to the application</p>
     *
     * @return the input manager attached to the application
     */
    InputManager getInputManger();

    /**
     * <p>Get the {@link Display} attached to the application</p>
     *
     * @return the display attached to the application
     */
    Display getDisplay();

    /**
     * <p>Get the {@link GL} attached to the application</p>
     *
     * @return the <b>OpenGL</b> attached to the application
     */
    GL getGL();

    /**
     * <p>Get the {@link AL} attached to the application</p>
     *
     * @return the <b>OpenAL</b> attached to the application
     */
    AL getAL();

    /**
     * <p>Get the life-cycle of the engine</p>
     *
     * @return the life-cycle of the engine
     */
    EngineLifecycle getLifecycle();
}