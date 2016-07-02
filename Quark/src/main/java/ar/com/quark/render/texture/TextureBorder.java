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
package ar.com.quark.render.texture;

import ar.com.quark.render.Render;

/**
 * <code>TextureBorder</code> enumerate {@link Texture} border mode(s).
 */
public enum TextureBorder {
    /**
     * Causes the integer part of the s coordinate to be ignored, creating a repeating pattern.
     */
    REPEAT(Render.GLES2.GL_REPEAT),

    /**
     * Causes coordinates to be clamped to the range (1/2n. 1 - 1/2n) where N is the size of the texture
     * in the direction of clamping.
     */
    CLAMP_TO_EDGE(Render.GLES2.GL_CLAMP_TO_EDGE),

    /**
     * Evaluates coordinates in a similar manner to {@link #CLAMP_TO_EDGE}, the fetched texel data
     * is substituted with the values specified by the border color of the texture.
     */
    CLAMP_TO_BORDER(Render.GLES2.GL_CLAMP_TO_BORDER),

    /**
     * The texture will also be repeated, but it will be mirrored when the integer part of the coordinate is odd.
     */
    MIRRORED_REPEAT(Render.GLES2.GL_MIRRORED_REPEAT);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    TextureBorder(int value) {
        eValue = value;
    }
}

