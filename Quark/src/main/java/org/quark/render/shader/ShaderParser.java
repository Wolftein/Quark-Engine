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

import org.quark.render.RenderCapabilities;
import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.Int8Array;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <code>ShaderParser</code> encapsulate an utility class to create {@link Shader} from any source.
 */
public final class ShaderParser {
    private final RenderCapabilities mCapabilities;

    /**
     * Hold up-to 81.920 instruction(s),
     */
    private final Int8Array mArray = ArrayFactory.allocateInt8Array(4096 * 20);

    /**
     * Hold all {@link Generator}.
     */
    private final Map<Integer, Generator> mGenerator = new HashMap<>();

    /**
     * <p>Constructor</p>
     */
    public ShaderParser(RenderCapabilities capabilities) {
        mCapabilities = capabilities;

        mGenerator.put(Builder.OP_BLOCK_BEGIN, new OnHeaderGenerator());
        mGenerator.put(Builder.OP_INPUT, new OnInputGenerator());
        mGenerator.put(Builder.OP_OUTPUT, new OnOutputGenerator());
        mGenerator.put(Builder.OP_UNIFORM, new OnUniformGenerator());
        mGenerator.put(Builder.OP_CODE, new OnCodeGenerator());
    }

    /**
     * <p>Create a {@link StageType#VERTEX}</p>
     *
     * @param precision the default precision of the stage
     *
     * @return <code>VertexBuilder</code> for constructing the stage
     */
    public VertexBuilder vertex(Precision precision) {
        return new VertexBuilder(mArray, precision);
    }

    /**
     * <p>Create a {@link StageType#FRAGMENT}</p>
     *
     * @param precision the default precision of the stage
     *
     * @return <code>VertexBuilder</code> for constructing the stage
     */
    public FragmentBuilder fragment(Precision precision) {
        return new FragmentBuilder(mArray, precision);
    }

    /**
     * <p>Create a {@link Shader} from all instruction(s)</p>
     *
     * @return <code>Shader</code> generated
     */
    public Shader generate() {
        //!
        //! Build the process for the operation.
        //!
        final Generator.Process process = new Generator.Process(mCapabilities);

        mArray.flip();

        while (mArray.hasRemaining()) {
            final StringBuffer output = new StringBuffer(64);

            //!
            //! Parse each instruction.
            //!
            int op;

            while ((op = mArray.readInt8()) != (byte) Builder.OP_BLOCK_END) {
                mGenerator.get(op).generate(process, mArray, output.append('\n'));
            }
            process.stages.add(new Stage(output.toString(), process.stage));
        }

        mArray.clear();

        return new Shader(process.stages, process.attributes, process.uniforms);
    }

    /**
     * <p>Read {@link StageType} from {@link Int8Array}</p>
     */
    private StageType getStageType(Int8Array array) {
        switch (array.readInt8()) {
            case VertexBuilder.TYPE:
                return StageType.VERTEX;
            case FragmentBuilder.TYPE:
                return StageType.FRAGMENT;
        }
        throw new IllegalStateException("Currently only fragment and vertex is supported");
    }

    /**
     * <code>Precision</code> enumerate(s) all supported precision(s).
     */
    public enum Precision {
        LOW("lowp"),
        MEDIUM("mediump"),
        HIGH("highp");

        public final String eName;

        /**
         * <p>Constructor</p>
         */
        Precision(String name) {
            eName = name;
        }
    }

    /**
     * <code>Builder</code> encapsulate the base constructor for any stage.
     */
    public class Builder<A extends Builder<?>> {
        public final static int OP_BLOCK_BEGIN = 0x00;
        public final static int OP_BLOCK_END = 0xFF;

        public final static int OP_INPUT = 0x01;
        public final static int OP_OUTPUT = 0x02;
        public final static int OP_UNIFORM = 0x03;
        public final static int OP_CODE = 0x04;

        /**
         * Hold the array that will contain(s) all information for generating stage(s).
         */
        private final Int8Array mArray;

        /**
         * Hold the default precision of the stage.
         */
        private final Precision mPrecision;

        /**
         * <p>Constructor</p>
         */
        public Builder(Int8Array array, Precision precision) {
            mArray = array;
            mPrecision = precision;

            mArray.writeInt8(OP_BLOCK_BEGIN);
        }

