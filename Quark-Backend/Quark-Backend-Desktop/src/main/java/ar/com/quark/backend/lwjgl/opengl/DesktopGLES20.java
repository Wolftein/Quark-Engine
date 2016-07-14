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
package ar.com.quark.backend.lwjgl.opengl;

import ar.com.quark.backend.lwjgl.utility.buffer.*;
import ar.com.quark.graphic.Graphic;
import ar.com.quark.graphic.GraphicCapabilities;
import ar.com.quark.utility.Manageable;
import ar.com.quark.utility.buffer.*;
import org.eclipse.collections.api.map.primitive.MutableObjectBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableObjectFloatMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectFloatHashMap;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Implementation for {@link Graphic.GLES2}.
 */
public class DesktopGLES20 implements Graphic.GLES2 {
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
        //! Attach the extension(s).
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
    public GraphicCapabilities glCapabilities() {
        //!
        //! Retrieves the capabilities from the context.
        //!
        final GLCapabilities capabilities = GL.getCapabilities();

        final GraphicCapabilities.LanguageVersion version;
        if (capabilities.OpenGL33) {
            version = GraphicCapabilities.LanguageVersion.GL33;
        } else if (capabilities.OpenGL32) {
            version = GraphicCapabilities.LanguageVersion.GL32;
        } else if (capabilities.OpenGL31) {
            version = GraphicCapabilities.LanguageVersion.GL31;
        } else if (capabilities.OpenGL30) {
            version = GraphicCapabilities.LanguageVersion.GL30;
        } else if (capabilities.OpenGL21) {
            version = GraphicCapabilities.LanguageVersion.GL21;
        } else {
            throw new RuntimeException("Cannot find a suitable context, OpenGL 2.1 is at-least required.");
        }

        //!
        //! Retrieves the limitation from the context.
        //!
        final MutableObjectFloatMap<GraphicCapabilities.Limit> limit
                = new ObjectFloatHashMap(GraphicCapabilities.Limit.values().length);

        limit.put(GraphicCapabilities.Limit.FRAME_ATTACHMENT,
                GL11.glGetFloat(GL30.GL_MAX_COLOR_ATTACHMENTS));

        limit.put(GraphicCapabilities.Limit.FRAME_MULTIPLE_RENDER_ATTACHMENT,
                GL11.glGetFloat(ARBDrawBuffers.GL_MAX_DRAW_BUFFERS_ARB));

        limit.put(GraphicCapabilities.Limit.FRAME_SAMPLE,
                GL11.glGetFloat(GL30.GL_MAX_SAMPLES));

        limit.put(GraphicCapabilities.Limit.TEXTURE_ANISOTROPIC,
                GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));

        limit.put(GraphicCapabilities.Limit.TEXTURE_SIZE,
                GL11.glGetFloat(GL11.GL_MAX_TEXTURE_SIZE));

        limit.put(GraphicCapabilities.Limit.TEXTURE_STAGE,
                GL11.glGetFloat(GL20.GL_MAX_TEXTURE_IMAGE_UNITS));

        limit.put(GraphicCapabilities.Limit.GLSL_MAX_VERTEX_ATTRIBUTES,
                GL11.glGetFloat(GL20.GL_MAX_VERTEX_ATTRIBS));

        //!
        //! Retrieves the extension from the context.
        //!
        final MutableObjectBooleanMap<GraphicCapabilities.Extension> extension
                = new ObjectBooleanHashMap<>(GraphicCapabilities.Extension.values().length);

        extension.put(GraphicCapabilities.Extension.FRAME_BUFFER,
                capabilities.GL_ARB_framebuffer_object);
        extension.put(GraphicCapabilities.Extension.FRAME_BUFFER_MULTIPLE_RENDER_TARGET,
                capabilities.GL_ARB_draw_buffers);
        extension.put(GraphicCapabilities.Extension.FRAME_BUFFER_MULTIPLE_SAMPLE,
                capabilities.GL_ARB_multisample);

        extension.put(GraphicCapabilities.Extension.VERTEX_ARRAY_OBJECT,
                capabilities.GL_ARB_vertex_array_object);

        extension.put(GraphicCapabilities.Extension.TEXTURE_3D, true);
        extension.put(GraphicCapabilities.Extension.TEXTURE_COMPRESSION_S3TC,
                capabilities.GL_EXT_texture_compression_s3tc);
        extension.put(GraphicCapabilities.Extension.TEXTURE_FILTER_ANISOTROPIC,
                capabilities.GL_EXT_texture_filter_anisotropic);

