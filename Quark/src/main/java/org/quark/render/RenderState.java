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
package org.quark.render;

import org.quark.mathematic.MutableVector2f;
import org.quark.mathematic.MutableVector4i;
import org.quark.mathematic.Vector2f;
import org.quark.mathematic.Vector4i;

/**
 * <code>RenderState</code> contain(s) all render state(s) of {@link Render}.
 */
public final class RenderState {
    /**
     * Enumerate flag mode(s).
     */
    public enum Flag {
        /**
         * Indicates the flag is enabled.
         */
        ENABLE,

        /**
         * Indicates the flag is disabled.
         */
        DISABLE,

        /**
         * Indicates the flag inherit.
         */
        INHERIT
    }

    /**
     * Enumerate(s) blend mode(s).
     */
    public enum Blend {
        /**
         * Turn off.
         */
        NONE(Render.GLES2.GL_NONE, Render.GLES2.GL_NONE),

        /**
         * Inherit.
         */
        INHERIT(Render.GLES2.GL_NONE, Render.GLES2.GL_NONE),

        /**
         * Result = source_colour + destination_colour.
         */
        ADD(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE),

        /**
         * Result = source_colour * destination_colour.
         */
        MULTIPLY(Render.GLES2.GL_DST_COLOR, Render.GLES2.GL_ZERO),

        /**
         * Result = source_colour * destination_colour * 2.
         */
        MULTIPLY_TWICE(Render.GLES2.GL_DST_COLOR, Render.GLES2.GL_SRC_COLOR),

        /**
         * Result = source_alpha * source_colour + (1 - source_alpha) * destination_colour.
         */
        ALPHA(Render.GLES2.GL_SRC_ALPHA, Render.GLES2.GL_ONE_MINUS_SRC_ALPHA),

        /**
         * Result = (source_alpha * source_colour) + destination_colour.
         */
        ALPHA_ADD(Render.GLES2.GL_SRC_ALPHA, Render.GLES2.GL_ONE),

        /**
         * Result = source_colour + (destination_colour * 1 - source_alpha).
         */
        ALPHA_PRE_MULTIPLY(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE_MINUS_SRC_ALPHA),

        /**
         * Result = source_colour + (1 - source_colour) * destination_colour.
         */
        COLOR(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE_MINUS_SRC_COLOR),

        /**
         * Result = (source_colour * (1 - destination_colour)) + (destination_colour * (1 - source_colour)).
         */
        COLOR_EXCLUSION(Render.GLES2.GL_ONE_MINUS_DST_COLOR, Render.GLES2.GL_ONE_MINUS_SRC_COLOR);

        public final int eSource;
        public final int eDestination;

        /**
         * <p>Constructor</p>
         */
        Blend(int source, int destination) {
            eSource = source;
            eDestination = destination;
        }
    }

    /**
     * Enumerate(s) blend equation(s).
     */
    public enum BlendEquation {
        /**
         * Result = source_rgba + destination_rgba.
         */
        ADD(Render.GLES2.GL_FUNC_ADD),

        /**
         * Result = source_rgba - destination_rgba.
         */
        SUBTRACT(Render.GLES2.GL_FUNC_SUBTRACT),

        /**
         * Result = destination_rgba - source_rgba.
         */
        SUBTRACT_REVERSE(Render.GLES2.GL_FUNC_REVERSE_SUBTRACT),

        /**
         * Result = min(source_rgba, destination_rgba).
         */
        MIN(Render.GLES3.GL_MIN),

        /**
         * Result = max(source_rgba, destination_rgba).
         */
        MAX(Render.GLES3.GL_MAX);

        public final int eValue;

        /**
         * <p>Constructor</p>
         */
        BlendEquation(int value) {
            eValue = value;
        }
    }

    /**
     * Enumerate(s) cull operation(s).
     */
    public enum Cull {
        /**
         * Turn off.
         */
        NONE(Render.GLES2.GL_NONE),

        /**
         * Inherit.
         */
        INHERIT(Render.GLES2.GL_NONE),

        /**
         * Cull only front-face.
         */
        FRONT(Render.GLES2.GL_FRONT),

