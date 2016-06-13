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
package org.quark.render.storage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <code>Vertex</code> encapsulate how a {@link Storage} of {@link StorageTarget#ARRAY} is managed.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Vertex {
    /**
     * Encapsulate the unique identifier of the attribute.
     */
    private final int mID;

    /**
     * Encapsulate the number of component(s) the attribute has.
     */
    private final int mComponent;

    /**
     * Encapsulate the offset of the attribute.
     */
    private final int mOffset;

    /**
     * Encapsulate the type of the attribute.
     */
    private final VertexFormat mType;

    /**
     * Encapsulate if the attribute is normalised.
     */
    private final boolean mNormalised;

    /**
     * <p>Constructor</p>
     */
    public Vertex(int id, int component, int offset, VertexFormat type, boolean normalised) {
        mID = id;
        mComponent = component;
        mOffset = offset;
        mType = type;
        mNormalised = normalised;
    }

    /**
     * <p>Constructor</p>
     */
    public Vertex(int id, int component, int offset, VertexFormat type) {
        this(id, component, offset, type, false);
    }

    /**
     * <p>Get the unique identifier of the attribute</p>
     *
     * @return the unique identifier of the attribute
     */
    public int getID() {
        return mID;
    }

    /**
     * <p>Get the number of component(s) the attribute has</p>
     *
     * @return the number of component(s) the attribute has
     */
    public int getComponent() {
        return mComponent;
    }

    /**
     * <p>Get the offset of the attribute</p>
     *
     * @return the offset of the attribute
     */
    public int getOffset() {
        return mOffset;
    }

    /**
     * <p>Get the length of the attribute</p>
     *
     * @return the length of the attribute
     */
    public int getLength() {
        return mComponent * mType.eLength;
    }

    /**
     * <p>Get the type of the attribute</p>
     *
     * @return the type of the attribute
     */
    public VertexFormat getType() {
        return mType;
    }

    /**
     * <p>Check if the attribute is normalised</p>
     *
     * @return <code>true</code> if the attribute is normalised, <code>false</code> otherwise
     */
    public boolean isNormalised() {
        return mNormalised;
    }

    /**
     * <code>Builder</code> build a collection of {@link Vertex} easily.
     */
    public final static class Builder {
        private final List<Vertex> mCollection = new LinkedList<>();
        private int mLength = 0;

        /**
         * <p>Register a new {@link Vertex}</p>
         *
         * @return <code>this</code> for chain operation(s)
         */
        public Builder add(int index, int component, VertexFormat format) {
            return add(index, component, format, false);
        }

        /**
         * <p>Register a new {@link Vertex}</p>
         *
         * @return <code>this</code> for chain operation(s)
         */
        public Builder add(int index, int component, VertexFormat format, boolean normalised) {
            mCollection.add(new Vertex(index, component, mLength, format, normalised));
            mLength += (component * format.eLength);
            return this;
        }

        /**
         * <p>Build a new {@link VertexDescriptor}</p>
         *
         * @return a new instance of the builder constructed
         */
        public List<Vertex> build() {
            return new ArrayList<>(mCollection);
        }
    }
}
