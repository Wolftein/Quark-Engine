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
package ar.com.quark.system;

import ar.com.quark.graphic.texture.Image;
import ar.com.quark.graphic.texture.ImageFormat;
import org.eclipse.collections.api.collection.ImmutableCollection;

/**
 * <code>Display</code> encapsulate the display of the framework.
 */
public interface Display {
    /**
     * <code>Preference</code> represent the preferences of {@link Display}.
     * <p>
     * All features may not be supported by the underlying implementation, check implementation for more information.
     */
    final class Preference {
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
         * Encapsulate a flag to define the synchronise feature.
         */
        public final static int FLAG_SYNCHRONISED = (1 << 4);

        /**
         * Encapsulate a flag to define the sRGB feature.
         */
        public final static int FLAG_SRGB = (1 << 5);

        private final DisplayMode mMode;
        private final String mTitle;
        private final int mSamples;
        private final int mFeatures;

        /**
         * <p>Constructor</p>
         */
        public Preference(DisplayMode mode, String title, int samples, int features) {
            mMode = mode;
            mTitle = title;
            mSamples = samples;
            mFeatures = features;
        }

        /**
         * <p>Constructor</p>
         */
        public Preference(DisplayMode mode, String title) {
            this(mode, title, 0, 0);
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
         * <p>Retrieves the samples of the display</p>
         *
         * @return the samples of the display
         */
        public int getSamples() {
            return mSamples;
        }

        /**
         * <p>Check if the display will be synchronised</p>
         *
         * @return <code>true</code> if the display will be synchronised, <code>false</code> otherwise
         */
        public boolean isSynchronised() {
            return (mFeatures & FLAG_SYNCHRONISED) != 0;
        }

        /**
         * <p>Check if the display will be decorated</p>
         *
         * @return <code>true</code> if the display will be decorated, <code>false</code> otherwise
         */
        public boolean isDecorated() {
            return (mFeatures & FLAG_DECORATED) != 0;
        }

        /**
         * <p>Check if the display will be resizable</p>
         *
         * @return <code>true</code> if the display will be resizable, <code>false</code> otherwise
         */
        public boolean isResizable() {
            return (mFeatures & FLAG_RESIZABLE) != 0;
        }

        /**
         * <p>Check if the display will be created in fullscreen</p>
         *
         * @return <code>true</code> if the display will be created in fullscreen, <code>false</code> otherwise
         */
        public boolean isFullscreen() {
            return (mFeatures & FLAG_FULLSCREEN) != 0;
        }

        /**
         * <p>Check if the display will be created using sRGB format</p>
         *
         * @return <code>true</code> if the display will be created using sRGB format, <code>false</code> otherwise
         */
        public boolean isSRGB() {
            return (mFeatures & FLAG_SRGB) != 0;
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
     * <p>Change the display to synchronise to the monitor's refresh rate</p>
     *
     * @param activate <code>true</code> to synchronise the display, <code>false</code> otherwise
     */
    void setSynchronised(boolean activate);

    /**
     * <p>Change the hardware based cursor of the display</p>
     * <p>
     * NOTE: Format must be {@link ImageFormat#RGBA}
     *
     * @param image    the image
     * @param xHotspot the hotspot for the x coordinate
     * @param yHotspot the hotspot for the y coordinate
     */
    void setCursor(Image image, int xHotspot, int yHotspot);

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
     * <p>Check if the display is resizable</p>
     *
     * @return <code>true</code> if the display is resizable, <code>false</code> otherwise
     */
    boolean isResizable();

    /**
     * <p>Check if the display is decorated</p>
     *
     * @return <code>true</code> if the display is decorated, <code>false</code> otherwise
     */
    boolean isDecorated();

    /**
     * <p>Check if the display is in windowed mode</p>
     *
     * @return <code>true</code> if the display is in windowed mode, <code>false</code> otherwise
     */
    boolean isWindowed();

    /**
     * <p>Check if the display is in fullscreen mode</p>
     *
     * @return <code>true</code> if the display is in fullscreen mode, <code>false</code> otherwise
     */
    boolean isFullscreen();

    /**
     * <p>Check if the display is active</p>
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
    ImmutableCollection<DisplayMode> getAvailableDisplayModes();
}
