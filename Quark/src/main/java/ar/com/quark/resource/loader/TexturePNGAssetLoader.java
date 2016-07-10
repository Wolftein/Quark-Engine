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
package ar.com.quark.resource.loader;

import ar.com.quark.render.texture.Image;
import ar.com.quark.render.texture.ImageFormat;
import ar.com.quark.render.texture.Texture2D;
import ar.com.quark.resource.AssetKey;
import ar.com.quark.resource.AssetLoader;
import ar.com.quark.resource.AssetManager;
import ar.com.quark.system.utility.array.ArrayFactory;
import ar.com.quark.system.utility.array.Int8Array;
import ar.com.quark.render.texture.Texture;

import java.io.*;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

/**
 * <code>TexturePNGAssetLoader</code> encapsulate an {@link AssetLoader} for loading PNG image(s).
 */
public final class TexturePNGAssetLoader implements AssetLoader<Texture, Texture.Descriptor> {
    private final static byte PIXEL_OPAQUE = (byte) 0xFF;
    private final static byte PIXEL_NON_OPAQUE = (byte) 0x00;

    private final static int TYPE_GREY_SCALE = 0;
    private final static int TYPE_TRUE_COLOR = 2;
    private final static int TYPE_INDEXED = 3;
    private final static int TYPE_GREY_SCALE_ALPHA = 4;
    private final static int TYPE_TRUE_COLOR_ALPHA = 6;

    /**
     * <code>ImageHeader</code> represent the file format of a PNG image.
     */
    private final static class ImageHeader {
        public int mImageWidth;
        public int mImageHeight;
        public int mImageDepth;
        public int mPixelType;
        public int mPixelCompression;
        public int mPixelFilter;
        public int mPixelInterlace;

        public byte[] mPalette;
        public byte[] mTransparency;
    }

    /**
     * <code>ImageChunk</code> represent the chunk format of a PNG image.
     */
    private final static class ImageChunk {
        public int mType;
        public byte[] mData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(AssetManager manager, AssetKey<Texture, Texture.Descriptor> key, InputStream input)
            throws IOException {
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
        if (input.readLong() != 0x89504E470D0A1A0AL) {
            throw new IOException("Trying to read an invalid <PNG> texture");
        }

        //!
        //! According to PNG documentation, the first chunk should be the header of the image
        //!
        final ImageHeader header = readHeader(readChunk(input));
        if (header.mPixelInterlace != 0) {
            throw new IOException("Interlace images are not supported");
        } else if (header.mPixelFilter != 0) {
            throw new IOException("Custom filter algorithm are not supported");
        } else if (header.mPixelCompression != 0) {
            throw new IOException("Custom compression algorithm are not supported");
        }

        //!
        //! Contain(s) the compressed data of the png texture
        //!
        final ByteArrayOutputStream data = new ByteArrayOutputStream();

        //!
        //! Read the whole format
        //!
        do {
            final ImageChunk chunk = readChunk(input);

            switch (chunk.mType) {
                case 0x504C5445:
                    handlePalette(header, chunk);
                    break;
                case 0x74524E53:
                    handleTransparency(header, chunk);
                    break;
                case 0x49444154:
                    data.write(chunk.mData);
                    break;
            }
        } while (input.available() > 0);

        //!
        //! Load the texture.
        //!
        return new Texture2D(
                descriptor.getFormat(),
                descriptor.getFilter(),
                descriptor.getBorderX(),
                descriptor.getBorderY(), readImage(descriptor, header, data.toByteArray()));
    }

    /**
     * <p>Read a chunk from the {@link InputStream} given</p>
     *
     * @param input the input-stream that contain(s) the chunk
     *
     * @return the chunk of the image
     *
     * @throws IOException indicates if the image has invalid chunk
     */
    private ImageChunk readChunk(DataInputStream input) throws IOException {
        final int length = input.readInt();

        if (length >= 0) {
            final ImageChunk chunk = new ImageChunk();
            chunk.mType = input.readInt();

            chunk.mData = new byte[length];
            input.readFully(chunk.mData);
            input.skipBytes(0x04);

            return chunk;
        }
        throw new IOException("Image is too big to read");
    }

