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
package org.quark.system.utility.array;

/**
 * Implementation of {@link Array} for 16-bit float element(s).
 */
public interface Float16Array extends Array<Float16Array> {
    /**
     * @see Array#writeInt16(short)
     */
    default Float16Array write(float value) {
        return writeInt16(toHalf(value));
    }

    /**
     * @see Array#writeInt16(short[])
     */
    default Float16Array write(float[] value) {
        return write(value, 0, value.length);
    }

    /**
     * @see Array#writeInt16(short[], int, int)
     */
    default Float16Array write(float[] value, int offset, int count) {
        for (int i = offset, j = offset + count; i < j; i++) {
            writeInt16(toHalf(value[i]));
        }
        return this;
    }

    /**
     * @see Array#writeInt16(int, short)
     */
    default Float16Array write(int index, float value) {
        return writeInt16(index * 0x02, toHalf(value));
    }

    /**
     * @see Array#readInt16()
     */
    default float read() {
        return toFloat(readInt16());
    }

    /**
     * @see Array#readInt16(int)
     */
    default float read(int index) {
        return toFloat(readInt16(index * 0x02));
    }

    /**
     * Transform 32-bit float to 16-bite float.
     */
    static short toHalf(float value) {
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

        return (short) (((f >> 16) & 0x8000)
                | ((((f & 0x7f800000) - 0x38000000) >> 13) & 0x7c00) | ((f >> 13) & 0x03ff));
    }

    /**
     * Transform 16-bit float to 32-bite float.
     */
    static float toFloat(int value) {
        switch (value) {
            case 0x0000:
                return 0.0f;
            case 0x8000:
                return -0.0f;
            case 0x7C00:
                return Float.POSITIVE_INFINITY;
            case 0xFC00:
                return Float.NEGATIVE_INFINITY;
            default:
                return Float.intBitsToFloat(
                        ((value & 0x8000) << 16) | (((value & 0x7c00) + 0x1C000) << 13) | ((value & 0x03FF) << 13));
        }
    }
}
