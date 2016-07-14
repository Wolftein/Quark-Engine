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

/**
 * <code>AssetDescriptor</code> encapsulate all parameter(s) of an asset.
 */
public class AssetDescriptor {
    private final String mName;
    private final String mFilename;
    private final String mFolder;
    private final boolean mCacheable;
    private final boolean mCloseable;
    private final boolean mDisposable;

    /**
     * <p>Constructor</p>
     */
    public AssetDescriptor(String filename, boolean cacheable, boolean closeable, boolean disposable) {
        mName = filename;
        mFilename = getAbsoluteFilename(filename);
        mFolder = getAbsoluteFolder(filename);

        mCacheable = cacheable;
        mCloseable = closeable;
        mDisposable = disposable;
    }

    /**
     * <p>Get the name of the descriptor</p>
     *
     * @return the name of the descriptor
     */
    public final String getName() {
        return mName;
    }

    /**
     * <p>Get the filename of the descriptor</p>
     *
     * @return the filename of the descriptor
     */
    public final String getFilename() {
        return mFilename;
    }

    /**
     * <p>Get the folder of the descriptor</p>
     *
     * @return the folder of the descriptor
     */
    public final String getFolder() {
        return mFolder;
    }

    /**
     * <p>Check if the asset can be cacheable in memory</p>
     *
     * @return <code>true</code> if the asset can be cacheable in memory, <code>false</code> otherwise
     */
    public final boolean isCacheable() {
        return mCacheable;
    }

    /**
     * <p>Check if the asset should be closed automatically</p>
     *
     * @return <code>true</code> if the asset should be closed automatically, <code>false</code> otherwise
     */
    public final boolean isCloseable() {
        return mCloseable;
    }

    /**
     * <p>Check if the asset should be disposed</p>
     *
     * @return <code>true</code> if the asset should be disposed, <code>false</code> otherwise
     */
    public final boolean isDisposable() {
        return mDisposable;
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