    /**
     * <p>Read image header from the {@link ImageChunk} given</p>
     *
     * @param chunk the chunk that contain(s) the header
     *
     * @throws IOException indicates if the image has invalid header
     */
    private ImageHeader readHeader(ImageChunk chunk) throws IOException {
        if (chunk.mType != 0x49484452) {
            throw new IOException("Found invalid <PNG> header");
        }

        final ImageHeader header = new ImageHeader();

        try (final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(chunk.mData))) {
            header.mImageWidth = stream.readInt();
            header.mImageHeight = Math.max(stream.readInt(), 1);
            header.mImageDepth = stream.readByte();
            header.mPixelType = stream.readByte();
            header.mPixelCompression = stream.readByte();
            header.mPixelFilter = stream.readByte();
            header.mPixelInterlace = stream.readByte();
        }
        return header;
    }

    /**
     * <p>Parse the image from the byte(s) given</p>
     *
     * @param descriptor the descriptor of the image
     * @param header     the header of the image
     * @param input      the bytes  of the image
     *
     * @return the image
     *
     * @throws IOException indicates failing loading the image
     */
    private Image readImage(Texture.Descriptor descriptor, ImageHeader header, byte[] input) throws IOException {
        final ImageFormat imageFormat
                = getUncompressedFormat(header);
        final Int8Array imageBuffer
                = ArrayFactory.allocateInt8Array(header.mImageWidth * header.mImageHeight * imageFormat.eComponent);
        
        final int imageLineLength = getUncompressedFormatLength(header);
        final int imageLineSize = ((header.mImageWidth * header.mImageDepth + 7) / 8) * imageLineLength;

        final byte[] zipFilter = new byte[1];
        final byte[] zipBuffer1 = new byte[imageLineSize];
        final byte[] zipBuffer2 = new byte[imageLineSize];
        final byte[] indexedBuffer = new byte[header.mImageWidth];

        //!
        //! Decompress the data of the image (using z-lib)
        //!
        try (final InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(input))) {

            for (int y = 0; y < header.mImageHeight; ++y) {
                readAll(inflater, zipFilter);
                readAll(inflater, zipBuffer1);

                switch (zipFilter[0]) {
                    case 0:
                        //!
                        //! With the None filter, the scan-line is transmitted unmodified.
                        //!
                        break;
                    case 1:
                        //!
                        //! The Sub filter transmits the difference between each byte and the value of the
                        //! corresponding byte of the prior pixel
                        //!
                        for (int i = imageLineLength; i < zipBuffer1.length; i++) {
                            zipBuffer1[i] += zipBuffer1[i - imageLineLength];
                        }
                        break;
                    case 2:
                        //!
                        //! The Up filter is just like the Sub filter except that the pixel immediately
                        //! above the current pixel, rather than just to its left, is used as the predictor
                        //!
                        for (int i = 0; i < zipBuffer1.length; i++) {
                            zipBuffer1[i] += zipBuffer2[i];
                        }
                        break;
                    case 3:
                        //!
                        //! The Average filter uses the average of the two neighboring pixels (left and above)
                        //! to predict the value of a pixel
                        //!
                        for (int i = 0; i < imageLineLength; i++) {
                            zipBuffer1[i] += (zipBuffer2[i] & 0xFF) >>> 1;
                        }
                        for (int i = imageLineLength; i < zipBuffer1.length; i++) {
                            zipBuffer1[i] += ((zipBuffer2[i] & 0xFF) + (zipBuffer1[i - imageLineLength] & 0xFF)) >>> 1;
                        }
                        break;

                    case 4:
                        //!
                        //! The Paeth filter computes a simple linear function of the three neighboring pixels
                        //! (left, above, upper left), then chooses as predictor the neighboring pixel closest
                        //! to the computed value
                        //!
                        for (int i = 0; i < imageLineLength; i++) {
                            zipBuffer1[i] += zipBuffer2[i];
                        }
                        for (int i = imageLineLength; i < zipBuffer1.length; i++) {
                            final int a = zipBuffer1[i - imageLineLength] & 0xFF;
                            final int b = zipBuffer2[i] & 0xFF;
                            int c = zipBuffer2[i - imageLineLength] & 0xFF;
                            final int path = a + b - c;

                            int pa = path - a;
                            if (pa < 0) {
                                pa = -pa;
                            }
                            int pb = path - b;
                            if (pb < 0) {
                                pb = -pb;
                            }
                            int pc = path - c;
                            if (pc < 0) {
                                pc = -pc;
                            }
                            if (pa <= pb && pa <= pc) {
                                zipBuffer1[i] += a;
                            } else if (pb <= pc) {
                                zipBuffer1[i] += b;
                            } else {
                                zipBuffer1[i] += c;
                            }
                        }
                        break;
                    default:
                        throw new IOException("PNG image contain(s) invalid filter");
                }
                System.arraycopy(zipBuffer1, 0, zipBuffer2, 0, zipBuffer1.length);

                //!
                //! Proceed to parse each color type of the texture
                //!
                if (header.mPixelType == TYPE_INDEXED) {
                    handleDataIndexed(header, zipBuffer1, indexedBuffer);
                    handleData(header, indexedBuffer, imageBuffer);
                } else {
                    handleData(header, zipBuffer1, imageBuffer);
                }
            }

        } catch (Exception exception) {
            throw new IOException(exception);
        }
        imageBuffer.flip();

