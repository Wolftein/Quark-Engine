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
package ar.com.quark.input.device;

import ar.com.quark.input.Input;
import ar.com.quark.utility.buffer.Int32Buffer;

/**
 * <code>InputKeyboard</code> encapsulate an {@link Input} for handling keyboard based device(s).
 */
public interface InputKeyboard extends Input {
    /**
     * Encapsulate the unique identifier for the key_up event.
     */
    int EVENT_KEY_UP = 0x00000001;

    /**
     * Encapsulate the unique identifier for the key_down event.
     */
    int EVENT_KEY_DOWN = EVENT_KEY_UP + 0x01;

    /**
     * Encapsulate the unique identifier for the key_type event.
     */
    int EVENT_KEY_TYPE = EVENT_KEY_DOWN + 0x01;

    /**
     * <p>Queue a key up event</p>
     *
     * @see #EVENT_KEY_UP
     */
    static void onFactoryKeyUp(Int32Buffer buffer, InputKey key) {
        buffer.write(EVENT_KEY_UP).write(key.ordinal());
    }

    /**
     * <p>Queue a key down event</p>
     *
     * @see #EVENT_KEY_DOWN
     */
    static void onFactoryKeyDown(Int32Buffer buffer, InputKey key) {
        buffer.write(EVENT_KEY_DOWN).write(key.ordinal());
    }

    /**
     * <p>Queue a key type event</p>
     *
     * @see #EVENT_KEY_TYPE
     */
    static void onFactoryKeyType(Int32Buffer buffer, char key) {
        buffer.write(EVENT_KEY_TYPE).write(key);
    }
}
