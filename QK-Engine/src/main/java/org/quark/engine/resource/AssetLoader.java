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

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>AssetLoader</code> represent an interface for loading asset(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface AssetLoader<A, B extends AssetDescriptor> {
    /**
     * <p>Tries to load an asset from an {@link InputStream}</p>
     *
     * @param context    the asset's context
     * @param descriptor the asset's descriptor
     * @param in         the input that has the asset
     *
     * @return an <code>AssetKey</code> with the asset
     *
     * @throws IOException indicates if the asset has failed to load
     */
    AssetKey<A, B> load(AssetManager context, B descriptor, InputStream in) throws IOException;
}
