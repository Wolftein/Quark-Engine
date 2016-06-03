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
package org.quark_engine.backend.lwjgl.render;

import org.lwjgl.opengl.*;
import org.quark_engine.mathematic.*;
import org.quark_engine.render.Render;
import org.quark_engine.render.RenderCapabilities;
import org.quark_engine.render.RenderState;
import org.quark_engine.render.shader.Shader;
import org.quark_engine.render.shader.Uniform;
import org.quark_engine.render.shader.data.*;
import org.quark_engine.render.storage.*;
import org.quark_engine.render.storage.factory.FactoryStorageIndices;
import org.quark_engine.render.texture.*;
import org.quark_engine.system.utility.Manageable;

import java.nio.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link Render}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class LWJGLRender implements Render {
    /**
     * Hold the capabilities of the renderer.
     */
    private RenderCapabilities mCapabilities;

    /**
     * Hold the states of the renderer.
     */
    private final RenderState mStates = new RenderState();

    /**
     * Hold the context of the renderer.
     */
    private int mTexture[], mStorage[], mShader, mDescriptor;

    /**
     * Hold all object(s) that is being removed.
     */
    private final List<Manageable> mManageable = new CopyOnWriteArrayList<>();

    /**
     * <p>Handle when the implementation requires to initialise</p>
     */
    public void initialise() {
        //!
        //! Get the capabilities of the context
        //!
        final org.lwjgl.opengl.GLCapabilities capabilities = org.lwjgl.opengl.GL.getCapabilities();

        final EnumMap<RenderCapabilities.Extension, Boolean> extension = new EnumMap<>(RenderCapabilities.Extension.class);
        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE,
                capabilities.GL_ARB_explicit_attrib_location);
        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM,
                capabilities.GL_ARB_explicit_uniform_location);
        extension.put(RenderCapabilities.Extension.GLSL_GEOMETRY,
                capabilities.GL_ARB_geometry_shader4);

        final EnumMap<RenderCapabilities.Limit, Float> limit = new EnumMap<>(RenderCapabilities.Limit.class);
        limit.put(RenderCapabilities.Limit.TEXTURE_STAGE, GL11.glGetFloat(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));

        //!
        //! Calculate all the capabilities and limit(s).
        //!
        final RenderCapabilities.LanguageVersion version = RenderCapabilities.LanguageVersion.fromStringVersion(
                GL11.glGetString(GL11.GL_VERSION));
        final RenderCapabilities.ShaderLanguageVersion shaderVersion = RenderCapabilities.ShaderLanguageVersion.fromStringVersion(
                GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
        mCapabilities = new RenderCapabilities(version, shaderVersion, extension, limit);

        //!
        //! Allocate the cache storage.
        //!
        mTexture = new int[(int) mCapabilities.getLimit(RenderCapabilities.Limit.TEXTURE_STAGE)];
        mStorage = new int[StorageTarget.values().length];
    }

    /**
     * <p>Handle when the implementation requires to update</p>
     */
    public void update() {
        //!
        //! Dispose all object(s) being mark for dispose.
        //!
        mManageable.forEach(Manageable::release);
        mManageable.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderCapabilities getCapabilities() {
        return mCapabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RenderState states) {
        //!
        //! Update STATE_BLEND
        //!
        if (states.getBlend() != mStates.getBlend()) {
            if (states.getBlend() == RenderState.Blend.NONE) {
                GL11.glDisable(GL11.GL_BLEND);
            } else {
                if (mStates.getBlend() == RenderState.Blend.NONE) {
                    GL11.glEnable(GL11.GL_BLEND);
                }
                GL11.glBlendFunc(states.getBlend().eSource, states.getBlend().eDestination);
            }
            mStates.setBlend(states.getBlend());
        }

        //!
        //! Update STATE_BLEND_EQUATION
        //!
        if (states.getBlendEquationColour() != mStates.getBlendEquationColour()
                || states.getBlendEquationAlpha() != mStates.getBlendEquationAlpha()) {
            GL20.glBlendEquationSeparate(
                    states.getBlendEquationColour().eValue,
                    states.getBlendEquationAlpha().eValue);
            mStates.setBlendEquation(
                    states.getBlendEquationColour(),
                    states.getBlendEquationAlpha());
        }

        //!
        //! Update CULL_FACE
        //!
        if (states.getCullFace() != mStates.getCullFace()) {
            GL11.glCullFace(states.getCullFace().eValue);
            mStates.setCullFace(states.getCullFace());
        }

        //!
        //! Update DEPTH
        //!
        if (states.isDepth() != mStates.isDepth()) {
            if (states.isDepth()) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            } else {
                GL11.glDisable(GL11.GL_DEPTH_TEST);
            }
            mStates.setDepth(states.isDepth());
        }

        //!
        //! Update DEPTH_MASK
        //!
        if (states.isDepthMask() != mStates.isDepthMask()) {
            GL11.glDepthMask(states.isDepthMask());
            mStates.setDepth(states.isDepthMask());
        }

        //!
        //! Update COLOR_MASK
        //!
        if (states.isRedMask() != mStates.isRedMask()
                || states.isGreenMask() != mStates.isGreenMask()
                || states.isBlueMask() != mStates.isBlueMask()
                || states.isAlphaMask() != mStates.isAlphaMask()) {
            GL11.glColorMask(
                    states.isRedMask(),
                    states.isGreenMask(),
                    states.isBlueMask(),
                    states.isAlphaMask());
            mStates.setColourMask(
                    states.isRedMask(),
                    states.isGreenMask(),
                    states.isBlueMask(),
                    states.isAlphaMask());
        }

        //!
        //! Update SCISSOR
        //!
        if (states.isScissor() != mStates.isScissor()) {
            if (states.isScissor()) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
            } else {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
            mStates.setScissor(states.isScissor());
        }
        if (states.getScissorViewport() != mStates.getScissorViewport()) {
            final Vector4i viewport = states.getScissorViewport();
            GL11.glScissor(
                    viewport.getX(),
                    viewport.getY(),
                    viewport.getZ(),
                    viewport.getW());
            mStates.setScissorViewport(
                    viewport.getX(),
                    viewport.getY(),
                    viewport.getZ(),
                    viewport.getW());
        }

        //!
        //! Update STENCIL
        //!
        if (states.isStencil() != mStates.isStencil()) {
            if (states.isStencil()) {
                GL11.glEnable(GL11.GL_STENCIL);
            } else {
                GL11.glDisable(GL11.GL_STENCIL);
            }
            mStates.setStencil(states.isStencil());
        }

        //!
        //! Update STENCIL_OP
        //!
        if (states.getStencilFrontOp() != mStates.getStencilFrontOp()
                || states.getStencilFrontFailOp() != mStates.getStencilFrontFailOp()
                || states.getStencilFrontDepthFailOp() != mStates.getStencilFrontDepthFailOp()
                || states.getStencilFrontDepthPassOp() != mStates.getStencilFrontDepthPassOp()
                || states.getStencilBackOp() != mStates.getStencilBackOp()
                || states.getStencilBackFailOp() != mStates.getStencilBackFailOp()
                || states.getStencilBackDepthFailOp() != mStates.getStencilBackDepthFailOp()
                || states.getStencilBackDepthPassOp() != mStates.getStencilBackDepthPassOp()) {
            GL20.glStencilOpSeparate(GL11.GL_FRONT,
                    states.getStencilFrontFailOp().eValue,
                    states.getStencilFrontDepthFailOp().eValue,
                    states.getStencilFrontDepthPassOp().eValue);

            GL20.glStencilOpSeparate(GL11.GL_BACK,
                    states.getStencilBackFailOp().eValue,
                    states.getStencilBackDepthFailOp().eValue,
                    states.getStencilBackDepthPassOp().eValue);

            GL20.glStencilFuncSeparate(GL11.GL_FRONT,
                    states.getStencilFrontOp().eValue, 0, Integer.MAX_VALUE);
            GL20.glStencilFuncSeparate(GL11.GL_BACK,
                    states.getStencilBackOp().eValue, 0, Integer.MAX_VALUE);

            mStates.setStencilOp(states.getStencilFrontOp(), states.getStencilBackOp());
            mStates.setStencilFrontOp(states.getStencilFrontFailOp(),
                    states.getStencilFrontDepthFailOp(),
                    states.getStencilFrontDepthPassOp());

            mStates.setStencilBackOp(states.getStencilBackFailOp(),
                    states.getStencilBackDepthFailOp(),
                    states.getStencilBackDepthPassOp());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(boolean colour, boolean depth, boolean stencil) {
        int value = colour ? GL11.GL_COLOR_BUFFER_BIT : 0;
        if (depth) {
            value |= GL11.GL_DEPTH_BUFFER_BIT;
        }
        if (stencil) {
            value |= GL11.GL_STENCIL_BUFFER_BIT;
        }
        GL11.glClear(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void viewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(Texture texture) {
        return isActive(texture, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(Texture texture, int stage) {
        if (stage > mTexture.length) {
            throw new IllegalStateException("Maximum possible texture stage is " + mTexture.length);
        }
        return mTexture[stage] == (texture != null ? texture.getHandle() : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(Storage<?> storage) {
        return mStorage[storage.getTarget().ordinal()] == storage.getHandle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(Shader shader) {
        return mShader == shader.getHandle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(VertexDescriptor descriptor) {
        return mDescriptor == descriptor.getHandle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Texture texture) {
        if (texture.getHandle() == Manageable.INVALID_HANDLE) {
            texture.setHandle(GL11.glGenTextures());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Storage<?> storage) {
        if (storage.getHandle() == Manageable.INVALID_HANDLE) {
            storage.setHandle(GL15.glGenBuffers());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Shader shader) {
        if (shader.getHandle() == Manageable.INVALID_HANDLE) {
            int handle;
            shader.setHandle(handle = GL20.glCreateProgram());

            //!
            //! Create each stage of the shader.
            //!
            final List<Integer> stages = new ArrayList<>(shader.getStages().size());
            shader.getStages().forEach(T -> {
                final int id = GL20.glCreateShader(T.getType().eValue);

                GL20.glShaderSource(id, T.getSource());
                GL20.glCompileShader(id);
                GL20.glAttachShader(handle, id);

                stages.add(id);
            });

            //!
            //! Bind each attribute (if attribute-layout is not supported).
            //!
            if (!mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
                shader.getAttributes().forEach((Name, Attribute) -> {
                    if (Attribute.isInput()) {
                        GL20.glBindAttribLocation(handle, Attribute.getID(), Name);
                    } else {
                        GL30.glBindFragDataLocation(handle, Attribute.getID(), Name);
                    }
                });
            }

            //!
            //! Link the program and check if there was any error
            //!
            GL20.glLinkProgram(handle);
            if (GL20.glGetProgrami(handle, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
                //!
                //! Notify the user why failed to compile
                //!
                throw new RuntimeException("Error linking program: " + GL20.glGetProgramInfoLog(handle));
            } else {
                GL20.glValidateProgram(handle);

                if (GL20.glGetProgrami(handle, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
                    //!
                    //! Notify the user why failed to compile
                    //!
                    throw new RuntimeException("Error validating program: " + GL20.glGetProgramInfoLog(handle));
                }
            }

            //!
            //! Bind each uniform
            //!
            shader.getUniforms().forEach((Name, Uniform) -> Uniform.setHandle(GL20.glGetUniformLocation(handle, Name)));

            //!
            //! Dispose all intermediary shader compiled
            //!
            stages.forEach(GL20::glDeleteShader);
            shader.setUpdated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(VertexDescriptor descriptor) {
        if (descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            descriptor.setHandle(GL30.glGenVertexArrays());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Texture texture) {
        if (texture.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        }
        for (int i = 0; i < mTexture.length; ++i) {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(texture, i);
        }
        GL11.glDeleteTextures(texture.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Storage<?> storage) {
        if (storage.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(storage);
        }
        GL15.glDeleteBuffers(storage.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Shader shader) {
        if (shader.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(shader);
        }
        GL20.glDeleteProgram(shader.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(VertexDescriptor descriptor) {
        if (descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(descriptor);
        }
        GL30.glDeleteVertexArrays(descriptor.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Texture texture) {
        acquire(texture, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Texture texture, int stage) {
        if (stage > mTexture.length) {
            throw new IllegalStateException("Maximum possible texture stage is " + mTexture.length);
        }
        if (!isActive(texture, stage)) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + stage);
            GL11.glBindTexture(texture.getType().eValue, mTexture[stage] = texture.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Storage<?> storage) {
        if (!isActive(storage)) {
            GL15.glBindBuffer(storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = storage.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Shader shader) {
        if (!isActive(shader)) {
            GL20.glUseProgram(mShader = shader.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(VertexDescriptor descriptor) {
        if (!isActive(descriptor)) {
            GL30.glBindVertexArray(mDescriptor = descriptor.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Texture texture) {
        if (texture.hasUpdate()) {
            //!
            //! Check if wrap mode(s) require(s) update.
            //!
            switch (texture.getType().eValue) {
                case GL11.GL_TEXTURE_1D:
                    final Texture1D texture1D = (Texture1D) texture;
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
                        GL11.glTexParameteri(
                                GL11.GL_TEXTURE_1D,
                                GL11.GL_TEXTURE_WRAP_T, texture1D.getBorderX().eValue);
                    }
                    break;
                case GL11.GL_TEXTURE_2D:
                    final Texture2D texture2D = (Texture2D) texture;
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
                        GL11.glTexParameteri(
                                GL11.GL_TEXTURE_2D,
                                GL11.GL_TEXTURE_WRAP_T, texture2D.getBorderX().eValue);
                    }
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
                        GL11.glTexParameteri(
                                GL11.GL_TEXTURE_2D,
                                GL11.GL_TEXTURE_WRAP_T, texture2D.getBorderY().eValue);
                    }
                    break;
                case GL12.GL_TEXTURE_3D:
                    final Texture3D texture3D = (Texture3D) texture;
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
                        GL11.glTexParameteri(
                                GL12.GL_TEXTURE_3D,
                                GL11.GL_TEXTURE_WRAP_T, texture3D.getBorderX().eValue);
                    }
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
                        GL11.glTexParameteri(
                                GL12.GL_TEXTURE_3D,
                                GL11.GL_TEXTURE_WRAP_T, texture3D.getBorderY().eValue);
                    }
                    if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Z)) {
                        GL11.glTexParameteri(
                                GL12.GL_TEXTURE_3D,
                                GL11.GL_TEXTURE_WRAP_T, texture3D.getBorderZ().eValue);
                    }
                    break;
            }

            //!
            //! Check if filter require(s) update.
            //!
            if (texture.hasUpdate(Texture.CONCEPT_FILTER)) {
                final TextureFilter filter = texture.getFilter();

                if (texture.getImages().size() > 1) {
                    GL11.glTexParameteri(
                            texture.getType().eValue,
                            GL11.GL_TEXTURE_MIN_FILTER, filter.eMinFilterWithMipmap);
                    GL11.glTexParameteri(
                            texture.getType().eValue,
                            GL11.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
                    GL11.glTexParameterf(
                            texture.getType().eValue,
                            EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, filter.eAnisotropicLevel);
                } else {
                    GL11.glTexParameteri(
                            texture.getType().eValue,
                            GL11.GL_TEXTURE_MIN_FILTER, filter.eMinFilter);
                    GL11.glTexParameteri(
                            texture.getType().eValue,
                            GL11.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
                }
            }

            //!
            //! Check if data require(s) update.
            //!
            if (texture.hasUpdate(Texture.CONCEPT_IMAGE)) {
                GL11.glTexParameteri(texture.getType().eValue, GL12.GL_TEXTURE_BASE_LEVEL, 0);
                GL11.glTexParameteri(texture.getType().eValue, GL12.GL_TEXTURE_MAX_LEVEL, texture.getImages().size());

                switch (texture.getType()) {
                    case TEXTURE_1D:
                    case TEXTURE_2D:
                        for (final Image image : texture.getImages()) {
                            if (image.getFormat().eCompressed) {
                                GL13.glCompressedTexImage2D(texture.getType().eValue,
                                        image.getLevel(),
                                        image.getFormat().eValue,
                                        image.getWidth(),
                                        image.getHeight(),
                                        0,
                                        image.getBytes());
                            } else {
                                GL11.glTexImage2D(texture.getType().eValue,
                                        image.getLevel(),
                                        texture.getFormat().eValue,
                                        image.getWidth(),
                                        image.getHeight(),
                                        0,
                                        image.getFormat().eValue,
                                        image.getBytesFormat().eValue,
                                        image.getBytes());
                            }
                            image.getBytes().clear();
                        }
                        break;
                    case TEXTURE_3D:
                        for (final Image image : texture.getImages()) {
                            if (image.getFormat().eCompressed) {
                                GL13.glCompressedTexImage3D(texture.getType().eValue,
                                        image.getLevel(),
                                        image.getFormat().eValue,
                                        image.getWidth(),
                                        image.getHeight(),
                                        image.getDepth(),
                                        0,
                                        image.getBytes());
                            } else {
                                GL12.glTexImage3D(texture.getType().eValue,
                                        image.getLevel(),
                                        texture.getFormat().eValue,
                                        image.getWidth(),
                                        image.getHeight(),
                                        image.getDepth(),
                                        0,
                                        image.getFormat().eValue,
                                        image.getBytesFormat().eValue,
                                        image.getBytes());
                            }
                            image.getBytes().clear();
                        }
                        break;
                }
            }
            texture.setUpdated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Storage<?> storage) {
        if (storage.hasUpdate(Storage.CONCEPT_DATA)) {
            //!
            //! Client-side storage.
            //!
            if (storage.getType() == StorageType.CLIENT) {
                switch (storage.getFormat()) {
                    case BYTE:
                    case UNSIGNED_BYTE:
                        GL15.glBufferData(storage.getTarget().eValue, (ByteBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case SHORT:
                    case UNSIGNED_SHORT:
                    case HALF_FLOAT:
                        GL15.glBufferData(storage.getTarget().eValue, (ShortBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case INT:
                    case UNSIGNED_INT:
                        GL15.glBufferData(storage.getTarget().eValue, (IntBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case FLOAT:
                        GL15.glBufferData(storage.getTarget().eValue, (FloatBuffer) storage.map(), storage.getMode().eValue);
                        break;
                }
            } else {
                //!
                //! Server-side storage.
                //!
                GL15.glBufferData(storage.getTarget().eValue, storage.getCapacity(), storage.getMode().eValue);
            }
            storage.setUpdated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Uniform uniform) {
        if (uniform.hasUpdate()) {
            switch (uniform.getType()) {
                case Float: {
                    final Float value = ((UniformFloat) uniform).getValue();
                    GL20.glUniform1f(uniform.getHandle(), value);
                }
                break;
                case Float2: {
                    final Vector2f value = ((UniformFloat2) uniform).getValue();
                    GL20.glUniform2f(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case Float3: {
                    final Vector3f value = ((UniformFloat3) uniform).getValue();
                    GL20.glUniform3f(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case Float4: {
                    final Vector4f value = ((UniformFloat4) uniform).getValue();
                    GL20.glUniform4f(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case FloatArray: {
                    final FloatBuffer value = ((UniformFloatArray) uniform).getValue();
                    GL20.glUniform1fv(uniform.getHandle(), value);
                }
                break;
                case Int: {
                    final int value = ((UniformInt) uniform).getValue();
                    GL20.glUniform1i(uniform.getHandle(), value);
                }
                break;
                case Int2: {
                    final Vector2i value = ((UniformInt2) uniform).getValue();
                    GL20.glUniform2i(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case Int3: {
                    final Vector3i value = ((UniformInt3) uniform).getValue();
                    GL20.glUniform3i(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case Int4: {
                    final Vector4i value = ((UniformInt4) uniform).getValue();
                    GL20.glUniform4i(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case IntArray: {
                    final IntBuffer value = ((UniformIntArray) uniform).getValue();
                    GL20.glUniform1iv(uniform.getHandle(), value);
                }
                break;
                case Matrix3x3: {
                    final FloatBuffer value = ((UniformMatrix3) uniform).getValue();
                    GL20.glUniformMatrix3fv(uniform.getHandle(), false, value);
                }
                break;
                case Matrix4x4: {
                    final FloatBuffer value = ((UniformMatrix4) uniform).getValue();
                    GL20.glUniformMatrix4fv(uniform.getHandle(), false, value);
                }
                break;
                case UInt: {
                    final int value = ((UniformInt) uniform).getValue();
                    GL30.glUniform1ui(uniform.getHandle(), value);
                }
                break;
                case UInt2: {
                    final Vector2i value = ((UniformInt2) uniform).getValue();
                    GL30.glUniform2ui(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case UInt3: {
                    final Vector3i value = ((UniformInt3) uniform).getValue();
                    GL30.glUniform3ui(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case UInt4: {
                    final Vector4i value = ((UniformInt4) uniform).getValue();
                    GL30.glUniform4ui(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case UIntArray: {
                    final IntBuffer value = ((UniformIntArray) uniform).getValue();
                    GL30.glUniform1uiv(uniform.getHandle(), value);
                }
                break;
            }
            uniform.setUpdated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(VertexDescriptor descriptor) {
        if (descriptor.hasUpdate()) {
            if (descriptor.hasVertices()) {
                descriptor.getVertices().forEach(vertices ->
                {
                    //!
                    //! Allocate and acquire the storage (which will remain in the descriptor).
                    //!
                    vertices.create();

                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertices.getHandle());

                    vertices.update();

                    //!
                    //! Allocate each attribute.
                    //!
                    vertices.getAttributes().forEach(attribute ->
                    {
                        GL20.glEnableVertexAttribArray(
                                attribute.getID());
                        GL20.glVertexAttribPointer(
                                attribute.getID(),
                                attribute.getComponent(),
                                attribute.getType().eValue,
                                attribute.isNormalised(), vertices.getAttributesLength(),
                                attribute.getOffset());
                    });
                });
            }

            if (descriptor.hasIndices()) {
                //!
                //! Allocate and acquire the storage (which will remain in the descriptor).
                //!
                final FactoryStorageIndices<?> indices = descriptor.getIndices();
                indices.create();

                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indices.getHandle());

                indices.update();
            }
            descriptor.setUpdated();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Texture texture) {
        release(texture, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Texture texture, int stage) {
        if (stage > mTexture.length) {
            throw new IllegalStateException("Maximum possible texture stage is " + mTexture.length);
        }
        if (isActive(texture, stage)) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + stage);
            GL11.glBindTexture(texture.getType().eValue, mTexture[stage] = 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Storage<?> storage) {
        if (isActive(storage)) {
            GL15.glBindBuffer(storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Shader shader) {
        if (isActive(shader)) {
            GL20.glUseProgram(mShader = 0);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(VertexDescriptor descriptor) {
        if (isActive(descriptor)) {
            GL30.glBindVertexArray(mDescriptor = 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Primitive primitive, long offset, long count) {
        GL11.glDrawArrays(primitive.eValue, (int) offset, (int) count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Primitive primitive, long offset, long count, VertexFormat format) {
        GL11.glDrawElements(primitive.eValue, (int) count, format.eValue, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer> T map(Storage<T> storage) {
        return map(storage, 0, 0L, storage.getCapacity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer> T map(Storage<T> storage, int access) {
        return map(storage, access, 0L, storage.getCapacity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer> T map(Storage<T> storage, long offset, long length) {
        return map(storage, 0, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer> T map(Storage<T> storage, int access, long offset, long length) {
        final int flag = (storage.getMode().eReadable ? GL30.GL_MAP_READ_BIT : GL30.GL_MAP_WRITE_BIT) | access;
        return Storage.cast(
                GL30.glMapBufferRange(storage.getTarget().eValue, offset, length, flag), storage.getFormat());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer> void unmap(Storage<T> storage) {
        GL15.glUnmapBuffer(storage.getTarget().eValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose(Manageable manageable) {
        mManageable.add(manageable);
    }
}
