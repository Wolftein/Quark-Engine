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
 * <code>AssetCallback</code> represent a callback for requesting asset synchronously and asynchronously.
 */
public interface AssetCallback<A> {
    /**
     * <p>Handle when the requested asset error</p>
     */
    void onFail();

    /**
     * <p>Handle when the requested asset has been successful loaded</p>
     *
     * @param asset the asset reference
     */
    void onSuccess(A asset);
}