        /**
         * Cull front- and back- face.
         */
        FRONT_BACK(Render.GLES2.GL_FRONT_BACK),

        /**
         * Cull only back-face.
         */
        BACK(Render.GLES2.GL_BACK);

        public final int eValue;

        /**
         * <p>Constructor</p>
         */
        Cull(int value) {
            eValue = value;
        }
    }

    /**
     * Enumerate(s) stencil operation(s).
     */
    public enum StencilOp {
        /**
         * Keeps the current value.
         */
        KEEP(Render.GLES2.GL_KEEP),

        /**
         * Sets the stencil buffer value to 0.
         */
        ZERO(Render.GLES2.GL_ZERO),

        /**
         * Replace the stencil buffer value.
         */
        REPLACE(Render.GLES2.GL_REPLACE),

        /**
         * Increments the current stencil buffer value. Clamps to the maximum representable unsigned value.
         */
        INCREMENT(Render.GLES2.GL_INCREMENT),

        /**
         * Increments the current stencil buffer value. Wraps stencil buffer value to zero when incrementing
         * the maximum representable unsigned value.
         */
        INCREMENT_WRAP(Render.GLES2.GL_INCREMENT_WRAP),

        /**
         * Decrements the current stencil buffer value. Clamps to 0.
         */
        DECREASE(Render.GLES2.GL_DECREASE),

        /**
         * Decrements the current stencil buffer value. Wraps stencil buffer value to the maximum representable
         * unsigned value when decrementing a stencil buffer value of zero.
         */
        DECREASE_WRAP(Render.GLES2.GL_DECREASE_WRAP),

        /**
         * Bitwise inverts the current stencil buffer value.
         */
        INVERT(Render.GLES2.GL_INVERT);

        public final int eValue;

        /**
         * <p>Constructor</p>
         */
        StencilOp(int value) {
            eValue = value;
        }
    }

    /**
     * Enumerate(s) test operation(s).
     */
    public enum TestOp {
        /**
         * Always fails.
         */
        NEVER(Render.GLES2.GL_NEVER),

        /**
         * Always passes.
         */
        ALWAYS(Render.GLES2.GL_ALWAYS),

        /**
         * Passes if reference is less than mask.
         */
        LESS(Render.GLES2.GL_LESS),

        /**
         * Passes if reference is less or equal than mask.
         */
        LESS_EQUAL(Render.GLES2.GL_LESS_EQUAL),

        /**
         * Passes if reference is greater than mask.
         */
        GREATER(Render.GLES2.GL_GREATER),

        /**
         * Passes iif reference is greater or equal than mask.
         */
        GREATER_EQUAL(Render.GLES2.GL_GREATER_EQUAL),

        /**
         * Passes if reference is equal than mask.
         */
        EQUAL(Render.GLES2.GL_EQUAL),

        /**
         * Passes if reference is not equal than mask.
         */
        NOT_EQUAL(Render.GLES2.GL_NOT_EQUAL);

        public final int eValue;

        /**
         * <p>Constructor</p>
         */
        TestOp(int value) {
            eValue = value;
        }
    }

    /**
     * Specifies whether do alpha to coverage sample.
     */
    private Flag mAlphaToCoverage = Flag.INHERIT;

    /**
     * Specifies how the red, green, blue, and alpha source blending factors are computed.
     *
     * @see Blend
     */
    private Blend mBlend = Blend.INHERIT;

    /**
     * Specifies the RGB blend equation, how the red, green, and blue components colors are combined.
     *
     * @see BlendEquation
     */
    private BlendEquation mBlendColourEquation = BlendEquation.ADD;

    /**
     * Specifies the alpha blend equation, how the alpha component of the source and destination colors are combined.
     *
     * @see BlendEquation
     */
    private BlendEquation mBlendAlphaEquation = BlendEquation.ADD;

    /**
     * Specifies whether red, green, blue, and alpha can or cannot be written into the frame buffer.
     */
    private Flag mRedMask = Flag.INHERIT, mGreenMask = Flag.INHERIT, mBlueMask = Flag.INHERIT, mAlphaMask = Flag.INHERIT;

    /**
     * Specifies whether front- or back-facing polygons are candidates for culling.
     *
     * @see Cull
     */
    private Cull mCull = Cull.INHERIT;

