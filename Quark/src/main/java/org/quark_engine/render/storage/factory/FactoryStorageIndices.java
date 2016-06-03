/*
 * This file is part of Quark Engine, licensed under the APACHE License.
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
package org.quark_engine.render.storage.factory;

import org.quark_engine.render.storage.*;

import java.nio.*;

/**
 * Specialised implementation for {@link StorageTarget#ELEMENT}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public class FactoryStorageIndices<A extends Buffer> extends Storage<A> {
    /**
     * <p>Constructor</p>
     */
    public FactoryStorageIndices(StorageType type, StorageMode mode, VertexFormat format, long capacity) {
        super(type, StorageTarget.ELEMENT, mode, format, capacity);
    }

    /**
     * <code>ElementStorage</code> implementation using byte.
     */
    public final static class Byte extends FactoryStorageIndices<ByteBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Byte(StorageType type, StorageMode mode, long capacity) {
            super(type, mode, VertexFormat.UNSIGNED_BYTE, capacity);
        }
    }

    /**
     * <code>ElementStorage</code> implementation using short.
     */
    public final static class Short extends FactoryStorageIndices<ShortBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Short(StorageType type, StorageMode mode, long capacity) {
            super(type, mode, VertexFormat.UNSIGNED_SHORT, capacity);
        }
    }

    /**
     * <code>ElementStorage</code> implementation using integer.
     */
    public final static class Int extends FactoryStorageIndices<IntBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Int(StorageType type, StorageMode mode, long capacity) {
            super(type, mode, VertexFormat.UNSIGNED_INT, capacity);
        }
    }
}
