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
package org.quark.engine.media.opengl.shader;

import org.quark.engine.media.opengl.GLComponent;
import org.quark.engine.resource.AssetDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <code>ShaderPipeline</code> represent the programmable stages of the rendering program.
 * <p>
 * The rendering program defines certain sections to be programmable. Each of these sections, or stages,
 * represents a particular type of programmable processing.
 * <p>
 * Each stage has a set of inputs and outputs, which are passed from prior stages and on to subsequent stages
 * (whether programmable or not).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class ShaderPipeline extends GLComponent {
    private final List<Shader> mStages;
    private final Map<String, Attribute> mAttributes;
    private final Map<String, Uniform> mUniforms;

    /**
     * <p>Constructor</p>
     */
    public ShaderPipeline(List<Shader> stages, Map<String, Attribute> attributes, Map<String, Uniform> uniforms) {
        mStages = stages;
        mAttributes = attributes;
        mUniforms = uniforms;
    }

    /**
     * <p>Get the stage of the pipeline</p>
     *
     * @return a collection that contain(s) the stage of the pipeline
     */
    public List<Shader> getStages() {
        return mStages;
    }

    /**
     * <p>Get the attributes of the pipeline</p>
     *
     * @return a map that contain(s) the attributes expressed as (name, attribute)
     */
    public Map<String, Attribute> getAttributes() {
        return mAttributes;
    }

    /**
     * <p>Get the uniforms of the pipeline</p>
     *
     * @return a map that contain(s) the attributes expressed as (name, <code>Uniform</code>)
     */
    public Map<String, Uniform> getUniforms() {
        return mUniforms;
    }

    /**
     * <p>Check whenever the given attribute is present</p>
     *
     * @param name the name of the attribute
     *
     * @return <code>true</code> if the attribute is present, <code>false</code> otherwise
     */
    public boolean hasAttribute(String name) {
        return mAttributes.containsKey(name);
    }

    /**
     * <p>Check whenever the given uniform is present</p>
     *
     * @param name the name of the uniform
     *
     * @return <code>true</code> if the uniform is present, <code>false</code> otherwise
     */
    public boolean hasUniform(String name) {
        return mUniforms.containsKey(name);
    }

    /**
     * <p>Get an {@link Attribute} from the pipeline</p>
     *
     * @param name the name of the attribute
     *
     * @return the attribute with the given name
     */
    public Attribute getAttribute(String name) {
        final Attribute attribute = mAttributes.getOrDefault(name, null);
        if (attribute == null) {
            throw new IllegalStateException(name + " attribute not present.");
        }
        return attribute;
    }

    /**
     * <p>Get an {@link Uniform} from the pipeline</p>
     *
     * @param name the name of the uniform
     *
     * @return the uniform with the given name
     */
    public <T extends Uniform> T getUniform(String name) {
        final Uniform value = mUniforms.getOrDefault(name, null);
        if (value == null) {
            throw new IllegalStateException(name + " uniform not present.");
        }
        return (T) value;
    }

    /**
     * <code>Descriptor</code> encapsulate an {@link AssetDescriptor} for {@link ShaderPipeline}.
     */
    public final static class Descriptor extends AssetDescriptor {
        private final Map<String, String> mVariables;

        /**
         * <p>Default constructor</p>
         */
        public Descriptor(Map<String, String> variables) {
            super(true, true);
            mVariables = variables;
        }

        /**
         * <p>Simple Constructor</p>
         */
        public Descriptor() {
            this(Collections.emptyMap());
        }

        /**
         * <p>Get the variable(s) for the pipeline</p>
         *
         * @return a collection that contain(s) all variables(s) for the pipeline
         */
        public Map<String, String> getVariables() {
            return mVariables;
        }
    }
}
