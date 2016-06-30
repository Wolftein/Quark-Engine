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

import de.lessvoid.nifty.spi.render.MouseCursor;
import org.quark.render.texture.Texture;

import static org.quark.Quark.QKDisplay;
import static org.quark.Quark.QKResources;

/**
 * <code>NiftyCursor</code> represent implementation of {@link MouseCursor}.
 */
public final class NiftyCursor implements MouseCursor {
    private final Texture mTexture;

    /**
     * Hold the coordinate(s) of the cursor's hotspot.
     */
    private final int mHotspotX, mHotspotY;

    /**
     * <p>Constructor</p>
     */
    public NiftyCursor(Texture texture, int xHotspot, int yHotspot) {
        mTexture = texture;

        mHotspotX = xHotspot;
        mHotspotY = yHotspot;
    }

    /**
     * <p>Get the underlying texture of the cursor</p>
     *
     * @return the underlying texture of the cursor
     */
    public Texture getTexture() {
        return mTexture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enable() {
        QKDisplay.setCursor(mTexture.getImage(), mHotspotX, mHotspotY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disable() {
        QKDisplay.setCursor(null, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        QKResources.unload(mTexture);
    }
}
