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
package org.quark.backend.teavm.utility.array;

import org.quark.system.utility.array.*;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.Float64Array;
import org.quark.system.utility.array.Int16Array;
import org.quark.system.utility.array.Int32Array;
import org.quark.system.utility.array.Int8Array;
import org.teavm.jso.typedarrays.*;

/**
 * <a href="http://teavm.org/">TeaVM</a> implementation for {@link ArrayFactory}.
 */
public final class WebArrayFactory extends ArrayFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Int8Array nAllocateInt8Array(int capacity) {
        return new TeaVMInt8Array(ArrayBuffer.create(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int16Array nAllocateInt16Array(int capacity) {
        return new TeaVMInt16Array(ArrayBuffer.create(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int32Array nAllocateInt32Array(int capacity) {
        return new TeaVMInt32Array(ArrayBuffer.create(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int64Array nAllocateInt64Array(int capacity) {
        return new TeaVMInt64Array(ArrayBuffer.create(capacity * 0x08));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt8Array nAllocateUInt8Array(int capacity) {
        return new TeaVMUInt8Array(ArrayBuffer.create(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt16Array nAllocateUInt16Array(int capacity) {
        return new TeaVMUInt16Array(ArrayBuffer.create(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt32Array nAllocateUInt32Array(int capacity) {
        return new TeaVMUInt32Array(ArrayBuffer.create(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float16Array nAllocateFloat16Array(int capacity) {
        return new TeaVMFloat16Array(ArrayBuffer.create(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float32Array nAllocateFloat32Array(int capacity) {
        return new TeaVMFloat32Array(ArrayBuffer.create(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float64Array nAllocateFloat64Array(int capacity) {
        return new TeaVMFloat64Array(ArrayBuffer.create(capacity * 0x08));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nFree(Array<?> view) {
    }

    /**
     * Specialised implementation for {@link Int8Array}.
     */
    public final static class TeaVMInt8Array extends WebArray<Int8Array> implements Int8Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMInt8Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Int16Array}.
     */
    public final static class TeaVMInt16Array extends WebArray<Int16Array> implements Int16Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMInt16Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Int32Array}.
     */
    public final static class TeaVMInt32Array extends WebArray<Int32Array> implements Int32Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMInt32Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Int64Array}.
     */
    public final static class TeaVMInt64Array extends WebArray<Int64Array> implements Int64Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMInt64Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt8Array}.
     */
    public final static class TeaVMUInt8Array extends WebArray<UInt8Array> implements UInt8Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMUInt8Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt16Array}.
     */
    public final static class TeaVMUInt16Array extends WebArray<UInt16Array> implements UInt16Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMUInt16Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt32Array}.
     */
    public final static class TeaVMUInt32Array extends WebArray<UInt32Array> implements UInt32Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMUInt32Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Float16Array}.
     */
    public final static class TeaVMFloat16Array extends WebArray<Float16Array> implements Float16Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMFloat16Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Float32Array}.
     */
    public final static class TeaVMFloat32Array extends WebArray<Float32Array> implements Float32Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMFloat32Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Float64Array}.
     */
    public final static class TeaVMFloat64Array extends WebArray<Float64Array> implements Float64Array {
        /**
         * <p>Constructor</p>
         */
        public TeaVMFloat64Array(ArrayBuffer buffer) {
            super(buffer);
        }
    }
}
