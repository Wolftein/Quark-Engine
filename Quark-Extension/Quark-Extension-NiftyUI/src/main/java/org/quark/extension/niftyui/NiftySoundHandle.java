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

import de.lessvoid.nifty.spi.sound.SoundHandle;
import org.quark.audio.Audio;
import org.quark.audio.AudioSource;

import static org.quark.Quark.QKResources;

/**
 * <code>NiftySoundHandle</code> represent implementation of {@link SoundHandle}.
 */
public final class NiftySoundHandle implements SoundHandle {
    private final Audio mAudio;

    /**
     * Hold the source of the audio.
     */
    private final AudioSource mSource = new AudioSource();

    /**
     * Hold a flag that indicates the audio is being played.
     */
    private boolean mPlaying = false;

    /**
     * <p>Constructor</p>
     */
    public NiftySoundHandle(Audio audio) {
        mAudio = audio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play() {
        mSource.play(mAudio);
        mPlaying = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        mSource.stop();
        mPlaying = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVolume(float volume) {
        mSource.setVolume(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getVolume() {
        return mSource.getVolume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaying() {
        return mPlaying;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        QKResources.unload(mAudio);
    }
}
