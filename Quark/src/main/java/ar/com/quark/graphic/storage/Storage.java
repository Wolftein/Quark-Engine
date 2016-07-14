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
package ar.com.quark.graphic.storage;

import ar.com.quark.graphic.Graphic;
import ar.com.quark.utility.Disposable;
import ar.com.quark.utility.Manageable;
import ar.com.quark.utility.buffer.Buffer;
import ar.com.quark.utility.buffer.BufferFactory;

import static ar.com.quark.Quark.QKGraphic;

/**
 * <code>Storage</code> encapsulate a buffer that store an buffer of un-formatted memory allocated by the GPU.
 * <p>
 * These can be used to store vertex data, pixel data retrieved from images or the frame-buffer.
 */
public class Storage<A extends Buffer<?>> extends Manageable implements Disposable {
    public final static int CONCEPT_DATA = (1 << 0);
    public final static int CONCEPT_DATA_CHANGE = (1 << 1);

    /**
     * Indicates that the previous contents of the specified range may be discarded.
     */
    public final static int ACCESS_INVALIDATE = Graphic.GLES3.GL_ACCESS_INVALIDATE;

    /**
     * Indicates that the previous contents of the entire storage may be discarded.
     */
    public final static int ACCESS_INVALIDATE_ALL = Graphic.GLES3.GL_ACCESS_INVALIDATE_ALL;

    /**
     * Indicates that the server should not attempt to synchronize pending operations.
     */
    public final static int ACCESS_UNSYNCHRONIZED = Graphic.GLES3.GL_ACCESS_UNSYNCHRONIZED;

    private final Factory<A> mFactory;
    private final StorageType mType;
    private final StorageTarget mTarget;
    private final StorageMode mMode;
    private final VertexFormat mFormat;
    private final int mCapacity;

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
                throw new UnsupportedOperationException();
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
     * @see Graphic#create(Storage)
     */
    public final void create() {
        QKGraphic.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void delete() {
        QKGraphic.delete(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public final void deleteAllMemory() {
        mFactory.dispose();
    }

    /**
     * @see Graphic#acquire(Storage)
     */
    public final void acquire() {
        QKGraphic.acquire(this);
    }

    /**
     * @see Graphic#update(Storage)
     */
    public final void update() {
        QKGraphic.update(this);
    }

    /**
     * @see Graphic#release(Storage)
     */
    public final void release() {
        QKGraphic.release(this);
    }

    /**
     * @see Graphic#map(Storage)
     */
    public final A map() {
        return mFactory.map(QKGraphic);
    }

    /**
     * @see Graphic#map(Storage, int)
     */
    public final A map(int access) {
        return mFactory.map(QKGraphic, access);
    }

    /**
     * @see Graphic#map(Storage, int, int, int)
     */
    public final A map(int access, int offset, int length) {
        return mFactory.map(QKGraphic, access, offset, length);
    }

    /**
     * @see Graphic#unmap(Storage)
     */
    public final void unmap() {
        mFactory.unmap(QKGraphic);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public final void dispose() {
        QKGraphic.dispose(this);
    }

    /**
     * <code>Factory</code> encapsulate the definition of a buffer within a <code>Storage</code>.
     */
    private interface Factory<A extends Buffer<?>> {
        A map(Graphic gl);

        A map(Graphic gl, int access);

        A map(Graphic gl, int access, int offset, int length);

        void unmap(Graphic gl);

        void dispose();
    }

    /**
     * Specialised implementation for {@link StorageType#CLIENT}
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
        public A map(Graphic gl) {
            return map(gl, 0, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access) {
            return map(gl, access, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access, int offset, int length) {
            if ((access & ACCESS_INVALIDATE_ALL) != 0) {
                //!
                //! Emulate ACCESS_INVALIDATE_ALL
                //!
                mData.clear();
            }
            mData.position(offset).limit(length).offset(offset);

            return mData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Graphic gl) {
            mData.flip();

            Storage.this.setUpdate(CONCEPT_DATA_CHANGE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose() {
            mData = BufferFactory.deallocate(mData);
        }
    }

    /**
     * Specialised implementation for {@link StorageType#SERVER}
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
        public A map(Graphic gl) {
            return map(gl, 0, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access) {
            return map(gl, access, 0, mData.capacity());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access, int offset, int length) {
            if (mData == null) {
                mData = create(length, Storage.this.mFormat);
            }

            if ((access & ACCESS_INVALIDATE_ALL) != 0) {
                //!
                //! Emulate ACCESS_INVALIDATE_ALL
                //!
                mData.clear();
            }
            mData.position(offset).limit(length).offset(offset);

            return mData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Graphic gl) {
            mData.flip();

            Storage.this.setUpdate(CONCEPT_DATA_CHANGE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void dispose() {
            mData = BufferFactory.deallocate(mData);
        }
    }

    /**
     * Specialised implementation for {@link StorageType#SERVER_MAPPED}
     */
    private final class BufferServerMappedFactory implements Factory<A> {
        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl) {
            return gl.map(Storage.this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access) {
            return gl.map(Storage.this, access);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A map(Graphic gl, int access, int offset, int length) {
            return gl.map(Storage.this, access, offset, length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unmap(Graphic gl) {
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
     * <p>Create an {@link Buffer} that matches the given {@link VertexFormat}</p>
     */
    private static <T extends Buffer<?>> T create(int capacity, VertexFormat format) {
        switch (format) {
            case BYTE:
                return (T) BufferFactory.allocateInt8(capacity / format.eLength);
            case UNSIGNED_BYTE:
                return (T) BufferFactory.allocateUnsignedInt8(capacity / format.eLength);
            case HALF_FLOAT:
                return (T) BufferFactory.allocateFloat16(capacity / format.eLength);
            case SHORT:
                return (T) BufferFactory.allocateInt16(capacity / format.eLength);
            case UNSIGNED_SHORT:
                return (T) BufferFactory.allocateUnsignedInt16(capacity / format.eLength);
            case INT:
                return (T) BufferFactory.allocateInt32(capacity / format.eLength);
            case UNSIGNED_INT:
                return (T) BufferFactory.allocateUnsignedInt32(capacity / format.eLength);
            case FLOAT:
                return (T) BufferFactory.allocateFloat32(capacity / format.eLength);
        }
        throw new UnsupportedOperationException();
    }
}