    /**
     * Specifies whether discard fragments that are outside the scissor rectangle.
     */
    private Flag mScissor = Flag.INHERIT;

    /**
     * Specifies the scissor viewport.
     */
    private MutableVector4i mScissorViewport = MutableVector4i.zero();

    /**
     * Specifies whether do depth comparisons and update the depth buffer.
     */
    private Flag mDepth = Flag.INHERIT;

    /**
     * Specifies whether the depth buffer is enabled for writing.
     */
    private Flag mDepthMask = Flag.INHERIT;

    /**
     * Specifies depth range.
     */
    private MutableVector2f mDepthRange = new MutableVector2f(0.0f, 1.0f);

    /**
     * Specifies the depth comparison function.
     *
     * @see TestOp
     */
    private TestOp mDepthOp = TestOp.LESS;

    /**
     * Specifies whether do stencil testing and update the stencil buffer.
     */
    private Flag mStencil = Flag.INHERIT;

    /**
     * Specifies the stencil test function for front-face.
     *
     * @see TestOp
     */
    private TestOp mStencilFrontOp = TestOp.ALWAYS;

    /**
     * Specifies the action to take when the stencil test fails.
     *
     * @see StencilOp
     */
    private StencilOp mStencilFrontFailOp = StencilOp.KEEP;

    /**
     * Specifies the stencil action when the stencil test passes, but the depth test fails.
     *
     * @see StencilOp
     */
    private StencilOp mStencilFrontDepthFailOp = StencilOp.KEEP;

    /**
     * Specifies the stencil action when both the stencil test and the depth test pass, or when the stencil
     * test passes and either there is no depth buffer or depth testing is not enabled.
     *
     * @see StencilOp
     */
    private StencilOp mStencilFrontDepthPassOp = StencilOp.KEEP;

    /**
     * Specifies the stencil test function.
     *
     * @see TestOp
     */
    private TestOp mStencilBackOp = TestOp.ALWAYS;

    /**
     * Specifies the action to take when the stencil test fails.
     *
     * @see StencilOp
     */
    private StencilOp mStencilBackFailOp = StencilOp.KEEP;

    /**
     * Specifies the stencil action when the stencil test passes, but the depth test fails.
     *
     * @see StencilOp
     */
    private StencilOp mStencilBackDepthFailOp = StencilOp.KEEP;

    /**
     * Specifies the stencil action when both the stencil test and the depth test pass, or when the stencil
     * test passes and either there is no depth buffer or depth testing is not enabled.
     *
     * @see StencilOp
     */
    private StencilOp mStencilBackDepthPassOp = StencilOp.KEEP;

    /**
     * <p>Merge the state(s) with other state(s)</p>
     *
     * @param other the other state to merge
     */
    public void merge(RenderState other) {
        mAlphaToCoverage = other.mAlphaToCoverage;
        mAlphaMask = other.mRedMask;
        mBlend = other.mBlend;
        mBlendAlphaEquation = other.mBlendAlphaEquation;
        mBlendColourEquation = other.mBlendColourEquation;
        mBlueMask = other.mRedMask;
        mCull = other.mCull;
        mDepth = other.mDepth;
        mDepthMask = other.mDepthMask;
        mDepthOp = other.mDepthOp;
        mDepthRange.set(other.mDepthRange);
        mGreenMask = other.mRedMask;
        mRedMask = other.mRedMask;
        mScissor = other.mScissor;
        mScissorViewport.set(other.mScissorViewport);
        mStencil = other.mStencil;
        mStencilBackOp = other.mStencilBackOp;
        mStencilBackFailOp = other.mStencilBackFailOp;
        mStencilBackDepthFailOp = other.mStencilBackDepthFailOp;
        mStencilBackDepthPassOp = other.mStencilBackDepthPassOp;
        mStencilFrontOp = other.mStencilFrontOp;
        mStencilFrontFailOp = other.mStencilFrontFailOp;
        mStencilFrontDepthFailOp = other.mStencilFrontDepthFailOp;
        mStencilFrontDepthPassOp = other.mStencilFrontDepthPassOp;
    }

