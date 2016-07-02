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

import ar.com.quark.render.font.FontGlyph;
import ar.com.quark.resource.AssetManager;
import ar.com.quark.render.font.Font;
import ar.com.quark.render.texture.Texture;
import ar.com.quark.render.texture.TextureFormat;
import ar.com.quark.resource.AssetKey;
import ar.com.quark.resource.AssetLoader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>FontAssetLoader</code> encapsulate an {@link AssetLoader} for loading angel code font(s).
 */
public final class FontAssetLoader implements AssetLoader<Font, Font.Descriptor> {
    /**
     * <code>FontHeader</code> represent the file format of a angel code file format.
     */
    private final static class FontHeader {
        public int mFontVersion;
        public int mFontOutline;
        public int mFontLineHeight;
        public int mFontWidth;
        public int mFontHeight;
        public int mFontPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(AssetManager manager,
            AssetKey<Font, Font.Descriptor> key, InputStream input) throws IOException {
        final DataInputStream in = new DataInputStream(input);

        if (in.readByte() != 0x42 || in.readByte() != 0x4D || in.readByte() != 0x46) {
            throw new IOException("<Font> contains an invalid header");
        }

        //!
        //! Parse header
        //!
        final FontHeader header = parseHeader(in);

        //!
        //! Parse page
        //!
        final List<String> dependencies = parsePages(in, header);

        final List<Texture> textures = new ArrayList<>(dependencies.size());

        for (final String dependency : dependencies) {
            final String absolute = key.getFolder() + dependency;

            key.setDependency(absolute);

            textures.add(manager.load(absolute,
                    new Texture.Descriptor(TextureFormat.RGBA8, key.getDescriptor().getFilter())));
        }

        //!
        //! Parse glyph
        //!
        final Map<Integer, FontGlyph> glyphs = parseGlyph(in, header);

        //!
        //! Read the kerning of the characters (if available)
        //!
        if (in.available() > 0) {
            in.skipBytes(0x01);
            in.skipBytes(0x04);
        }
        while (input.available() > 0) {
            glyphs.get(readIntLittleEndian(in)).addKerning(readIntLittleEndian(in), readShortLittleEndian(in));
        }

        key.setAsset(new Font(textures, glyphs, header.mFontLineHeight));
    }

    /**
     * <p>Parse header block</p>
     */
    private FontHeader parseHeader(DataInputStream input) throws IOException {
        final FontHeader header = new FontHeader();

        //!
        //! Get the version of the font format.
        //!
        header.mFontVersion = input.readByte();

        //!
        //! ==== INFO BLOCK ====
        //!
        input.skipBytes(0x01);
        input.skipBytes(0x04);

        input.skipBytes(0x0D);

        header.mFontOutline = (header.mFontVersion > 0x02 ? input.readByte() : 0);

        while (input.readByte() != 0x00) ;

        //!
        //! ==== COMMON BLOCK ====
        //!
        input.skipBytes(0x01);
        input.skipBytes(0x04);

        header.mFontLineHeight = readShortLittleEndian(input);

        input.skipBytes(0x02);

        header.mFontWidth = readShortLittleEndian(input);
        header.mFontHeight = readShortLittleEndian(input);
        header.mFontPages = readShortLittleEndian(input);

        input.skipBytes(0x05);
        return header;
    }

    /**
     * <p>Parse page block</p>
     */
    private List<String> parsePages(DataInputStream input, FontHeader header) throws IOException {
        final List<String> pages = new ArrayList<>(header.mFontPages);

        input.skipBytes(0x01);
        input.skipBytes(0x04);

        //!
        //! NOTE: Optimal way to construct a string from bytes.
        //!
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < header.mFontPages; i++) {
            //!
            //! Reuse the same buffer.
            //!
            buffer.setLength(0);
            buffer.setLength(32);

            int character;

            do {
                character = input.readByte() & 0xFF;

                if (character != 0x00) {
                    //!
                    //! A page name shouldn't be multi-wide string.
                    //!
                    buffer.appendCodePoint(character);
                }
            } while (character != 0x00);

            pages.add(i, buffer.toString().trim());
        }
        return pages;
    }

    /**
     * <p>Parse glyph block</p>
     */
    private Map<Integer, FontGlyph> parseGlyph(DataInputStream input, FontHeader header) throws IOException {
        input.skipBytes(0x01);

        final int length = readIntLittleEndian(input) / 20;

        //!
        //! Pre allocate the given number of glyph(s).
        //!
        final Map<Integer, FontGlyph> glyph = new HashMap<>();

        for (int i = 0; i < length; i++) {
            int cID = readIntLittleEndian(input);
            int cX = readShortLittleEndian(input);
            int cY = readShortLittleEndian(input);
            int cWidth = readShortLittleEndian(input);
            int cHeight = readShortLittleEndian(input);
            int cOffsetX = readShortLittleEndian(input);
            int cOffsetY = readShortLittleEndian(input);
            int cAdvance = readShortLittleEndian(input);
            int cPage = input.readByte();
            int cChannel = input.readByte();

            glyph.put(cID, new FontGlyph(
                    cID,
                    cX,
                    cY,
                    cWidth,
                    cHeight,
                    cOffsetX,
                    cOffsetY,
                    cAdvance + header.mFontOutline,
                    cPage,
                    (float) cX / header.mFontWidth,
                    (float) cY / header.mFontHeight,
                    (float) (cX + cWidth) / header.mFontWidth,
                    (float) (cY + cHeight) / header.mFontHeight));

            //!
            //! Set the line height of the font to match the tallest character in the list.
            //!
            header.mFontLineHeight = Math.max(cHeight + cOffsetY, header.mFontLineHeight);
        }
        return glyph;
    }

    /**
     * <b>Read a little endian integer (Required for format)</b>
     */
    private int readIntLittleEndian(DataInputStream in) throws IOException {
        final int b0 = in.readInt();
        final int b1 = (b0 >> 0) & 0xFF;
        final int b2 = (b0 >> 8) & 0xFF;
        final int b3 = (b0 >> 16) & 0xFF;
        final int b4 = (b0 >> 24) & 0xFF;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    /**
     * <b>Read a little endian short (Required for format)</b>
     */
    private int readShortLittleEndian(DataInputStream in) throws IOException {
        final int b0 = in.readShort();
        final int b1 = (b0 & 0xFF);
        final int b2 = (b0 >> 8) & 0xFF;

        return (short) (b1 << 8 | b2 << 0);
    }
}
