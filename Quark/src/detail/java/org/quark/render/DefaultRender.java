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

import org.quark.mathematic.*;
import org.quark.render.shader.Attribute;
import org.quark.render.shader.Shader;
import org.quark.render.shader.Stage;
import org.quark.render.shader.Uniform;
import org.quark.render.shader.data.*;
import org.quark.render.storage.*;
import org.quark.render.storage.factory.FactoryArrayStorage;
import org.quark.render.storage.factory.FactoryElementStorage;
import org.quark.render.texture.*;
import org.quark.render.texture.frame.Frame;
import org.quark.render.texture.frame.FrameAttachment;
import org.quark.system.utility.Manageable;
import org.quark.system.utility.array.Array;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.Int32Array;
import org.quark.system.utility.array.UInt32Array;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Default implementation for {@link Render}.
 */
public final class DefaultRender implements Render {
    /**
     * Hold {@link GLES32} context.
     */
    private GLES32 mGL;

    /**
     * Hold {@link RenderCapabilities} context.
     */
    private RenderCapabilities mCapabilities;

    /**
     * Hold the states of the renderer.
     */
    private final RenderState mStates = new RenderState()
            .setAlphaToCoverage(RenderState.Flag.DISABLE)
            .setBlend(RenderState.Blend.NONE)
            .setColourMask(RenderState.Flag.ENABLE)
            .setCullFace(RenderState.Cull.BACK)
            .setScissor(RenderState.Flag.DISABLE)
            .setDepth(RenderState.Flag.ENABLE)
            .setDepthMask(RenderState.Flag.ENABLE)
            .setStencil(RenderState.Flag.DISABLE);

    /**
     * Hold all object(s) acquired (cache).
     */
    private int mTexture[], mStorage[], mShader, mDescriptor, mFrame;

    /**
     * Hold all object(s) that is being removed.
     */
    private final Queue<Manageable> mManageable = new LinkedList<>();

