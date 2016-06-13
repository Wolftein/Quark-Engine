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
package org.quark.render;

import org.quark.mathematic.MutableVector4i;
import org.quark.mathematic.Vector4i;

/**
 * <code>RenderState</code> contain(s) all render state(s) of {@link Render}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class RenderState {
    /**
     * Enumerate(s) possible blend mode(s).
     */
    public enum Blend {
        /**
         * Turn off.
         */
        NONE(Render.GLES2.GL_NONE, Render.GLES2.GL_NONE),

        /**
         * Result = source_colour + destination_colour.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ADD(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE),

        /**
         * Result = source_colour * destination_colour.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        MULTIPLY(Render.GLES2.GL_DST_COLOR, Render.GLES2.GL_ZERO),

        /**
         * Result = source_colour * destination_colour * 2.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        MULTIPLY_TWICE(Render.GLES2.GL_DST_COLOR, Render.GLES2.GL_SRC_COLOR),

        /**
         * Result = source_alpha * source_colour + (1 - source_alpha) * destination_colour.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ALPHA(Render.GLES2.GL_SRC_ALPHA, Render.GLES2.GL_ONE_MINUS_SRC_COLOR),

        /**
         * Result = (source_alpha * source_colour) + destination_colour.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ALPHA_ADD(Render.GLES2.GL_SRC_ALPHA, Render.GLES2.GL_ONE),

        /**
         * Result = source_colour + (destination_colour * 1 - source_alpha).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ALPHA_PRE_MULTIPLY(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE_MINUS_SRC_ALPHA),

        /**
         * Result = source_colour + (1 - source_colour) * destination_colour.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        COLOR(Render.GLES2.GL_ONE, Render.GLES2.GL_ONE_MINUS_SRC_COLOR),

        /**
         * Result = (source_colour * (1 - destination_colour)) + (destination_colour * (1 - source_colour)).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
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
     * Enumerate(s) possible blend equation(s).
     */
    public enum BlendEquation {
        /**
         * Result = source_rgba + destination_rgba.
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 2.0}
         */
        ADD(Render.GLES2.GL_FUNC_ADD),

        /**
         * Result = source_rgba - destination_rgba.
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 2.0}
         */
        SUBTRACT(Render.GLES2.GL_FUNC_SUBTRACT),

        /**
         * Result = destination_rgba - source_rgba.
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 2.0}
         */
        SUBTRACT_REVERSE(Render.GLES2.GL_FUNC_REVERSE_SUBTRACT),

        /**
         * Result = min(source_rgba, destination_rgba).
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 3.0}
         */
        MIN(Render.GLES3.GL_MIN),

        /**
         * Result = max(source_rgba, destination_rgba).
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 3.0}
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
     * Enumerate(s) possible cull operation(s).
     */
    public enum Cull {
        /**
         * Turn off.
         */
        NONE(Render.GLES2.GL_NONE),

        /**
         * Cull only front-face</p>
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        FRONT(Render.GLES2.GL_FRONT),

        /**
         * Cull front- and back- face</p>
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        FRONT_BACK(Render.GLES2.GL_FRONT_BACK),

        /**
         * Cull only back-face</p>
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
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
     * Enumerate(s) possible stencil operation(s).
     */
    public enum StencilOp {
        /**
         * Keeps the current value.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        KEEP(Render.GLES2.GL_KEEP),

        /**
         * Sets the stencil buffer value to 0.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ZERO(Render.GLES2.GL_ZERO),

        /**
         * Replace the stencil buffer value.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        REPLACE(Render.GLES2.GL_REPLACE),

        /**
         * Increments the current stencil buffer value. Clamps to the maximum representable unsigned value.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        INCREMENT(Render.GLES2.GL_INCREMENT),

        /**
         * Increments the current stencil buffer value. Wraps stencil buffer value to zero when incrementing
         * the maximum representable unsigned value.
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 2.0}
         */
        INCREMENT_WRAP(Render.GLES2.GL_INCREMENT_WRAP),

        /**
         * Decrements the current stencil buffer value. Clamps to 0.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        DECREASE(Render.GLES2.GL_DECREASE),

        /**
         * Decrements the current stencil buffer value. Wraps stencil buffer value to the maximum representable
         * unsigned value when decrementing a stencil buffer value of zero.
         * <p>
         * {@since OpenGL    1.4}
         * {@since OpenGL ES 2.0}
         */
        DECREASE_WRAP(Render.GLES2.GL_DECREASE_WRAP),

        /**
         * Bitwise inverts the current stencil buffer value.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
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
     * Enumerate(s) possible test operation(s).
     */
    public enum TestOp {
        /**
         * Always fails.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        NEVER(Render.GLES2.GL_NEVER),

        /**
         * Always passes.
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        ALWAYS(Render.GLES2.GL_ALWAYS),

        /**
         * Passes if ( ref & mask ) < ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        LESS(Render.GLES2.GL_LESS),

        /**
         * Passes if ( ref & mask ) <= ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        LESS_EQUAL(Render.GLES2.GL_LESS_EQUAL),

        /**
         * Passes if ( ref & mask ) > ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        GREATER(Render.GLES2.GL_GREATER),

        /**
         * Passes if ( ref & mask ) >= ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        GREATER_EQUAL(Render.GLES2.GL_GREATER_EQUAL),

        /**
         * Passes if ( ref & mask ) = ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
         */
        EQUAL(Render.GLES2.GL_EQUAL),

        /**
         * Passes if ( ref & mask ) != ( stencil & mask ).
         * <p>
         * {@since OpenGL    1.1}
         * {@since OpenGL ES 2.0}
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
     * Specifies how the red, green, blue, and alpha source blending factors are computed.
     *
     * @see Blend
     */
    private Blend mBlend = Blend.NONE;

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
    private boolean mRedMask = true, mGreenMask = true, mBlueMask = true, mAlphaMask = true;

    /**
     * Specifies whether front- or back-facing polygons are candidates for culling.
     *
     * @see Cull
     */
    private Cull mCull = Cull.BACK;

    /**
     * If <code>true</code>, discard fragments that are outside the scissor rectangle.
     */
    private boolean mScissor = false;

    /**
     * Specifies the scissor viewport.
     */
    private MutableVector4i mScissorViewport = MutableVector4i.zero();

    /**
     * If <code>true</code>, do depth comparisons and update the depth buffer.
     */
    private boolean mDepth = true;

    /**
     * Specifies whether the depth buffer is enabled for writing.
     */
    private boolean mDepthMask = true;

    /**
     * Specifies the depth comparison function.
     *
     * @see TestOp
     */
    private TestOp mDepthOp = TestOp.LESS;

    /**
     * If <code>true</code>, do stencil testing and update the stencil buffer.
     */
    private boolean mStencil = false;

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
     * @param mask <code>true</code> to mask all colour(s), <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setColourMask(boolean mask) {
        return setColourMask(mask, mask, mask, mask);
    }

    /**
     * <p>Change colour mask</p>
     *
     * @param red   <code>true</code> to mask red colour, <code>false</code> otherwise
     * @param green <code>true</code> to mask green colour, <code>false</code> otherwise
     * @param blue  <code>true</code> to mask blue colour, <code>false</code> otherwise
     * @param alpha <code>true</code> to mask alpha colour, <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setColourMask(boolean red, boolean green, boolean blue, boolean alpha) {
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
     * @param activate <code>true</code> to activate depth mode, <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepth(boolean activate) {
        mDepth = activate;
        return this;
    }

    /**
     * <p>Change depth mask</p>
     *
     * @param activate <code>true</code> to activate depth mask (writable), <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setDepthMask(boolean activate) {
        mDepthMask = activate;
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
     * @param activate <code>true</code> to activate scissor, <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setScissor(boolean activate) {
        mScissor = activate;
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
     * @param activate <code>true</code> to activate stencil mode, <code>false</code> otherwise
     *
     * @return <code>this</code> for chain operation(s)
     */
    public RenderState setStencil(boolean activate) {
        mStencil = activate;
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
     * <p>Check if red mask colour is activated</p>
     *
     * @return <code>true</code> if red mask colour is activated, <code>false</code> otherwise
     */
    public boolean isRedMask() {
        return mRedMask;
    }

    /**
     * <p>Check if green mask colour is activated</p>
     *
     * @return <code>true</code> if green mask colour is activated, <code>false</code> otherwise
     */
    public boolean isGreenMask() {
        return mGreenMask;
    }

    /**
     * <p>Check if blue mask colour is activated</p>
     *
     * @return <code>true</code> if blue mask colour is activated, <code>false</code> otherwise
     */
    public boolean isBlueMask() {
        return mBlueMask;
    }

    /**
     * <p>Check if alpha mask colour is activated</p>
     *
     * @return <code>true</code> if alpha mask colour is activated, <code>false</code> otherwise
     */
    public boolean isAlphaMask() {
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
     * <p>Check if depth is activated</p>
     *
     * @return <code>true</code> if depth is activated, <code>false</code> otherwise
     */
    public boolean isDepth() {
        return mDepth;
    }

    /**
     * <p>Check if depth mask (writable) is activated</p>
     *
     * @return <code>true</code> if depth mask (writable) is activated, <code>false</code> otherwise
     */
    public boolean isDepthMask() {
        return mDepthMask;
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
     * <p>Check if scissor is activated</p>
     *
     * @return <code>true</code> if scissor is activated, <code>false</code> otherwise
     */
    public boolean isScissor() {
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
     * <p>Check if stencil is activated</p>
     *
     * @return <code>true</code> if stencil is activated, <code>false</code> otherwise
     */
    public boolean isStencil() {
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
}