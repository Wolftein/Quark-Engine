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
import org.quark.render.shader.StageType;

/**
 * <code>Primitive</code> enumerate all primitive(s).
 */
public enum Primitive {
    /**
     * Treats each vertex as a single point. VertexAttribute n defines point n. N points are drawn.
     */
    POINTS(Render.GLES2.GL_POINTS),

    /**
     * Treats each pair of vertices as an independent line segment.
     */
    LINES(Render.GLES2.GL_LINES),

    /**
     * Draws a connected group of line segments from the first vertex to the last, then back to the first.
     */
    LINE_LOOP(Render.GLES2.GL_LINE_LOOP),

    /**
     * Draws a connected group of line segments from the first vertex to the last.
     */
    LINE_STRIP(Render.GLES2.GL_LINE_STRIP),

    /**
     * Treats each triplet of vertices as an independent triangle.
     */
    TRIANGLES(Render.GLES2.GL_TRIANGLES),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     */
    TRIANGLE_FAN(Render.GLES2.GL_TRIANGLE_FAN),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     */
    TRIANGLE_STRIP(Render.GLES2.GL_TRIANGLE_STRIP),

    /**
     * Expected to be used specifically with {@link StageType#GEOMETRY}.
     */
    LINES_ADJACENCY(Render.GLES32.GL_LINES_ADJACENCY),

    /**
     * Expected to be used specifically with {@link StageType#GEOMETRY}.
     */
    LINE_STRIP_ADJACENCY(Render.GLES32.GL_LINE_STRIP_ADJACENCY),

    /**
     * Expected to be used specifically with {@link StageType#GEOMETRY}.
     */
    TRIANGLES_ADJACENCY(Render.GLES32.GL_TRIANGLES_ADJACENCY),

    /**
     * Expected to be used specifically with {@link StageType#GEOMETRY}.
     */
    TRIANGLE_STRIP_ADJACENCY(Render.GLES32.GL_TRIANGLE_STRIP_ADJACENCY);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    Primitive(int value) {
        eValue = value;
    }
}
