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
package org.quark.engine.media;

import java.util.Collection;

/**
 * <code>Display</code> encapsulate a physical display.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public interface Display {
    /**
     * Encapsulate the preferences of {@link Preferences}.
     * <p>
     * All features may not be supported by the underlying implementation, check implementation for more information.
     */
    final class Preferences {
        /**
         * Encapsulate a flag to define the decoration feature.
         */
        public final static int FLAG_DECORATED = (1 << 1);

        /**
         * Encapsulate a flag to define the resizable feature.
         */
        public final static int FLAG_RESIZABLE = (1 << 2);

        /**
         * Encapsulate a flag to define the fullscreen feature.
         */
        public final static int FLAG_FULLSCREEN = (1 << 3);

        /**
         * Encapsulate a flag to define the limited feature.
         */
        public final static int FLAG_LIMITED = (1 << 4);

        private final DisplayMode mMode;
        private final String mTitle;
        private final boolean mLimited;
        private final boolean mDecorated;
        private final boolean mResizable;
        private final boolean mFullscreen;

        /**
         * <p>Constructor</p>
         */
        public Preferences(DisplayMode mode, String title, int features) {
            mMode = mode;
            mTitle = title;
            mDecorated = (features & FLAG_DECORATED) != 0;
            mResizable = (features & FLAG_RESIZABLE) != 0;
            mFullscreen = (features & FLAG_FULLSCREEN) != 0;
            mLimited = (features & FLAG_LIMITED) != 0;
        }

        /**
         * <p>Constructor without features</p>
         */
        public Preferences(DisplayMode mode, String title) {
            this(mode, title, 0);
        }

        /**
         * <p>Get the display mode of the display</p>
         *
         * @return the display mode of the display
         */
        public DisplayMode getMode() {
            return mMode;
        }

        /**
         * <p>Retrieves the tile of the display</p>
         *
         * @return the tile of the display
         */
        public String getTitle() {
            return mTitle;
        }

        /**
         * <p>Check whenever the display will be limited</p>
         *
         * @return <code>true</code> if the display will be limited, <code>false</code> otherwise
         */
        public boolean isLimited() {
            return mLimited;
        }

        /**
         * <p>Check whenever the display will be decorated</p>
         *
         * @return <code>true</code> if the display will be decorated, <code>false</code> otherwise
         */
        public boolean isDecorated() {
            return mDecorated;
        }

        /**
         * <p>Check whenever the display will be resizable</p>
         *
         * @return <code>true</code> if the display will be resizable, <code>false</code> otherwise
         */
        public boolean isResizable() {
            return mResizable;
        }

        /**
         * <p>Check whenever the display will be created in fullscreen</p>
         *
         * @return <code>true</code> if the display will be created in fullscreen, <code>false</code> otherwise
         */
        public boolean isFullscreen() {
            return mFullscreen;
        }
    }

    /**
     * <p>Change the dimension of the display</p>
     *
     * @param width  the new width (in screen coordinates)
     * @param height the new height (in screen coordinates)
     */
    void setDimension(int width, int height);

    /**
     * <p>Change the title of the display</p>
     *
     * @param title the new title of the display
     */
    void setTitle(String title);

    /**
     * <p>Change the visibility of the display</p>
     *
     * @param visibility indicates whenever to show or hide the display
     */
    void setVisibility(boolean visibility);

    /**
     * <p>Change the display to limit the operation(s) to the monitor's refresh rate</p>
     *
     * @param activate indicates whenever to limit the device or not
     */
    void setLimited(boolean activate);

    /**
     * <p>Change the display mode to windowed mode using the given {@link DisplayMode}</p>
     *
     * @param mode the new display mode of the display
     *
     * @see #getDisplayMode()
     * @see #getAvailableDisplayModes()
     */
    void switchToWindowed(DisplayMode mode);

    /**
     * <p>Change the display mode to full-screen mode using the given {@link DisplayMode}</p>
     *
     * @param mode the new display mode of the display
     *
     * @see #getDisplayMode()
     * @see #getAvailableDisplayModes()
     */
    void switchToFullscreen(DisplayMode mode);

    /**
     * <p>Change the display mode to full-screen mode using the current {@link DisplayMode}</p>
     *
     * @see #getDisplayMode()
     */
    void switchToFullscreen();

    /**
     * <p>Get the width of the display (in screen coordinates)</p>
     *
     * @return the width of the display (in screen coordinates)
     */
    int getWidth();

    /**
     * <p>Get the height of the display (in screen coordinates)</p>
     *
     * @return the height of the display (in screen coordinates)
     */
    int getHeight();

    /**
     * <p>Check whenever the display is limited to refresh rate</p>
     *
     * @return <code>true</code> if the display is limited to refresh rate, <code>false</code> otherwise
     */
    boolean isLimited();

    /**
     * <p>Check whenever the display is resizable</p>
     *
     * @return <code>true</code> if the display is resizable, <code>false</code> otherwise
     */
    boolean isResizable();

    /**
     * <p>Check whenever the display is decorated</p>
     *
     * @return <code>true</code> if the display is decorated, <code>false</code> otherwise
     */
    boolean isDecorated();

    /**
     * <p>Check whenever the display is in displayed mode</p>
     *
     * @return <code>true</code> if the display is in displayed mode, <code>false</code> otherwise
     */
    boolean isWindowed();

    /**
     * <p>Check whenever the display is in fullscreen mode</p>
     *
     * @return <code>true</code> if the display is in fullscreen mode, <code>false</code> otherwise
     */
    boolean isFullscreen();

    /**
     * <p>Check whenever the display is visible</p>
     *
     * @return <code>true</code> if the display is visible, <code>false</code> otherwise
     */
    boolean isVisible();

    /**
     * <p>Check whenever the display is active</p>
     *
     * @return <code>true</code> if the display is active, <code>false</code> otherwise
     */
    boolean isActive();

    /**
     * <p>Get the {@link DisplayMode} of the primary device</p>
     *
     * @return the display mode of the primary device
     */
    DisplayMode getDisplayMode();

    /**
     * <p>Get all available {@link DisplayMode} of the primary device</p>
     *
     * @return all available display mode of the primary device
     */
    Collection<DisplayMode> getAvailableDisplayModes();
}
