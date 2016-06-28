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
package org.quark.extension.niftyui;

import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.spi.sound.SoundDevice;
import de.lessvoid.nifty.spi.sound.SoundHandle;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;
import org.quark.audio.Audio;

import static org.quark.Quark.QKResources;

/**
 * <code>NiftySoundDevice</code> represent implementation of {@link SoundDevice}.
 */
public final class NiftySoundDevice implements SoundDevice {
    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(NiftyResourceLoader niftyResourceLoader) {
        //!
        //! NOTE: No need to implement this method.
        //!
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoundHandle loadSound(SoundSystem soundSystem, String filename) {
        return new NiftySoundHandle(QKResources.loadAsset(filename, new Audio.Descriptor(false)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoundHandle loadMusic(SoundSystem soundSystem, String filename) {
        return new NiftySoundHandle(QKResources.loadAsset(filename, new Audio.Descriptor(true)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(int delta) {
        //!
        //! NOTE: No need to implement this method.
        //!
    }
}
