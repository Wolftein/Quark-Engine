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
import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.UInt32Array;

/**
 * <code>UniformUnsignedIntArray</code> encapsulate an {@link Uniform} for {@link UniformType#UIntArray}.
 */
public final class UniformUnsignedIntArray extends Uniform {
    private final UInt32Array mValue;

    /**
     * <p>Constructor</p>
     */
    public UniformUnsignedIntArray(UInt32Array buffer) {
        super(UniformType.UIntArray);
        mValue = buffer;
    }

    /**
     * <p>Constructor</p>
     */
    public UniformUnsignedIntArray(int capacity) {
        this(ArrayFactory.allocateUInt32Array(capacity));
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public UInt32Array getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an array
     */
    public void setValue(long[] newValue) {
        setValue(newValue, 0, newValue.length);
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an array
     * @param offset   the source offset of the <code>newValue</code>
     * @param length   the source length of the <code>newValue</code>
     */
    public void setValue(long[] newValue, int offset, int length) {
        mValue.rewind();
        mValue.write(newValue, offset, length);
        setUpdate(CONCEPT_VALUE);
    }
}
