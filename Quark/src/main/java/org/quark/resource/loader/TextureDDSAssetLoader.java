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
package org.quark.resource.loader;

import org.quark.render.texture.*;
import org.quark.resource.AssetKey;
import org.quark.resource.AssetLoader;
import org.quark.resource.AssetManager;
import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.Int8Array;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>TextureDDSAssetLoader</code> encapsulate an {@link AssetLoader} for loading DDS texture(s).
 */
public final class TextureDDSAssetLoader implements AssetLoader<Texture, Texture.Descriptor> {
    private final static int DDSD_MIPMAP_COUNT = 0x00020000;

    private final static int DDPF_FOUR_CC = 0x00000004;
    private final static int DDPF_ALPHA_PIXELS = 0x00000001;
    private final static int DDPF_ALPHA = 0x00000002;
    private final static int DDPF_RGB = 0x00000040;
    private final static int DDPF_GRAY_SCALE = 0x00020000;

    private final static int DDSCAPS2_CUBEMAP = 0x00000200;
    private final static int DDSCAPS2_VOLUME = 0x00200000;

    /**
     * <code>ImageHeader</code> represent the file format of a DDS image.
     */
    private final static class ImageHeader {
        public int mImageSize;
        public int mImageFlag;
        public int mImageWidth;
        public int mImageHeight;
        public int mImagePitchOrLinear;
        public int mImageDepth;
        public int mImageMipmapCount;
        public int mPixelFormatSize;
        public int mPixelFormatFlags;
        public int mPixelFormatFourCC;
        public int mPixelFormatRGBBitCount;
        public int mPixelFormatRBitMask;
        public int mPixelFormatGBitMask;
        public int mPixelFormatBBitMask;
        public int mPixelFormatABitMask;
        public int mImageCaps;
        public int mImageCaps2;
        public int mImageCaps3;
        public int mImageCaps4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(
            AssetManager manager, AssetKey<Texture, Texture.Descriptor> key, InputStream input) throws IOException {
        key.setAsset(readTexture(key.getDescriptor(), new DataInputStream(input)));
    }

    /**
     * <p>Read a {@link Texture} from the {@link InputStream} given</p>
     *
     * @param descriptor the texture descriptor
     * @param input      the input-stream that contain(s) the texture
     *
     * @return the texture
     *
     * @throws IOException indicates failing loading the texture
     */
    private Texture readTexture(Texture.Descriptor descriptor, DataInputStream input) throws IOException {
        if (readIntLittleEndian(input) != 0x20534444) {
            throw new IOException("Trying to read an invalid <DDS> texture");
        }

        //!
        //! According to DDS documentation, the first chunk should be the header of the image
        //!
        final ImageHeader header = readHeader(input);

        if ((header.mImageCaps2 & DDSCAPS2_CUBEMAP) != 0) {
            return new Texture2DCube(
                    descriptor.getFormat(),
                    descriptor.getFilter(),
                    descriptor.getBorderX(),
                    descriptor.getBorderY(),
                    descriptor.getBorderZ(), readImage(descriptor, header, input));
        } else if ((header.mImageCaps2 & DDSCAPS2_VOLUME) != 0) {
            return new Texture3D(
                    descriptor.getFormat(),
                    descriptor.getFilter(),
                    descriptor.getBorderX(),
                    descriptor.getBorderY(),
                    descriptor.getBorderZ(), readImage(descriptor, header, input));
        } else {
            return new Texture2D(
                    descriptor.getFormat(),
                    descriptor.getFilter(),
                    descriptor.getBorderX(),
                    descriptor.getBorderY(), readImage(descriptor, header, input));
        }
    }

    /**
     * <p>Read image header from the {@link DataInputStream} given</p>
     *
     * @param input the input that contain(s) the header
     *
     * @throws IOException indicates if the image has invalid header
     */
    private ImageHeader readHeader(DataInputStream input) throws IOException {
        final ImageHeader header = new ImageHeader();

        //!
        //! Texture common data
        //!
        header.mImageSize = readIntLittleEndian(input);
        header.mImageFlag = readIntLittleEndian(input);
        header.mImageHeight = Math.max(readIntLittleEndian(input), 0x01);
        header.mImageWidth = Math.max(1, readIntLittleEndian(input));
        header.mImagePitchOrLinear = readIntLittleEndian(input);
        header.mImageDepth = Math.max(1, readIntLittleEndian(input));
        header.mImageMipmapCount = Math.max(1, readIntLittleEndian(input));

        //!
        //! Reserve field
        //!
        input.skipBytes(0x04 * 0x0B);

        //!
        //! Texture pixel format data
        //!
        header.mPixelFormatSize = readIntLittleEndian(input);
        header.mPixelFormatFlags = readIntLittleEndian(input);

        if ((header.mPixelFormatFlags & DDPF_FOUR_CC) == 0) {
            header.mPixelFormatFourCC = -1;
        } else {
            header.mPixelFormatFourCC = input.readInt();
        }
        header.mPixelFormatRGBBitCount = readIntLittleEndian(input);
        header.mPixelFormatRBitMask = readIntLittleEndian(input);
        header.mPixelFormatGBitMask = readIntLittleEndian(input);
        header.mPixelFormatBBitMask = readIntLittleEndian(input);
        header.mPixelFormatABitMask = readIntLittleEndian(input);
        header.mImageCaps = readIntLittleEndian(input);
        header.mImageCaps2 = readIntLittleEndian(input);
        header.mImageCaps3 = readIntLittleEndian(input);
        header.mImageCaps4 = readIntLittleEndian(input);

        if ((header.mImageCaps2 & DDSCAPS2_CUBEMAP) != 0) {
            //!
            //! Cube-map have six faces.
            //!
            header.mImageDepth = 0x06;
        }

        //!
        //! Reserve field
        //!
        input.skipBytes(0x04);
        return header;
    }

