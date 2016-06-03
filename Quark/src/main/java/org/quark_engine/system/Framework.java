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
package org.quark_engine.system;

import java.util.Collection;

/**
 * <code>Framework</code> encapsulate the context of the library.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface Framework {
    /**
     * Describe display properties for {@link Framework}.
     */
    final class DisplayMode {
        private final int mWidth;
        private final int mHeight;
        private final int mRate;

        /**
         * <p>Constructor</p>
         */
        public DisplayMode(int width, int height, int rate) {
            mWidth = width;
            mHeight = height;
            mRate = rate;
        }

        /**
         * <p>Get the width of the display mode (in screen coordinates)</p>
         *
         * @return the width of the display mode (in screen coordinates)
         */
        public int getWidth() {
            return mWidth;
        }

        /**
         * <p>Get the height of the display mode (in screen coordinates)</p>
         *
         * @return the height of the display mode (in screen coordinates)
         */
        public int getHeight() {
            return mHeight;
        }

        /**
         * <p>Get the refresh rate of the display mode (in Hertz)</p>
         *
         * @return the refresh rate of the display mode (in Hertz)
         */
        public int getRate() {
            return mRate;
        }
    }

    /**
     * <p>Change the dimension of the context</p>
     *
     * @param width  the new width (in screen coordinates)
     * @param height the new height (in screen coordinates)
     */
    void setDimension(int width, int height);

    /**
     * <p>Change the title of the context</p>
     *
     * @param title the new title of the context
     */
    void setTitle(String title);

    /**
     * <p>Change the context to synchronise to the monitor's refresh rate</p>
     *
     * @param activate <code>true</code> to synchronise the context, <code>false</code> otherwise
     */
    void setSynchronised(boolean activate);

    /**
     * <p>Change the context mode to windowed mode using the given {@link DisplayMode}</p>
     *
     * @param mode the new context mode of the context
     *
     * @see #getDisplayMode()
     * @see #getAvailableDisplayModes()
     */
    void switchToWindowed(DisplayMode mode);

    /**
     * <p>Change the context mode to full-screen mode using the given {@link DisplayMode}</p>
     *
     * @param mode the new context mode of the context
     *
     * @see #getDisplayMode()
     * @see #getAvailableDisplayModes()
     */
    void switchToFullscreen(DisplayMode mode);

    /**
     * <p>Change the context mode to full-screen mode using the current {@link DisplayMode}</p>
     *
     * @see #getDisplayMode()
     */
    void switchToFullscreen();

    /**
     * <p>Get the width of the context (in screen coordinates)</p>
     *
     * @return the width of the context (in screen coordinates)
     */
    int getWidth();

    /**
     * <p>Get the height of the context (in screen coordinates)</p>
     *
     * @return the height of the context (in screen coordinates)
     */
    int getHeight();

    /**
     * <p>Check if the context is resizable</p>
     *
     * @return <code>true</code> if the context is resizable, <code>false</code> otherwise
     */
    boolean isResizable();

    /**
     * <p>Check if the context is decorated</p>
     *
     * @return <code>true</code> if the context is decorated, <code>false</code> otherwise
     */
    boolean isDecorated();

    /**
     * <p>Check if the context is in windowed mode</p>
     *
     * @return <code>true</code> if the context is in windowed mode, <code>false</code> otherwise
     */
    boolean isWindowed();

    /**
     * <p>Check if the context is in fullscreen mode</p>
     *
     * @return <code>true</code> if the context is in fullscreen mode, <code>false</code> otherwise
     */
    boolean isFullscreen();

    /**
     * <p>Check if the context is active</p>
     *
     * @return <code>true</code> if the context is active, <code>false</code> otherwise
     */
    boolean isActive();

    /**
     * <p>Get the {@link DisplayMode} of the primary device</p>
     *
     * @return the context mode of the primary device
     */
    DisplayMode getDisplayMode();

    /**
     * <p>Get all available {@link DisplayMode} of the primary device</p>
     *
     * @return all available context mode of the primary device
     */
    Collection<DisplayMode> getAvailableDisplayModes();
}
