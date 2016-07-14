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

import ar.com.quark.graphic.GraphicCapabilities;
import ar.com.quark.graphic.shader.data.*;
import ar.com.quark.mathematic.*;
import ar.com.quark.utility.buffer.BufferFactory;
import ar.com.quark.utility.buffer.Int8Buffer;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

/**
 * <code>ShaderParser</code> encapsulate an utility class to create {@link Shader} from any source.
 */
public final class ShaderParser {
    private final GraphicCapabilities mCapabilities;

    /**
     * Hold up-to 81.920 instruction(s),
     */
    private final Int8Buffer mArray = BufferFactory.allocateInt8(4096 * 20);

    /**
     * Hold all {@link Generator}.
     */
    private final ImmutableIntObjectMap<Generator> mGenerator;

    /**
     * <p>Constructor</p>
     */
    public ShaderParser(GraphicCapabilities capabilities) {
        mCapabilities = capabilities;

        final IntObjectHashMap<Generator> generator = new IntObjectHashMap<>(0x05);
        generator.put(Builder.OP_BLOCK_BEGIN, new OnHeaderGenerator());
        generator.put(Builder.OP_INPUT, new OnInputGenerator());
        generator.put(Builder.OP_OUTPUT, new OnOutputGenerator());
        generator.put(Builder.OP_UNIFORM, new OnUniformGenerator());
        generator.put(Builder.OP_CODE, new OnCodeGenerator());

        mGenerator = generator.toImmutable();
    }

    /**
     * <p>Create a {@link StageType#VERTEX}</p>
     *
     * @param precision the default precision of the stage
     *
     * @return <code>VertexBuilder</code> for constructing the stage
     */
    public VertexBuilder vertex(ShaderPrecision precision) {
        return new VertexBuilder(mArray, precision);
    }

    /**
     * <p>Create a {@link StageType#FRAGMENT}</p>
     *
     * @param precision the default precision of the stage
     *
     * @return <code>VertexBuilder</code> for constructing the stage
     */
    public FragmentBuilder fragment(ShaderPrecision precision) {
        return new FragmentBuilder(mArray, precision);
    }

    /**
     * <p>Generate the byte-code of the parser</p>
     *
     * @return an {@link Int8Buffer} that contain(s) the byte-code(s) generated
     */
    public Int8Buffer generate() {
        return mArray.flip();
    }

    /**
     * <p>Create a {@link Shader} from all instruction(s) in the given {@link Int8Buffer}</p>
     *
     * @param array the buffer that contain(s) all instruction(s)
     *
     * @return <code>Shader</code> generated
     */
    public Shader generate(Int8Buffer array) {
        //!
        //! Build the process for the operation.
        //!
        final Generator.Process process = new Generator.Process(mCapabilities);

        while (array.hasRemaining()) {
            final StringBuffer output = new StringBuffer(64);

            //!
            //! Parse each instruction.
            //!
            int op;

            while ((op = array.read()) != (byte) Builder.OP_BLOCK_END) {
                mGenerator.get(op).generate(process, array, output.append('\n'));
            }
            process.stages.add(new Stage(output.toString(), process.stage));
        }

        return new Shader(process.stages.toImmutable(), process.attributes.toImmutable(), process.uniforms.toImmutable());
    }

