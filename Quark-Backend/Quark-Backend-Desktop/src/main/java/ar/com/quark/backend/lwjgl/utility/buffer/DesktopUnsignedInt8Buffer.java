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

import ar.com.quark.utility.buffer.UnsignedInt8Buffer;

import java.nio.ByteBuffer;

/**
 * Implementation for {@link UnsignedInt8Buffer}.
 */
public final class DesktopUnsignedInt8Buffer extends DesktopBuffer<UnsignedInt8Buffer, ByteBuffer>
        implements UnsignedInt8Buffer {
    private final static int SIGN_OFFSET = 0x100;
    private final static int MAX_POSITIVE = 0x7F;
    private final static int MAX_NEGATIVE = 0xFF;

    /**
     * <p>Constructor</p>
     */
    public DesktopUnsignedInt8Buffer(ByteBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt8Buffer write(int value) {
        mUnderlying.put((byte) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt8Buffer write(byte[] values) {
        mUnderlying.put(values);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnsignedInt8Buffer write(byte[] values, int offset, int count) {
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
    public UnsignedInt8Buffer copy(UnsignedInt8Buffer other) {
        mUnderlying.put(other.<ByteBuffer>underlying());
        return this;
    }
}
