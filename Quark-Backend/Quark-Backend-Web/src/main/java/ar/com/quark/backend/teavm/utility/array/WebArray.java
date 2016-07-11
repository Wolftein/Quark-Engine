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
package ar.com.quark.backend.teavm.utility.array;

import ar.com.quark.system.utility.array.Array;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.platform.Platform;

/**
 * Implementation for {@link Array}.
 */
public class WebArray<A extends Array> implements Array<A> {
    /**
     * Hold a reference to {@link DataView} of the {@link ArrayBuffer}.
     */
    private final DataView mBuffer;

    private int mPosition = 0;
    private int mLimit;

    /**
     * <p>Constructor</p>
     */
    public WebArray(ArrayBuffer array) {
        mBuffer = DataView.create(array);

        mLimit = capacity();
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
        mLimit = capacity();
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
    public A writeInt8(int value) {
        mBuffer.setInt8(mPosition++, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(byte[] value, int offset, int count) {
        copy(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count);

        mPosition += count;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt8(int index, int value) {
        mBuffer.setInt8(index, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int value) {
        mBuffer.setInt16(mPosition, value, true);

        mPosition += 2;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(short[] value, int offset, int count) {
        copy(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 2);

        mPosition += (count * 0x02);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int index, int value) {
        mBuffer.setInt16(index, value, true);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int value) {
        mBuffer.setInt32(mPosition, value, true);

        mPosition += 4;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int[] value, int offset, int count) {
        copy(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 4);

        mPosition += (count * 0x04);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt32(int index, int value) {
        mBuffer.setInt32(index, value, true);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(float value) {
        mBuffer.setFloat32(mPosition, value, true);

        mPosition += 4;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(float[] value, int offset, int count) {
        copy(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 4);

        mPosition += (count * 0x04);

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat32(int index, float value) {
        mBuffer.setFloat32(index, value, true);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readInt8() {
        return mBuffer.getInt8(mPosition++);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readInt8(int index) {
        return mBuffer.getInt8(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readInt16() {
        final short value = mBuffer.getInt16(mPosition, true);

        mPosition += 0x02;

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short readInt16(int index) {
        return mBuffer.getInt16(index, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32() {
        final int value = mBuffer.getInt32(mPosition, true);

        mPosition += 0x04;

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readInt32(int index) {
        return mBuffer.getInt32(index, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32() {
        final float value = mBuffer.getFloat32(mPosition, true);

        mPosition += 0x04;

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float readFloat32(int index) {
        return mBuffer.getFloat32(index, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] value, int offset, int count) {
        count = Math.min(count, remaining());

        read(Platform.getPlatformObject(value), offset, mBuffer.getBuffer(), mPosition, count);

        mPosition += count;

        return count;
    }

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"dst", "offset", "src", "from", "length"},
            script = "var array1 = new Uint8Array(dst, offset, length);" +
                     "var array2 = new Uint8Array(src.data.buffer, from, length); array1.set(array2);")
    private static native void copy(ArrayBuffer dst, int offset, JSObject src, int from, int length);

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"dst", "offset", "src", "from", "length"},
            script = "var array1 = new Uint8Array(dst.data.buffer, offset, length);" +
                     "var array2 = new Uint8Array(src, from, length); array1.set(array2);")
    private static native void read(JSObject dst, int offset, ArrayBuffer src, int from, int length);

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"source", "offset", "length", "destination"},
            script = "destination.set(source.subarray(offset, offset + length));")
    public static native void copy(JSObject source, int offset, int length, JSObject destination);
}
