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
 * <code>UniformType</code> enumerate {@link Uniform} type(s).
 */
public enum UniformType {
    /**
     * Represent an IEEE-754 single-precision floating point number.
     *
     * @since GLSL 1.20
     */
    Float("float"),

    /**
     * Represent two IEEE-754 single-precision floating point number.
     *
     * @since GLSL 1.20
     */
    Float2("vec2"),

    /**
     * Represent three IEEE-754 single-precision floating point number.
     *
     * @since GLSL 1.20
     */
    Float3("vec3"),

    /**
     * Represent four IEEE-754 single-precision floating point number.
     *
     * @since GLSL 1.20
     */
    Float4("vec4"),

    /**
     * Represent an array of {@linkplain #Float}
     *
     * @since GLSL 1.20
     */
    FloatArray("float[]"),

    /**
     * Represent a signed, two's complement, 32-bit integer.
     *
     * @since GLSL 1.20
     */
    Int("int"),

    /**
     * Represent two signed, two's complement, 32-bit integer.
     *
     * @since GLSL 1.20
     */
    Int2("ivec2"),

    /**
     * Represent three signed, two's complement, 32-bit integer.
     *
     * @since GLSL 1.20
     */
    Int3("ivec3"),

    /**
     * Represent four signed, two's complement, 32-bit integer.
     *
     * @since GLSL 1.20
     */
    Int4("ivec4"),

    /**
     * Represent an array of {@linkplain #Int}
     *
     * @since GLSL 1.20
     */
    IntArray("int[]"),

    /**
     * Represent a signed, two's complement, 32-bit unsigned integer.
     *
     * @since GLSL 1.30
     */
    UInt("uint"),

    /**
     * Represent two signed, two's complement, 32-bit unsigned integer.
     *
     * @since GLSL 1.30
     */
    UInt2("uvec2"),

    /**
     * Represent three signed, two's complement, 32-bit unsigned integer.
     *
     * @since GLSL 1.30
     */
    UInt3("uvec3"),

    /**
     * Represent four signed, two's complement, 32-bit unsigned integer.
     *
     * @since GLSL 1.30
     */
    UInt4("uvec4"),

    /**
     * Represent an array of {@linkplain #UInt}
     *
     * @since GLSL 1.30
     */
    UIntArray("uint[]"),

    /**
     * Represent a matrix of 3x3 component(s).
     *
     * @since GLSL 1.20
     */
    Matrix3x3("mat3"),

    /**
     * Represent a matrix of 4x4 component(s).
     *
     * @since GLSL 1.20
     */
    Matrix4x4("mat4");

    public final String eName;

    /**
     * <p>Constructor</p>
     */
    UniformType(String name) {
        eName = name;
    }
}
