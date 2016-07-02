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
package ar.com.quark.mathematic;

/**
 * <code>Colour</code> encapsulate a 4-component colour (RGBA).
 */
public final class Colour {
    public static final Colour BLACK = Colour.fromPackedInt32FormatRGBA(0x000000FF);
    public static final Colour RED = Colour.fromPackedInt32FormatRGBA(0xFF0000FF);
    public static final Colour GREEN = Colour.fromPackedInt32FormatRGBA(0x00FF00FF);
    public static final Colour BLUE = Colour.fromPackedInt32FormatRGBA(0x0000FFFF);
    public static final Colour WHITE = Colour.fromPackedInt32FormatRGBA(0xFFFFFFFF);
    public static final Colour TRANSPARENT = Colour.fromPackedInt32FormatRGBA(0x000000FF);

    public static final Colour ALICE_BLUE = Colour.fromPackedInt32FormatRGBA(0xF0F8FFFF);
    public static final Colour ANTIQUE_WHITE = Colour.fromPackedInt32FormatRGBA(0xFAEBD7FF);
    public static final Colour AQUA = Colour.fromPackedInt32FormatRGBA(0x00FFFFFF);
    public static final Colour AQUA_MARINE = Colour.fromPackedInt32FormatRGBA(0x7FFFD4FF);
    public static final Colour AZURE = Colour.fromPackedInt32FormatRGBA(0xF0FFFFFF);
    public static final Colour BEIGE = Colour.fromPackedInt32FormatRGBA(0xF5F5DCFF);
    public static final Colour BISQUE = Colour.fromPackedInt32FormatRGBA(0xFFE4CEFF);
    public static final Colour BLANCHED_ALMOND = Colour.fromPackedInt32FormatRGBA(0xFFEBCDFF);
    public static final Colour BLUE_VIOLET = Colour.fromPackedInt32FormatRGBA(0x8A2BE2FF);
    public static final Colour BROWN = Colour.fromPackedInt32FormatRGBA(0xA52A2AFF);
    public static final Colour BURLY_WOOD = Colour.fromPackedInt32FormatRGBA(0xDEB887FF);
    public static final Colour CADET_BLUE = Colour.fromPackedInt32FormatRGBA(0x5F9EA0FF);
    public static final Colour CHART_REUSE = Colour.fromPackedInt32FormatRGBA(0x7FFF00FF);
    public static final Colour CHOCOLATE = Colour.fromPackedInt32FormatRGBA(0xD2691EFF);
    public static final Colour CORAL = Colour.fromPackedInt32FormatRGBA(0xFF7F50FF);
    public static final Colour CORN_FLOWER_BLUE = Colour.fromPackedInt32FormatRGBA(0x6495EDFF);
    public static final Colour CORN_SILK = Colour.fromPackedInt32FormatRGBA(0xFFF8DCFF);
    public static final Colour CRIMSON = Colour.fromPackedInt32FormatRGBA(0xDC143CFF);
    public static final Colour CYAN = Colour.fromPackedInt32FormatRGBA(0x00FFFFFF);
    public static final Colour DARK_BLUE = Colour.fromPackedInt32FormatRGBA(0x00008BFF);
    public static final Colour DARK_CYAN = Colour.fromPackedInt32FormatRGBA(0x008B8BFF);
    public static final Colour DARK_GOLDEN_ROD = Colour.fromPackedInt32FormatRGBA(0xB8860BFF);
    public static final Colour DARK_GRAY = Colour.fromPackedInt32FormatRGBA(0xA9A9A9FF);
    public static final Colour DARK_GREEN = Colour.fromPackedInt32FormatRGBA(0x006400FF);
    public static final Colour DARK_KHAKI = Colour.fromPackedInt32FormatRGBA(0xBDB76BFF);
    public static final Colour DARK_MAGENTA = Colour.fromPackedInt32FormatRGBA(0x8B008BFF);
    public static final Colour DARK_OLIVE_GREEN = Colour.fromPackedInt32FormatRGBA(0x556B2FFF);
    public static final Colour DARK_ORANGE = Colour.fromPackedInt32FormatRGBA(0xFF8C00FF);
    public static final Colour DARK_ORCHID = Colour.fromPackedInt32FormatRGBA(0x9932CCFF);
    public static final Colour DARK_RED = Colour.fromPackedInt32FormatRGBA(0x8B0000FF);
    public static final Colour DARK_SALMON = Colour.fromPackedInt32FormatRGBA(0xE9967AFF);
    public static final Colour DARK_SEA_GREEN = Colour.fromPackedInt32FormatRGBA(0x8FBC8FFF);
    public static final Colour DARK_SLATE_BLUE = Colour.fromPackedInt32FormatRGBA(0x483D8BFF);
    public static final Colour DARK_SLATE_GRAY = Colour.fromPackedInt32FormatRGBA(0x2F4F4FFF);
    public static final Colour DARK_TURQUOISE = Colour.fromPackedInt32FormatRGBA(0x00CED1FF);
    public static final Colour DARK_VIOLET = Colour.fromPackedInt32FormatRGBA(0x9400D3FF);
    public static final Colour DEEP_PINK = Colour.fromPackedInt32FormatRGBA(0xFF1493FF);
    public static final Colour DEEP_SKY_BLUE = Colour.fromPackedInt32FormatRGBA(0x00BFFFFF);
    public static final Colour DIM_GRAY = Colour.fromPackedInt32FormatRGBA(0x696969FF);
    public static final Colour DODGER_BLUE = Colour.fromPackedInt32FormatRGBA(0x1E90FFFF);
    public static final Colour FIRE_BRICK = Colour.fromPackedInt32FormatRGBA(0xB22222FF);
    public static final Colour FLORAL_WHITE = Colour.fromPackedInt32FormatRGBA(0xFFFAF0FF);
    public static final Colour FOREST_GREEN = Colour.fromPackedInt32FormatRGBA(0x228B22FF);
    public static final Colour FUHSIA = Colour.fromPackedInt32FormatRGBA(0xFF00FFFF);
    public static final Colour GAINSBORO = Colour.fromPackedInt32FormatRGBA(0xDCDCDCFF);
    public static final Colour GHOST_WHITE = Colour.fromPackedInt32FormatRGBA(0xF8F8FFFF);
    public static final Colour GOLD = Colour.fromPackedInt32FormatRGBA(0xFFD700FF);
    public static final Colour GOLDEN_ROD = Colour.fromPackedInt32FormatRGBA(0xDAA520FF);
    public static final Colour GRAY = Colour.fromPackedInt32FormatRGBA(0x808080FF);
    public static final Colour GREEN_YELLOW = Colour.fromPackedInt32FormatRGBA(0xADFF2FFF);
    public static final Colour HONEY_DEW = Colour.fromPackedInt32FormatRGBA(0xF0FFF0FF);
    public static final Colour HOT_PINK = Colour.fromPackedInt32FormatRGBA(0xFF69B4FF);
    public static final Colour INDIAN_RED = Colour.fromPackedInt32FormatRGBA(0xCD5C5CFF);
    public static final Colour INDIGO = Colour.fromPackedInt32FormatRGBA(0x4B0082FF);
    public static final Colour IVORY = Colour.fromPackedInt32FormatRGBA(0xFFFFF0FF);
    public static final Colour KHAKI = Colour.fromPackedInt32FormatRGBA(0xF0E68CFF);
    public static final Colour LAVENDER = Colour.fromPackedInt32FormatRGBA(0xE6E6FAFF);
    public static final Colour LAVENDER_BLUSH = Colour.fromPackedInt32FormatRGBA(0xFFF0F5FF);
    public static final Colour LAWN_GREEN = Colour.fromPackedInt32FormatRGBA(0x7CFC00FF);
    public static final Colour LEMON_CHIFFON = Colour.fromPackedInt32FormatRGBA(0xFFFACDFF);
    public static final Colour LIGHT_BLUE = Colour.fromPackedInt32FormatRGBA(0xADD8E6FF);
    public static final Colour LIGHT_CORAL = Colour.fromPackedInt32FormatRGBA(0xF08080FF);
    public static final Colour LIGHT_CYAN = Colour.fromPackedInt32FormatRGBA(0xE0FFFFFF);
    public static final Colour LIGHT_GOLDEN_ROD_YELLOW = Colour.fromPackedInt32FormatRGBA(0xFAFAD2FF);
    public static final Colour LIGHT_GRAY = Colour.fromPackedInt32FormatRGBA(0xD3D3D3FF);
    public static final Colour LIGHT_GREEN = Colour.fromPackedInt32FormatRGBA(0x90EE90FF);
    public static final Colour LIGHT_PINK = Colour.fromPackedInt32FormatRGBA(0xFFB6C1FF);
    public static final Colour LIGHT_SALMON = Colour.fromPackedInt32FormatRGBA(0xFFA07AFF);
    public static final Colour LIGHT_SEA_GREEN = Colour.fromPackedInt32FormatRGBA(0x20B2AAFF);
    public static final Colour LIGHT_SKY_BLUE = Colour.fromPackedInt32FormatRGBA(0x87CEFAFF);
    public static final Colour LIGHT_SLATE_GRAY = Colour.fromPackedInt32FormatRGBA(0x778899FF);
    public static final Colour LIGHT_STEEL_BLUE = Colour.fromPackedInt32FormatRGBA(0xB0C4DEFF);
    public static final Colour LIGHT_YELLOW = Colour.fromPackedInt32FormatRGBA(0xFFFFE0FF);
    public static final Colour LIME = Colour.fromPackedInt32FormatRGBA(0x00FF00FF);
    public static final Colour LIME_GREEN = Colour.fromPackedInt32FormatRGBA(0x32CD32FF);
    public static final Colour LINEN = Colour.fromPackedInt32FormatRGBA(0xFAF0E6FF);
    public static final Colour MAGENTA = Colour.fromPackedInt32FormatRGBA(0xFF00FFFF);
    public static final Colour MAROON = Colour.fromPackedInt32FormatRGBA(0x800000FF);
    public static final Colour MEDIUM_AQUA_MARINE = Colour.fromPackedInt32FormatRGBA(0x66CDAAFF);
    public static final Colour MEDIUM_BLUE = Colour.fromPackedInt32FormatRGBA(0x0000CDFF);
    public static final Colour MEDIUM_ORCHID = Colour.fromPackedInt32FormatRGBA(0xBA55D3FF);
    public static final Colour MEDIUM_PURPLE = Colour.fromPackedInt32FormatRGBA(0x9370DBFF);
    public static final Colour MEDIUM_SEA_GREEN = Colour.fromPackedInt32FormatRGBA(0x3CB371FF);
    public static final Colour MEDIUM_SLATE_BLUE = Colour.fromPackedInt32FormatRGBA(0x7B68EEFF);
    public static final Colour MEDIUM_SPRING_GREEN = Colour.fromPackedInt32FormatRGBA(0x00FA9AFF);
    public static final Colour MEDIUM_TURQUOISE = Colour.fromPackedInt32FormatRGBA(0x48D1CCFF);
    public static final Colour MEDIUM_VIOLET_RED = Colour.fromPackedInt32FormatRGBA(0xC71585FF);
    public static final Colour MIDNIGHT_BLUE = Colour.fromPackedInt32FormatRGBA(0x191970FF);
    public static final Colour MINT_CREAM = Colour.fromPackedInt32FormatRGBA(0xF5FFFAFF);
    public static final Colour MISTY_ROSE = Colour.fromPackedInt32FormatRGBA(0xFFE4E1FF);
    public static final Colour MOCCASIN = Colour.fromPackedInt32FormatRGBA(0xFFE4B5FF);
    public static final Colour NOVAJO_WHITE = Colour.fromPackedInt32FormatRGBA(0xFFDEADFF);
    public static final Colour NAVY = Colour.fromPackedInt32FormatRGBA(0x000080FF);
    public static final Colour OLD_LACE = Colour.fromPackedInt32FormatRGBA(0xFDF5E6FF);
    public static final Colour OLIVE = Colour.fromPackedInt32FormatRGBA(0x808000FF);
    public static final Colour OLIVE_DRAB = Colour.fromPackedInt32FormatRGBA(0x6B8E23FF);
    public static final Colour ORANGE = Colour.fromPackedInt32FormatRGBA(0xFFA500FF);
    public static final Colour ORANGE_RED = Colour.fromPackedInt32FormatRGBA(0xFF4500FF);
    public static final Colour ORCHID = Colour.fromPackedInt32FormatRGBA(0xDA70D6FF);
    public static final Colour PALE_GOLDEN_ROD = Colour.fromPackedInt32FormatRGBA(0xEEE8AAFF);
    public static final Colour PALE_GREEN = Colour.fromPackedInt32FormatRGBA(0x98FB98FF);
    public static final Colour PALE_TURQUOISE = Colour.fromPackedInt32FormatRGBA(0xAFEEEEFF);
    public static final Colour PALE_VIOLET_RED = Colour.fromPackedInt32FormatRGBA(0xDB7093FF);
    public static final Colour PAPAYA_WHIP = Colour.fromPackedInt32FormatRGBA(0xFFEFD5FF);
    public static final Colour PEACH_PUFF = Colour.fromPackedInt32FormatRGBA(0xFFDAB9FF);
    public static final Colour PERU = Colour.fromPackedInt32FormatRGBA(0xCD853FFF);
    public static final Colour PINK = Colour.fromPackedInt32FormatRGBA(0xFFC0CBFF);
    public static final Colour PLUM = Colour.fromPackedInt32FormatRGBA(0xDDA0DDFF);
    public static final Colour POWDER_BLUE = Colour.fromPackedInt32FormatRGBA(0xDDA0DDFF);
    public static final Colour PURPLE = Colour.fromPackedInt32FormatRGBA(0x800080FF);
    public static final Colour ROSY_BROWN = Colour.fromPackedInt32FormatRGBA(0xBC8F8FFF);
    public static final Colour ROYAL_BLUE = Colour.fromPackedInt32FormatRGBA(0x4169E1FF);
    public static final Colour SADDLE_BROWN = Colour.fromPackedInt32FormatRGBA(0x8B4513FF);
    public static final Colour SALMON = Colour.fromPackedInt32FormatRGBA(0xFA8072FF);
    public static final Colour SANDY_BROWN = Colour.fromPackedInt32FormatRGBA(0xF4AF60FF);
    public static final Colour SEA_GREEN = Colour.fromPackedInt32FormatRGBA(0x2E8B57FF);
    public static final Colour SEA_SHELL = Colour.fromPackedInt32FormatRGBA(0xFFF5EEFF);
    public static final Colour SIENNA = Colour.fromPackedInt32FormatRGBA(0xA0522DFF);
    public static final Colour SILVER = Colour.fromPackedInt32FormatRGBA(0xC0C0C0FF);
    public static final Colour SKY_BLUE = Colour.fromPackedInt32FormatRGBA(0x87CEEBFF);
    public static final Colour SLATE_BLUE = Colour.fromPackedInt32FormatRGBA(0x6A5ACDFF);
    public static final Colour SLATE_GRAY = Colour.fromPackedInt32FormatRGBA(0x708090FF);
    public static final Colour SNOW = Colour.fromPackedInt32FormatRGBA(0xFFFAFAFF);
    public static final Colour SPRING_GREEN = Colour.fromPackedInt32FormatRGBA(0x00FF7FFF);
    public static final Colour STEEL_BLUE = Colour.fromPackedInt32FormatRGBA(0x4682B4FF);
    public static final Colour TAN = Colour.fromPackedInt32FormatRGBA(0xD2B48CFF);
    public static final Colour TEAL = Colour.fromPackedInt32FormatRGBA(0x008080FF);
    public static final Colour THISTLE = Colour.fromPackedInt32FormatRGBA(0xD8BFD8FF);
    public static final Colour TOMATO = Colour.fromPackedInt32FormatRGBA(0xFF6347FF);
    public static final Colour TURQUOISE = Colour.fromPackedInt32FormatRGBA(0x40E0D0FF);
    public static final Colour VIOLET = Colour.fromPackedInt32FormatRGBA(0xEE82EEFF);
    public static final Colour WHEAT = Colour.fromPackedInt32FormatRGBA(0xF5DEB3FF);
    public static final Colour WHITE_SMOKE = Colour.fromPackedInt32FormatRGBA(0xF5F5F5FF);
    public static final Colour YELLOW = Colour.fromPackedInt32FormatRGBA(0xFFFF00FF);
    public static final Colour YELLOW_GREEN = Colour.fromPackedInt32FormatRGBA(0x9ACD32FF);

