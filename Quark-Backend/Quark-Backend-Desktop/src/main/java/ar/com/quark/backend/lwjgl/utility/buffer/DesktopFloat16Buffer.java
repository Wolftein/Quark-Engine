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

import ar.com.quark.utility.buffer.Float16Buffer;

import java.nio.ShortBuffer;

/**
 * Implementation for {@link Float16Buffer}.
 */
public final class DesktopFloat16Buffer extends DesktopBuffer<Float16Buffer, ShortBuffer> implements Float16Buffer {
    /**
     * <p>Constructor</p>
     */
    public DesktopFloat16Buffer(ShortBuffer underlying) {
        super(underlying);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float16Buffer write(float value) {
        mUnderlying.put((short) convert(value));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float16Buffer write(float[] values) {
        return write(values, 0, values.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float16Buffer write(float[] values, int offset, int count) {
        for (int i = offset, j = offset + count; i < j; i++) {
            write(convert(values[i]));
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float read() {
        return convert(mUnderlying.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Float16Buffer copy(Float16Buffer other) {
        mUnderlying.put(other.<ShortBuffer>underlying());
        return this;
    }

    /**
     * <p>Helper method to convert a 16-bit float into a 16-bit integer</p>
     */
    private static int convert(float value) {
        if (value == Float.POSITIVE_INFINITY) {
            return 0x7c00;
        } else if (value == Float.NEGATIVE_INFINITY) {
            return (short) 0xFC00;
        } else if (value == 0.0f) {
            return (short) 0x0000;
        } else if (value > 65504.0f) {
            return 0x7BFF;
        } else if (value < -65504.0f) {
            return (short) (0x7BFF | 0x8000);
        } else if (value > 0.0f && value < 5.96046E-8f) {
            return 0x0001;
        } else if (value < 0.0f && value > -5.96046E-8f) {
            return (short) 0x8001;
        }

        final int f = Float.floatToIntBits(value);

        final int val0
                = (f >> 16) & 0x8000;
        final int val1
                = (((f & 0x7F800000) - 0x38000000) >> 13) & 0x7C00;
        final int val2
                = (f >> 13) & 0x03FF;
        return val0 | val1 | val2;
    }

    /**
     * <p>Helper method to convert a 16-bit integer into a 16-bit float</p>
     */
    private static float convert(int value) {
        switch (value) {
            case 0x0000:
                return 0.0F;
            case 0x8000:
                return -0.0F;
            case 0x7C00:
                return Float.POSITIVE_INFINITY;
            case 0xFC00:
                return Float.NEGATIVE_INFINITY;
        }

        final int val0
                = (value & 0x8000) << 16;
        final int val1
                = ((value & 0x7C00) + 0x1C000) << 13;
        final int val2
                = (value & 0x03FF) << 13;
        return Float.intBitsToFloat(val0 | val1 | val2);
    }
}
