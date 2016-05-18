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
package org.quark.engine.media.opengl.storage;

/**
 * <code>VertexType</code> enumerates {@link Vertex} type.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum VertexType {
    /**
     * <b>OpenGL</b> implementation of signed {@link Byte}.
     * <p>
     * {@since OpenGL 1.1}
     */
    BYTE(0x1400, 0x01),

    /**
     * <b>OpenGL</b> implementation of unsigned {@link Byte}.
     * <p>
     * {@since OpenGL 1.1}
     */
    UNSIGNED_BYTE(0x1401, 0x01),

    /**
     * <b>OpenGL</b> implementation of signed {@link Short}.
     * <p>
     * {@since OpenGL 1.1}
     */
    SHORT(0x1402, 0x02),

    /**
     * <b>OpenGL</b> implementation of unsigned {@link Short}.
     * <p>
     * {@since OpenGL 1.1}
     */
    UNSIGNED_SHORT(0x1403, 0x02),

    /**
     * <b>OpenGL</b> implementation of signed {@link Integer}.
     * <p>
     * {@since OpenGL 1.1}
     */
    INT(0x1404, 0x04),

    /**
     * <b>OpenGL</b> implementation of unsigned {@link Integer}.
     * <p>
     * {@since OpenGL 1.1}
     */
    UNSIGNED_INT(0x1405, 0x04),

    /**
     * <b>OpenGL</b> implementation of {@link Float}.
     * <p>
     * {@since OpenGL 1.1}
     */
    FLOAT(0x1406, 0x04),

    /**
     * <b>OpenGL</b> implementation of half {@link Float}.
     * <p>
     * {@since OpenGL 3.0}
     */
    HALF_FLOAT(0x140B, 0x02),

    /**
     * <b>OpenGL</b> implementation of {@link Double}.
     * <p>
     * {@since OpenGL 1.1}
     */
    DOUBLE(0x140A, 0x08);

    public final int eValue;
    public final int eLength;

    /**
     * <p>Constructor</p>
     */
    VertexType(int value, int length) {
        this.eValue = value;
        this.eLength = length;
    }
}