    protected float mRed;
    protected float mGreen;
    protected float mBlue;
    protected float mAlpha;

    /**
     * <p>Constructor</p>
     */
    public Colour(Colour colour) {
        mRed = colour.mRed;
        mGreen = colour.mGreen;
        mBlue = colour.mBlue;
        mAlpha = colour.mAlpha;
    }

    /**
     * <p>Constructor</p>
     */
    public Colour(float red, float green, float blue, float alpha) {
        mRed = red;
        mGreen = green;
        mBlue = blue;
        mAlpha = alpha;
    }

    /**
     * <p>Get the red component of the colour</p>
     *
     * @return the red component of the colour
     */
    public float getRed() {
        return mRed;
    }

    /**
     * <p>Get the green component of the colour</p>
     *
     * @return the green component of the colour
     */
    public float getGreen() {
        return mGreen;
    }

    /**
     * <p>Get the blue component of the colour</p>
     *
     * @return the blue component of the colour
     */
    public float getBlue() {
        return mBlue;
    }

    /**
     * <p>Get the alpha component of the colour</p>
     *
     * @return the alpha component of the colour
     */
    public float getAlpha() {
        return mBlue;
    }

    /**
     * <p>Change the new component's value of the colour</p>
     *
     * @param red   the value of the red component
     * @param green the value of the green component
     * @param blue  the value of the blue component
     * @param alpha the value of the alpha component
     */
    public void set(float red, float green, float blue, float alpha) {
        mRed = red;
        mGreen = green;
        mBlue = blue;
        mAlpha = alpha;
    }

