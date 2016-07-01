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
 * Specialised implementation of {@link Array} for 16-bit integer element(s).
 */
public interface Int16Array extends Array<Int16Array> {
    /**
     * @see Array#writeInt16(int)
     */
    default Int16Array write(short value) {
        return writeInt16(value);
    }

    /**
     * @see Array#writeInt16(short[])
     */
    default Int16Array write(short[] value) {
        writeInt16(value);
        return this;
    }

    /**
     * @see Array#writeInt16(short[], int, int)
     */
    default Int16Array write(short[] value, int offset, int count) {
        writeInt16(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeInt16(int, int)
     */
    default Int16Array write(int index, short value) {
        return writeInt16(index * 0x02, value);
    }

    /**
     * @see Array#readInt16()
     */
    default short read() {
        return readInt16();
    }

    /**
     * @see Array#readInt16(int)
     */
    default short read(int index) {
        return readInt16(index * 0x02);
    }
}
