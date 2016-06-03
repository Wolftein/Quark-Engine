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
package org.quark_engine.render;

import java.util.EnumMap;

/**
 * <code>RenderCapabilities</code> contain(s) all capabilities supported by {@link Render}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class RenderCapabilities {
    /**
     * <code>Extension</code> enumerates all supported extension(s).
     */
    public enum Extension {
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
         * Indicates the maximum number of texture stage(s).
         */
        TEXTURE_STAGE
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
         * <p>Check if this version is better than the given</p>
         *
         * @param version the other version
         *
         * @return <code>true</code> if this version is better than the given, <code>false</code> otherwise
         */
        public boolean isBetterThan(LanguageVersion version) {
            return ordinal() >= version.ordinal();
        }

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

        /**
         * <p>Parse the {@link LanguageVersion} from an integer</p>
         *
         * @param version the integer that contain(s) the version
         *
         * @return the enumeration parsed from the integer
         */
        public static LanguageVersion fromIntVersion(int version) {
            switch (version) {
                case 30:
                    return GL30;
                case 31:
                    return GL31;
                case 32:
                    return GL32;
                case 33:
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
         * Represent OpenGL Pipeline Language 1.30 (August 2008).
         * <p>
         * {@since OpenGL 3.0}
         */
        GLSL300("130"),

        /**
         * Represent OpenGL Pipeline Language 1.40 (March 2009).
         * <p>
         * {@since OpenGL 3.1}
         */
        GLSL310("140"),

        /**
         * Represent OpenGL Pipeline Language 1.50 (August 2009).
         * <p>
         * {@since OpenGL 3.2}
         */
        GLSL320("150"),

        /**
         * Represent OpenGL Pipeline Language 3.30 (February 2010).
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
         * <p>Check if this version is better than the given</p>
         *
         * @param version the other version
         *
         * @return <code>true</code> if this version is better than the given, <code>false</code> otherwise
         */
        public boolean isBetterThan(ShaderLanguageVersion version) {
            return ordinal() >= version.ordinal();
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

        /**
         * <p>Parse the {@link ShaderLanguageVersion} from an integer</p>
         *
         * @param version the integer that contain(s) the version
         *
         * @return the enumeration parsed from the integer
         */
        public static ShaderLanguageVersion fromIntVersion(int version) {
            switch (version) {
                case 130:
                    return GLSL300;
                case 140:
                    return GLSL310;
                case 150:
                    return GLSL320;
                case 330:
                    return GLSL330;
                default:
                    return GLSL330;    // TODO: Implement more version(s) in the future
            }
        }
    }

    private final LanguageVersion mVersion;
    private final ShaderLanguageVersion mShaderLanguageVersion;
    private final EnumMap<Limit, Float> mLimits;
    private final EnumMap<Extension, Boolean> mExtensions;

    /**
     * <p>Constructor</p>
     */
    public RenderCapabilities(LanguageVersion version, ShaderLanguageVersion shaderLanguageVersion,
            EnumMap<Extension, Boolean> extensions, EnumMap<Limit, Float> limits) {
        mVersion = version;
        mShaderLanguageVersion = shaderLanguageVersion;
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
        return mShaderLanguageVersion;
    }
}