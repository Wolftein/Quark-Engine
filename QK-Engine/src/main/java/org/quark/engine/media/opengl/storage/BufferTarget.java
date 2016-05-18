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
 * <code>BufferTarget</code> enumerates {@link Buffer} target(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum BufferTarget {
    /**
     * Represent a buffer for vertices.
     * <p>
     * {@since OpenGL 1.5}
     */
    BUFFER_ARRAY(0x8892),

    /**
     * Represent a buffer for indices..
     * <p>
     * {@since OpenGL 1.5}
     */
    BUFFER_ELEMENT(0x8893),

    /**
     * Represent a buffer for pixel data being sent to the GPU.
     * <p>
     * {@since OpenGL 2.1}
     */
    BUFFER_PIXEL_PACK(0x88EB),

    /**
     * Represent a buffer for pixel data being read to the GPU.
     * <p>
     * {@since OpenGL 2.1}
     */
    BUFFER_PIXEL_UNPACK(0x88EC),

    /**
     * Represent a buffer for results from executing a transform feedback shader.
     * <p>
     * {@since OpenGL 3.0}
     */
    BUFFER_TRANSFORM_FEEDBACK(0x8C8E),

    /**
     * Represent a buffer for data copied between buffers.
     * <p>
     * {@since OpenGL 3.1}
     */
    BUFFER_COPY_READ(0x8F36),

    /**
     * Represent a buffer for data copied between buffers.
     * <p>
     * {@since OpenGL 3.1}
     */
    BUFFER_COPY_WRITE(0x8F37),

    /**
     * Represent a buffer for texture(s).
     * <p>
     * {@since OpenGL 3.1}
     */
    BUFFER_TEXTURE(0x8C2A),

    /**
     * Represent a buffer for uniform(s).
     * <p>
     * {@since OpenGL 3.1}
     * {@since GL_ARB_uniform_buffer_object}
     */
    BUFFER_UNIFORM(0x8A11);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    BufferTarget(int value) {
        eValue = value;
    }
}
