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
package org.quark.render.shader;

import org.quark.render.Render;

/**
 * <code>StageType</code> enumerate {@link Stage} type(s).
 */
public enum StageType {
    /**
     * A shader that is intended to run on the programmable fragment processor.
     *
     * @since OpenGL    2.0
     * @since OpenGL ES 2.0
     */
    FRAGMENT(Render.GLES2.GL_FRAGMENT_SHADER),

    /**
     * A shader that is intended to run on the programmable vertex processor.
     *
     * @since OpenGL    2.0
     * @since OpenGL ES 2.0
     */
    VERTEX(Render.GLES2.GL_VERTEX_SHADER),

    /**
     * A shader that is intended to run on the programmable geometry processor.
     *
     * @since OpenGL    3.2
     * @since OpenGL ES 3.2
     * @since GL_EXT_geometry_shader4
     */
    GEOMETRY(Render.GLES32.GL_GEOMETRY_SHADER);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    StageType(int value) {
        eValue = value;
    }
}
