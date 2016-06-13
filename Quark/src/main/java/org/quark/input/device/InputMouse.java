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
package org.quark.input.device;

import org.quark.input.Input;

import java.nio.IntBuffer;

/**
 * <code>InputMouse</code> encapsulate an {@link Input} for handling mouse based device(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface InputMouse extends Input {
    /**
     * Encapsulate the unique identifier for the mouse_move event.
     */
    int EVENT_MOVE = 0x00000010;

    /**
     * Encapsulate the unique identifier for the button_up event.
     */
    int EVENT_BUTTON_UP = EVENT_MOVE + 0x01;

    /**
     * Encapsulate the unique identifier for the button_down event.
     */
    int EVENT_BUTTON_DOWN = EVENT_BUTTON_UP + 0x01;

    /**
     * Encapsulate the unique identifier for the wheel event.
     */
    int EVENT_WHEEL = EVENT_BUTTON_DOWN + 0x01;

    /**
     * <p>Change the mode of the cursor</p>
     * <p>
     * NOTE: Activating the cursor mode will hide the cursor and provide unlimited move within the viewport.
     *
     * @param activate <code>true</code> to active the cursor mode, <code>false</code> otherwise
     */
    void setCursorMode(boolean activate);

    /**
     * <p>Change the cursor position</p>
     *
     * @param x the new x coordinate (in screen coordinates) of the cursor
     * @param y the new y coordinate (in screen coordinates) of the cursor
     */
    void setCursorPosition(int x, int y);

    /**
     * <p>Queue a mouse_move event</p>
     *
     * @see #EVENT_MOVE
     */
    static void onFactoryMove(IntBuffer buffer, int x, int y) {
        buffer.put(EVENT_MOVE).put(x).put(y);
    }

    /**
     * <p>Queue a mouse_button_up event</p>
     *
     * @see #EVENT_BUTTON_UP
     */
    static void onFactoryButtonUp(IntBuffer buffer, InputMouseButton button) {
        buffer.put(EVENT_BUTTON_UP).put(button.ordinal());
    }

    /**
     * <p>Queue a mouse_button_down event</p>
     *
     * @see #EVENT_BUTTON_DOWN
     */
    static void onFactoryButtonDown(IntBuffer buffer, InputMouseButton button) {
        buffer.put(EVENT_BUTTON_DOWN).put(button.ordinal());
    }

    /**
     * <p>Queue a mouse_wheel event</p>
     *
     * @see #EVENT_WHEEL
     */
    static void onFactoryWheel(IntBuffer buffer, int delta) {
        buffer.put(EVENT_WHEEL).put(delta);
    }
}
