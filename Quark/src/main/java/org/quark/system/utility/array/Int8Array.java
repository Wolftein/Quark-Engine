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
 * Specialised implementation of {@link Array} for 8-bit integer element(s).
 */
public interface Int8Array extends Array<Int8Array> {
    /**
     * @see Array#writeInt8(byte)
     */
    default Int8Array write(byte value) {
        return writeInt8(value);
    }

    /**
     * @see Array#writeInt8(byte[])
     */
    default Int8Array write(byte[] value) {
        writeInt8(value);
        return this;
    }

    /**
     * @see Array#writeInt8(byte[], int, int)
     */
    default Int8Array write(byte[] value, int offset, int count) {
        writeInt8(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeInt8(int, byte)
     */
    default Int8Array write(int index, byte value) {
        return writeInt8(index, value);
    }

    /**
     * @see Array#readInt8()
     */
    default byte read() {
        return readInt8();
    }

    /**
     * @see Array#readInt8(int)
     */
    default byte read(int index) {
        return readInt8(index);
    }
}
