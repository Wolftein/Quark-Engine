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
package org.quark.engine.media.opengl.storage;

import org.quark.engine.media.opengl.GLComponent;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * <code>VertexDescriptor</code> represent a collection of {@link Vertex} attached to a {@link Buffer}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class VertexDescriptor extends GLComponent {
    private final int mLength;
    private final Collection<Vertex> mElements;

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(Collection<Vertex> attributes, int length) {
        mElements = attributes;
        mLength = length;
    }

    /**
     * <p>Get all element(s) attached to the descriptor</p>
     *
     * @return a collection that contains all element(s) attached to the descriptor
     */
    public Collection<Vertex> getElements() {
        return mElements;
    }

    /**
     * <p>Get the length of the descriptor (in bytes)</p>
     *
     * @return the length of the descriptor (in bytes)
     */
    public int getLength() {
        return mLength;
    }

    /**
     * <code>Builder</code> represent a builder for {@link VertexDescriptor}.
     */
    public final static class Builder {
        private List<Vertex> mElements = new LinkedList<>();
        private int mLength;

        /**
         * <p>Register a new {@link Vertex}</p>
         *
         * @param component the component's capacity
         * @param type      the component's type
         *
         * @return <code>this</code> for chain operation(s)
         */
        public Builder add(int component, VertexType type) {
            return add(component, type, false);
        }

        /**
         * <p>Register a new {@link Vertex}</p>
         *
         * @param component  the component's capacity
         * @param type       the component's type
         * @param normalised <code>true</code> if the element is normalised, <code>false</code> otherwise
         *
         * @return <code>this</code> for chain operation(s)
         */
        public Builder add(int component, VertexType type, boolean normalised) {
            mElements.add(new Vertex(mElements.size(), component, mLength, type, normalised));
            mLength += (component * type.eLength);
            return this;
        }

        /**
         * <p>Build a new {@link VertexDescriptor}</p>
         *
         * @return a new instance of the builder constructed
         */
        public VertexDescriptor build() {
            return new VertexDescriptor(mElements, mLength);
        }
    }
}
