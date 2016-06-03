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
package org.quark_engine.input.device;

import org.quark_engine.input.Input;

import java.nio.IntBuffer;

/**
 * <code>InputKeyboard</code> encapsulate an {@link Input} for handling keyboard based device(s).
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
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
     * <p>Queue a key_up event</p>
     *
     * @see #EVENT_KEY_UP
     */
    static void onFactoryKeyUp(IntBuffer buffer, InputKey key) {
        buffer.put(EVENT_KEY_UP).put(key.ordinal());
    }

    /**
     * <p>Queue a key_down event</p>
     *
     * @see #EVENT_KEY_DOWN
     */
    static void onFactoryKeyDown(IntBuffer buffer, InputKey key) {
        buffer.put(EVENT_KEY_DOWN).put(key.ordinal());
    }

    /**
     * <p>Queue a key_type event</p>
     *
     * @see #EVENT_KEY_TYPE
     */
    static void onFactoryKeyType(IntBuffer buffer, char key) {
        buffer.put(EVENT_KEY_TYPE).put(key);
    }
}
