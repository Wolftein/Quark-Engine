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
package org.quark_engine.render.texture;

import org.quark_engine.render.Render;
import org.quark_engine.system.utility.Disposable;
import org.quark_engine.system.utility.Manageable;
import org.quark_engine.resource.AssetDescriptor;

import java.util.List;

import static org.quark_engine.Quark.QkRender;

/**
 * <code>Texture</code> encapsulate a texture that contains one or more {@link Image}(s).
 * <p>
 * A texture can be used as the source of a texture access from a pipeline, or it can be used as a render target.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public abstract class Texture extends Manageable implements Disposable {
    public final static int CONCEPT_FILTER = (1 << 0);
    public final static int CONCEPT_CLAMP_X = (1 << 1);
    public final static int CONCEPT_CLAMP_Y = (1 << 2);
    public final static int CONCEPT_CLAMP_Z = (1 << 3);
    public final static int CONCEPT_IMAGE = (1 << 4);

    protected final TextureType mType;
    protected final TextureFormat mFormat;
    protected final List<Image> mImages;
    protected TextureFilter mFilter;

    /**
     * <p>Constructor</p>
     */
    protected Texture(TextureType type, TextureFormat format, List<Image> images) {
        mType = type;
        mFormat = format;
        mImages = images;
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
     * <p>Get the image(s) of the texture</p>
     *
     * @return a collection that contain(s) all image(s) of the texture
     */
    public final List<Image> getImages() {
        return mImages;
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
        QkRender.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void delete() {
        QkRender.delete(this);
    }

    /**
     * @see Render#acquire(Texture)
     */
    public final void acquire() {
        QkRender.acquire(this);
    }

    /**
     * @see Render#acquire(Texture, int)
     */
    public final void acquire(int stage) {
        QkRender.acquire(this, stage);
    }

    /**
     * @see Render#update(Texture)
     */
    public final void update() {
        QkRender.update(this);
    }

    /**
     * @see Render#release(Texture)
     */
    public final void release() {
        QkRender.release(this);
    }

    /**
     * @see Render#release(Texture, int)
     */
    public final void release(int stage) {
        QkRender.release(this, stage);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public final void dispose() {
        QkRender.dispose(this);
    }

    /**
     * <code>Descriptor</code> represent the {@link AssetDescriptor} for {@link Texture}.
     */
    public final static class Descriptor extends AssetDescriptor {
        private final TextureFormat mFormat;
        private final TextureFilter mFilter;
        private final TextureBorder mBorderX;
        private final TextureBorder mBorderY;
        private final TextureBorder mBorderZ;

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter,
                TextureBorder borderX,
                TextureBorder borderY,
                TextureBorder borderZ) {
            super(true, true);
            mFormat = format;
            mFilter = filter;
            mBorderX = borderX;
            mBorderY = borderY;
            mBorderZ = borderZ;
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter, TextureBorder border) {
            this(format, filter, border, border, border);
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(TextureFormat format, TextureFilter filter) {
            this(format, filter, TextureBorder.REPEAT, TextureBorder.REPEAT, TextureBorder.REPEAT);
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
    }
}
