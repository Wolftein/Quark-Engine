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
package org.quark.render.texture;

import org.quark.render.Render;

/**
 * <code>TextureFormat</code> enumerate {@link Texture} format(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum TextureFormat {
    /**
     * Color value represented as red in 8-bit integer(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    R8(Render.GLES3.GL_R8, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red in 16-bit integer(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    R16(Render.GLES3.GL_R16, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red in 16-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    R16F(Render.GLES3.GL_R16F, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red in 32-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    R32F(Render.GLES3.GL_R32F, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as red/green in 8-bit integer(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RG8(Render.GLES3.GL_RG8, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green in 16-bit integer(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RG16(Render.GLES3.GL_RG16, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green in 16-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RG16F(Render.GLES3.GL_R16F, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green in 32-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RG32F(Render.GLES3.GL_RG32F, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as red/green/blue in 8-bit integer(s).
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 3.0}
     */
    RGB8(Render.GLES3.GL_RGB8, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue in 16-bit integer(s).
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 3.0}
     */
    RGB16(Render.GLES3.GL_RGB16, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green/blue in 16-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RGB16F(Render.GLES3.GL_RGB16F, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green/blue in 32-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RGB32F(Render.GLES3.GL_RGB32F, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as red/green/blue/alpha in 8-bit integers(s).
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 3.0}
     */
    RGBA8(Render.GLES3.GL_RGBA8, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue/alpha in 16-bit integer(s).
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 3.0}
     */
    RGBA16(Render.GLES3.GL_RGBA16, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green/blue/alpha in 16-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RGBA16F(Render.GLES3.GL_RGBA16F, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green/blue/alpha in 32-bit float(s).
     * <p>
     * {@since OpenGL    3.0}
     * {@since OpenGL ES 3.0}
     */
    RGBA32F(Render.GLES3.GL_RGB32F, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as depth in 16-bit integer.
     * <p>
     * {@since OpenGL    1.4}
     * {@since OpenGL ES 3.0}
     */
    DEPTH_COMPONENT16(Render.GLES2.GL_DEPTH_COMPONENT16, Render.GLES3.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as depth in 24-bit integer.
     * <p>
     * {@since OpenGL    1.4}
     * {@since OpenGL ES 3.0}
     */
    DEPTH_COMPONENT24(Render.GLES3.GL_DEPTH_COMPONENT24, Render.GLES3.GL_UNSIGNED_INT),

    /**
     * Color value represented as depth in 32-bit integer.
     * <p>
     * {@since OpenGL    1.4}
     * {@since OpenGL ES 3.0}
     */
    DEPTH_COMPONENT32(Render.GLES3.GL_DEPTH_COMPONENT32, Render.GLES3.GL_UNSIGNED_INT);

    public final int eValue;
    public final int eType;

    /**
     * <p>Constructor</p>
     */
    TextureFormat(int value, int type) {
        eValue = value;
        eType = type;
    }
}
