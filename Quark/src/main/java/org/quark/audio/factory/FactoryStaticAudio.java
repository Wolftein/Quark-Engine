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
package org.quark.audio.factory;

import org.quark.audio.Audio;
import org.quark.audio.AudioFormat;
import org.quark.system.utility.Disposable;
import org.quark.system.utility.array.Int8Array;

import static org.quark.Quark.QKAudio;

/**
 * Specialised implementation for {@link Audio} that are being loaded at once.
 */
public final class FactoryStaticAudio extends Audio {
    private final Int8Array mData;

    /**
     * <p>Constructor</p>
     */
    public FactoryStaticAudio(Int8Array data, AudioFormat format, int duration, int rate) {
        super(format, duration, rate);
        mData = data;
    }

    /**
     * <p>Get the data of the audio</p>
     *
     * @return the data of the audio
     */
    public Int8Array getData() {
        return mData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStreaming() {
        return false;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        QKAudio.delete(this);
    }
}
