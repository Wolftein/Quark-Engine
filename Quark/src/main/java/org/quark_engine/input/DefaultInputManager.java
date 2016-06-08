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
package org.quark_engine.input;

import org.quark_engine.input.device.InputKey;
import org.quark_engine.input.device.InputKeyboard;
import org.quark_engine.input.device.InputMouse;
import org.quark_engine.input.device.InputMouseButton;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation for {@link InputManager}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class DefaultInputManager implements InputManager {
    /**
     * Hold all raw input listener of the manager.
     */
    private final Set<InputListener> mListener = new HashSet<>();

    /**
     * Hold the keyboard device (Can only be one).
     */
    private final InputKeyboard mKeyboard;

    /**
     * Hold the mouse device (Can only be one).
     */
    private final InputMouse mMouse;

    private int mCursorX;
    private int mCursorY;
    private final boolean[] mKey = new boolean[InputKey.values().length];
    private final boolean[] mButton = new boolean[InputMouseButton.values().length];

    /**
     * Hold all the event(s) from the device(s) efficiently.
     */
    private final IntBuffer mBuffer = IntBuffer.allocate(4096);

    /**
     * <p>Constructor</p>
     */
    public DefaultInputManager(InputKeyboard keyboard, InputMouse mouse) {
        mKeyboard = keyboard;
        mMouse = mouse;
    }

    /**
     * <p>Called when the manager has been requested to initialise</p>
     * <p>
     * NOTE: This method is being used internally.
     */
    public void create() {
        //!
        //! Initialise the <code>Keyboard</code> device
        //!
        mKeyboard.create();

        //!
        //! Initialise the <code>Mouse</code> device
        //!
        mMouse.create();
    }

    /**
     * <p>Called when the manager has been requested to destroy</p>
     * <p>
     * NOTE: This method is being used internally.
     */
    public void destroy() {
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
     * <p>Called when the manager has been requested to update</p>
     * <p>
     * NOTE: This method is being used internally.
     */
    public void update() {
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
            switch (mBuffer.get()) {
                //!
                //! KEYBOARD
                //!
                case InputKeyboard.EVENT_KEY_UP:
                    onKeyboardKeyUp(InputKey.values()[mBuffer.get()]);
                    break;
                case InputKeyboard.EVENT_KEY_DOWN:
                    onKeyboardKeyDown(InputKey.values()[mBuffer.get()]);
                    break;
                case InputKeyboard.EVENT_KEY_TYPE:
                    onKeyboardKeyType((char) mBuffer.get());
                    break;

                //!
                //! MOUSE
                //!
                case InputMouse.EVENT_MOVE:
                    onMouseMove(mBuffer.get(), mBuffer.get());
                    break;
                case InputMouse.EVENT_BUTTON_UP:
                    onMouseButtonUp(InputMouseButton.values()[mBuffer.get()]);
                    break;
                case InputMouse.EVENT_BUTTON_DOWN:
                    onMouseButtonDown(InputMouseButton.values()[mBuffer.get()]);
                    break;
                case InputMouse.EVENT_WHEEL:
                    onMouseWheel(mBuffer.get());
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
