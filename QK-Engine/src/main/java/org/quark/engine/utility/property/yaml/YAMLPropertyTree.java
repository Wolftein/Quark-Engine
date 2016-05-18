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
package org.quark.engine.utility.property.yaml;

import org.quark.engine.utility.property.InvalidPreferenceException;
import org.quark.engine.utility.property.PropertyTree;
import org.quark.engine.utility.property.PropertyTreeComponent;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * <a href="http://en.wikipedia.org/wiki/YAML">YAML</a> implementation for {@link PropertyTree}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class YAMLPropertyTree extends YAMLPropertyTreeComponent implements PropertyTree {
    private final static Yaml sPARSER = new Yaml();

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(InputStream in) throws InvalidPreferenceException {
        try {
            convert((Map<?, ?>) sPARSER.load(in), this);
        } catch (Exception exception) {
            throw new InvalidPreferenceException("Failed to load", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(OutputStream out) throws InvalidPreferenceException {
        try {
            out.write(sPARSER.dump(mCollection).getBytes());
        } catch (IOException exception) {
            throw new InvalidPreferenceException("Failed to save", exception);
        }
    }

    /**
     * <p>Convert a {@linkplain Map} into a {@linkplain PropertyTreeComponent}</p>
     */
    private void convert(Map<?, ?> root, PropertyTreeComponent component) {
        root.forEach((key, value) -> {
            if (value instanceof Map<?, ?>) {
                convert((Map<?, ?>) value, component.setComponent(key.toString()));
            } else {
                component.set(key.toString(), value);
            }
        });
    }
}