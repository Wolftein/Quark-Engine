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
 * <code>TextureBorder</code> enumerate {@link Texture} border mode(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum TextureBorder {
    /**
     * Causes the integer part of the s coordinate to be ignored, creating a repeating pattern.
     * <p>
     * {@since OpenGL 1.1}
     */
    REPEAT(0x2901),

    /**
     * Causes coordinates to be clamped to the range (1/2n. 1 - 1/2n) where N is the size of the texture
     * in the direction of clamping.
     * <p>
     * {@since OpenGL 1.2}
     */
    CLAMP_TO_EDGE(0x812F),

    /**
     * Evaluates coordinates in a similar manner to {@link #CLAMP_TO_EDGE}, the fetched texel data
     * is substituted with the values specified by the border color of the texture.
     * <p>
     * {@since OpenGL 1.3}
     */
    CLAMP_TO_BORDER(0x812D),

    /**
     * The texture will also be repeated, but it will be mirrored when the integer part of the coordinate is odd.
     * <p>
     * {@since OpenGL 1.4}
     */
    MIRRORED_REPEAT(0x8370);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    TextureBorder(int value) {
        eValue = value;
    }
}

