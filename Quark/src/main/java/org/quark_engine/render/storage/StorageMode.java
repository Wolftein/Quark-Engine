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
 * <code>StorageMode</code> enumerate {@link Storage} mode(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum StorageMode {
    /**
     * The data store contents are modified repeatedly.
     * <p>
     * {@since OpenGL 1.5}
     */
    DYNAMIC_DRAW(0x88E8, false, true),

    /**
     * The data store contents are queried repeatedly.
     * <p>
     * {@since OpenGL 1.5}
     */
    DYNAMIC_READ(0x88E9, true, false),

    /**
     * The data store contents are copy between storage repeatedly.
     * <p>
     * {@since OpenGL 1.5}
     */
    DYNAMIC_COPY(0x88EA, false, false),

    /**
     * The data store contents is modified once.
     * <p>
     * {@since OpenGL 1.5}
     */
    STATIC_DRAW(0x88E4, false, true),

    /**
     * The data store contents is queried once.
     * <p>
     * {@since OpenGL 1.5}
     */
    STATIC_READ(0x88E5, true, false),

    /**
     * The data store contents are copy between storage once.
     * <p>
     * {@since OpenGL 1.5}
     */
    STATIC_COPY(0x88E6, false, false),

    /**
     * The data store contents are modified frequently.
     * <p>
     * {@since OpenGL 1.5}
     */
    STREAM_DRAW(0x88E0, false, true),

    /**
     * The data store contents are queried frequently.
     * <p>
     * {@since OpenGL 1.5}
     */
    STREAM_READ(0x88E1, true, false),

    /**
     * The data store contents are copy between storage frequently.
     * <p>
     * {@since OpenGL 1.5}
     */
    STREAM_COPY(0x88E2, false, false);

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