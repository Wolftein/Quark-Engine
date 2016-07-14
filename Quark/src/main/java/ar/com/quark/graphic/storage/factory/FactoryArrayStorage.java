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
package ar.com.quark.graphic.storage.factory;

import ar.com.quark.graphic.storage.*;
import ar.com.quark.utility.buffer.*;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * Specialised implementation for {@link StorageTarget#ARRAY}.
 */
public class FactoryArrayStorage<A extends Buffer<?>> extends Storage<A> {
    private final ImmutableList<Vertex> mAttributes;

    /**
     * <p>Constructor</p>
     */
    public FactoryArrayStorage(StorageType type, StorageMode mode, VertexFormat format, int capacity,
            ImmutableList<Vertex> vertex) {
        super(type, StorageTarget.ARRAY, mode, format, capacity);

        mAttributes = vertex;
    }

    /***
     * <p>Get all {@link Vertex}(s) of the storage</p>
     *
     * @return a collection that contain(s) all attribute(s) of the storage
     */
    public final ImmutableList<Vertex> getAttributes() {
        return mAttributes;
    }

    /***
     * <p>Get the length of all {@link Vertex}(s) of the storage</p>
     *
     * @return the length of all vertex(s) of the storage
     */
    public final int getAttributesLength() {
        int value = 0;

        for (final Vertex vertex : mAttributes) {
            value += vertex.getLength();
        }
        return value;
    }

    /**
     * Specialised implementation using {@link Int8Buffer}.
     */
    public final static class Int8 extends FactoryArrayStorage<Int8Buffer> {
        /**
         * <p>Constructor</p>
         */
        public Int8(StorageType type, StorageMode mode, int capacity, ImmutableList<Vertex> attributes) {
            super(type, mode, VertexFormat.BYTE, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Int16Buffer}.
     */
    public final static class Int16 extends FactoryArrayStorage<Int16Buffer> {
        /**
         * <p>Constructor</p>
         */
        public Int16(StorageType type, StorageMode mode, int capacity, ImmutableList<Vertex> attributes) {
            super(type, mode, VertexFormat.SHORT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Int32Buffer}.
     */
    public final static class Int32 extends FactoryArrayStorage<Int32Buffer> {
        /**
         * <p>Constructor</p>
         */
        public Int32(StorageType type, StorageMode mode, int capacity, ImmutableList<Vertex> attributes) {
            super(type, mode, VertexFormat.INT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Float16Buffer}.
     */
    public final static class Float16 extends FactoryArrayStorage<Float16Buffer> {
        /**
         * <p>Constructor</p>
         */
        public Float16(StorageType type, StorageMode mode, int capacity, ImmutableList<Vertex> attributes) {
            super(type, mode, VertexFormat.HALF_FLOAT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Float32Buffer}.
     */
    public final static class Float32 extends FactoryArrayStorage<Float32Buffer> {
        /**
         * <p>Constructor</p>
         */
        public Float32(StorageType type, StorageMode mode, int capacity, ImmutableList<Vertex> attributes) {
            super(type, mode, VertexFormat.FLOAT, capacity, attributes);
        }
    }
}
