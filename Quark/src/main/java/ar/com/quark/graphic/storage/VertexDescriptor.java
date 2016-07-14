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
package ar.com.quark.graphic.storage;

import ar.com.quark.graphic.Graphic;
import ar.com.quark.graphic.storage.factory.FactoryArrayStorage;
import ar.com.quark.graphic.storage.factory.FactoryElementStorage;
import ar.com.quark.utility.Disposable;
import ar.com.quark.utility.Manageable;
import org.eclipse.collections.api.list.ImmutableList;

import static ar.com.quark.Quark.QKGraphic;

/**
 * <code>VertexDescriptor</code> encapsulate how {@link FactoryArrayStorage} are fragmented.
 */
public final class VertexDescriptor extends Manageable implements Disposable {
    private final ImmutableList<FactoryArrayStorage<?>> mFactoryVertices;
    private final FactoryElementStorage<?> mFactoryIndices;

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(ImmutableList<FactoryArrayStorage<?>> vertices, FactoryElementStorage<?> indices) {
        mFactoryVertices = vertices;
        mFactoryIndices = indices;
    }

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(ImmutableList<FactoryArrayStorage<?>> vertices) {
        this(vertices, null);
    }

    /**
     * <p>Get the vertices factory (all storage for vertices) of the descriptor</p>
     *
     * @return the vertices factory (all storage for vertices) of the descriptor
     */
    public ImmutableList<FactoryArrayStorage<?>> getVertices() {
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
     * @see Graphic#create(VertexDescriptor)
     */
    public void create() {
        QKGraphic.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        QKGraphic.delete(this);
    }

    /**
     * @see Graphic#acquire(VertexDescriptor)
     */
    public void acquire() {
        QKGraphic.acquire(this);
    }

    /**
     * @see Graphic#release(VertexDescriptor)
     */
    public void release() {
        QKGraphic.release(this);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKGraphic.dispose(this);
    }
}
