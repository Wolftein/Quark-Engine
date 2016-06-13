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
package org.quark.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <code>DefaultAssetManager</code> represent the default implementation for {@link AssetManager}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class DefaultAssetManager implements AssetManager {
    /**
     * Encapsulate the <code>Logger</code> for <code>AssetManager</code>
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetManager.class);

    private final Map<String, AssetLocator> mLocators = new ConcurrentHashMap<>();
    private final Map<String, AssetLoader<?, ?>> mLoaders = new ConcurrentHashMap<>();
    private final Map<Object, String> mCacheNames = new HashMap<>();
    private final Map<String, AssetKey> mCache = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAssetLocator(String name, AssetLocator locator) {
        mLocators.putIfAbsent(name, locator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAssetLoader(AssetLoader loader, String... extensions) {
        for (String extension : extensions) {
            mLoaders.putIfAbsent(extension.toLowerCase(), loader);
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
    public InputStream findAsset(String filename) throws IOException {
        InputStream input = null;

        for (final AssetLocator locator : mLocators.values()) {
            if ((input = locator.locate(filename)) != null) {
                break;
            }
        }
        return input;
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
    public <A> A loadAsset(String filename) throws IOException {
        return loadAsset(filename, DEFAULT_DESCRIPTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> A loadAssetOrNull(String filename) {
        try {
            return loadAsset(filename, DEFAULT_DESCRIPTOR);
        } catch (IOException exception) {
            LOGGER.warn("Trying to load an asset", exception);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized <A, B extends AssetDescriptor> A loadAsset(String filename, B pDescriptor) throws IOException {
        //!
        //! Tries to find the Asset in the cache.
        //!
        AssetKey<A, B> key = pDescriptor.isCacheable() ? (AssetKey<A, B>) mCache.getOrDefault(filename, null) : null;

        if (key == null) {
            final String extension = filename.replaceAll("^.*\\.([^.]+)$", "$1").toLowerCase();

            //!
            //! Tries to find a suitable AssetLoader for the given extension.
            //!
            final AssetLoader<A, B> loader = (AssetLoader<A, B>) mLoaders.getOrDefault(extension, null);

            if (loader != null) {
                LOGGER.info("Loading Asset '{}'", filename); /* DEBUG */

                //!
                //! Tries to find the asset.
                //!
                final InputStream input = findAsset(filename);

                if (input != null) {
                    try {
                        key = loader.load(this, input, pDescriptor);

                        //!
                        //! Check if we should close the descriptor or not.
                        //!
                        if (pDescriptor.isCloseable()) {
                            try {
                                input.close();
                            } catch (IOException exception) {
                                LOGGER.warn(exception.getMessage()); /* WARNING */
                            }
                        }

                        //!
                        //! Check if the asset can be cached.
                        //!
                        if (pDescriptor.isCacheable()) {
                            mCache.put(filename, key);
                            mCacheNames.put(key.getAsset(), filename);
                        }
                    } catch (IOException exception) {
                        LOGGER.warn(exception.getMessage()); /* WARNING */
                    }
                } else {
                    LOGGER.warn("Failed to find Asset '{}'", filename); /* WARNING */
                }
            } else {
                LOGGER.warn("Failed to find AssetLoader for '{}'", extension); /* WARNING */
            }
        } else {
            //!
            //! Acquire the asset once more from the cache.
            //!
            key.acquire();
        }
        return key != null ? key.getAsset() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A, B extends AssetDescriptor> A loadAssetOrNull(String filename, B descriptor) {
        try {
            return loadAsset(filename, descriptor);
        } catch (IOException exception) {
            LOGGER.warn("Trying to load an asset", exception);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> void unloadAsset(A asset) {
        unloadAsset(mCacheNames.getOrDefault(asset, "\0"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void unloadAsset(String filename) {
        final AssetKey<?, ?> key = mCache.getOrDefault(filename, null);

        if (key != null && key.release()) {
            mCache.remove(filename);
            mCacheNames.remove(key.getAsset());

            //!
            //! Dispose and remove a reference to each dependency of the asset.
            //!
            key.getDependencies().forEach(this::unloadAsset);
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
}
