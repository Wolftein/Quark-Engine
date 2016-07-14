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
package ar.com.quark.graphic.shader;

import ar.com.quark.asset.AssetDescriptor;
import ar.com.quark.graphic.Graphic;
import ar.com.quark.utility.Disposable;
import ar.com.quark.utility.Manageable;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Maps;

import static ar.com.quark.Quark.QKGraphic;

/**
 * <code>Data.Shader</code> encapsulate the programmable stage(s) of the rendering pipeline.
 * <p>
 * The rendering pipeline defines certain sections to be programmable. Each of these sections, or stages,
 * represents a particular type of programmable processing.
 * <p>
 * Each stage has a set of inputs and outputs, which are passed from prior stages and on to subsequent stages
 * (whether programmable or not).
 */
public final class Shader extends Manageable implements Disposable {
    private final ImmutableList<Stage> mStages;
    private final ImmutableMap<String, Attribute> mAttributes;
    private final ImmutableMap<String, Uniform> mUniforms;

    /**
     * <p>Constructor</p>
     */
    public Shader(
            ImmutableList<Stage> stages,
            ImmutableMap<String, Attribute> attributes,
            ImmutableMap<String, Uniform> uniforms) {
        mStages = stages;
        mAttributes = attributes;
        mUniforms = uniforms;
    }

    /**
     * <p>Get the stage of the pipeline</p>
     *
     * @return a collection that contain(s) the stage of the pipeline
     */
    public ImmutableList<Stage> getStages() {
        return mStages;
    }

    /**
     * <p>Check if the given {@link Attribute} is present</p>
     *
     * @param name the name of the attribute
     *
     * @return <code>true</code> if the attribute is present, <code>false</code> otherwise
     */
    public boolean hasAttribute(String name) {
        return mAttributes.containsKey(name);
    }

    /**
     * <p>Get an {@link Attribute} from the pipeline</p>
     *
     * @param name the name of the attribute
     *
     * @return the attribute with the given name
     */
    public Attribute getAttribute(String name) {
        final Attribute attribute = mAttributes.getIfAbsentValue(name, null);
        if (attribute == null) {
            throw new IllegalStateException(name + " attribute not present.");
        }
        return attribute;
    }

    /**
     * <p>Get the {@link Attribute}(s) of the pipeline</p>
     *
     * @return a map that contain(s) the attributes expressed as (name, attribute)
     */
    public ImmutableMap<String, Attribute> getAttributes() {
        return mAttributes;
    }

    /**
     * <p>Check if the given {@link Uniform} is present</p>
     *
     * @param name the name of the data
     *
     * @return <code>true</code> if the data is present, <code>false</code> otherwise
     */
    public boolean hasUniform(String name) {
        return mUniforms.containsKey(name);
    }

    /**
     * <p>Get an {@link Uniform} from the pipeline</p>
     *
     * @param name the name of the data
     *
     * @return the data with the given name
     */
    public <T extends Uniform> T getUniform(String name) {
        final Uniform value = mUniforms.getIfAbsentValue(name, null);
        if (value == null) {
            throw new IllegalStateException(name + " data not present.");
        }
        return (T) value;
    }

    /**
     * <p>Get the {@link Uniform}(s) of the pipeline</p>
     *
     * @return a map that contain(s) the attributes expressed as (name, <code>Uniform</code>)
     */
    public ImmutableMap<String, Uniform> getUniforms() {
        return mUniforms;
    }

    /**
     * @see Graphic#create(Shader)
     */
    public void create() {
        QKGraphic.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        QKGraphic.delete(this);
    }

    /**
     * @see Graphic#acquire(Shader)
     */
    public void acquire() {
        QKGraphic.acquire(this);
    }

    /**
     * <p>Update all {@link Uniform} that belong to the shader</p>
     *
     * @see Graphic#update(Uniform)
     */
    public void update() {
        mUniforms.forEachValue(Uniform::update);
    }

    /**
     * @see Graphic#release(Shader)
     */
    public void release() {
        QKGraphic.release(this);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKGraphic.dispose(this);
    }

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link Shader}.
     */
    public final static class Descriptor extends AssetDescriptor {
        private final ImmutableMap<String, String> mProcessor;

        /**
         * <p>Constructor</p>
         */
        public Descriptor(String filename, ImmutableMap<String, String> processor) {
            super(filename, true, true, true);
            mProcessor = processor;
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor(String filename) {
            this(filename, Maps.immutable.empty());
        }

        /**
         * <p>Get the pre-processor for the shader</p>
         *
         * @return the pre-processor for the shader
         */
        public ImmutableMap<String, String> getProcessor() {
            return mProcessor;
        }
    }
}
