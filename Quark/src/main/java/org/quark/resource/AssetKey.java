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

import org.quark.system.utility.Disposable;

import java.util.Collection;
import java.util.Collections;

/**
 * <code>AssetKey</code> encapsulate a key that contain(s) all the information of an asset.
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
    private int mReferences = 1;

    /**
     * <p>Constructor</p>
     */
    public AssetKey(A asset, B assetDescriptor, Collection<String> dependencies) {
        mAsset = asset;
        mAssetDescriptor = assetDescriptor;
        mDependencies = dependencies;
    }

    /**
     * <p>Constructor</p>
     */
    public AssetKey(A asset, B assetDescriptor) {
        this(asset, assetDescriptor, Collections.emptyList());
    }

    /**
     * <p>Acquire the key</p>
     */
    public void acquire() {
        mReferences++;
    }

    /**
     * <p>Dispose the key</p>
     */
    public void dispose() {
        if (mAsset instanceof Disposable) {
            Disposable.class.cast(mAsset).dispose();
        }
    }

    /**
     * <p>Release the key and if the reference reach zero it will dispose it</p>
     *
     * @return true if the asset has no more reference, false otherwise
     */
    public boolean release() {
        final boolean isDisposed = mReferences-- <= 0;

        if (isDisposed) {
            dispose();
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
