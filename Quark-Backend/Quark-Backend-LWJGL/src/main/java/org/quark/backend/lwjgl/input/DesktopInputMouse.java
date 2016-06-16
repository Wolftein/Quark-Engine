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
package org.quark.backend.lwjgl.input;

import org.lwjgl.glfw.GLFW;
import org.quark.backend.lwjgl.utility.array.DesktopArrayFactory;
import org.quark.input.device.InputMouse;
import org.quark.input.device.InputMouseButton;
import org.quark.system.utility.array.Int32Array;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link InputMouse}.
 */
public final class DesktopInputMouse implements InputMouse {
    /**
     * Hold the mutex for allowing polling from another thread.
     */
    private final Object mLock = new Object();

    /**
     * Hold the display handle.
     */
    private final long mHandle;

    /**
     * Hold the device buffer.
     */
    private Int32Array mBuffer = DesktopArrayFactory.allocateInt32Array(1024);

    /**
     * <p>Constructor</p>
     */
    public DesktopInputMouse(long handle) {
        mHandle = handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        GLFW.glfwSetCursorPosCallback(mHandle, this::glfwCursorPosCallback);
        GLFW.glfwSetScrollCallback(mHandle, this::glfwScrollCallback);
        GLFW.glfwSetMouseButtonCallback(mHandle, this::glfwMouseButtonCallback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Int32Array buffer) {
        synchronized (mLock) {
            buffer.write(mBuffer.flip());

            //!
            //! Change the buffer mode to write-mode.
            //!
            mBuffer.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        GLFW.glfwSetCursorPosCallback(mHandle, null);
        GLFW.glfwSetScrollCallback(mHandle, null);
        GLFW.glfwSetMouseButtonCallback(mHandle, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorMode(boolean activate) {
        GLFW.glfwSetInputMode(mHandle,
                GLFW.GLFW_CURSOR, activate ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(int x, int y) {
        GLFW.glfwSetCursorPos(mHandle, x, y);
    }

    /**
     * <p>Handle GLFWCursorPosCallback</p>
     */
    private void glfwCursorPosCallback(long window, double x, double y) {
        synchronized (mLock) {
            InputMouse.onFactoryMove(mBuffer, (int) x, (int) y);
        }
    }

    /**
     * <p>Handle GLFWScrollCallback</p>
     */
    private void glfwScrollCallback(long window, double x, double y) {
        synchronized (mLock) {
            InputMouse.onFactoryWheel(mBuffer, (int) y);
        }
    }

    /**
     * <p>Handle GLFWMouseButtonCallback</p>
     */
    private void glfwMouseButtonCallback(long window, int button, int action, int mod) {
        final InputMouseButton input = transform(button);
        if (input != null) {
            if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
                synchronized (mLock) {
                    InputMouse.onFactoryButtonDown(mBuffer, input);
                }
            } else {
                synchronized (mLock) {
                    InputMouse.onFactoryButtonUp(mBuffer, input);
                }
            }
        }
    }

    /**
     * <p>Transform an implementation button code into {@link InputMouseButton}</p>
     */
    private InputMouseButton transform(int code) {
        switch (code) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT:
                return InputMouseButton.BUTTON_LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
                return InputMouseButton.BUTTON_RIGHT;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE:
                return InputMouseButton.BUTTON_MIDDLE;
        }
        return null;
    }
}
