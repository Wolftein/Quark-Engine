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
package ar.com.quark.backend.teavm.input;

import ar.com.quark.Quark;
import ar.com.quark.input.device.InputKey;
import ar.com.quark.input.device.InputKeyboard;
import ar.com.quark.system.utility.array.Int32Array;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Implementation for {@link InputKeyboard}.
 */
public final class WebInputKeyboard implements InputKeyboard {
    /**
     * Hold the canvas handle.
     */
    private final HTMLElement mHandle;

    /**
     * Hold the registration.
     */
    private final EventListener<KeyboardEvent> mRegistration1 = this::onKeyUp;
    private final EventListener<KeyboardEvent> mRegistration2 = this::onKeyDown;
    private final EventListener<KeyboardEvent> mRegistration3 = this::onKeyPress;

    /**
     * <p>Constructor</p>
     */
    public WebInputKeyboard(HTMLElement handle) {
        mHandle = handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        mHandle.listenKeyUp(mRegistration1);
        mHandle.listenKeyDown(mRegistration2);
        mHandle.listenKeyPress(mRegistration3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Int32Array buffer) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        mHandle.neglectKeyUp(mRegistration1);
        mHandle.neglectKeyDown(mRegistration2);
        mHandle.neglectKeyPress(mRegistration3);
    }

    /**
     * <p>Handle key-up event</p>
     */
    private void onKeyUp(KeyboardEvent event) {
        final InputKey input = transform(event.getKeyCode());
        if (input != null) {
            Quark.QKInput.invokeKeyUp(input);
        }
        event.preventDefault();
    }

    /**
     * <p>Handle key-down event</p>
     */
    private void onKeyDown(KeyboardEvent event) {
        final InputKey input = transform(event.getKeyCode());
        if (input != null) {
            Quark.QKInput.invokeKeyDown(input);
        }
        event.preventDefault();
    }

    /**
     * <p>Handle key-press event</p>
     */
    private void onKeyPress(KeyboardEvent event) {
        Quark.QKInput.invokeKeyType((char) event.getCharCode());

        event.preventDefault();
    }

