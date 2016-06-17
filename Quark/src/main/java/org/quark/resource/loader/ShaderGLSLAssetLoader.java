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
package org.quark.resource.loader;

import com.novoda.sax.Element;
import com.novoda.sax.RootElement;
import com.novoda.sexp.RootTag;
import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.Streamer;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.marshaller.AttributeMarshaller;
import com.novoda.sexp.marshaller.StringBodyMarshaller;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import org.quark.mathematic.*;
import org.quark.render.RenderCapabilities;
import org.quark.render.shader.*;
import org.quark.render.shader.data.*;
import org.quark.resource.AssetKey;
import org.quark.resource.AssetLoader;
import org.quark.resource.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>ShaderGLSLAssetLoader</code> encapsulate an {@linkplain AssetLoader} for loading GLSL shader(s).
 * <p>
 * NOTE: This class requires improvement.
 */
public final class ShaderGLSLAssetLoader implements AssetLoader<Shader, Shader.Descriptor> {
    private final PipelineParser mParser = new PipelineParser(SimpleEasyXmlParser.getElementFinderFactory());

    /**
     * Encapsulate the pipeline structure.
     */
    private final static class Header {
        public String mPrecision;

        public final Map<String, String> mProcessor = new HashMap<>();
        public final Map<String, Attribute> mAttributes = new HashMap<>();
        public final Map<String, Uniform> mUniforms = new HashMap<>();
    }

    /**
     * Hold the capabilities of the glsl parser.
     */
    private final RenderCapabilities mCapabilities;

    /**
     * <p>Constructor</p>
     */
    public ShaderGLSLAssetLoader(RenderCapabilities capabilities) {
        mCapabilities = capabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetKey<Shader, Shader.Descriptor> load(AssetManager manager, InputStream input,
            Shader.Descriptor descriptor) throws IOException {
        return new AssetKey<>(readShader(descriptor, input), descriptor);
    }

    /**
     * <p>Read a {@link Shader} from the {@link InputStream} given</p>
     *
     * @param descriptor the shader descriptor
     * @param input      the input-stream that contain(s) the shader
     *
     * @return the shader
     *
     * @throws IOException indicates failing loading the shader
     */
    private Shader readShader(Shader.Descriptor descriptor, InputStream input) throws IOException {
        System.out.println("Hello");

        //!
        //! Parse the XML file that contain(s) the pipeline.
        //!
        final Pipeline pipeline = SimpleEasyXmlParser.parse(input, mParser);

        final Header header = new Header();
        header.mPrecision = pipeline.precision;

        //!
        //! Handle and transform each stage.
        //!
        final List<Stage> stages = new ArrayList<>(pipeline.stage.size());

        for (final Unit stage : pipeline.stage) {
            stages.add(parseStage(stage, descriptor, header));
        }
        return new Shader(stages, header.mAttributes, header.mUniforms);
    }

    /**
     * <p>Parse a {@link Stage} from the {@link Stage} given</p>
     *
     * @param stage      the stage
     * @param descriptor the stage descriptor
     * @param header     the stage header
     *
     * @return the parsed stage
     *
     * @throws IOException indicates failing parsing the stage
     */
    private Stage parseStage(Unit stage, Shader.Descriptor descriptor, Header header) throws IOException {
        final StringBuilder content = new StringBuilder();

        //!
        //! Parse the type of the stage.
        //!
        final StageType type = StageType.valueOf(stage.type.toUpperCase());

        //!
        //! Build the version and extension(s) of the stage.
        //!
        content.append("#version ").append(mCapabilities.getShaderLanguageVersion().eName).append("\n\n");

        if (mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
            content.append("#extension GL_ARB_explicit_attrib_location : require").append("\n");
        }
        if (mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM)) {
            content.append("#extension GL_ARB_explicit_uniform_location : require").append("\n");
        }
        content.append("\n\n");

        //!
        //! OpenGL ES require precision.
        //!
        if (mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSLES2
                || mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSLES3) {
            content.append("precision ").append(header.mPrecision).append(" float\n\n");
        }

        //!
        //! Build input-data
        //!
        for (final Input input : stage.input) {
            content.append(parseInput(header, type, input)).append("\n");
        }
        content.append("\n\n");

        //!
        //! Build output-data
        //!
        for (final Output output : stage.output) {
            content.append(parseOutput(header, type, output)).append("\n");
        }
        content.append("\n\n");

        //!
        //! Build uniform-data
        //!
        for (final Variable variable : stage.uniform) {
            content.append(parseVariable(header, type, variable)).append("\n");
        }
        content.append("\n\n");

        //!
        //! Build method
        //!
        for (final String literal : stage.literal) {
            content.append(literal).append("\n");
        }
        content.append("\n\n");

        //!
        //! Build entry
        //!
        content.append("void main()\n{\n").append(stage.entry).append("\n}");

        return new Stage(content.toString(), type);
    }

