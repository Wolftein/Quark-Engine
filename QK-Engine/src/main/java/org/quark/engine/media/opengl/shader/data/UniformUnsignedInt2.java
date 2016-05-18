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
package org.quark.engine.media.opengl.shader.data;

import org.quark.engine.mathematic.MutableVector2i;
import org.quark.engine.mathematic.Vector2i;
import org.quark.engine.media.opengl.shader.Uniform;
import org.quark.engine.media.opengl.shader.UniformType;

/**
 * <code>UniformUnsignedInt2</code> represent an {@link Uniform} for {@link UniformType#UnsignedInteger2}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class UniformUnsignedInt2 extends Uniform {
    private final MutableVector2i mValue = MutableVector2i.zero();

    /**
     * <p>Default constructor</p>
     */
    public UniformUnsignedInt2(Vector2i value) {
        super(UniformType.UnsignedInteger2);
        setValue(value);
    }

    /**
     * <p>Empty constructor</p>
     */
    public UniformUnsignedInt2() {
        super(UniformType.UnsignedInteger2);
    }

    /**
     * <p>Get the value of the uniform</p>
     *
     * @return the value of the uniform
     */
    public Vector2i getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the uniform</p>
     *
     * @param newValue the new value of the uniform
     */
    public void setValue(Vector2i newValue) {
        setValue(newValue.getX(), newValue.getY());
    }

    /**
     * <p>Change the value of the uniform</p>
     *
     * @param newX the new x value of the uniform
     * @param newY the new y value of the uniform
     */
    public void setValue(int newX, int newY) {
        if (mValue.getX() != newX || mValue.getY() != newY) {
            mValue.setXY(newX, newY);
            setUpdate(CONCEPT_VALUE);
        }
    }
}