    /**
     * <p>Handle when the module initialise</p>
     *
     * @param gl the render implementation
     */
    public void onModuleCreate(GLES32 gl) {
        this.mGL = gl;

        //!
        //! Get the capabilities from the context.
        //!
        mCapabilities = mGL.glCapabilities();

        mTexture = new int[mCapabilities.getIntLimit(RenderCapabilities.Limit.TEXTURE_STAGE)];
        mStorage = new int[StorageTarget.values().length];
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
        onModuleUpdate();
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
        while (mManageable.size() > 0) {
            //!
            //! Manage the delete all component(s) on the correct thread.
            //!
            mManageable.poll().delete();
        }
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
        //! ALPHA_TO_COVERAGE
        //!
        if (states.getAlphaToCoverage() != mStates.getAlphaToCoverage()) {
            onUpdateState(states.getAlphaToCoverage(), GLES2.GL_SAMPLE_ALPHA_TO_COVERAGE);
        }

        //!
        //! BLEND
        //!
        final boolean isBlend = RenderState.isFlagEnabled(states.getBlend(), mStates.getBlend());

        if (RenderState.isFlagDirty(states.getBlend(), mStates.getBlend())
                && onUpdateState(states.getBlend(), mStates.getBlend(), RenderState.Blend.NONE, GLES2.GL_BLEND)) {
            mGL.glBlendFunc(states.getBlend().eSource, states.getBlend().eDestination);
        }

        //!
        //! BLEND_EQUATION
        //!
        if (isBlend && states.getBlendEquationColour() != mStates.getBlendEquationColour()
                || states.getBlendEquationAlpha() != mStates.getBlendEquationAlpha()) {
            mGL.glBlendEquationSeparate(states.getBlendEquationColour().eValue, states.getBlendEquationAlpha().eValue);
        }

        //!
        //! CULL
        //!
        if (RenderState.isFlagDirty(states.getCullFace(), mStates.getCullFace())
                && onUpdateState(states.getCullFace(), mStates.getCullFace(), RenderState.Cull.NONE, GLES2.GL_CULL_FACE)) {
            mGL.glBlendFunc(states.getBlend().eSource, states.getBlend().eDestination);
        }

        //!
        //! DEPTH
        //!
        final boolean isDepth = RenderState.isFlagEnabled(states.getDepth(), mStates.getDepth());

        if (states.getDepth() != mStates.getDepth()) {
            onUpdateState(states.getDepth(), GLES2.GL_DEPTH_TEST);
        }

        //!
        //! DEPTH_MASK
        //!
        if (isDepth && RenderState.isFlagDirty(states.getDepthMask(), mStates.getDepthMask())) {
            mGL.glDepthMask(states.getDepthMask() == RenderState.Flag.ENABLE);
        }

        //!
        //! DEPTH_OP
        //!
        if (isDepth && states.getDepthOp() != mStates.getDepthOp()) {
            mGL.glDepthFunc(states.getDepthOp().eValue);
        }

        //!
        //! DEPTH_RANGE
        //!
        if (isDepth && !states.getDepthRange().equals(mStates.getDepthRange())) {
            final Vector2f range = states.getDepthRange();

            mGL.glDepthRange(range.getX(), range.getY());
        }

        //!
        //! COLOR_MASK
        //!
        if (RenderState.isFlagDirty(states.getRedMask(), mStates.getRedMask())
                || RenderState.isFlagDirty(states.getGreenMask(), mStates.getGreenMask())
                || RenderState.isFlagDirty(states.getBlueMask(), mStates.getBlueMask())
                || RenderState.isFlagDirty(states.getAlphaMask(), mStates.getAlphaMask())) {

            final boolean red
                    = RenderState.isFlagEnabled(states.getRedMask(), mStates.getRedMask());
            final boolean green
                    = RenderState.isFlagEnabled(states.getRedMask(), mStates.getRedMask());
            final boolean blue
                    = RenderState.isFlagEnabled(states.getRedMask(), mStates.getRedMask());
            final boolean alpha
                    = RenderState.isFlagEnabled(states.getRedMask(), mStates.getRedMask());

            mGL.glColorMask(red, green, blue, alpha);
        }

        //!
        //! SCISSOR
        //!
        final boolean isScissor = RenderState.isFlagEnabled(states.getScissor(), mStates.getScissor());

        if (RenderState.isFlagDirty(states.getScissor(), mStates.getScissor())) {
            onUpdateState(states.getScissor(), GLES2.GL_SCISSOR_TEST);
        }

        //!
        //! SCISSOR_VIEWPORT
        //!
        if (isScissor && !states.getScissorViewport().equals(mStates.getScissorViewport())) {
            final Vector4i viewport = states.getScissorViewport();

            mGL.glScissor(viewport.getX(), viewport.getY(), viewport.getZ(), viewport.getW());
        }

        //!
        //! STENCIL
        //!
        final boolean isStencil = RenderState.isFlagEnabled(states.getStencil(), mStates.getStencil());

        if (RenderState.isFlagDirty(states.getStencil(), mStates.getStencil())) {
            onUpdateState(states.getStencil(), GLES2.GL_STENCIL_TEST);
        }

        //!
        //! STENCIL_OP
        //!
        if (isStencil && states.getStencilFrontOp() != mStates.getStencilFrontOp()
                || states.getStencilFrontFailOp() != mStates.getStencilFrontFailOp()
                || states.getStencilFrontDepthFailOp() != mStates.getStencilFrontDepthFailOp()
                || states.getStencilFrontDepthPassOp() != mStates.getStencilFrontDepthPassOp()
                || states.getStencilBackOp() != mStates.getStencilBackOp()
                || states.getStencilBackFailOp() != mStates.getStencilBackFailOp()
                || states.getStencilBackDepthFailOp() != mStates.getStencilBackDepthFailOp()
                || states.getStencilBackDepthPassOp() != mStates.getStencilBackDepthPassOp()) {

            mGL.glStencilOpSeparate(GLES2.GL_FRONT,
                    states.getStencilFrontFailOp().eValue,
                    states.getStencilFrontDepthFailOp().eValue,
                    states.getStencilFrontDepthPassOp().eValue);

            mGL.glStencilOpSeparate(GLES2.GL_BACK,
                    states.getStencilBackFailOp().eValue,
                    states.getStencilBackDepthFailOp().eValue,
                    states.getStencilBackDepthPassOp().eValue);

            mGL.glStencilFuncSeparate(GLES2.GL_FRONT,
                    states.getStencilFrontOp().eValue, 1, Integer.MAX_VALUE);
            mGL.glStencilFuncSeparate(GLES2.GL_BACK,
                    states.getStencilBackOp().eValue, 1, Integer.MAX_VALUE);
        }
        mStates.merge(states);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void colour(float red, float green, float blue, float alpha) {
        mGL.glClearColor(red, green, blue, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(boolean colour, boolean depth, boolean stencil) {
        int value = colour ? GLES2.GL_COLOR_BUFFER_BIT : 0;
        if (depth) {
            value |= GLES2.GL_DEPTH_BUFFER_BIT;
        }
        if (stencil) {
            value |= GLES2.GL_STENCIL_BUFFER_BIT;
        }
        mGL.glClear(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void viewport(int x, int y, int width, int height) {
        mGL.glViewport(x, y, width, height);
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
            throw new IllegalStateException("Maximum texture stage is " + mTexture.length);
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
            //!
            //! Prevent leaking the component if it was created.
            //!
            texture.setHandle(mGL.glGenTextures());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Storage<?> storage) {
        if (storage.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Prevent leaking the component if it was created.
            //!
            storage.setHandle(mGL.glGenBuffers());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Shader shader) {
        if (shader.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Prevent leaking the component if it was created.
            //!
            //! NOTE: Update the component once.
            //!
            shader.setHandle(mGL.glCreateProgram());

            onUpdateShader(shader);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(VertexDescriptor descriptor) {
        if (mCapabilities.hasExtension(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT)
                && descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Prevent leaking the component if it was created.
            //!
            descriptor.setHandle(mGL.glGenVertexArrays());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Frame frame) {
        if (mCapabilities.hasExtension(RenderCapabilities.Extension.FRAME_BUFFER)
                && frame.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Prevent leaking the component if it was created.
            //!
            //! NOTE: Update the component once.
            //!
            frame.setHandle(mGL.glGenFramebuffers());

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
        mGL.glDeleteTextures(texture.setHandle(Manageable.INVALID_HANDLE));
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
        mGL.glDeleteBuffers(storage.setHandle(Manageable.INVALID_HANDLE));
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
        mGL.glDeleteProgram(shader.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(VertexDescriptor descriptor) {
        if (!mCapabilities.hasExtension(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT)
                || descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(descriptor);
        }
        mGL.glDeleteVertexArrays(descriptor.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Frame frame) {
        if (!mCapabilities.hasExtension(RenderCapabilities.Extension.FRAME_BUFFER)
                || frame.getHandle() == Manageable.INVALID_HANDLE) {
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

        mGL.glDeleteFramebuffers(frame.setHandle(Manageable.INVALID_HANDLE));
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
            throw new IllegalStateException("Maximum texture stage is " + mTexture.length);
        }
        if (!isActive(texture, stage)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mGL.glActiveTexture(GLES2.GL_TEXTURE0 + stage);
            mGL.glBindTexture(texture.getType().eValue, mTexture[stage] = texture.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Storage<?> storage) {
        if (!isActive(storage)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mGL.glBindBuffer(storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = storage.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Shader shader) {
        if (!isActive(shader)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mGL.glUseProgram(mShader = shader.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(VertexDescriptor descriptor) {
        if (!isActive(descriptor) && mCapabilities.hasExtension(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mGL.glBindVertexArray(mDescriptor = descriptor.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Frame frame) {
        if (mCapabilities.hasExtension(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT) && !isActive(frame)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mGL.glBindFramebuffer(GLES2.GL_FRAMEBUFFER, mFrame = frame.getHandle());
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

                    onUpdateTextureBorder(texture,
                            texture2D.getBorderX(),
                            texture2D.getBorderY());
                    break;
                case TEXTURE_3D:
                    final Texture3D texture3D = (Texture3D) texture;

                    onUpdateTextureBorder(texture,
                            texture3D.getBorderX(),
                            texture3D.getBorderY(),
                            texture3D.getBorderZ());
                    break;
                case TEXTURE_CUBE:
                    final Texture2DCube texture2DCube = (Texture2DCube) texture;

                    onUpdateTextureBorder(texture,
                            texture2DCube.getBorderX(),
                            texture2DCube.getBorderY(),
                            texture2DCube.getBorderZ());
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
                mGL.glBufferData(storage.getTarget().eValue, storage.map(), storage.getMode().eValue);
            } else {
                //!
                //! Server-side storage.
                //!
                mGL.glBufferData(storage.getTarget().eValue, storage.getCapacity(), storage.getMode().eValue);
            }
            storage.setUpdated();
        } else if (storage.hasUpdate(Storage.CONCEPT_DATA_CHANGE)) {
            mGL.glBufferSubData(storage.getTarget().eValue, 0, storage.map());

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
                    mGL.glUniform1f(uniform.getHandle(), value);
                }
                break;
                case Float2: {
                    final Vector2f value = ((UniformFloat2) uniform).getValue();
                    mGL.glUniform2f(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case Float3: {
                    final Vector3f value = ((UniformFloat3) uniform).getValue();
                    mGL.glUniform3f(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case Float4: {
                    final Vector4f value = ((UniformFloat4) uniform).getValue();
                    mGL.glUniform4f(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case FloatArray: {
                    final Float32Array value = ((UniformFloatArray) uniform).getValue();
                    mGL.glUniform1fv(uniform.getHandle(), value);
                }
                break;
                case Int: {
                    final int value = ((UniformInt) uniform).getValue();
                    mGL.glUniform1i(uniform.getHandle(), value);
                }
                break;
                case Int2: {
                    final Vector2i value = ((UniformInt2) uniform).getValue();
                    mGL.glUniform2i(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case Int3: {
                    final Vector3i value = ((UniformInt3) uniform).getValue();
                    mGL.glUniform3i(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case Int4: {
                    final Vector4i value = ((UniformInt4) uniform).getValue();
                    mGL.glUniform4i(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case IntArray: {
                    final Int32Array value = ((UniformIntArray) uniform).getValue();
                    mGL.glUniform1iv(uniform.getHandle(), value);
                }
                break;
                case Matrix3x3: {
                    final Float32Array value = ((UniformMatrix3) uniform).getValue();
                    mGL.glUniformMatrix3fv(uniform.getHandle(), value);
                }
                break;
                case Matrix4x4: {
                    final Float32Array value = ((UniformMatrix4) uniform).getValue();
                    mGL.glUniformMatrix4fv(uniform.getHandle(), value);
                }
                break;
                case UInt: {
                    final int value = ((UniformInt) uniform).getValue();
                    mGL.glUniform1ui(uniform.getHandle(), value);
                }
                break;
                case UInt2: {
                    final Vector2i value = ((UniformInt2) uniform).getValue();
                    mGL.glUniform2ui(uniform.getHandle(), value.getX(), value.getY());
                }
                break;
                case UInt3: {
                    final Vector3i value = ((UniformInt3) uniform).getValue();
                    mGL.glUniform3ui(uniform.getHandle(), value.getX(), value.getY(), value.getZ());
                }
                break;
                case UInt4: {
                    final Vector4i value = ((UniformInt4) uniform).getValue();
                    mGL.glUniform4ui(uniform.getHandle(), value.getX(), value.getY(), value.getZ(), value.getW());
                }
                break;
                case UIntArray: {
                    final UInt32Array value = ((UniformUnsignedIntArray) uniform).getValue();
                    mGL.glUniform1uiv(uniform.getHandle(), value);
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
        if (descriptor.hasUpdate() || !mCapabilities.hasExtension(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT)) {

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
            throw new IllegalStateException("Maximum texture stage is " + mTexture.length);
        }
        if (isActive(texture, stage)) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glActiveTexture(GLES2.GL_TEXTURE0 + stage);
            mGL.glBindTexture(texture.getType().eValue, mTexture[stage] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Storage<?> storage) {
        if (isActive(storage)) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glBindBuffer(storage.getTarget().eValue, mStorage[storage.getTarget().ordinal()] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Shader shader) {
        if (isActive(shader)) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glUseProgram(mShader = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(VertexDescriptor descriptor) {
        if (isActive(descriptor)) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glBindVertexArray(mDescriptor = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Frame frame) {
        if (isActive(frame)) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glBindFramebuffer(GLES2.GL_FRAMEBUFFER, mFrame = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Primitive primitive, int offset, int count) {
        mGL.glDrawArrays(primitive.eValue, offset, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Primitive primitive, int offset, int count, VertexFormat format) {
        mGL.glDrawElements(primitive.eValue, count, format.eValue, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array> T map(Storage<T> storage) {
        final int flag = (storage.getMode().eReadable ? GLES2.GL_MAP_READ_BIT : GLES2.GL_MAP_WRITE_BIT);
        return (T) mGL.glMapBuffer(storage.getTarget().eValue, flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array> T map(Storage<T> storage, int access) {
        return map(storage, access, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array> T map(Storage<T> storage, int offset, int length) {
        return map(storage, 0, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array> T map(Storage<T> storage, int access, int offset, int length) {
        final int flag = (storage.getMode().eReadable ? GLES2.GL_MAP_READ_BIT : GLES2.GL_MAP_WRITE_BIT) | access;
        return (T) mGL.glMapBufferRange(storage.getTarget().eValue, offset, length, flag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array> void unmap(Storage<T> storage) {
        mGL.glUnmapBuffer(storage.getTarget().eValue);
    }

    /**
     * <p>Handle update {@link TextureBorder} inside a {@link Texture}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder xBorder, TextureBorder yBorder) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_S, xBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_T, yBorder.eValue);
        }
    }

    /**
     * <p>Handle update {@link TextureBorder} inside a {@link Texture}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder xBorder, TextureBorder yBorder,
            TextureBorder zBorder) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_S, xBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_T, yBorder.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Z)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_R, zBorder.eValue);
        }
    }

    /**
     * <p>Handle update {@link TextureFilter} inside a {@link Texture}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateTextureFilter(Texture texture, TextureFilter filter) {
        //!
        //! Get the first layer to check.
        //!
        final Image.Layer layer = texture.getImage().getLayer().get(0);

        if (layer.hasMipmap()) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MIN_FILTER, filter.eMinFilterWithMipmap);
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);

            if (mCapabilities.hasExtension(RenderCapabilities.Extension.TEXTURE_FILTER_ANISOTROPIC)) {
                //!
                //! Handle TEXTURE_ANISOTROPIC extension.
                //!
                final float anisotropic = Math.min(
                        mCapabilities.getLimit(RenderCapabilities.Limit.TEXTURE_ANISOTROPIC), filter.eAnisotropicLevel);

                mGL.glTexParameter(texture.getType().eValue, GLESExtension.GL_TEXTURE_MAX_ANISOTROPY, anisotropic);
            }
        } else {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MIN_FILTER, filter.eMinFilter);
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
        }
    }

    /**
     * <p>Handle update {@link Image} inside a {@link Texture}</p>
     *
     * @implNote [INTERNAL]
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
                    imageLayer.data.limit(
                            imageLayer.data.position() + imageLayer.images[mipmap]);
                }

                //!
                //! Upload the image.
                //!
                switch (texture.getType()) {
                    case TEXTURE_2D:
                        if (image.getFormat().eCompressed) {
                            mGL.glCompressedTexImage2D(texture.getType().eValue,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    imageLayer.data);
                        } else {
                            mGL.glTexImage2D(texture.getType().eValue,
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
                            mGL.glCompressedTexImage3D(texture.getType().eValue,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    depth,
                                    0,
                                    imageLayer.data);
                        } else {
                            mGL.glTexImage3D(texture.getType().eValue,
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
                            mGL.glCompressedTexImage2D(GLES2.GL_TEXTURE_CUBE_MAP_POSITIVE_X + layer,
                                    mipmap,
                                    image.getFormat().eValue,
                                    width,
                                    height,
                                    0,
                                    imageLayer.data);
                        } else {
                            mGL.glTexImage2D(GLES2.GL_TEXTURE_CUBE_MAP_POSITIVE_X + layer,
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
                mGL.glGenerateMipmap(texture.getType().eValue);
            }
        }
    }

    /**
     * <p>Handle update {@link Frame}</p>
     *
     * @implNote [INTERNAL]
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
     *
     * @implNote [INTERNAL]
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
     *
     * @implNote [INTERNAL]
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
                mGL.glFramebufferTexture2D(
                        GLES2.GL_FRAMEBUFFER, attachment.eValue, texture.getType().eValue, texture.getHandle(), 0);
                break;
            case TEXTURE_3D:
                throw new UnsupportedOperationException("Frame as Texture3D is not supported yet.");
            case TEXTURE_CUBE:
                throw new UnsupportedOperationException("Frame as Texture2DCube is not supported yet.");
        }
    }

    /**
     * <p>Handle update {@link Frame.RenderTarget} inside a {@link Frame}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.RenderTarget target) {
        //!
        //! Generates the render buffer name.
        //!
        target.setHandle(mGL.glGenRenderbuffers());

        //!
        //! Update the render buffer.
        //!
        mGL.glBindRenderbuffer(GLES2.GL_RENDERBUFFER, target.getHandle());

        if (frame.getSamples() > 1
                && mCapabilities.hasExtension(RenderCapabilities.Extension.FRAME_BUFFER_MULTIPLE_SAMPLE)) {
            final int samples = Math.min(
                    frame.getSamples(), mCapabilities.getIntLimit(RenderCapabilities.Limit.FRAME_SAMPLE));

            mGL.glRenderbufferStorageMultisample(GLES2.GL_RENDERBUFFER, samples, target.format.eValue,
                    frame.getWidth(),
                    frame.getHeight());
        } else {
            mGL.glRenderbufferStorage(GLES2.GL_RENDERBUFFER, target.format.eValue,
                    frame.getWidth(),
                    frame.getHeight());
        }
    }

    /**
     * <p>Handle delete {@link Frame.Target} from a {@link Frame}</p>
     *
     * @implNote [INTERNAL]
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
     *
     * @implNote [INTERNAL]
     */
    private void onDeleteFrameTarget(Frame frame, Frame.TextureTarget target) {
        //!
        //! Delete the texture
        //!
        target.texture.delete();
    }

    /**
     * <p>Handle delete {@link Frame.RenderTarget} from a {@link Frame}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onDeleteFrameTarget(Frame frame, Frame.RenderTarget target) {
        //!
        //! Delete the render buffer
        //!
        mGL.glDeleteRenderbuffers(target.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * <p>Handle update {@link Shader}</p>
     *
     * @implNote [INTERNAL]
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
        mGL.glLinkProgram(handle);

        if (mGL.glGetProgram(handle, GLES2.GL_LINK_STATUS) == GLES2.GL_FALSE) {
            //!
            //! Notify the user why failed to compile.
            //!
            throw new RuntimeException("Error linking program: " + mGL.glGetProgramInfoLog(handle));
        }

        //!
        //! Bind each uniform.
        //!
        shader.getUniforms().forEach((name, uniform) -> onUpdateShaderUniform(shader, name, uniform));

        //!
        //! Dispose all intermediary shader compiled.
        //!
        stages.forEach(mGL::glDeleteShader);
    }

    /**
     * <p>Handle update {@link Stage} inside a {@link Shader}</p>
     *
     * @implNote [INTERNAL]
     */
    private int onUpdateShaderStage(Shader shader, Stage stage) {
        final int id = mGL.glCreateShader(stage.getType().eValue);

        mGL.glShaderSource(id, stage.getSource());
        mGL.glCompileShader(id);
        mGL.glAttachShader(shader.getHandle(), id);

        return id;
    }

    /**
     * <p>Handle update {@link Attribute} inside a {@link Shader}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateShaderAttribute(Shader shader, String name, Attribute attribute) {
        if (attribute.isInput()) {
            mGL.glBindAttribLocation(shader.getHandle(), attribute.getID(), name);
        } else {
            mGL.glBindFragDataLocation(shader.getHandle(), attribute.getID(), name);
        }
    }

    /**
     * <p>Handle update {@link Uniform} inside a {@link Shader}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateShaderUniform(Shader shader, String name, Uniform uniform) {
        if (uniform.getHandle() == Manageable.INVALID_HANDLE) {
            uniform.setHandle(mGL.glGetUniformLocation(shader.getHandle(), name));
        }
    }

    /**
     * <p>Handle update {@link FactoryElementStorage} inside a {@link VertexDescriptor}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateDescriptorIndices(FactoryElementStorage<?> indices) {
        indices.create();

        //!
        //! NOTE: This is required since the renderer will not bind it inside VAO if already bind it outside.
        //!
        mGL.glBindBuffer(StorageTarget.ELEMENT.eValue, indices.getHandle());

        indices.update();
    }

    /**
     * <p>Handle update {@link FactoryArrayStorage} inside a {@link VertexDescriptor}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateDescriptorVertices(FactoryArrayStorage<?> vertices) {
        vertices.create();
        vertices.acquire();
        {
            vertices.getAttributes().forEach(T -> onUpdateDescriptorVertex(vertices, T));
        }
        vertices.update();
    }

    /**
     * <p>Handle update {@link Vertex} inside a {@link FactoryArrayStorage}</p>
     *
     * @implNote [INTERNAL]
     */
    private void onUpdateDescriptorVertex(FactoryArrayStorage<?> vertices, Vertex vertex) {
        mGL.glEnableVertexAttribArray(vertex.getID());
        mGL.glVertexAttribPointer(vertex.getID(),
                vertex.getComponent(),
                vertex.getType().eValue,
                vertex.isNormalised(),
                vertices.getAttributesLength(),
                vertex.getOffset());
    }

    /**
     * <p>Handle update {@link RenderState.Flag}</p>
     */
    private boolean onUpdateState(RenderState.Flag flag, int state) {
        if (flag == RenderState.Flag.ENABLE) {
            mGL.glEnable(state);
        } else if (flag == RenderState.Flag.DISABLE) {
            mGL.glDisable(state);
        } else {
            return false;
        }
        return true;
    }

    /**
     * <p>Handle update {@link RenderState.Flag}</p>
     */
    private boolean onUpdateState(Object source, Object destination, Object none, int state) {
        final boolean isEnabled = (source != none);

        if (!isEnabled) {
            mGL.glDisable(state);
        } else if (destination == none) {
            mGL.glEnable(state);
        }
        return (isEnabled);
    }
}
