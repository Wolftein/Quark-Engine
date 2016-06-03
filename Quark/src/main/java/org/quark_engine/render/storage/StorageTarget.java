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
package org.quark_engine.render.storage;

/**
 * <code>StorageTarget</code> enumerate {@link Storage} target(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum StorageTarget {
    /**
     * Represent a storage for vertices.
     * <p>
     * {@since OpenGL 1.5}
     */
    ARRAY(0x8892),

    /**
     * Represent a storage for indices..
     * <p>
     * {@since OpenGL 1.5}
     */
    ELEMENT(0x8893),

    /**
     * Represent a storage for pixel data being sent to the GPU.
     * <p>
     * {@since OpenGL 2.1}
     */
    PIXEL_PACK(0x88EB),

    /**
     * Represent a storage for pixel data being read to the GPU.
     * <p>
     * {@since OpenGL 2.1}
     */
    PIXEL_UNPACK(0x88EC),

    /**
     * Represent a storage for results from executing a transform feedback shader.
     * <p>
     * {@since OpenGL 3.0}
     */
    TRANSFORM_FEEDBACK(0x8C8E),

    /**
     * Represent a storage for data copied between storage.
     * <p>
     * {@since OpenGL 3.1}
     */
    COPY_READ(0x8F36),

    /**
     * Represent a storage for data copied between storage.
     * <p>
     * {@since OpenGL 3.1}
     */
    COPY_WRITE(0x8F37),

    /**
     * Represent a storage for texture(s).
     * <p>
     * {@since OpenGL 3.1}
     */
    TEXTURE(0x8C2A),

    /**
     * Represent a storage for data(s).
     * <p>
     * {@since OpenGL 3.1}
     */
    UNIFORM(0x8A11);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    StorageTarget(int value) {
        eValue = value;
    }
}
