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
 * <code>Buffer</code> encapsulate a cross-platform efficient container for holding data.
 */
public interface Buffer<A extends Buffer<A>> {
    /**
     * <p>Get the capacity (in bytes) of the buffer</p>
     *
     * @return the capacity (in bytes) of the buffer
     */
    int capacity();

    /**
     * <p>Get the read limit (in bytes) of the buffer</p>
     *
     * @return the read limit (in bytes) of the buffer
     */
    int limit();

    /**
     * <p>Get the position (in bytes) of the buffer</p>
     *
     * @return the position (in bytes) of the buffer
     */
    int position();

    /**
     * <p>Get the remaining bytes until the buffer each it's limit</p>
     *
     * @return the remaining bytes until the buffer each it's limit
     */
    int remaining();

    /**
     * <p>Get the offset (in bytes) of the buffer</p>
     *
     * @return the offset (in bytes) of the buffer
     */
    int offset();

    /**
     * <p>Check if the buffer has any remaining byte for reading</p>
     *
     * @return <code>true</code> if the buffer has remaining byte, <code>false</code> otherwise
     */
    boolean hasRemaining();

    /**
     * <p>Get the underlying implementation of this buffer</p>
     *
     * @return the underlying implementation of this buffer
     */
    <B> B underlying();

    /**
     * <p>Change the position of the buffer</p>
     *
     * @param position the new position
     *
     * @return a reference to <code>this</code>
     */
    A position(int position);

    /**
     * <p>Change the offset of the buffer</p>
     *
     * @param offset the new offset
     *
     * @return a reference to <code>this</code>
     */
    A offset(int offset);

    /**
     * <p>Change the limit of the buffer</p>
     *
     * @param limit the new limit
     *
     * @return a reference to <code>this</code>
     */
    A limit(int limit);

    /**
     * <p>Clear the buffer</p>
     * <p>
     * NOTE: The position will be reset to zero and the limit to it's capacity
     *
     * @return a reference to <code>this</code>
     */
    A clear();

    /**
     * <p>Flip the buffer</p>
     * <p>
     * NOTE: The position will be reset to zero and the limit to it's position
     *
     * @return a reference to <code>this</code>
     */
    A flip();

    /**
     * <p>Rewind the buffer</p>
     * <p>
     * NOTE: The position will be reset to zero
     *
     * @return a reference to <code>this</code>
     */
    A rewind();

    /**
     * <p>Copy the other buffer into this buffer</p>
     *
     * @param other the other buffer
     *
     * @return a reference to <code>this</code>
     */
    A copy(A other);
}