    /**
     * <p>Read {@link StageType} from {@link Int8Buffer}</p>
     */
    private StageType getStageType(Int8Buffer array) {
        switch (array.read()) {
            case VertexBuilder.TYPE:
                return StageType.VERTEX;
            case FragmentBuilder.TYPE:
                return StageType.FRAGMENT;
        }
        throw new IllegalStateException("Currently only fragment and vertex is supported");
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
         * Hold the buffer that will contain(s) all information for generating stage(s).
         */
        private final Int8Buffer mArray;

        /**
         * Hold the default precision of the stage.
         */
        private final ShaderPrecision mPrecision;

        /**
         * <p>Constructor</p>
         */
        public Builder(Int8Buffer array, ShaderPrecision precision) {
            mArray = array;
            mPrecision = precision;

            mArray.write(OP_BLOCK_BEGIN);
        }

        /**
         * <p>Register an input attribute</p>
         */
        public A input(int index, String id, AttributeType type) {
            return input(index, id, type, mPrecision);
        }

        /**
         * <p>Register an input attribute</p>
         */
        public A input(int index, String id, AttributeType type, ShaderPrecision precision) {
            mArray.write(OP_INPUT);
            mArray.write(index);

            write(mArray, id);

            mArray.write(type.ordinal());
            mArray.write(precision.ordinal());

            return (A) this;
        }

        /**
         * <p>Register an output attribute</p>
         */
        public A output(int index, String id, AttributeType type) {
            return output(index, id, type, mPrecision);
        }

        /**
         * <p>Register an output attribute</p>
         */
        public A output(int index, String id, AttributeType type, ShaderPrecision precision) {
            mArray.write(OP_OUTPUT);
            mArray.write(index);

            write(mArray, id);

            mArray.write(type.ordinal());
            mArray.write(precision.ordinal());

            return (A) this;
        }

        /**
         * <p>Register an uniform</p>
         */
        public A uniform(int index, String id, UniformType type) {
            return uniform(index, id, type, mPrecision, 1);
        }

        /**
         * <p>Register an uniform</p>
         */
        public A uniform(int index, String id, UniformType type, int length) {
            return uniform(index, id, type, mPrecision, length);
        }

        /**
         * <p>Register an uniform</p>
         */
        public A uniform(int index, String id, UniformType type, ShaderPrecision precision) {
            return uniform(index, id, type, precision, 1);
        }

        /**
         * <p>Register an uniform</p>
         */
        public A uniform(int index, String id, UniformType type, ShaderPrecision precision, int length) {
            mArray.write(OP_UNIFORM);
            mArray.write(index);

            write(mArray, id);

            mArray.write(type.ordinal());
            mArray.write(precision.ordinal());
            mArray.write(length);

            return (A) this;
        }

        /**
         * <p>Register a code block</p>
         */
        public A code(String code) {
            mArray.write(OP_CODE);
            write(mArray, code);

            return (A) this;
        }

        /**
         * <p>Build the stage</p>
         *
         * @return a reference to the underlying parser
         */
        public ShaderParser build() {
            mArray.write(OP_BLOCK_END);

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
        public VertexBuilder(Int8Buffer array, ShaderPrecision precision) {
            super(array, precision);

            array.write(TYPE);
            array.write(precision.ordinal());
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
        public FragmentBuilder(Int8Buffer array, ShaderPrecision precision) {
            super(array, precision);

            array.write(TYPE);
            array.write(precision.ordinal());
        }
    }

    /**
     * <code>Generator</code> represent a source generator for {@link ShaderParser}.
     */
    public interface Generator {
        final class Process {
            /**
             * Hold the {@link GraphicCapabilities} of the process.
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final GraphicCapabilities capabilities;

            /**
             * Hold all {@link Shader} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final MutableList<Stage> stages = Lists.mutable.empty();

            /**
             * Hold all {@link Attribute} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final MutableMap<String, Attribute> attributes = Maps.mutable.empty();

            /**
             * Hold all {@link Uniform} parsed from the instruction(s).
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public final MutableMap<String, Uniform> uniforms = Maps.mutable.empty();

            /**
             * Hold current {@link StageType} being parsed.
             * <p>
             * NOTE: Exposed to {@link Generator}
             */
            public StageType stage;

            /**
             * <p>Constructor</p>
             */
            public Process(GraphicCapabilities capabilities) {
                this.capabilities = capabilities;
            }
        }

        /**
         * <p>Parse instruction(s) from the given {@link Int8Buffer}</p>
         */
        void generate(Process process, Int8Buffer input, StringBuffer output);
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
        public void generate(Process process, Int8Buffer input, StringBuffer output) {
            final int index
                    = input.read();
            final String name
                    = read(input);
            final AttributeType attribute
                    = AttributeType.values()[input.read()];
            final ShaderPrecision precision
                    = ShaderPrecision.values()[input.read()];

            //!
            //! Pre-check for legacy.
            //!
            final boolean isLegacy = process.capabilities.getVersion().isLegacy();

            if (process.stage == StageType.VERTEX) {

                //!
                //! Support for GLSL_EXPLICIT_ATTRIBUTE extension.
                //!
                if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                    output.append(INPUT_LAYOUT_BEGIN).append(index).append(INPUT_LAYOUT_END).append(" ");
                }
                //!
                //! NOTE: Legacy use 'attribute' for input while core use 'in'.
                //!
                if (isLegacy) {
                    output.append(INPUT_LEGACY).append(" ");
                } else {
                    output.append(INPUT_CORE).append(" ");
                }

                process.attributes.put(name, new Attribute(index, Attribute.MODE_INPUT, attribute));
            } else if (process.stage == StageType.FRAGMENT) {
                //!
                //! NOTE: Legacy use 'varying' for inout while core use 'in'.
                //!
                if (isLegacy) {
                    output.append(INPUT_LEGACY_VARYING).append(" ");
                } else {
                    output.append(INPUT_CORE).append(" ");
                }
            }

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_PRECISION)) {
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
        public void generate(Process process, Int8Buffer input, StringBuffer output) {
            final int index
                    = input.read();
            final String name
                    = read(input);
            final AttributeType attribute
                    = AttributeType.values()[input.read()];
            final ShaderPrecision precision
                    = ShaderPrecision.values()[input.read()];

            //!
            //! Pre-check for legacy.
            //!
            final boolean isLegacy = process.capabilities.getVersion().isLegacy();

            if (process.stage == StageType.FRAGMENT) {
                process.attributes.put(name, new Attribute(index, Attribute.MODE_INPUT, attribute));

                if (isLegacy) {
                    //!
                    //! NOTE: Legacy doesn't support custom MRT
                    //!
                    output.append("#define ").append(name).append(" ").append("gl_FragData[").append(index).append("]");

                    return;
                } else {
                    if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                        //!
                        //! Support for GLSL_EXPLICIT_ATTRIBUTE extension.
                        //!
                        output.append(OUTPUT_LAYOUT_BEGIN).append(index).append(OUTPUT_LAYOUT_END).append(" ");
                    }
                    output.append(OUTPUT_CORE).append(" ");
                }
            } else if (process.stage == StageType.VERTEX) {
                //!
                //! NOTE: Legacy use 'varying' for inout while core use 'out'.
                //!
                if (isLegacy) {
                    output.append(OUTPUT_LEGACY_VARYING).append(" ");
                } else {
                    output.append(OUTPUT_CORE).append(" ");
                }
            }

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_PRECISION)) {
                //!
                //! Support for GLSL_PRECISION extension.
                //!
                output.append(precision.eName).append(" ");
            }
            output.append(attribute.eName).append(" ").append(name).append(";");
        }
    }

    /**
     * <code>OnUniformGenerator</code> encapsulate a {@link Generator} for {@link Uniform}(s).
     */
    private final static class OnUniformGenerator implements Generator {
        private final static String UNIFORM_CORE = "uniform";
        private final static String UNIFORM_LAYOUT_BEGIN = "layout(location = ";
        private final static String UNIFORM_LAYOUT_END = ")";

        /**
         * {@inheritDoc}
         */
        @Override
        public void generate(Process process, Int8Buffer input, StringBuffer output) {
            final int index
                    = input.read();
            final String name
                    = read(input);
            final UniformType uniform
                    = UniformType.values()[input.read()];
            final ShaderPrecision precision
                    = ShaderPrecision.values()[input.read()];
            final int length
                    = input.read();

            switch (uniform) {
                case Float:
                    if (length == 1) {
                        process.uniforms.put(name, new UniformFloat(0.0f));
                    } else {
                        process.uniforms.put(name, new UniformFloatArray(length));
                    }
                    break;
                case Float2:
                    process.uniforms.put(name, new UniformFloat2(MutableVector2f.zero()));
                    break;
                case Float3:
                    process.uniforms.put(name, new UniformFloat3(MutableVector3f.zero()));
                    break;
                case Float4:
                    process.uniforms.put(name, new UniformFloat4(MutableVector4f.zero()));
                    break;
                case Int:
                    if (length == 1) {
                        process.uniforms.put(name, new UniformInt(0));
                    } else {
                        process.uniforms.put(name, new UniformIntArray(length));
                    }
                    break;
                case Int2:
                    process.uniforms.put(name, new UniformInt2(MutableVector2i.zero()));
                    break;
                case Int3:
                    process.uniforms.put(name, new UniformInt3(MutableVector3i.zero()));
                    break;
                case Int4:
                    process.uniforms.put(name, new UniformInt4(MutableVector4i.zero()));
                    break;
                case UInt:
                    if (length == 1) {
                        process.uniforms.put(name, new UniformUnsignedInt(0));
                    } else {
                        process.uniforms.put(name, new UniformUnsignedIntArray(length));
                    }
                    break;
                case UInt2:
                    process.uniforms.put(name, new UniformUnsignedInt2(MutableVector2i.zero()));
                    break;
                case UInt3:
                    process.uniforms.put(name, new UniformUnsignedInt3(MutableVector3i.zero()));
                    break;
                case UInt4:
                    process.uniforms.put(name, new UniformUnsignedInt4(MutableVector4i.zero()));
                    break;
                case Matrix3x3:
                    process.uniforms.put(name, new UniformMatrix3());
                    break;
                case Matrix4x4:
                    process.uniforms.put(name, new UniformMatrix4());
                    break;
                case Sampler1D:
                case Sampler1DArray:
                case Sampler1DShadow:
                case Sampler1DShadowArray:
                case Sampler2D:
                case Sampler2DArray:
                case Sampler2DShadow:
                case Sampler2DShadowArray:
                case Sampler2DMultisample:
                case Sampler2DMultisampleArray:
                case Sampler3D:
                case SamplerCube:
                case SamplerCubeShadow:
                case SamplerInt1D:
                case SamplerInt1DArray:
                case SamplerInt2D:
                case SamplerInt2DArray:
                case SamplerInt2DMultisample:
                case SamplerInt2DMultisampleArray:
                case SamplerInt3D:
                case SamplerIntCube:
                case SamplerUInt1D:
                case SamplerUInt1DArray:
                case SamplerUInt2D:
                case SamplerUInt2DArray:
                case SamplerUInt2DMultisample:
                case SamplerUInt2DMultisampleArray:
                case SamplerUInt3D:
                case SamplerUIntCube:
                case SamplerBuffer:
                case SamplerIntBuffer:
                case SamplerUIntBuffer:
                    process.uniforms.put(name, new UniformInt(0));
                    break;
            }

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_UNIFORM)) {
                //!
                //! Support for GLSL_EXPLICIT_UNIFORM extension.
                //!
                output.append(UNIFORM_LAYOUT_BEGIN).append(index).append(UNIFORM_LAYOUT_END).append(" ");
            }

            output.append(UNIFORM_CORE).append(" ");

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_PRECISION)) {
                //!
                //! Support for GLSL_PRECISION extension.
                //!
                output.append(precision.eName).append(" ");
            }
            output.append(uniform.eName).append(" ").append(name).append(";");
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
        public void generate(Process process, Int8Buffer input, StringBuffer output) {
            output.append(read(input));
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
        public void generate(Process process, Int8Buffer input, StringBuffer output) {
            switch (input.read()) {
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

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                output.append("#extension GL_ARB_explicit_attrib_location : require").append("\n");
            }
            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_UNIFORM)) {
                output.append("#extension GL_ARB_explicit_uniform_location : require").append("\n");
            }
            output.append("\n");

            //!
            //! Apply the context precision.
            //!
            final ShaderPrecision precision = ShaderPrecision.values()[input.read()];

            if (process.capabilities.hasExtension(GraphicCapabilities.Extension.GLSL_PRECISION)) {
                output.append("precision ").append(precision.eName).append(" float;\n");
            }
        }
    }

    /**
     * <p>Helper method to write a string into the given {@link Int8Buffer}</p>
     */
    private static void write(Int8Buffer buffer, String data) {
        final byte[] bytes = data.getBytes();

        buffer.write((byte) ((bytes.length >> 0x08) & 0xFF));
        buffer.write((byte) (bytes.length & 0xFF));
        buffer.write(bytes);
    }

    /**
     * <p>Helper method to read a string from the given {@link Int8Buffer}</p>
     */
    private static String read(Int8Buffer buffer) {
        final byte[] bytes
                = new byte[(short) (buffer.read() << 8 | buffer.read() & 0xFF)];
        buffer.read(bytes);

        return new String(bytes);
    }
}