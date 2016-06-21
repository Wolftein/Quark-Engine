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
import org.teavm.jso.JSBody;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.platform.Platform;

/**
 * <a href="http://teavm.org/">TeaVM</a> implementation for {@link Array}.
 */
public abstract class TeaVMArray<A extends Array> implements Array<A> {
    /**
     * Hold a reference to the typed-array.
     */
    private final DataView mBuffer;

    private int mPosition = 0;
    private int mLimit;

    /**
     * <p>Constructor</p>
     */
    public TeaVMArray(ArrayBuffer array) {
        mBuffer = allocate(array);

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
    public A writeInt8(int index, byte value) {
        mBuffer.setInt8(index, value);
        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(short value) {
        mBuffer.setInt16(mPosition, value, true);

        mPosition += 2;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(short[] value, int offset, int count) {
        copyWrapped(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 2);

        mPosition += count;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt16(int index, short value) {
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
        copyWrapped(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 4);

        mPosition += count;

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
    public A writeInt64(long value) {
        return (A) this;    // NOT_SUPPORTED
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt64(long[] value, int offset, int count) {
        return (A) this;    // NOT_SUPPORTED
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeInt64(int index, long value) {
        return (A) this;    // NOT_SUPPORTED
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
        copyWrapped(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 4);

        mPosition += count;

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
    public A writeFloat64(double value) {
        mBuffer.setFloat64(mPosition, value, true);

        mPosition += 8;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat64(double[] value, int offset, int count) {
        copyWrapped(mBuffer.getBuffer(), mPosition, Platform.getPlatformObject(value), offset, count * 8);

        mPosition += count;

        return (A) this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A writeFloat64(int index, double value) {
        mBuffer.setFloat64(index, value, true);
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
    public long readInt64() {
        return Double.doubleToLongBits(readFloat64());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long readInt64(int index) {
        return Double.doubleToLongBits(readFloat64(index));
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
    public double readFloat64() {
        final double value = mBuffer.getFloat64(mPosition, true);

        mPosition += 0x08;

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double readFloat64(int index) {
        return mBuffer.getFloat64(index, true);
    }

    /**
     * @see <a href="https://developer.mozilla.org/docs/Web/JavaScript/Reference/Global_Objects/DataView">Reference</a>
     */
    @JSBody(params = {"buffer"}, script = "return new DataView(buffer)")
    private static native DataView allocate(ArrayBuffer buffer);

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"dst", "offset", "src", "from", "length"},
            script = "var array1 = new Uint8Array(dst, offset, length);" +
                     "var array2 = new Uint8Array(src.data, from, length); array1.set(array2);")
    public static native void copy(JSObject dst, int offset, JSObject src, int from, int length);

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"dst", "offset", "src", "from", "length"},
            script = "var array1 = new Uint8Array(dst, offset, length);" +
                     "var array2 = new Uint8Array(src.data.buffer, from, length); array1.set(array2);")
    public static native void copyWrapped(JSObject dst, int offset, JSObject src, int from, int length);

    /**
     * @see <a href="https://groups.google.com/forum/#!topic/teavm/NswjUF1DFlo">Unsafe</a>
     */
    @JSBody(params = {"dst", "offset", "src", "from", "length"},
            script = "var array1 = new Uint8Array(dst.data.buffer, offset, length);" +
                     "var array2 = new Uint8Array(src, from, length); array1.set(array2);")
    public static native void read(JSObject dst, int offset, JSObject src, int from, int length);

    /**
     * <p>Perform a memory-copy operation</p>
     */
    public static <T extends Array<?>> int copy(T array, byte[] buffer, int offset, int length) {
        length = Math.min(length, array.remaining());

        read(Platform.getPlatformObject(buffer),
                offset, array.<DataView>data().getBuffer(), array.position(), length);

        array.position(array.position() + length);

        return length;
    }

    /**
     * @see <a href="https://developer.mozilla.org/docs/Web/JavaScript/Reference/Global_Objects/DataView">Reference</a>
     */
    public interface DataView extends JSObject {
        @JSProperty
        ArrayBuffer getBuffer();

        @JSProperty
        int getByteLength();

        @JSProperty
        int getByteOffset();

        @JSMethod
        byte getInt8(int index);

        @JSMethod
        short getUInt8(int index);

        @JSMethod
        short getInt16(int index, boolean isLittleEndian);

        @JSMethod
        int getUInt16(int index, boolean isLittleEndian);

        @JSMethod
        int getInt32(int index, boolean isLittleEndian);

        @JSMethod
        long getUInt32(int index, boolean isLittleEndian);

        @JSMethod
        float getFloat32(int index, boolean isLittleEndian);

        @JSMethod
        double getFloat64(int index, boolean isLittleEndian);

        @JSMethod
        void setInt8(int index, int value);

        @JSMethod
        void setUInt8(int index, int value);

        @JSMethod
        void setInt16(int index, int value, boolean isLittleEndian);

        @JSMethod
        void setUInt16(int index, int value, boolean isLittleEndian);

        @JSMethod
        void setInt32(int index, int value, boolean isLittleEndian);

        @JSMethod
        void setUInt32(int index, long value, boolean isLittleEndian);

        @JSMethod
        void setFloat32(int index, float value, boolean isLittleEndian);

        @JSMethod
        void setFloat64(int index, double value, boolean isLittleEndian);
    }
}
