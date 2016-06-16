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

import org.quark.mathematic.Matrix3f;
import org.quark.mathematic.MutableMatrix3f;
import org.quark.render.shader.Uniform;
import org.quark.render.shader.UniformType;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.ArrayFactory;

/**
 * <code>UniformMatrix3</code> encapsulate an {@link Uniform} for {@link UniformType#Matrix3x3}.
 */
public final class UniformMatrix3 extends Uniform {
    private final Float32Array mBuffer = ArrayFactory.allocateFloat32Array(3 * 3);

    /**
     * <p>Constructor</p>
     */
    public UniformMatrix3() {
        super(UniformType.Matrix3x3);
    }

    /**
     * <p>Constructor</p>
     */
    public UniformMatrix3(Matrix3f matrix) {
        super(UniformType.Matrix3x3);
        matrix.store(mBuffer);
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public Float32Array getValue() {
        return mBuffer;
    }

    /**
     * <p>Get the value of the data to the given matrix</p>
     *
     * @param outValue the matrix to retrieve the value from
     *
     * @return the value of the data
     */
    public MutableMatrix3f getValue(MutableMatrix3f outValue) {
        return outValue.set(mBuffer);
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data
     */
    public void setValue(Matrix3f newValue) {
        mBuffer.rewind();

        newValue.store(mBuffer);
        setUpdate(CONCEPT_VALUE);
    }
}