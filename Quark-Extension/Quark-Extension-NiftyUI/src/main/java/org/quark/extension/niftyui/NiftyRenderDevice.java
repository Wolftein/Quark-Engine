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

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.MouseCursor;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.quark.mathematic.Colour;
import org.quark.mathematic.ImmutableMatrix4f;
import org.quark.render.RenderState;
import org.quark.render.font.Font;
import org.quark.render.shader.AttributeType;
import org.quark.render.shader.Shader;
import org.quark.render.shader.ShaderParser;
import org.quark.render.shader.UniformType;
import org.quark.render.shader.data.UniformMatrix4;
import org.quark.render.texture.Image;
import org.quark.render.texture.Texture;
import org.quark.render.texture.TextureFilter;
import org.quark.render.texture.TextureFormat;
import org.quark.resource.AssetManager;

import java.io.IOException;

import static org.quark.Quark.*;

/**
 * <code>NiftyRenderDevice</code> represent implementation of {@link RenderDevice}.
 */
public final class NiftyRenderDevice implements RenderDevice {
    /**
     * Hold the renderer of the device.
     */
    private final NiftyRender mRender = new NiftyRender(500);

    /**
     * Hold all state(s) being used by the device.
     */
    private final RenderState mRenderState = new RenderState().setDepth(RenderState.Flag.DISABLE);

    /**
     * Hold the shader of the device.
     */
    private final Shader mShader;

    /**
     * Hold the {@link Texture} for primitive(s).
     */
    private final Texture mEmptyTexture;

    /**
     * Hold the {@link Colour} for font(s).
     */
    private final Colour mColour = new Colour(0.0F, 0.0F, 0.0F, 0.0F);

    /**
     * Hold the current cursor of the device.
     */
    private NiftyCursor mCursor;

    /**
     * <p>Constructor</p>
     */
    public NiftyRenderDevice() {
        //!
        //! Build a shader for the executing model.
        //!
        mShader = buildShader();
        mShader.create();

        //!
        //! Initialise the render for the first time.
        //!
        mRender.initialise();

        //!
        //! Load the texture for rendering geometry (without texture).
        //!
        mEmptyTexture = QKResources.loadAsset("Data/NiftyUI/Texture/Empty.png",
                new Texture.Descriptor(TextureFormat.RGBA8, TextureFilter.POINT));
    }

