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

import org.quark.engine.utility.Disposable;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <code>AssetKey</code> represent a key for an asset that contain(s) {@link AssetDescriptor} and all parameter(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AssetKey<A, B extends AssetDescriptor> {
    /**
     * Encapsulate the wrapped asset expressed as <code>A</code>.
     */
    private final A mAsset;

    /**
     * Encapsulate the wrapped asset descriptor expressed as <code>B</code>.
     */
    private final B mAssetDescriptor;

    /**
     * Encapsulate all dependencies of the asset.
     */
    private final Collection<String> mDependencies;

    /**
     * Encapsulate the reference(s) to the asset being tracked down to perform manually de-allocation.
     */
    private final AtomicInteger mReferences = new AtomicInteger(1);

    /**
     * <p>Default constructor</p>
     */
    public AssetKey(A asset, B assetDescriptor, Collection<String> dependencies) {
        mAsset = asset;
        mAssetDescriptor = assetDescriptor;
        mDependencies = dependencies;
    }

    /**
     * <p>Non dependency constructor</p>
     */
    public AssetKey(A asset, B assetDescriptor) {
        this(asset, assetDescriptor, Collections.emptyList());
    }

    /**
     * <p>Dispose the key</p>
     * <p>
     * NOTE: Invoked internally.
     */
    public void onKeyDispose() {
        if (mAsset instanceof Disposable) {
            //!
            //! Dispose the asset using the Disposable interface
            //!
            Disposable.class.cast(mAsset).dispose();
        }
    }

    /**
     * <p>Acquire the key</p>
     * <p>
     * NOTE: Invoked internally.
     */
    public void onKeyAcquire() {
        mReferences.incrementAndGet();
    }

    /**
     * <p>Release the key and if the reference reach zero it will dispose it</p>
     * <p>
     * NOTE: Invoked internally.
     *
     * @return true if the asset has no more reference, false otherwise
     */
    public boolean onKeyRelease() {
        final boolean isDisposed = mReferences.decrementAndGet() <= 0;

        if (isDisposed) {
            onKeyDispose();
        }
        return isDisposed;
    }

    /**
     * <p>Get the asset of the key</p>
     *
     * @return the asset of the key
     */
    public A getAsset() {
        return mAsset;
    }

    /**
     * <p>Get the descriptor of the key</p>
     *
     * @return the descriptor of the key
     */
    public B getDescriptor() {
        return mAssetDescriptor;
    }

    /**
     * <p>Get the dependencies of the key</p>
     *
     * @return a collection that contain(s) each dependency of the asset
     */
    public Collection<String> getDependencies() {
        return mDependencies;
    }
}
