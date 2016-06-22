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
 * Specialised implementation of {@link Array} for 32-bit float element(s).
 */
public interface Float32Array extends Array<Float32Array> {
    /**
     * @see Array#writeFloat32(float)
     */
    default Float32Array write(float value) {
        return writeFloat32(value);
    }

    /**
     * @see Array#writeFloat32(float[])
     */
    default Float32Array write(float[] value) {
        writeFloat32(value);
        return this;
    }

    /**
     * @see Array#writeFloat32(float[], int, int)
     */
    default Float32Array write(float[] value, int offset, int count) {
        writeFloat32(value, offset, count);
        return this;
    }

    /**
     * @see Array#writeFloat32(int, float)
     */
    default Float32Array write(int index, float value) {
        return writeFloat32(index * 0x04, value);
    }

    /**
     * @see Array#readFloat32()
     */
    default float read() {
        return readFloat32();
    }

    /**
     * @see Array#readFloat32(int)
     */
    default float read(int index) {
        return readFloat32(index * 0x04);
    }
}
