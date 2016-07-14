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
package ar.com.quark.graphic.texture;

import ar.com.quark.graphic.Graphic;

/**
 * <code>ImageFormat</code> enumerate possible {@link Image} format(s).
 */
public enum ImageFormat {
    /**
     * Each element is an RED component.
     */
    RED(Graphic.GLES3.GL_RED, 1, false, false, false, false),

    /**
     * Each element is an RGB triple.
     */
    RGB(Graphic.GLES2.GL_RGB, 3, false, false, false, false),

    /**
     * Each element contains all four components.
     */
    RGBA(Graphic.GLES2.GL_RGBA, 4, false, true, false, false),

    /**
     * Only contains the depth component.
     */
    DEPTH_COMPONENT(Graphic.GLES3.GL_DEPTH_COMPONENT, 1, false, false, false, true),

    /**
     * Only contains the red and green component.
     */
    RG(Graphic.GLES3.GL_RG, 2, false, false, false, false),

    /**
     * Only contains the stencil and depth component.
     */
    DEPTH_STENCIL(Graphic.GLES3.GL_DEPTH_STENCIL, 2, false, false, true, true),

    /**
     * Compressed texture using S3TC for RGB.
     */
    RGB_DXT1(Graphic.GLESExtension.S3TC_RGB_DXT1, 3, true, false, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     */
    RGBA_DXT1(Graphic.GLESExtension.S3TC_RGBA_DXT1, 4, true, true, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     */
    RGBA_DXT3(Graphic.GLESExtension.S3TC_RGBA_DXT3, 4, true, true, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     */
    RGBA_DXT5(Graphic.GLESExtension.S3TC_RGBA_DXT5, 4, true, true, false, false);

    public final int eValue;
    public final int eComponent;
    public final boolean eCompressed;
    public final boolean eHasAlpha;
    public final boolean eHasDepth;
    public final boolean eHasStencil;

    /**
     * <p>Constructor</p>
     */
    ImageFormat(int value, int component, boolean compressed, boolean hasAlpha, boolean hasStencil, boolean hasDepth) {
        eValue = value;
        eComponent = component;
        eCompressed = compressed;
        eHasAlpha = hasAlpha;
        eHasDepth = hasStencil;
        eHasStencil = hasDepth;
    }
}