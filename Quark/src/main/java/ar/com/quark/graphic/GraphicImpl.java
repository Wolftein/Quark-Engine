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
package ar.com.quark.graphic;

import ar.com.quark.graphic.shader.Attribute;
import ar.com.quark.graphic.shader.Shader;
import ar.com.quark.graphic.shader.Stage;
import ar.com.quark.graphic.shader.Uniform;
import ar.com.quark.graphic.shader.data.*;
import ar.com.quark.graphic.storage.*;
import ar.com.quark.graphic.storage.factory.FactoryArrayStorage;
import ar.com.quark.graphic.storage.factory.FactoryElementStorage;
import ar.com.quark.graphic.texture.*;
import ar.com.quark.graphic.texture.frame.Frame;
import ar.com.quark.graphic.texture.frame.FrameAttachment;
import ar.com.quark.mathematic.*;
import ar.com.quark.utility.Manageable;
import ar.com.quark.utility.buffer.*;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation for {@link Graphic}.
 */
public final class GraphicImpl implements Graphic {
    /**
     * Hold {@link GLES32} context.
     */
    private GLES32 mGL;

    /**
     * Hold {@link GraphicCapabilities} context.
     */
    private GraphicCapabilities mCapabilities;

    /**
     * Hold the states of the renderer (Set all default states)
     */
    private final GraphicState mStates = new GraphicState();

    /**
     * Hold all object(s) acquired (cache).
     */
    private int mTexture[], mTextureActive, mStorage[], mShader, mDescriptor, mFrame;

    /**
     * Hold all object(s) that is being removed.
     */
    private final MutableStack<Manageable> mManageable = Stacks.mutable.empty();

    /**
     * Hold all emulated feature(s).
     */
    private VertexArrayObjectExtension mVertexArrayObjectExtension;