        public A input(int index, String id, AttributeType type) {
            return input(index, id, type, mPrecision);
        }

        public A input(int index, String id, AttributeType type, Precision precision) {
            mArray.writeInt8(OP_INPUT);
            mArray.writeInt8(index);
            mArray.writeString(id);
            mArray.writeInt8(type.ordinal());
            mArray.writeInt8(precision.ordinal());

            return (A) this;
        }

        public A output(int index, String id, AttributeType type) {
            return output(index, id, type, mPrecision);
        }

        public A output(int index, String id, AttributeType type, Precision precision) {
            mArray.writeInt8(OP_OUTPUT);
            mArray.writeInt8(index);
            mArray.writeString(id);
            mArray.writeInt8(type.ordinal());
            mArray.writeInt8(precision.ordinal());

            return (A) this;
        }

        public A code(String code) {
            mArray.writeInt8(OP_CODE);
            mArray.writeString(code);

            return (A) this;
        }

        public ShaderParser build() {
            mArray.writeInt8(OP_BLOCK_END);

            return ShaderParser.this;
        }
    }

    /**
     * Specialised {@link Builder} for {@link StageType#VERTEX}
     */
    public final class VertexBuilder extends Builder<VertexBuilder> {
        public final static int TYPE = 0x00;

        /**
         * <p>Constructor</p>
         */
        public VertexBuilder(Int8Array array, Precision precision) {
            super(array, precision);

            array.writeInt8(TYPE);
            array.writeInt8(precision.ordinal());
        }
    }

    /**
     * Specialised {@link Builder} for {@link StageType#FRAGMENT}
     */
    public final class FragmentBuilder extends Builder<FragmentBuilder> {
        public final static int TYPE = 0x01;

        /**
         * <p>Constructor</p>
         */
        public FragmentBuilder(Int8Array array, Precision precision) {
            super(array, precision);

            array.writeInt8(TYPE);
            array.writeInt8(precision.ordinal());
        }
    }

    /**
     * <code>Generator</code> represent a source generator for {@link ShaderParser}.
     */
    public interface Generator {
        final class Process {
            /**
             * Hold the {@link RenderCapabilities} of the process.
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final RenderCapabilities capabilities;

            /**
             * Hold all {@link Shader} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final List<Stage> stages = new LinkedList<>();

            /**
             * Hold all {@link Attribute} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final Map<String, Attribute> attributes = new HashMap<>();

            /**
             * Hold all {@link Uniform} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final Map<String, Uniform> uniforms = new HashMap<>();

            /**
             * Hold current {@link StageType} being parsed.
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public StageType stage;

            /**
             * <p>Constructor</p>
             */
            public Process(RenderCapabilities capabilities) {
                this.capabilities = capabilities;
            }
        }

        /**
         * <p>Parse instruction(s) from the given {@link Int8Array}</p>
         */
        void generate(Process process, Int8Array input, StringBuffer output);
    }

    /**
     * <code>OnInputGenerator</code> encapsulate a {@link Generator} for input {@link Attribute}(s).
     */
    private final static class OnInputGenerator implements Generator {
        private final static String INPUT_CORE = "in";
        private final static String INPUT_LEGACY = "attribute";
        private final static String INPUT_LEGACY_VARYING = "varying";
        private final static String INPUT_LAYOUT_BEGIN = "layout(location = ";
        private final static String INPUT_LAYOUT_END = ")";

        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Array input, StringBuffer output) {
            final int index
                    = input.readInt8();
            final String name
                    = input.readString();
            final AttributeType attribute
                    = AttributeType.values()[input.readInt8()];
            final Precision precision
                    = Precision.values()[input.readInt8()];

            //!
            //! Pre-check for legacy.
            //!
            final boolean isLegacy = process.capabilities.getVersion().isLegacy();

            if (process.stage == StageType.VERTEX) {
                //!
                //! Support for GLSL_EXPLICIT_ATTRIBUTE extension.
                //!
                if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                    output.append(INPUT_LAYOUT_BEGIN).append(index).append(INPUT_LAYOUT_END).append(" ");
                }

                //!
                //! NOTE: Legacy use 'attribute' for input while core use 'in'.
                //!
                output.append((isLegacy ? INPUT_LEGACY : INPUT_CORE)).append(" ");

                process.attributes.put(name, new Attribute(index, Attribute.MODE_INPUT, attribute));
            } else if (process.stage == StageType.FRAGMENT) {
                //!
                //! NOTE: Legacy use 'varying' for inout while core use 'in'.
                //!
                output.append((isLegacy ? INPUT_LEGACY_VARYING : INPUT_CORE)).append(" ");
            }

