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
package org.quark.backend.teavm.openal;

import org.quark.audio.AudioManager;
import org.quark.system.utility.array.Float32Array;
import org.quark.system.utility.array.Int8Array;

/**
 * <a href="http://teavm.org/">TeaVM</a> implementation for {@link AudioManager.ALES10}.
 */
public final class TeaVMOpenALES10 implements AudioManager.ALES10 {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean alcCreateContext() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alcDestroyContext() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenBuffers() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenSources() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteBuffers(int name) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteSources(int name) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePause(int name) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePlay(int name) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceStop(int name) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcei(int name, int type, int value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value1, float value2, float value3) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value1, float value2, float value3) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, Float32Array value) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGetSourcei(int name, int type) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alSourceUnqueueBuffers(int name) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceQueueBuffers(int name, int id) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alBufferData(int name, int format, Int8Array data, int rate) {

    }
}
