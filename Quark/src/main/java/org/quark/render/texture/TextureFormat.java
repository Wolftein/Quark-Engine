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
package org.quark.render.texture;

import org.quark.render.Render;

/**
 * <code>TextureFormat</code> enumerate {@link Texture} format(s).
 */
public enum TextureFormat {
    /**
     * Color value represented as red in 8-bit integer(s).
     */
    R8(Render.GLES3.GL_R8, false, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red in 16-bit integer(s).
     */
    R16(Render.GLES3.GL_R16, false, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red in 16-bit float(s).
     */
    R16F(Render.GLES3.GL_R16F, false, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red in 32-bit float(s).
     */
    R32F(Render.GLES3.GL_R32F, false, Render.GLES3.GL_FLOAT),

    /**
     */
    RG8(Render.GLES3.GL_RG8, false, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green in 16-bit integer(s).
     */
    RG16(Render.GLES3.GL_RG16, false, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green in 16-bit float(s).
     */
    RG16F(Render.GLES3.GL_R16F, false, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green in 32-bit float(s).
     */
    RG32F(Render.GLES3.GL_RG32F, false, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as red/green/blue in 8-bit integer(s).
     */
    RGB8(Render.GLES3.GL_RGB8, false, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue in 16-bit integer(s).
     */
    RGB16(Render.GLES3.GL_RGB16, false, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green/blue in 16-bit float(s).
     */
    RGB16F(Render.GLES3.GL_RGB16F, false, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green/blue in 32-bit float(s).
     */
    RGB32F(Render.GLES3.GL_RGB32F, false, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as red/green/blue/alpha in 8-bit integers(s).
     */
    RGBA8(Render.GLES3.GL_RGBA8, false, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue/alpha in 16-bit integer(s).
     */
    RGBA16(Render.GLES3.GL_RGBA16, false, Render.GLES2.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as red/green/blue/alpha in 16-bit float(s).
     */
    RGBA16F(Render.GLES3.GL_RGBA16F, false, Render.GLES3.GL_HALF_FLOAT),

    /**
     * Color value represented as red/green/blue/alpha in 32-bit float(s).
     */
    RGBA32F(Render.GLES3.GL_RGB32F, false, Render.GLES3.GL_FLOAT),

    /**
     * Color value represented as depth in 16-bit integer.
     */
    DEPTH_COMPONENT16(Render.GLES2.GL_DEPTH_COMPONENT16, false, Render.GLES3.GL_UNSIGNED_SHORT),

    /**
     * Color value represented as depth in 24-bit integer.
     */
    DEPTH_COMPONENT24(Render.GLES3.GL_DEPTH_COMPONENT24, false, Render.GLES3.GL_UNSIGNED_INT),

    /**
     * Color value represented as depth in 32-bit integer.
     */
    DEPTH_COMPONENT32(Render.GLES3.GL_DEPTH_COMPONENT32, false, Render.GLES3.GL_UNSIGNED_INT),

    /**
     * Color value represented as red compressed.
     */
    COMPRESSED_RED(Render.GLESExtension.GL_COMPRESSED_RED, true, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green compressed.
     */
    COMPRESSED_RG(Render.GLESExtension.GL_COMPRESSED_RG, true, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue compressed.
     */
    COMPRESSED_RGB(Render.GLESExtension.GL_COMPRESSED_RGB, true, Render.GLES2.GL_UNSIGNED_BYTE),

    /**
     * Color value represented as red/green/blue/alpha compressed.
     */
    COMPRESSED_RGBA(Render.GLESExtension.GL_COMPRESSED_RGBA, true, Render.GLES2.GL_UNSIGNED_BYTE);

    public final int eValue;
    public final boolean eCompressed;
    public final int eType;

    /**
     * <p>Constructor</p>
     */
    TextureFormat(int value, boolean compressed, int type) {
        eValue = value;
        eCompressed = compressed;
        eType = type;
    }
}
