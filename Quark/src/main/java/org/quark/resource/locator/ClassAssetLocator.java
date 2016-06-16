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
package org.quark.resource.locator;

import org.quark.resource.AssetLocator;

import java.io.InputStream;

/**
 * Encapsulate an {@link AssetLocator} that search asset(s) using {@link ClassLoader}.
 */
public final class ClassAssetLocator implements AssetLocator {
    private final ClassLoader mLoader = ClassAssetLocator.class.getClassLoader();

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream locate(String filename) {
        return mLoader.getResourceAsStream(filename);
    }
}
