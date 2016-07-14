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
package ar.com.quark.utility;

/**
 * <code>Manageable</code> encapsulate an manageable object.
 */
public abstract class Manageable {
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
    protected Manageable() {
    }

    /**
     * <p>Handle when the object requires to delete</p>
     */
    public void delete() {
        deleteAllMemory();
    }

    /**
     * <p>Handle when the object requires to delete bound memory (CPU-side memory)</p>
     */
    public void deleteAllMemory() {
    }

    /**
     * <p>Change the unique identifier of the component</p>
     *
     * @param handle the new unique identifier of the component
     *
     * @return the previous unique identifier of the component
     */
    public final int setHandle(int handle) {
        final int lastHandle = mHandle;
        mHandle = handle;
        return lastHandle;
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
    protected final void setUpdate(int concept) {
        mFlag |= concept;
    }

    /**
     * <p>Check if the component requires update</p>
     *
     * @return <code>true</code> if the component requires update, <code>false</code> otherwise
     */
    public final boolean hasUpdate() {
        return mFlag != 0;
    }

    /**
     * <p>Check if the component's concept given requires update</p>
     *
     * @param concept the component's concept to be check
     *
     * @return <code>true</code> if the component's concept given requires update, <code>false</code> otherwise
     */
    public final boolean hasUpdate(int concept) {
        return (mFlag & concept) != 0;
    }
}