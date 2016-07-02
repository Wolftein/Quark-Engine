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
 * <code>StorageTarget</code> enumerate {@link Storage} target(s).
 */
public enum StorageTarget {
    /**
     * Represent a storage for vertices.
     */
    ARRAY(Render.GLES2.GL_ARRAY_BUFFER),

    /**
     * Represent a storage for indices.
     */
    ELEMENT(Render.GLES2.GL_ELEMENT_ARRAY_BUFFER),

    /**
     * Represent a storage for pixel data being sent to the GPU.
     */
    PIXEL_PACK(Render.GLES3.GL_PIXEL_PACK_BUFFER),

    /**
     * Represent a storage for pixel data being read to the GPU.
     */
    PIXEL_UNPACK(Render.GLES3.GL_PIXEL_UNPACK_BUFFER),

    /**
     * Represent a storage for results from executing a transform feedback shader.
     */
    TRANSFORM_FEEDBACK(Render.GLES3.GL_TRANSFORM_FEEDBACK_BUFFER),

    /**
     * Represent a storage for data copied between storage.
     */
    COPY_READ(Render.GLES3.GL_COPY_READ_BUFFER),

    /**
     * Represent a storage for data copied between storage.
     */
    COPY_WRITE(Render.GLES3.GL_COPY_WRITE_BUFFER),

    /**
     * Represent a storage for data(s).
     */
    UNIFORM(Render.GLES3.GL_UNIFORM_BUFFER),

    /**
     * Represent a storage for texture(s).
     */
    TEXTURE(Render.GLES32.GL_TEXTURE_BUFFER);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    StorageTarget(int value) {
        eValue = value;
    }
}
