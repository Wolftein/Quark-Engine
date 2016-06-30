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

import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.tools.Color;
import org.quark.mathematic.Colour;
import org.quark.render.font.FontRenderer;
import org.quark.render.storage.*;
import org.quark.render.storage.factory.FactoryArrayStorage;
import org.quark.render.storage.factory.FactoryElementStorage;
import org.quark.render.texture.Texture;
import org.quark.system.utility.Manageable;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.UInt16Array;

import static org.quark.Quark.QKRender;

/**
 * <code>NiftyRender</code> encapsulate the batch renderer for {@link NiftyRenderDevice}.
 */
public final class NiftyRender extends Mesh implements FontRenderer {
    /**
     * Hold the storage that contains the {@linkplain #mVertices}.
     */
    private final FactoryArrayStorage.Float32 mStorage;

    /**
     * Hold a buffer being mapped to the GPU that hold(s) all vertices of the renderer..
     */
    private Float32Array mVertices;

    /**
     * Hold the current number of primitives in the batch.
     */
    private int mCount = 0;

    /**
     * Hold the clipping of the renderer.
     */
    private boolean mClip;

    /**
     * Hold the clipping of the renderer.
     */
    private int mClipX1, mClipY1, mClipX2, mClipY2;

    /**
     * <p>Constructor</p>
     *
     * @param count the max number of sprite(s) being batched at once.
     */
    public NiftyRender(int count) {
        super(
                new FactoryArrayStorage.Float32(StorageType.CLIENT, StorageMode.STREAM_DRAW, 4 * 4 * count,
                        new Vertex.Builder()
                                .add(0, 2, VertexFormat.FLOAT)
                                .add(1, 4, VertexFormat.UNSIGNED_BYTE, true)
                                .add(2, 2, VertexFormat.FLOAT).build()),
                new FactoryElementStorage.UInt16(StorageType.SERVER, StorageMode.STATIC_DRAW, 6 * count));
        mStorage = (FactoryArrayStorage.Float32) getVertices(0);
    }

    /**
     * <p>Initialise the renderer</p>
     */
    public void initialise() {
        //!
        //! Allocate the indices storage.
        //!
        final UInt16Array indices = (UInt16Array) getIndices().map();
        for (short i = 0, j = 0; i < indices.capacity(); i += 6 * 2, j += 4) {
            indices.write(j);
            indices.write(j + 1);
            indices.write(j + 2);
            indices.write(j + 2);
            indices.write(j + 3);
            indices.write(j);
        }
        getIndices().unmap();

        //!
        //! Allocate the descriptor.
        //!
        mDescriptor.create();
    }

    /**
     * @see RenderDevice#enableClip(int, int, int, int)
     */
    public void enableClipping(int x1, int y1, int x2, int y2) {
        mClip = true;
        mClipX1 = x1;
        mClipX2 = x2;
        mClipY1 = y1;
        mClipY2 = y2;
    }

    /**
     * @see RenderDevice#disableClip()
     */
    public void disableClipping(int x2, int y2) {
        mClip = false;
    }

    /**
     * <p>Draw a texture</p>
     */
    public void draw(Texture texture,
            float x1, float y1,
            float x2, float y2,
            float tx1, float ty1,
            float tx2, float ty2, Color color) {
        final float c0 = Float.intBitsToFloat((int) (color.getAlpha() * 255) << 24
                | (int) (color.getBlue() * 255) << 16
                | (int) (color.getGreen() * 255) << 8
                | (int) (color.getRed() * 255));

        draw(texture, x1, y1, x2, y2, tx1, ty1, tx2, ty2, c0, c0, c0, c0);
    }

    /**
     * <p>Draw a texture</p>
     */
    public void draw(Texture texture,
            float x1, float y1,
            float x2, float y2,
            float tx1, float ty1,
            float tx2, float ty2, Color color0, Color color1, Color color2, Color color3) {
        final float c0 = Float.intBitsToFloat((int) (color0.getAlpha() * 255) << 24
                | (int) (color0.getBlue() * 255) << 16
                | (int) (color0.getGreen() * 255) << 8
                | (int) (color0.getRed() * 255));
        final float c1 = Float.intBitsToFloat((int) (color1.getAlpha() * 255) << 24
                | (int) (color1.getBlue() * 255) << 16
                | (int) (color1.getGreen() * 255) << 8
                | (int) (color1.getRed() * 255));
        final float c2 = Float.intBitsToFloat((int) (color2.getAlpha() * 255) << 24
                | (int) (color2.getBlue() * 255) << 16
                | (int) (color2.getGreen() * 255) << 8
                | (int) (color2.getRed() * 255));
        final float c3 = Float.intBitsToFloat((int) (color3.getAlpha() * 255) << 24
                | (int) (color3.getBlue() * 255) << 16
                | (int) (color3.getGreen() * 255) << 8
                | (int) (color3.getRed() * 255));

        draw(texture, x1, y1, x2, y2, tx1, ty1, tx2, ty2, c0, c1, c2, c3);
    }