    /**
     * <p>Transform an implementation key code into {@link InputKey}</p>
     */
    private InputKey transform(int code) {
        switch (code) {
            case 32:
                return InputKey.KEY_SPACE;
            case 222:
                return InputKey.KEY_APOSTROPHE;
            case 188:
                return InputKey.KEY_COMMA;
            case 189:
                return InputKey.KEY_MINUS;
            case 190:
                return InputKey.KEY_PERIOD;
            case 191:
                return InputKey.KEY_SLASH;
            case 48:
                return InputKey.KEY_0;
            case 49:
                return InputKey.KEY_1;
            case 50:
                return InputKey.KEY_2;
            case 51:
                return InputKey.KEY_3;
            case 52:
                return InputKey.KEY_4;
            case 53:
                return InputKey.KEY_5;
            case 54:
                return InputKey.KEY_6;
            case 55:
                return InputKey.KEY_7;
            case 56:
                return InputKey.KEY_8;
            case 57:
                return InputKey.KEY_9;
            case 186:
                return InputKey.KEY_SEMICOLON;
            case 187:
                return InputKey.KEY_EQUAL;
            case 65:
                return InputKey.KEY_A;
            case 66:
                return InputKey.KEY_B;
            case 67:
                return InputKey.KEY_C;
            case 68:
                return InputKey.KEY_D;
            case 69:
                return InputKey.KEY_E;
            case 70:
                return InputKey.KEY_F;
            case 71:
                return InputKey.KEY_G;
            case 72:
                return InputKey.KEY_H;
            case 73:
                return InputKey.KEY_I;
            case 74:
                return InputKey.KEY_J;
            case 75:
                return InputKey.KEY_K;
            case 76:
                return InputKey.KEY_L;
            case 77:
                return InputKey.KEY_M;
            case 78:
                return InputKey.KEY_N;
            case 79:
                return InputKey.KEY_O;
            case 80:
                return InputKey.KEY_P;
            case 81:
                return InputKey.KEY_Q;
            case 82:
                return InputKey.KEY_R;
            case 83:
                return InputKey.KEY_S;
            case 84:
                return InputKey.KEY_T;
            case 85:
                return InputKey.KEY_U;
            case 86:
                return InputKey.KEY_V;
            case 87:
                return InputKey.KEY_W;
            case 88:
                return InputKey.KEY_X;
            case 89:
                return InputKey.KEY_Y;
            case 90:
                return InputKey.KEY_Z;
            case 219:
                return InputKey.KEY_LEFT_BRACKET;
            case 220:
                return InputKey.KEY_BACKSLASH;
            case 221:
                return InputKey.KEY_RIGHT_BRACKET;
            case 192:
                return InputKey.KEY_GRAVE_ACCENT;
            case 27:
                return InputKey.KEY_ESCAPE;
            case 13:
                return InputKey.KEY_ENTER;
            case 9:
                return InputKey.KEY_TAB;
            case 8:
                return InputKey.KEY_BACKSPACE;
            case 45:
                return InputKey.KEY_INSERT;
            case 46:
                return InputKey.KEY_DELETE;
            case 37:
                return InputKey.KEY_LEFT;
            case 38:
                return InputKey.KEY_UP;
            case 39:
                return InputKey.KEY_RIGHT;
            case 40:
                return InputKey.KEY_DOWN;
            case 33:
                return InputKey.KEY_PAGE_UP;
            case 34:
                return InputKey.KEY_PAGE_DOWN;
            case 36:
                return InputKey.KEY_HOME;
            case 35:
                return InputKey.KEY_END;
            case 20:
                return InputKey.KEY_CAPS_LOCK;
            case 145:
                return InputKey.KEY_SCROLL_LOCK;
            case 144:
                return InputKey.KEY_NUM_LOCK;
            case 44:
                return InputKey.KEY_PRINT_SCREEN;
            case 19:
                return InputKey.KEY_PAUSE;
            case 112:
                return InputKey.KEY_F1;
            case 113:
                return InputKey.KEY_F2;
            case 114:
                return InputKey.KEY_F3;
            case 115:
                return InputKey.KEY_F4;
            case 116:
                return InputKey.KEY_F5;
            case 117:
                return InputKey.KEY_F6;
            case 118:
                return InputKey.KEY_F7;
            case 119:
                return InputKey.KEY_F8;
            case 120:
                return InputKey.KEY_F9;
            case 121:
                return InputKey.KEY_F10;
            case 122:
                return InputKey.KEY_F11;
            case 123:
                return InputKey.KEY_F12;
            case 96:
                return InputKey.KEY_KP_0;
            case 97:
                return InputKey.KEY_KP_1;
            case 98:
                return InputKey.KEY_KP_2;
            case 99:
                return InputKey.KEY_KP_3;
            case 100:
                return InputKey.KEY_KP_4;
            case 101:
                return InputKey.KEY_KP_5;
            case 102:
                return InputKey.KEY_KP_6;
            case 103:
                return InputKey.KEY_KP_7;
            case 104:
                return InputKey.KEY_KP_8;
            case 105:
                return InputKey.KEY_KP_9;
            case 110:
                return InputKey.KEY_KP_DECIMAL;
            case 111:
                return InputKey.KEY_KP_DIVIDE;
            case 106:
                return InputKey.KEY_KP_MULTIPLY;
            case 109:
                return InputKey.KEY_KP_SUBTRACT;
            case 107:
                return InputKey.KEY_KP_ADD;
            // case KeyCodes.KEY_ENTER:
            //    return InputKey.KEY_KP_ENTER;
            //case KeyCodes.KEY_KP_EQUAL:
            //    return InputKey.KEY_KP_EQUAL;
            case 16:
                return InputKey.KEY_LEFT_SHIFT;
            case 17:
                return InputKey.KEY_LEFT_CONTROL;
            case 18:
                return InputKey.KEY_LEFT_ALT;
            case 91:
                return InputKey.KEY_LEFT_SUPER;
            //case KeyCodes.KEY_MENU:
            //    return InputKey.KEY_MENU;
            //case KeyCodes.KEY_RIGHT_SHIFT:
            //    return InputKey.KEY_RIGHT_SHIFT;
            //case KeyCodes.KEY_RIGHT_CONTROL:
            //    return InputKey.KEY_RIGHT_CONTROL;
            //case KeyCodes.KEY_RIGHT_ALT:
            //    return InputKey.KEY_RIGHT_ALT;
            case 92:
                return InputKey.KEY_RIGHT_SUPER;
        }
        return null;
    }
}