    /**
     * <p>Parse the image(s) from the {@link DataInputStream} given</p>
     *
     * @param descriptor the descriptor of the image
     * @param header     the header of the image
     * @param input      the stream of the image
     *
     * @return a collection that contain(s) all image(s)
     *
     * @throws IOException indicates failing loading the image(s)
     */
    private Image readImage(Texture.Descriptor descriptor, ImageHeader header, DataInputStream input) throws IOException {
        final List<Image.Layer> layer = new ArrayList<>(header.mImageDepth);

        //!
        //! Translate DDS format into native format.
        //!
        final ImageFormat format = getFormat(header);

        //!
        //! Calculate the bpp of the format.
        //!
        final int length = (format == ImageFormat.RGB_DXT1 ? 0x04 : 0x08);

        //!
        //! Load each layer of the image.
        //!
        for (int i = 0; i < header.mImageDepth; ++i) {
            int imageWidth = header.mImageWidth;
            int imageHeight = header.mImageHeight;
            int imageLength = 0;

            //!
            //! Calculate the number of mip-mapping per image.
            //!
            final int mipmap[] = new int[header.mImageMipmapCount];

            //!
            //! Calculate the offset of the mipmap.
            //!
            for (int k = 0; k < mipmap.length; ++k) {
                if (format.eCompressed) {
                    mipmap[k] = ((imageWidth + 3) / 4) * ((imageHeight + 3) / 4) * length * 2;
                } else {
                    mipmap[k] = ((imageWidth * imageHeight * length) / 8);
                }
                imageLength += mipmap[k];
                imageWidth = Math.max(imageWidth / 2, 1);
                imageHeight = Math.max(imageHeight / 2, 1);
            }

            //!
            //! Read the layer and all mipmap at once.
            //!
            final byte[] content = new byte[imageLength];
            input.readFully(content, 0, content.length);

            //!
            //! Create the buffer.
            //!
            final Int8Array buffer = ArrayFactory.allocateInt8Array(imageLength);
            buffer.write(content, 0, imageLength);
            buffer.flip();

            //!
            //! Create the layer.
            //!
            if (mipmap.length == 1) {
                layer.add(i, new Image.Layer(buffer, descriptor.hasFeature(Texture.Descriptor.FEATURE_MIPMAP)));
            } else {
                layer.add(i, new Image.Layer(buffer, mipmap));
            }
        }
        return new Image(format, header.mImageWidth, header.mImageHeight, header.mImageDepth, layer);
    }

    /**
     * <p>Get the format of the image</p>
     *
     * @param header the header of the image
     *
     * @return the format of the image
     *
     * @throws IOException indicates if the format is unsupported
     */
    private ImageFormat getFormat(ImageHeader header) throws IOException {
        return header.mPixelFormatFourCC == -1 ? getUncompressedFormat(header) : getCompressedFormat(header);
    }

    /**
     * <p>Get the uncompressed format of the image</p>
     *
     * @param header the header of the image
     *
     * @return the uncompressed format of the image
     *
     * @throws IOException indicates if the format is unsupported
     */
    private ImageFormat getUncompressedFormat(ImageHeader header) throws IOException {
        if ((header.mImageFlag & DDPF_RGB) != 0) {
            return (header.mPixelFormatFlags & DDPF_ALPHA_PIXELS) == 0 ? ImageFormat.RGB : ImageFormat.RGBA;
        } else if ((header.mImageFlag & DDPF_ALPHA) != 0 || (header.mImageFlag & DDPF_GRAY_SCALE) != 0) {
            return (header.mPixelFormatFlags & DDPF_ALPHA_PIXELS) == 0 ? ImageFormat.RED : ImageFormat.RG;
        }
        throw new IOException("Uncompressed format not supported.");
    }

    /**
     * <p>Get the compressed format of the image</p>
     *
     * @param header the header of the image
     *
     * @return the compressed format of the image
     *
     * @throws IOException indicates if the format is unsupported
     */
    private ImageFormat getCompressedFormat(ImageHeader header) throws IOException {
        switch (header.mPixelFormatFourCC) {
            case 0x44585431:
                return (header.mPixelFormatFlags & DDPF_ALPHA_PIXELS) == 0
                        ? ImageFormat.RGB_DXT1
                        : ImageFormat.RGBA_DXT1;
            case 0x44585433:
                return ImageFormat.RGBA_DXT3;
            case 0x44585435:
                return ImageFormat.RGBA_DXT5;
        }
        throw new IOException(header.mPixelFormatFourCC + " compressed format not supported.");
    }

    /**
     * <b>Read a little endian integer (Required for DDS format)</b>
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int b0 = in.readInt();
        final int b1 = (b0 >> 0) & 0xFF;
        final int b2 = (b0 >> 8) & 0xFF;
        final int b3 = (b0 >> 16) & 0xFF;
        final int b4 = (b0 >> 24) & 0xFF;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }
}
