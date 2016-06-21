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
import org.quark.system.utility.Manageable;
import org.quark.system.utility.array.Int8Array;
import org.quark.system.utility.array.UInt32Array;

import java.nio.ByteBuffer;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link Render.GLES3}.
 */
public class DesktopGLES30 extends DesktopGLES20 implements Render.GLES3 {
    /**
     * Hold {@link EXT_Vertex_Array_Object} extension.
     */
    protected final EXT_Vertex_Array_Object mVertexArrayObjectExtension;

    /**
     * Hold {@link EXT_Buffer_Map_Range} extension.
     */
    protected final EXT_Buffer_Map_Range mBufferMapRangeExtension;

    /**
     * Hold {@link EXT_Frame_Buffer_Multisample_Object} extension.
     */
    protected final EXT_Frame_Buffer_Multisample_Object mFrameBufferMultisampleExtension;

    /**
     * <p>Constructor</p>
     */
    public DesktopGLES30() {
        //!
        //! Ensure we have the capabilities.
        //!
        super();

        //!
        //! Attach the extension(s).
        //!
        if (mCapabilities.OpenGL30) {
            mVertexArrayObjectExtension = EXT_Vertex_Array_Object.CORE;
        } else if (mCapabilities.GL_ARB_vertex_array_object) {
            mVertexArrayObjectExtension = EXT_Vertex_Array_Object.ARB;
        } else {
            mVertexArrayObjectExtension = EXT_Vertex_Array_Object.NONE;
        }
        if (mCapabilities.OpenGL30) {
            mBufferMapRangeExtension = EXT_Buffer_Map_Range.CORE;
        } else if (mCapabilities.GL_ARB_map_buffer_range) {
            mBufferMapRangeExtension = EXT_Buffer_Map_Range.ARB;
        } else {
            mBufferMapRangeExtension = EXT_Buffer_Map_Range.NONE;
        }
        if (mCapabilities.OpenGL30) {
            mFrameBufferMultisampleExtension = EXT_Frame_Buffer_Multisample_Object.CORE;
        } else if (mCapabilities.GL_EXT_framebuffer_multisample) {
            mFrameBufferMultisampleExtension = EXT_Frame_Buffer_Multisample_Object.EXT;
        } else {
            mFrameBufferMultisampleExtension = EXT_Frame_Buffer_Multisample_Object.NONE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenVertexArrays() {
        return mVertexArrayObjectExtension.glGenVertexArrays();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteVertexArrays(int name) {
        mVertexArrayObjectExtension.glDeleteVertexArrays(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindVertexArray(int name) {
        mVertexArrayObjectExtension.glBindVertexArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int8Array glMapBufferRange(int target, int offset, int size, int access) {
        return new DesktopArrayFactory.DesktopInt8Array(
                mBufferMapRangeExtension.glMapBufferRange(target, offset, size, access));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexImage3D(int target, int level, int internal, int width, int height, int depth, int border,
            int format, int type, Int8Array data) {
        GL12.glTexImage3D(target, level, internal, width, height, depth, border, format, type, data.<ByteBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompressedTexImage3D(int target, int level, int internal, int width, int height, int depth,
            int border, Int8Array data) {
        GL13.glCompressedTexImage3D(target, level, internal, width, height, depth, border, data.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glRenderbufferStorageMultisample(int target, int samples, int format, int width, int height) {
        mFrameBufferMultisampleExtension.glRenderbufferStorageMultisample(target, samples, format, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1ui(int name, int i1) {
        GL30.glUniform1ui(name, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2ui(int name, int i1, int i2) {
        GL30.glUniform2ui(name, i1, i2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3ui(int name, int i1, int i2, int i3) {
        GL30.glUniform3ui(name, i1, i2, i3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4ui(int name, int i1, int i2, int i3, int i4) {
        GL30.glUniform4ui(name, i1, i2, i3, i4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1uiv(int name, UInt32Array buffer) {
        GL30.nglUniform1uiv(name, buffer.remaining(), MemoryUtil.memAddress(buffer.<ByteBuffer>data()));
    }

    /**
     * @see <a href="https://www.opengl.org/registry/specs/ARB/vertex_array_object.txt">Link</a>
     */
    private enum EXT_Vertex_Array_Object {
        /**
         * Handle when {@link EXT_Vertex_Array_Object} is not supported.
         */
        NONE,

        /**
         * Handle when {@link EXT_Vertex_Array_Object} is supported by extension.
         */
        ARB,

        /**
         * Handle when {@link EXT_Vertex_Array_Object} is fully supported.
         */
        CORE;

        /**
         * @see Render.GLES3#glGenVertexArrays()
         */
        public int glGenVertexArrays() {
            switch (this) {
                case ARB:
                    return ARBVertexArrayObject.glGenVertexArrays();
                case CORE:
                    return GL30.glGenVertexArrays();
            }
            return Manageable.INVALID_HANDLE;
        }

        /**
         * @see Render.GLES3#glBindVertexArray(int)
         */
        public void glBindVertexArray(int name) {
            switch (this) {
                case ARB:
                    ARBVertexArrayObject.glBindVertexArray(name);
                    break;
                case CORE:
                    GL30.glBindVertexArray(name);
                    break;
            }
        }

        /**
         * @see Render.GLES3#glDeleteVertexArrays(int)
         */
        public void glDeleteVertexArrays(int name) {
            switch (this) {
                case ARB:
                    ARBVertexArrayObject.glDeleteVertexArrays(name);
                    break;
                case CORE:
                    GL30.glDeleteVertexArrays(name);
                    break;
            }
        }
    }

    /**
     * @see <a href="https://www.khronos.org/registry/gles/extensions/EXT/EXT_map_buffer_range.txt">Link</a>
     */
    private enum EXT_Buffer_Map_Range {
        /**
         * Handle when {@link EXT_Buffer_Map_Range} is not supported.
         */
        NONE,

        /**
         * Handle when {@link EXT_Buffer_Map_Range} is supported by extension.
         */
        ARB,

        /**
         * Handle when {@link EXT_Buffer_Map_Range} is fully supported.
         */
        CORE;

        /**
         * @see Render.GLES3#glMapBufferRange(int, int, int, int) ()
         */
        public ByteBuffer glMapBufferRange(int target, int offset, int size, int access) {
            switch (this) {
                case ARB:
                    return ARBMapBufferRange.glMapBufferRange(target, offset, size, access);
                case CORE:
                    return GL30.glMapBufferRange(target, offset, size, access);
            }
            return GL15.glMapBuffer(target, access);
        }
    }

    /**
     * @see <a href="https://www.khronos.org/registry/gles/extensions/EXT/EXT_multisampled_render_to_texture.txt">Link</a>
     */
    private enum EXT_Frame_Buffer_Multisample_Object {
        /**
         * Handle when {@link EXT_Frame_Buffer_Multisample_Object} is not supported.
         */
        NONE,

        /**
         * Handle when {@link EXT_Frame_Buffer_Multisample_Object} is supported by extension.
         */
        EXT,

        /**
         * Handle when {@link EXT_Frame_Buffer_Multisample_Object} is fully supported.
         */
        CORE;

        /**
         * @see Render.GLES3#glRenderbufferStorageMultisample(int, int, int, int, int)
         */
        public void glRenderbufferStorageMultisample(int target, int samples, int format, int width, int height) {
            switch (this) {
                case EXT:
                    EXTFramebufferMultisample.glRenderbufferStorageMultisampleEXT(target, samples, format, width, height);
                    break;
                case CORE:
                    GL30.glRenderbufferStorageMultisample(target, samples, format, width, height);
                    break;
            }
        }
    }
}
