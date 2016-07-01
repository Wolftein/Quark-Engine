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
package com.github.wolftein.quark.render.storage.factory;

import com.github.wolftein.quark.render.storage.*;
import com.github.wolftein.quark.system.utility.array.*;
import com.github.wolftein.quark.system.utility.emulation.Emulation;

import java.util.List;

/**
 * Specialised implementation for {@link StorageTarget#ARRAY}.
 */
public class FactoryArrayStorage<A extends Array<?>> extends Storage<A> {
    private final List<Vertex> mAttributes;
    private final int mAttributesLength;

    /**
     * <p>Constructor</p>
     */
    public FactoryArrayStorage(StorageType type, StorageMode mode, VertexFormat format, int capacity, List<Vertex> vertex) {
        super(type, StorageTarget.ARRAY, mode, format, capacity);

        mAttributes = vertex;
        mAttributesLength = Emulation.forEachMapToInt(vertex, (attribute) -> attribute.getLength());
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
     * Specialised implementation using {@link Int8}.
     */
    public final static class Int8 extends FactoryArrayStorage<Int8Array> {
        /**
         * <p>Constructor</p>
         */
        public Int8(StorageType type, StorageMode mode, int capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.BYTE, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Int16}.
     */
    public final static class Int16 extends FactoryArrayStorage<Int16Array> {
        /**
         * <p>Constructor</p>
         */
        public Int16(StorageType type, StorageMode mode, int capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.SHORT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Int32}.
     */
    public final static class Int32 extends FactoryArrayStorage<Int32Array> {
        /**
         * <p>Constructor</p>
         */
        public Int32(StorageType type, StorageMode mode, int capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.INT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Float16}.
     */
    public final static class Float16 extends FactoryArrayStorage<Float16Array> {
        /**
         * <p>Constructor</p>
         */
        public Float16(StorageType type, StorageMode mode, int capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.HALF_FLOAT, capacity, attributes);
        }
    }

    /**
     * Specialised implementation using {@link Float32}.
     */
    public final static class Float32 extends FactoryArrayStorage<Float32Array> {
        /**
         * <p>Constructor</p>
         */
        public Float32(StorageType type, StorageMode mode, int capacity, List<Vertex> attributes) {
            super(type, mode, VertexFormat.FLOAT, capacity, attributes);
        }
    }
}
