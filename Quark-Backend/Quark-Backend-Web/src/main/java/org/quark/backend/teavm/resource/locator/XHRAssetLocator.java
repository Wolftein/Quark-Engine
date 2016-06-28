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

import org.quark.backend.teavm.utility.array.WebArrayFactory;
import org.quark.resource.AssetCallback;
import org.quark.resource.AssetLocator;
import org.quark.system.utility.array.ArrayInputStream;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.typedarrays.ArrayBuffer;

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
    public InputStream locate(String filename) {
        //!
        //! Build the request.
        //!
        final XMLHttpRequest xhr = XMLHttpRequest.create();

        xhr.open("GET", filename, false);
        xhr.setResponseType(BINARY_REQUEST);
        xhr.send();

        return (xhr.getStatus() == 200 || xhr.getStatus() == 0
                ? new ArrayInputStream(new WebArrayFactory.TeaVMInt8Array((ArrayBuffer) xhr.getResponse()))
                : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream locate(String filename, AssetCallback<InputStream> callback) {
        //!
        //! Build the request.
        //!
        final XMLHttpRequest xhr = XMLHttpRequest.create();

        xhr.open("GET", filename, true);
        xhr.setResponseType(BINARY_REQUEST);
        xhr.onComplete(() -> onAssetReadStateChange(xhr, callback));
        xhr.send();

        return null;
    }

    /**
     * <p>Handle when a request changed its state</p>
     */
    private void onAssetReadStateChange(XMLHttpRequest request, AssetCallback<InputStream> callback) {
        if (request.getStatus() == 200 || request.getStatus() == 0) {
            //!
            //! Get the content of the request.
            //!
            final ArrayBuffer response = (ArrayBuffer) request.getResponse();

            final Thread thread = new Thread(() ->
                    callback.onSuccess(
                            new ArrayInputStream(
                                    new WebArrayFactory.TeaVMInt8Array(response))));
            thread.start();
        } else {
            callback.onFail();
        }
    }
}
