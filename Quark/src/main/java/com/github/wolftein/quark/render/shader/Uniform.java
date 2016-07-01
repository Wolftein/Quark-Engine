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
package com.github.wolftein.quark.render.shader;

import com.github.wolftein.quark.render.Render;
import com.github.wolftein.quark.system.utility.Manageable;

import static com.github.wolftein.quark.Quark.QKRender;

/**
 * <code>Uniform</code> encapsulate a variable that resides in a {@link Stage}.
 */
public abstract class Uniform extends Manageable {
    public final static int CONCEPT_VALUE = (1 << 1);

    private final UniformType mType;

    /**
     * <p>Constructor</p>
     */
    protected Uniform(UniformType type) {
        mType = type;
    }

    /**
     * <p>Get the type of the data</p>
     *
     * @return the type of the data
     */
    public final UniformType getType() {
        return mType;
    }

    /**
     * @see Render#update(Uniform)
     */
    public final void update() {
        QKRender.update(this);
    }
}
