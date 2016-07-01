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
package com.github.wolftein.quark.resource;

import java.io.InputStream;

/**
 * <code>AssetLocator</code> encapsulate an interface for finding asset(s).
 */
public interface AssetLocator {
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
     * @return a {@link InputStream} to handle both synchronous and asynchronous requests.
     */
    InputStream locate(String filename);

    /**
     * <p>Locate an asset (asynchronously)</p>
     *
     * @param filename the filename of the asset
     * @param callback the callback
     *
     * @return an {@link InputStream} to handle both synchronous and asynchronous requests.
     */
    InputStream locate(String filename, AssetCallback<InputStream> callback);
}
