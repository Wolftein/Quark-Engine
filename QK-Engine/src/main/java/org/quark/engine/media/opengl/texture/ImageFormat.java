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
package org.quark.engine.media.opengl.texture;

/**
 * Enumerates possible {@link Image} format(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum ImageFormat {
    /**
     * Each element is an RED component.
     * <p>
     * {@since OpenGL 1.1}
     */
    RED(0x1903, 1, false, false, false, false),

    /**
     * Each element is an RGB triple.
     * <p>
     * {@since OpenGL 1.1}
     */
    RGB(0x1907, 3, false, false, false, false),

    /**
     * Each element contains all four components.
     * <p>
     * {@since OpenGL 1.1}
     */
    RGBA(0x1908, 4, false, true, false, false),

    /**
     * Only contains the depth component.
     * <p>
     * {@since OpenGL 1.1}
     */
    DEPTH_COMPONENT(0x1902, 1, false, false, false, true),

    /**
     * Only contains the red and green component.
     * <p>
     * {@since OpenGL 3.0}
     */
    RG(0x8227, 2, false, false, false, false),

    /**
     * Only contains the stencil and depth component.
     * <p>
     * {@since OpenGL 3.0}
     */
    DEPTH_STENCIL(0x1902, 2, false, false, true, true),

    /**
     * Compressed texture using S3TC for RGB.
     * <p>
     * {@since EXT_texture_compression_s3tc}
     */
    RGB_DXT1(0x83F0, 3, true, false, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     * <p>
     * {@since EXT_texture_compression_s3tc}
     */
    RGBA_DXT1(0x83F1, 4, true, true, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     * <p>
     * {@since EXT_texture_compression_s3tc}
     */
    RGBA_DXT3(0x83F2, 4, true, true, false, false),

    /**
     * Compressed texture using S3TC for RGBA.
     * <p>
     * {@since EXT_texture_compression_s3tc}
     */
    RGBA_DXT5(0x83F3, 4, true, true, false, false);

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