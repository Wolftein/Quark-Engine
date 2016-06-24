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
package org.quark.backend.teavm.opengl;

import org.quark.render.Render;
import org.quark.render.RenderCapabilities;
import org.quark.system.utility.array.Array;
import org.quark.system.utility.array.Int8Array;
import org.quark.system.utility.array.UInt32Array;
import org.teavm.jso.JSMethod;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.webgl.WebGLRenderingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for {@link Render.GLES3}.
 */
public class WebOpenGLES30 extends WebOpenGLES20 implements Render.GLES3 {
    private final OES_vertex_array_object mVertexArrayObjectExtension;

    /**
     * Hold all factories for all WebGL component(s).
     */
    protected final Map<Integer, JSObject> mVertexArrayFactory = new HashMap<>();

    /**
     * Hold all unique identifier(s) for all WebGL component(s).
     */
    protected int mVertexArray = 1;

    /**
     * <p>Constructor</p>
     */
    public WebOpenGLES30(HTMLCanvasElement canvas) {
        super(canvas);

        //!
        //! Attach the extension(s).
        //!
        mVertexArrayObjectExtension = getExtension(OES_vertex_array_object.NAMES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderCapabilities glCapabilities() {
        //!
        //! Retrieves the extension from the context.
        //!
        final Map<RenderCapabilities.Extension, Boolean> extension = new HashMap<>();

        extension.put(RenderCapabilities.Extension.FRAME_BUFFER, true);
        extension.put(RenderCapabilities.Extension.FRAME_BUFFER_MULTIPLE_RENDER_TARGET,
                isExtension("WEBGL_draw_buffers"));
        extension.put(RenderCapabilities.Extension.FRAME_BUFFER_MULTIPLE_SAMPLE, true);

        extension.put(RenderCapabilities.Extension.VERTEX_ARRAY_OBJECT,
                isExtension(OES_vertex_array_object.NAMES));

        extension.put(RenderCapabilities.Extension.TEXTURE_3D, false);
        extension.put(RenderCapabilities.Extension.TEXTURE_COMPRESSION_S3TC,
                isExtension(OES_texture_compression_s3tc.NAMES));
        extension.put(RenderCapabilities.Extension.TEXTURE_FILTER_ANISOTROPIC,
                isExtension(OES_texture_filter_anisotropic.NAMES));

        extension.put(RenderCapabilities.Extension.GLSL_PRECISION, true);
        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_ATTRIBUTE, false);
        extension.put(RenderCapabilities.Extension.GLSL_EXPLICIT_UNIFORM, false);
        extension.put(RenderCapabilities.Extension.GLSL_GEOMETRY, false);

        //!
        //! Retrieves the limitation from the context.
        //!
        final Map<RenderCapabilities.Limit, Float> limit = new HashMap<>();

        limit.put(RenderCapabilities.Limit.FRAME_SAMPLE,
                mGL.getParameterf(WebGLRenderingContext.SAMPLE_BUFFERS));

        limit.put(RenderCapabilities.Limit.TEXTURE_ANISOTROPIC,
                mGL.getParameterf(0x84FF));

        limit.put(RenderCapabilities.Limit.TEXTURE_SIZE,
                mGL.getParameterf(WebGLRenderingContext.MAX_TEXTURE_SIZE));
        limit.put(RenderCapabilities.Limit.TEXTURE_STAGE,
                mGL.getParameterf(WebGLRenderingContext.MAX_TEXTURE_IMAGE_UNITS));

        limit.put(RenderCapabilities.Limit.GLSL_MAX_VERTEX_ATTRIBUTES,
                mGL.getParameterf(WebGLRenderingContext.MAX_VERTEX_ATTRIBS));

        return new RenderCapabilities(RenderCapabilities.LanguageVersion.GLES20, extension, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenVertexArrays() {
        final int id = mVertexArray++;

        mVertexArrayFactory.put(id, mVertexArrayObjectExtension.createVertexArrayOES());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteVertexArrays(int name) {
        mVertexArrayObjectExtension.deleteVertexArrayOES(mVertexArrayFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindVertexArray(int name) {
        mVertexArrayObjectExtension.bindVertexArrayOES(mVertexArrayFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array<?>> T glMapBufferRange(int target, int offset, int size, int access, int format) {
        //!
        //! NOTE: Javascript doesn't support access to raw memory.
        //!
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexImage3D(int target, int level, int internal, int width, int height, int depth, int border,
            int format, int type, Int8Array data) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompressedTexImage3D(int target, int level, int internal, int width, int height, int depth,
            int border, Int8Array data) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glRenderbufferStorageMultisample(int target, int samples, int format, int width, int height) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1ui(int name, int i1) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2ui(int name, int i1, int i2) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3ui(int name, int i1, int i2, int i3) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4ui(int name, int i1, int i2, int i3, int i4) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1uiv(int name, UInt32Array buffer) {
    }

    /**
     * @see <a href="https://www.khronos.org/registry/webgl/extensions/OES_vertex_array_object/">Extension</a>
     */
    public interface OES_vertex_array_object extends JSObject {
        String[] NAMES = {
                "OES_vertex_array_object", "O_OES_vertex_array_object",
                "IE_OES_vertex_array_object", "MOZ_OES_vertex_array_object",
                "WEBKIT_OES_vertex_array_object"};

        @JSMethod
        void bindVertexArrayOES(JSObject name);

        @JSMethod
        void deleteVertexArrayOES(JSObject name);

        @JSMethod
        JSObject createVertexArrayOES();
    }

    /**
     * @see <a href="https://www.khronos.org/registry/webgl/extensions/OES_compressed_texture_s3tc/">Extension</a>
     */
    public interface OES_texture_compression_s3tc extends JSObject {
        String[] NAMES = {
                "WEBGL_compressed_texture_s3tc", "O_WEBGL_compressed_texture_s3tc",
                "IE_WEBGL_compressed_texture_s3tc", "MOZ_WEBGL_compressed_texture_s3tc",
                "WEBKIT_WEBGL_compressed_texture_s3tc"};
    }

    /**
     * @see <a href="https://www.khronos.org/registry/webgl/extensions/OES_texture_filter_anisotropic/">Extension</a>
     */
    public interface OES_texture_filter_anisotropic extends JSObject {
        String[] NAMES = {
                "EXT_texture_filter_anisotropic", "O_EXT_texture_filter_anisotropic",
                "IE_EXT_texture_filter_anisotropic", "MOZ_EXT_texture_filter_anisotropic",
                "WEBKIT_EXT_texture_filter_anisotropic"};
    }
}
