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

import ar.com.quark.graphic.storage.factory.FactoryArrayStorage;
import ar.com.quark.graphic.storage.factory.FactoryElementStorage;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

import static ar.com.quark.Quark.QKGraphic;

/**
 * A <code>Mesh</code> consists of vertices and optionally indices which specify which
 * vertices define a primitive.
 * <p>
 * Each vertex is composed of attribute(s) such as position, normal, color or texture coordinate.
 */
public class Mesh {
    protected final VertexDescriptor mDescriptor;

    /**
     * <p>Constructor</p>
     */
    public Mesh(VertexDescriptor descriptor) {
        mDescriptor = descriptor;
    }

    /**
     * <p>Constructor</p>
     */
    public Mesh(ImmutableList<FactoryArrayStorage<?>> vertices, FactoryElementStorage indices) {
        this(new VertexDescriptor(vertices, indices));
    }

    /**
     * <p>Constructor</p>
     */
    public Mesh(FactoryArrayStorage<?> vertices, FactoryElementStorage indices) {
        this(new VertexDescriptor(Lists.immutable.of(vertices), indices));
    }

    /**
     * <p>Constructor</p>
     */
    public Mesh(FactoryArrayStorage<?> vertices) {
        this(new VertexDescriptor(Lists.immutable.of(vertices)));
    }

    /**
     * <p>Get the descriptor of the mesh</p>
     *
     * @return the descriptor of the mesh
     */
    public final VertexDescriptor getDescriptor() {
        return mDescriptor;
    }

    /**
     * <p>Get the vertices of the mesh</p>
     *
     * @return the vertices of the mesh
     */
    public final ImmutableList<FactoryArrayStorage<?>> getVertices() {
        return mDescriptor.getVertices();
    }

    /**
     * <p>Get the vertices of the mesh</p>
     *
     * @param index the index of the vertices storage
     *
     * @return the vertices of the mesh
     */
    public final FactoryArrayStorage<?> getVertices(int index) {
        final ImmutableList<FactoryArrayStorage<?>> vertices = mDescriptor.getVertices();

        if (vertices == null || index < 0 || index >= vertices.size()) {
            throw new IllegalArgumentException("Invalid vertices storage");
        }
        return vertices.get(index);
    }

    /**
     * <p>Get the indices of the mesh</p>
     *
     * @return the indices of the mesh
     */
    public final FactoryElementStorage<?> getIndices() {
        return mDescriptor.getIndices();
    }

    /**
     * <p>Draw the mesh</p>
     *
     * @param primitive the render primitive
     * @param offset    the render offset
     * @param count     the render primitive count
     */
    public final void draw(Primitive primitive, int offset, int count) {
        mDescriptor.acquire();

        //!
        //! Draw a single instance of the mesh using vertices only.
        //!
        QKGraphic.draw(primitive, offset, count);
    }

    /**
     * <p>Draw the mesh (using indices)</p>
     *
     * @param primitive the render primitive
     * @param offset    the render offset
     * @param count     the render primitive count
     * @param format    the indices format
     */
    public final void draw(Primitive primitive, int offset, int count, VertexFormat format) {
        if (!mDescriptor.hasIndices()) {
            throw new IllegalStateException("Cannot draw the mesh without an indices storage");
        }
        mDescriptor.acquire();

        //!
        //! Draw a single instance of the mesh using indices.
        //!
        QKGraphic.draw(primitive, offset, count, format);
    }
}
