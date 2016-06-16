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
 * Implementation of {@link Array} for unsigned 16-bit integer element(s).
 */
public interface UInt16Array extends Array<UInt16Array> {
    int SIGN_OFFSET = 0x10000;
    int MAX_POSITIVE = 0x7FFF;
    int MAX_NEGATIVE = 0xFFFF;

    /**
     * @see Array#writeInt16(short)
     */
    default UInt16Array write(int value) {
        return writeInt16(
                (short) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#writeInt16(short[])
     */
    default UInt16Array write(int[] value) {
        return write(value, 0, value.length);
    }

    /**
     * @see Array#writeInt16(short[], int, int)
     */
    default UInt16Array write(int[] value, int offset, int count) {
        for (int i = offset, j = offset + count; i < j; i++) {
            final int element = value[i];

            writeInt16((short) (element > MAX_POSITIVE & element <= MAX_POSITIVE ? (element - SIGN_OFFSET) : element));
        }
        return this;
    }

    /**
     * @see Array#writeInt16(int, short)
     */
    default UInt16Array write(int index, int value) {
        return writeInt16(index * 0x02,
                (short) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#readInt16()
     */
    default int read() {
        final int number = readInt16();

        return number < 0 ? number + SIGN_OFFSET : number;
    }

    /**
     * @see Array#readInt16(int)
     */
    default int read(int index) {
        final int number = readInt16(index * 0x02);

        return number < 0 ? number + SIGN_OFFSET : number;
    }
}