    /**
     * <p>Draw a texture</p>
     */
    public void draw(Texture texture, float x1, float y1, float x2, float y2,
            float tx1, float ty1,
            float tx2, float ty2,
            float c1, float c2,
            float c3, float c4) {
        final float x3 = x1 + x2;
        final float y3 = y1 + y2;

        //!
        //! Handle clipping of the geometry.
        //!
        final float cX1, cY1, cX2, cY2;
        final float cTX1, cTY1, cTX2, cTY2;

        if (!mClip) {
            //!
            //! The geometry is not clipped.
            //!
            cX1 = x1;
            cY1 = y1;
            cX2 = x3;
            cY2 = y3;
            cTX1 = tx1;
            cTY1 = ty1;
            cTX2 = tx2;
            cTY2 = ty2;
        } else if ((x1 > mClipX2) || (x3 < mClipX1) || (y1 > mClipY2) || (y3 < mClipY1)) {
            //!
            //! The geometry is completely outside the clipping range.
            //!
            return;
        } else if (x1 >= mClipX1 && x1 <= mClipX2 &&
                x3 >= mClipX1 && x3 <= mClipX2 &&
                y1 >= mClipY1 && y1 <= mClipY2 &&
                y3 >= mClipY1 && y3 <= mClipY2) {
            //!
            //! The geometry is completely inside the clipping range.
            //!
            cX1 = x1;
            cY1 = y1;
            cX2 = x3;
            cY2 = y3;
            cTX1 = tx1;
            cTY1 = ty1;
            cTX2 = tx2;
            cTY2 = ty2;
        } else {
            //!
            //! The geometry is partially inside the clipping range.
            //!
            cX1 = (x1 >= mClipX1 ? x1 : mClipX1);
            cY1 = (y1 >= mClipY1 ? y1 : mClipY1);
            cX2 = (x3 <= mClipX2 ? x3 : mClipX2);
            cY2 = (y3 <= mClipY2 ? y3 : mClipY2);
            cTX1 = (cX1 == x1 ? tx1 : ((cX1 * tx1) / x1));
            cTY1 = (cY1 == y1 ? ty1 : ((cY1 * ty1) / y1));
            cTX2 = (cX2 == x3 ? tx2 : ((cX2 * tx2) / x3));
            cTY2 = (cY2 == y3 ? ty2 : ((cY2 * ty2) / y3));
        }

        //!
        //! Check whenever the batch need to change context.
        //!
        flushIfRequired(texture);

        mVertices.write(cX1).write(cY1).write(c1).write(cTX1).write(cTY1);
        mVertices.write(cX1).write(cY2).write(c2).write(cTX1).write(cTY2);
        mVertices.write(cX2).write(cY2).write(c3).write(cTX2).write(cTY2);
        mVertices.write(cX2).write(cY1).write(c4).write(cTX2).write(cTY1);
        mCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawFontGlyph(Texture texture,
            float x1, float y1,
            float x2, float y2,
            float tx1, float ty1,
            float tx2, float ty2, Colour colour) {
        final float c0 = colour.toPackedFloat32FormatABGR();

        draw(texture, x1, y1, x2, y2, tx1, ty1, tx2, ty2, c0, c0, c0, c0);
    }

    /**
     * <p>Begin the batch</p>
     * <p>
     * NOTE: This method should be called before starting to batching.
     */
    public void begin() {
        mDescriptor.acquire();

        //!
        //! Acquire a pointer to the vertices of the mesh.
        //!
        mStorage.acquire();

        mVertices = mStorage.map();
    }

    /**
     * <p>Finalise the batch</p>
     * <p>
     * NOTE: This method should be called after finishing batching.
     */
    public void end() {
        //!
        //! Flush all vertices before ending the batch.
        //!
        flush(false);

        mDescriptor.release();
    }

    /**
     * <p>Flush the batch</p>
     *
     * @param restore <code>true</code> if the render should be restore after, <code>false</code> otherwise
     */
    public void flush(boolean restore) {
        if (mCount > 0) {
            //!
            //! We need to stop using the storage before rendering.
            //!
            mStorage.unmap();
            mStorage.update();

            draw(Primitive.TRIANGLES, 0, mCount * 6, VertexFormat.UNSIGNED_SHORT);
        }

        if (restore) {
            //!
            //! Acquire the storage again if required for rendering.
            //!
            if (mCount > 0) {
                mVertices = mStorage.map();
            }

        } else if (mCount == 0) {
            //!
            //! We need to stop using the storage before finish the batch.
            //!
            mStorage.unmap();
        }
        mCount = 0;
    }

    /**
     * <p>Check if the batch requires to be flush automatically</p>
     */
    private void flushIfRequired(Texture texture) {
        if (texture.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! NOTE: Some texture(s) may be loaded asynchronous and requires to update.
            //!
            flush(true);

            texture.create();
            texture.acquire();
            texture.update();
        } else if (!QKRender.isActive(texture)) {
            //!
            //! NOTE: Handle when the texture doesn't match.
            //!
            flush(true);

            texture.acquire();
        } else if (mVertices.position() == mVertices.capacity()) {
            //!
            //! NOTE: Handle when the storage is full.
            //!
            flush(true);
        }
    }
}
