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
package org.quark.system;

/**
 * <code>FrameworkSetting</code> encapsulate the setting of {@link Framework}.
 * <p>
 * All features may not be supported by the underlying implementation, check implementation for more information.
 */
public final class FrameworkSetting {
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

    private final Framework.Display mMode;
    private final String mTitle;
    private final int mSamples;
    private final boolean mSynchronised;
    private final boolean mDecorated;
    private final boolean mResizable;
    private final boolean mFullscreen;
    private final boolean mSRGB;

    /**
     * <p>Constructor</p>
     */
    public FrameworkSetting(Framework.Display mode, String title, int samples, int features) {
        mMode = mode;
        mTitle = title;
        mSamples = samples;
        mDecorated = (features & FLAG_DECORATED) != 0;
        mResizable = (features & FLAG_RESIZABLE) != 0;
        mFullscreen = (features & FLAG_FULLSCREEN) != 0;
        mSynchronised = (features & FLAG_SYNCHRONISED) != 0;
        mSRGB = (features & FLAG_SRGB) != 0;
    }

    /**
     * <p>Constructor</p>
     */
    public FrameworkSetting(Framework.Display mode, String title) {
        this(mode, title, 0, 0);
    }

    /**
     * <p>Get the display mode of the display</p>
     *
     * @return the display mode of the display
     */
    public Framework.Display getMode() {
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
        return mSynchronised;
    }

    /**
     * <p>Check if the display will be decorated</p>
     *
     * @return <code>true</code> if the display will be decorated, <code>false</code> otherwise
     */
    public boolean isDecorated() {
        return mDecorated;
    }

    /**
     * <p>Check if the display will be resizable</p>
     *
     * @return <code>true</code> if the display will be resizable, <code>false</code> otherwise
     */
    public boolean isResizable() {
        return mResizable;
    }

    /**
     * <p>Check if the display will be created in fullscreen</p>
     *
     * @return <code>true</code> if the display will be created in fullscreen, <code>false</code> otherwise
     */
    public boolean isFullscreen() {
        return mFullscreen;
    }

    /**
     * <p>Check if the display will be created using sRGB format</p>
     *
     * @return <code>true</code> if the display will be created using sRGB format, <code>false</code> otherwise
     */
    public boolean isSRGB() {
        return mSRGB;
    }
}
