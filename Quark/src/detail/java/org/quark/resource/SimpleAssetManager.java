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

import org.quark.system.utility.emulation.Emulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Default implementation for {@link AssetManager}.
 */
public final class SimpleAssetManager implements AssetManager {
    /**
     * Encapsulate the <code>Logger</code> for <code>AssetManager</code>
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetManager.class);

    private final Set<AssetListener> mListeners = new HashSet<>();
    private final Map<String, AssetLocator> mLocators = new HashMap<>();
    private final Map<String, AssetLoader<?, ?>> mLoaders = new HashMap<>();
    private final Map<Object, String> mCacheNames = new HashMap<>();
    private final Map<String, AssetKey> mCache = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerEventListener(AssetListener listener) {
        mListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAssetLocator(String name, AssetLocator locator) {
        mLocators.put(name, locator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAssetLoader(AssetLoader loader, String... extensions) {
        for (String extension : extensions) {
            mLoaders.put(extension.toLowerCase(), loader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEventListener(AssetListener listener) {
        if (!mListeners.remove(listener)) {
            throw new IllegalStateException("An <AssetListener> with the provided name doesn't exist.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAssetLocator(String name) {
        if (mLocators.remove(name) == null) {
            throw new IllegalStateException("An <AssetLocator> with the provided name doesn't exist.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAssetLoader(String... extensions) {
        for (final String extension : extensions) {
            if (mLoaders.remove(extension) == null) {
                throw new IllegalStateException("An <AssetLoader> with the provided extension doesn't exist.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetLocator.AsynchronousInputStream findAsset(String filename) {
        AssetLocator.AsynchronousInputStream input = null;

        for (final AssetLocator locator : mLocators.values()) {
            //!
            //! Locate the asset on those locator which support synchronous request(s).
            //!
            if (locator.isSynchronousSupported()) {
                input = locator.locate(filename);

                if (input.isLoaded()) {
                    break;
                }
            }
        }
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetLocator.AsynchronousInputStream findAsset(
            String filename, AssetCallback<AssetLocator.AsynchronousInputStream> callback) {
        //!
        //! Hold all locator(s) being used.
        //!
        final Iterator<AssetLocator> locators = mLocators.values().iterator();

        //!
        //! An internal callback to do async-re-entry execution.
        //!
        final AssetCallback entry = new AssetCallback<AssetLocator.AsynchronousInputStream>() {
            @Override
            public void onFail() {
                if (locators.hasNext()) {
                    //!
                    //! Continue with other locator(s).
                    //!
                    locators.next().locate(filename, this);
                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onSuccess(AssetLocator.AsynchronousInputStream asset) {
                callback.onSuccess(asset);
            }
        };

        if (locators.hasNext()) {
            //!
            //! Execute the first locator (if valid).
            //!
            locators.next().locate(filename, entry);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> A getAsset(String filename) {
        //!
        //! Tries to find the asset in the cache
        //!
        final AssetKey<A, ?> key = (AssetKey<A, ?>) mCache.getOrDefault(filename, null);
        return key != null ? key.getAsset() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> A loadAsset(String filename) {
        return loadAsset(filename, DEFAULT_DESCRIPTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> void loadAssetAsynchronous(String filename, AssetCallback<A> callback) {
        loadAssetAsynchronous(filename, DEFAULT_DESCRIPTOR, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <A, B extends AssetDescriptor> A loadAsset(String filename, B descriptor) {
        AssetKey<A, B> key = loadAssetFromCache(filename, descriptor);

        if (key == null) {

            //!
            //! Tries to find the asset.
            //!
            final AssetLocator.AsynchronousInputStream input = findAsset(filename);

            if (input != null) {
                //!
                //! Tries to load the asset.
                //!
                key = loadAssetFrom(filename, descriptor, input);

            } else {
                LOGGER.warn("Failed to find Asset '{}'", filename); /* WARNING */

