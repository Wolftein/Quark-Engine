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
package org.quark_engine.render;

import org.quark_engine.render.shader.Shader;
import org.quark_engine.render.shader.Uniform;
import org.quark_engine.render.storage.Primitive;
import org.quark_engine.render.storage.Storage;
import org.quark_engine.render.storage.VertexDescriptor;
import org.quark_engine.render.storage.VertexFormat;
import org.quark_engine.render.texture.Texture;
import org.quark_engine.system.utility.ManageableManager;

import java.nio.Buffer;

/**
 * <code>Render</code> encapsulate the render module.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface Render extends ManageableManager {
    /**
     * <p>Get the capabilities</p>
     *
     * @return the capabilities that contain(s) version and supported extension(s).
     */
    RenderCapabilities getCapabilities();

    /**
     * <p>Apply new states</p>
     *
     * @param states the new states (that should apply) of the render
     */
    void apply(RenderState states);

    /**
     * <p>Clear the current buffer</p>
     *
     * @param colour  <code>true</code> if the color colour buffer should be clear, <code>false</code> otherwise
     * @param depth   <code>true</code> if the depth depth buffer should be clear, <code>false</code> otherwise
     * @param stencil <code>true</code> if the depth stencil buffer should be clear, <code>false</code> otherwise
     */
    void clear(boolean colour, boolean depth, boolean stencil);

    /**
     * <p>Changes the viewport's rectangle</p>
     *
     * @param x      the offset for the x coordinate of the rectangle (in pixel)
     * @param y      the offset for the y coordinate of the rectangle (in pixel)
     * @param width  the width of the rectangle (in pixel)
     * @param height the height of the rectangle (in pixel)
     */
    void viewport(int x, int y, int width, int height);

    /**
     * <p>Check if the given <code>Texture</code> is active</p>
     *
     * @param texture the texture
     *
     * @return <code>true</code> if the texture is active, <code>false</code> otherwise
     */
    boolean isActive(Texture texture);

    /**
     * <p>Check if the given <code>Texture</code> is active</p>
     *
     * @param texture the texture
     * @param stage   the texture stage
     *
     * @return <code>true</code> if the texture is active, <code>false</code> otherwise
     */
    boolean isActive(Texture texture, int stage);

    /**
     * <p>Check if the given <code>Storage</code> is active</p>
     *
     * @param storage the storage
     *
     * @return <code>true</code> if the storage is active, <code>false</code> otherwise
     */
    boolean isActive(Storage<?> storage);

    /**
     * <p>Check if the given <code>Shader</code> is active</p>
     *
     * @param shader the shader
     *
     * @return <code>true</code> if the shader is active, <code>false</code> otherwise
     */
    boolean isActive(Shader shader);

    /**
     * <p>Check if the given <code>VertexDescriptor</code> is active</p>
     *
     * @param descriptor the descriptor
     *
     * @return <code>true</code> if the descriptor is active, <code>false</code> otherwise
     */
    boolean isActive(VertexDescriptor descriptor);

    /**
     * <p>Unbind the <code>Texture</code> given</p>
     *
     * @param texture the texture
     */
    void create(Texture texture);

    /**
     * <p>Unbind the <code>Storage</code> given</p>
     *
     * @param storage the storage
     */
    void create(Storage<?> storage);

    /**
     * <p>Unbind the <code>Shader</code> given</p>
     *
     * @param shader the shader
     */
    void create(Shader shader);

    /**
     * <p>Unbind the <code>VertexDescriptor</code> given</p>
     *
     * @param descriptor the descriptor
     */
    void create(VertexDescriptor descriptor);

    /**
     * <p>Delete the <code>Texture</code> given</p>
     *
     * @param texture the texture
     */
    void delete(Texture texture);

    /**
     * <p>Delete the <code>Storage</code> given</p>
     *
     * @param storage the storage
     */
    void delete(Storage<?> storage);

    /**
     * <p>Delete the <code>Shader</code> given</p>
     *
     * @param shader the shader
     */
    void delete(Shader shader);

    /**
     * <p>Delete the <code>VertexDescriptor</code> given</p>
     *
     * @param descriptor the descriptor
     */
    void delete(VertexDescriptor descriptor);

    /**
     * <p>Bind the <code>Texture</code> given</p>
     *
     * @param texture the texture
     */
    void acquire(Texture texture);

    /**
     * <p>Bind the <code>Texture</code> given</p>
     *
     * @param texture the texture
     * @param stage   the texture stage
     */
    void acquire(Texture texture, int stage);

    /**
     * <p>Bind the <code>Storage</code> given</p>
     *
     * @param storage the storage
     */
    void acquire(Storage<?> storage);

    /**
     * <p>Bind the <code>Shader</code> given</p>
     *
     * @param shader the shader
     */
    void acquire(Shader shader);

    /**
     * <p>Bind the <code>VertexDescriptor</code> given</p>
     *
     * @param descriptor the descriptor
     */
    void acquire(VertexDescriptor descriptor);

    /**
     * <p>Update the <code>Texture</code> given</p>
     *
     * @param texture the texture
     */
    void update(Texture texture);

    /**
     * <p>Update the <code>Storage</code> given</p>
     *
     * @param storage the storage
     */
    void update(Storage<?> storage);

    /**
     * <p>Update the <code>Uniform</code> given</p>
     *
     * @param uniform the data
     */
    void update(Uniform uniform);

    /**
     * <p>Update the <code>VertexDescriptor</code> given</p>
     *
     * @param descriptor the data
     */
    void update(VertexDescriptor descriptor);

    /**
     * <p>Unbind the <code>Texture</code> given</p>
     *
     * @param texture the texture
     */
    void release(Texture texture);

    /**
     * <p>Unbind the <code>Texture</code> given</p>
     *
     * @param texture the texture
     * @param stage   the texture stage
     */
    void release(Texture texture, int stage);

    /**
     * <p>Unbind the <code>Storage</code> given</p>
     *
     * @param storage the storage
     */
    void release(Storage<?> storage);

    /**
     * <p>Unbind the <code>Shader</code> given</p>
     *
     * @param shader the shader
     */
    void release(Shader shader);

    /**
     * <p>Unbind the <code>VertexDescriptor</code> given</p>
     *
     * @param descriptor the descriptor
     */
    void release(VertexDescriptor descriptor);

    /**
     * <p>Perform a draw operation</p>
     *
     * @param primitive the action's primitive
     * @param offset    the action's offset
     * @param count     the action's count
     */
    void draw(Primitive primitive, long offset, long count);

    /**
     * <p>Perform a draw operation using element(s)</p>
     *
     * @param primitive the action primitive
     * @param offset    the action offset
     * @param count     the action count
     * @param format    the action vertex's format(s)
     */
    void draw(Primitive primitive, long offset, long count, VertexFormat format);

    /**
     * <p>Map a <code>Storage</code></p>
     *
     * @param storage the storage
     *
     * @return a reference to the shared buffer expressed as <code>ByteBuffer</code>
     *
     * @see #unmap(Storage)
     */
    <T extends Buffer> T map(Storage<T> storage);

    /**
     * <p>Map a <code>Storage</code></p>
     *
     * @param storage the storage
     * @param access  the storage access
     *
     * @return a reference to the shared storage expressed as <code>T</code>
     *
     * @see #unmap(Storage)
     */
    <T extends Buffer> T map(Storage<T> storage, int access);

    /**
     * <p>Map a range of the <code>Storage</code></p>
     *
     * @param storage the storage
     * @param offset  the offset of the range
     * @param length  the length of the range
     *
     * @return a reference to the shared storage expressed as <code>T</code>
     *
     * @see #unmap(Storage)
     */
    <T extends Buffer> T map(Storage<T> storage, long offset, long length);

    /**
     * <p>Map a range of the <code>Storage</code></p>
     *
     * @param storage the storage
     * @param access  the buffer access
     * @param offset  the offset of the range
     * @param length  the length of the range
     *
     * @return a reference to the shared storage expressed as <code>T</code>
     *
     * @see #unmap(Storage)
     */
    <T extends Buffer> T map(Storage<T> storage, int access, long offset, long length);

    /**
     * <p>Un-map a <code>Storage</code></p>
     *
     * @param storage the storage
     *
     * @see #map(Storage)
     * @see #map(Storage, int)
     * @see #map(Storage, int, long, long)
     * @see #map(Storage, long, long)
     */
    <T extends Buffer> void unmap(Storage<T> storage);
}