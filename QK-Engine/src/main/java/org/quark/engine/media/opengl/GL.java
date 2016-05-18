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
package org.quark.engine.media.opengl;

import org.quark.engine.media.opengl.shader.ShaderPipeline;
import org.quark.engine.media.opengl.shader.Uniform;
import org.quark.engine.media.opengl.storage.Buffer;
import org.quark.engine.media.opengl.storage.VertexDescriptor;
import org.quark.engine.media.opengl.texture.Texture;

import java.nio.ByteBuffer;

/**
 * <p>GLComponent</p> represent the <b>OpenGL</b> context.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface GL {
    /**
     * <p>Get the capabilities of the framework</p>
     *
     * @return the capabilities of the framework
     */
    GLCapabilities getCapabilities();

    /**
     * <p>Clear the current buffer</p>
     *
     * @param colour <code>true</code> if the color buffer should be clear, <code>false</code> otherwise
     * @param depth  <code>true</code> if the depth buffer should be clear, <code>false</code> otherwise
     */
    void clear(boolean colour, boolean depth);

    /**
     * <p>Changes the viewport's rectangle</p>
     *
     * @param x      the offset for the x coordinate of the rectangle (in px)
     * @param y      the offset for the y coordinate of the rectangle (in px)
     * @param width  the width of the rectangle (in px)
     * @param height the height of the rectangle (in px)
     */
    void viewport(int x, int y, int width, int height);

    /**
     * <p>Bind a {@link Texture}</p>
     *
     * @param texture the texture component
     *
     * @see #unbind(Texture)
     */
    void bind(Texture texture);

    /**
     * <p>Bind a {@link Texture}</p>
     *
     * @param texture the texture component
     * @param unit    the texture's unit
     *
     * @see #unbind(Texture, int)
     */
    void bind(Texture texture, int unit);

    /**
     * <p>Bind a {@link Buffer}</p>
     *
     * @param buffer the buffer component
     *
     * @see #unbind(Buffer)
     */
    void bind(Buffer buffer);

    /**
     * <p>Bind a {@link VertexDescriptor}</p>
     *
     * @param descriptor the descriptor component
     *
     * @see #unbind(VertexDescriptor)
     */
    void bind(VertexDescriptor descriptor);

    /**
     * <p>Bind a {@link ShaderPipeline}</p>
     *
     * @param pipeline the pipeline component
     *
     * @see #unbind(ShaderPipeline)
     */
    void bind(ShaderPipeline pipeline);

    /**
     * <p>Update a {@link Texture}</p>
     *
     * @param texture the texture component
     *
     * @see #bind(Texture)
     * @see #bind(Texture, int)
     */
    void update(Texture texture);

    /**
     * <p>Allocate a {@link Buffer}</p>
     *
     * @param buffer the buffer component
     *
     * @see #bind(Buffer)
     */
    void allocate(Buffer buffer);

    /**
     * <p>Allocate a {@link Buffer}</p>
     *
     * @param buffer  the buffer component
     * @param content the buffer's content
     *
     * @see #bind(Buffer)
     */
    <T extends java.nio.Buffer> void allocate(Buffer buffer, T content);

    /**
     * <p>Update a {@link VertexDescriptor}</p>
     *
     * @param descriptor the descriptor component
     *
     * @see #bind(VertexDescriptor)
     */
    void update(VertexDescriptor descriptor);

    /**
     * <p>Update a {@link ShaderPipeline}</p>
     *
     * @param pipeline the pipeline component
     */
    void update(ShaderPipeline pipeline);

    /**
     * <p>Update an {@link Uniform}</p>
     *
     * @param uniform the uniform component
     */
    void update(Uniform uniform);

    /**
     * <p>Unbind a {@link Texture}</p>
     *
     * @param texture the texture component
     *
     * @see #bind(Texture)
     */
    void unbind(Texture texture);

    /**
     * <p>Unbind a {@link Texture}</p>
     *
     * @param texture the texture component
     * @param unit    the texture's unit
     *
     * @see #bind(Texture, int)
     */
    void unbind(Texture texture, int unit);

    /**
     * <p>Unbind a {@link Buffer}</p>
     *
     * @param buffer the buffer component
     *
     * @see #bind(Buffer)
     */
    void unbind(Buffer buffer);

    /**
     * <p>Unbind a {@link VertexDescriptor}</p>
     *
     * @param descriptor the descriptor component
     *
     * @see #bind(VertexDescriptor)
     */
    void unbind(VertexDescriptor descriptor);

    /**
     * <p>Unbind a {@link ShaderPipeline}</p>
     *
     * @param pipeline the pipeline component
     *
     * @see #bind(ShaderPipeline)
     */
    void unbind(ShaderPipeline pipeline);

    /**
     * <p>Map a {@link Buffer}</p>
     * <p/>
     * Note: map the entire content of the buffer.
     *
     * @param buffer the buffer component
     *
     * @return a reference to the shared buffer expressed as <code>ByteBuffer</code>
     *
     * @see #unmap(Buffer)
     */
    ByteBuffer map(Buffer buffer);

    /**
     * <p>Map a {@link Buffer}</p>
     * <p/>
     * Note: map the entire content of the buffer.
     *
     * @param buffer the buffer component
     * @param access the buffer access
     *
     * @return a reference to the shared buffer expressed as <code>ByteBuffer</code>
     *
     * @see #unmap(Buffer)
     */
    ByteBuffer map(Buffer buffer, int access);

    /**
     * <p>Map a range of the {@link Buffer}</p>
     * <p/>
     * NOTE: Will map the given range of the buffer.
     *
     * @param buffer the buffer component
     * @param offset the offset of the range
     * @param length the length of the range
     *
     * @return a reference to the shared buffer expressed as <code>ByteBuffer</code>
     *
     * @see #unmap(Buffer)
     */
    ByteBuffer mapRange(Buffer buffer, long offset, long length);

    /**
     * <p>Map a range of the {@link Buffer}</p>
     *
     * @param buffer the buffer component
     * @param access the buffer access
     * @param offset the offset of the range
     * @param length the length of the range
     *
     * @return a reference to the shared buffer expressed as <code>ByteBuffer</code>
     *
     * @see #unmap(Buffer)
     */
    ByteBuffer mapRange(Buffer buffer, int access, long offset, long length);

    /**
     * <p>Un-map a {@link Buffer}</p>
     *
     * @param buffer the buffer component
     *
     * @see #map(Buffer)
     * @see #map(Buffer, int)
     * @see #mapRange(Buffer, int, long, long)
     * @see #mapRange(Buffer, long, long)
     */
    void unmap(Buffer buffer);

    /**
     * <p>Check whenever the given {@link Texture} is active</p>
     *
     * @param texture the texture component
     *
     * @return <code>true</code> if the texture is active, <code>false</code> otherwise
     */
    boolean isActive(Texture texture);

    /**
     * <p>Check whenever the given {@link Texture} is active</p>
     *
     * @param texture the texture component
     * @param unit    the texture's unit
     *
     * @return <code>true</code> if the texture is active, <code>false</code> otherwise
     */
    boolean isActive(Texture texture, int unit);

    /**
     * <p>Check whenever the given {@link VertexDescriptor} is bound</p>
     *
     * @param descriptor the descriptor component
     *
     * @return <code>true</code> if the descriptor is active, <code>false</code> otherwise
     */
    boolean isActive(VertexDescriptor descriptor);

    /**
     * <p>Check whenever the given {@link ShaderPipeline} is bound</p>
     *
     * @param pipeline the pipeline component
     *
     * @return <code>true</code> if the pipeline is active, <code>false</code> otherwise
     */
    boolean isActive(ShaderPipeline pipeline);
}
