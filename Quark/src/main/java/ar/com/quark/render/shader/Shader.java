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
package ar.com.quark.render.shader;

import ar.com.quark.render.Render;
import ar.com.quark.resource.AssetDescriptor;
import ar.com.quark.system.utility.Disposable;
import ar.com.quark.system.utility.Manageable;
import ar.com.quark.system.utility.emulation.Emulation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ar.com.quark.Quark.QKRender;

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
    private final List<Stage> mStages;
    private final Map<String, Attribute> mAttributes;
    private final Map<String, Uniform> mUniforms;

    /**
     * <p>Constructor</p>
     */
    public Shader(List<Stage> stages, Map<String, Attribute> attributes, Map<String, Uniform> uniforms) {
        mStages = stages;
        mAttributes = attributes;
        mUniforms = uniforms;
    }

    /**
     * <p>Get the stage of the pipeline</p>
     *
     * @return a collection that contain(s) the stage of the pipeline
     */
    public List<Stage> getStages() {
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
        final Attribute attribute = mAttributes.get(name);
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
    public Map<String, Attribute> getAttributes() {
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
        final Uniform value = mUniforms.get(name);
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
    public Map<String, Uniform> getUniforms() {
        return mUniforms;
    }

    /**
     * @see Render#create(Shader)
     */
    public void create() {
        QKRender.create(this);
    }

    /**
     * @see Manageable#delete()
     */
    @Override
    public void delete() {
        QKRender.delete(this);
    }

    /**
     * @see Render#acquire(Shader)
     */
    public void acquire() {
        QKRender.acquire(this);
    }

    /**
     * <p>Update all {@link Uniform} that belong to the shader</p>
     *
     * @see Render#update(Uniform)
     */
    public void update() {
        Emulation.forEach(mUniforms.values(), Uniform::update);
    }

    /**
     * @see Render#release(Shader)
     */
    public void release() {
        QKRender.release(this);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKRender.dispose(this);
    }

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link Shader}.
     */
    public final static class Descriptor extends AssetDescriptor {
        private final Map<String, String> mProcessor;

        /**
         * <p>Constructor</p>
         */
        public Descriptor(Map<String, String> processor) {
            super(true, true);
            mProcessor = processor;
        }

        /**
         * <p>Constructor</p>
         */
        public Descriptor() {
            this(Collections.emptyMap());
        }

        /**
         * <p>Get the pre-processor for the shader</p>
         *
         * @return the pre-processor for the shader
         */
        public Map<String, String> getProcessor() {
            return mProcessor;
        }
    }
}
