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
package com.github.wolftein.quark.backend.teavm.input;

import com.github.wolftein.quark.system.utility.array.Int32Array;
import com.github.wolftein.quark.input.device.InputMouse;
import com.github.wolftein.quark.input.device.InputMouseButton;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.events.WheelEvent;
import org.teavm.jso.dom.html.HTMLElement;

import static com.github.wolftein.quark.Quark.QKInput;

/**
 * Implementation for {@link InputMouse}.
 */
public final class WebInputMouse implements InputMouse {
    /**
     * Hold the canvas handle.
     */
    private final HTMLElement mHandle;

    /**
     * Hold the registration.
     */
    private final EventListener<MouseEvent> mRegistration1 = this::onMouseButtonUp;
    private final EventListener<MouseEvent> mRegistration2 = this::onMouseButtonDown;
    private final EventListener<MouseEvent> mRegistration3 = this::onMouseMove;
    private final EventListener<WheelEvent> mRegistration4 = this::onMouseWheel;

    /**
     * <p>Constructor</p>
     */
    public WebInputMouse(HTMLElement handle) {
        mHandle = handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create() {
        mHandle.listenMouseUp(mRegistration1);
        mHandle.listenMouseDown(mRegistration2);
        mHandle.addEventListener("mousemove", mRegistration3);
        mHandle.addEventListener("wheel", mRegistration4);
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
        mHandle.neglectMouseUp(mRegistration1);
        mHandle.neglectMouseDown(mRegistration2);
        mHandle.removeEventListener("mousemove", mRegistration3);
        mHandle.removeEventListener("wheel", mRegistration4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorMode(boolean activate) {
        if (activate) {
            onPointerLock(mHandle);
        } else {
            onPointerUnlock(mHandle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(int x, int y) {
    }

    /**
     * <p>Handle mouse-up event</p>
     */
    private void onMouseButtonUp(MouseEvent event) {
        final InputMouseButton input = transform(event.getButton());

        if (input != null) {
            QKInput.invokeMouseButtonUp(input);
        }
    }

    /**
     * <p>Handle mouse-down event</p>
     */
    private void onMouseButtonDown(MouseEvent event) {
        final InputMouseButton input = transform(event.getButton());

        if (input != null) {
            QKInput.invokeMouseButtonDown(input);
        }
    }

    /**
     * <p>Handle mouse-move event</p>
     */
    private void onMouseMove(MouseEvent event) {
        if (isPointerLocked(mHandle)) {
            QKInput.invokeMouseMove(
                    QKInput.getCursorX() + getMovementX(event),
                    QKInput.getCursorY() + getMovementY(event));
        } else {
            QKInput.invokeMouseMove(event.getClientX(), event.getClientY());
        }
    }

    /**
     * <p>Handle mouse-wheel event</p>
     */
    private void onMouseWheel(WheelEvent event) {
        QKInput.invokeMouseWheel(Math.min(-1, Math.max(1, (int) event.getDeltaY())));
    }

    /**
     * <p>Transform an implementation button code into {@link InputMouseButton}</p>
     */
    private InputMouseButton transform(int code) {
        switch (code) {
            case 0x00:
                return InputMouseButton.BUTTON_LEFT;
            case 0x01:
                return InputMouseButton.BUTTON_MIDDLE;
            case 0x02:
                return InputMouseButton.BUTTON_RIGHT;
        }
        return null;
    }

    /**
     * @see <a href="https://w3c.github.io/pointerlock/">Pointer Lock</a>
     */
    @JSBody(params = {"element"},
            script = "element.requestPointerLock = element.requestPointerLock " +
                    "|| element.mozRequestPointerLock || element.webkitRequestPointerLock; element.requestPointerLock();")
    private static native void onPointerLock(HTMLElement element);

    /**
     * @see <a href="https://w3c.github.io/pointerlock/">Pointer Lock</a>
     */
    @JSBody(params = {"element"},
            script = "document.exitPointerLock = document.exitPointerLock " +
                    "|| document.mozExitPointerLock || document.webkitExitPointerLock; document.exitPointerLock();")
    private static native void onPointerUnlock(HTMLElement element);

    /**
     * @see <a href="https://w3c.github.io/pointerlock/">Pointer Lock</a>
     */
    @JSBody(params = {"element"},
            script = "return (document.pointerLockElement == element || document.mozPointerLockElement  == element);")
    private static native boolean isPointerLocked(HTMLElement element);

    /**
     * @see <a href="https://w3c.github.io/pointerlock/">Pointer Lock</a>
     */
    @JSBody(params = {"event"},
            script = "return event.movementX;")
    private static native int getMovementX(MouseEvent event);

    /**
     * @see <a href="https://w3c.github.io/pointerlock/">Pointer Lock</a>
     */
    @JSBody(params = {"event"},
            script = "return event.movementY;")
    private static native int getMovementY(MouseEvent event);
}
