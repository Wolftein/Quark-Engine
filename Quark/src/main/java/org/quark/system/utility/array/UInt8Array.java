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
 * Implementation of {@link Array} for unsigned 8-bit integer element(s).
 */
public interface UInt8Array extends Array<UInt8Array> {
    int SIGN_OFFSET = 0x10000;
    int MAX_POSITIVE = 0x7FFF;
    int MAX_NEGATIVE = 0xFFFF;

    /**
     * @see Array#writeInt8(byte)
     */
    default UInt8Array write(int value) {
        return writeInt8(
                (byte) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#writeInt8(byte[])
     */
    default UInt8Array write(int[] value) {
        return write(value, 0, value.length);
    }

    /**
     * @see Array#writeInt8(byte[], int, int)
     */
    default UInt8Array write(int[] value, int offset, int count) {
        for (int i = offset, j = offset + count; i < j; i++) {
            final int element = value[i];

            writeInt8((byte) (element > MAX_POSITIVE & element <= MAX_POSITIVE ? (element - SIGN_OFFSET) : element));
        }
        return this;
    }

    /**
     * @see Array#writeInt8(int, byte)
     */
    default UInt8Array write(int index, int value) {
        return writeInt8(index,
                (byte) (value > MAX_POSITIVE & value <= MAX_POSITIVE ? (value - SIGN_OFFSET) : value));
    }

    /**
     * @see Array#readInt8()
     */
    default int read() {
        final int number = readInt8();

        return number < 0 ? number + SIGN_OFFSET : number;
    }

    /**
     * @see Array#readInt8(int)
     */
    default int read(int index) {
        final int number = readInt8(index);

        return number < 0 ? number + SIGN_OFFSET : number;
    }
}
