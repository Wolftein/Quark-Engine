/*
 * This file is part of Quark Engine, licensed under the APACHE License.
 * <p>
 * Copyright (c) 2014-2016 Agustin L. Alvarez <wolftein1@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.quark.engine.resource.loader;

import org.quark.engine.media.opengl.texture.*;
import org.quark.engine.resource.AssetKey;
import org.quark.engine.resource.AssetLoader;
import org.quark.engine.resource.AssetManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulate an {@link AssetLoader} for loading S3TC texture(s).
 * <p>
 * {@link org.quark.engine.media.opengl.texture.Texture}
 * {@link org.quark.engine.media.opengl.texture.Texture1D}
 * {@link org.quark.engine.media.opengl.texture.Texture2D}
 * {@link org.quark.engine.media.opengl.texture.ImageFormat#RED}
 * {@link org.quark.engine.media.opengl.texture.ImageFormat#RG}
 * {@link org.quark.engine.media.opengl.texture.ImageFormat#RGB}
 * {@link org.quark.engine.media.opengl.texture.ImageFormat#RGBA}
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class TextureS3TCAssetLoader implements AssetLoader<Texture, Texture.Descriptor> {
    /**
     * Encapsulate the texture structure for any ST3C texture.
     */
    private final static class DDSHeader {
        public int mImageSize;
        public int mImageFlag;
        public int mImageWidth;
        public int mImageHeight;
        public int mImagePitchOrLinear;
        public int mImageDepth;
        public int mImageMipmapCount;
        public int mPixelFormatSize;
        public int mPixelFormatFlags;
        public String mPixelFormatFourCC;
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
    public AssetKey<Texture, Texture.Descriptor> load(AssetManager context, Texture.Descriptor descriptor,
            InputStream in) throws IOException {
        return new AssetKey<>(parseTexture(new DataInputStream(in), descriptor), descriptor);
    }

    /**
     * <p>Parse the texture from {@link DataInputStream}</p>
     *
     * @param in         the input      of the texture expressed as <code>DataInputStream</code>
     * @param descriptor the descriptor of the texture expressed as <code>Texture.Descriptor</code>
     *
     * @return a reference to the texture
     *
     * @throws IOException indicates the texture is invalid
     */
    private Texture parseTexture(DataInputStream in, Texture.Descriptor descriptor) throws IOException {
        if (readIntLittleEndian(in) != 0x20534444) {
            throw new IOException("Trying to read an invalid ST3C texture.");
        }

        final DDSHeader header = parseHeader(in);

        if (header.mImageHeight == -1) {
            return new Texture1D(descriptor.getFormat(), descriptor.getFilter(),
                    descriptor.getBorderX(), parseImages(in, header));
        }
        if ((header.mImageCaps2 & 0x00200000) != 0 && (header.mImageCaps & 0x00800000) != 0) {
            return new Texture3D(descriptor.getFormat(), descriptor.getFilter(),
                    descriptor.getBorderX(),
                    descriptor.getBorderY(),
                    descriptor.getBorderZ(), parseImages(in, header));
        }
        if ((header.mImageCaps2 & 0x00000200) != 0) {
            throw new IOException("S3TC Cube-map texture(s) are not supported yet.");
        }
        return new Texture2D(descriptor.getFormat(), descriptor.getFilter(),
                descriptor.getBorderX(),
                descriptor.getBorderY(), parseImages(in, header));
    }

    /**
     * <p>Parse the header of the texture from {@link DataInputStream}</p>
     *
     * @param in the input of the texture expressed as <code>DataInputStream</code>
     *
     * @return a reference to the header of the texture
     *
     * @throws IOException indicates the texture has invalid header
     */
    private DDSHeader parseHeader(DataInputStream in) throws IOException {
        final DDSHeader header = new DDSHeader();

        //!
        //! Texture common data
        //!
        header.mImageSize = readIntLittleEndian(in);
        header.mImageFlag = readIntLittleEndian(in);
        header.mImageHeight = readIntLittleEndian(in);
        header.mImageWidth = Math.max(1, readIntLittleEndian(in));
        header.mImagePitchOrLinear = readIntLittleEndian(in);
        header.mImageDepth = Math.max(1, readIntLittleEndian(in));
        header.mImageMipmapCount = Math.max(1, readIntLittleEndian(in));

        //!
        //! Reserve field
        //!
        in.skipBytes(0x04 * 0x0B);

        //!
        //! Texture pixel format data
        //!
        header.mPixelFormatSize = readIntLittleEndian(in);
        header.mPixelFormatFlags = readIntLittleEndian(in);

        if ((header.mPixelFormatFlags & 0x00000004) == 0) {
            header.mPixelFormatFourCC = null;
        } else {
            final byte[] name = new byte[0x04];
            in.readFully(name);
            header.mPixelFormatFourCC = new String(name, "US-ASCII");
        }
        header.mPixelFormatRGBBitCount = readIntLittleEndian(in);
        header.mPixelFormatRBitMask = readIntLittleEndian(in);
        header.mPixelFormatGBitMask = readIntLittleEndian(in);
        header.mPixelFormatBBitMask = readIntLittleEndian(in);
        header.mPixelFormatABitMask = readIntLittleEndian(in);
        header.mImageCaps = readIntLittleEndian(in);
        header.mImageCaps2 = readIntLittleEndian(in);
        header.mImageCaps3 = readIntLittleEndian(in);
        header.mImageCaps4 = readIntLittleEndian(in);

        //!
        //! Reserve field
        //!
        in.skipBytes(0x04);

        return header;
    }

    /**
     * <p>Parse the images of the texture from {@link DataInputStream}</p>
     *
     * @param in     the input  of the texture expressed as <code>DataInputStream</code>
     * @param header the header of the texture expressed as <code>DDSHeader</code>
     *
     * @return a collection that contain(s) all image(s)
     *
     * @throws IOException indicates failing parsing any of the image(s)
     */
    private List<Image> parseImages(DataInputStream in, DDSHeader header) throws IOException {
        int imageDepth = header.mImageDepth;
        int imageWidth = header.mImageWidth;
        int imageHeight = header.mImageHeight;

        //!
        //! Translate S3TC format to <code>ImageFormat</code>
        //!
        final ImageFormat imageFormat =
                header.mPixelFormatFourCC == null ? getUncompressedFormat(header) : getCompressedFormat(header);
        final int imageFormatScanLength = (imageFormat == ImageFormat.RGB_DXT1 ? 0x08 : 0x10);
        final int imageCount = ((header.mImageFlag & 0x00020000) == 0 ? 1 : header.mImageMipmapCount);

        //!
        //! Parse each mip-map of the image
        //!
        final List<Image> imageList = new ArrayList<>(imageCount);

        for (int i = 0; i < imageCount; ++i) {
            //!
            //! Calculate the length of the mip-map
            //!
            final int imageLength = imageFormat.eCompressed
                    ? ((imageWidth + 3) / 4 * imageFormatScanLength) * ((imageHeight + 3) / 4) * ((imageDepth + 3) / 4)
                    : ((imageWidth * imageFormat.eComponent * imageHeight * imageDepth));

            //!
            //! Read the image
            //!
            final byte data[] = new byte[imageLength];
            in.readFully(data, 0, imageLength);

            final ByteBuffer buffer = ByteBuffer.allocateDirect(imageLength).order(ByteOrder.nativeOrder());
            buffer.put(data, 0, imageLength).flip();
            imageList.add(new Image(imageFormat, imageWidth, imageHeight, imageDepth, i, buffer));

            imageDepth = Math.max(imageDepth / 2, 1);
            imageWidth = Math.max(imageWidth / 2, 1);
            imageHeight = Math.max(imageHeight / 2, 1);
        }
        return imageList;
    }

    /**
     * <p>Get the uncompressed format</p>
     *
     * @param header the header of the texture expressed as <code>DDSHeader</code>
     *
     * @return the uncompressed format of the image
     *
     * @throws IOException indicates the format is unsupported
     */
    private ImageFormat getUncompressedFormat(DDSHeader header) throws IOException {
        if (header.mPixelFormatRBitMask == 0xFF
                && header.mPixelFormatGBitMask == 0xFF00
                && header.mPixelFormatBBitMask == 0xFF0000) {

            if ((header.mPixelFormatFlags & 0x00000001) == 0) {
                return ImageFormat.RGB;
            } else if (header.mPixelFormatABitMask == 0xFF000000) {
                return ImageFormat.RGBA;
            }
        }
        throw new IOException("compressed format not supported.");
    }

    /**
     * <p>Get the compressed format</p>
     *
     * @param header the header of the texture expressed as <code>DDSHeader</code>
     *
     * @return the compressed format of the image
     *
     * @throws IOException indicates the format is unsupported
     */
    private ImageFormat getCompressedFormat(DDSHeader header) throws IOException {
        switch (header.mPixelFormatFourCC) {
            case "DXT1":
                return (header.mPixelFormatFlags & 0x00000001) == 0 ? ImageFormat.RGB_DXT1 : ImageFormat.RGBA_DXT1;
            case "DXT3":
                return ImageFormat.RGBA_DXT3;
            case "DXT5":
                return ImageFormat.RGBA_DXT5;
        }
        throw new IOException(header.mPixelFormatFourCC + " compressed format not supported.");
    }

    /**
     * (NON-DOCUMENTED)
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int int1 = in.readByte();
        final int int2 = in.readByte();
        final int int3 = in.readByte();
        final int int4 = in.readByte();

        return (int4 << 24 | (int3 & 0xFF) << 16 | (int2 & 0xFF) << 8 | (int1 & 0xFF));
    }
}