    /**
     * <p>Parse input-attribute(s)</p>
     */
    private String parseInput(Header header, StageType type, Input input) throws IOException {
        final boolean isDeprecated =
                (mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSL210
                        || mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSLES2);

        //!
        //! Check if the element is valid.
        //!
        if (input.index == -1 && type == StageType.VERTEX) {
            throw new IOException("<Index> is required for input-attribute(s) in vertex-shader");
        }

        //!
        //! Produce the code.
        //!
        if (type == StageType.VERTEX) {
            header.mAttributes.put(input.name, new Attribute(input.index, Attribute.TYPE_INPUT));

            if (isDeprecated) {
                return "attribute " + input.type + " " + input.name + ";";
            } else if (mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                return "layout(location = " + input.index + ")" + " in " + input.type + " " + input.name + ";";
            }
        } else if (isDeprecated) {
            return "varying " + input.type + " " + input.name + ";";
        }
        return "in " + input.type + " " + input.name + ";";
    }

    /**
     * <p>Parse output-attribute(s)</p>
     */
    private String parseOutput(Header header, StageType type, Output output) throws IOException {
        final boolean isDeprecated =
                (mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSL210
                        || mCapabilities.getShaderLanguageVersion() == RenderCapabilities.ShaderLanguageVersion.GLSLES2);

        //!
        //! Check if the element is valid.
        //!
        if (output.index == -1 && type == StageType.FRAGMENT) {
            throw new IOException("<Index> is required for output-attribute(s) in fragment-shader");
        }

        //!
        //! Produce the code.
        //!
        if (type == StageType.FRAGMENT) {
            header.mAttributes.put(output.name, new Attribute(output.index, Attribute.TYPE_OUTPUT));

            if (isDeprecated) {
                return "";  // TODO: What to do here?
            }
            if (mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                return "layout(location = " + output.index + ")" + " out " + output.type + " " + output.name + ";";
            }
        } else if (isDeprecated) {
            return "varying " + output.type + " " + output.name + ";";
        }
        return "out " + output.type + " " + output.name + ";";
    }

