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

/**
 * <code>Vertex</code> represent how a {@link Buffer} of {@link BufferTarget#BUFFER_ARRAY} is fragmented.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Vertex {
    private final int mID;
    private final int mComponent;
    private final int mOffset;
    private final VertexType mType;
    private final boolean mNormalised;

    /**
     * <p>Constructor</p>
     */
    public Vertex(int id, int component, int offset, VertexType type, boolean normalised) {
        mID = id;
        mComponent = component;
        mOffset = offset;
        mType = type;
        mNormalised = normalised;
    }

    /**
     * <p>Get the unique identifier of the vertex</p>
     *
     * @return the unique identifier of the vertex
     */
    public int getID() {
        return mID;
    }

    /**
     * <p>Get the number of component(s) of the vertex</p>
     *
     * @return the number of component(s) of the vertex
     */
    public int getComponent() {
        return mComponent;
    }

    /**
     * <p>Get the offset of the vertex</p>
     *
     * @return the offset of the vertex
     */
    public int getOffset() {
        return mOffset;
    }

    /**
     * <p>Get the length of the vertex</p>
     *
     * @return the length of the vertex
     */
    public int getLength() {
        return mComponent * mType.eLength;
    }

    /**
     * <p>Get the type of the vertex</p>
     *
     * @return the type of the vertex
     */
    public VertexType getType() {
        return mType;
    }

    /**
     * <p>Check whenever the vertex is normalised</p>
     *
     * @return <code>true</code> if the vertex is normalised, <code>false</code> otherwise
     */
    public boolean isNormalised() {
        return mNormalised;
    }
}
