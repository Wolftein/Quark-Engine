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
package org.quark.render.storage.factory;

import org.quark.render.storage.*;

import java.nio.*;
import java.util.List;

/**
 * Specialised implementation for {@link StorageTarget#ARRAY}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public class FactoryStorageVertices<A extends Buffer> extends Storage<A> {
    private final List<Vertex> mAttributes;
    private final int mAttributesLength;

    /**
     * <p>Constructor</p>
     */
    public FactoryStorageVertices(
            StorageType type, StorageMode mode, VertexFormat format, long capacity, List<Vertex> vertex) {
        super(type, StorageTarget.ARRAY, mode, format, capacity);

        mAttributes = vertex;
        mAttributesLength = vertex.stream().mapToInt(Vertex::getLength).sum();
    }

    /***
     * <p>Get all {@link Vertex}(s) of the storage</p>
     *
     * @return a collection that contain(s) all attribute(s) of the storage
     */
    public final List<Vertex> getAttributes() {
        return mAttributes;
    }

    /***
     * <p>Get the length of all {@link Vertex}(s) of the storage</p>
     *
     * @return the length of all vertex(s) of the storage
     */
    public final int getAttributesLength() {
        return mAttributesLength;
    }

    /**
     * <code>VertexStorage</code> implementation using byte.
     */
    public final static class Byte extends FactoryStorageVertices<ByteBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Byte(StorageType type, StorageMode mode, long capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.UNSIGNED_BYTE, capacity, attributes);
        }
    }

    /**
     * <code>VertexStorage</code> implementation using short.
     */
    public final static class Short extends FactoryStorageVertices<ShortBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Short(StorageType type, StorageMode mode, long capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.UNSIGNED_SHORT, capacity, attributes);
        }
    }

    /**
     * <code>VertexStorage</code> implementation using integer.
     */
    public final static class Int extends FactoryStorageVertices<IntBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Int(StorageType type, StorageMode mode, long capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.UNSIGNED_INT, capacity, attributes);
        }
    }

    /**
     * <code>VertexStorage</code> implementation using float.
     */
    public final static class Float extends FactoryStorageVertices<FloatBuffer> {
        /**
         * <p>Constructor</p>
         */
        public Float(StorageType type, StorageMode mode, long capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.FLOAT, capacity, attributes);
        }
    }
}
