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
package ar.com.quark.input;

import ar.com.quark.utility.buffer.Int32Buffer;

/**
 * <code>Input</code> encapsulate an input-device to interface with {@link InputManager}.
 */
public interface Input {
    /**
     * <p>Create and initialise the device</p>
     */
    void create();

    /**
     * <p>Update and poll all input event(s) from the device</p>
     *
     * @param buffer the buffer that will contain the input when updating
     */
    void update(Int32Buffer buffer);

    /**
     * <p>Destroy the device</p>
     */
    void destroy();
}
