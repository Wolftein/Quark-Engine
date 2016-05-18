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
import org.quark.engine.media.openal.AL;
import org.quark.engine.media.opengl.GL;
import org.quark.engine.resource.AssetManager;

/**
 * <code>EngineAPI</code> represent a singleton for {@link Engine}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class EngineAPI {
    private static Engine sEngine = null;

    /**
     * <p>Set the <code>Engine</code> instance of the engine</p>
     *
     * @param Engine the new Engine instance
     */
    public static void setEngine(Engine Engine) {
        if (sEngine != null) {
            throw new IllegalStateException("Cannot set the engine instance twice");
        }
        sEngine = Engine;
    }

    /**
     * <p>Get the <code>Engine</code> instance of the engine</p>
     *
     * @return the instance of the engine
     */
    public static Engine getEngine() {
        return sEngine;
    }

    /**
     * @see Engine#getAssetManager()
     */
    public static AssetManager getAssetManager() {
        return sEngine.getAssetManager();
    }

    /**
     * @see Engine#getDisplay()
     */
    public static Display getDisplay() {
        return sEngine.getDisplay();
    }

    /**
     * @see Engine#getGL()
     */
    public static GL getGL() {
        return sEngine.getGL();
    }

    /**
     * @see Engine#getAL()
     */
    public static AL getAL() {
        return sEngine.getAL();
    }
}
