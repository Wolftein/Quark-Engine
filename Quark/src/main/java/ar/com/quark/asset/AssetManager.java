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
package ar.com.quark.asset;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * <code>AssetManager</code> encapsulate an interface that holds every resource.
 * <p>
 * Provides a means to register {@link AssetLocator}(s) (which are used to find resources),
 * then {@link AssetLoader}(s) are invoked to convert the data into an usable object.
 */
public interface AssetManager {
    /**
     * <p>Register an {@link AssetListener}</p>
     *
     * @param listener the unique instance of the listener
     */
    void registerEventListener(AssetListener listener);

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
     * <p>Remove an {@link AssetListener}</p>
     *
     * @param listener the unique instance of the listener
     */
    void removeEventListener(AssetListener listener);

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
     */
    InputStream find(String filename);

    /**
     * <p>Tries to find an asset from any of the {@link AssetLocator} registered</p>
     *
     * @param filename the filename (as unique identifier) of the asset
     * @param callback the callback
     *
     * @return <code>null</code>
     */
    InputStream find(String filename, Consumer<InputStream> callback);

    /**
     * <p>Request an asset that has been loaded</p>
     *
     * @param filename the name (as unique identifier) of the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    <A> A get(String filename);

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param filename the name (as unique identifier) of the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    default <A> A load(String filename) {
        return load(new AssetDescriptor(filename, false, true, false));
    }

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     * <p>
     * NOTE: This method will execute asynchronous and not return
     *
     * @param filename the name (as unique identifier) of the asset
     * @param callback the callback
     *
     * @return a reference to the asset requested (if was in cache) or <code>null</code> if not
     */
    default <A> A loadAsynchronous(String filename, Consumer<A> callback) {
        return loadAsynchronous(new AssetDescriptor(filename, false, true, false), callback);
    }

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     *
     * @param descriptor the descriptor that contains all parameters and information about the asset
     *
     * @return a reference to the asset requested or <code>null</code> if doesn't exist
     */
    <A, B extends AssetDescriptor> A load(B descriptor);

    /**
     * <p>Tries to load an asset from any of the {@link AssetLoader} registered using any of the
     * {@link AssetLocator} registered to find it</p>
     * <p>
     * NOTE: This method will execute asynchronous and not return
     *
     * @param descriptor the descriptor that contains all parameters and information about the asset
     * @param callback   the callback
     *
     * @return a reference to the asset requested (if was in cache) or <code>null</code> if not
     */
    <A, B extends AssetDescriptor> A loadAsynchronous(B descriptor, Consumer<A> callback);

    /**
     * <p>Unload an asset</p>
     *
     * @param asset the asset to dispose.
     */
    <A> void unload(A asset);

    /**
     * <p>Unload an asset</p>
     *
     * @param filename the name (as unique identifier) of the asset
     */
    void unload(String filename);

    /**
     * <p>Unload all asset(s)</p>
     */
    void unloadAll();
}
