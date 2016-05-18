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
 * Enumerate {@link Texture} type(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public enum TextureType {
    /**
     * Images in this texture all are 1-dimensional. They have width, but no height or depth.
     * <p>
     * {@since OpenGL 1.1}
     */
    TEXTURE_1D(0xDE0),

    /**
     * Images in this texture all are 2-dimensional. They have width and height, but no depth.
     * <p>
     * {@since OpenGL 1.1}
     */
    TEXTURE_2D(0xDE1),

    /**
     * Images in this texture all are 3-dimensional. They have width, height, and depth.
     * <p>
     * {@since OpenGL 1.2}
     */
    TEXTURE_3D(0x806F);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    TextureType(int value) {
        eValue = value;
    }
}
