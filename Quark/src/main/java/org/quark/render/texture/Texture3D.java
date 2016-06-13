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

/**
 * <code>Texture3D</code> encapsulate a {@link Texture} of 3 dimension.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Texture3D extends Texture {
    protected TextureBorder mBorderX = TextureBorder.REPEAT;
    protected TextureBorder mBorderY = TextureBorder.REPEAT;
    protected TextureBorder mBorderZ = TextureBorder.REPEAT;

    /**
     * <p>Constructor</p>
     */
    public Texture3D(TextureFormat format, Image image) {
        super(TextureType.TEXTURE_3D, format, image);
    }

    /**
     * <p>Constructor</p>
     */
    public Texture3D(TextureFormat format, TextureFilter filter,
            TextureBorder borderX,
            TextureBorder borderY,
            TextureBorder borderZ, Image image) {
        this(format, image);
        setFilter(filter);
        setClamp(borderX, borderY, borderZ);
    }

    /**
     * <p>Change the border mode for the x, y and z coordinate</p>
     *
     * @param xBorder the new border mode for the x coordinate
     * @param yBorder the new border mode for the y coordinate
     * @param zBorder the new border mode for the z coordinate
     */
    public void setClamp(TextureBorder xBorder, TextureBorder yBorder, TextureBorder zBorder) {
        if (mBorderX != xBorder) {
            mBorderX = xBorder;
            setUpdate(CONCEPT_CLAMP_X);
        }
        if (mBorderY != yBorder) {
            mBorderY = yBorder;
            setUpdate(CONCEPT_CLAMP_Y);
        }
        if (mBorderZ != zBorder) {
            mBorderZ = zBorder;
            setUpdate(CONCEPT_CLAMP_Z);
        }
    }

    /**
     * <p>Get the border mode for the x coordinate</p>
     *
     * @return the border mode for the x coordinate
     */
    public TextureBorder getBorderX() {
        return mBorderX;
    }

    /**
     * <p>Get the border mode for the y coordinate</p>
     *
     * @return the border mode for the y coordinate
     */
    public TextureBorder getBorderY() {
        return mBorderY;
    }

    /**
     * <p>Get the border mode for the z coordinate</p>
     *
     * @return the border mode for the y coordinate
     */
    public TextureBorder getBorderZ() {
        return mBorderZ;
    }
}
