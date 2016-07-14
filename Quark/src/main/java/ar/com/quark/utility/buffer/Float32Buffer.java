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
package ar.com.quark.utility.buffer;

/**
 * Specialised implementation of {@link Buffer} for 32-bit float element(s).
 */
public interface Float32Buffer extends Buffer<Float32Buffer> {
    /**
     * <p>Writes an 32-bit floating point</p>
     *
     * @param value the byte
     *
     * @return a reference to <code>this</code>
     */
    Float32Buffer write(float value);

    /**
     * <p>Writes an buffer of 32-bit floating point</p>
     *
     * @param values the bytes
     *
     * @return a reference to <code>this</code>
     */
    Float32Buffer write(float[] values);

    /**
     * <p>Writes an buffer of 32-bit floating point</p>
     *
     * @param values the buffer of bytes
     * @param offset the buffer's offset
     * @param count  the buffer's count
     *
     * @return a reference to <code>this</code>
     */
    Float32Buffer write(float[] values, int offset, int count);

    /**
     * <p>Read an 32-bit floating point</p>
     *
     * @return the byte read from the buffer
     */
    float read();
}
