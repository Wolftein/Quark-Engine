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
package org.quark.engine.resource.loader;

import org.quark.engine.media.opengl.GLCapabilities;
import org.quark.engine.media.opengl.shader.*;
import org.quark.engine.media.opengl.shader.data.*;
import org.quark.engine.resource.AssetKey;
import org.quark.engine.resource.AssetLoader;
import org.quark.engine.resource.AssetManager;
import org.quark.engine.utility.property.InvalidPreferenceException;
import org.quark.engine.utility.property.PropertyTree;
import org.quark.engine.utility.property.PropertyTreeComponent;
import org.quark.engine.utility.property.yaml.YAMLPropertyTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulate an {@linkplain AssetLoader} for loading GLSL program(s).
 * <p>
 * {@link org.quark.engine.media.opengl.shader.Shader}
 * {@link org.quark.engine.media.opengl.shader.ShaderPipeline}
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class PipelineAssetLoader implements AssetLoader<ShaderPipeline, ShaderPipeline.Descriptor> {
    /**
     * Encapsulate the pipeline structure.
     */
    private final static class Header {
        public final Map<String, String> mProcessor = new HashMap<>();
        public final Map<String, Attribute> mAttributes = new HashMap<>();
        public final Map<String, Uniform> mUniforms = new HashMap<>();
    }

    /**
     * Encapsulate a pattern for '<%(GROUP1) (GROUP2)%>'
     */
    private final static Pattern TOKEN_PATTERN = Pattern.compile("<%([^\\s]+)(\\s[^>]*?)?(?<!/)%>");

    /**
     * Encapsulate a pattern for 'Property = Value'
     */
    private final static Pattern TOKEN_DATA_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*\"(.*?)\"");

    private final GLCapabilities mCapabilities;

    /**
     * <p>Constructor</p>
     */
    public PipelineAssetLoader(GLCapabilities capabilities) {
        mCapabilities = capabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetKey<ShaderPipeline, ShaderPipeline.Descriptor> load(AssetManager context,
            ShaderPipeline.Descriptor descriptor, InputStream in) throws IOException {
        final PropertyTree tree = new YAMLPropertyTree();
        try {
            tree.load(in);
        } catch (InvalidPreferenceException exception) {
            throw new IOException(exception);
        }

        //!
        //! Parse the header and pre-processor for the pipeline
        //!
        final Header header = new Header();
        header.mProcessor.putAll(parseProcessor(tree));
        header.mProcessor.putAll(descriptor.getVariables());

        //!
        //! Parse the shader list for the pipeline
        //!
        final List<Map<String, ?>> pipeline = tree.getMapList("Pipeline");
        final List<Shader> stages = new ArrayList<>(pipeline.size());

        for (final Map<String, ?> data : pipeline) {
            //!
            //! Read the type of the shader as <code>ShaderType</code>
            //!
            final ShaderType type = ShaderType.valueOf(String.valueOf(data.get("Type")));

            //!
            //! Read the filename of the <code>Shader</code>
            //!
            final String filename = String.valueOf(data.get("File"));

            final String source0 = "#version " + mCapabilities.getShaderLanguageVersion().eName + "\n\n";
            final String source1 = parseShader(context, header, type, context.findAsset(filename));
            stages.add(new Shader(source0 + source1, type));
        }
        return new AssetKey<>(new ShaderPipeline(stages, header.mAttributes, header.mUniforms), descriptor);
    }

    /**
     * <p>Parse the processor of the {@link ShaderPipeline} from {@link PropertyTree}</p>
     *
     * @param in the processor's input
     *
     * @return a map that contain(s) the name and the value of the preprocessor variable(s)
     */
    private Map<String, String> parseProcessor(PropertyTree in) {
        final PropertyTreeComponent component = in.getComponent("Preprocessor");

        if (component != null) {
            final Map<String, String> processor = new HashMap<>(component.getKeys().size());

            component.getKeys().forEach(key -> processor.put(key, String.valueOf(component.get(key))));
            return processor;
        }
        return new HashMap<>();
    }

    /**
     * <p>Parse a shader file</p>
     */
    private String parseShader(AssetManager manager, Header header, ShaderType type, InputStream in)
            throws IOException {
        final StringBuilder buffer = new StringBuilder(in.available());

        //!
        //! Read the content of the shader
        //!
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            final Matcher matcher1 = TOKEN_PATTERN.matcher("");
            final Matcher matcher2 = TOKEN_DATA_PATTERN.matcher("");
            final Map<String, String> data = new HashMap<>();

            //!
            //! Parse line by line the content of the shader and replace any feature missing from previously
            //! GLSL version(s)
            //!
            for (String inLine = reader.readLine(); inLine != null; inLine = reader.readLine()) {
                matcher1.reset(inLine);

                if (matcher1.matches()) {
                    data.clear();

                    //!
                    //! Parse the token pattern (custom token(s) from the GLSL parser)
                    //!
                    matcher2.reset(matcher1.group(2));
                    while (matcher2.find()) {
                        data.put(matcher2.group(1).toUpperCase(), matcher2.group(2));
                    }

                    switch (matcher1.group(1).toUpperCase()) {
                        case "INCLUDE":
                            return parseShader(manager, header, type, manager.findAsset(data.get("FILE")));
                        case "OUT":
                            return onTokenOut(header, type, data);
                        case "IN":
                            return onTokenIn(header, type, data);
                        case "UNIFORM":
                            return onTokenUniform(header, type, data);
                        default:
                            throw new IOException("Trying to parse an invalid token '" + matcher1.group(1) + "'");
                    }

                } else {
                    //!
                    //! Parse everything else that doesn't fall in the token pattern
                    //!
                    buffer.append(inLine);
                }
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    /**
     * <p>Parse shader token <code>IN</code></p>
     */
    private String onTokenIn(Header header, ShaderType type, Map<String, String> data) throws IOException {
        //!
        //! Retrieves all value(s) of the token
        //!
        final int tID = Integer.valueOf(data.getOrDefault("ID", "-1"));
        final String tType = data.getOrDefault("TYPE", null);
        final String tName = data.getOrDefault("NAME", null);

        //!
        //! Check if the given input is valid
        //!
        if (tType == null) {
            throw new IOException("Missing <Type> tag in <IN> token");
        } else if (tName == null) {
            throw new IOException("Missing <Name> tag in <IN> token");
        } else if (tID == -1 && type == ShaderType.VERTEX) {
            throw new IOException("Missing <ID> tag in <IN> token");
        }

        //!
        //! Produce the code
        //!
        switch (type) {
            case VERTEX:
                //!
                //! Register the attribute
                //!
                header.mAttributes.put(tName, new Attribute(tID, Attribute.TYPE_INPUT));

                if (mCapabilities.hasExtension(GLCapabilities.Extension.ARB_explicit_attribute_location)) {
                    return "layout(location = " + tID + ")" + " in " + tType + " " + tName + ";";
                }
                return "in " + tType + " " + tName + ";";
            case FRAGMENT:
                return "in " + tType + " " + tName + ";";
        }
        throw new IOException("<IN> is not supported in <" + type.name() + ">");
    }

    /**
     * <p>Parse shader token <code>OUT</code></p>
     */
    private String onTokenOut(Header header, ShaderType type, Map<String, String> data) throws IOException {
        //!
        //! Retrieves all value(s) of the token
        //!
        final int tID = Integer.valueOf(data.getOrDefault("ID", "-1"));
        final String tType = data.getOrDefault("TYPE", null);
        final String tName = data.getOrDefault("NAME", null);

        //!
        //! Check if the given input is valid
        //!
        if (tType == null) {
            throw new IOException("Missing <Type> tag in <OUT> token");
        } else if (tName == null) {
            throw new IOException("Missing <Name> tag in <OUT> token");
        } else if (tID == -1 && type == ShaderType.FRAGMENT) {
            throw new IOException("Missing <ID> tag in <OUT> token");
        }

        //!
        //! Produce the code
        //!
        switch (type) {
            case FRAGMENT:
                //!
                //! Register the attribute
                //!
                header.mAttributes.put(tName, new Attribute(tID, Attribute.TYPE_OUTPUT));

                if (mCapabilities.hasExtension(GLCapabilities.Extension.ARB_explicit_attribute_location)) {
                    return "layout(location = " + tID + ")" + " out " + tType + " " + tName + ";";
                }
                return "out " + tType + " " + tName + ";";
            case VERTEX:
                return "out " + tType + " " + tName + ";";
        }
        throw new IOException("<OUT> is not supported in <" + type.name() + ">");
    }

    /**
     * <p>Parse shader token <code>UNIFORM</code></p>
     */
    private String onTokenUniform(Header header, ShaderType type, Map<String, String> data) throws IOException {
        //!
        //! Retrieves all value(s) of the token
        //!
        final int tLength = Integer.valueOf(data.getOrDefault("LENGTH", "1"));
        final String tType = data.getOrDefault("TYPE", null);
        final String tName = data.getOrDefault("NAME", null);

        //!
        //! Check if the given input is valid
        //!
        if (tType == null) {
            throw new IOException("Missing <Type> tag in <UNIFORM> token");
        } else if (tName == null) {
            throw new IOException("Missing <Name> tag in <UNIFORM> token");
        } else if (tLength <= 0) {
            throw new IOException("Invalid <Length> for <UNIFORM> token");
        }

        //!
        //! Allocate and register the uniform
        //!
        final Uniform uniform;
        switch (tType.toUpperCase()) {
            case "FLOAT":
                switch (tLength) {
                    case 1:
                        uniform = new UniformFloat();
                        break;
                    case 2:
                        uniform = new UniformFloat2();
                        break;
                    case 3:
                        uniform = new UniformFloat3();
                        break;
                    case 4:
                        uniform = new UniformFloat4();
                        break;
                    default:
                        uniform = new UniformFloatArray(tLength);
                        break;
                }
                break;
            case "VEC2":
                switch (tLength) {
                    case 1:
                        uniform = new UniformFloat2();
                        break;
                    case 2:
                        uniform = new UniformFloat4();
                        break;
                    default:
                        uniform = new UniformFloatArray(tLength * 2);
                        break;
                }
                break;
            case "VEC3":
                switch (tLength) {
                    case 1:
                        uniform = new UniformFloat3();
                        break;
                    default:
                        uniform = new UniformFloatArray(tLength * 3);
                        break;
                }
                break;
            case "VEC4":
                switch (tLength) {
                    case 1:
                        uniform = new UniformFloat4();
                        break;
                    default:
                        uniform = new UniformFloatArray(tLength * 4);
                        break;
                }
                break;
            case "BOOL":
            case "INT":
                switch (tLength) {
                    case 1:
                        uniform = new UniformInt();
                        break;
                    case 2:
                        uniform = new UniformInt2();
                        break;
                    case 3:
                        uniform = new UniformInt3();
                        break;
                    case 4:
                        uniform = new UniformInt4();
                        break;
                    default:
                        uniform = new UniformIntArray(tLength);
                        break;
                }
                break;
            case "BVEC2":
            case "IVEC2":
                switch (tLength) {
                    case 1:
                        uniform = new UniformInt2();
                        break;
                    case 2:
                        uniform = new UniformInt4();
                        break;
                    default:
                        uniform = new UniformIntArray(tLength * 2);
                        break;
                }
                break;
            case "BVEC3":
            case "IVEC3":
                switch (tLength) {
                    case 1:
                        uniform = new UniformInt3();
                        break;
                    default:
                        uniform = new UniformIntArray(tLength * 3);
                        break;
                }
                break;
            case "BVEC4":
            case "IVEC4":
                switch (tLength) {
                    case 1:
                        uniform = new UniformInt4();
                        break;
                    default:
                        uniform = new UniformIntArray(tLength * 4);
                        break;
                }
                break;
            case "UINT":
                switch (tLength) {
                    case 1:
                        uniform = new UniformUnsignedInt();
                        break;
                    case 2:
                        uniform = new UniformUnsignedInt2();
                        break;
                    case 3:
                        uniform = new UniformUnsignedInt3();
                        break;
                    case 4:
                        uniform = new UniformUnsignedInt4();
                        break;
                    default:
                        uniform = new UniformUnsignedIntArray(tLength);
                        break;
                }
                break;
            case "UVEC2":
                switch (tLength) {
                    case 1:
                        uniform = new UniformUnsignedInt2();
                        break;
                    case 2:
                        uniform = new UniformUnsignedInt4();
                        break;
                    default:
                        uniform = new UniformUnsignedIntArray(tLength * 2);
                        break;
                }
                break;
            case "UVEC3":
                switch (tLength) {
                    case 1:
                        uniform = new UniformUnsignedInt3();
                        break;
                    default:
                        uniform = new UniformUnsignedIntArray(tLength * 3);
                        break;
                }
                break;
            case "UVEC4":
                switch (tLength) {
                    case 1:
                        uniform = new UniformUnsignedInt4();
                        break;
                    default:
                        uniform = new UniformUnsignedIntArray(tLength * 4);
                        break;
                }
                break;
            case "MAT3":
                switch (tLength) {
                    case 1:
                        uniform = new UniformMatrix3();
                        break;
                    default:
                        throw new IOException("Mat3 array is not supported yet");
                }
                break;
            case "MAT4":
                switch (tLength) {
                    case 1:
                        uniform = new UniformMatrix4();
                        break;
                    default:
                        throw new IOException("Mat4 array is not supported yet");
                }
                break;
            case "SAMPLER1D":
            case "SAMPLER2D":
            case "SAMPLER3D":
                uniform = new UniformInt();
                break;
            default:
                throw new IOException("Uniform type '" + tType + "' not supported");
        }
        header.mUniforms.put(tName, uniform);

        //!
        //! Produce the code
        //!
        return "uniform " + tType + " " + tName + (tLength > 1 ? "[" + tLength + "];" : ";");
    }
}