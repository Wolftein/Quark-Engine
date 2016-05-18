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
package org.quark.engine.media.opengl;

/**
 * <p>GLComponent</p> represent an <b>OpenGL</b> object.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public abstract class GLComponent {
    /**
     * Represent the invalid handle of a component.
     */
    public final static int INVALID_HANDLE = 0;

    protected int mHandle;
    protected int mFlag;

    /**
     * <p>Constructor</p>
     * <p>
     * NOTE: Prevent constructing this class without implementation.
     */
    protected GLComponent() {
    }

    /**
     * <p>Change the unique identifier of the component</p>
     *
     * @param handle the new unique identifier of the component
     */
    public final void setHandle(int handle) {
        mHandle = handle;
    }

    /**
     * <p>Get the unique identifier of the component</p>
     *
     * @return the unique identifier of the component
     */
    public final int getHandle() {
        return mHandle;
    }

    /**
     * <p>Indicates the component has been updated</p>
     */
    public final void setUpdated() {
        mFlag = 0;
    }

    /**
     * <p>Indicates the component's concept given requires to update</p>
     *
     * @param concept the concept that requires to update
     */
    public final void setUpdate(int concept) {
        mFlag |= concept;
    }

    /**
     * <p>Check whenever the component requires update</p>
     *
     * @return <code>true</code> if the component requires update, <code>false</code> otherwise
     */
    public final boolean hasUpdate() {
        return mFlag != 0;
    }

    /**
     * <p>Check whenever the component's concept given requires update</p>
     *
     * @param concept the component's concept to be check
     *
     * @return <code>true</code> if the component's concept given requires update, <code>false</code> otherwise
     */
    public final boolean hasUpdate(int concept) {
        return (mFlag & concept) != 0;
    }
}