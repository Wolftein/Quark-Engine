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
package org.quark.render.texture;

import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.Int8Array;

import java.util.Collections;
import java.util.List;

/**
 * <code>Image</code> encapsulate an image in memory. It has a width, height and depth as well as a format specifying
 * the number and order of color components per pixel.
 * <p>
 * Coordinates of pixels are specified with respect to the top left corner of the image, with the x-axis pointing
 * to the right and the y-axis pointing downwards.
 */
public final class Image {
    private final List<Layer> mLayer;
    private final int mWidth;
    private final int mHeight;
    private final int mDepth;
    private final ImageFormat mFormat;

    /**
     * <p>Constructor</p>
     */
    public Image(ImageFormat format, int width, int height, int depth, List<Layer> data) {
        mFormat = format;
        mWidth = width;
        mHeight = height;
        mDepth = depth;
        mLayer = data;
    }

    /**
     * <p>Constructor</p>
     */
    public Image(ImageFormat format, int width, int height, int depth, Layer data) {
        this(format, width, height, depth, Collections.singletonList(data));
    }

    /**
     * <p>Constructor</p>
     */
    public Image(ImageFormat format, int width, int height, int depth) {
        this(format, width, height, depth, new Layer(null, false));
    }

    /**
     * <p>Get all {@link Layer} of the image</p>
     *
     * @return all Layer of the image
     */
    public List<Layer> getLayer() {
        return mLayer;
    }

    /**
     * <p>Get the width of the image (expressed in px)</p>
     *
     * @return the width of the image (expressed in px)
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * <p>Get the height of the image (expressed in px)</p>
     *
     * @return the width of the image (expressed in px)
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * <p>Get the depth of the image</p>
     *
     * @return the depth of the image
     */
    public int getDepth() {
        return mDepth;
    }

    /**
     * <p>Get the format of the image</p>
     *
     * @return the format of the image
     */
    public ImageFormat getFormat() {
        return mFormat;
    }

    /**
     * <p>Layer</p> represent a layer within an {@link Image} which may contain another {@link Image}.
     */
    public final static class Layer {
        /**
         * Hold the data of the layer (The layer and all mip-map).
         *
         * @apiNote [MUTABLE-DISPOSABLE]
         */
        public Int8Array data;

        /**
         * Hold the images of each image in the layer (in bytes, including mip-map).
         */
        public final int[] images;

        /**
         * Hold a flag that indicates whenever it should generate mip-map if not any.
         */
        public final boolean mipmap;

        /**
         * <p>Constructor</p>
         */
        public Layer(Int8Array data, int[] images) {
            this.data = data;
            this.images = images;
            this.mipmap = false;
        }

        /**
         * <p>Constructor</p>
         */
        public Layer(Int8Array data, boolean mipmap) {
            this.data = data;
            this.images = new int[]{hasData() ? data.capacity() : 0};
            this.mipmap = mipmap;
        }

        /**
         * <p>Delete all memory allocated by the layer</p>
         */
        public void delete() {
            data = ArrayFactory.free(data);
        }

        /**
         * <p>Check if the layer has mip-map enabled</p>
         *
         * @return <code>true</code> if the layer has mip-map, <code>false</code> otherwise
         */
        public boolean hasMipmap() {
            return mipmap || images.length > 1;
        }

        /**
         * <p>Check if the layer has data</p>
         *
         * @return <code>true</code> if the layer has data, <code>false</code> otherwise
         */
        public boolean hasData() {
            return data != null;
        }
    }
}
