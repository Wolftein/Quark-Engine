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
package org.quark.engine.media.opengl;

import java.util.EnumMap;

/**
 * <code>OpenGLCapabilities</code> contain(s) all capabilities supported by the engine for {@link GL}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class GLCapabilities {
    /**
     * <code>Extension</code> enumerates all supported extension(s).
     */
    public enum Extension {
        ARB_explicit_attribute_location
    }

    /**
     * <code>LanguageVersion</code> enumerates supported version(s)
     */
    public enum LanguageVersion {
        /**
         * Represent OpenGL 3.0 (August 2008).
         * <p>
         * {@since OpenGL 3.0}
         */
        GL30,

        /**
         * Represent OpenGL 3.1 (March 2009).
         * <p>
         * {@since OpenGL 3.1}
         */
        GL31,

        /**
         * Represent OpenGL 3.2 (August 2009).
         * <p>
         * {@since OpenGL 3.2}
         */
        GL32,

        /**
         * Represent OpenGL 3.3 (February 2010).
         * <p>
         * {@since OpenGL 3.3}
         */
        GL33;

        /**
         * <p>Parse the {@link LanguageVersion} from a string</p>
         *
         * @param version the string that contain(s) the version
         *
         * @return the enumeration parsed from the string
         */
        public static LanguageVersion fromStringVersion(String version) {
            final String id = version.substring(0, version.indexOf(" "));
            switch (id) {
                case "3.0.0":
                    return GL30;
                case "3.1.0":
                    return GL31;
                case "3.2.0":
                    return GL32;
                case "3.3.0":
                    return GL33;
                default:
                    return GL33;    // TODO: Implement more version(s) in the future
            }
        }
    }

    /**
     * <code>ShaderLanguageVersion</code> enumerates supported shading language(s)
     */
    public enum ShaderLanguageVersion {
        /**
         * Represent OpenGL Shader Language 1.30 (August 2008).
         * <p>
         * {@since OpenGL 3.0}
         */
        GLSL300("130"),

        /**
         * Represent OpenGL Shader Language 1.40 (March 2009).
         * <p>
         * {@since OpenGL 3.1}
         */
        GLSL310("140"),

        /**
         * Represent OpenGL Shader Language 1.50 (August 2009).
         * <p>
         * {@since OpenGL 3.2}
         */
        GLSL320("150"),

        /**
         * Represent OpenGL Shader Language 3.30 (February 2010).
         * <p>
         * {@since OpenGL 3.3}
         */
        GLSL330("330");

        public final String eName;

        /**
         * <code>Default constructor</code>
         */
        ShaderLanguageVersion(String name) {
            eName = name;
        }

        /**
         * <p>Parse the {@link ShaderLanguageVersion} from a string</p>
         *
         * @param version the string that contain(s) the version
         *
         * @return the enumeration parsed from the string
         */
        public static ShaderLanguageVersion fromStringVersion(String version) {
            final String id = version.substring(0, version.indexOf(" "));
            switch (id) {
                case "1.30":
                    return GLSL300;
                case "1.40":
                    return GLSL310;
                case "1.50":
                    return GLSL320;
                case "3.30":
                    return GLSL330;
                default:
                    return GLSL330;    // TODO: Implement more version(s) in the future
            }
        }
    }

    private final LanguageVersion mVersion;
    private final ShaderLanguageVersion mShaderLanguageVersion;
    private final EnumMap<Extension, Boolean> mExtensions;

    /**
     * <p>Constructor</p>
     */
    public GLCapabilities(LanguageVersion version, ShaderLanguageVersion shaderLanguageVersion,
                          EnumMap<Extension, Boolean> extensions) {
        mVersion = version;
        mShaderLanguageVersion = shaderLanguageVersion;
        mExtensions = extensions;
    }

    /**
     * <p>Check whenever the given extension of the <b>OpenGL framework</b> is supported</p>
     *
     * @param extension the extension to check if supported
     *
     * @return <code>true</code> if the extension is supported, <code>false</code> otherwise
     */
    public boolean hasExtension(Extension extension) {
        return mExtensions.getOrDefault(extension, false);
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
        return mShaderLanguageVersion;
    }
}