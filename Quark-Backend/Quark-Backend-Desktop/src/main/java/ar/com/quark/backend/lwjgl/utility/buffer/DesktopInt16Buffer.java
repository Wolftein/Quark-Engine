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

import ar.com.quark.utility.buffer.Int16Buffer;

import java.nio.ShortBuffer;

/**
 * Implementation for {@link Int16Buffer}.
 */
public final class DesktopInt16Buffer extends DesktopBuffer<Int16Buffer, ShortBuffer> implements Int16Buffer {
    /**
     * <p>Constructor</p>
     */
    public DesktopInt16Buffer(ShortBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int16Buffer write(int value) {
        mUnderlying.put((short) value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int16Buffer write(short[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int16Buffer write(short[] values, int offset, int count) {
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
    public Int16Buffer copy(Int16Buffer other) {
        mUnderlying.put(other.<ShortBuffer>underlying());
        return this;
    }
}
