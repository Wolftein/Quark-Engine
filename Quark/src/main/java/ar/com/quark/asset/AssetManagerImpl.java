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

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Implementation for {@link AssetManager}.
 */
public final class AssetManagerImpl implements AssetManager {
    /**
     * Encapsulate the <code>Logger</code> for <code>AssetManager</code>
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetManager.class);

    private final Executor mExecutor;

    private final MutableSet<AssetListener> mListeners = Sets.mutable.empty();
    private final MutableIntObjectMap<AssetLocator> mLocators = IntObjectMaps.mutable.empty();
    private final MutableIntObjectMap<AssetLoader<?, ?>> mLoaders = IntObjectMaps.mutable.empty();
    private final MutableMap<String, AssetKey> mCache = Maps.mutable.empty();
    private final MutableMap<Object, String> mCacheReferences = Maps.mutable.empty();

    /**
     * <p>Constructor</p>
     */
    public AssetManagerImpl(Executor executor) {
        mExecutor = executor;
    }

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
        mLocators.put(name.hashCode(), locator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAssetLoader(AssetLoader loader, String... extensions) {
        for (String extension : extensions) {
            mLoaders.put(extension.toLowerCase().hashCode(), loader);
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
        if (mLocators.remove(name.hashCode()) == null) {
            throw new IllegalStateException("An <AssetLocator> with the provided name doesn't exist.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAssetLoader(String... extensions) {
        for (final String extension : extensions) {
            if (mLoaders.remove(extension.hashCode()) == null) {
                throw new IllegalStateException("An <AssetLoader> with the provided extension doesn't exist.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream find(String filename) {
        InputStream input = null;

        for (final AssetLocator locator : mLocators.values()) {
            //!
            //! Locate the asset on those locator which support synchronous request(s).
            //!
            if (locator.isSynchronous()) {
                input = locator.locate(filename);

                if (input != null) {
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
    public InputStream find(String filename, Consumer<InputStream> callback) {
        //!
        //! Hold all locator(s) being used.
        //!
        final Iterator<AssetLocator> locators = mLocators.values().iterator();

        //!
        //! An internal callback to do async-re-entry execution.
        //!
        final Consumer<InputStream> consumer = new Consumer<InputStream>() {
            @Override
            public void accept(InputStream input) {
                if (input == null && locators.hasNext()) {
                    //!
                    //! Execute the first locator (if valid).
                    //!
                    locators.next().locate(filename, this);
                } else {
                    callback.accept(input);
                }
            }
        };

        if (locators.hasNext()) {
            //!
            //! Execute the first locator (if valid).
            //!
            locators.next().locate(filename, consumer);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> A get(String filename) {
        final AssetKey<A, ?> key = (AssetKey<A, ?>) mCache.get(filename);
        return key != null ? key.getAsset() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A, B extends AssetDescriptor> A load(B descriptor) {
        AssetKey<A, B> key = fromCache(descriptor);

        if (key == null) {
            final InputStream input = find(descriptor.getName());

            if (input != null) {

                key = fromElse(descriptor, input);

            } else {
                LOGGER.warn("Failed to find Asset '{}'", descriptor.getName()); /* WARNING */

                mListeners.forEach(T -> T.onAssetFailed(descriptor.getName())); /* NOTIFY */
            }
        }
        return (key != null ? key.getAsset() : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A, B extends AssetDescriptor> A loadAsynchronous(B descriptor, Consumer<A> callback) {
        final AssetKey<A, B> key = fromCache(descriptor);

        if (key == null) {
            mExecutor.execute(() -> find(descriptor.getName(), (input) ->
            {
                if (input != null) {

                    final AssetKey<A, B> ofKey = fromElse(descriptor, input);

                    callback.accept(ofKey.getAsset());

                } else {
                    LOGGER.warn("Failed to find Asset '{}'", descriptor.getName()); /* WARNING */

                    mListeners.forEach(T -> T.onAssetFailed(descriptor.getName())); /* NOTIFY */
                }
            }));
        } else {
            //!
            //! Notify about the asset immediately.
            //!
            callback.accept(key.getAsset());
        }
        return (key != null ? key.getAsset() : null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> void unload(A asset) {
        unload(mCacheReferences.get(asset));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unload(String filename) {
        final AssetKey<?, ?> key = mCache.get(filename);

        if (key != null && key.release()) {
            mCache.remove(filename);
            mCacheReferences.remove(key.getAsset());

            //!
            //! Dispose and remove a reference to each dependency of the asset.
            //!
            final ImmutableCollection<String> dependencies = key.getDependencies();

            if (dependencies != null) {
                dependencies.forEach(this::unload);
            }

            //!
            //! Notify the asset has been disposed.
            //!
            mListeners.forEach(T -> T.onAssetDisposed(filename)); /* NOTIFY */
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unloadAll() {
        mCache.forEachValue(AssetKey::dispose);
        mCache.clear();

        //!
        //! Clear all asset's references.
        //!
        mCacheReferences.clear();
    }


    /**
     * <p>Request an asset from an {@link InputStream}</p>
     */
    private <A, B extends AssetDescriptor> AssetKey<A, B> fromElse(B descriptor, InputStream input) {
        AssetKey<A, B> key = null;

        //!
        //! Find the extension of the file efficiently.
        //!
        final String name = descriptor.getName();

        final String extension = name.replaceAll("^.*\\.([^.]+)$", "$1").toLowerCase();

        //!
        //! Tries to find a suitable AssetLoader for the given extension.
        //!
        final AssetLoader<A, B> loader = (AssetLoader<A, B>) mLoaders.get(extension.hashCode());

        if (loader != null) {
            try {
                LOGGER.info("Loading asset '{}'", name); /* INFO */

                key = loader.load(this, descriptor, input);

                //!
                //! Check if we should close the descriptor or not.
                //!
                if (descriptor.isCloseable()) {
                    try {
                        input.close();
                    } catch (IOException ignored) {
                    }
                }

                //!
                //! Check if the asset can be cached.
                //!
                if (descriptor.isCacheable()) {
                    mCache.put(name, key);
                    mCacheReferences.put(key.getAsset(), name);
                }

                mListeners.forEach(T -> T.onAssetLoaded(name)); /* NOTIFY */
            } catch (IOException exception) {
                LOGGER.warn(exception.getMessage()); /* WARNING */

                mListeners.forEach(T -> T.onAssetFailed(name)); /* NOTIFY */
            }
        }
        return key;
    }

    /**
     * <p>Request an asset from the cache</p>
     */
    private <A, B extends AssetDescriptor> AssetKey<A, B> fromCache(B descriptor) {
        final AssetKey<A, B> key = descriptor.isCacheable() ? (AssetKey<A, B>) mCache.get(descriptor.getName()) : null;

        if (key != null) {
            key.acquire();
        }
        return key;
    }
}
