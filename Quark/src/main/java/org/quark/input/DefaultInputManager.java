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
package org.quark.input;

import org.quark.input.device.InputKey;
import org.quark.input.device.InputKeyboard;
import org.quark.input.device.InputMouse;
import org.quark.input.device.InputMouseButton;
import org.quark.system.utility.array.ArrayFactory;
import org.quark.system.utility.array.Int32Array;

import java.util.HashSet;
import java.util.Set;

/**
 * <b>Default</b> implementation for {@link InputManager}.
 */
public final class DefaultInputManager implements InputManager {
    /**
     * Hold all raw input listener of the manager.
     */
    private final Set<InputListener> mListener = new HashSet<>();

    /**
     * Hold the keyboard device (Can only be one).
     */
    private InputKeyboard mKeyboard;

    /**
     * Hold the mouse device (Can only be one).
     */
    private InputMouse mMouse;

    private int mCursorX;
    private int mCursorY;
    private final boolean[] mKey = new boolean[InputKey.VALUES.length];
    private final boolean[] mButton = new boolean[InputMouseButton.VALUES.length];

    /**
     * Hold all the event(s) from the device(s) efficiently.
     */
    private final Int32Array mBuffer = ArrayFactory.allocateInt32Array(1024);

    /**
     * <p>Handle when the module initialise</p>
     *
     * @param keyboard the keyboard implementation
     * @param mouse    the mouse implementation
     */
    public void onModuleCreate(InputKeyboard keyboard, InputMouse mouse) {
        //!
        //! Initialise the <code>Keyboard</code> device
        //!
        mKeyboard = keyboard;
        mKeyboard.create();

        //!
        //! Initialise the <code>Mouse</code> device
        //!
        mMouse = mouse;
        mMouse.create();
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
        //!
        //! Destroy the <code>Keyboard</code> device
        //!
        mKeyboard.destroy();

        //!
        //! Destroy the <code>Mouse</code> device
        //!
        mMouse.destroy();
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
        //!
        //! Update <code>Keyboard</code> device.
        //!
        mKeyboard.update(mBuffer);

        //!
        //! Update <code>Mouse</code> device.
        //!
        mMouse.update(mBuffer);

        //!
        //! Process all input-event being queue by all device(s) attached.
        //!
        onProcessInputEvent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invoke(int[] event) {
        switch (event[0]) {
            //!
            //! KEYBOARD
            //!
            case InputKeyboard.EVENT_KEY_UP:
                onKeyboardKeyUp(InputKey.VALUES[event[1]]);
                break;
            case InputKeyboard.EVENT_KEY_DOWN:
                onKeyboardKeyDown(InputKey.VALUES[event[1]]);
                break;
            case InputKeyboard.EVENT_KEY_TYPE:
                onKeyboardKeyType((char) event[1]);
                break;

            //!
            //! MOUSE
            //!
            case InputMouse.EVENT_MOVE:
                onMouseMove(event[1], event[2]);
                break;
            case InputMouse.EVENT_BUTTON_UP:
                onMouseButtonUp(InputMouseButton.VALUES[event[1]]);
                break;
            case InputMouse.EVENT_BUTTON_DOWN:
                onMouseButtonDown(InputMouseButton.VALUES[event[1]]);
                break;
            case InputMouse.EVENT_WHEEL:
                onMouseWheel(event[1]);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInputListener(InputListener listener) {
        mListener.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeInputListener(InputListener listener) {
        mListener.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorMode(boolean activate) {
        mMouse.setCursorMode(activate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(int x, int y) {
        mMouse.setCursorPosition(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorX() {
        return mCursorX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorY() {
        return mCursorY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isButtonUp(InputMouseButton button) {
        return !mButton[button.ordinal()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isButtonDown(InputMouseButton button) {
        return mButton[button.ordinal()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isKeyUp(InputKey key) {
        return !mKey[key.ordinal()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isKeyDown(InputKey key) {
        return mKey[key.ordinal()];
    }

    /**
     * <p>Process all input-event(s)</p>
     */
    private void onProcessInputEvent() {
        mBuffer.flip();
        while (mBuffer.hasRemaining()) {
            switch (mBuffer.read()) {
                //!
                //! KEYBOARD
                //!
                case InputKeyboard.EVENT_KEY_UP:
                    onKeyboardKeyUp(InputKey.VALUES[mBuffer.read()]);
                    break;
                case InputKeyboard.EVENT_KEY_DOWN:
                    onKeyboardKeyDown(InputKey.VALUES[mBuffer.read()]);
                    break;
                case InputKeyboard.EVENT_KEY_TYPE:
                    onKeyboardKeyType((char) mBuffer.read());
                    break;

                //!
                //! MOUSE
                //!
                case InputMouse.EVENT_MOVE:
                    onMouseMove(mBuffer.read(), mBuffer.read());
                    break;
                case InputMouse.EVENT_BUTTON_UP:
                    onMouseButtonUp(InputMouseButton.VALUES[mBuffer.read()]);
                    break;
                case InputMouse.EVENT_BUTTON_DOWN:
                    onMouseButtonDown(InputMouseButton.VALUES[mBuffer.read()]);
                    break;
                case InputMouse.EVENT_WHEEL:
                    onMouseWheel(mBuffer.read());
                    break;
            }
        }
        mBuffer.clear();
    }

    /**
     * <p>Handle {@link InputKeyboard#EVENT_KEY_UP}</p>
     */
    private void onKeyboardKeyUp(InputKey key) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onKeyboardKeyUp(key))
                break;
        }
        mKey[key.ordinal()] = false;
    }

    /**
     * <p>Handle {@link InputKeyboard#EVENT_KEY_DOWN}</p>
     */
    private void onKeyboardKeyDown(InputKey key) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onKeyboardKeyDown(key))
                break;
        }
        mKey[key.ordinal()] = true;
    }

    /**
     * <p>Handle {@link InputKeyboard#EVENT_KEY_TYPE}</p>
     */
    private void onKeyboardKeyType(char key) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onKeyboardKeyType(key))
                break;
        }
    }

    /**
     * <p>Handle {@link InputMouse#EVENT_MOVE}</p>
     */
    private void onMouseMove(int x, int y) {
        final int dx = mCursorX - x;
        final int dy = mCursorY - y;

        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onMouseMove(x, y, dx, dy))
                break;
        }
        mCursorX = x;
        mCursorY = y;
    }

    /**
     * <p>Handle {@link InputMouse#EVENT_BUTTON_UP}</p>
     */
    private void onMouseButtonUp(InputMouseButton button) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onMouseButtonUp(mCursorX, mCursorY, button))
                break;
        }
        mButton[button.ordinal()] = false;
    }

    /**
     * <p>Handle {@link InputMouse#EVENT_BUTTON_DOWN}</p>
     */
    private void onMouseButtonDown(InputMouseButton button) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onMouseButtonDown(mCursorX, mCursorY, button))
                break;
        }
        mButton[button.ordinal()] = true;
    }

    /**
     * <p>Handle {@link InputMouse#EVENT_WHEEL}</p>
     */
    private void onMouseWheel(int delta) {
        for (final InputListener listener : mListener) {
            //!
            //! Check if the listener has consume the event.
            //!
            if (!listener.onMouseWheel(mCursorX, mCursorY, delta))
                break;
        }
    }
}