    /**
     * <p>Adds a scalar value to this colour</p>
     *
     * @param scalar the value to add to this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour add(float scalar) {
        return add(scalar, this);
    }

    /**
     * <p>Adds a scalar value to this colour</p>
     *
     * @param scalar the value to add to this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour add(float scalar, Colour result) {
        result.mRed = mRed + scalar;
        result.mGreen = mGreen + scalar;
        result.mBlue = mBlue + scalar;
        result.mAlpha = mAlpha + scalar;
        return result;
    }

    /**
     * <p>Adds the provided values to this colour</p>
     *
     * @param red   the value to add to the red component
     * @param green the value to add to the green component
     * @param blue  the value to add to the blue component
     * @param alpha the value to add to the alpha component
     *
     * @return a reference to <code>this</code>
     */
    public Colour add(float red, float green, float blue, float alpha) {
        return add(red, green, blue, alpha, this);
    }

    /**
     * <p>Adds the provided values to this colour</p>
     *
     * @param red    the value to add to the red component
     * @param green  the value to add to the green component
     * @param blue   the value to add to the blue component
     * @param alpha  the value to add to the alpha component
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour add(float red, float green, float blue, float alpha, Colour result) {
        result.mRed = mRed + red;
        result.mGreen = mGreen + green;
        result.mBlue = mBlue + blue;
        result.mAlpha = mAlpha + alpha;
        return result;
    }

    /**
     * <p>Adds a provided colour to this colour</p>
     *
     * @param colour the colour to add to this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour add(Colour colour) {
        return add(colour, this);
    }

    /**
     * <p>Adds a provided colour to this colour</p>
     *
     * @param colour the colour to add to this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour add(Colour colour, Colour result) {
        result.mRed = mRed + colour.mRed;
        result.mGreen = mGreen + colour.mGreen;
        result.mBlue = mBlue + colour.mBlue;
        result.mAlpha = mAlpha + colour.mAlpha;
        return result;
    }

    /**
     * <p>Subtracts a scalar value from this colour</p>
     *
     * @param scalar the value to subtract from this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour sub(float scalar) {
        return sub(scalar, this);
    }

    /**
     * <p>Subtracts a scalar value from this colour</p>
     *
     * @param scalar the value to subtract from this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour sub(float scalar, Colour result) {
        result.mRed = mRed - scalar;
        result.mGreen = mGreen - scalar;
        result.mBlue = mBlue - scalar;
        result.mAlpha = mAlpha - scalar;
        return result;
    }

    /**
     * <p>Subtracts the provided values from this colour</p>
     *
     * @param red   the value to subtract from the red component
     * @param green the value to subtract from the green component
     * @param blue  the value to subtract from the blue component
     * @param alpha the value to subtract from the alpha component
     *
     * @return a reference to <code>this</code>
     */
    public Colour sub(float red, float green, float blue, float alpha) {
        return sub(red, green, blue, alpha, this);
    }

