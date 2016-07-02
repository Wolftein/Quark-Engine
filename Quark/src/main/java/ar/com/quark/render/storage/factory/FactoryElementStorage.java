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
package ar.com.quark.render.storage.factory;

import ar.com.quark.render.storage.*;
import ar.com.quark.system.utility.array.UInt8Array;
import ar.com.quark.system.utility.array.Array;
import ar.com.quark.system.utility.array.UInt16Array;
import ar.com.quark.system.utility.array.UInt32Array;

/**
 * Specialised implementation for {@link StorageTarget#ELEMENT}.
 */
public class FactoryElementStorage<A extends Array<?>> extends Storage<A> {
    /**
     * <p>Constructor</p>
     */
    public FactoryElementStorage(StorageType type, StorageMode mode, VertexFormat format, int capacity) {
        super(type, StorageTarget.ELEMENT, mode, format, capacity);
    }

    /**
     * Specialised implementation using {@link UInt8Array}.
     */
    public final static class UInt8 extends FactoryElementStorage<UInt8Array> {
        /**
         * <p>Constructor</p>
         */
        public UInt8(StorageType type, StorageMode mode, int capacity) {
            super(type, mode, VertexFormat.UNSIGNED_BYTE, capacity);
        }
    }

    /**
     * Specialised implementation using {@link UInt16Array}.
     */
    public final static class UInt16 extends FactoryElementStorage<UInt16Array> {
        /**
         * <p>Constructor</p>
         */
        public UInt16(StorageType type, StorageMode mode, int capacity) {
            super(type, mode, VertexFormat.UNSIGNED_SHORT, capacity);
        }
    }

    /**
     * Specialised implementation using {@link UInt32Array}.
     */
    public final static class UInt32 extends FactoryElementStorage<UInt32Array> {
        /**
         * <p>Constructor</p>
         */
        public UInt32(StorageType type, StorageMode mode, int capacity) {
            super(type, mode, VertexFormat.UNSIGNED_INT, capacity);
        }
    }
}
