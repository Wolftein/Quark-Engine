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
 * Specialised implementation of {@link Buffer} for 16-bit unsigned integer element(s).
 */
public interface UnsignedInt16Buffer extends Buffer<UnsignedInt16Buffer> {
    /**
     * <p>Writes an 16-bit unsigned integer</p>
     *
     * @param value the byte
     *
     * @return a reference to <code>this</code>
     */
    UnsignedInt16Buffer write(int value);

    /**
     * <p>Writes an buffer of 16-bit unsigned integer</p>
     *
     * @param values the bytes
     *
     * @return a reference to <code>this</code>
     */
    UnsignedInt16Buffer write(short[] values);

    /**
     * <p>Writes an buffer of 16-bit unsigned integer</p>
     *
     * @param values the buffer of bytes
     * @param offset the buffer's offset
     * @param count  the buffer's count
     *
     * @return a reference to <code>this</code>
     */
    UnsignedInt16Buffer write(short[] values, int offset, int count);

    /**
     * <p>Read an 16-bit unsigned integer</p>
     *
     * @return the byte read from the buffer
     */
    int read();
}
