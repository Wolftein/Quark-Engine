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
package org.quark.engine.utility.property;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <code>PropertyTree</code> represent an asset which contain(s) serializable data in form of properties.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface PropertyTree extends PropertyTreeComponent {
    /**
     * Encapsulate the separator of attribute(s).
     * <p/>
     * <code>get("Section1:Section2:Attribute")</code>
     */
    String SEPARATOR = ":";

    /**
     * <p>Load the document from the given filename</p>
     *
     * @param filename the filename of the asset
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    default void load(String filename) throws InvalidPreferenceException {
        try {
            load(new FileInputStream(new File(filename)));
        } catch (IOException exception) {
            throw new InvalidPreferenceException("Failed to parse", exception);
        }
    }

    /**
     * <p>Load the document from the given filename</p>
     * <p>
     * NOTE: If the filename does not exist, provide a default input that will be copy to the destination.
     *
     * @param filename the asset's filename
     * @param in       the asset's input stream
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    default void loadIfAbsentCreate(String filename, InputStream in) throws InvalidPreferenceException {
        loadIfAbsentCreate(Paths.get(filename), in);
    }

    /**
     * <p>Load the document from the given path</p>
     * <p>
     * NOTE: If the filename does not exist, provide a default input that will be copy to the destination.
     *
     * @param path the asset's path
     * @param in   the asset's input stream
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    default void loadIfAbsentCreate(Path path, InputStream in) throws InvalidPreferenceException {
        try {
            if (Files.notExists(path)) {
                Files.copy(in, path);
            }
            load(Files.newInputStream(path));
        } catch (IOException exception) {
            throw new InvalidPreferenceException("Failed to parseIfAbsentCreate", exception);
        }
    }

    /**
     * <p>Load the document from the given {@link InputStream}</p>
     *
     * @param in the asset's input stream
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    void load(InputStream in) throws InvalidPreferenceException;

    /**
     * <p>Save the document into the given path</p>
     *
     * @param path the path to store this document
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    default void save(Path path) throws InvalidPreferenceException {
        try {
            save(Files.newOutputStream(path));
        } catch (IOException exception) {
            throw new InvalidPreferenceException("Failed to save", exception);
        }
    }

    /**
     * <p>Save the document into the given {@link OutputStream}</p>
     *
     * @param out the asset's output stream
     *
     * @throws InvalidPreferenceException indicates a failure in the document
     */
    void save(OutputStream out) throws InvalidPreferenceException;
}
