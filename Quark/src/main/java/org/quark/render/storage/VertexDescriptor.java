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
package org.quark.render.storage;

import org.quark.render.Render;
import org.quark.system.utility.Disposable;
import org.quark.system.utility.Manageable;
import org.quark.render.storage.factory.FactoryElementStorage;
import org.quark.render.storage.factory.FactoryArrayStorage;

import java.util.List;

import static org.quark.Quark.QKRender;

/**
 * <code>VertexDescriptor</code> encapsulate how {@link FactoryArrayStorage} are fragmented.
 */
public final class VertexDescriptor extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);

    /**
     * Hold every <code>StorageFactoryVertex</code> that contain(s) the vertices.
     */
    private final List<FactoryArrayStorage<?>> mFactoryVertices;

    /**
     * Hold an optional <code>StorageFactoryElement</code> that contain(s) the indices.
     */
    private final FactoryElementStorage<?> mFactoryIndices;

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(List<FactoryArrayStorage<?>> vertices, FactoryElementStorage<?> indices) {
        mFactoryVertices = vertices;
        mFactoryIndices = indices;

        setUpdate(CONCEPT_DATA);
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
     * @see Render#update(VertexDescriptor)
     */
    public void update() {
        QKRender.update(this);
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
