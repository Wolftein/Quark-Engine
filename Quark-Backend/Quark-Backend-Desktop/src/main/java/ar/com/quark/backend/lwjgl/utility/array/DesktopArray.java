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
package ar.com.quark.backend.lwjgl.utility.array;

import ar.com.quark.system.utility.array.Array;

import java.nio.ByteBuffer;

/**
 * Implementation for {@link Array}.
 */
public class DesktopArray<A extends Array> implements Array<A> {
    private final ByteBuffer mBuffer;

    /**
     * <p>Constructor</p>
     */
    public DesktopArray(ByteBuffer buffer) {
        mBuffer = buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int capacity() {
        return mBuffer.capacity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int limit() {
        return mBuffer.limit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A limit(int limit) {
        mBuffer.limit(limit);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int position() {
        return mBuffer.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A position(int position) {
        mBuffer.position(position);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int remaining() {
        return mBuffer.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() {
        return mBuffer.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A clear() {
        mBuffer.clear();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A flip() {
        mBuffer.flip();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A rewind() {
        mBuffer.rewind();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer data() {
        return mBuffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(int value) {
        mBuffer.put((byte) value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(byte[] value, int offset, int count) {
        mBuffer.put(value, offset, count);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(int index, int value) {
        mBuffer.put(index, (byte) value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int value) {
        mBuffer.putShort((short) value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(short[] value, int offset, int count) {
        mBuffer.asShortBuffer().put(value, offset, count);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int index, int value) {
        mBuffer.putShort(index, (short) value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int value) {
        mBuffer.putInt(value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int[] value, int offset, int count) {
        mBuffer.asIntBuffer().put(value, offset, count);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int index, int value) {
        mBuffer.putInt(index, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(float value) {
        mBuffer.putFloat(value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(float[] value, int offset, int count) {
        mBuffer.asFloatBuffer().put(value, offset, count);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(int index, float value) {
        mBuffer.putFloat(index, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readInt8() {
        return mBuffer.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readInt8(int index) {
        return mBuffer.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readInt16() {
        return mBuffer.getShort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readInt16(int index) {
        return mBuffer.getShort(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32() {
        return mBuffer.getInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32(int index) {
        return mBuffer.getInt(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32() {
        return mBuffer.getFloat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32(int index) {
        return mBuffer.getFloat(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] value, int offset, int count) {
        final int position = mBuffer.position();

        mBuffer.get(value, offset, count);

        return mBuffer.position() - position;
    }
}