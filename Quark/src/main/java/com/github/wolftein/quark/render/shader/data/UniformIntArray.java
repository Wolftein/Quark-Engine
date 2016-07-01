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
package com.github.wolftein.quark.render.shader.data;

import com.github.wolftein.quark.render.shader.Uniform;
import com.github.wolftein.quark.render.shader.UniformType;
import com.github.wolftein.quark.system.utility.array.Int32Array;
import com.github.wolftein.quark.system.utility.array.ArrayFactory;

/**
 * <code>UniformIntArray</code> encapsulate an {@link Uniform} for {@link UniformType#IntArray}.
 */
public final class UniformIntArray extends Uniform {
    private final Int32Array mValue;

    /**
     * <p>Constructor from a buffer</p>
     */
    public UniformIntArray(Int32Array buffer) {
        super(UniformType.IntArray);
        mValue = buffer;

        setUpdate(CONCEPT_VALUE);
    }

    /**
     * <p>Constructor</p>
     */
    public UniformIntArray(int capacity) {
        this(ArrayFactory.allocateInt32Array(capacity));
    }

    /**
     * <p>Get the value of the data</p>
     *
     * @return the value of the data
     */
    public Int32Array getValue() {
        return mValue;
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an array
     */
    public void setValue(int[] newValue) {
        setValue(newValue, 0, newValue.length);
    }

    /**
     * <p>Change the value of the data</p>
     *
     * @param newValue the new value of the data from an array
     * @param offset   the source offset of the <code>newValue</code>
     * @param length   the source length of the <code>newValue</code>
     */
    public void setValue(int[] newValue, int offset, int length) {
        mValue.rewind();
        mValue.write(newValue, offset, length);
        setUpdate(CONCEPT_VALUE);
    }
}
