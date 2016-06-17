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
package org.quark.backend.teavm.utility.array;

import org.quark.system.utility.array.Array;
import org.teavm.jso.typedarrays.Int8Array;

/**
 * <a href="http://teavm.org/">TeaVM</a> implementation for {@link Array}.
 */
public abstract class TeaVMArray<A extends Array> implements Array<A> {
    /**
     * Hold a reference to the typed-array.
     */
    private final Int8Array mBuffer;

    private int mPosition;
    private int mLimit;

    /**
     * <p>Constructor</p>
     */
    public TeaVMArray(Int8Array array) {
        mBuffer = array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int capacity() {
        return mBuffer.getByteLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int limit() {
        return mLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A limit(int limit) {
        mLimit = limit;
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int position() {
        return mPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A position(int position) {
        mPosition = position;
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int remaining() {
        return mLimit - mPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() {
        return remaining() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A clear() {
        mPosition = 0;
        mLimit = mBuffer.getByteLength();
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A flip() {
        mLimit = mPosition;
        mPosition = 0;
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A rewind() {
        mPosition = 0;
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> B data() {
        return (B) mBuffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(byte value) {
        mBuffer.set(mPosition++, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(int index, byte value) {
        mBuffer.set(index, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(short value) {
        writeInt8((byte) ((value & 0xFF00) >> 8));
        writeInt8((byte) ((value & 0x00FF) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int index, short value) {
        writeInt8(index, (byte) ((value & 0xFF00) >> 8));
        writeInt8(index + 1, (byte) ((value & 0x00FF) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int value) {
        writeInt8((byte) ((value & 0xFF000000) >> 24));
        writeInt8((byte) ((value & 0x00FF0000) >> 16));
        writeInt8((byte) ((value & 0x0000FF00) >> 8));
        writeInt8((byte) ((value & 0x000000FF) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int index, int value) {
        writeInt8(index, (byte) ((value & 0xFF000000) >> 24));
        writeInt8(index + 1, (byte) ((value & 0x00FF0000) >> 16));
        writeInt8(index + 2, (byte) ((value & 0x0000FF00) >> 8));
        writeInt8(index + 3, (byte) ((value & 0x000000FF) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt64(long value) {
        writeInt8((byte) ((value & 0xFF00000000000000L) >> 54));
        writeInt8((byte) ((value & 0x00FF000000000000L) >> 48));
        writeInt8((byte) ((value & 0x0000FF0000000000L) >> 40));
        writeInt8((byte) ((value & 0x000000FF00000000L) >> 32));
        writeInt8((byte) ((value & 0x00000000FF000000L) >> 24));
        writeInt8((byte) ((value & 0x0000000000FF0000L) >> 16));
        writeInt8((byte) ((value & 0x000000000000FF00L) >> 8));
        writeInt8((byte) ((value & 0x00000000000000FFL) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt64(int index, long value) {
        writeInt8(index, (byte) ((value & 0xFF00000000000000L) >> 54));
        writeInt8(index + 1, (byte) ((value & 0x00FF000000000000L) >> 48));
        writeInt8(index + 2, (byte) ((value & 0x0000FF0000000000L) >> 40));
        writeInt8(index + 3, (byte) ((value & 0x000000FF00000000L) >> 32));
        writeInt8(index + 4, (byte) ((value & 0x00000000FF000000L) >> 24));
        writeInt8(index + 5, (byte) ((value & 0x0000000000FF0000L) >> 16));
        writeInt8(index + 6, (byte) ((value & 0x000000000000FF00L) >> 8));
        writeInt8(index + 7, (byte) ((value & 0x00000000000000FFL) >> 0));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(float value) {
        writeInt32(Float.floatToIntBits(value));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(int index, float value) {
        writeInt32(index, Float.floatToIntBits(value));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat64(double value) {
        writeInt64(Double.doubleToLongBits(value));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat64(int index, double value) {
        writeInt64(index, Double.doubleToLongBits(value));
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readInt8() {
        return mBuffer.get(mPosition++);
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
        return (short) ((readInt8() << 0x08) & 0x0000ff00 | (readInt8() << 0x00) & 0x000000ff);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readInt16(int index) {
        return (short) ((readInt8(index) << 0x08) & 0x0000ff00 | (readInt8(index + 1) << 0x00) & 0x000000ff);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32() {
        return (readInt8() << 0x18) & 0xff000000 |
                (readInt8() << 0x10) & 0x00ff0000 |
                (readInt8() << 0x08) & 0x0000ff00 |
                (readInt8() << 0x00) & 0x000000ff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32(int index) {
        return (readInt8(index) << 0x18) & 0xff000000 |
                (readInt8(index + 1) << 0x10) & 0x00ff0000 |
                (readInt8(index + 2) << 0x08) & 0x0000ff00 |
                (readInt8(index + 3) << 0x00) & 0x000000ff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long readInt64() {
        return ((long) readInt8() << 0x36) & 0xff00000000000000L |
                ((long) readInt8() << 0x30) & 0x00ff000000000000L |
                ((long) readInt8() << 0x28) & 0x0000ff0000000000L |
                ((long) readInt8() << 0x20) & 0x000000ff00000000L |
                ((long) readInt8() << 0x18) & 0x00000000ff000000L |
                ((long) readInt8() << 0x10) & 0x0000000000ff0000L |
                ((long) readInt8() << 0x08) & 0x000000000000ff00L |
                ((long) readInt8() << 0x00) & 0x00000000000000ffL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long readInt64(int index) {
        return ((long) readInt8(index) << 0x36) & 0xff00000000000000L |
                ((long) readInt8(index + 1) << 0x30) & 0x00ff000000000000L |
                ((long) readInt8(index + 2) << 0x28) & 0x0000ff0000000000L |
                ((long) readInt8(index + 3) << 0x20) & 0x000000ff00000000L |
                ((long) readInt8(index + 4) << 0x18) & 0x00000000ff000000L |
                ((long) readInt8(index + 5) << 0x10) & 0x0000000000ff0000L |
                ((long) readInt8(index + 6) << 0x08) & 0x000000000000ff00L |
                ((long) readInt8(index + 7) << 0x00) & 0x00000000000000ffL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32() {
        return Float.intBitsToFloat(readInt32());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32(int index) {
        return Float.intBitsToFloat(readInt32(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double readFloat64() {
        return Double.longBitsToDouble(readInt64());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double readFloat64(int index) {
        return Double.longBitsToDouble(readInt64(index));
    }
}
