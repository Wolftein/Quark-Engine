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
package org.quark.render.shader;

/**
 * <code>Stage</code> encapsulate an user-defined stage of a graphics processor.
 * <p>
 * Its purpose is to execute one of the programmable stages of the rendering pipeline.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class Stage {
    private final String mSource;
    private final StageType mType;

    /**
     * <p>Constructor</p>
     */
    public Stage(String source, StageType type) {
        mSource = source;
        mType = type;
    }

    /**
     * <p>Get the source of the stage</p>
     *
     * @return the source of the stage
     */
    public String getSource() {
        return mSource;
    }

    /**
     * <p>Get the type of the stage</p>
     *
     * @return the type of the stage
     */
    public StageType getType() {
        return mType;
    }
}