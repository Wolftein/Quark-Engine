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
package org.quark_engine.render.texture;

import org.quark_engine.render.Render;

/**
 * <code>TextureFilter</code> enumerate {@link Texture} filter(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum TextureFilter {
    /**
     * Selects the nearest texel in the level 0 texture map.
     * <p>
     * Uses the value of the texture element that is nearest (in Manhattan distance) to the center
     * of the pixel being textured.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    POINT(Render.GLES2.GL_NEAREST, Render.GLES2.GL_NEAREST_MIPMAP_NEAREST, Render.GLES2.GL_NEAREST, 1),

    /**
     * Performs a bilinear interpolation on the four nearest texels in the level 0 texture map.
     * <p>
     * Uses the weighted average of the four texture elements that are closest to the center of the pixel
     * being textured. These can include border texture elements, depending on the values of GL_TEXTURE_WRAP_S and
     * GL_TEXTURE_WRAP_T, and on the exact mapping.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    BILINEAR(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_NEAREST, Render.GLES2.GL_LINEAR, 1),

    /**
     * Performs a trilinear interpolation of the texels between four texels each from the two nearest mip-map levels.
     * <p>
     * Chooses the two mip-maps that most closely match the size of the pixel being textured and uses
     * the BASE_LEVEL_LINEAR criterion (a weighted average of the four texture elements that are closest to
     * the center of the pixel) to produce a texture value from each mip-map. The final texture value is a
     * weighted average of those two values.
     * <p>
     * {@since OpenGL    1.1}
     * {@since OpenGL ES 2.0}
     */
    TRILINEAR(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_LINEAR, Render.GLES2.GL_LINEAR, 1),

    /**
     * Uses {@link #TRILINEAR} as base filter and additionally applies a 2x anisotropic filter.
     * <p>
     * {@since OpenGL    1.2}
     * {@since OpenGL ES 2.0}
     * {@since GL_EXT_texture_filter_anisotropic}
     */
    ANISOTROPIC_2(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_LINEAR, Render.GLES2.GL_LINEAR, 2),

    /**
     * Uses {@link #TRILINEAR} as base filter and additionally applies a 4x anisotropic filter.
     * <p>
     * {@since OpenGL    1.2}
     * {@since OpenGL ES 2.0}
     * {@since GL_EXT_texture_filter_anisotropic}
     */
    ANISOTROPIC_4(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_LINEAR, Render.GLES2.GL_LINEAR, 4),

    /**
     * Uses {@link #TRILINEAR} as base filter and additionally applies a 8x anisotropic filter.
     * <p>
     * {@since OpenGL    1.2}
     * {@since OpenGL ES 2.0}
     * {@since GL_EXT_texture_filter_anisotropic}
     */
    ANISOTROPIC_8(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_LINEAR, Render.GLES2.GL_LINEAR, 8),

    /**
     * Uses {@link #TRILINEAR} as base filter and additionally applies a 16x anisotropic filter.
     * <p>
     * {@since OpenGL    1.2}
     * {@since OpenGL ES 2.0}
     * {@since GL_EXT_texture_filter_anisotropic}
     */
    ANISOTROPIC_16(Render.GLES2.GL_LINEAR, Render.GLES2.GL_LINEAR_MIPMAP_LINEAR, Render.GLES2.GL_LINEAR, 16);

    /**
     * Uses the fastest available method for processing geometry.
     */
    public static final TextureFilter FASTEST = POINT;

    /**
     * Uses the nicest available method for processing geometry (excluding anisotropy).
     */
    public static final TextureFilter NICER = TRILINEAR;

    /**
     * Uses the nicest available method for processing geometry.
     */
    public static final TextureFilter NICEST = ANISOTROPIC_16;

    public final int eMinFilter;
    public final int eMinFilterWithMipmap;
    public final int eMagFilter;
    public final int eAnisotropicLevel;

    /**
     * <p>Constructor</p>
     */
    TextureFilter(int minFilter, int minFilterWithMipmap, int magFilter, int anisotropicLevel) {
        eMinFilter = minFilter;
        eMinFilterWithMipmap = minFilterWithMipmap;
        eMagFilter = magFilter;
        eAnisotropicLevel = anisotropicLevel;
    }
}
