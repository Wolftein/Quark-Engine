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

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>AssetLoader</code> encapsulate an interface for loading asset(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface AssetLoader<A, B extends AssetDescriptor> {
    /**
     * <p>Load an asset</p>
     *
     * @param manager    the <b>asset</b> manager
     * @param input      the <b>asset</b> stream
     * @param descriptor the <b>asset</b> parameter
     *
     * @return an <code>AssetKey</code> which encapsulate the asset
     *
     * @throws IOException indicates failure loading the asset
     */
    AssetKey<A, B> load(AssetManager manager, InputStream input, B descriptor) throws IOException;
}
