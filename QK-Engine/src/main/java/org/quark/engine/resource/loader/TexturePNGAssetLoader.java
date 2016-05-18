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
package org.quark.engine.resource.loader;

import org.quark.engine.media.opengl.texture.*;
import org.quark.engine.resource.AssetKey;
import org.quark.engine.resource.AssetLoader;
import org.quark.engine.resource.AssetManager;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

/**
 * Encapsulate an {@link AssetLoader} for loading PNG texture(s).
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
public final class TexturePNGAssetLoader implements AssetLoader<Texture, Texture.Descriptor> {
    /**
     * Encapsulate the texture structure for any PNG texture.
     */
    private final static class PNGHeader {
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
     * Encapsulate a chunk within the PNG texture.
     */
    private final static class PNGChunk {
        public byte[] mType;
        public byte[] mData;
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
        if (in.readLong() != 0x89504E470D0A1A0AL) {
            throw new IOException("Trying to read an invalid PNG texture.");
        }

        //!
        //! Contain(s) the compressed data of the png texture
        //!
        final ByteArrayOutputStream data = new ByteArrayOutputStream();

        //!
        //! Read the header of the png texture
        //!
        final PNGHeader header = parseHeader(parseChunk(in));
        if (header.mPixelInterlace != 0) {
            throw new IOException("Interlace png images are not supported.");
        } else if (header.mPixelFilter != 0) {
            throw new IOException("Filtered png images are not supported.");
        } else if (header.mPixelCompression != 0) {
            throw new IOException("Custom compressed png images are not supported.");
        }

        //!
        //! Read other chunk of the png texture
        //!
        while (in.available() > 0) {
            final PNGChunk chunk = parseChunk(in);

            switch (new String(chunk.mType, "UTF-8")) {
                case "PLTE":
                    parseChunkPalette(header, chunk);
                    break;
                case "tRNS":
                    parseChunkTransparency(header, chunk);
                    break;
                case "IDAT":
                    data.write(chunk.mData);
                    break;
            }
        }

        if (header.mImageHeight == -1) {
            return new Texture1D(descriptor.getFormat(), descriptor.getFilter(), descriptor.getBorderX(),
                    parseImages(data.toByteArray(), header));
        }
        return new Texture2D(descriptor.getFormat(), descriptor.getFilter(), descriptor.getBorderX(),
                descriptor.getBorderY(), parseImages(data.toByteArray(), header));
    }

    /**
     * <p>Parse a chunk of the texture from {@link DataInputStream}</p>
     *
     * @param in the input  of the texture expressed as <code>DataInputStream</code>
     *
     * @return a reference to the chunk of the texture
     *
     * @throws IOException indicates the texture has invalid chunk
     */
    private PNGChunk parseChunk(DataInputStream in) throws IOException {
        final int length = in.readInt();

        if (length >= 0) {
            final PNGChunk chunk = new PNGChunk();
            chunk.mType = new byte[0x04];
            in.readFully(chunk.mType);

            chunk.mData = new byte[length];
            in.readFully(chunk.mData);

            //!
            //! Reserve bytes
            //!
            in.skipBytes(0x04);
            return chunk;
        } else {
            throw new IOException("File is too large to read");
        }
    }

    /**
     * <p>Parse the header of the texture from {@link PNGChunk}</p>
     *
     * @param chunk the chunk  of the texture expressed as <code>PNGChunk</code>
     *
     * @return a reference to the header of the texture
     *
     * @throws IOException indicates the texture has invalid header
     */
    private PNGHeader parseHeader(PNGChunk chunk) throws IOException {
        if (!Objects.equals(new String(chunk.mType, "UTF-8"), "IHDR")) {
            throw new IOException("File contains invalid header.");
        }

        final PNGHeader header = new PNGHeader();

        try (final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(chunk.mData))) {
            header.mImageWidth = stream.readInt();
            header.mImageHeight = stream.readInt();
            header.mImageDepth = stream.readByte();
            header.mPixelType = stream.readByte();
            header.mPixelCompression = stream.readByte();
            header.mPixelFilter = stream.readByte();
            header.mPixelInterlace = stream.readByte();
        }
        return header;
    }

    /**
     * <p>Parse the images of the texture from {@literal byte[]}</p>
     *
     * @param in     the input  of the texture expressed as <code>byte[]</code>
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     *
     * @return a collection that contain(s) all image(s)
     *
     * @throws IOException indicates failing parsing any of the image(s)
     */
    private List<Image> parseImages(byte[] in, PNGHeader header) throws IOException {
        final ImageFormat imageFormat = getUncompressedFormat(header);
        final ByteBuffer imageBuffer = ByteBuffer.allocateDirect(
                header.mImageWidth * header.mImageHeight * imageFormat.eComponent).order(ByteOrder.nativeOrder());

        final int imageFormatScanLength
                = getUncompressedFormatBytesPerPixel(header);
        final int imageLineSize
                = ((header.mImageWidth * header.mImageDepth + 7) / 8) * imageFormatScanLength;


        final byte[] zipFilter = new byte[1];
        final byte[] zipBuffer1 = new byte[imageLineSize];
        final byte[] zipBuffer2 = new byte[imageLineSize];
        final byte[] indexedBuffer = new byte[header.mImageWidth];

        //!
        //! Deflate the data of the texture
        //!
        final Inflater inflater = new Inflater();
        inflater.setInput(in);

        try {
            for (int y = 0; y < header.mImageHeight; ++y) {
                inflater.inflate(zipFilter);
                inflater.inflate(zipBuffer1);

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
                        for (int i = imageFormatScanLength; i < zipBuffer1.length; i++) {
                            zipBuffer1[i] += zipBuffer1[i - imageFormatScanLength];
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
                        for (int i = 0; i < imageFormatScanLength; i++) {
                            zipBuffer1[i] +=
                                    (zipBuffer2[i] & 0xFF) >>> 1;
                        }
                        for (int i = imageFormatScanLength; i < zipBuffer1.length; i++) {
                            zipBuffer1[i] +=
                                    ((zipBuffer2[i] & 0xFF) + (zipBuffer1[i - imageFormatScanLength] & 0xFF)) >>> 1;
                        }
                        break;

                    case 4:
                        //!
                        //! The Paeth filter computes a simple linear function of the three neighboring pixels
                        //! (left, above, upper left), then chooses as predictor the neighboring pixel closest
                        //! to the computed value
                        //!
                        for (int i = 0; i < imageFormatScanLength; i++) {
                            zipBuffer1[i] += zipBuffer2[i];
                        }
                        for (int i = imageFormatScanLength; i < zipBuffer1.length; i++) {
                            final int a = zipBuffer1[i - imageFormatScanLength] & 0xFF;
                            final int b = zipBuffer2[i] & 0xFF;
                            int c = zipBuffer2[i - imageFormatScanLength] & 0xFF;
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
                        throw new IOException("Invalid filter in scanline.");
                }

                System.arraycopy(zipBuffer1, 0, zipBuffer2, 0, zipBuffer1.length);

                //!
                //! Proceed to parse each color type of the texture
                //!
                parseImagesBytes(header.mPixelType == 0x03
                        ? parseImagesBytesIndexed(zipBuffer1, indexedBuffer, header) : zipBuffer1, imageBuffer, header);
            }

        } catch (Exception exception) {
            throw new IOException(exception);
        } finally {
            inflater.end();
        }
        return Collections.singletonList(new Image(imageFormat,
                header.mImageWidth,
                header.mImageHeight, 0, 0, (ByteBuffer) imageBuffer.flip()));
    }

    /**
     * <p>Parse the images's bytes of the texture from {@literal byte[]}</p>
     *
     * @param in     the input  of the texture expressed as <code>byte[]</code>
     * @param out    the output of the texture expressed as <code>ByteBuffer</code>
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     *
     * @throws IOException indicates when failing parsing any of the images
     */
    private void parseImagesBytes(byte[] in, ByteBuffer out, PNGHeader header) throws IOException {
        switch (header.mPixelType) {
            case 0: // Greyscale
                if (header.mTransparency == null) {
                    out.put(in);
                } else {
                    for (byte pixel : in) {
                        out.put(pixel).put(pixel == header.mTransparency[1] ? (byte) 0x00 : (byte) 0xFF);
                    }
                }
                break;
            case 2: // True-color
                if (header.mTransparency == null) {
                    out.put(in);
                } else {
                    for (int i = 0; i < in.length; i += 3) {
                        out.put(in, i, 0x03).put(in[i] == header.mTransparency[1] &&
                                in[i + 1] == header.mTransparency[3] &&
                                in[i + 2] == header.mTransparency[5] ? (byte) 0x00 : (byte) 0xFF);
                    }
                }
                break;
            case 3: // Indexed
                if (header.mTransparency == null) {
                    for (byte pixel : in) {
                        final int index = pixel & 0xFF;
                        out.put(header.mPalette[index * 3]).put(header.mPalette[index * 3 + 1])
                                .put(header.mPalette[index * 3 + 2]).put(header.mTransparency[index]);
                    }
                } else {
                    for (byte pixel : in) {
                        final int index = pixel & 0xFF;
                        out.put(header.mPalette[index * 3]).put(header.mPalette[index * 3 + 1])
                                .put(header.mPalette[index * 3 + 2]);
                    }
                }
                break;
            case 4: // Greyscale-alpha
            case 6: // Truecolor-alpha
                out.put(in);
                break;
        }

    }

    /**
     * <p>Parse the images's bytes indexed of the texture from {@literal byte[]}</p>
     *
     * @param in     the input  of the texture expressed as <code>byte[]</code>
     * @param out    the output of the texture expressed as <code>byte[]</code>
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     *
     * @return the image's bytes indexed parsed
     */
    private byte[] parseImagesBytesIndexed(byte[] in, byte[] out, PNGHeader header) {
        switch (header.mImageDepth) {
            case 8:
                return in;
            case 4:
                for (int i = 0; i < out.length; i += 2) {
                    final int val = in[(i >> 1)] & 0xFF;

                    switch (out.length - i) {
                        default:
                            out[i + 1] = (byte) (val & 0x0F);
                        case 1:
                            out[i] = (byte) (val >> 0x04);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < out.length; i += 4) {
                    final int val = in[(i >> 2)] & 0xFF;

                    switch (out.length - i) {
                        default:
                            out[i + 3] = (byte) (val & 0x03);
                        case 3:
                            out[i + 2] = (byte) ((val >> 0x02) & 0x03);
                        case 2:
                            out[i + 1] = (byte) ((val >> 0x04) & 0x03);
                        case 1:
                            out[i] = (byte) (val >> 0x06);
                    }
                }
                break;
            case 1:
                for (int i = 0; i < out.length; i += 8) {
                    final int val = in[(i >> 3)] & 0xFF;

                    switch (out.length - i) {
                        default:
                            out[i + 7] = (byte) (val & 0x01);
                        case 7:
                            out[i + 6] = (byte) ((val >> 0x01) & 0x01);
                        case 6:
                            out[i + 5] = (byte) ((val >> 0x02) & 0x01);
                        case 5:
                            out[i + 4] = (byte) ((val >> 0x03) & 0x01);
                        case 4:
                            out[i + 3] = (byte) ((val >> 0x04) & 0x01);
                        case 3:
                            out[i + 2] = (byte) ((val >> 0x05) & 0x01);
                        case 2:
                            out[i + 1] = (byte) ((val >> 0x06) & 0x01);
                        case 1:
                            out[i] = (byte) (val >> 0x07);
                    }
                }
                break;
        }
        return out;
    }

    /**
     * <p>Parse the <b>Palette</b> chunk of the texture from {@link PNGChunk}</p>
     *
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     * @param chunk  the chunk  of the texture expressed as <code>PNGChunk</code>
     *
     * @throws IOException indicates whenever the texture has invalid palette
     */
    private void parseChunkPalette(PNGHeader header, PNGChunk chunk) throws IOException {
        final int entries = chunk.mData.length / 3;
        if (entries < 1 || entries > 256) {
            throw new IOException("PLTE chunk has wrong length.");
        }
        header.mPalette = chunk.mData;
    }

    /**
     * <p>Parse the <b>Transparency</b> chunk of the texture from {@link PNGChunk}</p>
     *
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     * @param chunk  the chunk  of the texture expressed as <code>PNGChunk</code>
     *
     * @throws IOException indicates whenever the texture has invalid transparency
     */
    private void parseChunkTransparency(PNGHeader header, PNGChunk chunk) throws IOException {
        switch (header.mPixelType) {
            case 0: // Greyscale
                if (chunk.mData.length != 0x02) {
                    throw new IOException("Unsupported transparency for greyscale image.");
                }
                header.mTransparency = chunk.mData;
                break;
            case 2: // True-color
                if (chunk.mData.length != 0x06) {
                    throw new IOException("Unsupported transparency for true color image.");
                }
                header.mTransparency = chunk.mData;
                break;
            case 3: // Indexed
                if (header.mPalette == null) {
                    throw new IOException("tRNS chunk without PLTE chunk.");
                }
                header.mTransparency = new byte[header.mPalette.length / 3];
                Arrays.fill(header.mTransparency, (byte) 0xFF);
                System.arraycopy(chunk.mData, 0, header.mTransparency, 0, chunk.mData.length);
                break;
            default:
                throw new IOException("Unsupported transparency type.");
        }
    }

    /**
     * <p>Get the uncompressed format</p>
     *
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     *
     * @return the uncompressed format of the image
     *
     * @throws IOException indicates whenever the format is unsupported
     */
    private ImageFormat getUncompressedFormat(PNGHeader header) throws IOException {
        switch (header.mPixelType) {
            case 0: // Greyscale
                return header.mTransparency != null ? ImageFormat.RG : ImageFormat.RED;
            case 2: // True-color
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return header.mTransparency != null ? ImageFormat.RGBA : ImageFormat.RGB;
            case 3: // Indexed
                if (header.mImageDepth == 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return header.mTransparency != null ? ImageFormat.RGBA : ImageFormat.RGB;
            case 4: // Greyscale-alpha
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return ImageFormat.RG;
            case 6: // True-alpha
                if (header.mImageDepth != 0x08 && header.mImageDepth != 0x10) {
                    throw new IOException("Unsupported bit depth: " + header.mImageDepth);
                }
                return ImageFormat.RGBA;
            default:
                throw new IOException("Unsupported format type.");
        }
    }

    /**
     * <p>Get the uncompressed bytes per pixel of the internal format</p>
     *
     * @param header the header of the texture expressed as <code>PNGHeader</code>
     *
     * @return the uncompressed bytes per pixel of the internal format
     *
     * @throws IOException indicates whenever the format is unsupported
     */
    private int getUncompressedFormatBytesPerPixel(PNGHeader header) throws IOException {
        switch (header.mPixelType) {
            case 0: // Greyscale
                return 1;
            case 2: // True-color
                return 3;
            case 3: // Indexed
                return 1;
            case 4: // Greyscale-alpha
                return 2;
            case 6: // True-alpha
                return 4;
            default:
                throw new IOException("Unsupported format type.");
        }
    }
}