    /**
     * <p>Resize the device</p>
     */
    public void resize(int width, int height) {
        mShader.<UniformMatrix4>getUniform("uProjectionView")
                .setValue(ImmutableMatrix4f.createOrthographic(0, width, height, 0, -1, 1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
        //!
        //! NOTE: No need to implement this method.
        //!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderImage createImage(String filename, boolean filter) {
        return new NiftyRenderImage(QKResources.loadAsset(filename,
                new Texture.Descriptor(TextureFormat.RGBA8, filter ? TextureFilter.BILINEAR : TextureFilter.POINT)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderFont createFont(String filename) {
        return new NiftyRenderFont(QKResources.loadAsset(filename, AssetManager.DEFAULT_CACHEABLE_DESCRIPTOR));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return QKDisplay.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return QKDisplay.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginFrame() {
        mShader.acquire();
        mShader.update();

        //!
        //! NOTE: Initialise the batch.
        //!
        mRender.begin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endFrame() {
        //!
        //! NOTE: Release the batch.
        //!
        mRender.end();

        mShader.release();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        QKRender.clear(true, false, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlendMode(BlendMode renderMode) {
        if (renderMode == BlendMode.BLEND) {
            mRenderState.setBlend(RenderState.Blend.ALPHA);
        } else if (renderMode == BlendMode.MULIPLY) {
            mRenderState.setBlend(RenderState.Blend.MULTIPLY);
        } else {
            mRenderState.setBlend(RenderState.Blend.NONE);
        }
        QKRender.apply(mRenderState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderQuad(int x, int y, int width, int height, Color color) {
        renderQuad(x, y, width, height, color, color, color, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderQuad(int x, int y, int width, int height, Color c0, Color c1, Color c2, Color c3) {
        mRender.draw(mEmptyTexture, x, y, width, height, 0.0f, 0.0f, 0.0f, 0.0f, c0, c1, c2, c3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderImage(RenderImage image, int x, int y, int width, int height, Color color, float scale) {
        final Texture texture = ((NiftyRenderImage) image).getTexture();

        final float x0 = x + 0.5f * width * (1.0f - scale);
        final float y0 = y + 0.5f * height * (1.0f - scale);

        mRender.draw(texture, x0, y0, width * scale, height * scale, 0.0f, 0.0f, 1.0f, 1.0f, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderImage(RenderImage image, int x, int y, int w, int h, int srcX, int srcY, int srcW, int srcH,
            Color color, float scale, int centerX, int centerY) {
        final Texture internal = ((NiftyRenderImage) image).getTexture();

        //!
        //! Calculate offset of the image.
        //!
        final Image data = internal.getImage();

        final float tx1 = (float) srcX / data.getWidth();
        final float ty1 = (float) srcY / data.getHeight();
        final float tx2 = tx1 + (float) srcW / data.getWidth();
        final float ty2 = ty1 + (float) srcH / data.getHeight();

        final float x0 = centerX + (x - centerX) * scale;
        final float y0 = centerY + (y - centerY) * scale;

        mRender.draw(internal, x0, y0, w * scale, h * scale, tx1, ty1, tx2, ty2, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderFont(RenderFont font, String text, int x, int y, Color color, float sizeX, float sizeY) {
        final Font internal = ((NiftyRenderFont) font).getFont();

        mColour.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        internal.render(mRender, text, x, y, sizeX, sizeY, mColour);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableClip(int x0, int y0, int x1, int y1) {
        mRender.enableClipping(x0, y0, x1, y1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableClip() {
        mRender.disableClipping(getWidth(), getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseCursor createMouseCursor(String filename, int hotspotX, int hotspotY) throws IOException {
        return new NiftyCursor(QKResources.loadAsset(filename,
                new Texture.Descriptor(TextureFormat.RGBA8, TextureFilter.BILINEAR)), hotspotX, hotspotY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableMouseCursor(MouseCursor mouseCursor) {
        mCursor = (NiftyCursor) mouseCursor;
        mCursor.enable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableMouseCursor() {
        mCursor.disable();
    }

    /**
     * <p>Allocate the shader for the device</p>
     */
    private Shader buildShader() {
        final ShaderParser parser = new ShaderParser(QKRender.getCapabilities());

        //!
        //! Build vertex-stage pass.
        //!
        final ShaderParser.VertexBuilder vertex = parser.vertex(ShaderParser.Precision.MEDIUM);
        {
            vertex.input(
                    0, "inPosition", AttributeType.Float2);
            vertex.input(
                    1, "inColour", AttributeType.Float4);
            vertex.input(
                    2, "inTexture", AttributeType.Float2);
            vertex.output(
                    0, "vyColour", AttributeType.Float4);
            vertex.output(
                    1, "vyTexture", AttributeType.Float2);
            vertex.uniform(
                    0, "uProjectionView", UniformType.Matrix4x4);
            vertex.code("void main() {\n" +
                    "   vyColour    = inColour;\n" +
                    "   vyTexture   = inTexture;\n" +
                    "   gl_Position = uProjectionView * vec4(inPosition, 0.0, 1.0);\n" +
                    "}");
        }
        vertex.build();

        //!
        //! Build fragment-stage pass.
        //!
        final ShaderParser.FragmentBuilder fragment = parser.fragment(ShaderParser.Precision.MEDIUM);
        {
            fragment.input(
                    0, "vyTexture", AttributeType.Float2);
            fragment.input(
                    1, "vyColour", AttributeType.Float4);
            fragment.uniform(
                    1, "uTexture0", UniformType.Sampler2D);
            fragment.output(
                    0, "outColor", AttributeType.Float4, ShaderParser.Precision.MEDIUM);
            fragment.code("void main() {\n" +
                    "    outColor = texture2D(uTexture0, vyTexture) * vyColour;\n" +
                    "}");
        }
        return fragment.build().generate();
    }
}