    /**
     * <p>Subtracts the provided values from this colour</p>
     *
     * @param red    the value to subtract from the red component
     * @param green  the value to subtract from the green component
     * @param blue   the value to subtract from the blue component
     * @param alpha  the value to subtract from the alpha component
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour sub(float red, float green, float blue, float alpha, Colour result) {
        result.mRed = mRed - red;
        result.mGreen = mGreen - green;
        result.mBlue = mBlue - blue;
        result.mAlpha = mAlpha - alpha;
        return result;
    }

    /**
     * <p>Subtracts the provided colour from this colour</p>
     *
     * @param colour the colour to sub to this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour sub(Colour colour) {
        return sub(colour, this);
    }

    /**
     * <p>Subtracts the provided colour from this colour</p>
     *
     * @param colour the colour to sub to this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour sub(Colour colour, Colour result) {
        result.mRed = mRed - colour.mRed;
        result.mGreen = mGreen - colour.mGreen;
        result.mBlue = mBlue - colour.mBlue;
        result.mAlpha = mAlpha - colour.mAlpha;
        return result;
    }

    /**
     * <p>Multiplies a scalar value alpha with this colour</p>
     *
     * @param scalar the value to multiply green alpha with this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour mul(float scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Multiplies a scalar value alpha with this colour</p>
     *
     * @param scalar the value to multiply green alpha with this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour mul(float scalar, Colour result) {
        result.mRed = mRed * scalar;
        result.mGreen = mGreen * scalar;
        result.mBlue = mBlue * scalar;
        result.mAlpha = mAlpha * scalar;
        return result;
    }

    /**
     * <p>Multiplies the provided values with this colour</p>
     *
     * @param red   the value to multiply with the red component
     * @param green the value to multiply with the green component
     * @param blue  the value to multiply with the blue component
     * @param alpha the value to multiply with the alpha component
     *
     * @return a reference to <code>this</code>
     */
    public Colour mul(float red, float green, float blue, float alpha) {
        return mul(red, green, blue, alpha, this);
    }

