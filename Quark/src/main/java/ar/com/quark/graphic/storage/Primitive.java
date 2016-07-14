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

/**
 * <code>Primitive</code> enumerate all primitive(s).
 */
public enum Primitive {
    /**
     * Treats each vertex as a single point. VertexAttribute n defines point n. N points are drawn.
     */
    POINTS(Graphic.GLES2.GL_POINTS),

    /**
     * Treats each pair of vertices as an independent line segment.
     */
    LINES(Graphic.GLES2.GL_LINES),

    /**
     * Draws a connected group of line segments from the first vertex to the last, then back to the first.
     */
    LINE_LOOP(Graphic.GLES2.GL_LINE_LOOP),

    /**
     * Draws a connected group of line segments from the first vertex to the last.
     */
    LINE_STRIP(Graphic.GLES2.GL_LINE_STRIP),

    /**
     * Treats each triplet of vertices as an independent triangle.
     */
    TRIANGLES(Graphic.GLES2.GL_TRIANGLES),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     */
    TRIANGLE_FAN(Graphic.GLES2.GL_TRIANGLE_FAN),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     */
    TRIANGLE_STRIP(Graphic.GLES2.GL_TRIANGLE_STRIP),

    /**
     * Expected to be used specifically with geometry stage.
     */
    LINES_ADJACENCY(Graphic.GLES32.GL_LINES_ADJACENCY),

    /**
     * Expected to be used specifically with geometry stage.
     */
    LINE_STRIP_ADJACENCY(Graphic.GLES32.GL_LINE_STRIP_ADJACENCY),

    /**
     * Expected to be used specifically with geometry stage.
     */
    TRIANGLES_ADJACENCY(Graphic.GLES32.GL_TRIANGLES_ADJACENCY),

    /**
     * Expected to be used specifically with geometry stage.
     */
    TRIANGLE_STRIP_ADJACENCY(Graphic.GLES32.GL_TRIANGLE_STRIP_ADJACENCY);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    Primitive(int value) {
        eValue = value;
    }
}