    /**
     * <p>Handle when the module initialise</p>
     */
    public void onModuleCreate(GLES32 gl) {
        this.mGL = gl;

        //!
        //! Get the capabilities from the context.
        //!
        mCapabilities = mGL.glCapabilities();

        mTexture = new int[mCapabilities.getInteger(GraphicCapabilities.Limit.TEXTURE_STAGE)];
        mStorage = new int[StorageTarget.values().length];

        //!
        //! Get all emulated feature(s).
        //!
        if (hasExtension(GraphicCapabilities.Extension.VERTEX_ARRAY_OBJECT)) {
            mVertexArrayObjectExtension = new VertexArrayObjectExtensionCore();
        } else {
            mVertexArrayObjectExtension = new VertexArrayObjectExtensionEmulated();
        }
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
        //!
        //! clean-up all object(s).
        //!
        onModuleUpdate();

        //!
        //! Remove all reference(s).
        //!
        mVertexArrayObjectExtension = null;
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
        while (mManageable.notEmpty()) {
            //!
            //! Manage the delete all component(s) on the correct thread.
            //!
            mManageable.pop().delete();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose(Manageable manageable) {
        mManageable.push(manageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphicCapabilities getCapabilities() {
        return mCapabilities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphicState getStates() {
        return mStates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(GraphicState states) {
        //!
        //! ALPHA_TO_COVERAGE
        //!
        if (states.getAlphaToCoverage() != mStates.getAlphaToCoverage()) {
            onUpdateState(states.getAlphaToCoverage(), GLES2.GL_SAMPLE_ALPHA_TO_COVERAGE);

            mStates.setAlphaToCoverage(states.getAlphaToCoverage());
        }

        //!
        //! BLEND
        //!
        final boolean isBlend = GraphicState.isFlagEnabled(states.getBlend(), mStates.getBlend());

        if (GraphicState.isFlagDirty(states.getBlend(), mStates.getBlend())) {
            if (onUpdateState(isBlend, GLES2.GL_BLEND) && states.getBlend() != GraphicState.Blend.NONE) {
                mGL.glBlendFunc(states.getBlend().eSource, states.getBlend().eDestination);
            }

            mStates.setBlend(states.getBlend());
        }

        //!
        //! BLEND_EQUATION
        //!
        if (isBlend
                && (states.getBlendEquationColour() != mStates.getBlendEquationColour()
                || states.getBlendEquationAlpha() != mStates.getBlendEquationAlpha())) {
            mGL.glBlendEquationSeparate(
                    states.getBlendEquationColour().eValue,
                    states.getBlendEquationAlpha().eValue);

            mStates.setBlendEquation(states.getBlendEquationColour(), states.getBlendEquationAlpha());
        }

        //!
        //! COLOR_MASK
        //!
        if (GraphicState.isFlagDirty(states.getRedMask(), mStates.getRedMask())
                || GraphicState.isFlagDirty(states.getGreenMask(), mStates.getGreenMask())
                || GraphicState.isFlagDirty(states.getBlueMask(), mStates.getBlueMask())
                || GraphicState.isFlagDirty(states.getAlphaMask(), mStates.getAlphaMask())) {

            final boolean red
                    = GraphicState.isFlagEnabled(states.getRedMask(), mStates.getRedMask());
            final boolean green
                    = GraphicState.isFlagEnabled(states.getGreenMask(), mStates.getGreenMask());
            final boolean blue
                    = GraphicState.isFlagEnabled(states.getBlueMask(), mStates.getBlueMask());
            final boolean alpha
                    = GraphicState.isFlagEnabled(states.getAlphaMask(), mStates.getAlphaMask());

            mGL.glColorMask(red, green, blue, alpha);

            final GraphicState.Flag fRed = (states.getRedMask() != GraphicState.Flag.INHERIT)
                    ? states.getRedMask() : mStates.getRedMask();
            final GraphicState.Flag fGreen = (states.getGreenMask() != GraphicState.Flag.INHERIT)
                    ? states.getGreenMask() : mStates.getGreenMask();
            final GraphicState.Flag fBlue = (states.getBlueMask() != GraphicState.Flag.INHERIT)
                    ? states.getBlueMask() : mStates.getBlueMask();
            final GraphicState.Flag fAlpha = (states.getAlphaMask() != GraphicState.Flag.INHERIT)
                    ? states.getAlphaMask() : mStates.getAlphaMask();
            mStates.setColourMask(fRed, fGreen, fBlue, fAlpha);
        }

        //!
        //! CULL
        //!
        final boolean isCull = GraphicState.isFlagEnabled(states.getCullFace(), mStates.getCullFace());

        if (GraphicState.isFlagDirty(states.getCullFace(), mStates.getCullFace())) {
            if (onUpdateState(isCull, GLES2.GL_CULL_FACE) && states.getCullFace() != GraphicState.Cull.NONE) {
                mGL.glCullFace(states.getCullFace().eValue);
            }
            mStates.setCullFace(states.getCullFace());
        }

        //!
        //! DEPTH
        //!
        final boolean isDepth = GraphicState.isFlagEnabled(states.getDepth(), mStates.getDepth());

        if (GraphicState.isFlagDirty(states.getDepth(), mStates.getDepth())) {
            onUpdateState(states.getDepth(), GLES2.GL_DEPTH_TEST);

            mStates.setDepth(states.getDepth());
        }

        //!
        //! DEPTH_MASK
        //!
        if (isDepth && GraphicState.isFlagDirty(states.getDepthMask(), mStates.getDepthMask())) {
            mGL.glDepthMask(states.getDepthMask() == GraphicState.Flag.ENABLE);

            mStates.setDepthMask(states.getDepthMask());
        }

        //!
        //! DEPTH_OP
        //!
        if (isDepth && states.getDepthOp() != mStates.getDepthOp()) {
            mGL.glDepthFunc(states.getDepthOp().eValue);

            mStates.setDepthOp(states.getDepthOp());
        }

        //!
        //! DEPTH_RANGE
        //!
        if (isDepth && !states.getDepthRange().equals(mStates.getDepthRange())) {
            final Vector2f range = states.getDepthRange();

            mGL.glDepthRange(range.getX(), range.getY());

            mStates.setDepthRange(range);
        }

        //!
        //! SCISSOR
        //!
        final boolean isScissor = GraphicState.isFlagEnabled(states.getScissor(), mStates.getScissor());

        if (GraphicState.isFlagDirty(states.getScissor(), mStates.getScissor())) {
            onUpdateState(states.getScissor(), GLES2.GL_SCISSOR_TEST);

            mStates.setScissor(states.getScissor());
        }

        //!
        //! SCISSOR_VIEWPORT
        //!
        if (isScissor && !states.getScissorViewport().equals(mStates.getScissorViewport())) {
            final Vector4i viewport = states.getScissorViewport();

            mGL.glScissor(viewport.getX(), viewport.getY(), viewport.getZ(), viewport.getW());

            mStates.setScissorViewport(viewport.getX(), viewport.getY(), viewport.getZ(), viewport.getW());
        }

        //!
        //! STENCIL
        //!
        final boolean isStencil = GraphicState.isFlagEnabled(states.getStencil(), mStates.getStencil());

        if (GraphicState.isFlagDirty(states.getStencil(), mStates.getStencil())) {
            onUpdateState(states.getStencil(), GLES2.GL_STENCIL_TEST);

            mStates.setStencil(states.getStencil());
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
        return mTexture[stage] ==
                (texture != null ? texture.getHandle() : Manageable.INVALID_HANDLE);
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
        return mShader ==
                (shader != null ? shader.getHandle() : Manageable.INVALID_HANDLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(VertexDescriptor descriptor) {
        return mDescriptor ==
                (descriptor != null ? descriptor.getHandle() : Manageable.INVALID_HANDLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive(Frame frame) {
        return mFrame ==
                (frame != null ? frame.getHandle() : Manageable.INVALID_HANDLE);
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
        if (descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Prevent leaking the component if it was created.
            //!
            //! NOTE: Update the component once.
            //!
            descriptor.setHandle(mVertexArrayObjectExtension.glCreateVertexArray());

            mVertexArrayObjectExtension.glUpdateVertexArray(descriptor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(Frame frame) {
        if (hasExtension(GraphicCapabilities.Extension.FRAME_BUFFER)
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
        if (descriptor.getHandle() == Manageable.INVALID_HANDLE) {
            return;
        } else {
            //!
            //! Proceed to release the component if is acquired
            //!
            release(descriptor);
        }
        mVertexArrayObjectExtension.glDeleteVertexArray(descriptor);
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
        frame.getAttachment().forEachValue((attachment) -> onDeleteFrameTarget(frame, attachment));

        //!
        //! Delete all frame-buffer.
        //!
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
        if (!isActive(texture, stage) && texture.getHandle() != Manageable.INVALID_HANDLE) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            if (mTextureActive != stage) {
                mGL.glActiveTexture(GLES2.GL_TEXTURE0 + stage);

                mTextureActive = stage;
            }
            mGL.glBindTexture(texture.getType().eValue, mTexture[stage] = texture.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Storage<?> storage) {
        if (!isActive(storage) && storage.getHandle() != Manageable.INVALID_HANDLE) {
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
        if (!isActive(shader) && shader.getHandle() != Manageable.INVALID_HANDLE) {
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
        if (!isActive(descriptor)) {
            //!
            //! Prevent acquiring the component if isn't needed.
            //!
            mVertexArrayObjectExtension.glBindVertexArray(descriptor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire(Frame frame) {
        if (!isActive(frame) && frame.getHandle() != Manageable.INVALID_HANDLE) {
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
                    final Texture2D tex2D = (Texture2D) texture;

                    onUpdateTextureBorder(texture, tex2D.getBorderX(), tex2D.getBorderY());
                    break;
                case TEXTURE_3D:
                    final Texture3D tex3D = (Texture3D) texture;

                    onUpdateTextureBorder(texture, tex3D.getBorderX(), tex3D.getBorderY(), tex3D.getBorderZ());
                    break;
                case TEXTURE_CUBE:
                    final Texture2DCube tex2DCube = (Texture2DCube) texture;

                    if (hasExtension(GraphicCapabilities.Extension.TEXTURE_3D)) {
                        //!
                        //! Requires TEXTURE_3D extension.
                        //!
                        onUpdateTextureBorder(texture,
                                tex2DCube.getBorderX(), tex2DCube.getBorderY(), tex2DCube.getBorderZ());
                    } else {
                        onUpdateTextureBorder(texture, tex2DCube.getBorderX(), tex2DCube.getBorderY());
                    }
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
            //! Update the storage for the first time.
            //!
            switch (storage.getType()) {
                case CLIENT:
                    onUpdateStorage(storage);

                    break;
                case SERVER:
                    onUpdateStorage(storage);

                    //!
                    //! Manually remove the memory from the cpu-side.
                    //!
                    storage.deleteAllMemory();
                    break;
                case SERVER_MAPPED:
                    mGL.glBufferData(storage.getTarget().eValue, storage.getCapacity(), storage.getMode().eValue);

                    break;
            }
            storage.setUpdated();
        } else if (storage.hasUpdate(Storage.CONCEPT_DATA_CHANGE)) {
            //!
            //! Update the storage (requires to have been updated for the first time).
            //!
            switch (storage.getType()) {
                case CLIENT:
                    onUpdateStorageAgain(storage);

                    break;
                case SERVER:
                    onUpdateStorageAgain(storage);
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
                    final Float32Buffer value = ((UniformFloatArray) uniform).getValue();
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
                    final Int32Buffer value = ((UniformIntArray) uniform).getValue();
                    mGL.glUniform1iv(uniform.getHandle(), value);
                }
                break;
                case Matrix3x3: {
                    final Float32Buffer value = ((UniformMatrix3) uniform).getValue();
                    mGL.glUniformMatrix3fv(uniform.getHandle(), false, value);
                }
                break;
                case Matrix4x4: {
                    final Float32Buffer value = ((UniformMatrix4) uniform).getValue();
                    mGL.glUniformMatrix4fv(uniform.getHandle(), false, value);
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
                    final UnsignedInt32Buffer value = ((UniformUnsignedIntArray) uniform).getValue();
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
        if (isActive(texture, stage) && texture.getHandle() != Manageable.INVALID_HANDLE) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            if (mTextureActive != stage) {
                mGL.glActiveTexture(GLES2.GL_TEXTURE0 + stage);

                mTextureActive = stage;
            }
            mGL.glBindTexture(texture.getType().eValue, mTexture[stage] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Storage<?> storage) {
        if (isActive(storage) && storage.getHandle() != Manageable.INVALID_HANDLE) {
            //!
            //! Prevent releasing the component if not acquired.
            //!
            mGL.glBindBuffer(storage.getTarget().eValue,
                    mStorage[storage.getTarget().ordinal()] = Manageable.INVALID_HANDLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Shader shader) {
        if (isActive(shader) && shader.getHandle() != Manageable.INVALID_HANDLE) {
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
            //! Prevent acquiring the component if isn't needed.
            //!
            mVertexArrayObjectExtension.glUnbindVertexArray(descriptor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(Frame frame) {
        if (isActive(frame) && frame.getHandle() != Manageable.INVALID_HANDLE) {
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
    public <T extends Buffer<?>> T map(Storage<T> storage) {
        final int flag = (storage.getMode().eReadable ? GLES2.GL_READ_WRITE : GLES2.GL_WRITE_ONLY);
        return (T) mGL.glMapBuffer(storage.getTarget().eValue, flag, storage.getFormat().eValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer<?>> T map(Storage<T> storage, int access) {
        return map(storage, access, 0, storage.getCapacity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer<?>> T map(Storage<T> storage, int offset, int length) {
        return map(storage, 0, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer<?>> T map(Storage<T> storage, int access, int offset, int length) {
        final int flag
                = (storage.getMode().eReadable ? GLES2.GL_MAP_READ_BIT : GLES2.GL_MAP_WRITE_BIT) | access;
        return (T) mGL.glMapBufferRange(storage.getTarget().eValue, offset, length, flag, storage.getFormat().eValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Buffer<?>> void unmap(Storage<T> storage) {
        mGL.glUnmapBuffer(storage.getTarget().eValue);
    }

    /**
     * <p>Update{@link GraphicState.Flag}</p>
     */
    private boolean onUpdateState(GraphicState.Flag flag, int state) {
        if (flag == GraphicState.Flag.ENABLE) {
            mGL.glEnable(state);
        } else if (flag == GraphicState.Flag.DISABLE) {
            mGL.glDisable(state);
        } else {
            return false;
        }
        return true;
    }

    /**
     * <p>Update{@link GraphicState.Flag}</p>
     */
    private boolean onUpdateState(boolean flag, int state) {
        return onUpdateState(flag ? GraphicState.Flag.ENABLE : GraphicState.Flag.DISABLE, state);
    }

    /**
     * <p>Update {@link TextureBorder} with two coordinates</p>
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder x, TextureBorder y) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_S, x.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_T, y.eValue);
        }
    }

    /**
     * <p>Update {@link TextureBorder} with three coordinates</p>
     */
    private void onUpdateTextureBorder(Texture texture, TextureBorder x, TextureBorder y, TextureBorder z) {
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_X)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_S, x.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Y)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_T, y.eValue);
        }
        if (texture.hasUpdate(Texture.CONCEPT_CLAMP_Z)) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_WRAP_R, z.eValue);
        }
    }

    /**
     * <p>Update {@link TextureFilter}</p>
     */
    private void onUpdateTextureFilter(Texture texture, TextureFilter filter) {
        //!
        //! Get the first layer to check.
        //!
        final Image.Layer layer = texture.getImage().getLayer().get(0);

        if (layer.hasMipmap()) {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MIN_FILTER, filter.eMinFilterWithMipmap);
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);

            if (hasExtension(GraphicCapabilities.Extension.TEXTURE_FILTER_ANISOTROPIC)) {
                //!
                //! [EXT: TEXTURE_FILTER_ANISOTROPIC]
                //!
                final float anisotropic = Math.min(
                        mCapabilities.getFloat(GraphicCapabilities.Limit.TEXTURE_ANISOTROPIC), filter.eAnisotropicLevel);
                mGL.glTexParameter(texture.getType().eValue, GLESExtension.GL_TEXTURE_MAX_ANISOTROPY, anisotropic);
            }
        } else {
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MIN_FILTER, filter.eMinFilter);
            mGL.glTexParameter(texture.getType().eValue, GLES2.GL_TEXTURE_MAG_FILTER, filter.eMagFilter);
        }
    }

    /**
     * <p>Update {@link Image}</p>
     */
    private void onUpdateTextureImage(Texture texture, Image image) {
        final ImmutableList<Image.Layer> layers = image.getLayer();

        //!
        //! Iterate over all layer(s) in the image.
        //!
        for (int layer = 0, j = layers.size(); layer < j; ++layer) {
            final Image.Layer imageLayer = layers.get(layer);

            //!
            //! Iterate over all mipmap(s) in the layer.
            //!
            for (int mipmap = 0; mipmap < imageLayer.images.length; mipmap++) {
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
                    imageLayer.data.position(imageLayer.data.limit());
                }
            }

            //!
            //! Generate mip-map if required (will use hardware feature to generate it).
            //!
            if (imageLayer.mipmap && imageLayer.images.length <= 1) {
                mGL.glGenerateMipmap(texture.getType().eValue);
            }

            //!
            //! Delete the image from memory.
            //!
            imageLayer.delete();
        }
    }

    /**
     * <p>Update {@link Storage}</p>
     */
    private void onUpdateStorage(Storage storage) {
        final Buffer buffer = storage.map();

        switch (storage.getFormat()) {
            case BYTE:
                mGL.glBufferData(storage.getTarget().eValue, (Int8Buffer) buffer, storage.getMode().eValue);
                break;
            case UNSIGNED_BYTE:
                mGL.glBufferData(storage.getTarget().eValue, (UnsignedInt8Buffer) buffer, storage.getMode().eValue);
                break;
            case SHORT:
                mGL.glBufferData(storage.getTarget().eValue, (Int16Buffer) buffer, storage.getMode().eValue);
                break;
            case UNSIGNED_SHORT:
                mGL.glBufferData(storage.getTarget().eValue, (UnsignedInt16Buffer) buffer, storage.getMode().eValue);
                break;
            case INT:
                mGL.glBufferData(storage.getTarget().eValue, (Int32Buffer) buffer, storage.getMode().eValue);
                break;
            case UNSIGNED_INT:
                mGL.glBufferData(storage.getTarget().eValue, (UnsignedInt32Buffer) buffer, storage.getMode().eValue);
                break;
            case HALF_FLOAT:
                mGL.glBufferData(storage.getTarget().eValue, (Float16Buffer) buffer, storage.getMode().eValue);
                break;
            case FLOAT:
                mGL.glBufferData(storage.getTarget().eValue, (Float32Buffer) buffer, storage.getMode().eValue);
                break;
        }
    }

    /**
     * <p>Update {@link Storage}</p>
     */
    private void onUpdateStorageAgain(Storage storage) {
        final Buffer buffer = storage.map();
        
        switch (storage.getFormat()) {
            case BYTE:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (Int8Buffer) buffer);
                break;
            case UNSIGNED_BYTE:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (UnsignedInt8Buffer) buffer);
                break;
            case SHORT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (Int16Buffer) buffer);
                break;
            case UNSIGNED_SHORT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (UnsignedInt16Buffer) buffer);
                break;
            case INT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (Int32Buffer) buffer);
                break;
            case UNSIGNED_INT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (UnsignedInt32Buffer) buffer);
                break;
            case HALF_FLOAT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (Float16Buffer) buffer);
                break;
            case FLOAT:
                mGL.glBufferSubData(storage.getTarget().eValue, buffer.offset(), (Float32Buffer) buffer);
                break;
        }
    }

    /**
     * <p>Update {@link Shader}</p>
     */
    private void onUpdateShader(Shader shader) {
        final int handle = shader.getHandle();

        //!
        //! Update each stage of the shader.
        //!
        final List<Integer> stages = new ArrayList<>(shader.getStages().size());

        for (final Stage stage : shader.getStages()) {
            final int id = mGL.glCreateShader(stage.getType().eValue);

            mGL.glShaderSource(id, stage.getSource());
            mGL.glCompileShader(id);
            mGL.glAttachShader(shader.getHandle(), id);

            stages.add(id);
        }

        if (!hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE)) {
            //!
            //! Bind each attribute (if attribute-layout is not supported).
            //!
            shader.getAttributes().forEachKeyValue(
                    (name, attachment) -> onUpdateShaderAttribute(shader, name, attachment));
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
        final boolean force = !hasExtension(GraphicCapabilities.Extension.GLSL_EXPLICIT_UNIFORM);

        shader.getUniforms().forEachKeyValue((name, uniform) -> onUpdateShaderUniform(shader, name, uniform, force));

        //!
        //! Dispose all intermediary shader compiled.
        //!
        stages.forEach(mGL::glDeleteShader);
    }

    /**
     * <p>Update {@link Attribute}</p>
     */
    private void onUpdateShaderAttribute(Shader shader, String name, Attribute attribute) {
        if (attribute.isInput()) {
            mGL.glBindAttribLocation(shader.getHandle(), attribute.getID(), name);
        } else {
            mGL.glBindFragDataLocation(shader.getHandle(), attribute.getID(), name);
        }
    }

    /**
     * <p>Update {@link Uniform}</p>
     */
    private void onUpdateShaderUniform(Shader shader, String name, Uniform uniform, boolean force) {
        if (force || uniform.getHandle() == Manageable.INVALID_HANDLE) {
            uniform.setHandle(mGL.glGetUniformLocation(shader.getHandle(), name));
        }
    }

    /**
     * <p>Update {@link VertexDescriptor}</p>
     */
    private void onUpdateDescriptor(VertexDescriptor descriptor) {
        //!
        //! Bind all storage that contain(s) attribute(s).
        //!
        if (descriptor.hasVertices()) {
            descriptor.getVertices().forEach(this::onUpdateDescriptorVertices);
        }

        //!
        //!  Bind storage that contain(s) indices.
        //!
        if (descriptor.hasIndices()) {
            onUpdateDescriptorIndices(descriptor.getIndices());
        }
    }

    /**
     * <p>Update {@link FactoryElementStorage} in {@link VertexDescriptor}</p>
     */
    private void onUpdateDescriptorIndices(FactoryElementStorage<?> indices) {
        indices.create();

        if (mCapabilities.hasExtension(GraphicCapabilities.Extension.VERTEX_ARRAY_OBJECT)) {
            //!
            //! NOTE: This is required since the renderer will not bind it inside VAO if already bind it outside.
            //!
            mGL.glBindBuffer(
                    StorageTarget.ELEMENT.eValue, mStorage[StorageTarget.ELEMENT.ordinal()] = indices.getHandle());
        } else {
            //!
            //! NOTE: Update it normally if fallback to legacy
            //!
            indices.acquire();
        }

        //!
        //! Force an update on the indices.
        //!
        indices.update();
    }

    /**
     * <p>Update {@link FactoryArrayStorage} in {@link VertexDescriptor}</p>
     */
    private void onUpdateDescriptorVertices(FactoryArrayStorage<?> vertices) {
        vertices.create();

        if (mCapabilities.hasExtension(GraphicCapabilities.Extension.VERTEX_ARRAY_OBJECT)) {
            //!
            //! NOTE: This is required since the renderer will not bind it inside VAO if already bind it outside.
            //!
            mGL.glBindBuffer(
                    StorageTarget.ARRAY.eValue, mStorage[StorageTarget.ARRAY.ordinal()] = vertices.getHandle());
        } else {
            //!
            //! NOTE: Update it normally if fallback to legacy
            //!
            vertices.acquire();
        }
        mVertexArrayObjectExtension.glUpdateVertexArrayAttributes(
                vertices.getAttributes(),
                vertices.getAttributesLength());

        //!
        //! Force an update on the storage.
        //!
        vertices.update();
    }

    /**
     * <p>Update {@link Frame}</p>
     */
    private void onUpdateFrame(Frame frame) {
        frame.acquire();
        {
            frame.getAttachment().forEachKeyValue((name, attachment) -> onUpdateFrameTarget(frame, name, attachment));
        }
        frame.release();
    }

    /**
     * <p>Update {@link Frame.Target}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.Target target) {
        if (target.isTexture()) {
            onUpdateFrameTarget(frame, attachment, (Frame.TextureTarget) target);
        } else {
            onUpdateFrameTarget(frame, attachment, (Frame.RenderTarget) target);
        }
    }

    /**
     * <p>Update {@link Frame.TextureTarget}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.TextureTarget target) {
        final Texture texture = target.texture;

        //!
        //! Create the texture (if not created)
        //!
        texture.create();

        //!
        //! Acquire the texture (if not acquired)
        //!
        texture.acquire();

        //!
        //! Update the texture (if not updated)
        //!
        texture.update();

        //!
        //! Attach the texture into the frame buffer.
        //!
        switch (texture.getType()) {
            case TEXTURE_2D:
                mGL.glFramebufferTexture2D(
                        GLES2.GL_FRAMEBUFFER, attachment.eValue, texture.getType().eValue, texture.getHandle(), 0);
                break;
            default:
                throw new IllegalArgumentException("Currently TEXTURE_2D is supported");
        }
    }

    /**
     * <p>Update {@link Frame.RenderTarget}</p>
     */
    private void onUpdateFrameTarget(Frame frame, FrameAttachment attachment, Frame.RenderTarget target) {
        //!
        //! Create the render buffer.
        //!
        target.setHandle(mGL.glGenRenderbuffers());

        //!
        //! Acquire the render buffer.
        //!
        mGL.glBindRenderbuffer(GLES2.GL_RENDERBUFFER, target.getHandle());

        final int samples = Math.min(
                frame.getSamples(), mCapabilities.getInteger(GraphicCapabilities.Limit.FRAME_SAMPLE));

        if (hasExtension(GraphicCapabilities.Extension.FRAME_BUFFER_MULTIPLE_SAMPLE) && frame.getSamples() > 1) {
            //!
            //! Requires FRAME_BUFFER_MULTIPLE_SAMPLE extension.
            //!
            mGL.glRenderbufferStorageMultisample(GLES2.GL_RENDERBUFFER, samples, target.format.eValue,
                    frame.getWidth(),
                    frame.getHeight());
        } else {
            mGL.glRenderbufferStorage(GLES2.GL_RENDERBUFFER, target.format.eValue, frame.getWidth(), frame.getHeight());
        }
    }

    /**
     * <p>Delete {@link Frame.Target}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.Target target) {
        if (target.isTexture()) {
            onDeleteFrameTarget(frame, (Frame.TextureTarget) target);
        } else {
            onDeleteFrameTarget(frame, (Frame.RenderTarget) target);
        }
    }

    /**
     * <p>Delete {@link Frame.TextureTarget}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.TextureTarget target) {
        target.texture.delete();
    }

    /**
     * <p>Delete {@link Frame.RenderTarget}</p>
     */
    private void onDeleteFrameTarget(Frame frame, Frame.RenderTarget target) {
        mGL.glDeleteRenderbuffers(target.setHandle(Manageable.INVALID_HANDLE));
    }

    /**
     * <p>Short-hand method to check for extension</p>
     */
    private boolean hasExtension(GraphicCapabilities.Extension extension) {
        return mCapabilities.hasExtension(extension);
    }

    /**
     * Encapsulate an interface for (VAO) on non supported platform(s).
     */
    private interface VertexArrayObjectExtension {
        int glCreateVertexArray();

        void glDeleteVertexArray(VertexDescriptor name);

        void glBindVertexArray(VertexDescriptor name);

        void glUnbindVertexArray(VertexDescriptor name);

        void glUpdateVertexArray(VertexDescriptor name);

        void glUpdateVertexArrayAttributes(ImmutableList<Vertex> vertex, int length);
    }

    /**
     * Specialised implementation of {@link VertexArrayObjectExtension} for emulated extension.
     */
    private final class VertexArrayObjectExtensionEmulated implements VertexArrayObjectExtension {
        private int mFactory = 0;

        /**
         * Hold all attribute(s) being enabled or disabled.
         */
        private final boolean mAttributes[], mTemp[];

        /**
         * <p>Constructor</p>
         */
        public VertexArrayObjectExtensionEmulated() {
            mAttributes
                    = new boolean[mCapabilities.getInteger(GraphicCapabilities.Limit.GLSL_MAX_VERTEX_ATTRIBUTES)];
            mTemp
                    = new boolean[mCapabilities.getInteger(GraphicCapabilities.Limit.GLSL_MAX_VERTEX_ATTRIBUTES)];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int glCreateVertexArray() {
            return ++mFactory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glDeleteVertexArray(VertexDescriptor name) {
            name.setHandle(Manageable.INVALID_HANDLE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glBindVertexArray(VertexDescriptor name) {
            mDescriptor = name.getHandle();

            //!
            //! Update the descriptor once it has been acquired.
            //!
            onUpdateDescriptor(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUnbindVertexArray(VertexDescriptor name) {
            mDescriptor = Manageable.INVALID_HANDLE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUpdateVertexArray(VertexDescriptor name) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUpdateVertexArrayAttributes(ImmutableList<Vertex> vertex, int length) {
            //!
            //! Clear all previous values.
            //!
            Arrays.fill(mTemp, false);

            for (final Vertex attribute : vertex) {
                mTemp[attribute.getID()] = true;

                mGL.glVertexAttribPointer(
                        attribute.getID(),
                        attribute.getComponent(),
                        attribute.getType().eValue,
                        attribute.isNormalised(),
                        length,
                        attribute.getOffset());
            }

            for (int i = 0; i < mAttributes.length; i++) {
                //!
                //! Update only if the attribute(s) are different
                //!
                if (mAttributes[i] != mTemp[i]) {
                    if (mTemp[i]) {
                        mGL.glEnableVertexAttribArray(i);
                    } else {
                        mGL.glDisableVertexAttribArray(i);
                    }
                    mAttributes[i] = mTemp[i];
                }
            }
        }
    }

    /**
     * Specialised implementation of {@link VertexArrayObjectExtension} for core extension.
     */
    private final class VertexArrayObjectExtensionCore implements VertexArrayObjectExtension {
        /**
         * {@inheritDoc}
         */
        @Override
        public int glCreateVertexArray() {
            return mGL.glGenVertexArrays();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glDeleteVertexArray(VertexDescriptor name) {
            mGL.glDeleteVertexArrays(name.setHandle(Manageable.INVALID_HANDLE));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glBindVertexArray(VertexDescriptor name) {
            mGL.glBindVertexArray(mDescriptor = name.getHandle());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUnbindVertexArray(VertexDescriptor name) {
            mGL.glBindVertexArray(mDescriptor = Manageable.INVALID_HANDLE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUpdateVertexArray(VertexDescriptor name) {
            acquire(name);
            {
                onUpdateDescriptor(name);
            }
            release(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void glUpdateVertexArrayAttributes(ImmutableList<Vertex> vertex, int length) {
            for (final Vertex attribute : vertex) {
                mGL.glEnableVertexAttribArray(
                        attribute.getID());
                mGL.glVertexAttribPointer(
                        attribute.getID(),
                        attribute.getComponent(),
                        attribute.getType().eValue,
                        attribute.isNormalised(),
                        length,
                        attribute.getOffset());
            }
        }
    }
}