        return new Image(imageFormat, header.mImageWidth, header.mImageHeight, 0,
                new Image.Layer(imageBuffer, descriptor.hasFeature(Texture.Descriptor.FEATURE_MIPMAP)));
    }

    /**
     * <p>Parse the image's byte(s) from the byte(s) given</p>
     *
     * @param header the header of the image
     * @param input  the bytes  of the image
     * @param output the buffer that will contain(s) the byte(s)
     *
     * @throws IOException indicates failing loading the image
     */
    private void handleData(ImageHeader header, byte[] input, Int8Array output) throws IOException {
        final byte[] transparency = header.mTransparency;

        switch (header.mPixelType) {
            case TYPE_GREY_SCALE:
                if (transparency == null) {
                    output.write(input);
                } else {
                    for (final byte texel : input)
                        output.write(texel).write(texel == transparency[0x01] ? PIXEL_NON_OPAQUE : PIXEL_OPAQUE);
                }
                break;
            case TYPE_GREY_SCALE_ALPHA:
                output.write(input);
                break;
            case TYPE_INDEXED:
                if (transparency == null) {
                    for (final byte pixel : input) {
                        final int index = pixel & 0xFF;

                        output.write(header.mPalette[index * 3])
                                .write(header.mPalette[index * 3 + 1])
                                .write(header.mPalette[index * 3 + 2]);
                    }
                } else {
                    for (final byte pixel : input) {
                        final int index = pixel & 0xFF;

                        output.write(header.mPalette[index * 3])
                                .write(header.mPalette[index * 3 + 1])
                                .write(header.mPalette[index * 3 + 2])
                                .write(transparency[index]);
                    }
                }
                break;
            case TYPE_TRUE_COLOR:
                if (transparency == null) {
                    output.write(input);
                } else {
                    for (int i = 0; i < input.length; i += 3)
                        output.write(input, i, 0x03).write(input[i] == transparency[0x01] &&
                                input[i + 1] == transparency[3] &&
                                input[i + 2] == transparency[5] ? PIXEL_NON_OPAQUE : PIXEL_OPAQUE);
                }
                break;
            case TYPE_TRUE_COLOR_ALPHA:
                output.write(input);
                break;
        }
    }

    /**
     * <p>Parse the image's indexed byte(s) from the byte(s) given</p>
     *
     * @param header the header of the image
     * @param input  the bytes  of the image
     * @param output the buffer that will contain(s) the byte(s)
     *
     * @throws IOException indicates failing loading the image
     */
    private void handleDataIndexed(ImageHeader header, byte[] input, byte[] output) throws IOException {
        switch (header.mImageDepth) {
            case 8:
                break;
            case 4:
                for (int i = 0; i < output.length; i += 2) {
                    final int val = input[(i >> 1)] & 0xFF;

                    switch (output.length - i) {
                        default:
                            output[i + 1] = (byte) (val & 0x0F);
                        case 1:
                            output[i] = (byte) (val >> 0x04);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < output.length; i += 4) {
                    final int val = input[(i >> 2)] & 0xFF;

                    switch (output.length - i) {
                        default:
                            output[i + 3] = (byte) (val & 0x03);
                        case 3:
                            output[i + 2] = (byte) ((val >> 0x02) & 0x03);
                        case 2:
                            output[i + 1] = (byte) ((val >> 0x04) & 0x03);
                        case 1:
                            output[i] = (byte) (val >> 0x06);
                    }
                }
                break;
            case 1:
                for (int i = 0; i < output.length; i += 8) {
                    final int val = input[(i >> 3)] & 0xFF;

                    switch (output.length - i) {
                        default:
                            output[i + 7] = (byte) (val & 0x01);
                        case 7:
                            output[i + 6] = (byte) ((val >> 0x01) & 0x01);
                        case 6:
                            output[i + 5] = (byte) ((val >> 0x02) & 0x01);
                        case 5:
                            output[i + 4] = (byte) ((val >> 0x03) & 0x01);
                        case 4:
                            output[i + 3] = (byte) ((val >> 0x04) & 0x01);
                        case 3:
                            output[i + 2] = (byte) ((val >> 0x05) & 0x01);
                        case 2:
                            output[i + 1] = (byte) ((val >> 0x06) & 0x01);
                        case 1:
                            output[i] = (byte) (val >> 0x07);
                    }
                }
                break;
        }
    }

    /**
     * <p>Handle palette chunk</p>
     *
     * @param header the header of the image
     * @param chunk  the chunk
     *
     * @throws IOException indicates if the image has invalid palette
     */
    private void handlePalette(ImageHeader header, ImageChunk chunk) throws IOException {
        final int entries = chunk.mData.length / 3;
        if (entries < 1 || entries > 256 || entries % 3 != 0) {
            throw new IOException("Wrong length for palette chunk");
        }
        header.mPalette = chunk.mData;
    }

    /**
     * <p>Handle transparency chunk</p>
     *
     * @param header the header of the image
     * @param chunk  the chunk
     *
     * @throws IOException indicates if the image has invalid transparency
     */
    private void handleTransparency(ImageHeader header, ImageChunk chunk) throws IOException {
        switch (header.mPixelType) {
            case TYPE_GREY_SCALE:
                if (chunk.mData.length != 0x02) {
                    throw new IOException("Unsupported transparency for grey-scale image");
                }
                header.mTransparency = chunk.mData;
                break;
            case TYPE_TRUE_COLOR:
                if (chunk.mData.length != 0x06) {
                    throw new IOException("Unsupported transparency for true-color image");
                }
                header.mTransparency = chunk.mData;
                break;
            case TYPE_INDEXED:
                if (header.mPalette == null) {
                    throw new IOException("Missing palette chunk");
                }
                header.mTransparency = new byte[header.mPalette.length / 3];
                Arrays.fill(header.mTransparency, (byte) 0xFF);
                System.arraycopy(chunk.mData, 0, header.mTransparency, 0, chunk.mData.length);
                break;
            default:
                throw new IOException("Unsupported transparency");
        }
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
        switch (header.mPixelType) {
            case TYPE_GREY_SCALE:
                return header.mTransparency != null ? ImageFormat.RG : ImageFormat.RED;
            case TYPE_GREY_SCALE_ALPHA:
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return ImageFormat.RG;
            case TYPE_INDEXED:
                if (header.mImageDepth == 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return header.mTransparency != null ? ImageFormat.RGBA : ImageFormat.RGB;
            case TYPE_TRUE_COLOR:
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return header.mTransparency != null ? ImageFormat.RGBA : ImageFormat.RGB;
            case TYPE_TRUE_COLOR_ALPHA:
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return ImageFormat.RGBA;
        }
        throw new IOException("Uncompressed format not supported");
    }

    /**
     * <p>Get the uncompressed format length of the image</p>
     *
     * @param header the header of the image
     *
     * @return the uncompressed format length of the image
     *
     * @throws IOException indicates if the format is unsupported
     */
    private int getUncompressedFormatLength(ImageHeader header) throws IOException {
        switch (header.mPixelType) {
            case TYPE_GREY_SCALE:
                return 0x01;
            case TYPE_GREY_SCALE_ALPHA:
                return 0x02;
            case TYPE_INDEXED:
                return 0x01;
            case TYPE_TRUE_COLOR:
                return 0x03;
            case TYPE_TRUE_COLOR_ALPHA:
                return 0x04;
        }
        throw new IOException("Uncompressed format not supported");
    }

    /**
     * <p>Helper method to read all byte(s) from an {@link InflaterInputStream}</p>
     */
    private int readAll(InflaterInputStream input, byte[] data) throws IOException {
        int length = data.length;
        int offset = 0;

        while (length > 0) {
            final int read = input.read(data, offset, length);

            length -= read;
            offset += read;
        }
        return offset;
    }
}