    /**
     * <p>Parse variable(s)</p>
     */
    private String parseVariable(Header header, StageType type, Variable variable) throws IOException {
        String content = "uniform " + variable.type + " " + variable.name;
        if (variable.length > 1) {
            content += "[" + variable.length + "]";
        }
        content += ";";

        //!
        //! Register the variable.
        //!
        switch (variable.type.toUpperCase()) {
            //!
            //! (Float, Float2, Float3, Float4)
            //!
            case "FLOAT":
                if (variable.length == 1) {
                    header.mUniforms.put(variable.name, new UniformFloat(0.0f));
                } else {
                    header.mUniforms.put(variable.name, new UniformFloatArray(variable.length));
                }
                break;
            case "VEC2":
                header.mUniforms.put(variable.name, new UniformFloat2(MutableVector2f.zero()));
                break;
            case "VEC3":
                header.mUniforms.put(variable.name, new UniformFloat3(MutableVector3f.zero()));
                break;
            case "VEC4":
                header.mUniforms.put(variable.name, new UniformFloat4(MutableVector4f.zero()));
                break;

            //!
            //! (Int, Int2, Int3, Int4)
            //!
            case "BOOL":
            case "INT":
                if (variable.length == 1) {
                    header.mUniforms.put(variable.name, new UniformInt(0));
                } else {
                    header.mUniforms.put(variable.name, new UniformIntArray(variable.length));
                }
                break;

            case "BVEC2":
            case "IVEC2":
                header.mUniforms.put(variable.name, new UniformInt2(MutableVector2i.zero()));
                break;

            case "BVEC3":
            case "IVEC3":
                header.mUniforms.put(variable.name, new UniformInt3(MutableVector3i.zero()));
                break;

            case "BVEC4":
            case "IVEC4":
                header.mUniforms.put(variable.name, new UniformInt4(MutableVector4i.zero()));
                break;

            //!
            //! (UInt, UInt2, UInt3, UInt4)
            //!
            case "UINT":
                if (variable.length == 1) {
                    header.mUniforms.put(variable.name, new UniformUnsignedInt(0));
                } else {
                    header.mUniforms.put(variable.name, new UniformUnsignedIntArray(variable.length));
                }
                break;
            case "UVEC2":
                header.mUniforms.put(variable.name, new UniformUnsignedInt2(MutableVector2i.zero()));
                break;
            case "UVEC3":
                header.mUniforms.put(variable.name, new UniformUnsignedInt3(MutableVector3i.zero()));
                break;
            case "UVEC4":
                header.mUniforms.put(variable.name, new UniformUnsignedInt4(MutableVector4i.zero()));
                break;

            //!
            //! (Mat3, Mat4)
            //!
            case "MAT3":
                header.mUniforms.put(variable.name, new UniformMatrix3());
                break;
            case "MAT4":
                header.mUniforms.put(variable.name, new UniformMatrix4());
                break;

            //!
            //! (Sampler)
            //!
            case "SAMPLER1D":
            case "ISAMPLER1D":
            case "USAMPLER1D":
            case "SAMPLER1DARRAY":
            case "ISAMPLER1DARRAY":
            case "USAMPLER1DARRAY":
            case "SAMPLER2D":
            case "ISAMPLER2D":
            case "USAMPLER2D":
            case "SAMPLER2DARRAY":
            case "ISAMPLER2DARRAY":
            case "USAMPLER2DARRAY":
            case "SAMPLER2DMS":
            case "ISAMPLER2DMS":
            case "USAMPLER2DMS":
            case "SAMPLER2DMSARRAY":
            case "ISAMPLER2DMSARRAY":
            case "USAMPLER2DMSARRAY":
            case "SAMPLER3D":
            case "ISAMPLER3D":
            case "USAMPLER3D":
            case "SAMPLER1DSHADOW":
            case "SAMPLER1DARRAYSHADOW":
            case "SAMPLER2DSHADOW":
            case "SAMPLER2DARRAYSHADOW":
            case "SAMPLERCUBESHADOW":
            case "SAMPLERCUBEARRAYSHADOW":
            case "SAMPLERBUFFER":
            case "ISAMPLERBUFFER":
            case "USAMPLERBUFFER":
                header.mUniforms.put(variable.name, new UniformInt(0));
                break;
        }

        //!
        //! Produce the code.
        //!
        if (mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM)) {
            return "layout(location = " + variable.index + ") " + content;
        }
        return content;
    }

    /**
     * Encapsulate a structure for holding an input-attribute.
     */
    private final static class Input {
        /**
         * Hold the unique position of the attribute
         */
        public int index;

        /**
         * Hold the unique name of the attribute
         */
        public String name;

        /**
         * Hold the type of the attribute
         */
        public String type;

        /**
         * Hold the precision of the attribute
         */
        public String precision;
    }

    /**
     * Encapsulate an {@link AttributeMarshaller} for loading {@link InputAttributeMarshaller};
     */
    private final static class InputAttributeMarshaller implements AttributeMarshaller<Input> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Input marshall(String... input) {
            final Input instance = new Input();

            instance.index = input[0] != null ? Integer.parseInt(input[0]) : -1;

            instance.name = input[1];
            instance.type = input[2];
            instance.precision = input[3];

            return instance;
        }
    }

    /**
     * Encapsulate a structure for holding an output-attribute.
     */
    private final static class Output {
        /**
         * Hold the unique position of the attribute
         */
        public int index;

        /**
         * Hold the unique name of the attribute
         */
        public String name;

        /**
         * Hold the type of the attribute
         */
        public String type;

        /**
         * Hold the precision of the attribute
         */
        public String precision;
    }

    /**
     * Encapsulate an {@link AttributeMarshaller} for loading {@link OutputAttributeMarshaller};
     */
    private final static class OutputAttributeMarshaller implements AttributeMarshaller<Output> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Output marshall(String... input) {
            final Output instance = new Output();

            instance.index = input[0] != null ? Integer.parseInt(input[0]) : -1;

            instance.name = input[1];
            instance.type = input[2];
            instance.precision = input[3];

            return instance;
        }
    }

    /**
     * Encapsulate a structure for holding a variable.
     */
    private final static class Variable {
        /**
         * Hold the unique position of the variable
         */
        public int index;

        /**
         * Hold the length of the variable
         */
        public int length;

        /**
         * Hold the unique name of the variable
         */
        public String name;

        /**
         * Hold the type of the variable
         */
        public String type;

        /**
         * Hold the precision of the variable
         */
        public String precision;
    }

    /**
     * Encapsulate an {@link AttributeMarshaller} for loading {@link Variable};
     */
    private final static class VariableAttributeMarshaller implements AttributeMarshaller<Variable> {
        /**
         * {@inheritDoc}
         */
        @Override
        public Variable marshall(String... input) {
            final Variable instance = new Variable();

            instance.index = input[0] != null ? Integer.parseInt(input[0]) : -1;

            instance.name = input[1];
            instance.type = input[2];

            instance.length = input[3] != null ? Integer.parseInt(input[3]) : 1;

            instance.precision = input[4];

            return instance;
        }
    }

    /**
     * Encapsulate a structure for holding a stage.
     */
    private final static class Unit {
        /**
         * Hold all {@link Input} attribute(s).
         */
        public List<Input> input = new ArrayList<>();

        /**
         * Hold all {@link Output} attribute(s).
         */
        public List<Output> output = new ArrayList<>();

        /**
         * Hold all {@link Variable}.
         */
        public List<Variable> uniform = new ArrayList<>();

        /**
         * Hold all literal(s).
         */
        public List<String> literal = new ArrayList<>();

        /**
         * Hold the type of the stage.
         */
        public String type;

        /**
         * Hold the entry of the stage.
         */
        public String entry;
    }

    /**
     * Encapsulate an {@link Parser} for loading {@link Stage};
     */
    private final static class UnitParser implements Parser<Unit> {
        private final ElementFinder<Input> mFinder0;
        private final ElementFinder<Output> mFinder1;
        private final ElementFinder<Variable> mFinder2;
        private final ElementFinder<String> mFinder3;
        private final ElementFinder<String> mFinder4;

        /**
         * Hold the structure of the element.
         */
        private Unit mUnit;

        /**
         * <p>Constructor</p>
         */
        public UnitParser(ElementFinderFactory factory) {
            mFinder0 = factory.getListAttributeFinder(new InputAttributeMarshaller(),
                    A -> mUnit.input.add(A), "index", "name", "type", "precision");
            mFinder1 = factory.getListAttributeFinder(new OutputAttributeMarshaller(),
                    A -> mUnit.output.add(A), "index", "name", "type", "precision");
            mFinder2 = factory.getListAttributeFinder(new VariableAttributeMarshaller(),
                    A -> mUnit.uniform.add(A), "index", "name", "type", "length", "precision");
            mFinder3 = factory.getStringFinder();
            mFinder4 = factory.getListElementFinder(new StringBodyMarshaller(),
                    A -> mUnit.literal.add(A));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void parse(Element element, ParseWatcher<Unit> listener) {
            mFinder0.find(element, "input");
            mFinder1.find(element, "output");
            mFinder2.find(element, "uniform");
            mFinder3.find(element, "entry");
            mFinder4.find(element, "literal");

            element.setStartElementListener(attributes -> {
                mUnit = new Unit();
                mUnit.type = attributes.getValue("type");
            });
            element.setEndElementListener(() -> {
                mUnit.entry = mFinder3.getResultOrThrow();

                listener.onParsed(mUnit);
            });
        }
    }

    /**
     * Encapsulate a structure for holding a pipeline.
     */
    private final static class Pipeline {
        /**
         * Hold the name of the pipeline.
         */
        public String name;

        /**
         * Hold the precision of the pipeline.
         */
        public String precision;

        /**
         * Hold all the stage(s) of the pipeline.
         */
        public List<Unit> stage = new ArrayList<>();
    }

    /**
     * Encapsulate an {@link Streamer} for loading {@link Pipeline};
     */
    private final static class PipelineParser implements Streamer<Pipeline> {
        private final ElementFinder<Unit> mFinder0;

        /**
         * Hold the structure of the element.
         */
        private Pipeline mPipeline;

        /**
         * <p>Constructor</p>
         */
        public PipelineParser(ElementFinderFactory factory) {
            mFinder0 = factory.getListElementFinder(new UnitParser(factory), (unit) -> mPipeline.stage.add(unit));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RootTag getRootTag() {
            return RootTag.create("pipeline");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void stream(RootElement element) {
            mFinder0.find(element, "stage");

            element.setStartElementListener(attributes -> {
                mPipeline = new Pipeline();
                mPipeline.name = attributes.getValue("name");
                mPipeline.precision = attributes.getValue("precision");
            });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Pipeline getStreamResult() {
            return mPipeline;
        }
    }
}
