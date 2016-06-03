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

/**
 * <code>Primitive</code> enumerate all primitive(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum Primitive {
    /**
     * Treats each vertex as a single point. VertexAttribute n defines point n. N points are drawn.
     * <p>
     * {@since OpenGL 1.1}
     */
    POINTS(0x00),

    /**
     * Treats each pair of vertices as an independent line segment.
     * <p>
     * {@since OpenGL 1.1}
     */
    LINES(0x01),

    /**
     * Draws a connected group of line segments from the first vertex to the last, then back to the first.
     * <p>
     * {@since OpenGL 1.1}
     */
    LINE_LOOP(0x02),

    /**
     * Draws a connected group of line segments from the first vertex to the last.
     * <p>
     * {@since OpenGL 1.1}
     */
    LINE_STRIP(0x03),

    /**
     * Treats each triplet of vertices as an independent triangle.
     * <p>
     * {@since OpenGL 1.1}
     */
    TRIANGLES(0x04),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     * <p>
     * {@since OpenGL 1.1}
     */
    TRIANGLE_FAN(0x05),

    /**
     * Draws a connected group of triangles. One triangle is defined for each vertex presented
     * after the first two vertices.
     * <p>
     * {@since OpenGL 1.1}
     */
    TRIANGLE_STRIP(0x06),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    LINES_ADJACENCY(0x0A),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    LINE_STRIP_ADJACENCY(0x0B),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    TRIANGLES_ADJACENCY(0x0C),

    /**
     * Expected to be used specifically with Geometry ShaderSource (GS).
     * <p>
     * {@since OpenGL 3.2}
     * {@since GL_EXT_geometry_shader4}
     */
    TRIANGLE_STRIP_ADJACENCY(0x0D);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    Primitive(int value) {
        eValue = value;
    }
}