        extension.put(GraphicCapabilities.Extension.GLSL_PRECISION,
                capabilities.GL_ARB_shader_precision);
        extension.put(GraphicCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE,
                capabilities.GL_ARB_explicit_attrib_location);
        extension.put(GraphicCapabilities.Extension.GLSL_EXPLICIT_UNIFORM,
                capabilities.GL_ARB_explicit_uniform_location);
        extension.put(GraphicCapabilities.Extension.GLSL_GEOMETRY,
                capabilities.GL_ARB_geometry_shader4);
        return new GraphicCapabilities(version, extension.toImmutable(), limit.toImmutable());
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
    public void glBufferData(int target, Int8Buffer data, int usage) {
        GL15.glBufferData(target, data.<ByteBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int16Buffer data, int usage) {
        GL15.glBufferData(target, data.<ShortBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int32Buffer data, int usage) {
        GL15.glBufferData(target, data.<IntBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UnsignedInt8Buffer data, int usage) {
        GL15.glBufferData(target, data.<ByteBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UnsignedInt16Buffer data, int usage) {
        GL15.glBufferData(target, data.<ShortBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UnsignedInt32Buffer data, int usage) {
        GL15.glBufferData(target, data.<IntBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Float16Buffer data, int usage) {
        GL15.glBufferData(target, data.<ShortBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Float32Buffer data, int usage) {
        GL15.glBufferData(target, data.<FloatBuffer>underlying(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int8Buffer data) {
        GL15.glBufferSubData(target, offset, data.<ByteBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int16Buffer data) {
        GL15.glBufferSubData(target, offset, data.<ShortBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int32Buffer data) {
        GL15.glBufferSubData(target, offset, data.<IntBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UnsignedInt8Buffer data) {
        GL15.glBufferSubData(target, offset, data.<ByteBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UnsignedInt16Buffer data) {
        GL15.glBufferSubData(target, offset, data.<ShortBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UnsignedInt32Buffer data) {
        GL15.glBufferSubData(target, offset, data.<IntBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Float16Buffer data) {
        GL15.glBufferSubData(target, offset, data.<ShortBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Float32Buffer data) {
        GL15.glBufferSubData(target, offset, data.<FloatBuffer>underlying());
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
    public <T extends Buffer<?>> T glMapBuffer(int target, int access, int format) {
        switch (format) {
            case GL_UNSIGNED_BYTE:
                return (T) new DesktopUnsignedInt8Buffer(GL15.glMapBuffer(target, access));
            case GL_UNSIGNED_SHORT:
                return (T) new DesktopUnsignedInt16Buffer(GL15.glMapBuffer(target, access).asShortBuffer());
            case DesktopGLES30.GL_UNSIGNED_INT:
                return (T) new DesktopUnsignedInt32Buffer(GL15.glMapBuffer(target, access).asIntBuffer());
            case GL_BYTE:
                return (T) new DesktopInt8Buffer(GL15.glMapBuffer(target, access));
            case GL_SHORT:
                return (T) new DesktopInt16Buffer(GL15.glMapBuffer(target, access).asShortBuffer());
            case DesktopGLES30.GL_INT:
                return (T) new DesktopInt32Buffer(GL15.glMapBuffer(target, access).asIntBuffer());
            case DesktopGLES30.GL_HALF_FLOAT:
                return (T) new DesktopFloat16Buffer(GL15.glMapBuffer(target, access).asShortBuffer());
            case GL_FLOAT:
                return (T) new DesktopFloat32Buffer(GL15.glMapBuffer(target, access).asFloatBuffer());
        }
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, int value) {
        GL11.glTexParameteri(target, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, float value) {
        GL11.glTexParameterf(target, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexImage2D(int target, int level, int internal, int width, int height, int border,
            int format, int type, Int8Buffer data) {
        GL11.glTexImage2D(target, level, internal, width, height, border, format, type, data.<ByteBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompressedTexImage2D(int target, int level, int internal, int width, int height, int border,
            Int8Buffer data) {
        GL13.glCompressedTexImage2D(target, level, internal, width, height, border, data.underlying());
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
    public void glUniform1fv(int name, Float32Buffer buffer) {
        GL20.glUniform1fv(name, buffer.<FloatBuffer>underlying());
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
    public void glUniform1iv(int name, Int32Buffer buffer) {
        GL20.glUniform1iv(name, buffer.<IntBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix3fv(int name, boolean transpose, Float32Buffer buffer) {
        GL20.glUniformMatrix3fv(name, transpose, buffer.<FloatBuffer>underlying());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix4fv(int name, boolean transpose, Float32Buffer buffer) {
        GL20.glUniformMatrix4fv(name, transpose, buffer.<FloatBuffer>underlying());
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
         * @see Graphic.GLES2#glGenFramebuffers()
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
         * @see Graphic.GLES2#glGenRenderbuffers()
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
         * @see Graphic.GLES2#glBindFramebuffer(int, int)
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
         * @see Graphic.GLES2#glBindRenderbuffer(int, int)
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
         * @see Graphic.GLES2#glFramebufferTexture2D(int, int, int, int, int)
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
         * @see Graphic.GLES2#glRenderbufferStorage(int, int, int, int)
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
         * @see Graphic.GLES2#glDeleteFramebuffers(int)
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
         * @see Graphic.GLES2#glDeleteRenderbuffers(int)
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