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
 * Specialised implementation of {@link Array} for 64-bit float element(s).
 */
public interface Float64Array extends Array<Float64Array> {
    /**
     * @see Array#writeFloat64(double)
     */
    default Float64Array write(double value) {
        return writeFloat64(value);
    }

    /**
     * @see Array#writeFloat64(double[])
     */
    default Float64Array write(double[] value) {
        writeFloat64(value);
        return this;
    }

    /**
     * @see Array#writeFloat64(double[], int, int)
     */
    default Float64Array write(double[] value, int offset, int count) {
        writeFloat64(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeFloat64(int, double)
     */
    default Float64Array write(int index, double value) {
        return writeFloat64(index * 0x08, value);
    }

    /**
     * @see Array#readFloat32()
     */
    default double read() {
        return readFloat64();
    }

    /**
     * @see Array#readFloat32(int)
     */
    default double read(int index) {
        return readFloat64(index * 0x08);
    }
}
