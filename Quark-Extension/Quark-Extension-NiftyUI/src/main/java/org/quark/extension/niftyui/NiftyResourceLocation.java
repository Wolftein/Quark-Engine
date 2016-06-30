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
package org.quark.extension.niftyui;

import de.lessvoid.nifty.tools.resourceloader.ResourceLocation;

import java.io.InputStream;
import java.net.URL;

import static org.quark.Quark.QKResources;

/**
 * <code>NiftyResourceLocation</code> represent implementation of {@link ResourceLocation}.
 */
public final class NiftyResourceLocation implements ResourceLocation {
    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResourceAsStream(String ref) {
        return QKResources.find(ref);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getResource(String ref) {
        //!
        //! NOTE: No need to implement this method.
        //!
        return null;
    }
}
