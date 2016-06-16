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

import org.quark.render.Render;
import org.quark.resource.AssetDescriptor;
import org.quark.system.utility.Disposable;
import org.quark.system.utility.Manageable;

import static org.quark.Quark.QKRender;

/**
 * <code>Texture</code> encapsulate a texture that contains one or more {@link Image}(s).
 * <p>
 * A texture can be used as the source of a texture access from a pipeline, or it can be used as a render target.
 */
public abstract class Texture extends Manageable implements Disposable {
    public final static int CONCEPT_FILTER = (1 << 0);
    public final static int CONCEPT_CLAMP_X = (1 << 1);
    public final static int CONCEPT_CLAMP_Y = (1 << 2);
    public final static int CONCEPT_CLAMP_Z = (1 << 3);
    public final static int CONCEPT_IMAGE = (1 << 4);

    protected final Image mImage;
    protected final TextureType mType;
    protected final TextureFormat mFormat;
    protected TextureFilter mFilter;

    /**
     * <p>Constructor</p>
     */
    protected Texture(TextureType type, TextureFormat format, Image images) {
        mType = type;
        mFormat = format;
        mImage = images;

        setUpdate(Texture.CONCEPT_IMAGE);
    }

    /**
     * <p>Change the filter of the texture</p>
     *
     * @param filter the new filter of the texture
     */
    public final void setFilter(TextureFilter filter) {
        if (mFilter != filter) {
            mFilter = filter;
            setUpdate(CONCEPT_FILTER);
        }
    }

    /**
     * <p>Get the type of the texture</p>
     *
     * @return the type of the texture
     */
    public final TextureType getType() {
        return mType;
    }

    /**
     * <p>Get the image of the texture</p>
     *
     * @return the image of the texture
     */
    public final Image getImage() {
        return mImage;
    }

    /**
     * <p>Get the format of the texture</p>
     *
     * @return the format of the texture
     */
    public final TextureFormat getFormat() {
        return mFormat;
    }

    /**
     * <p>Get the filter of the texture</p>
     *
     * @return the filter of the texture
     */
    public final TextureFilter getFilter() {
        return mFilter;
    }

    /**
     * @see Render#create(Texture)
     */
    public final void create() {
        QKRender.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void delete() {
        QKRender.delete(this);
    }

    /**
     * @see Render#acquire(Texture)
     */
    public final void acquire() {
        QKRender.acquire(this);
    }

    /**
     * @see Render#acquire(Texture, int)
     */
    public final void acquire(int stage) {
        QKRender.acquire(this, stage);
    }

    /**
     * @see Render#update(Texture)
     */
    public final void update() {
        QKRender.update(this);
    }

    /**
     * @see Render#release(Texture)
     */
    public final void release() {
        QKRender.release(this);
    }

    /**
     * @see Render#release(Texture, int)
     */
    public final void release(int stage) {
        QKRender.release(this, stage);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public final void dispose() {
        QKRender.dispose(this);
    }

    /**
     * <code>Descriptor</code> represent the {@link AssetDescriptor} for {@link Texture}.
     */
    public final static class Descriptor extends AssetDescriptor {
        public final static int FEATURE_MIPMAP = (1 << 0);

        private final TextureFormat mFormat;
        private final TextureFilter mFilter;
        private final TextureBorder mBorderX;
        private final TextureBorder mBorderY;
        private final TextureBorder mBorderZ;
        private final int mFeatures;

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter, int features,
                TextureBorder borderX,
                TextureBorder borderY,
                TextureBorder borderZ) {
            super(true, true);
            mFormat = format;
            mFilter = filter;
            mFeatures = features;
            mBorderX = borderX;
            mBorderY = borderY;
            mBorderZ = borderZ;
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter, int features, TextureBorder border) {
            this(format, filter, features, border, border, border);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter, TextureBorder border) {
            this(format, filter, 0, border, border, border);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter, int features) {
            this(format, filter, features, TextureBorder.REPEAT, TextureBorder.REPEAT, TextureBorder.REPEAT);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter) {
            this(format, filter, 0, TextureBorder.REPEAT, TextureBorder.REPEAT, TextureBorder.REPEAT);
        }

        /**
         * <p>Get the format of the texture</p>
         *
         * @return the format of the texture
         */
        public TextureFormat getFormat() {
            return mFormat;
        }

        /**
         * <p>Get the filter of the texture</p>
         *
         * @return the filter of the texture
         */
        public TextureFilter getFilter() {
            return mFilter;
        }

        /**
         * <p>Get the border mode for the x coordinate</p>
         *
         * @return the border mode for the x coordinate
         */
        public TextureBorder getBorderX() {
            return mBorderX;
        }

        /**
         * <p>Get the border mode for the y coordinate</p>
         *
         * @return the border mode for the y coordinate
         */
        public TextureBorder getBorderY() {
            return mBorderY;
        }

        /**
         * <p>Get the border mode for the z coordinate</p>
         *
         * @return the border mode for the y coordinate
         */
        public TextureBorder getBorderZ() {
            return mBorderZ;
        }

        /**
         * <p>Check if the texture has the given feature</p>
         *
         * @return <code>true</code> if the texture has given feature, <code>false</code> otherwise
         */
        public boolean hasFeature(int feature) {
            return (mFeatures & feature) != 0;
        }
    }
}
