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
package ar.com.quark.render.storage;

import ar.com.quark.render.storage.factory.FactoryElementStorage;
import ar.com.quark.render.Render;
import ar.com.quark.system.utility.Disposable;
import ar.com.quark.system.utility.Manageable;
import ar.com.quark.render.storage.factory.FactoryArrayStorage;

import java.util.List;

import static ar.com.quark.Quark.QKRender;

/**
 * <code>VertexDescriptor</code> encapsulate how {@link FactoryArrayStorage} are fragmented.
 */
public final class VertexDescriptor extends Manageable implements Disposable {
    private final List<FactoryArrayStorage<?>> mFactoryVertices;
    private final FactoryElementStorage<?> mFactoryIndices;

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(List<FactoryArrayStorage<?>> vertices, FactoryElementStorage<?> indices) {
        mFactoryVertices = vertices;
        mFactoryIndices = indices;
    }

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(List<FactoryArrayStorage<?>> vertices) {
        this(vertices, null);
    }

    /**
     * <p>Get the vertices factory (all storage for vertices) of the descriptor</p>
     *
     * @return the vertices factory (all storage for vertices) of the descriptor
     */
    public List<FactoryArrayStorage<?>> getVertices() {
        return mFactoryVertices;
    }

    /**
     * <p>Get the indices factory (all storage for indices) of the descriptor</p>
     *
     * @return the indices factory (all storage for indices) of the descriptor
     */
    public FactoryElementStorage<?> getIndices() {
        return mFactoryIndices;
    }

    /**
     * <p>Check if the descriptor has vertices</p>
     *
     * @return <code>true</code> if the descriptor has vertices, <code>false</code> otherwise
     */
    public boolean hasVertices() {
        return mFactoryVertices != null && mFactoryVertices.size() > 0;
    }

    /**
     * <p>Check if the descriptor has indices</p>
     *
     * @return <code>true</code> if the descriptor has indices, <code>false</code> otherwise
     */
    public boolean hasIndices() {
        return mFactoryIndices != null;
    }

    /**
     * @see Render#create(VertexDescriptor)
     */
    public void create() {
        QKRender.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        QKRender.delete(this);
    }

    /**
     * @see Render#acquire(VertexDescriptor)
     */
    public void acquire() {
        QKRender.acquire(this);
    }

    /**
     * @see Render#release(VertexDescriptor)
     */
    public void release() {
        QKRender.release(this);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKRender.dispose(this);
    }
}