    /**
     * <p>Change alpha to coverage mode</p>
     *
     * @param flag the new flag of the state
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setAlphaToCoverage(Flag flag) {
        mAlphaToCoverage = flag;
        return this;
    }

    /**
     * <p>Change blend mode</p>
     *
     * @param blend tne blend mode
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setBlend(Blend blend) {
        mBlend = blend;
        return this;
    }

    /**
     * <p>Change blend mode equation(s)</p>
     *
     * @param op tne new colour and alpha blend equation
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setBlendEquation(BlendEquation op) {
        return setBlendEquation(op, op);
    }

    /**
     * <p>Change blend mode equation(s)</p>
     *
     * @param colour tne new colour blend equation
     * @param alpha  tne new alpha blend equation
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setBlendEquation(BlendEquation colour, BlendEquation alpha) {
        mBlendColourEquation = colour;
        mBlendAlphaEquation = alpha;
        return this;
    }

    /**
     * <p>Change colour mask</p>
     *
     * @param mask the flag mask for all colour(s)
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setColourMask(Flag mask) {
        return setColourMask(mask, mask, mask, mask);
    }

    /**
     * <p>Change colour mask</p>
     *
     * @param red   the flag of the red colour
     * @param green the flag of the green colour
     * @param blue  the flag of the blue colour
     * @param alpha the flag of the alpha colour
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setColourMask(Flag red, Flag green, Flag blue, Flag alpha) {
        mRedMask = red;
        mGreenMask = green;
        mBlueMask = blue;
        mAlphaMask = alpha;
        return this;
    }

    /**
     * <p>Change cull face mode</p>
     *
     * @param op the new cull face mode
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setCullFace(Cull op) {
        mCull = op;
        return this;
    }

    /**
     * <p>Change depth mode</p>
     *
     * @param flag the new flag of the state
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepth(Flag flag) {
        mDepth = flag;
        return this;
    }

    /**
     * <p>Change depth mask</p>
     *
     * @param flag the new flag of the state
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepthMask(Flag flag) {
        mDepthMask = flag;
        return this;
    }

    /**
     * <p>Change depth range</p>
     *
     * @param range the new depth range
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepthRange(Vector2f range) {
        return setDepthRange(range.getX(), range.getY());
    }

    /**
     * <p>Change depth range</p>
     *
     * @param near the new near depth range
     * @param far  the new far depth range
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepthRange(float near, float far) {
        mDepthRange.setXY(near, far);
        return this;
    }

    /**
     * <p>Change depth operation</p>
     *
     * @param op the new depth operation
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepthOp(TestOp op) {
        mDepthOp = op;
        return this;
    }

    /**
     * <p>Change scissor mask</p>
     *
     * @param flag the new flag of the state
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setScissor(Flag flag) {
        mScissor = flag;
        return this;
    }

    /**
     * <p>Change scissor viewport</p>
     *
     * @param x1 the x1 coordinate (in screen coordinates) of the scissor test
     * @param y1 the y1 coordinate (in screen coordinates) of the scissor test
     * @param x2 the x2 coordinate (in screen coordinates) of the scissor test
     * @param y2 the y2 coordinate (in screen coordinates) of the scissor test
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setScissorViewport(int x1, int y1, int x2, int y2) {
        mScissorViewport.setXYZW(x1, y1, x2, y2);
        return this;
    }

    /**
     * <p>Change stencil mode</p>
     *
     * @param flag the new flag of the state
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencil(Flag flag) {
        mStencil = flag;
        return this;
    }

    /**
     * <p>Change stencil operation</p>
     *
     * @param op the new stencil operation (front-, back-face)
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilOp(TestOp op) {
        return setStencilOp(op, op);
    }

    /**
     * <p>Change stencil operation</p>
     *
     * @param front the new stencil operation (front face).
     * @param back  the new stencil operation (back face).
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilOp(TestOp front, TestOp back) {
        mStencilFrontOp = front;
        mStencilBackOp = back;
        return this;
    }

    /**
     * <p>Change stencil front operation(s)</p>
     *
     * @param op the new stencil front operation
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilFrontOp(StencilOp op) {
        return setStencilFrontOp(op, op, op);
    }

    /**
     * <p>Change stencil front operation(s)</p>
     *
     * @param sfail the new stencil operation (front-face stencil fail).
     * @param dfail the new stencil operation (front-face depth fail).
     * @param dpass the new stencil operation (front-face depth pass).
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilFrontOp(StencilOp sfail, StencilOp dfail, StencilOp dpass) {
        mStencilFrontFailOp = sfail;
        mStencilFrontDepthFailOp = dfail;
        mStencilFrontDepthPassOp = dpass;
        return this;
    }

    /**
     * <p>Change stencil back operation(s)</p>
     *
     * @param op the new stencil back operation
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilBackOp(StencilOp op) {
        return setStencilBackOp(op, op, op);
    }

    /**
     * <p>Change stencil back operation(s)</p>
     *
     * @param sfail the new stencil operation (back-face stencil fail).
     * @param dfail the new stencil operation (back-face depth fail).
     * @param dpass the new stencil operation (back-face depth pass).
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencilBackOp(StencilOp sfail, StencilOp dfail, StencilOp dpass) {
        mStencilBackFailOp = sfail;
        mStencilBackDepthFailOp = dfail;
        mStencilBackDepthPassOp = dpass;
        return this;
    }

    /**
     * <p>Get the alpha coverage flag</p>
     *
     * @return the alpha coverage flag
     */
    public Flag getAlphaToCoverage() {
        return mAlphaToCoverage;
    }

