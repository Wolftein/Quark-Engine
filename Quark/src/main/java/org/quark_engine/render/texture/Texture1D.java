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

import java.util.List;

/**
 * <code>Texture1D</code> encapsulate a {@link Texture} of 1 dimension.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Texture1D extends Texture {
    protected TextureBorder mBorderX = TextureBorder.REPEAT;

    /**
     * <p>Default constructor</p>
     */
    public Texture1D(TextureFormat format, List<Image> images) {
        super(TextureType.TEXTURE_1D, format, images);
    }

    /**
     * <p>Complex constructor</p>
     */
    public Texture1D(TextureFormat format, TextureFilter filter, TextureBorder borderX, List<Image> images) {
        this(format, images);
        setFilter(filter);
        setClamp(borderX);
    }

    /**
     * <p>Change the border mode for the x coordinate</p>
     *
     * @param xBorder the new border mode for the x coordinate
     */
    public void setClamp(TextureBorder xBorder) {
        if (mBorderX != xBorder) {
            mBorderX = xBorder;
            setUpdate(CONCEPT_CLAMP_X);
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
}
