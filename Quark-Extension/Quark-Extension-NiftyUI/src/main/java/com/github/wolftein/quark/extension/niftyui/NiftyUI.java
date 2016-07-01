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
package com.github.wolftein.quark.extension.niftyui;

import com.github.wolftein.quark.input.InputListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

import java.lang.reflect.Field;

/**
 * <code>NiftyUI</code> encapsulate all nifty-gui component(s).
 */
public final class NiftyUI extends Nifty {
    private NiftyRenderDevice mNiftyRenderDevice = null;

    /**
     * <p>Constructor</p>
     */
    public NiftyUI(InputListener listener) {
        super(
                new NiftyRenderDevice(),
                new NiftySoundDevice(),
                new NiftyInputSystem(listener),
                new AccurateTimeProvider());

        //!
        //! Required to get the render device implementation.
        //!
        try {
            final Field field
                    = getRenderEngine().getRenderDevice().getClass().getDeclaredField("internal");
            field.setAccessible(true);
            mNiftyRenderDevice = (NiftyRenderDevice) field.get(getRenderEngine().getRenderDevice());
        } catch (Throwable ignored) {
        }

        //!
        //! Register all resource(s) location for the extension.
        //!
        getResourceLoader().removeAllResourceLocations();
        getResourceLoader().addResourceLocation(new NiftyResourceLocation());
    }

    /**
     * <p>Notify {@link Nifty} to resize the viewport</p>
     */
    public void resize(int width, int height) {
        mNiftyRenderDevice.resize(width, height);
    }

    /**
     * <p>Notify {@link Nifty} to update and render the screen</p>
     */
    public void render() {
        render(true);
    }

    /**
     * <p>Notify {@link Nifty} to update and render the screen</p>
     *
     * @param clear <code>true</code> if it should clear the screen, <code>false</code> otherwise
     */
    public void render(boolean clear) {
        update();

        //!
        //! Call the underlying method to render the screen.
        //!
        super.render(clear);
    }
}
