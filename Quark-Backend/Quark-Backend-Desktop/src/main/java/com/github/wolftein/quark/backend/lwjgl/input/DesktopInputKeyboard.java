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
package com.github.wolftein.quark.backend.lwjgl.input;

import com.github.wolftein.quark.backend.lwjgl.utility.array.DesktopArrayFactory;
import com.github.wolftein.quark.system.utility.array.Int32Array;
import org.lwjgl.glfw.GLFW;
import com.github.wolftein.quark.input.device.InputKey;
import com.github.wolftein.quark.input.device.InputKeyboard;

/**
 * Implementation for {@link InputKeyboard}.
 */
public final class DesktopInputKeyboard implements InputKeyboard {
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
    private Int32Array mBuffer = DesktopArrayFactory.allocateInt32Array(512);

    /**
     * <p>Constructor</p>
     */
    public DesktopInputKeyboard(long handle) {
        mHandle = handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        GLFW.glfwSetKeyCallback(mHandle, this::glfwKeyCallback);
        GLFW.glfwSetCharCallback(mHandle, this::glfwCharCallback);
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
        GLFW.glfwSetKeyCallback(mHandle, null);
        GLFW.glfwSetCharCallback(mHandle, null);
    }

    /**
     * <p>Handle GLFWSetKeyCallback</p>
     */
    private void glfwKeyCallback(long window, int key, int code, int action, int mod) {
        final InputKey input = transform(key);
        if (input != null) {
            if (action == GLFW.GLFW_REPEAT || action == GLFW.GLFW_PRESS) {
                synchronized (mLock) {
                    InputKeyboard.onFactoryKeyDown(mBuffer, input);
                }
            } else {
                synchronized (mLock) {
                    InputKeyboard.onFactoryKeyUp(mBuffer, input);
                }
            }
        }
    }

    /**
     * <p>Handle GLFWSetCharCallback</p>
     */
    private void glfwCharCallback(long window, int code) {
        synchronized (mLock) {
            InputKeyboard.onFactoryKeyType(mBuffer, (char) code);
        }
    }

