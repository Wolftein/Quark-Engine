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
package org.quark.implementation.desktop.render;

import org.lwjgl.opengl.*;
import org.quark.mathematic.*;
import org.quark.render.Render;
import org.quark.render.RenderCapabilities;
import org.quark.render.RenderState;
import org.quark.render.shader.Attribute;
import org.quark.render.shader.Shader;
import org.quark.render.shader.Stage;
import org.quark.render.shader.Uniform;
import org.quark.render.shader.data.*;
import org.quark.render.storage.*;
import org.quark.render.storage.factory.FactoryStorageIndices;
import org.quark.render.storage.factory.FactoryStorageVertices;
import org.quark.render.texture.*;
import org.quark.render.texture.frame.Frame;
import org.quark.render.texture.frame.FrameAttachment;
import org.quark.system.utility.Manageable;

import java.nio.*;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link Render} using OpenGL 3.0-3.3.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class QKOpenGL_3_0 implements Render {
    /**
     * Hold the capabilities of the renderer.
     */
    private RenderCapabilities mCapabilities;

    /**
     * Hold the context of the renderer.
     */
    private int mTexture[], mStorage[], mShader, mDescriptor, mFrame;

    /**
     * Hold the states of the renderer.
     */
    private final RenderState mStates = new RenderState();

    /**
     * Hold all object(s) that is being removed.
     */
    private final List<Manageable> mManageable = new CopyOnWriteArrayList<>();

    /**
     * <p>Called to initialise the module</p>
     * <p>
     * (INTERNAL)
     */
    public void create() {
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

        final RenderCapabilities.LanguageVersion version;
        if (capabilities.OpenGL33) {
            version = RenderCapabilities.LanguageVersion.GL33;
        } else if (capabilities.OpenGL32) {
            version = RenderCapabilities.LanguageVersion.GL32;
        } else if (capabilities.OpenGL31) {
            version = RenderCapabilities.LanguageVersion.GL31;
        } else if (capabilities.OpenGL30) {
            version = RenderCapabilities.LanguageVersion.GL30;
        } else {
            throw new RuntimeException("OpenL 3.0 is minimum requirement.");
        }

        //!
        //! Calculate all the capabilities and limit(s).
        //!
        mCapabilities = new RenderCapabilities(version,
                RenderCapabilities.ShaderLanguageVersion.GLSL330, extension, limit);

        //!
        //! Allocate the cache storage.
        //!
        mTexture = new int[(int) mCapabilities.getLimit(RenderCapabilities.Limit.TEXTURE_STAGE)];
        mStorage = new int[StorageTarget.values().length];
    }

    /**
     * <p>Called to update the module</p>
     * <p>
     * (INTERNAL)
     */
    public void update() {
        mManageable.forEach(Manageable::delete);
        mManageable.clear();
    }

    /**
     * <p>Called to destroy the module</p>
     * <p>
     * (INTERNAL)
     */
    public void destroy() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose(Manageable manageable) {
        mManageable.add(manageable);
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
            if (states.getCullFace() == RenderState.Cull.NONE) {
                GL11.glDisable(GL11.GL_BLEND);
            } else {
                if (mStates.getCullFace() == RenderState.Cull.NONE) {
                    GL11.glEnable(GL11.GL_CULL_FACE);
                }
                GL11.glCullFace(states.getCullFace().eValue);
            }
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
                GL11.glEnable(GL11.GL_STENCIL_TEST);
            } else {
                GL11.glDisable(GL11.GL_STENCIL_TEST);
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
                    states.getStencilFrontOp().eValue, 1, Integer.MAX_VALUE);
            GL20.glStencilFuncSeparate(GL11.GL_BACK,
                    states.getStencilBackOp().eValue, 1, Integer.MAX_VALUE);

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
    public void colour(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
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
        return mTexture[stage] == texture.getHandle();
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
    public boolean isActive(Frame frame) {
        return mFrame == frame.getHandle();
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
            shader.setHandle(GL20.glCreateProgram());

            //!
            //! NOTE: Update the shader once.
            //!
            onUpdateShader(shader);
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
    public void create(Frame frame) {
        if (frame.getHandle() == Manageable.INVALID_HANDLE) {
            frame.setHandle(GL30.glGenFramebuffers());

            //!
            //! NOTE: Update the frame once.
            //!
            onUpdateFrame(frame);
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
    public void delete(Frame frame) {
        if (frame.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(frame);
        }

        //!
        //! Delete all attachment.
        //!
        frame.getAttachment().forEach((name, target) -> onDeleteFrameTarget(frame, target));

        GL30.glDeleteFramebuffers(frame.setHandle(Manageable.INVALID_HANDLE));
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
            GL15.glBindBuffer(
                    storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = storage.getHandle());
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
    public void acquire(Frame frame) {
        if (!isActive(frame)) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mFrame = frame.getHandle());
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
            switch (texture.getType()) {
                case TEXTURE_2D:
                    final Texture2D texture2D = (Texture2D) texture;

                    onUpdateTextureBorder(texture, texture2D.getBorderX(), texture2D.getBorderY());
                    break;
                case TEXTURE_3D:
                    final Texture3D texture3D = (Texture3D) texture;

                    onUpdateTextureBorder(
                            texture, texture3D.getBorderX(), texture3D.getBorderY(), texture3D.getBorderZ());
                    break;
                case TEXTURE_CUBE:
                    final Texture2DCube texture2DCube = (Texture2DCube) texture;

                    onUpdateTextureBorder(
                            texture, texture2DCube.getBorderX(), texture2DCube.getBorderY(), texture2DCube.getBorderZ());
                    break;
            }

            //!
            //! Check if filter require(s) update.
            //!
            if (texture.hasUpdate(Texture.CONCEPT_FILTER)) {
                onUpdateTextureFilter(texture, texture.getFilter());
            }

            //!
            //! Check if data require(s) update.
            //!
            if (texture.hasUpdate(Texture.CONCEPT_IMAGE)) {
                onUpdateTextureImage(texture, texture.getImage());
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
                        GL15.glBufferData(
                                storage.getTarget().eValue, (ByteBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case SHORT:
                    case UNSIGNED_SHORT:
                    case HALF_FLOAT:
                        GL15.glBufferData(
                                storage.getTarget().eValue, (ShortBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case INT:
                    case UNSIGNED_INT:
                        GL15.glBufferData(
                                storage.getTarget().eValue, (IntBuffer) storage.map(), storage.getMode().eValue);
                        break;
                    case FLOAT:
                        GL15.glBufferData(
                                storage.getTarget().eValue, (FloatBuffer) storage.map(), storage.getMode().eValue);
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

            //!
            //! Bind all attribute(s) of the storage(s)
            //!
            if (descriptor.hasVertices()) {
                descriptor.getVertices().forEach(this::onUpdateDescriptorVertices);
            }

            //!
            //! Bind indices storage.
            //!
            if (descriptor.hasIndices()) {
                onUpdateDescriptorIndices(descriptor.getIndices());
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
            GL11.glBindTexture(texture.getType().eValue, mTexture[stage] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Storage<?> storage) {
        if (isActive(storage)) {
            GL15.glBindBuffer(
                    storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Shader shader) {
        if (isActive(shader)) {
            GL20.glUseProgram(mShader = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(VertexDescriptor descriptor) {
        if (isActive(descriptor)) {
            GL30.glBindVertexArray(mDescriptor = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Frame frame) {
        if (isActive(frame)) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mFrame = Manageable.INVALID_HANDLE);
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
     * <p>Handle update {@link TextureBorder} inside a {@link Texture}</p>
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder xBorder, TextureBorder yBorder) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_WRAP_S, xBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_WRAP_T, yBorder.eValue);
        }
    }

    /**
     * <p>Handle update {@link TextureBorder} inside a {@link Texture}</p>
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder xBorder, TextureBorder yBorder,
            TextureBorder zBorder) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_WRAP_S, xBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_WRAP_T, yBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Z)) {
            GL11.glTexParameteri(texture.getType().eValue, GL12.GL_TEXTURE_WRAP_R, zBorder.eValue);
        }
    }

    /**
     * <p>Handle update {@link TextureFilter} inside a {@link Texture}</p>
     */
    private void onUpdateTextureFilter(Texture texture, TextureFilter filter) {
        //!
        //! Get the first layer to check.
        //!
        final Image.Layer layer = texture.getImage().getLayer().get(0);

        if (layer.hasMipmap()) {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_MIN_FILTER, filter.eMinFilterWithMipmap);
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
            GL11.glTexParameterf(texture.getType().eValue,
                    EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, filter.eAnisotropicLevel);
        } else {
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_MIN_FILTER, filter.eMinFilter);
            GL11.glTexParameteri(texture.getType().eValue, GL11.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
        }
    }

    /**
     * <p>Handle update {@link Image} inside a {@link Texture}</p>
     */
    private void onUpdateTextureImage(Texture texture, Image image) {
        final List<Image.Layer> layers = image.getLayer();


        //!
        //! Iterate over all layer(s) in the image.
        //!
        for (int layer = 0, j = layers.size(); layer < j; ++layer) {
            final Image.Layer imageLayer = layers.get(layer);

            //!
            //! Iterate over all mipmap(s) in the layer.
            //!
            for (int mipmap = 0; mipmap < imageLayer.images.length; ++mipmap) {
                //!
                //! Handle each mip-map
                //!
                final int width = Math.max(1, image.getWidth() >> mipmap);
                final int height = Math.max(1, image.getHeight() >> mipmap);
                final int depth = Math.max(1, image.getDepth() >> mipmap);

                //!
                //! Limit the length of the buffer.
                //!
                if (imageLayer.data != null) {
                    imageLayer.data.limit(imageLayer.data.position() + imageLayer.images[mipmap]);
                }

                //!
                //! Upload the image.
                //!
                switch (texture.getType()) {
                    case TEXTURE_2D:
                        if (image.getFormat().eCompressed) {
                            GL13.glCompressedTexImage2D(texture.getType().eValue,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    imageLayer.data);
                        } else {
                            GL11.glTexImage2D(texture.getType().eValue,
                                    mipmap,
                                    texture.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    image.getFormat().eValue,
                                    texture.getFormat().eType,
                                    imageLayer.data);
                        }
                        break;
                    case TEXTURE_3D:
                        if (image.getFormat().eCompressed) {
                            GL13.glCompressedTexImage3D(texture.getType().eValue,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    depth,
                                    0,
                                    imageLayer.data);
                        } else {
                            GL12.glTexImage3D(texture.getType().eValue,
                                    mipmap,
                                    texture.getFormat().eValue,
                                    width,
                                    height,
                                    depth,
                                    0,
                                    image.getFormat().eValue,
                                    texture.getFormat().eType,
                                    imageLayer.data);
                        }
                        break;
                    case TEXTURE_CUBE:
                        if (image.getFormat().eCompressed) {
                            GL13.glCompressedTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + layer,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    imageLayer.data);
                        } else {
                            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + layer,
                                    mipmap,
                                    texture.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    image.getFormat().eValue,
                                    texture.getFormat().eType,
                                    imageLayer.data);
                        }
                        break;
                }

                //!
                //! Change the position of the buffer.
                //!
                if (imageLayer.data != null) {
                    imageLayer.data.position(imageLayer.data.position() + imageLayer.images[mipmap]);
                }
            }

            //!
            //! Generate mip-map if required (will use hardware feature to generate it).
            //!
            if (imageLayer.mipmap && imageLayer.images.length <= 1) {
                GL30.glGenerateMipmap(texture.getType().eValue);
            }
        }
    }

    /**
     * <p>Handle update {@link Frame}</p>
     */
    private void onUpdateFrame(Frame frame) {
        //!
        //! To create teh frame, it requires to be acquired.
        //!
        frame.acquire();
        {
            frame.getAttachment().forEach((attachment, target) -> onUpdateFrameTarget(frame, attachment, target));
        }
        frame.release();
    }

    /**
     * <p>Handle update {@link Frame.Target} inside a {@link Frame}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.Target target) {
        if (target.isTexture()) {
            onUpdateFrameTarget(frame, attachment, (Frame.TextureTarget) target);
        } else {
            onUpdateFrameTarget(frame, attachment, (Frame.RenderTarget) target);
        }
    }

    /**
     * <p>Handle update {@link Frame.TextureTarget} inside a {@link Frame}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.TextureTarget target) {
        final Texture texture = target.texture;

        //!
        //! Perform - if the texture isn't created, isn't acquired and isn't updated.
        //!
        texture.create();
        texture.acquire();
        texture.update();

        //!
        //! Attach the texture into the frame buffer.
        //!
        switch (texture.getType()) {
            case TEXTURE_2D:
                GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment.eValue,
                        texture.getType().eValue,
                        texture.getHandle(), 0);
                break;
            case TEXTURE_3D:
                throw new UnsupportedOperationException("Frame as Texture3D is not supported yet.");
            case TEXTURE_CUBE:
                throw new UnsupportedOperationException("Frame as Texture2DCube is not supported yet.");
        }
    }

    /**
     * <p>Handle update {@link Frame.RenderTarget} inside a {@link Frame}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.RenderTarget target) {
        //!
        //! Generates the render buffer name.
        //!
        target.setHandle(GL30.glGenRenderbuffers());

        //!
        //! Update the render buffer.
        //!
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, target.getHandle());

        if (frame.getSamples() > 1) {
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, frame.getSamples(), target.format.eValue,
                    frame.getWidth(),
                    frame.getHeight());
        } else {
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, target.format.eValue,
                    frame.getWidth(),
                    frame.getHeight());
        }
    }

    /**
     * <p>Handle delete {@link Frame.Target} from a {@link Frame}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.Target target) {
        if (target.isTexture()) {
            onDeleteFrameTarget(frame, (Frame.TextureTarget) target);
        } else {
            onDeleteFrameTarget(frame, (Frame.RenderTarget) target);
        }
    }

    /**
     * <p>Handle delete {@link Frame.TextureTarget} from a {@link Frame}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.TextureTarget target) {
        //!
        //! Delete the texture
        //!
        target.texture.delete();
    }

    /**
     * <p>Handle delete {@link Frame.RenderTarget} from a {@link Frame}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.RenderTarget target) {
        //!
        //! Delete the render buffer
        //!
        GL30.glDeleteRenderbuffers(target.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * <p>Handle update {@link Shader}</p>
     */
    private void onUpdateShader(Shader shader) {
        final int handle = shader.getHandle();

        //!
        //! Update each stage of the shader.
        //!
        final List<Integer> stages = shader.getStages()
                .stream()
                .map(stage -> onUpdateShaderStage(shader, stage)).collect(Collectors.toList());

        //!
        //! Bind each attribute (if attribute-layout is not supported).
        //!
        if (!mCapabilities.hasExtension(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
            shader.getAttributes().forEach((name, attribute) -> onUpdateShaderAttribute(shader, name, attribute));
        }

        //!
        //! Link the program and check if there was any error.
        //!
        GL20.glLinkProgram(handle);

        if (GL20.glGetProgrami(handle, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            //!
            //! Notify the user why failed to compile.
            //!
            throw new RuntimeException("Error linking program: " + GL20.glGetProgramInfoLog(handle));
        }

        //!
        //! Bind each uniform.
        //!
        shader.getUniforms().forEach((name, uniform) -> onUpdateShaderUniform(shader, name, uniform));

        //!
        //! Dispose all intermediary shader compiled.
        //!
        stages.forEach(GL20::glDeleteShader);
    }

    /**
     * <p>Handle update {@link Stage} inside a {@link Shader}</p>
     */
    private int onUpdateShaderStage(Shader shader, Stage stage) {
        final int id = GL20.glCreateShader(stage.getType().eValue);

        GL20.glShaderSource(id, stage.getSource());
        GL20.glCompileShader(id);
        GL20.glAttachShader(shader.getHandle(), id);

        return id;
    }

    /**
     * <p>Handle update {@link Attribute} inside a {@link Shader}</p>
     */
    private void onUpdateShaderAttribute(Shader shader, String name, Attribute attribute) {
        if (attribute.isInput()) {
            GL20.glBindAttribLocation(shader.getHandle(), attribute.getID(), name);
        } else {
            GL30.glBindFragDataLocation(shader.getHandle(), attribute.getID(), name);
        }
    }

    /**
     * <p>Handle update {@link Uniform} inside a {@link Shader}</p>
     */
    private void onUpdateShaderUniform(Shader shader, String name, Uniform uniform) {
        if (uniform.getHandle() == Manageable.INVALID_HANDLE) {
            uniform.setHandle(GL20.glGetUniformLocation(shader.getHandle(), name));
        }
    }

    /**
     * <p>Handle update {@link FactoryStorageIndices} inside a {@link VertexDescriptor}</p>
     */
    private void onUpdateDescriptorIndices(FactoryStorageIndices<?> indices) {
        indices.create();

        //!
        //! NOTE: This is required since the renderer will not bind it inside VAO if already bind it outside.
        //!
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indices.getHandle());

        indices.update();
    }

    /**
     * <p>Handle update {@link FactoryStorageVertices} inside a {@link VertexDescriptor}</p>
     */
    private void onUpdateDescriptorVertices(FactoryStorageVertices<?> vertices) {
        vertices.create();
        vertices.acquire();
        {
            vertices.getAttributes().forEach(T -> onUpdateDescriptorVertex(vertices, T));
        }
        vertices.update();
        vertices.release();
    }

    /**
     * <p>Handle update {@link Vertex} inside a {@link FactoryStorageVertices}</p>
     */
    private void onUpdateDescriptorVertex(FactoryStorageVertices<?> vertices, Vertex vertex) {
        GL20.glEnableVertexAttribArray(vertex.getID());
        GL20.glVertexAttribPointer(
                vertex.getID(),
                vertex.getComponent(),
                vertex.getType().eValue,
                vertex.isNormalised(), vertices.getAttributesLength(),
                vertex.getOffset());
    }
}
