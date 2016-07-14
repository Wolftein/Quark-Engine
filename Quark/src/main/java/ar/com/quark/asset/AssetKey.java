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

import ar.com.quark.utility.Disposable;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

/**
 * <code>AssetKey</code> encapsulate a key that contain(s) all the information of an asset.
 */
public final class AssetKey<A, B extends AssetDescriptor> {
    /**
     * Hold the reference to the asset.
     */
    private final A mAsset;

    /**
     * Hold the reference to the descriptor.
     */
    private final B mDescriptor;

    /**
     * Hold all dependencies of the key.
     */
    private final ImmutableList<String> mDependencies;

    /**
     * Hold the reference(s) to the asset being tracked down to perform manually de-allocation.
     * <p>
     * NOTE: Mutable
     */
    private int mReferences = 1;

    /**
     * <p>Constructor</p>
     */
    public AssetKey(A asset, B descriptor, ImmutableList<String> dependencies) {
        mAsset = asset;
        mDescriptor = descriptor;
        mDependencies = dependencies;
    }

    /**
     * <p>Constructor</p>
     */
    public AssetKey(A asset, B descriptor) {
        this(asset, descriptor, Lists.immutable.empty());
    }

    /**
     * <p>Acquire the key</p>
     * <p>
     * NOTE: This method is for key management.
     */
    public void acquire() {
        mReferences++;
    }

    /**
     * <p>Dispose the key</p>
     * <p>
     * NOTE: This method is for key management.
     */
    public void dispose() {
        if (mDescriptor.isDisposable()) {
            Disposable.class.cast(mAsset).dispose();
        }
    }

    /**
     * <p>Release the key and if the reference reach zero it will dispose it</p>
     * <p>
     * NOTE: This method is for key management.
     *
     * @return true if the asset has no more reference, false otherwise
     */
    public boolean release() {
        final boolean isDisposed = --mReferences <= 0;

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
        return mDescriptor;
    }

    /**
     * <p>Get the dependencies of the key</p>
     *
     * @return a collection that contain(s) all dependencies of the key
     */
    public ImmutableList<String> getDependencies() {
        return mDependencies;
    }
}
