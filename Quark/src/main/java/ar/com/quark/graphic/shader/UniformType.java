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
package ar.com.quark.graphic.shader;

/**
 * <code>UniformType</code> enumerate {@link Uniform} type(s).
 */
public enum UniformType {
    /**
     * Represent an IEEE-754 single-precision floating point number.
     */
    Float("float"),

    /**
     * Represent two IEEE-754 single-precision floating point number.
     */
    Float2("vec2"),

    /**
     * Represent three IEEE-754 single-precision floating point number.
     */
    Float3("vec3"),

    /**
     * Represent four IEEE-754 single-precision floating point number.
     */
    Float4("vec4"),

    /**
     * Represent an buffer of {@linkplain #Float}
     */
    FloatArray("float[]"),

    /**
     * Represent a signed, two's complement, 32-bit integer.
     */
    Int("int"),

    /**
     * Represent two signed, two's complement, 32-bit integer.
     */
    Int2("ivec2"),

    /**
     * Represent three signed, two's complement, 32-bit integer.
     */
    Int3("ivec3"),

    /**
     * Represent four signed, two's complement, 32-bit integer.
     */
    Int4("ivec4"),

    /**
     * Represent an buffer of {@linkplain #Int}
     */
    IntArray("int[]"),

    /**
     * Represent a signed, two's complement, 32-bit unsigned integer.
     */
    UInt("uint"),

    /**
     * Represent two signed, two's complement, 32-bit unsigned integer.
     */
    UInt2("uvec2"),

    /**
     * Represent three signed, two's complement, 32-bit unsigned integer.
     */
    UInt3("uvec3"),

    /**
     * Represent four signed, two's complement, 32-bit unsigned integer.
     */
    UInt4("uvec4"),

    /**
     * Represent an buffer of {@linkplain #UInt}
     */
    UIntArray("uint[]"),

    /**
     * Represent a matrix of 3x3 component(s).
     */
    Matrix3x3("mat3"),

    /**
     * Represent a matrix of 4x4 component(s).
     */
    Matrix4x4("mat4"),

    /**
     * Represent a 1D <code>Texture</code>.
     */
    Sampler1D("sampler1D"),

    /**
     * Represent an buffer of 1D <code>Texture</code>.
     */
    Sampler1DArray("sampler1DArray"),

    /**
     * Represent a 1D shadow <code>Texture</code>.
     */
    Sampler1DShadow("sampler1DShadow"),

    /**
     * Represent an buffer of 1D shadow <code>Texture</code>.
     */
    Sampler1DShadowArray("sampler1DArrayShadow"),

    /**
     * Represent a <code>Texture2D</code>.
     */
    Sampler2D("sampler2D"),

    /**
     * Represent an buffer of <code>Texture2D</code>.
     */
    Sampler2DArray("sampler2DArray"),

    /**
     * Represent a shadow <code>Texture2D</code>.
     */
    Sampler2DShadow("sampler2DShadow"),

    /**
     * Represent an buffer of shadow <code>Texture2D</code>.
     */
    Sampler2DShadowArray("sampler2DArrayShadow"),

    /**
     * Represent a multi-sampled <code>Texture2D</code>.
     */
    Sampler2DMultisample("sampler2DMS"),

    /**
     * Represent an buffer of multi-sampled <code>Texture2D</code>.
     */
    Sampler2DMultisampleArray("sampler2DMSArray"),

    /**
     * Represent a <code>Texture3D</code>.
     */
    Sampler3D("sampler3D"),

    /**
     * Represent a <code>Texture2DCube</code>.
     */
    SamplerCube("samplerCube"),

    /**
     * Represent a shadow <code>Texture2DCube</code>.
     */
    SamplerCubeShadow("samplerCubeShadow"),

    /**
     * Represent a 1D integer <code>Texture</code>.
     */
    SamplerInt1D("isampler1D"),

    /**
     * Represent an buffer of 1D integer <code>Texture</code>.
     */
    SamplerInt1DArray("isampler1DArray"),

    /**
     * Represent an integer <code>Texture2D</code>.
     */
    SamplerInt2D("isampler2D"),

    /**
     * Represent an buffer of integer <code>Texture2D</code>.
     */
    SamplerInt2DArray("isampler2DArray"),

    /**
     * Represent an integer multi-sampled <code>Texture2D</code>.
     */
    SamplerInt2DMultisample("isampler2DMS"),

    /**
     * Represent an buffer of integer multi-sampled <code>Texture2D</code>.
     */
    SamplerInt2DMultisampleArray("isampler2DMSArray"),

    /**
     * Represent an integer <code>Texture3D</code>.
     */
    SamplerInt3D("isampler3D"),

    /**
     * Represent an integer <code>Texture2DCube</code>.
     */
    SamplerIntCube("isamplerCube"),

    /**
     * Represent a 1D unsigned integer <code>Texture</code>.
     */
    SamplerUInt1D("usampler1D"),

    /**
     * Represent an buffer of 1D unsigned integer <code>Texture</code>.
     */
    SamplerUInt1DArray("usampler1DArray"),

    /**
     * Represent an unsigned integer <code>Texture2D</code>.
     */
    SamplerUInt2D("usampler2D"),

    /**
     * Represent an buffer of unsigned integer <code>Texture2D</code>.
     */
    SamplerUInt2DArray("usampler2DArray"),

    /**
     * Represent an unsigned integer multi-sampled <code>Texture2D</code>.
     */
    SamplerUInt2DMultisample("usampler2DMS"),

    /**
     * Represent an buffer of unsigned integer multi-sampled <code>Texture2D</code>.
     */
    SamplerUInt2DMultisampleArray("usampler2DMSArray"),

    /**
     * Represent an unsigned integer <code>Texture3D</code>.
     */
    SamplerUInt3D("usampler3D"),

    /**
     * Represent an unsigned integer <code>Texture2DCube</code>.
     */
    SamplerUIntCube("usamplerCube"),

    /**
     * Represent a <code>Texture</code> buffer.
     */
    SamplerBuffer("samplerBuffer"),

    /**
     * Represent an integer <code>Texture</code> buffer.
     */
    SamplerIntBuffer("isamplerBuffer"),

    /**
     * Represent an unsigned integer <code>Texture</code> buffer.
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
