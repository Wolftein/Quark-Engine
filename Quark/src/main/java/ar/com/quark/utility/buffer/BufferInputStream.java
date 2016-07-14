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
package ar.com.quark.utility.buffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>BufferInputStream</code> encapsulate an {@link InputStream} from {@link Int8Buffer}.
 */
public final class BufferInputStream extends InputStream {
    private final Int8Buffer mBuffer;

    /**
     * Hold the mark position that the stream will be reset to.
     */
    private int mMark;

    /**
     * <p>Constructor</p>
     */
    public BufferInputStream(Int8Buffer buffer) {
        mBuffer = buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (!mBuffer.hasRemaining()) {
            return -1;
        }
        return mBuffer.read() & 0xFF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (!mBuffer.hasRemaining()) {
            return -1;
        }
        return mBuffer.read(bytes, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        return mBuffer.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long count) throws IOException {
        final int skip = (int) (mBuffer.position() + count);

        mBuffer.position(skip);

        return skip;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mark(int limit) {
        mMark = mBuffer.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() throws IOException {
        mBuffer.position(mMark);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
        return true;
    }
}
