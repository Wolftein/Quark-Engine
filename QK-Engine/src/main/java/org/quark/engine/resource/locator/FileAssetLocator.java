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
package org.quark.engine.resource.locator;

import org.quark.engine.resource.AssetLocator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * <code>FileAssetLocator</code> encapsulate an {@link AssetLocator} that search asset(s) using {@link FileSystem}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class FileAssetLocator implements AssetLocator {
    private final FileSystem mFilesystem;

    /**
     * <p>Constructor using default {@link FileSystem}</p>
     */
    public FileAssetLocator() {
        mFilesystem = FileSystems.getDefault();
    }

    /**
     * <p>Constructor using provided {@link FileSystem}</p>
     */
    public FileAssetLocator(FileSystem filesystem) {
        mFilesystem = filesystem;
    }

    /**
     * <p>Constructor using a {@link FileSystem} from the given path</p>
     */
    public FileAssetLocator(Path location) {
        try {
            mFilesystem = FileSystems.newFileSystem(location, null);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream find(String filename) {
        final Path child = mFilesystem.getPath(filename);

        if (Files.exists(child) && Files.isReadable(child)) {
            try {
                return Files.newInputStream(child, StandardOpenOption.READ);
            } catch (IOException ignored) {
            }
        }
        return null;
    }
}