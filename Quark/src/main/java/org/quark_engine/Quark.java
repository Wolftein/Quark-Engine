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
package org.quark_engine;

import org.quark_engine.input.InputManager;
import org.quark_engine.render.Render;
import org.quark_engine.resource.AssetManager;
import org.quark_engine.system.Framework;

/**
 * <code>Quark</code> encapsulate a singleton for the entire framework.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Quark {
    /**
     * Hold the {@link Framework} implementation of the framework.
     */
    public static Framework Qk = null;

    /**
     * Hold the {@link Render} implementation of the framework.
     */
    public static Render QkRender = null;

    /**
     * Hold the {@link AssetManager} implementation of the framework.
     */
    public static AssetManager QkAssetManager = null;

    /**
     * Hold the {@link InputManager} implementation of the framework.
     */
    public static InputManager QkInputManager = null;
}
