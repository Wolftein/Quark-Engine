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

import org.quark.engine.media.opengl.shader.Uniform;
import org.quark.engine.media.opengl.shader.UniformType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * <code>UniformUnsignedIntArray</code> represent an {@link Uniform} for {@link UniformType#UnsignedIntegerArray}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class UniformUnsignedIntArray extends Uniform {
    private final IntBuffer mValue;

    /**
     * <p>Default constructor</p>
     */
    public UniformUnsignedIntArray(IntBuffer buffer) {
        super(UniformType.UnsignedIntegerArray);
        mValue = buffer;
    }

    /**
     * <p>Empty constructor</p>
     */
    public UniformUnsignedIntArray(int capacity) {
        this(ByteBuffer.allocateDirect(4 * capacity).order(ByteOrder.nativeOrder()).asIntBuffer());
    }

    /**
     * <p>Get the value of the uniform</p>
     *
     * @return the value of the uniform
     */
    public IntBuffer getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the uniform</p>
     *
     * @param newValue the new value of the uniform from an array
     */
    public void setValue(int[] newValue) {
        setValue(newValue, 0, newValue.length);
    }

    /**
     * <p>Change the value of the uniform</p>
     *
     * @param newValue the new value of the uniform from an array
     * @param offset   the source offset of the <code>newValue</code>
     * @param length   the source length of the <code>newValue</code>
     */
    public void setValue(int[] newValue, int offset, int length) {
        mValue.rewind();
        mValue.put(newValue, offset, length);
        setUpdate(CONCEPT_VALUE);
    }
}
