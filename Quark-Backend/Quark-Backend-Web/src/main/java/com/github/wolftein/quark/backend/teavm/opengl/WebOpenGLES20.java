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
package com.github.wolftein.quark.backend.teavm.opengl;

import com.github.wolftein.quark.render.RenderCapabilities;
import com.github.wolftein.quark.system.utility.array.*;
import com.github.wolftein.quark.system.utility.array.Int16Array;
import com.github.wolftein.quark.system.utility.array.Int8Array;
import com.github.wolftein.quark.render.Render;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.typedarrays.*;
import org.teavm.jso.webgl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for {@link Render.GLES2}.
 */
public class WebOpenGLES20 implements Render.GLES2 {
    protected final WebGLRenderingContext mGL;

    /**
     * Hold all factories for all WebGL component(s).
     */
    protected final Map<Integer, WebGLProgram> mProgramFactory = new HashMap<>();
    protected final Map<Integer, WebGLTexture> mTextureFactory = new HashMap<>();
    protected final Map<Integer, WebGLShader> mShaderFactory = new HashMap<>();
    protected final Map<Integer, WebGLBuffer> mBufferFactory = new HashMap<>();
    protected final Map<Integer, WebGLFramebuffer> mFramebufferFactory = new HashMap<>();
    protected final Map<Integer, WebGLRenderbuffer> mRenderbufferFactory = new HashMap<>();
    protected final Map<Integer, WebGLUniformLocation> mUniformFactory = new HashMap<>();

    /**
     * Hold all unique identifier(s) for all WebGL component(s).
     */
    protected int mProgram = 1;
    protected int mTexture = 1;
    protected int mShader = 1;
    protected int mBuffer = 1;
    protected int mFramebuffer = 1;
    protected int mRenderbuffer = 1;
    protected int mUniform = 1;

