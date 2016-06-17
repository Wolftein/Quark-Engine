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
package org.quark.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>AssetLocator</code> encapsulate an interface for finding asset(s).
 */
public interface AssetLocator {
    /**
     * <code>AsynchronousInputStream</code> represent a future implementation for {@link InputStream}.
     */
    final class AsynchronousInputStream extends InputStream {
        private InputStream mBackend = null;

        /**
         * Hold a callback for when the input-stream is being notified.
         */
        private final AssetCallback<AsynchronousInputStream> mCallback;

        /**
         * Hold a flag to indicate when the stream has finished.
         */
        private boolean mFinished = false;

        /**
         * <p>Constructor</p>
         */
        public AsynchronousInputStream(AssetCallback<AsynchronousInputStream> callback) {
            mCallback = callback;
        }

        /**
         * <p>Constructor</p>
         */
        public AsynchronousInputStream(InputStream stream, AssetCallback<AsynchronousInputStream> callback) {
            notify(stream);

            mCallback = callback;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int read() throws IOException {
            return mBackend.read();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int available() throws IOException {
            return mBackend.available();
        }

        /**
         * <p>Handle when the {@link InputStream} has finished and request notification</p>
         *
         * @param input the new input stream
         */
        public void notify(InputStream input) {
            if (mFinished) {
                throw new IllegalStateException("The stream has already being notified");
            }
            mFinished = true;

            //!
            //! NOTE: The back-end may be null, ensure validation using [isLoaded].
            //!
            mBackend = input;

            //!
            //! Notify about the stream if the request is asynchronously.
            //!
            if (isLoaded()) {
                mCallback.onSuccess(this);
            } else {
                mCallback.onFail();
            }
        }

        /**
         * <p>Check if the {@link InputStream} is valid</p>
         *
         * @return <code>true</code> if the stream is valid, <code>false</code> otherwise
         */
        public boolean isLoaded() {
            return mFinished && mBackend != null;
        }

        /**
         * <p>Check if the {@link InputStream} has finished</p>
         *
         * @return <code>true</code> if the stream has finished, <code>false</code> otherwise
         */
        public boolean isFinished() {
            return mFinished;
        }
    }

    /**
     * <p>Check if the locator support synchronous request(s)</p>
     *
     * @return <code>true</code> if the locator support synchronous request(s), <code>false</code> otherwise
     */
    boolean isSynchronousSupported();

    /**
     * <p>Check if the locator support asynchronous request(s)</p>
     *
     * @return <code>true</code> if the locator support asynchronous request(s), <code>false</code> otherwise
     */
    boolean isAsynchronousSupported();

    /**
     * <p>Locate an asset</p>
     *
     * @param filename the filename of the asset
     *
     * @return a {@link AsynchronousInputStream} to handle both synchronous and asynchronous requests.
     */
    AsynchronousInputStream locate(String filename);

    /**
     * <p>Locate an asset (asynchronously)</p>
     *
     * @param filename the filename of the asset
     * @param callback the callback
     *
     * @return a {@link AsynchronousInputStream} to handle both synchronous and asynchronous requests.
     */
    AsynchronousInputStream locate(String filename, AssetCallback<AsynchronousInputStream> callback);
}
