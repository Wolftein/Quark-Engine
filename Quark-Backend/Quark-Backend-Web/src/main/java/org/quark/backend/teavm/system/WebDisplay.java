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
package org.quark.backend.teavm.system;

import org.quark.render.texture.Image;
import org.quark.system.Display;
import org.quark.system.DisplayMode;
import org.teavm.jso.JSBody;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation for {@link Display}.
 */
public final class WebDisplay implements Display {
    private HTMLCanvasElement mHandle;

    /**
     * <p>Handle when the module create</p>
     */
    public void onModuleCreate(HTMLCanvasElement handle, Display.Preference preference) {
        mHandle = handle;
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDimension(int width, int height) {
        mHandle.setWidth(width);
        mHandle.setHeight(height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSynchronised(boolean activate) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursor(Image image, int xHotspot, int yHotspot) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToWindowed(DisplayMode mode) {
        if (isFullscreen()) {
            //!
            //! Only proceed if the window is in fullscreen mode
            //!
            onExitFullscreen(mHandle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToFullscreen(DisplayMode mode) {
        if (isWindowed()) {
            //!
            //! Only proceed if the window is in windowed mode
            //!
            onEnterFullscreen(mHandle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchToFullscreen() {
        switchToFullscreen(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return mHandle.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return mHandle.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isResizable() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDecorated() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWindowed() {
        return !isFullscreen(mHandle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullscreen() {
        return isFullscreen(mHandle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayMode getDisplayMode() {
        return new DisplayMode(getWidth(), getHeight(), 60);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DisplayMode> getAvailableDisplayModes() {
        return Collections.singletonList(getDisplayMode());
    }

    /**
     * @see <a href="https://robertnyman.com/2012/03/08/using-the-fullscreen-api-in-web-browsers/">Fullscreen</a>
     */
    @JSBody(params = {"element"}, script = "return " +
            "document.fullscreenElement || " +
            "document.webkitFullscreenElement ||" +
            "document.mozFullScreenElement || " +
            "document.msFullscreenElement")
    public static native boolean isFullscreen(HTMLElement element);

    /**
     * @see <a href="https://robertnyman.com/2012/03/08/using-the-fullscreen-api-in-web-browsers/">Fullscreen</a>
     */
    @JSBody(params = {"element"}, script = "if (document.exitFullscreen) document.exitFullscreen();" +
            "else if (document.webkitExitFullscreen) document.webkitExitFullscreen();" +
            "else if (document.mozCancelFullScreen) document.mozCancelFullScreen();" +
            "else if (document.msExitFullscreen) document.msExitFullscreen();")
    public static native void onExitFullscreen(HTMLElement element);

    /**
     * @see <a href="https://robertnyman.com/2012/03/08/using-the-fullscreen-api-in-web-browsers/">Fullscreen</a>
     */
    @JSBody(params = {"element"}, script = "if (element.requestFullscreen) element.requestFullscreen();" +
            "else if (element.webkitRequestFullscreen) element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);" +
            "else if (element.mozRequestFullScreen) element.mozRequestFullScreen();" +
            "else if (element.msRequestFullscreen) element.msRequestFullscreen();")
    public static native void onEnterFullscreen(HTMLElement element);
}
