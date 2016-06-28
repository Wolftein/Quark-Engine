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
package org.quark.extension.niftyui;

import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.quark.input.InputListener;
import org.quark.input.device.InputKey;
import org.quark.input.device.InputKeyboard;
import org.quark.input.device.InputMouse;
import org.quark.input.device.InputMouseButton;
import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.Int32Array;

import static org.quark.Quark.QKInput;

/**
 * <code>NiftyInputSystem</code> represent implementation of {@link InputSystem}.
 */
public final class NiftyInputSystem implements InputSystem, InputListener {
    private final KeyboardInputEvent mKeyboardInputEvent = new KeyboardInputEvent();

    /**
     * Hold an array that contain(s) all event(s)
     */
    private final Int32Array mInput = ArrayFactory.allocateInt32Array(2048);

    /**
     * Hold the listener of the input-system doesn't handle the event.
     */
    private final InputListener mListener;

    /**
     * <p>Constructor</p>
     */
    public NiftyInputSystem(InputListener listener) {
        mListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
        //!
        //! NOTE: No need to implement this method.
        //!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void forwardEvents(NiftyInputConsumer consumer) {
        mInput.flip();
        {
            while (mInput.hasRemaining()) {
                switch (mInput.read()) {
                    //!
                    //! KEYBOARD
                    //!
                    case InputKeyboard.EVENT_KEY_UP:
                        onForwardKeyUp(consumer, InputKey.VALUES[mInput.read()],
                                mInput.read() > 0,
                                mInput.read() > 0);
                        break;
                    case InputKeyboard.EVENT_KEY_DOWN:
                        onForwardKeyDown(consumer, InputKey.VALUES[mInput.read()],
                                mInput.read() > 0,
                                mInput.read() > 0);
                        break;
                    case InputKeyboard.EVENT_KEY_TYPE:
                        onForwardKeyType(consumer, (char) mInput.read(), false, false);
                        break;

                    //!
                    //! MOUSE
                    //!
                    case InputMouse.EVENT_MOVE:
                        onForwardMove(consumer, mInput.read(), mInput.read(),
                                mInput.read(),
                                mInput.read());
                        break;
                    case InputMouse.EVENT_BUTTON_UP:
                        onForwardButtonUp(consumer, InputMouseButton.VALUES[mInput.read()],
                                mInput.read(),
                                mInput.read());
                        break;
                    case InputMouse.EVENT_BUTTON_DOWN:
                        onForwardButtonDown(consumer, InputMouseButton.VALUES[mInput.read()],
                                mInput.read(),
                                mInput.read());
                        break;
                    case InputMouse.EVENT_WHEEL:
                        onForwardWheel(consumer, mInput.read(), mInput.read(), mInput.read());
                        break;
                }
            }
        }
        mInput.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMousePosition(int x, int y) {
        QKInput.setCursorPosition(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onKeyboardKeyDown(InputKey key) {
        InputKeyboard.onFactoryKeyDown(mInput, key);

        mInput
                .write(QKInput.isKeyDown(InputKey.KEY_LEFT_SHIFT)
                        || QKInput.isKeyDown(InputKey.KEY_RIGHT_SHIFT) ? 1 : 0)
                .write(QKInput.isKeyDown(InputKey.KEY_LEFT_CONTROL)
                        || QKInput.isKeyDown(InputKey.KEY_RIGHT_CONTROL) ? 1 : 0);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onKeyboardKeyUp(InputKey key) {
        InputKeyboard.onFactoryKeyUp(mInput, key);

        mInput
                .write(QKInput.isKeyDown(InputKey.KEY_LEFT_SHIFT)
                        || QKInput.isKeyDown(InputKey.KEY_RIGHT_SHIFT) ? 1 : 0)
                .write(QKInput.isKeyDown(InputKey.KEY_LEFT_CONTROL)
                        || QKInput.isKeyDown(InputKey.KEY_RIGHT_CONTROL) ? 1 : 0);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onKeyboardKeyType(char key) {
        InputKeyboard.onFactoryKeyType(mInput, key);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onMouseButtonDown(int x, int y, InputMouseButton button) {
        InputMouse.onFactoryButtonDown(mInput, button);

        mInput.write(x).write(y);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onMouseButtonUp(int x, int y, InputMouseButton button) {
        InputMouse.onFactoryButtonUp(mInput, button);

        mInput.write(x).write(y);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onMouseMove(int x, int y, int dx, int dy) {
        InputMouse.onFactoryMove(mInput, x, y);

        mInput.write(dx).write(dy);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean onMouseWheel(int x, int y, int delta) {
        InputMouse.onFactoryWheel(mInput, delta);

        mInput.write(x).write(y);

        return true;
    }

    /**
     * <p>Forward {@link InputKeyboard#EVENT_KEY_DOWN}</p>
     */
    private void onForwardKeyDown(NiftyInputConsumer consumer, InputKey key, boolean isShift, boolean isControl) {
        mKeyboardInputEvent.setData(nativeToNifty(key), '\0', true, isShift, isControl);

        if (!consumer.processKeyboardEvent(mKeyboardInputEvent) && mListener != null) {
            mListener.onKeyboardKeyDown(key);
        }
    }

    /**
     * <p>Forward {@link InputKeyboard#EVENT_KEY_UP}</p>
     */
    private void onForwardKeyUp(NiftyInputConsumer consumer, InputKey key, boolean isShift, boolean isControl) {
        mKeyboardInputEvent.setData(nativeToNifty(key), '\0', false, isShift, isControl);

        if (!consumer.processKeyboardEvent(mKeyboardInputEvent) && mListener != null) {
            mListener.onKeyboardKeyUp(key);
        }
    }

    /**
     * <p>Forward {@link InputKeyboard#EVENT_KEY_TYPE}</p>
     */
    private void onForwardKeyType(NiftyInputConsumer consumer, char key, boolean isShift, boolean isControl) {
        mKeyboardInputEvent.setData(-1, key, true, isShift, isControl);

        if (!consumer.processKeyboardEvent(mKeyboardInputEvent) && mListener != null) {
            mListener.onKeyboardKeyType(key);
        }
    }

    /**
     * <p>Forward {@link InputMouse#EVENT_MOVE}</p>
     */
    private void onForwardMove(NiftyInputConsumer consumer, int x, int y, int dx, int dy) {
        if (!consumer.processMouseEvent(x, y, 0, -1, false) && mListener != null) {
            mListener.onMouseMove(x, y, dx, dy);
        }
    }

    /**
     * <p>Forward {@link InputMouse#EVENT_BUTTON_DOWN}</p>
     */
    private void onForwardButtonDown(NiftyInputConsumer consumer, InputMouseButton button, int x, int y) {
        if (!consumer.processMouseEvent(x, y, 0, nativeToNifty(button), true) && mListener != null) {
            mListener.onMouseButtonDown(x, y, button);
        }
    }

    /**
     * <p>Forward {@link InputMouse#EVENT_BUTTON_UP}</p>
     */
    private void onForwardButtonUp(NiftyInputConsumer consumer, InputMouseButton button, int x, int y) {
        if (!consumer.processMouseEvent(x, y, 0, nativeToNifty(button), false) && mListener != null) {
            mListener.onMouseButtonUp(x, y, button);
        }
    }

    /**
     * <p>Forward {@link InputMouse#EVENT_WHEEL}</p>
     */
    private void onForwardWheel(NiftyInputConsumer consumer, int delta, int x, int y) {
        if (!consumer.processMouseEvent(x, y, delta, -1, false) && mListener != null) {
            mListener.onMouseWheel(x, y, delta);
        }
    }

    /**
     * <p>Transform native input button into nifty button code</p>
     */
    private int nativeToNifty(InputMouseButton button) {
        return button.ordinal();
    }

    /**
     * <p>Transform native input key into nifty key code</p>
     */
    private int nativeToNifty(InputKey key) {
        switch (key) {
            case KEY_SPACE:
                return KeyboardInputEvent.KEY_SPACE;
            case KEY_APOSTROPHE:
                return KeyboardInputEvent.KEY_APOSTROPHE;
            case KEY_COMMA:
                return KeyboardInputEvent.KEY_COMMA;
            case KEY_MINUS:
                return KeyboardInputEvent.KEY_MINUS;
            case KEY_PERIOD:
                return KeyboardInputEvent.KEY_PERIOD;
            case KEY_SLASH:
                return KeyboardInputEvent.KEY_SLASH;
            case KEY_0:
                return KeyboardInputEvent.KEY_0;
            case KEY_1:
                return KeyboardInputEvent.KEY_1;
            case KEY_2:
                return KeyboardInputEvent.KEY_2;
            case KEY_3:
                return KeyboardInputEvent.KEY_3;
            case KEY_4:
                return KeyboardInputEvent.KEY_4;
            case KEY_5:
                return KeyboardInputEvent.KEY_5;
            case KEY_6:
                return KeyboardInputEvent.KEY_6;
            case KEY_7:
                return KeyboardInputEvent.KEY_7;
            case KEY_8:
                return KeyboardInputEvent.KEY_8;
            case KEY_9:
                return KeyboardInputEvent.KEY_9;
            case KEY_SEMICOLON:
                return KeyboardInputEvent.KEY_SEMICOLON;
            case KEY_EQUAL:
                return KeyboardInputEvent.KEY_EQUALS;
            case KEY_A:
                return KeyboardInputEvent.KEY_A;
            case KEY_B:
                return KeyboardInputEvent.KEY_B;
            case KEY_C:
                return KeyboardInputEvent.KEY_C;
            case KEY_D:
                return KeyboardInputEvent.KEY_D;
            case KEY_E:
                return KeyboardInputEvent.KEY_E;
            case KEY_F:
                return KeyboardInputEvent.KEY_F;
            case KEY_G:
                return KeyboardInputEvent.KEY_G;
            case KEY_H:
                return KeyboardInputEvent.KEY_H;
            case KEY_I:
                return KeyboardInputEvent.KEY_I;
            case KEY_J:
                return KeyboardInputEvent.KEY_J;
            case KEY_K:
                return KeyboardInputEvent.KEY_K;
            case KEY_L:
                return KeyboardInputEvent.KEY_L;
            case KEY_M:
                return KeyboardInputEvent.KEY_M;
            case KEY_N:
                return KeyboardInputEvent.KEY_N;
            case KEY_O:
                return KeyboardInputEvent.KEY_O;
            case KEY_P:
                return KeyboardInputEvent.KEY_P;
            case KEY_Q:
                return KeyboardInputEvent.KEY_Q;
            case KEY_R:
                return KeyboardInputEvent.KEY_R;
            case KEY_S:
                return KeyboardInputEvent.KEY_S;
            case KEY_T:
                return KeyboardInputEvent.KEY_T;
            case KEY_U:
                return KeyboardInputEvent.KEY_U;
            case KEY_V:
                return KeyboardInputEvent.KEY_V;
            case KEY_W:
                return KeyboardInputEvent.KEY_W;
            case KEY_X:
                return KeyboardInputEvent.KEY_X;
            case KEY_Y:
                return KeyboardInputEvent.KEY_Y;
            case KEY_Z:
                return KeyboardInputEvent.KEY_Z;
            case KEY_LEFT_BRACKET:
                return KeyboardInputEvent.KEY_LBRACKET;
            case KEY_BACKSLASH:
                return KeyboardInputEvent.KEY_BACKSLASH;
            case KEY_RIGHT_BRACKET:
                return KeyboardInputEvent.KEY_RBRACKET;
            case KEY_GRAVE_ACCENT:
                return KeyboardInputEvent.KEY_GRAVE;
            case KEY_ESCAPE:
                return KeyboardInputEvent.KEY_ESCAPE;
            case KEY_ENTER:
                return KeyboardInputEvent.KEY_RETURN;
            case KEY_TAB:
                return KeyboardInputEvent.KEY_TAB;
            case KEY_BACKSPACE:
                return KeyboardInputEvent.KEY_BACK;
            case KEY_INSERT:
                return KeyboardInputEvent.KEY_INSERT;
            case KEY_DELETE:
                return KeyboardInputEvent.KEY_DELETE;
            case KEY_RIGHT:
                return KeyboardInputEvent.KEY_RIGHT;
            case KEY_LEFT:
                return KeyboardInputEvent.KEY_LEFT;
            case KEY_DOWN:
                return KeyboardInputEvent.KEY_DOWN;
            case KEY_UP:
                return KeyboardInputEvent.KEY_UP;
            case KEY_PAGE_UP:
                return KeyboardInputEvent.KEY_PRIOR;
            case KEY_PAGE_DOWN:
                return KeyboardInputEvent.KEY_NEXT;
            case KEY_HOME:
                return KeyboardInputEvent.KEY_HOME;
            case KEY_END:
                return KeyboardInputEvent.KEY_END;
            case KEY_CAPS_LOCK:
                return KeyboardInputEvent.KEY_CAPITAL;
            case KEY_SCROLL_LOCK:
                return KeyboardInputEvent.KEY_SCROLL;
            case KEY_NUM_LOCK:
                return KeyboardInputEvent.KEY_NUMLOCK;
            case KEY_PRINT_SCREEN:
                return KeyboardInputEvent.KEY_NONE;
            case KEY_PAUSE:
                return KeyboardInputEvent.KEY_PAUSE;
            case KEY_F1:
                return KeyboardInputEvent.KEY_F1;
            case KEY_F2:
                return KeyboardInputEvent.KEY_F2;
            case KEY_F3:
                return KeyboardInputEvent.KEY_F3;
            case KEY_F4:
                return KeyboardInputEvent.KEY_F4;
            case KEY_F5:
                return KeyboardInputEvent.KEY_F5;
            case KEY_F6:
                return KeyboardInputEvent.KEY_F6;
            case KEY_F7:
                return KeyboardInputEvent.KEY_F7;
            case KEY_F8:
                return KeyboardInputEvent.KEY_F8;
            case KEY_F9:
                return KeyboardInputEvent.KEY_F9;
            case KEY_F10:
                return KeyboardInputEvent.KEY_F10;
            case KEY_F11:
                return KeyboardInputEvent.KEY_F11;
            case KEY_F12:
                return KeyboardInputEvent.KEY_F12;
            case KEY_KP_0:
                return KeyboardInputEvent.KEY_NUMPAD0;
            case KEY_KP_1:
                return KeyboardInputEvent.KEY_NUMPAD1;
            case KEY_KP_2:
                return KeyboardInputEvent.KEY_NUMPAD2;
            case KEY_KP_3:
                return KeyboardInputEvent.KEY_NUMPAD3;
            case KEY_KP_4:
                return KeyboardInputEvent.KEY_NUMPAD4;
            case KEY_KP_5:
                return KeyboardInputEvent.KEY_NUMPAD5;
            case KEY_KP_6:
                return KeyboardInputEvent.KEY_NUMPAD6;
            case KEY_KP_7:
                return KeyboardInputEvent.KEY_NUMPAD7;
            case KEY_KP_8:
                return KeyboardInputEvent.KEY_NUMPAD8;
            case KEY_KP_9:
                return KeyboardInputEvent.KEY_NUMPAD9;
            case KEY_KP_DECIMAL:
                return KeyboardInputEvent.KEY_DECIMAL;
            case KEY_KP_DIVIDE:
                return KeyboardInputEvent.KEY_DIVIDE;
            case KEY_KP_MULTIPLY:
                return KeyboardInputEvent.KEY_MULTIPLY;
            case KEY_KP_SUBTRACT:
                return KeyboardInputEvent.KEY_SUBTRACT;
            case KEY_KP_ADD:
                return KeyboardInputEvent.KEY_ADD;
            case KEY_KP_ENTER:
                return KeyboardInputEvent.KEY_NUMPADENTER;
            case KEY_KP_EQUAL:
                return KeyboardInputEvent.KEY_NUMPADEQUALS;
            case KEY_LEFT_SHIFT:
                return KeyboardInputEvent.KEY_LSHIFT;
            case KEY_LEFT_CONTROL:
                return KeyboardInputEvent.KEY_LCONTROL;
            case KEY_LEFT_ALT:
                return KeyboardInputEvent.KEY_LMENU;
            case KEY_LEFT_SUPER:
                return KeyboardInputEvent.KEY_LMETA;
            case KEY_MENU:
                return KeyboardInputEvent.KEY_LMENU;
            case KEY_RIGHT_SHIFT:
                return KeyboardInputEvent.KEY_RSHIFT;
            case KEY_RIGHT_CONTROL:
                return KeyboardInputEvent.KEY_RCONTROL;
            case KEY_RIGHT_ALT:
                return KeyboardInputEvent.KEY_RMENU;
            case KEY_RIGHT_SUPER:
                return KeyboardInputEvent.KEY_RMETA;
        }
        throw new UnsupportedOperationException("Translation of missing key");
    }
}
