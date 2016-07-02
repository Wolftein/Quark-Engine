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
package ar.com.quark.render.storage;

import ar.com.quark.render.Render;

/**
 * <code>VertexFormat</code> enumerate {@link Vertex} type(s).
 */
public enum VertexFormat {
    /**
     * Represent a signed 8-bit integer.
     */
    BYTE(Render.GLES2.GL_BYTE, 0x01),

    /**
     * Represent an unsigned 8-bit integer.
     */
    UNSIGNED_BYTE(Render.GLES2.GL_UNSIGNED_BYTE, 0x01),

    /**
     * Represent a signed 16-bit integer.
     */
    SHORT(Render.GLES2.GL_SHORT, 0x02),

    /**
     * Represent an unsigned 16-bit integer.
     */
    UNSIGNED_SHORT(Render.GLES2.GL_UNSIGNED_SHORT, 0x02),

    /**
     * Represent a signed 32-bit integer.
     */
    INT(Render.GLES3.GL_INT, 0x04),

    /**
     * Represent an unsigned 32-bit integer.
     */
    UNSIGNED_INT(Render.GLES3.GL_UNSIGNED_INT, 0x04),

    /**
     * Represent a IEEE-754 single-precision floating point number.
     */
    FLOAT(Render.GLES2.GL_FLOAT, 0x04),

    /**
     * Represent a IEEE-754 half-precision floating point number.
     */
    HALF_FLOAT(Render.GLES3.GL_HALF_FLOAT, 0x02);

    public final int eValue;
    public final int eLength;

    /**
     * <p>Constructor</p>
     */
    VertexFormat(int value, int length) {
        this.eValue = value;
        this.eLength = length;
    }
}

