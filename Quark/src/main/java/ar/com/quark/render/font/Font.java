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
package ar.com.quark.render.font;

import ar.com.quark.mathematic.Colour;
import ar.com.quark.render.texture.TextureFilter;
import ar.com.quark.resource.AssetDescriptor;
import ar.com.quark.render.texture.Texture;

import java.util.List;
import java.util.Map;

/**
 * <code>Font</code> encapsulate a font.
 */
public class Font {
    /**
     * Hold all {@link Texture} page(s).
     */
    protected final List<Texture> mPages;

    /**
     * Hold all {@link FontGlyph}.
     */
    protected final Map<Integer, FontGlyph> mGlyphFactory;

    /**
     * Hold the line height of the font.
     */
    protected final int mLineHeight;

    /**
     * <p>Constructor</p>
     */
    public Font(List<Texture> pages, Map<Integer, FontGlyph> factory, int lineHeight) {
        mPages = pages;
        mGlyphFactory = factory;
        mLineHeight = lineHeight;
    }

    /**
     * <p>Render the given text</p>
     *
     * @param renderer the text's renderer
     * @param text     the text
     * @param x        the x coordinates (in screen coordinates)
     * @param y        the y coordinates (in screen coordinates)
     */
    public void render(FontRenderer renderer, String text, int x, int y) {
        render(renderer, text, x, y, 1.0F, 1.0F, Colour.WHITE);
    }

    /**
     * <p>Render the given text</p>
     *
     * @param renderer the text's renderer
     * @param text     the text
     * @param x        the x coordinates (in screen coordinates)
     * @param y        the y coordinates (in screen coordinates)
     * @param colour   the text's colour
     */
    public void render(FontRenderer renderer, String text, int x, int y, Colour colour) {
        render(renderer, text, x, y, 1.0F, 1.0F, colour);
    }

    /**
     * <p>Render the given text</p>
     *
     * @param renderer the text's renderer
     * @param text     the text
     * @param x        the x coordinates (in screen coordinates)
     * @param y        the y coordinates (in screen coordinates)
     * @param scaleX   the x coordinates scale (clamp from 0.1-max)
     * @param scaleY   the y coordinates scale (clamp from 0.1-max)
     */
    public void render(FontRenderer renderer, String text, int x, int y, float scaleX, float scaleY) {
        render(renderer, text, x, y, scaleX, scaleY, Colour.BLACK);
    }

    /**
     * <p>Render the given text</p>
     *
     * @param renderer the text's renderer
     * @param text     the text
     * @param x        the x coordinates (in screen coordinates)
     * @param y        the y coordinates (in screen coordinates)
     * @param scaleX   the x coordinates scale (clamp from 0.1-max)
     * @param scaleY   the y coordinates scale (clamp from 0.1-max)
     * @param colour   the text's colour
     */
    public void render(FontRenderer renderer, String text, int x, int y, float scaleX, float scaleY, Colour colour) {
        render(renderer, text, x, y, scaleX, scaleY, 0.0F, colour);
    }

    /**
     * <p>Render the given text</p>
     *
     * @param renderer the text's renderer
     * @param text     the text
     * @param x        the x coordinates (in screen coordinates)
     * @param y        the y coordinates (in screen coordinates)
     * @param scaleX   the x coordinates scale (clamp from 0.1-max)
     * @param scaleY   the y coordinates scale (clamp from 0.1-max)
     * @param border   the x/y coordinates scale (for border)
     * @param colour   the text's colour
     */
    public void render(FontRenderer renderer, String text, int x, int y, float scaleX, float scaleY, float border, Colour colour) {
        int xPosition = x;
        int yPosition = y;

        for (int i = 0, j = text.length(); i < j; ) {
            final int c1 = text.codePointAt(i);

            final int c2 = (i + Character.charCount(c1) < j ? text.codePointAt(i + Character.charCount(c1)) : '\0');

            i += Character.charCount(c1);

            //!
            //! Handle special character
            //!
            switch (c1) {
                case '\n':
                    yPosition += getHeight() * scaleY;
                    xPosition = x;
                    continue;
            }

            final FontGlyph glyph = mGlyphFactory.get(c1);

            if (glyph != null) {
                renderer.drawFontGlyph(mPages.get(glyph.getPage()),
                        xPosition + (glyph.getOffsetX() * scaleX),
                        yPosition + (glyph.getOffsetY() * scaleY),
                        glyph.getWidth() * scaleX,
                        glyph.getHeight() * scaleY,
                        glyph.getTextureX1Coordinate(),
                        glyph.getTextureY1Coordinate(),
                        glyph.getTextureX2Coordinate(),
                        glyph.getTextureY2Coordinate(), colour);

                xPosition += getAdvance(c1, c2, scaleX) + (border * scaleX);
            }
        }
    }

