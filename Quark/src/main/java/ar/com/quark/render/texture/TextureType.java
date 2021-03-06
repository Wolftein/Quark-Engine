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
 * <code>TextureType</code> enumerate {@link Texture} type(s).
 */
public enum TextureType {
    /**
     * Images in this texture all are 2-dimensional. They have width and height, but no depth.
     */
    TEXTURE_2D(Render.GLES2.GL_TEXTURE_2D),

    /**
     * Images in this texture all are 3-dimensional. They have width, height, and depth.
     */
    TEXTURE_3D(Render.GLES3.GL_TEXTURE_3D),

    /**
     * Images in this texture consists of six 2D images. The images are arranged in a cube-shape.
     */
    TEXTURE_CUBE(Render.GLES2.GL_TEXTURE_CUBE_MAP);

    public final int eValue;

    /**
     * <p>Constructor</p>
     */
    TextureType(int value) {
        eValue = value;
    }
}
