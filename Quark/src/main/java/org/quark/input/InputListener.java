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
import org.quark.input.device.InputMouseButton;

/**
 * <code>InputListener</code> encapsulate a raw listener for handling any input-event.
 */
public interface InputListener {
    /**
     * <p>Called when a {@link InputKey} is being held down</p>
     *
     * @param key the input key that trigger the event
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onKeyboardKeyDown(InputKey key);

    /**
     * <p>Called when a {@link InputKey} stop being held down</p>
     *
     * @param key the input key that trigger the event
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onKeyboardKeyUp(InputKey key);

    /**
     * <p>Called when a {@link InputKey} typed a key</p>
     *
     * @param key the input key that trigger the event
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onKeyboardKeyType(char key);

    /**
     * <p>Called when a {@link InputMouseButton} is being held down</p>
     *
     * @param x      the x coordinate (in screen coordinate) of the cursor
     * @param y      the y coordinate (in screen coordinate) of the cursor
     * @param button the input button that trigger the event
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onMouseButtonDown(int x, int y, InputMouseButton button);

    /**
     * <p>Called when a {@link InputMouseButton} stop being held down</p>
     *
     * @param x      the x coordinate (in screen coordinate) of the cursor
     * @param y      the y coordinate (in screen coordinate) of the cursor
     * @param button the input button that trigger the event
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onMouseButtonUp(int x, int y, InputMouseButton button);

    /**
     * <p>Called when the cursor has been move</p>
     *
     * @param x  the new x coordinate (in screen coordinate) of the cursor
     * @param y  the new y coordinate (in screen coordinate) of the cursor
     * @param dx the delta x coordinate (in screen coordinate) of the cursor
     * @param dy the delta y coordinate (in screen coordinate) of the cursor
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onMouseMove(int x, int y, int dx, int dy);

    /**
     * <p>Called when the wheel has been move</p>
     *
     * @param x     the x coordinate (in screen coordinate) of the cursor
     * @param y     the y coordinate (in screen coordinate) of the cursor
     * @param delta the delta value of the wheel movement (positive means up, negative means down)
     *
     * @return <code>true</code> if the event has been consume, <code>false</code> otherwise
     */
    boolean onMouseWheel(int x, int y, int delta);
}
