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
package org.quark_engine.render.storage;

import org.quark_engine.render.Render;
import org.quark_engine.system.utility.Disposable;
import org.quark_engine.system.utility.Manageable;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.quark_engine.Quark.QkRender;

/**
 * <code>Storage</code> encapsulate a buffer that store an array of un-formatted memory allocated by the GPU.
 * <p>
 * These can be used to store vertex data, pixel data retrieved from images or the frame-buffer.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public class Storage<A extends Buffer> extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);

    /**
     * Indicates that the previous contents of the specified range may be discarded.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_INVALIDATE = 0x04;

    /**
     * Indicates that the previous contents of the entire storage may be discarded.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_INVALIDATE_ALL = 0x08;

    /**
     * Indicates that the server should not attempt to synchronize pending operations.
     * <p>
     * {@since OpenGL 3.0}
     */
    public final static int ACCESS_UNSYNCHRONIZED = 0x20;


    private final StorageType mType;
    private final StorageTarget mTarget;
    private final StorageMode mMode;
    private final VertexFormat mFormat;
    private final long mCapacity;

    /**
     * Hold the underlying factory of the storage, for transparency between client and server side storage(s).
     */
    private final Factory<A> mFactory;

    /**
     * <p>Constructor</p>
     */
    public Storage(StorageType type, StorageTarget target, StorageMode mode, VertexFormat format, long capacity) {
        mType = type;
        mTarget = target;
        mMode = mode;
        mFormat = format;
        mCapacity = format.eLength * capacity;
        mFactory = (type == StorageType.CLIENT) ? new BufferClientFactory() : new BufferServerFactory();

        setUpdate(CONCEPT_DATA);
    }

    /**
     * <p>Get the type of the storage</p>
     *
     * @return the type of the storage
     */
    public final StorageType getType() {
        return mType;
    }

    /**
     * <p>Get the target of the storage</p>
     *
     * @return the target of the storage
     */
    public final StorageTarget getTarget() {
        return mTarget;
    }

    /**
     * <p>Get the type of the storage</p>
     *
     * @return the type of the storage
     */
    public final StorageMode getMode() {
        return mMode;
    }

    /**
     * <p>Get the capacity of the storage (in bytes)</p>
     *
     * @return the capacity of the storage (in bytes)
     */
    public final long getCapacity() {
        return mCapacity;
    }

    /**
     * <p>Get the format of the storage</p>
     *
     * @return the format of the storage
     */
    public final VertexFormat getFormat() {
        return mFormat;
    }

    /**
     * @see Render#create(Storage)
     */
    public final void create() {
        QkRender.create(this);
    }

    /**
     * @see Render#create(Storage)
     */
    public final void delete() {
        QkRender.delete(this);
    }

    /**
     * @see Render#acquire(Storage)
     */
    public final void acquire() {
        QkRender.acquire(this);
    }

    /**
     * @see Render#update(Storage)
     */
    public final void update() {
        QkRender.update(this);
    }

    /**
     * @see Manageable#release()
     * @see Render#release(Storage)
     */
    @Override
    public final void release() {
        QkRender.release(this);
    }

    /**
     * @see Render#map(Storage)
     */
    public final A map() {
        return mFactory.map(QkRender);
    }

    /**
     * @see Render#map(Storage, int)
     */
    public final A map(int access) {
        return mFactory.map(QkRender, access);
    }

    /**
     * @see Render#map(Storage, int, long, long)
     */
    public final A map(int access, long offset, long length) {
        return mFactory.map(QkRender, access, offset, length);
    }

    /**
     * @see Render#unmap(Storage)
     */
    public final void unmap() {
        mFactory.unmap(QkRender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void dispose() {
        QkRender.dispose(this);
    }

    /**
     * <code>Factory</code> encapsulate the definition of a buffer within a <code>Storage</code>.
     */
    private interface Factory<A extends Buffer> {
        A map(Render gl);

        A map(Render gl, int access);

        A map(Render gl, int access, long offset, long length);

        void unmap(Render gl);
    }

    /**
     * <code>Factory</code> implementation for {@link StorageType#CLIENT}
     */
    private final class BufferClientFactory implements Factory<A> {
        private final ByteBuffer mData;

        /**
         * <p>Constructor</p>
         */
        public BufferClientFactory() {
            mData = ByteBuffer.allocateDirect((int) (Storage.this.mCapacity)).order(ByteOrder.nativeOrder());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl) {
            return map(gl, 0, 0L, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access) {
            return map(gl, access, 0L, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access, long offset, long length) {
            //!
            //! We only support offset and length (in the client-side) due to some limitation
            //!
            //! Missing: INVALIDATE, INVALIDATE_ALL requires mem-set which requires JNI.
            //!
            mData.position((int) offset).limit((int) length);

            return cast(mData, Storage.this.mFormat);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Render gl) {
            mData.flip();

            Storage.this.setUpdate(CONCEPT_DATA);
        }
    }

    /**
     * <code>Factory</code> implementation for {@link StorageType#SERVER}
     */
    private final class BufferServerFactory implements Factory<A> {
        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl) {
            return gl.map(Storage.this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access) {
            return gl.map(Storage.this, access);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access, long offset, long length) {
            return gl.map(Storage.this, access, offset, length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Render gl) {
            gl.unmap(Storage.this);
        }
    }

    /**
     * <p>Cast a {@link Buffer} that matches the {@link VertexFormat}</p>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Buffer> T cast(ByteBuffer buffer, VertexFormat format) {
        switch (format) {
            case BYTE:
            case UNSIGNED_BYTE:
                return (T) buffer;
            case HALF_FLOAT:
            case SHORT:
            case UNSIGNED_SHORT:
                return (T) buffer.asShortBuffer();
            case INT:
            case UNSIGNED_INT:
                return (T) buffer.asIntBuffer();
            case FLOAT:
                return (T) buffer.asFloatBuffer();
        }
        throw new IllegalArgumentException("Trying to parser a buffer with invalid format");
    }
}