    /**
     * <p>Multiplies the provided values with this colour</p>
     *
     * @param red    the value to multiply with the red component
     * @param green  the value to multiply with the green component
     * @param blue   the value to multiply with the blue component
     * @param alpha  the value to multiply with the alpha component
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour mul(float red, float green, float blue, float alpha, Colour result) {
        result.mRed = mRed * red;
        result.mGreen = mGreen * green;
        result.mBlue = mBlue * blue;
        result.mAlpha = mAlpha * alpha;
        return result;
    }

    /**
     * <p>Multiplies a provided colour with this colour</p>
     *
     * @param colour the colour to multiply with this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour mul(Colour colour) {
        return mul(colour, this);
    }

    /**
     * <p>Multiplies a provided colour with this colour</p>
     *
     * @param colour the colour to multiply with this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour mul(Colour colour, Colour result) {
        result.mRed = mRed * colour.mRed;
        result.mGreen = mGreen * colour.mGreen;
        result.mBlue = mBlue * colour.mBlue;
        result.mAlpha = mAlpha * colour.mAlpha;
        return result;
    }

    /**
     * <p>Divides a scalar value with this colour</p>
     *
     * @param scalar the value to divide with this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour div(float scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Divides a scalar value with this colour</p>
     *
     * @param scalar the value to divide with this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour div(float scalar, Colour result) {
        result.mRed = mRed / scalar;
        result.mGreen = mGreen / scalar;
        result.mBlue = mBlue / scalar;
        result.mAlpha = mAlpha / scalar;
        return result;
    }

    /**
     * <p>Divides the provided values with this colour</p>
     *
     * @param red   the value to divide with the red component
     * @param green the value to divide with the green component
     * @param blue  the value to divide with the blue component
     * @param alpha the value to divide with the alpha component
     *
     * @return a reference to <code>this</code>
     */
    public Colour div(float red, float green, float blue, float alpha) {
        return div(red, green, blue, alpha, this);
    }

