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
package org.quark.engine.media.opengl.shader;

/**
 * <code>Attribute</code> represent an actual variable located in a {@link Shader}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Attribute {
    /**
     * Define an input <code>Attribute</code> which is being used as source.
     */
    public final static int TYPE_INPUT = 0;

    /**
     * Define an output <code>Attribute</code> which is being used as destination.
     */
    public final static int TYPE_OUTPUT = 1;

    private final int mID;
    private final int mType;

    /**
     * <code>Constructor</code>
     */
    public Attribute(int id, int type) {
        mID = id;
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
     * <p>Check whenever the given attribute is being used as input</p>
     *
     * @return <code>true</code> if the attribute is being used as input, <code>false</code> otherwise
     */
    public boolean isInput() {
        return mType == TYPE_INPUT;
    }

    /**
     * <p>Check whenever the given attribute is being used as output</p>
     *
     * @return <code>true</code> if the attribute is being used as output, <code>false</code> otherwise
     */
    public boolean isOutput() {
        return mType == TYPE_OUTPUT;
    }
}
