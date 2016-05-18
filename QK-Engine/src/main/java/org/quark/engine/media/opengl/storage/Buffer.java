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

import org.quark.engine.media.opengl.GLComponent;

/**
 * <code>Buffer</code> represent a buffer that store an array of un-formatted memory allocated by the GPU.
 * <p>
 * These can be used to store vertex data, pixel data retrieved from images or the frame-buffer.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public class Buffer extends GLComponent {
    /**
     * Indicates that the server should not attempt to synchronize pending operations.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_UNSYNCHRONIZED = 0x02;

    /**
     * Indicates that the previous contents of the specified range may be discarded.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_INVALIDATE = 0x04;

    /**
     * Indicates that the previous contents of the entire buffer may be discarded.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_INVALIDATE_ALL = 0x08;

    private final BufferTarget mTarget;
    private final BufferMode mMode;
    private final long mCapacity;

    /**
     * <p>Constructor</p>
     */
    public Buffer(BufferTarget target, BufferMode mode, long capacity) {
        mTarget = target;
        mMode = mode;
        mCapacity = capacity;
    }

    /**
     * <p>Get the target of the buffer</p>
     *
     * @return the target of the buffer
     */
    public final BufferTarget getTarget() {
        return mTarget;
    }

    /**
     * <p>Get the type of the buffer</p>
     *
     * @return the type of the buffer
     */
    public final BufferMode getMode() {
        return mMode;
    }

    /**
     * <p>Get the capacity of the buffer (in bytes)</p>
     *
     * @return the capacity of the buffer (in bytes)
     */
    public final long getCapacity() {
        return mCapacity;
    }
}
