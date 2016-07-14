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
package ar.com.quark.backend.lwjgl.utility.buffer;

import ar.com.quark.utility.buffer.Buffer;

/**
 * Implementation for {@link Buffer}.
 */
public abstract class DesktopBuffer<A extends Buffer<A>, B extends java.nio.Buffer> implements Buffer<A> {
    protected final B mUnderlying;

    /**
     * Hold the offset of the buffer.
     */
    private int mOffset = 0;

    /**
     * <p>Constructor</p>
     */
    protected DesktopBuffer(B underlying) {
        mUnderlying = underlying;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int capacity() {
        return mUnderlying.capacity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int limit() {
        return mUnderlying.limit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int position() {
        return mUnderlying.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int remaining() {
        return mUnderlying.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int offset() {
        return mOffset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() {
        return mUnderlying.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public B underlying() {
        return mUnderlying;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A position(int position) {
        mUnderlying.position(position);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A offset(int offset) {
        mOffset = offset;
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A limit(int limit) {
        mUnderlying.limit(limit);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A clear() {
        mUnderlying.clear();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A flip() {
        mUnderlying.flip();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A rewind() {
        mUnderlying.rewind();
        return (A) this;
    }
}
