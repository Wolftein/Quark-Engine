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
 * <code>ArrayFactory</code> encapsulate a singleton for allocating {@link Array}(s).
 */
public abstract class ArrayFactory {
    /**
     * Hold the instance of the class.
     */
    public static ArrayFactory instance = null;

    /**
     * <p>Allocate a new {@link Int8Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Int8Array allocateInt8Array(int capacity) {
        return instance.nAllocateInt8Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Int16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Int16Array allocateInt16Array(int capacity) {
        return instance.nAllocateInt16Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Int32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Int32Array allocateInt32Array(int capacity) {
        return instance.nAllocateInt32Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Int64Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Int64Array allocateInt64Array(int capacity) {
        return instance.nAllocateInt64Array(capacity);
    }

    /**
     * <p>Allocate a new {@link UInt8Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static UInt8Array allocateUInt8Array(int capacity) {
        return instance.nAllocateUInt8Array(capacity);
    }

    /**
     * <p>Allocate a new {@link UInt16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static UInt16Array allocateUInt16Array(int capacity) {
        return instance.nAllocateUInt16Array(capacity);
    }

    /**
     * <p>Allocate a new {@link UInt32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static UInt32Array allocateUInt32Array(int capacity) {
        return instance.nAllocateUInt32Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Float16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Float16Array allocateFloat16Array(int capacity) {
        return instance.nAllocateFloat16Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Float32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Float32Array allocateFloat32Array(int capacity) {
        return instance.nAllocateFloat32Array(capacity);
    }

    /**
     * <p>Allocate a new {@link Float64Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    public static Float64Array allocateFloat64Array(int capacity) {
        return instance.nAllocateFloat64Array(capacity);
    }

    /**
     * <p>Deallocate a {@link Array}</p>
     *
     * @return <code>null</code>
     */
    public static <T extends Array<?>> T free(T view) {
        if (view != null) {
            instance.nFree(view);
        }
        return null;
    }

    /**
     * <p>Allocate a new {@link Int8Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Int8Array nAllocateInt8Array(int capacity);

    /**
     * <p>Allocate a new {@link Int16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Int16Array nAllocateInt16Array(int capacity);

    /**
     * <p>Allocate a new {@link Int32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Int32Array nAllocateInt32Array(int capacity);

    /**
     * <p>Allocate a new {@link Int64Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Int64Array nAllocateInt64Array(int capacity);

    /**
     * <p>Allocate a new {@link UInt8Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract UInt8Array nAllocateUInt8Array(int capacity);

    /**
     * <p>Allocate a new {@link UInt16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract UInt16Array nAllocateUInt16Array(int capacity);

    /**
     * <p>Allocate a new {@link UInt32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract UInt32Array nAllocateUInt32Array(int capacity);

    /**
     * <p>Allocate a new {@link Float16Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Float16Array nAllocateFloat16Array(int capacity);

    /**
     * <p>Allocate a new {@link Float32Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Float32Array nAllocateFloat32Array(int capacity);

    /**
     * <p>Allocate a new {@link Float64Array}</p>
     *
     * @param capacity the number of element(s) in the array
     *
     * @return the allocated array
     */
    protected abstract Float64Array nAllocateFloat64Array(int capacity);

    /**
     * <p>Deallocate a {@link Array}</p>
     */
    protected abstract void nFree(Array<?> view);
}