                Emulation.forEach(
                        mListeners, (listener) -> listener.onAssetFailed(filename)); /* NOTIFY */
            }
        }
        return (key != null ? key.getAsset() : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A, B extends AssetDescriptor> A loadAssetAsynchronous(String filename, B descriptor, AssetCallback<A> callback) {
        AssetKey<A, B> key = loadAssetFromCache(filename, descriptor);

        if (key == null) {
            //!
            //! Tries to find the asset (asynchronous).
            //!
            findAsset(filename, new AssetCallback<AssetLocator.AsynchronousInputStream>() {
                @Override
                public void onFail() {
                    LOGGER.warn("Failed to find Asset '{}'", filename); /* WARNING */

                    Emulation.forEach(
                            mListeners, (listener) -> listener.onAssetFailed(filename)); /* NOTIFY */
                }

                @Override
                public void onSuccess(AssetLocator.AsynchronousInputStream asset) {
                    final AssetKey<A, B> asyncKey = loadAssetFrom(filename, descriptor, asset);

                    if (asyncKey != null) {
                        callback.onSuccess(asyncKey.getAsset());
                    }
                }
            });
        }
        return (key != null ? key.getAsset() : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> void unloadAsset(A asset) {
        unloadAsset(mCacheNames.get(asset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void unloadAsset(String filename) {
        final AssetKey<?, ?> key = mCache.get(filename);

        if (key != null && key.release()) {
            mCache.remove(filename);
            mCacheNames.remove(key.getAsset());

            //!
            //! Dispose and remove a reference to each dependency of the asset.
            //!
            key.getDependencies().forEach(this::unloadAsset);

            //!
            //! Notify the asset has been disposed.
            //!
            Emulation.forEach(mListeners, (listener) -> listener.onAssetDisposed(filename));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unloadAllAssets() {
        mCache.values().forEach(AssetKey::dispose);
        mCache.clear();
        mCacheNames.clear();
    }

    /**
     * <p>Request an asset from an {@link InputStream}</p>
     */
    private <A, B extends AssetDescriptor> AssetKey<A, B> loadAssetFrom(String filename, B descriptor, InputStream input) {
        AssetKey<A, B> key = null;

        //!
        //! Find the extension of the file efficiently.
        //!
        final String extension = filename.replaceAll("^.*\\.([^.]+)$", "$1").toLowerCase();

        //!
        //! Tries to find a suitable AssetLoader for the given extension.
        //!
        final AssetLoader<A, B> loader = (AssetLoader<A, B>) mLoaders.get(extension);

        if (loader != null) {
            try {
                key = loader.load(SimpleAssetManager.this, input, descriptor);

                //!
                //! Check if we should close the descriptor or not.
                //!
                if (descriptor.isCloseable()) {
                    try {
                        input.close();
                    } catch (IOException exception) {
                        LOGGER.warn(exception.getMessage()); /* WARNING */
                    }
                }

                //!
                //! Check if the asset can be cached.
                //!
                if (descriptor.isCacheable()) {
                    mCache.put(filename, key);
                    mCacheNames.put(key.getAsset(), filename);
                }

                Emulation.forEach(
                        mListeners, (listener) -> listener.onAssetLoaded(filename)); /* NOTIFY */
            } catch (IOException exception) {
                LOGGER.warn(exception.getMessage()); /* WARNING */

                Emulation.forEach(
                        mListeners, (listener) -> listener.onAssetFailed(filename)); /* NOTIFY */
            }
        }
        return key;
    }

    /**
     * <p>Request an asset from the cache</p>
     */
    private <A, B extends AssetDescriptor> AssetKey<A, B> loadAssetFromCache(String filename, B descriptor) {
        AssetKey<A, B> key = descriptor.isCacheable() ? (AssetKey<A, B>) mCache.get(filename) : null;

        if (key != null) {
            //!
            //! Acquire the asset once more from the cache.
            //!
            key.acquire();
        }
        return key;
    }
}
