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
package org.quark.engine.media.input;

/**
 * <code>InputManager</code> represent a service to handle raw input and transforming them to abstract data.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface InputManager {
    /**
     * <p>Change the input mode</p>
     * <p>
     * NOTE: If <code>true</code> then the cursor will be available and allow to exit the window, otherwise the cursor
     * will be grabbed providing virtual and unlimited cursor movement.
     *
     * @param active <code>true</code> to active the input mode, <code>false</code> to de-activate it
     */
    void setInputMode(boolean active);

    /**
     * <p>Get the x coordinate of the cursor</p>
     *
     * @return the x coordinate of the cursor
     */
    int getCursorX();

    /**
     * <p>Get the y coordinate of the cursor</p>
     *
     * @return the y coordinate of the cursor
     */
    int getCursorY();

    /**
     * <p>Check if the given {@link InputButton} has been released</p>
     *
     * @param button the button code
     *
     * @return <code>true</code> if the button has been released, <code>false</code> otherwise
     */
    boolean isButtonReleased(InputButton button);

    /**
     * <p>Check if the given {@link InputButton} has been pressed</p>
     *
     * @param button the button code
     *
     * @return <code>true</code> if the button has been pressed, <code>false</code> otherwise
     */
    boolean isButtonPressed(InputButton button);

    /**
     * <p>Check if the given {@link InputKey} has been released</p>
     *
     * @param key the key code
     *
     * @return <code>true</code> if the key has been released, <code>false</code> otherwise
     */
    boolean isKeyReleased(InputKey key);

    /**
     * <p>Check if the given {@link InputKey} has been pressed</p>
     *
     * @param key the key code
     *
     * @return <code>true</code> if the key has been pressed, <code>false</code> otherwise
     */
    boolean isKeyPressed(InputKey key);
}
