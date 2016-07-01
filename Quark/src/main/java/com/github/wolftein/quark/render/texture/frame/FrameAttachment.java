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
package com.github.wolftein.quark.render.texture.frame;

import com.github.wolftein.quark.render.Render;

/**
 * <code>FrameAttachment</code> enumerate {@link Frame} attachment(s).
 */
public enum FrameAttachment {
    /**
     * Enumerates all color attachment(s)
     * <p>
     * NOTE: Not all attachment(s) may be supported by the underlying context.
     */
    COLOR0(Render.GLES2.GL_COLOR_ATTACHMENT0, true, false, false),
    COLOR1(Render.GLES2.GL_COLOR_ATTACHMENT0 + 1, true, false, false),
    COLOR2(Render.GLES2.GL_COLOR_ATTACHMENT0 + 2, true, false, false),
    COLOR3(Render.GLES2.GL_COLOR_ATTACHMENT0 + 3, true, false, false),
    COLOR4(Render.GLES2.GL_COLOR_ATTACHMENT0 + 4, true, false, false),
    COLOR5(Render.GLES2.GL_COLOR_ATTACHMENT0 + 5, true, false, false),
    COLOR6(Render.GLES2.GL_COLOR_ATTACHMENT0 + 6, true, false, false),
    COLOR7(Render.GLES2.GL_COLOR_ATTACHMENT0 + 7, true, false, false),
    COLOR8(Render.GLES2.GL_COLOR_ATTACHMENT0 + 8, true, false, false),
    COLOR9(Render.GLES2.GL_COLOR_ATTACHMENT0 + 9, true, false, false),
    COLOR10(Render.GLES2.GL_COLOR_ATTACHMENT0 + 10, true, false, false),
    COLOR11(Render.GLES2.GL_COLOR_ATTACHMENT0 + 11, true, false, false),
    COLOR12(Render.GLES2.GL_COLOR_ATTACHMENT0 + 12, true, false, false),
    COLOR13(Render.GLES2.GL_COLOR_ATTACHMENT0 + 13, true, false, false),
    COLOR14(Render.GLES2.GL_COLOR_ATTACHMENT0 + 14, true, false, false),
    COLOR15(Render.GLES2.GL_COLOR_ATTACHMENT0 + 15, true, false, false),
    COLOR16(Render.GLES2.GL_COLOR_ATTACHMENT0 + 16, true, false, false),
    COLOR17(Render.GLES2.GL_COLOR_ATTACHMENT0 + 17, true, false, false),
    COLOR18(Render.GLES2.GL_COLOR_ATTACHMENT0 + 18, true, false, false),
    COLOR19(Render.GLES2.GL_COLOR_ATTACHMENT0 + 19, true, false, false),
    COLOR20(Render.GLES2.GL_COLOR_ATTACHMENT0 + 20, true, false, false),
    COLOR21(Render.GLES2.GL_COLOR_ATTACHMENT0 + 21, true, false, false),
    COLOR22(Render.GLES2.GL_COLOR_ATTACHMENT0 + 22, true, false, false),
    COLOR23(Render.GLES2.GL_COLOR_ATTACHMENT0 + 23, true, false, false),
    COLOR24(Render.GLES2.GL_COLOR_ATTACHMENT0 + 24, true, false, false),
    COLOR25(Render.GLES2.GL_COLOR_ATTACHMENT0 + 25, true, false, false),
    COLOR26(Render.GLES2.GL_COLOR_ATTACHMENT0 + 26, true, false, false),
    COLOR27(Render.GLES2.GL_COLOR_ATTACHMENT0 + 27, true, false, false),
    COLOR28(Render.GLES2.GL_COLOR_ATTACHMENT0 + 28, true, false, false),
    COLOR29(Render.GLES2.GL_COLOR_ATTACHMENT0 + 29, true, false, false),
    COLOR30(Render.GLES2.GL_COLOR_ATTACHMENT0 + 30, true, false, false),
    COLOR31(Render.GLES2.GL_COLOR_ATTACHMENT0 + 31, true, false, false),

    /**
     * Enumerates the depth attachment (for depth buffer)
     */
    DEPTH(Render.GLES2.GL_DEPTH_ATTACHMENT, false, true, false),

    /**
     * Enumerates the stencil attachment (for stencil buffer)
     */
    STENCIL(Render.GLES2.GL_STENCIL_ATTACHMENT, false, false, true);

    public final int eValue;
    public final boolean eColour;
    public final boolean eDepth;
    public final boolean eStencil;

    /**
     * <p>Constructor</p>
     */
    FrameAttachment(int value, boolean colour, boolean depth, boolean stencil) {
        eValue = value;
        eColour = colour;
        eDepth = depth;
        eStencil = stencil;
    }
}
