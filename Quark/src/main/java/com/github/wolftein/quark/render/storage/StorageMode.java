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
package com.github.wolftein.quark.render.storage;

import com.github.wolftein.quark.render.Render;

/**
 * <code>StorageMode</code> enumerate {@link Storage} mode(s).
 */
public enum StorageMode {
    /**
     * The data store contents are modified repeatedly.
     */
    DYNAMIC_DRAW(Render.GLES2.GL_DYNAMIC_DRAW, false, true),

    /**
     * The data store contents are queried repeatedly.
     */
    DYNAMIC_READ(Render.GLES3.GL_DYNAMIC_READ, true, false),

    /**
     * The data store contents are copy between storage repeatedly.
     */
    DYNAMIC_COPY(Render.GLES3.GL_DYNAMIC_COPY, false, false),

    /**
     * The data store contents is modified once.
     */
    STATIC_DRAW(Render.GLES2.GL_STATIC_DRAW, false, true),

    /**
     * The data store contents is queried once.
     */
    STATIC_READ(Render.GLES3.GL_STATIC_READ, true, false),

    /**
     * The data store contents are copy between storage once.
     */
    STATIC_COPY(Render.GLES3.GL_STATIC_COPY, false, false),

    /**
     * The data store contents are modified frequently.
     */
    STREAM_DRAW(Render.GLES2.GL_STREAM_DRAW, false, true),

    /**
     * The data store contents are queried frequently.
     */
    STREAM_READ(Render.GLES3.GL_STREAM_READ, true, false),

    /**
     * The data store contents are copy between storage frequently.
     */
    STREAM_COPY(Render.GLES3.GL_STREAM_COPY, false, false);

    public final int eValue;
    public final boolean eReadable;
    public final boolean eWritable;

    /**
     * <p>Constructor</p>
     */
    StorageMode(int value, boolean readable, boolean writable) {
        eValue = value;
        eReadable = readable;
        eWritable = writable;
    }
}