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
package org.quark.backend.lwjgl.opengl;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;
import org.quark.backend.lwjgl.utility.array.DesktopArrayFactory;
import org.quark.render.Render;
import org.quark.render.RenderCapabilities;
import org.quark.system.utility.Manageable;
import org.quark.system.utility.array.*;

import java.nio.ByteBuffer;
import java.util.EnumMap;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link Render.GLES2}.
 */
public class DesktopGLES20 implements Render.GLES2 {
    protected final GLCapabilities mCapabilities;

    /**
     * Hold {@link EXT_Frame_Buffer_Object} extension.
     */
    protected final EXT_Frame_Buffer_Object mFrameBufferObjectExtension;

    /**
     * <p>Constructor</p>
     */
    public DesktopGLES20() {
        //!
        //! Read the capabilities.
        //!
        mCapabilities = GL.getCapabilities();

        //!
        //! Attach the the extension(s).
        //!
        if (mCapabilities.OpenGL30) {
            mFrameBufferObjectExtension = EXT_Frame_Buffer_Object.CORE;
        } else if (mCapabilities.GL_ARB_framebuffer_object) {
            mFrameBufferObjectExtension = EXT_Frame_Buffer_Object.ARB;
        } else if (mCapabilities.GL_EXT_framebuffer_object) {
            mFrameBufferObjectExtension = EXT_Frame_Buffer_Object.EXT;
        } else {
            mFrameBufferObjectExtension = EXT_Frame_Buffer_Object.NONE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderCapabilities glCapabilities() {
        //!
        //! Retrieves the capabilities from the context.
        //!
        final GLCapabilities capabilities = GL.getCapabilities();

        final RenderCapabilities.LanguageVersion version;
        if (capabilities.OpenGL33) {
            version = RenderCapabilities.LanguageVersion.GL33;
        } else if (capabilities.OpenGL32) {
            version = RenderCapabilities.LanguageVersion.GL32;
        } else if (capabilities.OpenGL31) {
            version = RenderCapabilities.LanguageVersion.GL31;
        } else if (capabilities.OpenGL30) {
            version = RenderCapabilities.LanguageVersion.GL30;
        } else if (capabilities.OpenGL21) {
            version = RenderCapabilities.LanguageVersion.GL21;
        } else {
            throw new RuntimeException("Cannot find a suitable context, OpenGL 2.1 is at-least required.");
        }

        //!
        //! Retrieves the limitation from the context.
        //!
        final EnumMap<RenderCapabilities.Limit, Float> limit = new EnumMap<>(RenderCapabilities.Limit.class);
        limit.put(RenderCapabilities.Limit.FRAME_ATTACHMENT,
                GL11.glGetFloat(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));

        limit.put(RenderCapabilities.Limit.FRAME_MULTIPLE_RENDER_ATTACHMENT,
                GL11.glGetFloat(ARBDrawBuffers.GL_MAX_DRAW_BUFFERS_ARB));
        limit.put(RenderCapabilities.Limit.FRAME_SAMPLE,
                GL11.glGetFloat(GL30.GL_MAX_SAMPLES));

        limit.put(RenderCapabilities.Limit.TEXTURE_ANISOTROPIC,
                GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
        limit.put(RenderCapabilities.Limit.TEXTURE_SIZE,
                GL11.glGetFloat(GL11.GL_MAX_TEXTURE_SIZE));
        limit.put(RenderCapabilities.Limit.TEXTURE_STAGE,
                GL11.glGetFloat(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));

        //!
        //! Retrieves the extension from the context.
        //!
        final EnumMap<RenderCapabilities.Extension, Boolean> extension = new EnumMap<>(RenderCapabilities.Extension.class);
        extension.put(RenderCapabilities.Extension.FRAME_BUFFER,
                capabilities.GL_ARB_framebuffer_object);
        extension.put(RenderCapabilities.Extension.FRAME_BUFFER_MULTIPLE_RENDER_TARGET,
                capabilities.GL_ARB_draw_buffers);
        extension.put(RenderCapabilities.Extension.FRAME_BUFFER_MULTIPLE_SAMPLE,
                capabilities.GL_ARB_multisample);
        extension.put(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT,
                capabilities.GL_ARB_vertex_array_object);

        extension.put(RenderCapabilities.Extension.TEXTURE_COMPRESSION_S3TC,
                capabilities.GL_EXT_texture_compression_s3tc);
        extension.put(RenderCapabilities.Extension.TEXTURE_FILTER_ANISOTROPIC,
                capabilities.GL_EXT_texture_filter_anisotropic);

        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE,
                capabilities.GL_ARB_explicit_attrib_location);
        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM,
                capabilities.GL_ARB_explicit_uniform_location);
        extension.put(RenderCapabilities.Extension.GLSL_GEOMETRY,
                capabilities.GL_ARB_geometry_shader4);
        return new RenderCapabilities(version, extension, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glEnable(int value) {
        GL11.glEnable(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDisable(int value) {
        GL11.glDisable(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBlendFunc(int source, int destination) {
        GL11.glBlendFunc(source, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBlendEquationSeparate(int rgb, int alpha) {
        GL20.glBlendEquationSeparate(rgb, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCullFace(int mode) {
        GL11.glCullFace(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthMask(boolean activate) {
        GL11.glDepthMask(activate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthRange(float near, float far) {
        GL11.glDepthRange(near, far);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthFunc(int mode) {
        GL11.glDepthFunc(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GL11.glColorMask(red, green, blue, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glScissor(int x1, int y1, int x2, int y2) {
        GL11.glScissor(x1, y1, x2, y2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glStencilOpSeparate(int face, int stencilFail, int depthFail, int depthPass) {
        GL20.glStencilOpSeparate(face, stencilFail, depthFail, depthPass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
        GL20.glStencilFuncSeparate(face, func, ref, mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glClear(int value) {
        GL11.glClear(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenTextures() {
        return GL11.glGenTextures();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenBuffers() {
        return GL15.glGenBuffers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glCreateProgram() {
        return GL20.glCreateProgram();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenFramebuffers() {
        return mFrameBufferObjectExtension.glGenFramebuffers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenRenderbuffers() {
        return mFrameBufferObjectExtension.glGenRenderbuffers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteTextures(int name) {
        GL11.glDeleteTextures(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteBuffers(int name) {
        GL15.glDeleteBuffers(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteProgram(int name) {
        GL20.glDeleteProgram(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteFramebuffers(int name) {
        mFrameBufferObjectExtension.glDeleteFramebuffers(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteRenderbuffers(int name) {
        mFrameBufferObjectExtension.glDeleteRenderbuffers(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glActiveTexture(int stage) {
        GL13.glActiveTexture(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindTexture(int target, int name) {
        GL11.glBindTexture(target, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindBuffer(int target, int name) {
        GL15.glBindBuffer(target, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUseProgram(int name) {
        GL20.glUseProgram(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindFramebuffer(int type, int name) {
        mFrameBufferObjectExtension.glBindFramebuffer(type, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindRenderbuffer(int type, int name) {
        mFrameBufferObjectExtension.glBindRenderbuffer(type, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, int capacity, int usage) {
        GL15.glBufferData(target, capacity, usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Array<?> data, int usage) {
        GL15.nglBufferData(target, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int8Array data, int usage) {
        GL15.nglBufferData(target, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int16Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int32Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt8Array data, int usage) {
        GL15.nglBufferData(target, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt16Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt32Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Float16Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Float32Array data, int usage) {
        GL15.nglBufferData(target, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()), usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, Array<?> data) {
        GL15.nglBufferSubData(target, offset, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int8Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int16Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int32Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt8Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining(), MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt16Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt32Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Float16Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 1, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Float32Array data) {
        GL15.nglBufferSubData(target, offset, data.remaining() << 2, MemoryUtil.memAddress(data.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDrawArrays(int primitive, int offset, int count) {
        GL11.glDrawArrays(primitive, offset, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDrawElements(int primitive, int count, int format, int offset) {
        GL11.glDrawElements(primitive, count, format, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUnmapBuffer(int target) {
        GL15.glUnmapBuffer(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Array glMapBuffer(int target, int access) {
        return new DesktopArrayFactory.DesktopInt8Array(GL15.glMapBuffer(target, access));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, int value) {
        GL11.glTexParameteri(target, target, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, float value) {
        GL11.glTexParameterf(target, target, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexImage2D(int target, int level, int internal, int width, int height, int border,
            int format, int type, Int8Array data) {
        GL11.glTexImage2D(target, level, internal, width, height, border, format, type, data.<ByteBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompressedTexImage2D(int target, int level, int internal, int width, int height, int border,
            Int8Array data) {
        GL13.glCompressedTexImage2D(target, level, internal, width, height, border, data.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glGenerateMipmap(int target) {
        GL30.glGenerateMipmap(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glFramebufferTexture2D(int target, int attachment, int texture, int name, int level) {
        mFrameBufferObjectExtension.glFramebufferTexture2D(target, attachment, texture, name, level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glEnableVertexAttribArray(int name) {
        GL20.glEnableVertexAttribArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDisableVertexAttribArray(int name) {
        GL20.glDisableVertexAttribArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glVertexAttribPointer(int name, int component, int type, boolean normalised, int stride, int offset) {
        GL20.glVertexAttribPointer(name, component, type, normalised, stride, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glLinkProgram(int name) {
        GL20.glLinkProgram(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGetProgram(int name, int property) {
        return GL20.glGetProgrami(name, property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteShader(int name) {
        GL20.glDeleteShader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glCreateShader(int type) {
        return GL20.glCreateShader(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glShaderSource(int name, String source) {
        GL20.glShaderSource(name, source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompileShader(int name) {
        GL20.glCompileShader(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glAttachShader(int name, int shader) {
        GL20.glAttachShader(name, shader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindAttribLocation(int name, int id, String attribute) {
        GL20.glBindAttribLocation(name, id, attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGetUniformLocation(int name, String uniform) {
        return GL20.glGetUniformLocation(name, uniform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String glGetProgramInfoLog(int name) {
        return GL20.glGetProgramInfoLog(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glRenderbufferStorage(int target, int format, int width, int height) {
        mFrameBufferObjectExtension.glRenderbufferStorage(target, format, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1f(int name, float i1) {
        GL20.glUniform1f(name, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2f(int name, float i1, float i2) {
        GL20.glUniform2f(name, i1, i2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3f(int name, float i1, float i2, float i3) {
        GL20.glUniform3f(name, i1, i2, i3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4f(int name, float i1, float i2, float i3, float i4) {
        GL20.glUniform4f(name, i1, i2, i3, i4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1fv(int name, Float32Array buffer) {
        GL20.nglUniform1fv(name, buffer.remaining(), MemoryUtil.memAddress(buffer.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1i(int name, int i1) {
        GL20.glUniform1i(name, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2i(int name, int i1, int i2) {
        GL20.glUniform2i(name, i1, i2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3i(int name, int i1, int i2, int i3) {
        GL20.glUniform3i(name, i1, i2, i3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4i(int name, int i1, int i2, int i3, int i4) {
        GL20.glUniform4i(name, i1, i2, i3, i4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1iv(int name, Int32Array buffer) {
        GL20.nglUniform1iv(name, buffer.remaining(), MemoryUtil.memAddress(buffer.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix3fv(int name, Float32Array buffer) {
        GL20.nglUniformMatrix3fv(
                name, (buffer.remaining() >> 2) / 9, false, MemoryUtil.memAddress(buffer.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix4fv(int name, Float32Array buffer) {
        GL20.nglUniformMatrix4fv(
                name, buffer.remaining() >> 6, false, MemoryUtil.memAddress(buffer.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindFragDataLocation(int name, int index, String attribute) {
        GL30.glBindFragDataLocation(name, index, attribute);
    }

    /**
     * @see <a href="https://www.opengl.org/registry/specs/ARB/framebuffer_object.txt">Link</a>
     */
    private enum EXT_Frame_Buffer_Object {
        /**
         * Handle when {@link EXT_Frame_Buffer_Object} is not supported.
         */
        NONE,

        /**
         * Handle when {@link EXT_Frame_Buffer_Object} is supported by extension.
         */
        ARB,

        /**
         * Handle when {@link EXT_Frame_Buffer_Object} is supported by extension.
         */
        EXT,

        /**
         * Handle when {@link EXT_Frame_Buffer_Object} is fully supported.
         */
        CORE;

        /**
         * @see Render.GLES2#glGenFramebuffers()
         */
        public int glGenFramebuffers() {
            switch (this) {
                case ARB:
                    return ARBFramebufferObject.glGenFramebuffers();
                case EXT:
                    return EXTFramebufferObject.glGenFramebuffersEXT();
                case CORE:
                    return GL30.glGenFramebuffers();
            }
            return Manageable.INVALID_HANDLE;
        }

        /**
         * @see Render.GLES2#glGenRenderbuffers()
         */
        public int glGenRenderbuffers() {
            switch (this) {
                case ARB:
                    return ARBFramebufferObject.glGenRenderbuffers();
                case EXT:
                    return EXTFramebufferObject.glGenRenderbuffersEXT();
                case CORE:
                    return GL30.glGenRenderbuffers();
            }
            return Manageable.INVALID_HANDLE;
        }

        /**
         * @see Render.GLES2#glBindFramebuffer(int, int)
         */
        public void glBindFramebuffer(int type, int name) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glBindFramebuffer(type, name);
                    break;
                case EXT:
                    EXTFramebufferObject.glBindFramebufferEXT(type, name);
                    break;
                case CORE:
                    GL30.glBindFramebuffer(type, name);
                    break;
            }
        }

        /**
         * @see Render.GLES2#glBindRenderbuffer(int, int)
         */
        public void glBindRenderbuffer(int type, int name) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glBindRenderbuffer(type, name);
                    break;
                case EXT:
                    EXTFramebufferObject.glBindRenderbufferEXT(type, name);
                    break;
                case CORE:
                    GL30.glBindRenderbuffer(type, name);
                    break;
            }
        }

        /**
         * @see Render.GLES2#glFramebufferTexture2D(int, int, int, int, int)
         */
        public void glFramebufferTexture2D(int target, int attachment, int texture, int name, int level) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glFramebufferTexture2D(target, attachment, texture, name, level);
                    break;
                case EXT:
                    EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, texture, name, level);
                    break;
                case CORE:
                    GL30.glFramebufferTexture2D(target, attachment, texture, name, level);
                    break;
            }
        }

        /**
         * @see Render.GLES2#glRenderbufferStorage(int, int, int, int)
         */
        public void glRenderbufferStorage(int target, int format, int width, int height) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glRenderbufferStorage(target, format, width, height);
                    break;
                case EXT:
                    EXTFramebufferObject.glRenderbufferStorageEXT(target, format, width, height);
                    break;
                case CORE:
                    GL30.glRenderbufferStorage(target, format, width, height);
                    break;
            }
        }

        /**
         * @see Render.GLES2#glDeleteFramebuffers(int)
         */
        public void glDeleteFramebuffers(int name) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glDeleteFramebuffers(name);
                    break;
                case EXT:
                    EXTFramebufferObject.glDeleteFramebuffersEXT(name);
                    break;
                case CORE:
                    GL30.glDeleteFramebuffers(name);
                    break;
            }
        }

        /**
         * @see Render.GLES2#glDeleteRenderbuffers(int)
         */
        public void glDeleteRenderbuffers(int name) {
            switch (this) {
                case ARB:
                    ARBFramebufferObject.glDeleteRenderbuffers(name);
                    break;
                case EXT:
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(name);
                    break;
                case CORE:
                    GL30.glDeleteRenderbuffers(name);
                    break;
            }
        }
    }
}