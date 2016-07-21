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
package ar.com.quark.graphic.texture.frame;

import ar.com.quark.graphic.Graphic;
import ar.com.quark.graphic.texture.Texture;
import ar.com.quark.graphic.texture.TextureFormat;
import ar.com.quark.utility.Disposable;
import ar.com.quark.utility.Manageable;
import org.eclipse.collections.api.map.ImmutableMap;

import static ar.com.quark.Quark.QKGraphic;

/**
 * <code>Frame</code> is a collection of buffers that can be used as the destination for rendering.
 */
public final class Frame extends Manageable implements Disposable {
    private final ImmutableMap<FrameAttachment, Target> mAttachment;
    private final int mWidth;
    private final int mHeight;
    private final int mSamples;

    /**
     * <p>Constructor</p>
     */
    public Frame(ImmutableMap<FrameAttachment, Target> attachment, int width, int height, int samples) {
        mAttachment = attachment;
        mWidth = width;
        mHeight = height;
        mSamples = Math.min(samples, 1);
    }

    /**
     * <p>Get all attachment(s)</p>
     *
     * @return all attachment(s)
     */
    public ImmutableMap<FrameAttachment, Target> getAttachment() {
        return mAttachment;
    }

    /**
     * <p>Get the given attachment</p>
     *
     * @param type the type of the attachment
     *
     * @return the given attachment if valid, <code>null</code> otherwise
     */
    public <T extends Target> T getAttachment(FrameAttachment type) {
        return (T) mAttachment.get(type);
    }

    /**
     * <p>Check if the buffer have the given attachment</p>
     *
     * @return <code>true</code> if the buffer have the given attachment, <code>false</code> otherwise
     */
    public boolean hasAttachment(FrameAttachment type) {
        return mAttachment.containsKey(type);
    }

    /**
     * <p>Check if the buffer have colour attachment</p>
     *
     * @return <code>true</code> if the buffer have colour attachment, <code>false</code> otherwise
     */
    public boolean hasColour() {
        return hasAttachment(FrameAttachment.COLOR0);
    }

    /**
     * <p>Check if the buffer have depth attachment</p>
     *
     * @return <code>true</code> if the buffer have depth attachment, <code>false</code> otherwise
     */
    public boolean hasDepth() {
        return hasAttachment(FrameAttachment.DEPTH);
    }

    /**
     * <p>Check if the buffer have stencil attachment</p>
     *
     * @return <code>true</code> if the buffer have stencil attachment, <code>false</code> otherwise
     */
    public boolean hasStencil() {
        return hasAttachment(FrameAttachment.STENCIL);
    }

    /**
     * <p>Get the width of the frame buffer</p>
     *
     * @return the width of the frame buffer
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * <p>Get the height of the frame buffer</p>
     *
     * @return the height of the frame buffer
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * <p>Get the samples of the frame buffer</p>
     *
     * @return the samples of the frame buffer
     */
    public int getSamples() {
        return mSamples;
    }

    /**
     * @see Graphic#colour(float, float, float, float)
     */
    public void colour(float red, float green, float blue, float alpha) {
        QKGraphic.colour(red, green, blue, alpha);
    }

    /**
     * @see Graphic#clear(boolean, boolean, boolean)
     */
    public void clear(boolean colour, boolean depth, boolean stencil) {
        QKGraphic.clear(colour, depth, stencil);
    }

    /**
     * @see Graphic#clear(boolean, boolean, boolean)
     */
    public void clear() {
        QKGraphic.clear(hasColour(), hasDepth(), hasStencil());
    }

    /**
     * @see Graphic#viewport(int, int, int, int)
     */
    public void viewport(int x, int y, int width, int height) {
        QKGraphic.viewport(x, y, width, height);
    }

    /**
     * @see Graphic#viewport(int, int, int, int)
     */
    public void viewport() {
        QKGraphic.viewport(0, 0, mWidth, mHeight);
    }

    /**
     * @see Graphic#create(Frame)
     */
    public void create() {
        QKGraphic.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        QKGraphic.delete(this);
    }

    /**
     * @see Graphic#acquire(Frame)
     */
    public void acquire() {
        QKGraphic.acquire(this);
    }

    /**
     * @see Graphic#release(Frame)
     */
    public void release() {
        QKGraphic.release(this);
    }

    /**
     * @see Disposable#dispose()
     * <p>
     * NOTE: Safe implementation of {@linkplain #delete()}
     */
    @Override
    public void dispose() {
        QKGraphic.dispose(this);
    }

    /**
     * <code>Target</code> encapsulate an attachment for {@link Frame}.
     */
    public interface Target {
        /**
         * <p>Check if the data is a texture</p>
         *
         * @return <code>true</code> if the data is a texture, <code>false</code> otherwise
         */
        boolean isTexture();
    }

    /**
     * <code>TextureTarget</code> encapsulate a {@link Target} using a texture.
     */
    public final static class TextureTarget implements Target {
        private final Texture mTexture;

        /**
         * <p>Constructor</p>
         */
        public TextureTarget(Texture texture) {
            this.mTexture = texture;
        }

        /**
         * <p>Get the texture of the render target</p>
         *
         * @return the texture of the render target
         */
        public Texture getTexture() {
            return mTexture;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isTexture() {
            return true;
        }
    }

    /**
     * <code>RenderTarget</code> encapsulate a {@link Target} using a render buffer.
     */
    public final static class RenderTarget extends Manageable implements Target {
        private final TextureFormat mFormat;

        /**
         * <p>Constructor</p>
         */
        public RenderTarget(TextureFormat format) {
            this.mFormat = format;
        }

        /**
         * <p>Get the format of the render target</p>
         *
         * @return the format of the render target
         */
        public TextureFormat getFormat() {
            return mFormat;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isTexture() {
            return false;
        }
    }
}
