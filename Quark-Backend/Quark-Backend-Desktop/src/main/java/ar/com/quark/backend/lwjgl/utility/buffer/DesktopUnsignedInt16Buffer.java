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

import ar.com.quark.utility.buffer.UnsignedInt16Buffer;

import java.nio.ShortBuffer;

/**
 * Implementation for {@link UnsignedInt16Buffer}.
 */
public final class DesktopUnsignedInt16Buffer extends DesktopBuffer<UnsignedInt16Buffer, ShortBuffer>
        implements UnsignedInt16Buffer {
    private final static int SIGN_OFFSET = 0x10000;
    private final static int MAX_POSITIVE = 0x7FFF;
    private final static int MAX_NEGATIVE = 0xFFFF;

    /**
     * <p>Constructor</p>
     */
    public DesktopUnsignedInt16Buffer(ShortBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt16Buffer write(int value) {
        mUnderlying.put((short) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt16Buffer write(short[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt16Buffer write(short[] values, int offset, int count) {
        mUnderlying.put(values, offset, count);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() {
        final int number = mUnderlying.get();

        return number < 0 ? number + SIGN_OFFSET : number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt16Buffer copy(UnsignedInt16Buffer other) {
        mUnderlying.put(other.<ShortBuffer>underlying());
        return this;
    }
}
