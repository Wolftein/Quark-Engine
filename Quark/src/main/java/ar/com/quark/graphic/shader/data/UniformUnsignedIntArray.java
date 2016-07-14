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

import ar.com.quark.graphic.shader.Uniform;
import ar.com.quark.graphic.shader.UniformType;
import ar.com.quark.utility.buffer.BufferFactory;
import ar.com.quark.utility.buffer.UnsignedInt32Buffer;

/**
 * <code>UniformUnsignedIntArray</code> encapsulate an {@link Uniform} for {@link UniformType#UIntArray}.
 */
public final class UniformUnsignedIntArray extends Uniform {
    private final UnsignedInt32Buffer mValue;

    /**
     * <p>Constructor</p>
     */
    public UniformUnsignedIntArray(UnsignedInt32Buffer buffer) {
        super(UniformType.UIntArray);
        mValue = buffer;

        setUpdate(CONCEPT_VALUE);
    }

    /**
     * <p>Constructor</p>
     */
    public UniformUnsignedIntArray(int capacity) {
        this(BufferFactory.allocateUnsignedInt32(capacity));
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public UnsignedInt32Buffer getValue() {
        return mValue.flip();
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an buffer
     */
    public void setValue(int[] newValue) {
        setValue(newValue, 0, newValue.length);
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an buffer
     * @param offset   the source offset of the <code>newValue</code>
     * @param length   the source length of the <code>newValue</code>
     */
    public void setValue(int[] newValue, int offset, int length) {
        mValue.clear();
        mValue.write(newValue, offset, length);
        setUpdate(CONCEPT_VALUE);
    }
}
