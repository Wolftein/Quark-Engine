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
package com.github.wolftein.quark.extension.niftyui;

import com.github.wolftein.quark.render.font.Font;
import de.lessvoid.nifty.spi.render.RenderFont;

import static com.github.wolftein.quark.Quark.QKResources;

/**
 * <code>NiftyRenderFont</code> represent implementation of {@link RenderFont}.
 */
public final class NiftyRenderFont implements RenderFont {
    private final Font mFont;

    /**
     * <p>Constructor</p>
     */
    public NiftyRenderFont(Font font) {
        mFont = font;
    }

    /**
     * <p>Get the underlying font of the font</p>
     *
     * @return the underlying font of the font
     */
    public Font getFont() {
        return mFont;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth(String text) {
        return mFont.getWidth(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth(String text, float size) {
        return mFont.getWidth(text, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return mFont.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCharacterAdvance(char currentCharacter, char nextCharacter, float size) {
        return mFont.getAdvance(currentCharacter, nextCharacter, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        QKResources.unload(mFont);
    }
}
