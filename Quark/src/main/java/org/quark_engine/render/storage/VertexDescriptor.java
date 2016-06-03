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
package org.quark_engine.render.storage;

import org.quark_engine.render.Render;
import org.quark_engine.system.utility.Disposable;
import org.quark_engine.system.utility.Manageable;
import org.quark_engine.render.storage.factory.FactoryStorageIndices;
import org.quark_engine.render.storage.factory.FactoryStorageVertices;

import java.util.List;

import static org.quark_engine.Quark.QkRender;

/**
 * <code>VertexDescriptor</code> encapsulate how {@link FactoryStorageVertices} are fragmented.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class VertexDescriptor extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);

    /**
     * Hold every <code>StorageFactoryVertex</code> that contain(s) the vertices.
     */
    private final List<FactoryStorageVertices<?>> mFactoryVertices;

    /**
     * Hold an optional <code>StorageFactoryElement</code> that contain(s) the indices.
     */
    private final FactoryStorageIndices<?> mFactoryIndices;

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(List<FactoryStorageVertices<?>> vertices, FactoryStorageIndices<?> indices) {
        mFactoryVertices = vertices;
        mFactoryIndices = indices;

        setUpdate(CONCEPT_DATA);
    }

    /**
     * <p>Constructor</p>
     */
    public VertexDescriptor(List<FactoryStorageVertices<?>> vertices) {
        this(vertices, null);
    }

    /**
     * <p>Get the vertices factory (all storage for vertices) of the descriptor</p>
     *
     * @return the vertices factory (all storage for vertices) of the descriptor
     */
    public List<FactoryStorageVertices<?>> getVertices() {
        return mFactoryVertices;
    }

    /**
     * <p>Get the indices factory (all storage for indices) of the descriptor</p>
     *
     * @return the indices factory (all storage for indices) of the descriptor
     */
    public FactoryStorageIndices<?> getIndices() {
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
        QkRender.create(this);
    }

    /**
     * @see Render#delete(VertexDescriptor)
     */
    public void delete() {
        QkRender.delete(this);
    }

    /**
     * @see Render#acquire(VertexDescriptor)
     */
    public void acquire() {
        QkRender.acquire(this);
    }

    /**
     * @see Render#update(VertexDescriptor)
     */
    public void update() {
        QkRender.update(this);
    }

    /**
     * @see Render#release(VertexDescriptor)
     */
    public void release() {
        QkRender.release(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        QkRender.dispose(this);
    }
}
