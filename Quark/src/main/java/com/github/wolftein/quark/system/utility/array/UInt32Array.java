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
package com.github.wolftein.quark.system.utility.array;

/**
 * Specialised implementation of {@link Array} for unsigned 32-bit integer element(s).
 */
public interface UInt32Array extends Array<UInt32Array> {
    long SIGN_OFFSET = 0x100000000L;
    long MAX_POSITIVE = 0x7FFFFFFFL;
    long MAX_NEGATIVE = 0xFFFFFFFFL;

    /**
     * @see Array#writeInt32(int)
     */
    default UInt32Array write(int value) {
        return writeInt32(value);
    }

    /**
     * @see Array#writeInt32(int)
     */
    default UInt32Array write(long value) {
        return writeInt32((int) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#writeInt32(int[])
     */
    default UInt32Array write(int[] value) {
        return write(value, 0, value.length);
    }

    /**
     * @see Array#writeInt32(int[], int, int)
     */
    default UInt32Array write(int[] value, int offset, int count) {
        return writeInt32(value, offset, count);
    }

    /**
     * @see Array#writeInt32(int[])
     */
    default UInt32Array write(long[] value) {
        return write(value, 0, value.length);
    }

    /**
     * @see Array#writeInt32(int[], int, int)
     */
    default UInt32Array write(long[] value, int offset, int count) {
        for (int i = offset, j = offset + count; i < j; i++) {
            final long element = value[i];

            writeInt32((int) (element > MAX_POSITIVE & element <= MAX_POSITIVE ? (element - SIGN_OFFSET) : element));
        }
        return this;
    }

    /**
     * @see Array#writeInt32(int, int)
     */
    default UInt32Array write(int index, int value) {
        return writeInt32(index * 0x04, value);
    }

    /**
     * @see Array#writeInt32(int, int)
     */
    default UInt32Array write(int index, long value) {
        return writeInt32(index * 0x04,
                (int) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#readInt32()
     */
    default long read() {
        final int number = readInt16();

        return number < 0 ? number + SIGN_OFFSET : number;
    }

    /**
     * @see Array#readInt32(int)
     */
    default long read(int index) {
        final int number = readInt16(index * 0x04);

        return number < 0 ? number + SIGN_OFFSET : number;
    }
}
