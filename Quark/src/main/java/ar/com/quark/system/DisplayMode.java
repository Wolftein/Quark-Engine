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

/**
 * <code>DisplayMode</code> describe display properties for {@link Display}.
 */
public final class DisplayMode {
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
