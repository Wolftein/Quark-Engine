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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>PropertyTreeComponent</code> represent a view of a {@link PropertyTree}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface PropertyTreeComponent {
    /**
     * <p>Get the name of the component</p>
     *
     * @return the name of the component
     */
    String getName();

    /**
     * <p>Get the path of the component</p>
     *
     * @return the path of the component
     */
    String getPath();

    /**
     * <p>Get all key(s) of the component</p>
     *
     * @return a collection that contain(s) all key(s) of the component
     */
    Set<String> getKeys();

    /**
     * <p>Sets the specified attribute to the given value</p>
     * <p>
     * NOTE: If value is null, the entry will be removed. any existing entry will be replaced, regardless of the value.
     *
     * @param name  the attribute's name
     * @param value the attribute's new value
     */
    void set(String name, Object value);

    /**
     * <p>Creates an empty {@link PropertyTreeComponent}</p>
     * <p>
     * any value that was previously set at this path will be overwritten. If the previous value was itself a
     * {@link PropertyTreeComponent}, it will be orphaned.
     *
     * @param name the attribute's name
     *
     * @return the newly component created
     */
    PropertyTreeComponent setComponent(String name);

    /**
     * <p>Creates an non empty {@link PropertyTreeComponent}</p>
     * <p>
     * any value that was previously set at this path will be overwritten. If the previous value was itself a
     * {@link PropertyTreeComponent}, it will be orphaned.
     *
     * @param name   the attribute's name
     * @param values the attribute's new value
     *
     * @return the newly component created
     */
    PropertyTreeComponent setComponent(String name, Map<?, ?> values);

    /**
     * <p>Get the value of the given attribute</p>
     *
     * @param name the attribute's name
     *
     * @return the component with the given name, or <code>null</code> if not present
     */
    PropertyTreeComponent getComponent(String name);

    /**
     * <p>Get the value of the given attribute</p>
     *
     * @param name      the attribute's name
     * @param asDefault the attribute's default value
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     */
    Object get(String name, Object asDefault);

    /**
     * <p>Get the value of the given attribute</p>
     *
     * @param name the attribute's name
     *
     * @return the value of the given attribute or <code>null</code> if not present
     */
    Object get(String name);

    /**
     * <p>Get the string value of the given attribute</p>
     *
     * @see #getString(String, String)
     */
    default String getString(String name) {
        return getString(name, "");
    }

    /**
     * <p>Get the string value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    String getString(String name, String asDefault);

    /**
     * <p>Get the byte value of the given attribute</p>
     *
     * @see #getByte(String, int)
     */
    default byte getByte(String name) {
        return getByte(name, 0);
    }

    /**
     * <p>Get the byte value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    byte getByte(String name, int asDefault);

    /**
     * <p>Get the short value of the given attribute</p>
     *
     * @see #getShort(String, int)
     */
    default short getShort(String name) {
        return getShort(name, 0);
    }

    /**
     * <p>Get the short value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    short getShort(String name, int asDefault);

    /**
     * <p>Get the integer value of the given attribute</p>
     *
     * @see #getInt(String, int)
     */
    default int getInt(String name) {
        return getInt(name, 0);
    }

    /**
     * <p>Get the integer value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    int getInt(String name, int asDefault);

    /**
     * <p>Get the long value of the given attribute</p>
     *
     * @see #getLong(String, long)
     */
    default long getLong(String name) {
        return getLong(name, 0L);
    }

    /**
     * <p>Get the long value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    long getLong(String name, long asDefault);

    /**
     * <p>Get the float value of the given attribute</p>
     *
     * @see #getFloat(String, float)
     */
    default float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    /**
     * <p>Get the float value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    float getFloat(String name, float asDefault);

    /**
     * <p>Get the double value of the given attribute</p>
     *
     * @see #get(String, Object)
     */
    default double getDouble(String name) {
        return getDouble(name, 0.0D);
    }

    /**
     * <p>Get the double value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    double getDouble(String name, double asDefault);

    /**
     * <p>Get the boolean value of the given attribute</p>
     *
     * @see #getBoolean(String, boolean)
     */
    default boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * <p>Get the boolean value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    boolean getBoolean(String name, boolean asDefault);

    /**
     * <p>Get the {@literal List<String>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<String> getStringList(String name);

    /**
     * <p>Get the {@literal List<Byte>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Byte> getByteList(String name);

    /**
     * <p>Get the {@literal List<Short>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Short> getShortList(String name);

    /**
     * <p>Get the {@literal List<Integer>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Integer> getIntList(String name);

    /**
     * <p>Get the {@literal List<Long>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Long> getLongList(String name);

    /**
     * <p>Get the {@literal List<Float>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Float> getFloatList(String name);

    /**
     * <p>Get the {@literal List<Double>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Double> getDoubleList(String name);

    /**
     * <p>Get the {@literal List<Boolean>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    List<Boolean> getBoolList(String name);

    /**
     * <p>Get the {@literal Map<String, ?>} value of the given attribute</p>
     *
     * @see #get(String)
     */
    default List<Map<String, ?>> getMapList(String name) {
        return getMapList(name, Collections.emptyList());
    }

    /**
     * <p>Get the {@literal Map<String, ?>} value of the given attribute</p>
     *
     * @return the value of the given attribute or <code>asDefault</code> if not present
     *
     * @see #get(String, Object)
     */
    List<Map<String, ?>> getMapList(String name, List<Map<String, ?>> asDefault);
}
