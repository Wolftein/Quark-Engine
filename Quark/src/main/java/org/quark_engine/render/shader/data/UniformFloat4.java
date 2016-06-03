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
package org.quark_engine.render.shader.data;

import org.quark_engine.mathematic.MutableVector4f;
import org.quark_engine.mathematic.Quaternionf;
import org.quark_engine.mathematic.Vector4f;
import org.quark_engine.render.shader.Uniform;
import org.quark_engine.render.shader.UniformType;

/**
 * <code>UniformFloat4</code> encapsulate an {@link Uniform} for {@link UniformType#Float4}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class UniformFloat4 extends Uniform {
    private final MutableVector4f mValue = MutableVector4f.zero();

    /**
     * <p>Constructor</p>
     */
    public UniformFloat4(Vector4f value) {
        super(UniformType.Float4);
        setValue(value);
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public Vector4f getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data
     */
    public void setValue(Vector4f newValue) {
        setValue(newValue.getX(), newValue.getY(), newValue.getZ(), newValue.getW());
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data
     */
    public void setValue(Quaternionf newValue) {
        setValue(newValue.getX(), newValue.getY(), newValue.getZ(), newValue.getW());
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newX the new x value of the data
     * @param newY the new y value of the data
     * @param newZ the new z value of the data
     * @param newW the new w value of the data
     */
    public void setValue(float newX, float newY, float newZ, float newW) {
        if (mValue.getX() != newX || mValue.getY() != newY || mValue.getZ() != newZ || mValue.getW() != newW) {
            mValue.setXYZW(newX, newY, newZ, newW);
            setUpdate(CONCEPT_VALUE);
        }
    }
}
