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
package org.quark.render;

import org.quark.render.storage.VertexDescriptor;
import org.quark.render.texture.Texture;
import org.quark.render.texture.frame.Frame;

import java.util.Map;

/**
 * <code>RenderCapabilities</code> contain(s) all capabilities supported by {@link Render}.
 */
public final class RenderCapabilities {
    /**
     * <code>Extension</code> enumerates all supported extension(s).
     */
    public enum Extension {
        /**
         * Indicates if {@link Frame} is supported.
         */
        FRAME_BUFFER,

        /**
         * Indicates if {@link Frame} support sRGB format.
         */
        FRAME_BUFFER_SRGB,

        /**
         * Indicates if {@link Frame} support multiple render target (MRT).
         */
        FRAME_BUFFER_MULTIPLE_RENDER_TARGET,

        /**
         * Indicates if {@link Frame} support multiple samples.
         */
        FRAME_BUFFER_MULTIPLE_SAMPLE,

        /**
         * Indicates if {@link VertexDescriptor} is supported.
         */
        VERTEX_ARRAY_OBJECT,

        /**
         * Indicates if {@link Texture} support S3TC compression.
         */
        TEXTURE_COMPRESSION_S3TC,

        /**
         * Indicates if {@link Texture} support anisotropic filter.
         */
        TEXTURE_FILTER_ANISOTROPIC,

        /**
         * Indicates if geometry stage is supported by GLSL.
         */
        GLSL_GEOMETRY,

        /**
         * Indicates if explicit attribute location is supported by GLSL.
         */
        GLSL_EXPLICIT_ATTRIBUTE,

        /**
         * Indicates if explicit data location is supported by GLSL.
         */
        GLSL_EXPLICIT_UNIFORM
    }

    /**
     * <code>Limit</code> enumerates all limit(s).
     */
    public enum Limit {
        /**
         * Indicates {@link Frame} maximum sample(s).
         */
        FRAME_SAMPLE,

        /**
         * Indicates {@link Frame} maximum attachment(s).
         */
        FRAME_ATTACHMENT,

        /**
         * Indicates {@link Frame} maximum multiple render attachment(s).
         */
        FRAME_MULTIPLE_RENDER_ATTACHMENT,

        /**
         * Indicates {@link Texture} maximum anisotropic filter.
         */
        TEXTURE_ANISOTROPIC,

        /**
         * Indicates {@link Texture} maximum size.
         */
        TEXTURE_SIZE,

        /**
         * Indicates {@link Texture} maximum stage(s).
         */
        TEXTURE_STAGE,
    }

    /**
     * <code>LanguageVersion</code> enumerates supported version(s)
     */
    public enum LanguageVersion {
        /**
         * Represent OpenGL ES 2.0 (March 2007).
         */
        GLES20,

        /**
         * Represent OpenGL ES 3.0 (August 2012).
         */
        GLES30,

        /**
         * Represent OpenGL ES 3.1 (March 2014).
         */
        GLES31,

        /**
         * Represent OpenGL ES 3.2 (August 2015).
         */
        GLES32,

        /**
         * Represent OpenGL 2.1 (2006).
         */
        GL21,

        /**
         * Represent OpenGL 3.0 (August 2008).
         */
        GL30,

        /**
         * Represent OpenGL 3.1 (March 2009).
         */
        GL31,

        /**
         * Represent OpenGL 3.2 (August 2009).
         */
        GL32,

        /**
         * Represent OpenGL 3.3 (February 2010).
         */
        GL33;


        /**
         * <p>Get the {@link ShaderLanguageVersion}</p>
         *
         * @return the shader language version
         */
        public ShaderLanguageVersion getShaderVersion() {
            switch (this) {
                case GLES20:
                    return ShaderLanguageVersion.GLSLES2;
                case GLES30:
                case GLES31:
                case GLES32:
                    return ShaderLanguageVersion.GLSLES3;
                case GL21:
                    return ShaderLanguageVersion.GLSL210;
                case GL30:
                    return ShaderLanguageVersion.GLSL300;
                case GL31:
                    return ShaderLanguageVersion.GLSL310;
                case GL32:
                    return ShaderLanguageVersion.GLSL320;
                case GL33:
                    return ShaderLanguageVersion.GLSL330;
            }
            throw new IllegalStateException("Missing shader version");
        }
    }

    /**
     * <code>ShaderLanguageVersion</code> enumerates supported shading language(s)
     */
    public enum ShaderLanguageVersion {
        /**
         * Represent OpenGL ES Pipeline Language 2 (March 2007).
         *
         * @since OpenGL ES 2.0
         */
        GLSLES2("100"),

        /**
         * Represent OpenGL ES Pipeline Language 3 (August 2012).
         *
         * @since OpenGL ES 3.0
         */
        GLSLES3("300"),

        /**
         * Represent OpenGL Pipeline Language 1.20 (2006).
         *
         * @since OpenGL 2.1
         */
        GLSL210("120"),

        /**
         * Represent OpenGL Pipeline Language 1.30 (August 2008).
         *
         * @since OpenGL 3.0
         */
        GLSL300("130"),

        /**
         * Represent OpenGL Pipeline Language 1.40 (March 2009).
         *
         * @since OpenGL 3.1
         */
        GLSL310("140"),

        /**
         * Represent OpenGL Pipeline Language 1.50 (August 2009).
         *
         * @since OpenGL 3.2
         */
        GLSL320("150"),

        /**
         * Represent OpenGL Pipeline Language 3.30 (February 2010).
         *
         * @since OpenGL 3.3
         */
        GLSL330("330");

        public final String eName;

        /**
         * <code>Default constructor</code>
         */
        ShaderLanguageVersion(String name) {
            eName = name;
        }
    }

    private final LanguageVersion mVersion;
    private final Map<Limit, Float> mLimits;
    private final Map<Extension, Boolean> mExtensions;

    /**
     * <p>Constructor</p>
     */
    public RenderCapabilities(LanguageVersion version, Map<Extension, Boolean> extensions, Map<Limit, Float> limits) {
        mVersion = version;
        mExtensions = extensions;
        mLimits = limits;
    }

    /**
     * <p>Check if the given extension of the <b>OpenGL framework</b> is supported</p>
     *
     * @param extension the extension to check if supported
     *
     * @return <code>true</code> if the extension is supported, <code>false</code> otherwise
     */
    public boolean hasExtension(Extension extension) {
        return mExtensions.getOrDefault(extension, false);
    }

    /**
     * <p>Get the limit of a variable in the <b>OpenGL framework</b></p>
     *
     * @param limit the limit to retrieve
     *
     * @return the value of the limit
     */
    public float getLimit(Limit limit) {
        return mLimits.getOrDefault(limit, 0.0f);
    }

    /**
     * <p>Get the version of the <b>OpenGL framework</b></p>
     *
     * @return the version of the <b>OpenGL framework</b>
     */
    public LanguageVersion getVersion() {
        return mVersion;
    }

    /**
     * <p>Get the version of the <b>OpenGL</b> shading language</p>
     *
     * @return the version of the <b>OpenGL</b> shading language
     */
    public ShaderLanguageVersion getShaderLanguageVersion() {
        return mVersion.getShaderVersion();
    }
}