    /**
     * <p>Divides the provided values with this colour</p>
     *
     * @param red    the value to divide with the red component
     * @param green  the value to divide with the green component
     * @param blue   the value to divide with the blue component
     * @param alpha  the value to divide with the alpha component
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour div(float red, float green, float blue, float alpha, Colour result) {
        result.mRed = mRed / red;
        result.mGreen = mGreen / green;
        result.mBlue = mBlue / blue;
        result.mAlpha = mAlpha / alpha;
        return result;
    }

    /**
     * <p>Divides a provided colour with this colour</p>
     *
     * @param colour the colour to divide with this colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour div(Colour colour) {
        return div(colour, this);
    }

    /**
     * <p>Divides a provided colour with this colour</p>
     *
     * @param colour the colour to divide with this colour
     * @param result the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour div(Colour colour, Colour result) {
        result.mRed = mRed / colour.mRed;
        result.mGreen = mGreen / colour.mGreen;
        result.mBlue = mBlue / colour.mBlue;
        result.mAlpha = mAlpha / colour.mAlpha;
        return result;
    }

    /**
     * <p>Interpolates this colour with another colour</p>
     *
     * @param destination the destination colour to interpolate towards
     * @param amount      the percentage change from the colour towards the destination colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour interpolate(Colour destination, float amount) {
        return interpolate(this, destination, amount, this);
    }

    /**
     * <p>Interpolates this colour with another colour</p>
     *
     * @param source      the source colour to interpolate from
     * @param destination the destination colour to interpolate towards
     * @param amount      the percentage change from the colour towards the destination colour
     *
     * @return a reference to <code>this</code>
     */
    public Colour interpolate(Colour source, Colour destination, float amount) {
        return interpolate(source, destination, amount, this);
    }

