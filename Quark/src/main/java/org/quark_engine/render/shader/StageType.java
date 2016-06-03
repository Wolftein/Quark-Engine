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
package org.quark_engine.render.shader;

/**
 * <code>StageType</code> enumerate {@link Stage} type(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum StageType {
    /**
     * A shader that is intended to run on the programmable fragment processor.
     * <p>
     * {@since OpenGL 2.0}
     */
    FRAGMENT(0x8B30),

    /**
     * A shader that is intended to run on the programmable vertex processor.
     * <p>
     * {@since OpenGL 2.0}
     */
    VERTEX(0x8B31),

    /**
     * A shader that is intended to run on the programmable geometry processor.
     * <p>
     * {@since OpenGL 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    GEOMETRY(0x8DD9);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    StageType(int value) {
        eValue = value;
    }
}
