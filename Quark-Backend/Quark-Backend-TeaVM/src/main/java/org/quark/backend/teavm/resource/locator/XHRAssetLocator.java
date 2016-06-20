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
package org.quark.backend.teavm.resource.locator;

import org.quark.backend.teavm.utility.array.TeaVMArray;
import org.quark.backend.teavm.utility.array.TeaVMArrayFactory;
import org.quark.resource.AssetCallback;
import org.quark.resource.AssetLocator;
import org.quark.system.utility.array.Int8Array;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.typedarrays.ArrayBuffer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Encapsulate an {@link AssetLocator} that search asset(s) using XHR request(s).
 */
public final class XHRAssetLocator implements AssetLocator {
    private final static String BINARY_REQUEST = "arraybuffer";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSynchronousSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAsynchronousSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsynchronousInputStream locate(String filename) {
        //!
        //! Build the request.
        //!
        final XMLHttpRequest xhr = XMLHttpRequest.create();

        xhr.open("GET", filename, false);
        xhr.setResponseType(BINARY_REQUEST);
        xhr.send();

        final InputStream stream = (xhr.getStatus() == 200 || xhr.getStatus() == 0
                ? new XHRInputStream(new TeaVMArrayFactory.TeaVMInt8Array((ArrayBuffer) xhr.getResponse()))
                : null);
        return new AsynchronousInputStream(stream, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsynchronousInputStream locate(String filename, AssetCallback<AsynchronousInputStream> callback) {
        final AsynchronousInputStream input = new AsynchronousInputStream(callback);

        //!
        //! Build the request.
        //!
        final XMLHttpRequest xhr = XMLHttpRequest.create();

        xhr.open("GET", filename, true);
        xhr.setResponseType(BINARY_REQUEST);
        xhr.onComplete(() -> onAssetReadStateChange(xhr, input));
        xhr.send();

        return input;
    }

    /**
     * <p>Handle when a request changed the state</p>
     *
     * @param request the request
     * @param stream  the request's stream
     */
    private void onAssetReadStateChange(XMLHttpRequest request, AsynchronousInputStream stream) {
        if (request.getStatus() == 200 || request.getStatus() == 0) {
            //!
            //! Get the content of the request.
            //!
            final ArrayBuffer response = (ArrayBuffer) request.getResponse();

            final Thread thread = new Thread(() ->
            {
                stream.notify(
                        new XHRInputStream(
                                new TeaVMArrayFactory.TeaVMInt8Array(response)));
            });
            thread.start();
        } else {
            stream.notify(null);
        }
    }

    /**
     * <code>XHRInputStream</code> represent a specialised {@link InputStream} for binary raw data.
     */
    private final static class XHRInputStream extends InputStream {
        private final Int8Array mArray;

        /**
         * <p>Constructor</p>
         */
        public XHRInputStream(Int8Array array) {
            mArray = array;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read() throws IOException {
            if (!mArray.hasRemaining()) {
                return -1;
            }
            return mArray.read() & 0xFF;
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
            if (!mArray.hasRemaining()) {
                return -1;
            }
            return TeaVMArray.copy(mArray, bytes, offset, length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int available() throws IOException {
            return mArray.remaining();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long skip(long count) throws IOException {
            final int skip = (int) (mArray.position() + count);

            mArray.position(skip);

            return skip;
        }
    }
}
