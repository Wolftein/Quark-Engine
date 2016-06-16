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
 * Implementation of {@link Array} for 8-bit integer element(s).
 */
public interface Int64Array extends Array<Int64Array> {
    /**
     * @see Array#writeInt64(long)
     */
    default Int64Array write(long value) {
        return writeInt64(value);
    }

    /**
     * @see Array#writeInt64(long[])
     */
    default Int64Array write(long[] value) {
        writeInt64(value);
        return this;
    }

    /**
     * @see Array#writeInt64(long[], int, int)
     */
    default Int64Array write(long[] value, int offset, int count) {
        writeInt64(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeInt64(int, long)
     */
    default Int64Array write(int index, long value) {
        return writeInt64(index * 0x08, value);
    }

    /**
     * @see Array#readInt64()
     */
    default long read() {
        return readInt64();
    }

    /**
     * @see Array#readInt64(int)
     */
    default long read(int index) {
        return readInt64(index * 0x08);
    }
}