    /**
     * <p>Get the blend mode state</p>
     *
     * @return the blend mode state
     */
    public Blend getBlend() {
        return mBlend;
    }

    /**
     * <p>Get the blend equation for colour</p>
     *
     * @return the blend equation for colour
     */
    public BlendEquation getBlendEquationColour() {
        return mBlendColourEquation;
    }

    /**
     * <p>Get the blend equation for alpha</p>
     *
     * @return the blend equation for alpha
     */
    public BlendEquation getBlendEquationAlpha() {
        return mBlendAlphaEquation;
    }

    /**
     * <p>Get the red colour mask flag</p>
     *
     * @return the red colour mask flag
     */
    public Flag getRedMask() {
        return mRedMask;
    }

    /**
     * <p>Get the green colour mask flag</p>
     *
     * @return the green colour mask flag
     */
    public Flag getGreenMask() {
        return mGreenMask;
    }

    /**
     * <p>Get the blue colour mask flag</p>
     *
     * @return the blue colour mask flag
     */
    public Flag getBlueMask() {
        return mBlueMask;
    }

    /**
     * <p>Get the alpha colour mask flag</p>
     *
     * @return the alpha colour mask flag
     */
    public Flag getAlphaMask() {
        return mAlphaMask;
    }

    /**
     * <p>Get the cull face state</p>
     *
     * @return the cull face state
     */
    public Cull getCullFace() {
        return mCull;
    }

    /**
     * <p>Get the depth flag</p>
     *
     * @return the depth flag
     */
    public Flag getDepth() {
        return mDepth;
    }

    /**
     * <p>Get the depth mask flag</p>
     *
     * @return the depth mask flag
     */
    public Flag getDepthMask() {
        return mDepthMask;
    }

    /**
     * <p>Get the depth range</p>
     *
     * @return the depth range
     */
    public Vector2f getDepthRange() {
        return mDepthRange;
    }

    /**
     * <p>Get the depth test operation</p>
     *
     * @return the depth test operation
     */
    public TestOp getDepthOp() {
        return mDepthOp;
    }

    /**
     * <p>Get the scissor flag</p>
     *
     * @return the scissor flag
     */
    public Flag getScissor() {
        return mScissor;
    }

    /**
     * <p>Get the scissor test viewport</p>
     *
     * @return the scissor test viewport
     */
    public Vector4i getScissorViewport() {
        return mScissorViewport;
    }

    /**
     * <p>Get the stencil flag</p>
     *
     * @return the stencil flag
     */
    public Flag getStencil() {
        return mStencil;
    }

    /**
     * <p>Get the stencil front-face operation</p>
     *
     * @return the stencil front-face operation
     */
    public TestOp getStencilFrontOp() {
        return mStencilFrontOp;
    }

