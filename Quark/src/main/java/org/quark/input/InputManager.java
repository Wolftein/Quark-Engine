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

import org.quark.input.device.InputKeyboard;
import org.quark.input.device.InputMouse;
import org.quark.input.device.InputMouseButton;
import org.quark.input.device.InputKey;

/**
 * <code>InputManager</code> encapsulate a service on which handle raw input and transforming them to abstract data.
 */
public interface InputManager {
    /**
     * <p>Invoke a raw input event immediately</p>
     *
     * @param event an array that contain(s) the event (in binary)
     */
    void invoke(int[] event);

    /**
     * <p>Invoke a {@link InputKeyboard#EVENT_KEY_UP} event</p>
     *
     * @param key the key of the event
     *
     * @see #invoke(int[])
     */
    default void invokeKeyUp(InputKey key) {
        final int[] event = {
                InputKeyboard.EVENT_KEY_UP, key.ordinal()
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputKeyboard#EVENT_KEY_DOWN} event</p>
     *
     * @param key the key of the event
     *
     * @see #invoke(int[])
     */
    default void invokeKeyDown(InputKey key) {
        final int[] event = {
                InputKeyboard.EVENT_KEY_DOWN, key.ordinal()
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputKeyboard#EVENT_KEY_TYPE} event</p>
     *
     * @param key the key of the event
     *
     * @see #invoke(int[])
     */
    default void invokeKeyType(char key) {
        final int[] event = {
                InputKeyboard.EVENT_KEY_DOWN, key
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputMouse#EVENT_BUTTON_UP} event</p>
     *
     * @param button the button of the event
     *
     * @see #invoke(int[])
     */
    default void invokeMouseButtonUp(InputMouseButton button) {
        final int[] event = {
                InputMouse.EVENT_BUTTON_UP, button.ordinal()
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputMouse#EVENT_BUTTON_DOWN} event</p>
     *
     * @param button the button of the event
     *
     * @see #invoke(int[])
     */
    default void invokeMouseButtonDown(InputMouseButton button) {
        final int[] event = {
                InputMouse.EVENT_BUTTON_DOWN, button.ordinal()
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputMouse#EVENT_MOVE} event</p>
     *
     * @param x the x coordinate of the event
     * @param y the y coordinate of the event
     *
     * @see #invoke(int[])
     */
    default void invokeMouseMove(int x, int y) {
        final int[] event = {
                InputMouse.EVENT_MOVE, x, y
        };
        invoke(event);
    }

    /**
     * <p>Invoke a {@link InputMouse#EVENT_WHEEL} event</p>
     *
     * @param delta the wheel delta of the event
     *
     * @see #invoke(int[])
     */
    default void invokeMouseWheel(int delta) {
        final int[] event = {
                InputMouse.EVENT_WHEEL, delta
        };
        invoke(event);
    }

    /**
     * <p>Register an {@link InputListener}</p>
     *
     * @param listener the listener
     *
     * @see #removeInputListener(InputListener)
     */
    void addInputListener(InputListener listener);

    /**
     * <p>Removes an {@link InputListener} previously registered</p>
     *
     * @param listener the listener
     *
     * @see #addInputListener(InputListener)
     */
    void removeInputListener(InputListener listener);

    /**
     * <p>Change the mode of the cursor</p>
     * <p>
     * NOTE: Activating the cursor mode will hide the cursor and provide unlimited move within the viewport.
     *
     * @param activate <code>true</code> to active the cursor mode, <code>false</code> otherwise
     */
    void setCursorMode(boolean activate);

    /**
     * <p>Change the position of the cursor</p>
     *
     * @param x the new x coordinate (in screen coordinates) of the cursor
     * @param y the new y coordinate (in screen coordinates) of the cursor
     */
    void setCursorPosition(int x, int y);

    /**
     * <p>Get the x coordinate (in screen coordinates) of the cursor</p>
     *
     * @return the x coordinate (in screen coordinates) of the cursor
     */
    int getCursorX();

    /**
     * <p>Get the y coordinate (in screen coordinates) of the cursor</p>
     *
     * @return the y coordinate (in screen coordinates) of the cursor
     */
    int getCursorY();

    /**
     * <p>Check if the given {@link InputMouseButton} isn't being hold down</p>
     *
     * @param button the button code
     *
     * @return <code>true</code> if the button has been released, <code>false</code> otherwise
     */
    boolean isButtonUp(InputMouseButton button);

    /**
     * <p>Check if the given {@link InputMouseButton} is being hold down</p>
     *
     * @param button the button code
     *
     * @return <code>true</code> if the button has been pressed, <code>false</code> otherwise
     */
    boolean isButtonDown(InputMouseButton button);

    /**
     * <p>Check if the given {@link InputKey} isn't being hold down</p>
     *
     * @param key the key code
     *
     * @return <code>true</code> if the key has been released, <code>false</code> otherwise
     */
    boolean isKeyUp(InputKey key);

    /**
     * <p>Check if the given {@link InputKey} is being hold down</p>
     *
     * @param key the key code
     *
     * @return <code>true</code> if the key has been pressed, <code>false</code> otherwise
     */
    boolean isKeyDown(InputKey key);
}