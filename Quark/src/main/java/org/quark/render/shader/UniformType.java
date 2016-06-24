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

import org.quark.render.texture.Texture;
import org.quark.render.texture.Texture2D;
import org.quark.render.texture.Texture2DCube;
import org.quark.render.texture.Texture3D;

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
    Matrix4x4("mat4"),

    /**
     * Represent a 1D {@link Texture}.
     *
     * @since GLSL 1.20
     */
    Sampler1D("sampler1D"),

    /**
     * Represent an array of 1D {@link Texture}.
     *
     * @since GLSL 1.30
     */
    Sampler1DArray("sampler1DArray"),

    /**
     * Represent a 1D shadow {@link Texture}.
     *
     * @since GLSL 1.20
     */
    Sampler1DShadow("sampler1DShadow"),

    /**
     * Represent an array of 1D shadow {@link Texture}.
     *
     * @since GLSL 1.30
     */
    Sampler1DShadowArray("sampler1DArrayShadow"),

    /**
     * Represent a {@link Texture2D}.
     *
     * @since GLSL 1.20
     */
    Sampler2D("sampler2D"),

    /**
     * Represent an array of {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    Sampler2DArray("sampler2DArray"),

    /**
     * Represent a shadow {@link Texture2D}.
     *
     * @since GLSL 1.20
     */
    Sampler2DShadow("sampler2DShadow"),

    /**
     * Represent an array of shadow {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    sampler2DShadowArray("sampler2DArrayShadow"),

    /**
     * Represent a multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    Sampler2DMultisample("sampler2DMS"),

    /**
     * Represent an array of multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    Sampler2DMultisampleArray("sampler2DMSArray"),

    /**
     * Represent a {@link Texture3D}.
     *
     * @since GLSL 1.20
     */
    Sampler3D("sampler3D"),

    /**
     * Represent a {@link Texture2DCube}.
     *
     * @since GLSL 1.20
     */
    SamplerCube("samplerCube"),

    /**
     * Represent a shadow {@link Texture2DCube}.
     *
     * @since GLSL 1.20
     */
    SamplerCubeShadow("samplerCubeShadow"),

    /**
     * Represent a 1D integer {@link Texture}.
     *
     * @since GLSL 1.30
     */
    SamplerInt1D("isampler1D"),

    /**
     * Represent an array of 1D integer {@link Texture}.
     *
     * @since GLSL 1.30
     */
    SamplerInt1DArray("isampler1DArray"),

    /**
     * Represent an integer {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    SamplerInt2D("isampler2D"),

    /**
     * Represent an array of integer {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    SamplerInt2DArray("isampler2DArray"),

    /**
     * Represent an integer multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    SamplerInt2DMultisample("isampler2DMS"),

    /**
     * Represent an array of integer multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    SamplerInt2DMultisampleArray("isampler2DMSArray"),

    /**
     * Represent an integer {@link Texture3D}.
     *
     * @since GLSL 1.30
     */
    SamplerInt3D("isampler3D"),

    /**
     * Represent an integer {@link Texture2DCube}.
     *
     * @since GLSL 1.30
     */
    SamplerIntCube("isamplerCube"),

    /**
     * Represent a 1D unsigned integer {@link Texture}.
     *
     * @since GLSL 1.30
     */
    SamplerUInt1D("usampler1D"),

    /**
     * Represent an array of 1D unsigned integer {@link Texture}.
     *
     * @since GLSL 1.30
     */
    SamplerUInt1DArray("usampler1DArray"),

    /**
     * Represent an unsigned integer {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    SamplerUInt2D("usampler2D"),

    /**
     * Represent an array of unsigned integer {@link Texture2D}.
     *
     * @since GLSL 1.30
     */
    SamplerUInt2DArray("usampler2DArray"),

    /**
     * Represent an unsigned integer multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    SamplerUInt2DMultisample("usampler2DMS"),

    /**
     * Represent an array of unsigned integer multi-sampled {@link Texture2D}.
     *
     * @since GLSL 1.50
     */
    SamplerUInt2DMultisampleArray("usampler2DMSArray"),

    /**
     * Represent an unsigned integer {@link Texture3D}.
     *
     * @since GLSL 1.30
     */
    SamplerUInt3D("usampler3D"),

    /**
     * Represent an unsigned integer {@link Texture2DCube}.
     *
     * @since GLSL 1.30
     */
    SamplerUIntCube("usamplerCube"),

    /**
     * Represent a {@link Texture} buffer.
     *
     * @since GLSL 1.40
     */
    SamplerBuffer("samplerBuffer"),

    /**
     * Represent an integer {@link Texture} buffer.
     *
     * @since GLSL 1.40
     */
    SamplerIntBuffer("isamplerBuffer"),

    /**
     * Represent an unsigned integer {@link Texture} buffer.
     *
     * @since GLSL 1.40
     */
    SamplerUIntBuffer("usamplerBuffer");

    public final String eName;

    /**
     * <p>Constructor</p>
     */
    UniformType(String name) {
        eName = name;
    }
}
