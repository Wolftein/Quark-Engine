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
 * Specialised implementation of {@link Array} for 32-bit integer element(s).
 */
public interface Int32Array extends Array<Int32Array> {
    /**
     * @see Array#writeInt32(int)
     */
    default Int32Array write(int value) {
        return writeInt32(value);
    }

    /**
     * @see Array#writeInt32(int[])
     */
    default Int32Array write(int[] value) {
        writeInt32(value);
        return this;
    }

    /**
     * @see Array#writeInt32(int[], int, int)
     */
    default Int32Array write(int[] value, int offset, int count) {
        writeInt32(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeInt32(int, int)
     */
    default Int32Array write(int index, int value) {
        return writeInt32(index * 0x04, value);
    }

    /**
     * @see Array#writeInt32(int[])
     */
    default Int32Array write(Int32Array array) {
        for (int i = 0, j = array.limit(); i < j; i += 0x04) {
            write(array.read());
        }
        return this;
    }

    /**
     * @see Array#readInt32()
     */
    default int read() {
        return readInt32();
    }

    /**
     * @see Array#readInt32(int)
     */
    default int read(int index) {
        return readInt32(index * 0x04);
    }
}
