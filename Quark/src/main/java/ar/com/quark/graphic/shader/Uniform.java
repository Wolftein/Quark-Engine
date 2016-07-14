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

import ar.com.quark.graphic.Graphic;
import ar.com.quark.utility.Manageable;

import static ar.com.quark.Quark.QKGraphic;

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
     * @see Graphic#update(Uniform)
     */
    public final void update() {
        QKGraphic.update(this);
    }
}
