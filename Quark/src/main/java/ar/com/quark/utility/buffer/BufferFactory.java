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
 * <code>BufferFactory</code> encapsulate a singleton for allocating {@link Buffer}(s).
 */
public abstract class BufferFactory {
    /**
     * Hold the instance of the class.
     */
    public static BufferFactory INSTANCE = null;

    /**
     * <p>Allocate a new {@link Int8Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static Int8Buffer allocateInt8(int capacity) {
        return INSTANCE.nAllocateInt8(capacity);
    }

    /**
     * <p>Allocate a new {@link Int16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static Int16Buffer allocateInt16(int capacity) {
        return INSTANCE.nAllocateInt16(capacity);
    }

    /**
     * <p>Allocate a new {@link Int32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static Int32Buffer allocateInt32(int capacity) {
        return INSTANCE.nAllocateInt32(capacity);
    }

    /**
     * <p>Allocate a new {@link UnsignedInt8Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static UnsignedInt8Buffer allocateUnsignedInt8(int capacity) {
        return INSTANCE.nAllocateUnsignedInt8(capacity);
    }

    /**
     * <p>Allocate a new {@link UnsignedInt16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static UnsignedInt16Buffer allocateUnsignedInt16(int capacity) {
        return INSTANCE.nAllocateUnsignedInt16(capacity);
    }

    /**
     * <p>Allocate a new {@link UnsignedInt32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static UnsignedInt32Buffer allocateUnsignedInt32(int capacity) {
        return INSTANCE.nAllocateUnsignedInt32(capacity);
    }

    /**
     * <p>Allocate a new {@link Float16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static Float16Buffer allocateFloat16(int capacity) {
        return INSTANCE.nAllocateFloat16(capacity);
    }

    /**
     * <p>Allocate a new {@link Float32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    public static Float32Buffer allocateFloat32(int capacity) {
        return INSTANCE.nAllocateFloat32(capacity);
    }

    /**
     * <p>Deallocate a {@link Buffer}</p>
     *
     * @param buffer the buffer to deallocate
     *
     * @return <code>null</code>
     */
    public static <T extends Buffer<?>> T deallocate(T buffer) {
        if (buffer != null) {
            INSTANCE.nDeallocate(buffer);
        }
        return null;
    }

    /**
     * <p>Allocate a new {@link Int8Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract Int8Buffer nAllocateInt8(int capacity);

    /**
     * <p>Allocate a new {@link Int16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract Int16Buffer nAllocateInt16(int capacity);

    /**
     * <p>Allocate a new {@link Int32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract Int32Buffer nAllocateInt32(int capacity);

    /**
     * <p>Allocate a new {@link UnsignedInt8Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract UnsignedInt8Buffer nAllocateUnsignedInt8(int capacity);

    /**
     * <p>Allocate a new {@link UnsignedInt16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract UnsignedInt16Buffer nAllocateUnsignedInt16(int capacity);

    /**
     * <p>Allocate a new {@link UnsignedInt32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract UnsignedInt32Buffer nAllocateUnsignedInt32(int capacity);

    /**
     * <p>Allocate a new {@link Float16Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract Float16Buffer nAllocateFloat16(int capacity);

    /**
     * <p>Allocate a new {@link Float32Buffer}</p>
     *
     * @param capacity the number of element(s) in the buffer
     *
     * @return the allocated buffer
     */
    protected abstract Float32Buffer nAllocateFloat32(int capacity);

    /**
     * <p>Deallocate a {@link Buffer}</p>
     *
     * @param buffer the buffer to deallocate
     */
    protected abstract void nDeallocate(Buffer<?> buffer);
}
