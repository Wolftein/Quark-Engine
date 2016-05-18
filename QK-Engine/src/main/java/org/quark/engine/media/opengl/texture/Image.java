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
package org.quark.engine.media.opengl.texture;

import java.nio.ByteBuffer;

/**
 * <code>Image</code> represent an image in memory. It has a width, height and depth as well as a format specifying
 * the number and order of color components per pixel.
 * <p>
 * Coordinates of pixels are specified with respect to the top left corner of the image, with the x-axis pointing
 * to the right and the y-axis pointing downwards.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Image {
    private final ByteBuffer mBytes;
    private final int mWidth;
    private final int mHeight;
    private final int mDepth;
    private final int mLevel;
    private final ImageFormat mFormat;

    /**
     * <p>Constructor</p>
     */
    public Image(ImageFormat format, int width, int height, int depth, int level, ByteBuffer bytes) {
        mFormat = format;
        mWidth = width;
        mHeight = height;
        mDepth = depth;
        mLevel = level;
        mBytes = bytes;
    }

    /**
     * <p>Retrieve the width of the image (expressed in px)</p>
     *
     * @return the width of the image (expressed in px)
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * <p>Retrieve the height of the image (expressed in px)</p>
     *
     * @return the width of the image (expressed in px)
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * <p>Retrieve the depth of the image</p>
     *
     * @return the depth of the image
     */
    public int getDepth() {
        return mDepth;
    }

    /**
     * <p>Retrieve the level of the image (as mip-map level)</p>
     *
     * @return the level of the image (as mip-map level)
     */
    public int getLevel() {
        return mLevel;
    }

    /**
     * <p>Retrieve the buffer where all the pixel(s) are stored</p>
     *
     * @return the buffer that contain(s) every pixel of the image
     */
    public ByteBuffer getBytes() {
        return mBytes;
    }

    /**
     * <p>Retrieve the format of the image</p>
     *
     * @return the format of the image
     */
    public ImageFormat getFormat() {
        return mFormat;
    }
}
