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

/**
 * <code>Primitive</code> enumerate all primitive(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum Primitive {
    /**
     * Treats each vertex as a single point. VertexAttribute n defines point n. N points are drawn.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    POINTS(Render.GLES2.GL_POINTS),

    /**
     * Treats each pair of vertices as an independent line segment.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    LINES(Render.GLES2.GL_LINES),

    /**
     * Draws a connected group of line segments from the first vertex to the last, then back to the first.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    LINE_LOOP(Render.GLES2.GL_LINE_LOOP),

    /**
     * Draws a connected group of line segments from the first vertex to the last.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    LINE_STRIP(Render.GLES2.GL_LINE_STRIP),

    /**
     * Treats each triplet of vertices as an independent triangle.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    TRIANGLES(Render.GLES2.GL_TRIANGLES),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    TRIANGLE_FAN(Render.GLES2.GL_TRIANGLE_FAN),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    TRIANGLE_STRIP(Render.GLES2.GL_TRIANGLE_STRIP),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL    3.2}
     * {@since OpenGL ES 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    LINES_ADJACENCY(Render.GLES32.GL_LINES_ADJACENCY),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL    3.2}
     * {@since OpenGL ES 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    LINE_STRIP_ADJACENCY(Render.GLES32.GL_LINE_STRIP_ADJACENCY),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL    3.2}
     * {@since OpenGL ES 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    TRIANGLES_ADJACENCY(Render.GLES32.GL_TRIANGLES_ADJACENCY),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL    3.2}
     * {@since OpenGL ES 3.2}
     * {@since GL_EXT_geometry_shader4}
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
