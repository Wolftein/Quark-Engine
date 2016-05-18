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
package org.quark.engine.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>AssetManager</code> represent an interface that holds and manage every resource in the engine.
 * <p>
 * Provides a means to register {@link AssetLocator}(s) (which are used to find resources),
 * then {@link AssetLoader}(s) are invoked to convert the data into an usable object.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface AssetManager {
    /**
     * Encapsulate a default {@linkplain AssetDescriptor} for every non parameter asset.
     */
    AssetDescriptor DEFAULT_DESCRIPTOR = new AssetDescriptor(false, true);

    /**
     * Encapsulate a default {@linkplain AssetDescriptor} for every non parameter cache asset.
     */
    AssetDescriptor DEFAULT_CACHEABLE_DESCRIPTOR = new AssetDescriptor(true, true);

    /**
     * <p>Register an {@link AssetLocator}</p>
     *
     * @param name    the unique identifier (as name) of the locator
     * @param locator the unique instance of the locator
     */
    void registerAssetLocator(String name, AssetLocator locator);

    /**
     * <p>Register an {@link AssetLoader}</p>
     *
     * @param loader     the unique instance of the loader
     * @param extensions the unique extension(s) being handled by the loader
     */
    void registerAssetLoader(AssetLoader loader, String... extensions);

    /**
     * <p>Remove an {@link AssetLocator}</p>
     *
     * @param name the unique identifier (as name) of the locator
     */
    void removeAssetLocator(String name);

    /**
     * <p>Remove an {@link AssetLoader}</p>
     *
     * @param extensions the unique extension(s) being handled by the loader
     */
    void removeAssetLoader(String... extensions);

    /**
     * <p>Tries to find an asset from any of the {@link AssetLocator} registered</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     *
     * @return a reference to the <code>InputStream</code> of the asset
     *
     * @throws IOException indicates if the asset hasn't been found
     */
    InputStream findAsset(String filename) throws IOException;

    /**
     * <p>Request an asset that has been loaded</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    <A> A getAsset(String filename);

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     *
     * @return a reference to the asset requested
     *
     * @throws IOException indicates if the asset hasn't been found or failed to load
     */
    <A> A loadAsset(String filename) throws IOException;

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    <A> A loadAssetOrNull(String filename);

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param filename   the filename (as unique identifier) of the asset
     * @param descriptor the descriptor that contains all parameters and information about the asset
     *
     * @return a reference to the asset requested
     *
     * @throws IOException indicates if the asset hasn't been found or failed to load
     */
    <A, B extends AssetDescriptor> A loadAsset(String filename, B descriptor) throws IOException;

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param filename   the filename (as unique identifier) of the asset
     * @param descriptor the descriptor that contains all parameters and information about the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    <A, B extends AssetDescriptor> A loadAssetOrNull(String filename, B descriptor);

    /**
     * <p>Unload an asset</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     */
    void unloadAsset(String filename);

    /**
     * <p>Unload all asset(s)</p>
     */
    void unloadAllAssets();
}
