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
package com.github.wolftein.quark.system;

/**
 * <code>DisplayLifecycle</code> encapsulate the lifecycle of {@link Display}.
 */
public interface DisplayLifecycle {
    /**
     * <p>Called when the display is created</p>
     *
     * @see #onDispose()
     */
    void onCreate();

    /**
     * <p>Called when the display has resize</p>
     *
     * @param width  the new width (in pixel) of the display
     * @param height the new height (in pixel) of the display
     */
    void onResize(int width, int height);

    /**
     * <p>Called when the display require to render</p>
     *
     * @param time the time since the last render
     */
    void onRender(float time);

    /**
     * <p>Called when the display has been pause from a running state</p>
     *
     * @see #onResume()
     */
    void onPause();

    /**
     * <p>Called when the display has been resume from a pause state</p>
     *
     * @see #onPause()
     */
    void onResume();

    /**
     * <p>Called when the display is disposed</p>
     *
     * @see #onCreate()
     */
    void onDispose();
}
