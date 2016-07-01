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
package com.github.wolftein.quark.input.device;

/**
 * <code>InputMouseButton</code> enumerate(s) all button(s).
 */
public enum InputMouseButton {
    /**
     * Represent the left button of a pointing device.
     */
    BUTTON_LEFT,

    /**
     * Represent the right button of a pointing device.
     */
    BUTTON_RIGHT,

    /**
     * Represent the middle button of a pointing device.
     */
    BUTTON_MIDDLE;

    /**
     * Hold all value(s).
     */
    public final static InputMouseButton[] VALUES = InputMouseButton.values();
}
