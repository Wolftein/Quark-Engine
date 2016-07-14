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
package ar.com.quark.graphic.shader.data;

import ar.com.quark.mathematic.Matrix4f;
import ar.com.quark.mathematic.MutableMatrix4f;
import ar.com.quark.graphic.shader.Uniform;
import ar.com.quark.graphic.shader.UniformType;
import ar.com.quark.utility.buffer.BufferFactory;
import ar.com.quark.utility.buffer.Float32Buffer;

/**
 * <code>UniformMatrix4</code> encapsulate an {@link Uniform} for {@link UniformType#Matrix4x4}.
 */
public final class UniformMatrix4 extends Uniform {
    private final Float32Buffer mBuffer = BufferFactory.allocateFloat32(4 * 4);

    /**
     * <p>Constructor</p>
     */
    public UniformMatrix4() {
        super(UniformType.Matrix4x4);
    }

    /**
     * <p>Constructor</p>
     */
    public UniformMatrix4(Matrix4f matrix) {
        super(UniformType.Matrix4x4);
        matrix.store(mBuffer);

        setUpdate(CONCEPT_VALUE);
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public Float32Buffer getValue() {
        return mBuffer.flip();
    }

    /**
     * <p>Get the value of the data to the given <code>Matrix3f</code></p>
     *
     * @param outValue the <code>Matrix3f</code> to retrieve the value from
     *
     * @return the value of the data
     */
    public MutableMatrix4f getValue(MutableMatrix4f outValue) {
        return outValue.set(mBuffer);
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data
     */
    public void setValue(Matrix4f newValue) {
        mBuffer.rewind();

        newValue.store(mBuffer);

        setUpdate(CONCEPT_VALUE);
    }
}