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

import org.quark.system.utility.Disposable;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <code>AssetKey</code> encapsulate a key that contain(s) all the information of an asset.
 */
public final class AssetKey<A, B extends AssetDescriptor> {
    private final String mFilename;
    private final String mFolder;

    /**
     * Hold the reference(s) to the asset being tracked down to perform manually de-allocation.
     */
    private int mReferences = 1;

    /**
     * Hold the reference(s) to the asset.
     */
    private A mAsset;

    /**
     * Hold the {@link AssetDescriptor} that contain(s) all parameter(s).
     */
    private final B mDescriptor;

    /**
     * Hold all dependencies of the key.
     * <p>
     * NOTE: This property is optional.
     */
    private final Collection<String> mDependencies = new LinkedList<>();

    /**
     * <p>Constructor</p>
     */
    public AssetKey(String filename, B descriptor) {
        mFilename = getAbsoluteFilename(filename);
        mFolder = getAbsoluteFolder(filename);

        mDescriptor = descriptor;
    }

    /**
     * <p>Get the filename of the key</p>
     *
     * @return the filename of the key
     */
    public String getFilename() {
        return mFilename;
    }

    /**
     * <p>Get the folder of the key</p>
     *
     * @return the folder of the key
     */
    public String getFolder() {
        return mFolder;
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
    public Collection<String> getDependencies() {
        return mDependencies;
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
        if (mAsset instanceof Disposable) {
            ((Disposable) mAsset).dispose();
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
        final boolean isDisposed = mReferences-- <= 0;

        if (isDisposed) {
            dispose();
        }
        return isDisposed;
    }

    /**
     * <p>Set the asset reference of the key</p>
     *
     * @param asset the asset reference of the key
     */
    public void setAsset(A asset) {
        mAsset = asset;
    }

    /**
     * <p>Add a dependency into the collection</p>
     *
     * @param dependency the dependency
     */
    public void setDependency(String dependency) {
        mDependencies.add(dependency);
    }

    /**
     * <p>Helper method to obtain the filename of an absolute path</p>
     */
    private static String getAbsoluteFilename(String name) {
        final int index = name.lastIndexOf('/');

        return (index <= 0 || index == name.length() - 1 ? name : name.substring(index + 1));
    }

    /**
     * <p>Helper method to obtain the folder of an absolute path</p>
     */
    private static String getAbsoluteFolder(String name) {
        final int index = name.lastIndexOf('/');

        return (index <= 0 || index == name.length() - 1 ? "" : name.substring(0, index + 1));
    }
}
