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
package ar.com.quark.backend.lwjgl.utility.buffer;

import ar.com.quark.utility.buffer.*;
import org.lwjgl.system.MemoryUtil;

/**
 * Implementation for {@link BufferFactory}.
 */
public final class DesktopBufferFactory extends BufferFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Int8Buffer nAllocateInt8(int capacity) {
        return new DesktopInt8Buffer(MemoryUtil.memAlloc(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int16Buffer nAllocateInt16(int capacity) {
        return new DesktopInt16Buffer(MemoryUtil.memAllocShort(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Int32Buffer nAllocateInt32(int capacity) {
        return new DesktopInt32Buffer(MemoryUtil.memAllocInt(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UnsignedInt8Buffer nAllocateUnsignedInt8(int capacity) {
        return new DesktopUnsignedInt8Buffer(MemoryUtil.memAlloc(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UnsignedInt16Buffer nAllocateUnsignedInt16(int capacity) {
        return new DesktopUnsignedInt16Buffer(MemoryUtil.memAllocShort(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UnsignedInt32Buffer nAllocateUnsignedInt32(int capacity) {
        return new DesktopUnsignedInt32Buffer(MemoryUtil.memAllocInt(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float16Buffer nAllocateFloat16(int capacity) {
        return new DesktopFloat16Buffer(MemoryUtil.memAllocShort(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Float32Buffer nAllocateFloat32(int capacity) {
        return new DesktopFloat32Buffer(MemoryUtil.memAllocFloat(capacity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void nDeallocate(Buffer<?> buffer) {
        MemoryUtil.memFree(buffer.<java.nio.Buffer>underlying());
    }
}
