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
package ar.com.quark.render.shader;

/**
 * <code>Attribute</code> encapsulate an (input or output) {@link Stage} variable.
 */
public final class Attribute {
    /**
     * Define an input <code>Attribute</code> which is being used as source.
     */
    public final static int MODE_INPUT = 0;

    /**
     * Define an output <code>Attribute</code> which is being used as destination.
     */
    public final static int MODE_OUTPUT = 1;

    private final int mID;
    private final int mMode;
    private final AttributeType mType;

    /**
     * <p>Constructor</p>
     */
    public Attribute(int id, int mode, AttributeType type) {
        mID = id;
        mMode = mode;
        mType = type;
    }

    /**
     * <p>Get the unique identifier of the attribute</p>
     *
     * @return the unique identifier of the attribute
     */
    public int getID() {
        return mID;
    }

    /**
     * <p>Get the type of the attribute</p>
     *
     * @return the type of the attribute
     */
    public AttributeType getType() {
        return mType;
    }

    /**
     * <p>Check if the given attribute is being used as input</p>
     *
     * @return <code>true</code> if the attribute is being used as input, <code>false</code> otherwise
     */
    public boolean isInput() {
        return mMode == MODE_INPUT;
    }

    /**
     * <p>Check if the given attribute is being used as output</p>
     *
     * @return <code>true</code> if the attribute is being used as output, <code>false</code> otherwise
     */
    public boolean isOutput() {
        return mMode == MODE_OUTPUT;
    }
}