    /**
     * <p>Constructor</p>
     */
    public WebOpenGLES20(HTMLCanvasElement canvas) {
        //!
        //! The attribute(s) of the context.
        //!
        final WebGLContextAttributes attributes = WebGLContextAttributes.create();
        attributes.setAlpha(true);
        attributes.setDepth(true);
        attributes.setStencil(true);
        attributes.setPremultipliedAlpha(false);

        final WebGLRenderingContext primary
                = (WebGLRenderingContext) canvas.getContext("webgl", attributes);

        if (primary == null) {
            mGL = (WebGLRenderingContext) canvas.getContext("experimental-webgl", attributes);
        } else {
            mGL = primary;
        }
        mGL.pixelStorei(WebGLRenderingContext.UNPACK_PREMULTIPLY_ALPHA_WEBGL, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderCapabilities glCapabilities() {
        //!
        //! Deferred to #WebOpenGLES30
        //!
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glEnable(int value) {
        mGL.enable(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDisable(int value) {
        mGL.disable(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBlendFunc(int source, int destination) {
        mGL.blendFunc(source, destination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBlendEquationSeparate(int rgb, int alpha) {
        mGL.blendEquationSeparate(rgb, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCullFace(int mode) {
        mGL.cullFace(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthMask(boolean activate) {
        mGL.depthMask(activate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthRange(float near, float far) {
        mGL.depthRange(near, far);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDepthFunc(int mode) {
        mGL.depthFunc(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        mGL.colorMask(red, green, blue, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glScissor(int x1, int y1, int x2, int y2) {
        mGL.scissor(x1, y1, x2, y2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glStencilOpSeparate(int face, int stencilFail, int depthFail, int depthPass) {
        mGL.stencilOpSeparate(face, stencilFail, depthFail, depthPass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
        mGL.stencilFuncSeparate(face, func, ref, mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glClear(int value) {
        mGL.clear(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glClearColor(float red, float green, float blue, float alpha) {
        mGL.clearColor(red, green, blue, alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glViewport(int x, int y, int width, int height) {
        mGL.viewport(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenTextures() {
        final int id = mTexture++;

        mTextureFactory.put(id, mGL.createTexture());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenBuffers() {
        final int id = mBuffer++;

        mBufferFactory.put(id, mGL.createBuffer());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glCreateProgram() {
        final int id = mProgram++;

        mProgramFactory.put(id, mGL.createProgram());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenFramebuffers() {
        final int id = mFramebuffer++;

        mFramebufferFactory.put(id, mGL.createFramebuffer());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGenRenderbuffers() {
        final int id = mRenderbuffer++;

        mRenderbufferFactory.put(id, mGL.createRenderbuffer());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteTextures(int name) {
        mGL.deleteTexture(mTextureFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteBuffers(int name) {
        mGL.deleteBuffer(mBufferFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteProgram(int name) {
        mGL.deleteProgram(mProgramFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteFramebuffers(int name) {
        mGL.deleteFramebuffer(mFramebufferFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteRenderbuffers(int name) {
        mGL.deleteRenderbuffer(mRenderbufferFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glActiveTexture(int stage) {
        mGL.activeTexture(stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindTexture(int target, int name) {
        mGL.bindTexture(target, mTextureFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindBuffer(int target, int name) {
        mGL.bindBuffer(target, mBufferFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUseProgram(int name) {
        mGL.useProgram(mProgramFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindFramebuffer(int type, int name) {
        mGL.bindFramebuffer(type, mFramebufferFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindRenderbuffer(int type, int name) {
        mGL.bindRenderbuffer(type, mRenderbufferFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDrawArrays(int primitive, int offset, int count) {
        mGL.drawArrays(primitive, offset, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDrawElements(int primitive, int count, int format, int offset) {
        mGL.drawElements(primitive, count, format, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, int capacity, int usage) {
        mGL.bufferData(target, capacity, usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, com.github.wolftein.quark.system.utility.array.Int8Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Int16Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, com.github.wolftein.quark.system.utility.array.Int32Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt8Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt16Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, UInt32Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, Float16Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferData(int target, com.github.wolftein.quark.system.utility.array.Float32Array data, int usage) {
        mGL.bufferData(target, data.<ArrayBuffer>data(), usage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int8Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Int16Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, com.github.wolftein.quark.system.utility.array.Int32Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt8Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt16Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, UInt32Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, Float16Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBufferSubData(int target, int offset, com.github.wolftein.quark.system.utility.array.Float32Array data) {
        mGL.bufferSubData(target, offset, data.<ArrayBuffer>data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUnmapBuffer(int target) {
        //!
        //! NOTE: Javascript doesn't support access to raw memory.
        //!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Array<?>> T glMapBuffer(int target, int access, int format) {
        //!
        //! NOTE: Javascript doesn't support access to raw memory.
        //!
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, int value) {
        mGL.texParameteri(target, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexParameter(int target, int type, float value) {
        mGL.texParameterf(target, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glTexImage2D(int target, int level, int internal, int width, int height,
            int border, int format, int type, Int8Array data) {
        final ArrayBufferView view;

        switch (format) {
            case WebGLRenderingContext.UNSIGNED_BYTE:
                view = Uint8Array.create(data.<DataView>data().getBuffer());
                break;
            case WebGLRenderingContext.UNSIGNED_SHORT:
                view = Uint16Array.create(data.<DataView>data().getBuffer());
                break;
            case WebGLRenderingContext.FLOAT:
                view = org.teavm.jso.typedarrays.Float32Array.create(data.<DataView>data().getBuffer());
                break;
            default:
                view = Uint8Array.create(data.<DataView>data().getBuffer());
                break;
        }
        mGL.texImage2D(target, level, format /* WebGL 1.0 */, width, height, border, format, type, view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompressedTexImage2D(int target, int level, int internal, int width, int height,
            int border, Int8Array data) {
        mGL.compressedTexImage2D(target, level, internal, width, height, border, data.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glGenerateMipmap(int target) {
        mGL.generateMipmap(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glFramebufferTexture2D(int target, int attachment, int texture, int name, int level) {
        mGL.framebufferTexture2D(target, attachment, texture, mTextureFactory.get(name), level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glEnableVertexAttribArray(int name) {
        mGL.enableVertexAttribArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDisableVertexAttribArray(int name) {
        mGL.disableVertexAttribArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glVertexAttribPointer(int name, int component, int type, boolean normalised, int stride, int offset) {
        mGL.vertexAttribPointer(name, component, type, normalised, stride, offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glLinkProgram(int name) {
        mGL.linkProgram(mProgramFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGetProgram(int name, int property) {
        return mGL.getProgramParameteri(mProgramFactory.get(name), property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glDeleteShader(int name) {
        mGL.deleteShader(mShaderFactory.remove(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glCreateShader(int type) {
        final int id = mShader++;

        mShaderFactory.put(id, mGL.createShader(type));

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glShaderSource(int name, String source) {
        mGL.shaderSource(mShaderFactory.get(name), source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glCompileShader(int name) {
        mGL.compileShader(mShaderFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glAttachShader(int name, int shader) {
        mGL.attachShader(mProgramFactory.get(name), mShaderFactory.get(shader));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindAttribLocation(int name, int id, String attribute) {
        mGL.bindAttribLocation(mProgramFactory.get(name), id, attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int glGetUniformLocation(int name, String uniform) {
        final int id = mUniform++;

        mUniformFactory.put(id, mGL.getUniformLocation(mProgramFactory.get(name), uniform));

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String glGetProgramInfoLog(int name) {
        return mGL.getProgramInfoLog(mProgramFactory.get(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glRenderbufferStorage(int target, int format, int width, int height) {
        mGL.renderbufferStorage(target, format, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1f(int name, float i1) {
        mGL.uniform1f(mUniformFactory.get(name), i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2f(int name, float i1, float i2) {
        mGL.uniform2f(mUniformFactory.get(name), i1, i2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3f(int name, float i1, float i2, float i3) {
        mGL.uniform3f(mUniformFactory.get(name), i1, i2, i3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4f(int name, float i1, float i2, float i3, float i4) {
        mGL.uniform4f(mUniformFactory.get(name), i1, i2, i3, i4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1fv(int name, com.github.wolftein.quark.system.utility.array.Float32Array buffer) {
        mGL.uniform1fv(mUniformFactory.get(name),
                org.teavm.jso.typedarrays.Float32Array.create(buffer.<DataView>data().getBuffer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1i(int name, int i1) {
        mGL.uniform1i(mUniformFactory.get(name), i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform2i(int name, int i1, int i2) {
        mGL.uniform2i(mUniformFactory.get(name), i1, i2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform3i(int name, int i1, int i2, int i3) {
        mGL.uniform3i(mUniformFactory.get(name), i1, i2, i3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform4i(int name, int i1, int i2, int i3, int i4) {
        mGL.uniform4i(mUniformFactory.get(name), i1, i2, i3, i4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniform1iv(int name, com.github.wolftein.quark.system.utility.array.Int32Array buffer) {
        mGL.uniform1iv(mUniformFactory.get(name),
                org.teavm.jso.typedarrays.Int32Array.create(buffer.<DataView>data().getBuffer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix3fv(int name, boolean transpose, com.github.wolftein.quark.system.utility.array.Float32Array buffer) {
        mGL.uniformMatrix3fv(mUniformFactory.get(name), transpose,
                org.teavm.jso.typedarrays.Float32Array.create(buffer.<DataView>data().getBuffer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glUniformMatrix4fv(int name, boolean transpose, com.github.wolftein.quark.system.utility.array.Float32Array buffer) {
        mGL.uniformMatrix4fv(mUniformFactory.get(name), transpose,
                org.teavm.jso.typedarrays.Float32Array.create(buffer.<DataView>data().getBuffer()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void glBindFragDataLocation(int name, int index, String attribute) {
        //!
        //! WebGL doesn't support output location.
        //!
    }

    /**
     * (non-Javadoc)
     */
    protected <T extends JSObject> T getExtension(String... names) {
        T object = null;

        for (final String name : names) {
            object = (T) mGL.getExtension(name);

            if (object != null) {
                break;
            }
        }
        return object;
    }

    /**
     * (non-Javadoc)
     */
    protected boolean isExtension(String... names) {
        return getExtension(names) != null;
    }
}