    /**
     * <p>Interpolates this colour with another colour</p>
     *
     * @param source      the source colour to interpolate from
     * @param destination the destination colour to interpolate towards
     * @param amount      the percentage change from the colour towards the destination colour
     * @param result      the colour's destination
     *
     * @return a reference to <code>result</code>
     */
    public Colour interpolate(Colour source, Colour destination, float amount, Colour result) {
        result.mRed = (1 - amount) * source.mRed + amount * destination.mRed;
        result.mGreen = (1 - amount) * source.mGreen + amount * destination.mGreen;
        result.mBlue = (1 - amount) * source.mBlue + amount * destination.mBlue;
        result.mAlpha = (1 - amount) * source.mAlpha + amount * destination.mAlpha;
        return result;
    }

    /**
     * <p>Pack the colour into a single precision floating point</p>
     *
     * @return the packed colour
     */
    public float toPackedFloat32FormatABGR() {
        return Float.intBitsToFloat(toPackedInt32FormatABGR() & 0xFEFFFFFF);
    }

    /**
     * <p>Pack the colour into a 32-bit integer</p>
     *
     * @return the packed colour
     */
    public int toPackedInt32FormatABGR() {
        return ((int) (mAlpha * 255) << 24) | ((int) (mBlue * 255) << 16) | ((int) (mGreen * 255) << 8) | ((int) (mRed * 255));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash = 37 * hash + Float.floatToIntBits(mRed);
        hash = 37 * hash + Float.floatToIntBits(mGreen);
        hash = 37 * hash + Float.floatToIntBits(mBlue);
        hash = 37 * hash + Float.floatToIntBits(mAlpha);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object colour) {
        if (!(colour instanceof Colour)) {
            return false;
        }
        Colour other = (Colour) colour;
        return Float.compare(mRed, other.mRed) == 0 && Float.compare(mGreen, other.mGreen) == 0 &&
                Float.compare(mBlue, other.mBlue) == 0 && Float.compare(mAlpha, other.mAlpha) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + mRed + ", " + mGreen + ", " + mBlue + ", " + mAlpha + "]";
    }

    /**
     * <p>Unpack a colour from a 32-bit integer</p>
     */
    public static Colour fromPackedInt32FormatRGBA(int colour) {
        final float r = ((colour & 0xFF000000) >>> 24) / 255.0F;
        final float g = ((colour & 0x00FF0000) >>> 16) / 255.0F;
        final float b = ((colour & 0x0000FF00) >>> 8) / 255.0F;
        final float a = ((colour & 0x000000FF)) / 255.0F;
        return new Colour(r, g, b, a);
    }
}
