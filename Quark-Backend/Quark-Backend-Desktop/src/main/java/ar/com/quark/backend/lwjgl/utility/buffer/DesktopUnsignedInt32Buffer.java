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

import ar.com.quark.utility.buffer.UnsignedInt32Buffer;

import java.nio.IntBuffer;

/**
 * Implementation for {@link UnsignedInt32Buffer}.
 */
public final class DesktopUnsignedInt32Buffer extends DesktopBuffer<UnsignedInt32Buffer, IntBuffer>
        implements UnsignedInt32Buffer {
    private final static long SIGN_OFFSET = 0x100000000L;
    private final static long MAX_POSITIVE = 0x7FFFFFFFL;
    private final static long MAX_NEGATIVE = 0xFFFFFFFFL;

    /**
     * <p>Constructor</p>
     */
    public DesktopUnsignedInt32Buffer(IntBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt32Buffer write(long value) {
        mUnderlying.put((int) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt32Buffer write(int[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt32Buffer write(int[] values, int offset, int count) {
        mUnderlying.put(values, offset, count);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long read() {
        final int number = mUnderlying.get();

        return number < 0 ? number + SIGN_OFFSET : number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt32Buffer copy(UnsignedInt32Buffer other) {
        mUnderlying.put(other.<IntBuffer>underlying());
        return this;
    }
}
