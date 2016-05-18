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

import org.quark.engine.utility.property.PropertyTree;
import org.quark.engine.utility.property.PropertyTreeComponent;

import java.util.*;
import java.util.function.Consumer;

/**
 * <a href="http://en.wikipedia.org/wiki/YAML">YAML</a> implementation for {@link PropertyTreeComponent}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
class YAMLPropertyTreeComponent implements PropertyTreeComponent {
    protected final String mName;
    protected final String mPath;
    protected final YAMLPropertyTreeComponent mParent;
    protected final Map<String, Object> mCollection = new HashMap<>();

    /**
     * <p>Empty constructor</p>
     */
    protected YAMLPropertyTreeComponent() {
        this("<Base>", "", null);
    }

    /**
     * <p>Filled constructor</p>
     */
    protected YAMLPropertyTreeComponent(String name, String path, YAMLPropertyTreeComponent parent) {
        mName = name;
        mPath = path;
        mParent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return mPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getKeys() {
        return mCollection.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String name, Object value) {
        final int delimiter = name.indexOf(PropertyTree.SEPARATOR);

        if (delimiter != -1) {
            PropertyTreeComponent child = (PropertyTreeComponent) get(name.substring(0, delimiter));

            if (child == null) {
                child = new YAMLPropertyTreeComponent(
                        name,
                        mPath + PropertyTree.SEPARATOR + name, this);
                mCollection.put(name, child);
            }
            child.set(name.substring(delimiter + PropertyTree.SEPARATOR.length()), value);
        } else if (value != null) {
            mCollection.put(name, value);
        } else {
            mCollection.remove(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyTreeComponent setComponent(String name) {
        PropertyTreeComponent child;

        final int delimiter = name.indexOf(PropertyTree.SEPARATOR);

        if (delimiter != -1) {
            child = (PropertyTreeComponent) get(name.substring(0, delimiter));

            if (child == null) {
                child = new YAMLPropertyTreeComponent(
                        name,
                        mPath + PropertyTree.SEPARATOR + name, this);
                mCollection.put(name, child);
            }
            child = child.setComponent(name.substring(delimiter + PropertyTree.SEPARATOR.length()));
        } else {
            child = new YAMLPropertyTreeComponent(
                    name,
                    mPath + PropertyTree.SEPARATOR + name, this);
            mCollection.put(name, child);
        }
        return child;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyTreeComponent setComponent(String name, Map<?, ?> values) {
        final PropertyTreeComponent component = setComponent(name);

        values.entrySet().forEach((Consumer<Map.Entry<?, ?>>) entry -> {
            if (entry.getValue() instanceof Map) {
                component.setComponent(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
            } else {
                component.set(entry.getKey().toString(), entry.getValue());
            }
        });
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyTreeComponent getComponent(String name) {
        return (PropertyTreeComponent) get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, Object asDefault) {
        final int delimiter = name.indexOf(PropertyTree.SEPARATOR);

        if (delimiter != -1) {
            final PropertyTreeComponent child = (PropertyTreeComponent) get(name.substring(0, delimiter), null);
            return (child == null
                    ? null
                    : child.get(name.substring(delimiter + PropertyTree.SEPARATOR.length()), asDefault));
        }
        return mCollection.getOrDefault(name, asDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name) {
        return get(name, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String name, String asDefault) {
        return (String) get(name, asDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String name, int asDefault) {
        return ((Number) get(name, asDefault)).byteValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String name, int asDefault) {
        return ((Number) get(name, asDefault)).shortValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String name, int asDefault) {
        return ((Number) get(name, asDefault)).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String name, long asDefault) {
        return ((Number) get(name, asDefault)).longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String name, float asDefault) {
        return ((Number) get(name, asDefault)).floatValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String name, double asDefault) {
        return ((Number) get(name, asDefault)).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(String name, boolean asDefault) {
        return (Boolean) get(name, asDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getStringList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<String> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().map(Object::toString).forEach(result::add);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Byte> getByteList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Byte> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.byteValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Short> getShortList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Short> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.shortValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIntList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Integer> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.intValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> getLongList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Long> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.longValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Float> getFloatList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Float> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.floatValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Double> getDoubleList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Double> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Number).map(Number.class::cast)
                    .forEach(T -> result.add(T.doubleValue()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Boolean> getBoolList(String name) {
        final List<?> values = (List<?>) get(name, null);

        final List<Boolean> result = new ArrayList<>(values != null ? values.size() : 0);
        if (values != null) {
            values.stream().filter(T -> T instanceof Boolean).map(Boolean.class::cast)
                    .forEach(result::add);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, ?>> getMapList(String name) {
        final List<?> values = (List<?>) get(name, null);
        final List<Map<String, ?>> result = values != null ? new ArrayList<>(values.size()) : null;

        if (values != null) {
            values.stream().filter(e -> e instanceof Map).forEach(e -> result.add((Map<String, ?>) e));
        }
        return result;
    }

    @Override
    public List<Map<String, ?>> getMapList(String name, List<Map<String, ?>> asDefault) {
        final List<?> values = (List<?>) get(name, null);
        final List<Map<String, ?>> result = values != null ? new ArrayList<>(values.size()) : asDefault;

        if (values != null) {
            values.stream().filter(e -> e instanceof Map).forEach(e -> result.add((Map<String, ?>) e));
        }
        return result;
    }
}