    /**
     * <p>Transform an implementation key code into {@link InputKey}</p>
     */
    private InputKey transform(int code) {
        switch (code) {
            case GLFW.GLFW_KEY_SPACE:
                return InputKey.KEY_SPACE;
            case GLFW.GLFW_KEY_APOSTROPHE:
                return InputKey.KEY_APOSTROPHE;
            case GLFW.GLFW_KEY_COMMA:
                return InputKey.KEY_COMMA;
            case GLFW.GLFW_KEY_MINUS:
                return InputKey.KEY_MINUS;
            case GLFW.GLFW_KEY_PERIOD:
                return InputKey.KEY_PERIOD;
            case GLFW.GLFW_KEY_SLASH:
                return InputKey.KEY_SLASH;
            case GLFW.GLFW_KEY_0:
                return InputKey.KEY_0;
            case GLFW.GLFW_KEY_1:
                return InputKey.KEY_1;
            case GLFW.GLFW_KEY_2:
                return InputKey.KEY_2;
            case GLFW.GLFW_KEY_3:
                return InputKey.KEY_3;
            case GLFW.GLFW_KEY_4:
                return InputKey.KEY_4;
            case GLFW.GLFW_KEY_5:
                return InputKey.KEY_5;
            case GLFW.GLFW_KEY_6:
                return InputKey.KEY_6;
            case GLFW.GLFW_KEY_7:
                return InputKey.KEY_7;
            case GLFW.GLFW_KEY_8:
                return InputKey.KEY_8;
            case GLFW.GLFW_KEY_9:
                return InputKey.KEY_9;
            case GLFW.GLFW_KEY_SEMICOLON:
                return InputKey.KEY_SEMICOLON;
            case GLFW.GLFW_KEY_EQUAL:
                return InputKey.KEY_EQUAL;
            case GLFW.GLFW_KEY_A:
                return InputKey.KEY_A;
            case GLFW.GLFW_KEY_B:
                return InputKey.KEY_B;
            case GLFW.GLFW_KEY_C:
                return InputKey.KEY_C;
            case GLFW.GLFW_KEY_D:
                return InputKey.KEY_D;
            case GLFW.GLFW_KEY_E:
                return InputKey.KEY_E;
            case GLFW.GLFW_KEY_F:
                return InputKey.KEY_F;
            case GLFW.GLFW_KEY_G:
                return InputKey.KEY_G;
            case GLFW.GLFW_KEY_H:
                return InputKey.KEY_H;
            case GLFW.GLFW_KEY_I:
                return InputKey.KEY_I;
            case GLFW.GLFW_KEY_J:
                return InputKey.KEY_J;
            case GLFW.GLFW_KEY_K:
                return InputKey.KEY_K;
            case GLFW.GLFW_KEY_L:
                return InputKey.KEY_L;
            case GLFW.GLFW_KEY_M:
                return InputKey.KEY_M;
            case GLFW.GLFW_KEY_N:
                return InputKey.KEY_N;
            case GLFW.GLFW_KEY_O:
                return InputKey.KEY_O;
            case GLFW.GLFW_KEY_P:
                return InputKey.KEY_P;
            case GLFW.GLFW_KEY_Q:
                return InputKey.KEY_Q;
            case GLFW.GLFW_KEY_R:
                return InputKey.KEY_R;
            case GLFW.GLFW_KEY_S:
                return InputKey.KEY_S;
            case GLFW.GLFW_KEY_T:
                return InputKey.KEY_T;
            case GLFW.GLFW_KEY_U:
                return InputKey.KEY_U;
            case GLFW.GLFW_KEY_V:
                return InputKey.KEY_V;
            case GLFW.GLFW_KEY_W:
                return InputKey.KEY_W;
            case GLFW.GLFW_KEY_X:
                return InputKey.KEY_X;
            case GLFW.GLFW_KEY_Y:
                return InputKey.KEY_Y;
            case GLFW.GLFW_KEY_Z:
                return InputKey.KEY_Z;
            case GLFW.GLFW_KEY_LEFT_BRACKET:
                return InputKey.KEY_LEFT_BRACKET;
            case GLFW.GLFW_KEY_BACKSLASH:
                return InputKey.KEY_BACKSLASH;
            case GLFW.GLFW_KEY_RIGHT_BRACKET:
                return InputKey.KEY_RIGHT_BRACKET;
            case GLFW.GLFW_KEY_GRAVE_ACCENT:
                return InputKey.KEY_GRAVE_ACCENT;
            case GLFW.GLFW_KEY_ESCAPE:
                return InputKey.KEY_ESCAPE;
            case GLFW.GLFW_KEY_ENTER:
                return InputKey.KEY_ENTER;
            case GLFW.GLFW_KEY_TAB:
                return InputKey.KEY_TAB;
            case GLFW.GLFW_KEY_BACKSPACE:
                return InputKey.KEY_BACKSPACE;
            case GLFW.GLFW_KEY_INSERT:
                return InputKey.KEY_INSERT;
            case GLFW.GLFW_KEY_DELETE:
                return InputKey.KEY_DELETE;
            case GLFW.GLFW_KEY_RIGHT:
                return InputKey.KEY_RIGHT;
            case GLFW.GLFW_KEY_LEFT:
                return InputKey.KEY_LEFT;
            case GLFW.GLFW_KEY_DOWN:
                return InputKey.KEY_DOWN;
            case GLFW.GLFW_KEY_UP:
                return InputKey.KEY_UP;
            case GLFW.GLFW_KEY_PAGE_UP:
                return InputKey.KEY_PAGE_UP;
            case GLFW.GLFW_KEY_PAGE_DOWN:
                return InputKey.KEY_PAGE_DOWN;
            case GLFW.GLFW_KEY_HOME:
                return InputKey.KEY_HOME;
            case GLFW.GLFW_KEY_END:
                return InputKey.KEY_END;
            case GLFW.GLFW_KEY_CAPS_LOCK:
                return InputKey.KEY_CAPS_LOCK;
            case GLFW.GLFW_KEY_SCROLL_LOCK:
                return InputKey.KEY_SCROLL_LOCK;
            case GLFW.GLFW_KEY_NUM_LOCK:
                return InputKey.KEY_NUM_LOCK;
            case GLFW.GLFW_KEY_PRINT_SCREEN:
                return InputKey.KEY_PRINT_SCREEN;
            case GLFW.GLFW_KEY_PAUSE:
                return InputKey.KEY_PAUSE;
            case GLFW.GLFW_KEY_F1:
                return InputKey.KEY_F1;
            case GLFW.GLFW_KEY_F2:
                return InputKey.KEY_F2;
            case GLFW.GLFW_KEY_F3:
                return InputKey.KEY_F3;
            case GLFW.GLFW_KEY_F4:
                return InputKey.KEY_F4;
            case GLFW.GLFW_KEY_F5:
                return InputKey.KEY_F5;
            case GLFW.GLFW_KEY_F6:
                return InputKey.KEY_F6;
            case GLFW.GLFW_KEY_F7:
                return InputKey.KEY_F7;
            case GLFW.GLFW_KEY_F8:
                return InputKey.KEY_F8;
            case GLFW.GLFW_KEY_F9:
                return InputKey.KEY_F9;
            case GLFW.GLFW_KEY_F10:
                return InputKey.KEY_F10;
            case GLFW.GLFW_KEY_F11:
                return InputKey.KEY_F11;
            case GLFW.GLFW_KEY_F12:
                return InputKey.KEY_F12;
            case GLFW.GLFW_KEY_KP_0:
                return InputKey.KEY_KP_0;
            case GLFW.GLFW_KEY_KP_1:
                return InputKey.KEY_KP_1;
            case GLFW.GLFW_KEY_KP_2:
                return InputKey.KEY_KP_2;
            case GLFW.GLFW_KEY_KP_3:
                return InputKey.KEY_KP_3;
            case GLFW.GLFW_KEY_KP_4:
                return InputKey.KEY_KP_4;
            case GLFW.GLFW_KEY_KP_5:
                return InputKey.KEY_KP_5;
            case GLFW.GLFW_KEY_KP_6:
                return InputKey.KEY_KP_6;
            case GLFW.GLFW_KEY_KP_7:
                return InputKey.KEY_KP_7;
            case GLFW.GLFW_KEY_KP_8:
                return InputKey.KEY_KP_8;
            case GLFW.GLFW_KEY_KP_9:
                return InputKey.KEY_KP_9;
            case GLFW.GLFW_KEY_KP_DECIMAL:
                return InputKey.KEY_KP_DECIMAL;
            case GLFW.GLFW_KEY_KP_DIVIDE:
                return InputKey.KEY_KP_DIVIDE;
            case GLFW.GLFW_KEY_KP_MULTIPLY:
                return InputKey.KEY_KP_MULTIPLY;
            case GLFW.GLFW_KEY_KP_SUBTRACT:
                return InputKey.KEY_KP_SUBTRACT;
            case GLFW.GLFW_KEY_KP_ADD:
                return InputKey.KEY_KP_ADD;
            case GLFW.GLFW_KEY_KP_ENTER:
                return InputKey.KEY_KP_ENTER;
            case GLFW.GLFW_KEY_KP_EQUAL:
                return InputKey.KEY_KP_EQUAL;
            case GLFW.GLFW_KEY_LEFT_SHIFT:
                return InputKey.KEY_LEFT_SHIFT;
            case GLFW.GLFW_KEY_LEFT_CONTROL:
                return InputKey.KEY_LEFT_CONTROL;
            case GLFW.GLFW_KEY_LEFT_ALT:
                return InputKey.KEY_LEFT_ALT;
            case GLFW.GLFW_KEY_LEFT_SUPER:
                return InputKey.KEY_LEFT_SUPER;
            case GLFW.GLFW_KEY_MENU:
                return InputKey.KEY_MENU;
            case GLFW.GLFW_KEY_RIGHT_SHIFT:
                return InputKey.KEY_RIGHT_SHIFT;
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
                return InputKey.KEY_RIGHT_CONTROL;
            case GLFW.GLFW_KEY_RIGHT_ALT:
                return InputKey.KEY_RIGHT_ALT;
            case GLFW.GLFW_KEY_RIGHT_SUPER:
                return InputKey.KEY_RIGHT_SUPER;
        }
        return null;
    }
}