            if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_PRECISION)) {
                //!
                //! Support for GLSL_PRECISION extension.
                //!
                output.append(precision.eName).append(" ");
            }
            output.append(attribute.eName).append(" ").append(name).append(";");
        }
    }

    /**
     * <code>OnOutputGenerator</code> encapsulate a {@link Generator} for output {@link Attribute}(s).
     */
    private final static class OnOutputGenerator implements Generator {
        private final static String OUTPUT_CORE = "out";
        private final static String OUTPUT_LEGACY_VARYING = "varying";
        private final static String OUTPUT_LAYOUT_BEGIN = "layout(location = ";
        private final static String OUTPUT_LAYOUT_END = ")";

        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Array input, StringBuffer output) {
            final int index
                    = input.readInt8();
            final String name
                    = input.readString();
            final AttributeType attribute
                    = AttributeType.values()[input.readInt8()];
            final Precision precision
                    = Precision.values()[input.readInt8()];

            //!
            //! Pre-check for legacy.
            //!
            final boolean isLegacy = process.capabilities.getVersion().isLegacy();

            if (process.stage == StageType.FRAGMENT) {

                if (isLegacy) {
                    //!
                    //! NOTE: Legacy doesn't support custom MRT
                    //!
                    output.append("#define ").append(name).append(" ").append("gl_FragData[").append(index).append("]");
                } else {

                    if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                        output.append(OUTPUT_LAYOUT_BEGIN).append(index).append(OUTPUT_LAYOUT_END).append(" ");
                    }
                    output.append(OUTPUT_CORE).append(" ");
                }

                process.attributes.put(name, new Attribute(index, Attribute.MODE_INPUT, attribute));
            } else if (process.stage == StageType.VERTEX) {
                //!
                //! NOTE: Legacy use 'varying' for inout while core use 'out'.
                //!
                output.append((isLegacy ? OUTPUT_LEGACY_VARYING : OUTPUT_CORE)).append(" ");
            }

            if (!isLegacy) {
                if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_PRECISION)) {
                    //!
                    //! Support for GLSL_PRECISION extension.
                    //!
                    output.append(precision.eName).append(" ");
                }
                output.append(attribute.eName).append(" ").append(name).append(";");
            }
        }
    }

    /**
     * <code>OnUniformGenerator</code> encapsulate a {@link Generator} for {@link Uniform}(s).
     */
    private final static class OnUniformGenerator implements Generator {
        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Array input, StringBuffer output) {

        }
    }

    /**
     * <code>OnCodeGenerator</code> encapsulate a {@link Generator} for generating plain code.
     */
    private final static class OnCodeGenerator implements Generator {
        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Array input, StringBuffer output) {
            output.append(input.readString());
        }
    }

    /**
     * <code>OnHeaderGenerator</code> encapsulate a {@link Generator} for generating stage header.
     */
    private final static class OnHeaderGenerator implements Generator {
        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Array input, StringBuffer output) {
            switch (input.readInt8()) {
                case VertexBuilder.TYPE:
                    process.stage = StageType.VERTEX;
                    break;
                case FragmentBuilder.TYPE:
                    process.stage = StageType.FRAGMENT;
                    break;
                default:
                    throw new IllegalStateException("Currently only vertex and fragment is supported");
            }

            //!
            //! Build the version and extension(s) of the stage.
            //!
            output.append("#version ").append(process.capabilities.getShaderLanguageVersion().eName).append("\n");

            if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                output.append("#extension GL_ARB_explicit_attrib_location : require").append("\n");
            }
            if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM)) {
                output.append("#extension GL_ARB_explicit_uniform_location : require").append("\n");
            }
            output.append("\n");

            //!
            //! Apply the context precision.
            //!
            final Precision precision = Precision.values()[input.readInt8()];

            if (process.capabilities.hasExtension(RenderCapabilities.Extension.GLSL_PRECISION)) {
                output.append("precision ").append(precision.eName).append(" float;\n");
            }
        }
    }
}