    /**
     * <p>Get the stencil front-face failure operation</p>
     *
     * @return the stencil front-face failure operation
     */
    public StencilOp getStencilFrontFailOp() {
        return mStencilFrontFailOp;
    }

    /**
     * <p>Get the depth front-face failure operation</p>
     *
     * @return the depth front-face failure operation
     */
    public StencilOp getStencilFrontDepthFailOp() {
        return mStencilFrontDepthFailOp;
    }

    /**
     * <p>Get the depth front-face pass operation</p>
     *
     * @return the depth front-face pass operation
     */
    public StencilOp getStencilFrontDepthPassOp() {
        return mStencilFrontDepthPassOp;
    }

    /**
     * <p>Get the stencil back-face operation</p>
     *
     * @return the stencil back-face operation
     */
    public TestOp getStencilBackOp() {
        return mStencilBackOp;
    }

    /**
     * <p>Get the stencil back-face failure operation</p>
     *
     * @return the stencil back-face failure operation
     */
    public StencilOp getStencilBackFailOp() {
        return mStencilBackFailOp;
    }

    /**
     * <p>Get the depth back-face failure operation</p>
     *
     * @return the depth back-face failure operation
     */
    public StencilOp getStencilBackDepthFailOp() {
        return mStencilBackDepthFailOp;
    }

    /**
     * <p>Get the depth back-face pass operation</p>
     *
     * @return the depth back-face pass operation
     */
    public StencilOp getStencilBackDepthPassOp() {
        return mStencilBackDepthPassOp;
    }

    /**
     * <p>Check whether the given {@link Flag} is dirty</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is dirty, <code>false</code> otherwise
     */
    public static boolean isFlagDirty(Flag source, Flag destination) {
        return (source != destination && source != Flag.INHERIT);
    }

    /**
     * <p>Check whether the given {@link Flag} is enabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is enabled, <code>false</code> otherwise
     */
    public static boolean isFlagEnabled(Flag source, Flag destination) {
        return ((source == Flag.INHERIT && destination != Flag.DISABLE) || source != Flag.DISABLE);
    }

    /**
     * <p>Check whether the given {@link Flag} is disabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is disabled, <code>false</code> otherwise
     */
    public static boolean isFlagDisabled(Flag source, Flag destination) {
        return (source == Flag.DISABLE || (source == Flag.INHERIT && destination == Flag.DISABLE));
    }

    /**
     * <p>Check whether the given {@link Blend} is dirty</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is dirty, <code>false</code> otherwise
     */
    public static boolean isFlagDirty(Blend source, Blend destination) {
        return (source != destination && source != Blend.INHERIT);
    }

    /**
     * <p>Check whether the given {@link Blend} is enabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is enabled, <code>false</code> otherwise
     */
    public static boolean isFlagEnabled(Blend source, Blend destination) {
        return ((source == Blend.INHERIT && destination != Blend.NONE) || source != Blend.NONE);
    }

    /**
     * <p>Check whether the given {@link Blend} is disabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is disabled, <code>false</code> otherwise
     */
    public static boolean isFlagDisabled(Blend source, Blend destination) {
        return (source == Blend.NONE || (source == Blend.INHERIT && destination == Blend.NONE));
    }

    /**
     * <p>Check whether the given {@link Cull} is dirty</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is dirty, <code>false</code> otherwise
     */
    public static boolean isFlagDirty(Cull source, Cull destination) {
        return (source != destination && source != Cull.INHERIT);
    }

    /**
     * <p>Check whether the given {@link Cull} is enabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is enabled, <code>false</code> otherwise
     */
    public static boolean isFlagEnabled(Cull source, Cull destination) {
        return ((source == Cull.INHERIT && destination != Cull.NONE) || source != Cull.NONE);
    }

    /**
     * <p>Check whether the given {@link Cull} is disabled</p>
     *
     * @param source      the source
     * @param destination the destination
     *
     * @return <code>true</code> if the flag is disabled, <code>false</code> otherwise
     */
    public static boolean isFlagDisabled(Cull source, Cull destination) {
        return (source == Cull.NONE || (source == Cull.INHERIT && destination == Cull.NONE));
    }
}