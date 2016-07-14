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
package ar.com.quark.backend.lwjgl.utility.buffer;

import ar.com.quark.utility.buffer.Int32Buffer;

import java.nio.IntBuffer;

/**
 * Implementation for {@link Int32Buffer}.
 */
public final class DesktopInt32Buffer extends DesktopBuffer<Int32Buffer, IntBuffer> implements Int32Buffer {
    /**
     * <p>Constructor</p>
     */
    public DesktopInt32Buffer(IntBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int32Buffer write(int value) {
        mUnderlying.put(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int32Buffer write(int[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int32Buffer write(int[] values, int offset, int count) {
        mUnderlying.put(values, offset, count);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() {
        return mUnderlying.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int32Buffer copy(Int32Buffer other) {
        mUnderlying.put(other.<IntBuffer>underlying());
        return this;
    }
}
