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
package ar.com.quark.backend.lwjgl.utility.array;

import ar.com.quark.system.utility.array.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Implementation for {@link ArrayFactory}.
 */
public final class DesktopArrayFactory extends ArrayFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Int8Array nAllocateInt8Array(int capacity) {
        return new DesktopInt8Array(MemoryUtil.memAlloc(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int16Array nAllocateInt16Array(int capacity) {
        return new DesktopInt16Array(MemoryUtil.memAlloc(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int32Array nAllocateInt32Array(int capacity) {
        return new DesktopInt32Array(MemoryUtil.memAlloc(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt8Array nAllocateUInt8Array(int capacity) {
        return new DesktopUInt8Array(MemoryUtil.memAlloc(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt16Array nAllocateUInt16Array(int capacity) {
        return new DesktopUInt16Array(MemoryUtil.memAlloc(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInt32Array nAllocateUInt32Array(int capacity) {
        return new DesktopUInt32Array(MemoryUtil.memAlloc(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float16Array nAllocateFloat16Array(int capacity) {
        return new DesktopFloat16Array(MemoryUtil.memAlloc(capacity * 0x02));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float32Array nAllocateFloat32Array(int capacity) {
        return new DesktopFloat32Array(MemoryUtil.memAlloc(capacity * 0x04));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nFree(Array<?> view) {
        MemoryUtil.memFree(view.<ByteBuffer>data());
    }

    /**
     * Specialised implementation for {@link Int8Array}.
     */
    public final static class DesktopInt8Array extends DesktopArray<Int8Array> implements Int8Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopInt8Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Int16Array}.
     */
    public final static class DesktopInt16Array extends DesktopArray<Int16Array> implements Int16Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopInt16Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Int32Array}.
     */
    public final static class DesktopInt32Array extends DesktopArray<Int32Array> implements Int32Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopInt32Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt8Array}.
     */
    public final static class DesktopUInt8Array extends DesktopArray<UInt8Array> implements UInt8Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopUInt8Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt16Array}.
     */
    public final static class DesktopUInt16Array extends DesktopArray<UInt16Array> implements UInt16Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopUInt16Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link UInt32Array}.
     */
    public final static class DesktopUInt32Array extends DesktopArray<UInt32Array> implements UInt32Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopUInt32Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Float16Array}.
     */
    public final static class DesktopFloat16Array extends DesktopArray<Float16Array> implements Float16Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopFloat16Array(ByteBuffer buffer) {
            super(buffer);
        }
    }

    /**
     * Specialised implementation for {@link Float32Array}.
     */
    public final static class DesktopFloat32Array extends DesktopArray<Float32Array> implements Float32Array {
        /**
         * <p>Constructor</p>
         */
        public DesktopFloat32Array(ByteBuffer buffer) {
            super(buffer);
        }
    }
}
