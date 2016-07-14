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

import ar.com.quark.utility.buffer.Int8Buffer;

import java.nio.ByteBuffer;

/**
 * Implementation for {@link Int8Buffer}.
 */
public final class DesktopInt8Buffer extends DesktopBuffer<Int8Buffer, ByteBuffer> implements Int8Buffer {
    /**
     * <p>Constructor</p>
     */
    public DesktopInt8Buffer(ByteBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Buffer write(int value) {
        mUnderlying.put((byte) value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Buffer write(byte[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Buffer write(byte[] values, int offset, int count) {
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
    public int read(byte[] values) {
        final int position = mUnderlying.position();

        mUnderlying.get(values);

        return mUnderlying.position() - position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] values, int offset, int count) {
        final int position = mUnderlying.position();

        mUnderlying.get(values, offset, count);

        return mUnderlying.position() - position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Buffer copy(Int8Buffer other) {
        mUnderlying.put(other.<ByteBuffer>underlying());
        return this;
    }
}
