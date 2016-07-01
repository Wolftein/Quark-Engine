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
package com.github.wolftein.quark.render.font;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>FontGlyph</code> encapsulate the information of a character in a {@link Font}.
 */
public final class FontGlyph {
    private final int mID;
    private final int mX;
    private final int mY;
    private final int mWidth;
    private final int mHeight;
    private final int mOffsetX;
    private final int mOffsetY;
    private final int mAdvance;
    private final int mPage;
    private final Map<Integer, Integer> mKerning = new HashMap<>();
    private final float mTextureX1;
    private final float mTextureY1;
    private final float mTextureX2;
    private final float mTextureY2;

    /**
     * <p>Constructor</p>
     */
    public FontGlyph(int id,
            int x,
            int y,
            int width,
            int height,
            int offsetX,
            int offsetY,
            int advance,
            int page,
            float textureX1,
            float textureY1,
            float textureX2,
            float textureY2) {
        mID = id;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mOffsetX = offsetX;
        mOffsetY = offsetY;
        mAdvance = advance;
        mPage = page;
        mTextureX1 = textureX1;
        mTextureY1 = textureY1;
        mTextureX2 = textureX2;
        mTextureY2 = textureY2;
    }

    /**
     * <p>Get the x1 coordinate of the character</p>
     *
     * @return the x1 coordinate of the character
     */
    public float getTextureX1Coordinate() {
        return mTextureX1;
    }

    /**
     * <p>Get the y1 coordinate of the character</p>
     *
     * @return the y1 coordinate of the character
     */
    public float getTextureY1Coordinate() {
        return mTextureY1;
    }

    /**
     * <p>Get the x2 coordinate of the character</p>
     *
     * @return the x2 coordinate of the character
     */
    public float getTextureX2Coordinate() {
        return mTextureX2;
    }

    /**
     * <p>Get the y2 coordinate of the character</p>
     *
     * @return the y2 coordinate of the character
     */
    public float getTextureY2Coordinate() {
        return mTextureY2;
    }

    /**
     * <p>Get the unique identifier of the character</p>
     *
     * @return the unique identifier of the character
     */
    public int getID() {
        return mID;
    }

    /**
     * <p>Get the x coordinate (in pixel coordinates) of the character</p>
     *
     * @return the x coordinate (in pixel coordinates) of the character
     */
    public int getX() {
        return mX;
    }

    /**
     * <p>Get the y coordinate (in pixel coordinates) of the character</p>
     *
     * @return the y coordinate (in pixel coordinates) of the character
     */
    public int getY() {
        return mY;
    }

    /**
     * <p>Get the width (in pixel coordinates) of the character</p>
     *
     * @return the width (in pixel coordinates) of the character
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * <p>Get the height (in pixel coordinates) of the character</p>
     *
     * @return the height (in pixel coordinates) of the character
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * <p>Get the offset of the x coordinate (in pixel coordinates) of the character</p>
     *
     * @return the offset of the x coordinate (in pixel coordinates) of the character
     */
    public int getOffsetX() {
        return mOffsetX;
    }

    /**
     * <p>Get the offset of the y coordinate (in pixel coordinates) of the character</p>
     *
     * @return the offset of the y coordinate (in pixel coordinates) of the character
     */
    public int getOffsetY() {
        return mOffsetY;
    }

    /**
     * <p>Get the advance (in pixel coordinates) of the character</p>
     *
     * @return the advance (in pixel coordinates) of the character
     */
    public int getAdvance() {
        return mAdvance;
    }

    /**
     * <p>Get the page (image) of the character</p>
     *
     * @return the page (image) of the character
     */
    public int getPage() {
        return mPage;
    }

    /**
     * <p>Register a new kerning character</p>
     *
     * @param character the kerning's character
     * @param amount    the kerning's amount
     */
    public void addKerning(int character, int amount) {
        mKerning.put(character, amount);
    }

    /**
     * <p>Get the kerning value of the given character</p>
     *
     * @param character the character to retrieve the kerning from
     *
     * @return the kerning value of the given character
     */
    public int getKerning(int character) {
        return mKerning.containsKey(character) ? mKerning.get(character) : 0;
    }
}
