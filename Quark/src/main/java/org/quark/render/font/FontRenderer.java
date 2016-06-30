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
package org.quark.render.font;

import org.quark.mathematic.Colour;
import org.quark.render.texture.Texture;

/**
 * <code>FontRenderer</code> represent a renderer for {@link Font}.
 */
public interface FontRenderer {
    /**
     * <p>Render a {@link FontGlyph}</p>
     *
     * @param texture the texture
     * @param x       the x1 coordinate (in screen coordinates) of the glyph
     * @param y       the y1 coordinate (in screen coordinates) of the glyph
     * @param x2      the x2 coordinate (in screen coordinates) of the glyph
     * @param y2      the y2 coordinate (in screen coordinates) of the glyph
     * @param tx1     the x1 texture coordinate (normalised) of the glyph
     * @param ty1     the y1 texture coordinate (normalised) of the glyph
     * @param tx2     the x2 texture coordinate (normalised) of the glyph
     * @param ty2     the y2 texture coordinate (normalised) of the glyph
     * @param colour  the colour
     */
    void drawFontGlyph(Texture texture, float x, float y, float x2, float y2,
            float tx1, float ty1,
            float tx2, float ty2, Colour colour);
}
