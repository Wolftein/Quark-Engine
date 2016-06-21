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
package org.quark.render.shader.data;

import org.quark.render.shader.Uniform;
import org.quark.render.shader.UniformType;

/**
 * <code>UniformFloat</code> encapsulate an {@link Uniform} for {@link UniformType#Float}.
 */
public final class UniformFloat extends Uniform {
    private float mValue;

    /**
     * <p>Constructor</p>
     */
    public UniformFloat(float value) {
        super(UniformType.Float);
        setValue(value);
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public float getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data
     */
    public void setValue(float newValue) {
        if (mValue != newValue) {
            mValue = newValue;
            setUpdate(CONCEPT_VALUE);
        }
    }
}