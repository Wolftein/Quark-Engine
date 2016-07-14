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
package ar.com.quark.graphic.texture;

import ar.com.quark.asset.AssetDescriptor;
import ar.com.quark.graphic.Graphic;
import ar.com.quark.utility.Disposable;
import ar.com.quark.utility.Manageable;

import static ar.com.quark.Quark.QKGraphic;

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
     * @see Graphic#create(Texture)
     */
    public final void create() {
        QKGraphic.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void delete() {
        super.delete();

        QKGraphic.delete(this);
    }

    /**
     * @see Manageable#deleteAllMemory()
     */
    @Override
    public final void deleteAllMemory() {
        if (mImage != null) {
            mImage.getLayer().forEach(Image.Layer::delete);
        }
    }

    /**
     * @see Graphic#acquire(Texture)
     */
    public final void acquire() {
        QKGraphic.acquire(this);
    }

    /**
     * @see Graphic#acquire(Texture, int)
     */
    public final void acquire(int stage) {
        QKGraphic.acquire(this, stage);
    }

    /**
     * @see Graphic#update(Texture)
     */
    public final void update() {
        QKGraphic.update(this);
    }

    /**
     * @see Graphic#release(Texture)
     */
    public final void release() {
        QKGraphic.release(this);
    }

    /**
     * @see Graphic#release(Texture, int)
     */
    public final void release(int stage) {
        QKGraphic.release(this, stage);
    }

    /**
     * @see Disposable#dispose()
     * <p>
     * NOTE: Safe implementation of {@linkplain #delete()}
     */
    @Override
    public final void dispose() {
        QKGraphic.dispose(this);
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
        public Descriptor(String filename, TextureFormat format, TextureFilter filter,
                TextureBorder borderX,
                TextureBorder borderY,
                TextureBorder borderZ, int features) {
            super(filename, true, true, true);
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
        public Descriptor(String filename, TextureFormat format, TextureFilter filter, TextureBorder border, int features) {
            this(filename, format, filter, border, border, border, features);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(String filename, TextureFormat format, TextureFilter filter, TextureBorder border) {
            this(filename, format, filter, border, border, border, 0);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(String filename, TextureFormat format, TextureFilter filter, int features) {
            this(filename, format, filter, TextureBorder.REPEAT, TextureBorder.REPEAT, TextureBorder.REPEAT, features);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(String filename, TextureFormat format, TextureFilter filter) {
            this(filename, format, filter, TextureBorder.REPEAT, TextureBorder.REPEAT, TextureBorder.REPEAT, 0);
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