    /**
     * <p>Get the width (in pixel coordinates) of the given character</p>
     *
     * @param character the character
     *
     * @return the width of the given character
     */
    public int getWidth(int character) {
        return getWidth(character, 1.0F);
    }

    /**
     * <p>Get the width (in pixel coordinates) of the given character with scale applied</p>
     *
     * @param character the character
     * @param scale     the character's scale
     *
     * @return the width of the given character
     */
    public int getWidth(int character, float scale) {
        final FontGlyph glyph = mGlyphFactory.get(character);

        return glyph != null ? (int) (glyph.getAdvance() * scale) : 0;
    }

    /**
     * <p>Get the width (in pixel coordinates) of the given string</p>
     *
     * @param string the string
     *
     * @return the width of the given string
     */
    public int getWidth(String string) {
        return getWidth(string, 1.0F);
    }

    /**
     * <p>Get the width (in pixel coordinates) of the given string with scale applied</p>
     *
     * @param string the string
     * @param scale  the string's scale
     *
     * @return the width of the given string
     */
    public int getWidth(String string, float scale) {
        int value0 = 0, value1 = 0, value2 = 0;

        for (int i = 0, j = string.length(); i < j; ) {
            final int c1 = string.codePointAt(i);

            i += Character.charCount(c1);

            //!
            //! Handle special character
            //!
            switch (c1) {
                case '\n':
                    value1 = Math.max(value0, value1);
                    value0 = 0;
                    value2 = i;
                    continue;
            }

            final FontGlyph glyph = mGlyphFactory.get(c1);
            if (glyph == null) {
                continue;
            }

            final int c2 = (i < j ? string.codePointAt(i) : '\0');

            if (i == value2) {
                //!
                //! When the character is the first one we only extract the offset.
                //!
                value0 -= glyph.getOffsetX() * scale;
            } else {
                value0 += glyph.getKerning(c2) * scale;
            }

            if (c2 != '\0' && c2 != '\n') {
                //!
                //! When the character is the last one, don't add advance.
                //!
                value0 += glyph.getWidth() * scale + glyph.getOffsetX() * scale;
            } else {
                value0 += glyph.getAdvance() * scale;
            }
        }
        return Math.max(value0, value1);
    }

    /**
     * <p>Get the height (in pixel coordinates) of the font</p>
     *
     * @return the height (in pixel coordinates) of the font
     */
    public int getHeight() {
        return mLineHeight;
    }

    /**
     * <p>Get the height (in pixel coordinates) of the given character</p>
     *
     * @param character the character
     *
     * @return the height (in pixel coordinates) of the character
     */
    public int getHeight(int character) {
        return getHeight(character, 1.0F);
    }

    /**
     * <p>Get the height (in pixel coordinates) of the given character with scale applied</p>
     *
     * @param character the character
     * @param scale     the character's scale
     *
     * @return the height (in pixel coordinates) of the character
     */
    public int getHeight(int character, float scale) {
        final FontGlyph glyph = mGlyphFactory.get(character);

        return glyph != null ? (int) (glyph.getHeight() * scale) : mLineHeight;
    }

    /**
     * <p>Get the height (in pixel coordinates) of the given string</p>
     *
     * @param string the string
     *
     * @return the height (in pixel coordinates) of the string
     */
    public int getHeight(String string) {
        return getHeight(string, 1.0F);
    }

    /**
     * <p>Get the height (in pixel coordinates) of the given string with scale applied</p>
     *
     * @param string the string
     * @param scale  the string's scale
     *
     * @return the height (in pixel coordinates) of the string
     */
    public int getHeight(String string, float scale) {
        int value = 0;

        for (int i = 0, j = string.length(); i < j; ) {
            final int c1 = string.codePointAt(i);

            i += Character.charCount(c1);

            if (c1 == '\n') {
                value += getHeight() * scale;
            } else {
                value = Math.max(value, getHeight(c1, scale));
            }
        }
        return value;
    }

    /**
     * <p>Get the advance offset (in pixel coordinates) of the given sequence</p>
     *
     * @param current the current character
     * @param next    the next character
     * @param scale   the scale
     *
     * @return the advance offset (in pixel coordinates) of the sequence
     */
    public int getAdvance(int current, int next, float scale) {
        final FontGlyph glyph = mGlyphFactory.get(current);

        return glyph != null ? (int) ((glyph.getAdvance() + glyph.getKerning(next)) * scale) : 0;
    }

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link Font}.
     */
    public final static class Descriptor extends AssetDescriptor {
        private final TextureFilter mFilter;

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFilter filter) {
            super(true, true);

            mFilter = filter;
        }

        /**
         * <p>Get the {@link TextureFilter} of the font {@link Texture}</p>
         *
         * @return the {@link TextureFilter} of the font {@link Texture}
         */
        public TextureFilter getFilter() {
            return mFilter;
        }
    }
}
