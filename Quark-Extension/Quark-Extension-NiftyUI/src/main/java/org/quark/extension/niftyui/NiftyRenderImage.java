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
package org.quark.extension.niftyui;

import de.lessvoid.nifty.spi.render.RenderImage;
import org.quark.render.texture.Texture;

import static org.quark.Quark.QKResources;

/**
 * <code>NiftyRenderImage</code> represent implementation of {@link RenderImage}.
 */
public final class NiftyRenderImage implements RenderImage {
    private final Texture mTexture;

    /**
     * <p>Constructor</p>
     */
    public NiftyRenderImage(Texture texture) {
        mTexture = texture;
    }

    /**
     * <p>Get the underlying texture of the image</p>
     *
     * @return the underlying texture of the image
     */
    public Texture getTexture() {
        return mTexture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return mTexture.getImage().getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return mTexture.getImage().getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        QKResources.unload(mTexture);
    }
}
