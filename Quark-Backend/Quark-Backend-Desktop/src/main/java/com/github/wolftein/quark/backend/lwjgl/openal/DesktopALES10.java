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
package com.github.wolftein.quark.backend.lwjgl.openal;

import com.github.wolftein.quark.system.utility.array.Float32Array;
import com.github.wolftein.quark.system.utility.array.Int8Array;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;
import com.github.wolftein.quark.audio.AudioManager;

import java.nio.ByteBuffer;

/**
 * Implementation for {@link AudioManager.ALES10}.
 */
public final class DesktopALES10 implements AudioManager.ALES10 {
    private long mDevice = MemoryUtil.NULL;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean alcCreateContext() {
        if ((mDevice = ALC10.alcOpenDevice((ByteBuffer) null)) != MemoryUtil.NULL) {
            final ALCCapabilities capabilities = ALC.createCapabilities(mDevice);

            //!
            //! Create the new context
            //!
            final long context = ALC10.alcCreateContext(mDevice, (int[]) null);

            if (context != MemoryUtil.NULL) {
                ALC10.alcMakeContextCurrent(context);

                //!
                //! Create the capabilities of the current context.
                //!
                AL.createCapabilities(capabilities);
            } else {
                ALC10.alcCloseDevice(mDevice);

                //!
                //! Revert the device that has been open.
                //!
                mDevice = MemoryUtil.NULL;
            }
        }
        return (mDevice != MemoryUtil.NULL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alcDestroyContext() {
        ALC10.alcDestroyContext(ALC10.alcGetCurrentContext());
        ALC10.alcCloseDevice(mDevice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenBuffers() {
        return AL10.alGenBuffers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenSources() {
        return AL10.alGenSources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteBuffers(int name) {
        AL10.alDeleteBuffers(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteSources(int name) {
        AL10.alDeleteSources(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePause(int name) {
        AL10.alSourcePause(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePlay(int name) {
        AL10.alSourcePlay(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceStop(int name) {
        AL10.alSourceStop(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcei(int name, int type, int value) {
        AL10.alSourcei(name, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value) {
        AL10.alSourcef(name, type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value1, float value2, float value3) {
        AL10.alSource3f(name, type, value1, value2, value3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value) {
        AL10.alListenerf(type, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value1, float value2, float value3) {
        AL10.alListener3f(type, value1, value2, value3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, Float32Array value) {
        AL10.nalListenerfv(AL10.AL_ORIENTATION, MemoryUtil.memAddress(value.<ByteBuffer>data()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGetSourcei(int name, int type) {
        return AL10.alGetSourcei(name, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alSourceUnqueueBuffers(int name) {
        return AL10.alSourceUnqueueBuffers(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceQueueBuffers(int name, int id) {
        AL10.alSourceQueueBuffers(name, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alBufferData(int name, int format, Int8Array data, int rate) {
        AL10.alBufferData(name, format, data.<ByteBuffer>data(), rate);
    }
}
