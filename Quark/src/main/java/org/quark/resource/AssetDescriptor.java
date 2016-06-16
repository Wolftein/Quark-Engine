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

/**
 * <code>AssetDescriptor</code> encapsulate all parameter(s) of an asset.
 */
public class AssetDescriptor {
    private final boolean mCacheable;
    private final boolean mCloseable;

    /**
     * <p>Constructor</p>
     */
    public AssetDescriptor(boolean cacheable, boolean closeable) {
        mCacheable = cacheable;
        mCloseable = closeable;
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
}
