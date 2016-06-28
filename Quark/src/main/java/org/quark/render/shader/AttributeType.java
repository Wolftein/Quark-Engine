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
package org.quark.render.shader;

/**
 * <code>AttributeType</code> enumerate {@link Attribute} type(s).
 */
public enum AttributeType {
    /**
     * Represent a floating point number.
     */
    Float("float"),

    /**
     * Represent two floating point number.
     */
    Float2("vec2"),

    /**
     * Represent three floating point number.
     */
    Float3("vec3"),

    /**
     * Represent four floating point number.
     */
    Float4("vec4"),

    /**
     * Represent an integer number.
     */
    Int("int"),

    /**
     * Represent two integer number.
     */
    Int2("ivec2"),

    /**
     * Represent three integer number.
     */
    Int3("ivec3"),

    /**
     * Represent four integer number.
     */
    Int4("ivec4"),

    /**
     * Represent a matrix of 3x3 component(s).
     */
    Matrix3x3("mat3"),

    /**
     * Represent a matrix of 4x4 component(s).
     */
    Matrix4x4("mat4");

    public final String eName;

    /**
     * <p>Constructor</p>
     */
    AttributeType(String name) {
        eName = name;
    }
}
