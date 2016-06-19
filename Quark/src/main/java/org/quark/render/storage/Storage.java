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
package org.quark.render.storage;

import org.quark.render.Render;
import org.quark.system.utility.Disposable;
import org.quark.system.utility.Manageable;
import org.quark.system.utility.array.Array;
import org.quark.system.utility.array.ArrayFactory;

import static org.quark.Quark.QKRender;

/**
 * <code>Storage</code> encapsulate a buffer that store an array of un-formatted memory allocated by the GPU.
 * <p>
 * These can be used to store vertex data, pixel data retrieved from images or the frame-buffer.
 */
public class Storage<A extends Array<?>> extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);
    public final static int CONCEPT_DATA_CHANGE = (1 << 1);

    /**
     * Indicates that the previous contents of the specified range may be discarded.
     *
     * @since OpenGL 3.0
     */
    public final static int ACCESS_INVALIDATE = Render.GLES3.GL_ACCESS_INVALIDATE;

    /**
     * Indicates that the previous contents of the entire storage may be discarded.
     *
     * @since OpenGL 3.0
     */
    public final static int ACCESS_INVALIDATE_ALL = Render.GLES3.GL_ACCESS_INVALIDATE_ALL;

    /**
     * Indicates that the server should not attempt to synchronize pending operations.
     *
     * @since OpenGL 3.0
     */
    public final static int ACCESS_UNSYNCHRONIZED = Render.GLES3.GL_ACCESS_UNSYNCHRONIZED;

    private final StorageType mType;
    private final StorageTarget mTarget;
    private final StorageMode mMode;
    private final VertexFormat mFormat;
    private final int mCapacity;

    /**
     * Hold the underlying factory of the storage, for transparency between client and server side storage(s).
     */
    private final Factory<A> mFactory;

    /**
     * <p>Constructor</p>
     */
    public Storage(StorageType type, StorageTarget target, StorageMode mode, VertexFormat format, int capacity) {
        mType = type;
        mTarget = target;
        mMode = mode;
        mFormat = format;
        mCapacity = format.eLength * capacity;

        switch (type) {
            case CLIENT:
                mFactory = new BufferClientFactory();
                break;
            case SERVER:
                mFactory = new BufferServerFactory();
                break;
            case SERVER_MAPPED:
                mFactory = new BufferServerMappedFactory();
                break;
            default:
                throw new IllegalArgumentException("Storage type not supported.");
        }

        setUpdate(CONCEPT_DATA);
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
    public final StorageType getType() {
        return mType;
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
    public final int getCapacity() {
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
        QKRender.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void delete() {
        QKRender.delete(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void deleteAllMemory() {
        mFactory.dispose();
    }

    /**
     * @see Render#acquire(Storage)
     */
    public final void acquire() {
        QKRender.acquire(this);
    }

    /**
     * @see Render#update(Storage)
     */
    public final void update() {
        QKRender.update(this);
    }

    /**
     * @see Render#release(Storage)
     */
    public final void release() {
        QKRender.release(this);
    }

    /**
     * @see Render#map(Storage)
     */
    public final A map() {
        return mFactory.map(QKRender);
    }

    /**
     * @see Render#map(Storage, int)
     */
    public final A map(int access) {
        return mFactory.map(QKRender, access);
    }

    /**
     * @see Render#map(Storage, int, int, int)
     */
    public final A map(int access, int offset, int length) {
        return mFactory.map(QKRender, access, offset, length);
    }

    /**
     * @see Render#unmap(Storage)
     */
    public final void unmap() {
        mFactory.unmap(QKRender);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public final void dispose() {
        QKRender.dispose(this);
    }

    /**
     * <code>Factory</code> encapsulate the definition of a buffer within a <code>Storage</code>.
     */
    private interface Factory<A extends Array> {
        A map(Render gl);

        A map(Render gl, int access);

        A map(Render gl, int access, int offset, int length);

        void unmap(Render gl);

        void dispose();
    }

    /**
     * <code>Factory</code> implementation for {@link StorageType#CLIENT}
     */
    private final class BufferClientFactory implements Factory<A> {
        private A mData;

        /**
         * <p>Constructor</p>
         */
        public BufferClientFactory() {
            mData = create(Storage.this.mCapacity, Storage.this.mFormat);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl) {
            return map(gl, 0, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access) {
            return map(gl, access, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access, int offset, int length) {
            if ((access & ACCESS_INVALIDATE_ALL) != 0) {
                //!
                //! Emulate ACCESS_INVALIDATE_ALL
                //!
                mData.clear();
            }

            //!
            //! Emulate offset and length.
            //!
            mData.position(offset).limit(length);

            return mData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Render gl) {
            mData.flip();

            Storage.this.setUpdate(CONCEPT_DATA_CHANGE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose() {
            mData = ArrayFactory.free(mData);
        }
    }

    /**
     * <code>Factory</code> implementation for {@link StorageType#SERVER}
     */
    private final class BufferServerFactory implements Factory<A> {
        private A mData;

        /**
         * <p>Constructor</p>
         */
        public BufferServerFactory() {
            mData = create(Storage.this.mCapacity, Storage.this.mFormat);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl) {
            return map(gl, 0, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access) {
            return map(gl, access, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Render gl, int access, int offset, int length) {
            if (mData == null) {
                //!
                //! Create the data (if the data has been disposed).
                //!
                mData = create(length, Storage.this.mFormat);
            }

            if ((access & ACCESS_INVALIDATE_ALL) != 0) {
                //!
                //! Emulate ACCESS_INVALIDATE_ALL
                //!
                mData.clear();
            }

            //!
            //! Emulate offset and length.
            //!
            mData.position(offset).limit(length);

            return mData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Render gl) {
            mData.flip();

            Storage.this.setUpdate(CONCEPT_DATA_CHANGE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose() {
            mData = ArrayFactory.free(mData);
        }
    }

    /**
     * <code>Factory</code> implementation for {@link StorageType#SERVER_MAPPED}
     */
    private final class BufferServerMappedFactory implements Factory<A> {
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
        public A map(Render gl, int access, int offset, int length) {
            return gl.map(Storage.this, access, offset, length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Render gl) {
            gl.unmap(Storage.this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose() {
        }
    }

    /**
     * <p>Create a {@link Array} that matches the {@link VertexFormat}</p>
     */
    public static <T extends Array> T create(int capacity, VertexFormat format) {
        switch (format) {
            case BYTE:
                return (T) ArrayFactory.allocateInt8Array(capacity / format.eLength);
            case UNSIGNED_BYTE:
                return (T) ArrayFactory.allocateUInt8Array(capacity / format.eLength);
            case HALF_FLOAT:
                return (T) ArrayFactory.allocateFloat16Array(capacity / format.eLength);
            case SHORT:
                return (T) ArrayFactory.allocateInt16Array(capacity / format.eLength);
            case UNSIGNED_SHORT:
                return (T) ArrayFactory.allocateUInt16Array(capacity / format.eLength);
            case INT:
                return (T) ArrayFactory.allocateInt32Array(capacity / format.eLength);
            case UNSIGNED_INT:
                return (T) ArrayFactory.allocateUInt32Array(capacity / format.eLength);
            case FLOAT:
                return (T) ArrayFactory.allocateFloat32Array(capacity / format.eLength);
        }
        throw new IllegalArgumentException("Trying to create an array with invalid format");
    }
}
