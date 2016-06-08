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
package org.quark_engine.backend.lwjgl;

import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;
import org.quark_engine.audio.Audio;
import org.quark_engine.audio.AudioListener;
import org.quark_engine.audio.AudioManager;
import org.quark_engine.audio.AudioSource;
import org.quark_engine.audio.factory.FactoryAudioStatic;
import org.quark_engine.audio.factory.FactoryAudioStreaming;
import org.quark_engine.mathematic.ImmutableVector3f;
import org.quark_engine.mathematic.Vector3f;
import org.quark_engine.system.utility.Manageable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <a href="http://lwjgl.org">LWJGL 3</a> implementation for {@link AudioManager}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class LWJGLOpenAL implements AudioManager {
    /**
     * Represent the max capacity of a streaming buffer.
     */
    private static final int MAX_BUFFER_CAPACITY = 44100 * 2 * 2;

    /**
     * Represent the max number of streaming buffer.
     */
    private static final int MAX_BUFFER_COUNT = 5;

    /**
     * Represent the max number of source available.
     */
    private final static int MAX_SOURCE = 32;

    /**
     * Represent the max number of buffer available.
     */
    private final static int MAX_BUFFER_STREAMING = MAX_BUFFER_COUNT * MAX_SOURCE;

    /**
     * Hold the audio device.
     */
    private long mDevice = MemoryUtil.NULL;

    /**
     * Hold the audio capabilities.
     */
    private ALCCapabilities mCapabilities = null;

    /**
     * Hold all source(s).
     */
    private final AudioSource[] mAttachments = new AudioSource[MAX_SOURCE];

    /**
     * Hold all source(s) being available.
     */
    private final Queue<Integer> mSources = new LinkedList<>();

    /**
     * Hold all streaming buffer(s).
     */
    private final Queue<Integer> mBuffers = new LinkedList<>();

    /**
     * Hold temporally buffer for streaming.
     */
    private final byte[] mTemp = new byte[MAX_BUFFER_CAPACITY];

    /**
     * Hold temporally buffer for the listener.
     */
    private final ByteBuffer mTempForBuffer
            = ByteBuffer.allocateDirect(MAX_BUFFER_CAPACITY).order(ByteOrder.nativeOrder());
    private final FloatBuffer mTempForListener
            = ByteBuffer.allocateDirect(6 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

    /**
     * <p>Called to initialise the module</p>
     */
    protected void create() {
        if ((mDevice = ALC10.alcOpenDevice((ByteBuffer) null)) != MemoryUtil.NULL) {
            mCapabilities = ALC.createCapabilities(mDevice);

            //!
            //! Create the new context
            //!
            final long context = ALC10.alcCreateContext(mDevice, (int[]) null);

            if (context != MemoryUtil.NULL) {
                ALC10.alcMakeContextCurrent(context);
                org.lwjgl.openal.AL.createCapabilities(mCapabilities);

                //!
                //! Pre-generate all source(s).
                //!
                for (int i = 0; i < MAX_SOURCE; ++i) {
                    mSources.add(AL10.alGenSources());
                }

                //!
                //! Pre-generate all buffer(s).
                //!
                for (int i = 0; i < MAX_BUFFER_STREAMING; ++i) {
                    mBuffers.add(AL10.alGenBuffers());
                }
            } else {
                //!
                //! Reset each state.
                //!
                mDevice = MemoryUtil.NULL;
            }
        }
    }

    /**
     * <p>Called to update the module</p>
     */
    protected void update() {
        for (final AudioSource source : mAttachments) {
            if (source != null) {
                onUpdateSource(source);
            }
        }
    }

    /**
     * <p>Called to dispose the module</p>
     */
    protected void destroy() {
        //!
        //! Stop all attachment(s).
        //!
        for (final AudioSource source : mAttachments) {
            if (source != null) {
                onRemoveSource(source);
            }
        }

        //!
        //! Clear all source(s) and buffer(s).
        //!
        mSources.forEach(AL10::alDeleteSources);
        mSources.clear();
        mBuffers.forEach(AL10::alDeleteBuffers);
        mBuffers.clear();

        //!
        //! Close the device.
        //!
        ALC10.alcDestroyContext(ALC10.alcGetCurrentContext());
        ALC10.alcCloseDevice(mDevice);

        mDevice = MemoryUtil.NULL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void play(AudioSource source) {
        //!
        //! Update the source if not valid.
        //!
        if (source.getHandle() == Manageable.INVALID_HANDLE) {
            source.setHandle(mSources.size() > 0 ? mSources.poll() : Manageable.INVALID_HANDLE);

            //!
            //! Only proceed if the source is valid.
            //!
            if (source.getHandle() == Manageable.INVALID_HANDLE) {
                return;
            }
            mAttachments[source.getHandle() - 1] = source;
        } else {
            //!
            //! Stop the audio.
            //!
            AL10.alSourceStop(source.getHandle());

            //!
            //! Detach the audio from the source.
            //!
            onDetachSource(source, source.getAudio());
        }

        //!
        //! Update the source.
        //!
        onUpdateSource(source, true);
        onAttachSource(source, source.getAudio());

        //!
        //! Play the source.
        //!
        AL10.alSourcePlay(source.getHandle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && AL10.alGetSourcei(source.getHandle(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
            AL10.alSourcePause(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        for (AudioSource mAttachment : mAttachments) {
            if (mAttachment != null) {
                pause(mAttachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && AL10.alGetSourcei(source.getHandle(), AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED) {
            AL10.alSourcePlay(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        for (AudioSource mAttachment : mAttachments) {
            if (mAttachment != null) {
                resume(mAttachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && AL10.alGetSourcei(source.getHandle(), AL10.AL_SOURCE_STATE) != AL10.AL_STOPPED) {
            AL10.alSourceStop(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        for (AudioSource mAttachment : mAttachments) {
            if (mAttachment != null) {
                stop(mAttachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AudioListener listener) {
        final Vector3f position = listener.getPosition();
        final Vector3f velocity = listener.getVelocity();

        final Vector3f up = listener.getUp();
        final Vector3f direction = listener.getDirection();

        AL10.alListener3f(AL10.AL_POSITION, position.getX(), position.getY(), position.getZ());
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());

        mTempForListener.rewind();
        mTempForListener.put(direction.getX()).put(direction.getY()).put(direction.getZ());
        mTempForListener.put(up.getX()).put(up.getY()).put(up.getZ());
        mTempForListener.flip();
        AL10.alListenerfv(AL10.AL_ORIENTATION, mTempForListener);

        AL10.alListenerf(AL10.AL_GAIN, listener.getVolume());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(FactoryAudioStatic audio) {
        if (audio.getHandle() != Manageable.INVALID_HANDLE) {
            AL10.alDeleteBuffers(audio.setHandle(Manageable.INVALID_HANDLE));
        }

        //!
        //! Clear the buffer.
        //!
        //! NOTE: This is to ease some memory in cause it wasn't uploaded.
        //!
        audio.getData().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(FactoryAudioStreaming audio) {
        //!
        //! Close the stream.
        //!
        //! NOTE: Since streaming is using shared buffer, doesn't require to actually dispose them.
        //!
        audio.close();
    }

    /**
     * <p>Handle when updating an streaming audio data</p>
     *
     * @see #onUpdateStreaming(AudioSource, FactoryAudioStreaming)
     */
    private boolean onUpdateData(FactoryAudioStreaming audio, int buffer) {
        final int length = audio.read(mTemp, 0, mTemp.length);

        if (length > 0) {
            mTempForBuffer.clear();
            mTempForBuffer.put(mTemp, 0, length);
            mTempForBuffer.flip();
            AL10.alBufferData(buffer, audio.getFormat().eType, mTempForBuffer, audio.getRate());
        }
        return (length > 0);
    }

    /**
     * <p>Handle when updating a source</p>
     */
    private synchronized void onUpdateSource(AudioSource source) {
        if (AL10.alGetSourcei(source.getHandle(), AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED) {
            //!
            //! The source has stopped.
            //!
            onRemoveSource(source);
        } else {

            //!
            //! Proceed to update any property changed.
            //!
            onUpdateSource(source, false);

            if (source.getAudio().isStreaming()) {
                onUpdateStreaming(source, (FactoryAudioStreaming) source.getAudio());
            }
        }
    }

    /**
     * <p>Handle when updating a source's parameter(s)</p>
     */
    private void onUpdateSource(AudioSource source, boolean force) {
        if (force || source.hasUpdate()) {
            final int id = source.getHandle();

            //!
            //! [CONCEPT_POSITION].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_POSITION)) {
                final Vector3f position = source.isPositional() ? ImmutableVector3f.ZERO : source.getPosition();

                AL10.alSourcei(
                        id, AL10.AL_SOURCE_RELATIVE, source.isPositional() ? AL10.AL_FALSE : AL10.AL_TRUE);
                AL10.alSource3f(
                        id, AL10.AL_POSITION, position.getX(), position.getY(), position.getZ());

                AL10.alSourcef(
                        id, AL10.AL_REFERENCE_DISTANCE, source.getDistance());
                AL10.alSourcef(
                        id, AL10.AL_MAX_DISTANCE, source.getMaxDistance());
            }

            //!
            //! [CONCEPT_DIRECTION].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_DIRECTION)) {
                final Vector3f direction = source.isDirectional() ? ImmutableVector3f.ZERO : source.getDirection();

                AL10.alSource3f(id, AL10.AL_DIRECTION,
                        direction.getX(),
                        direction.getY(),
                        direction.getZ());

                AL10.alSourcef(id, AL10.AL_CONE_INNER_ANGLE,
                        source.getInnerAngle());
                AL10.alSourcef(id, AL10.AL_CONE_OUTER_ANGLE,
                        source.getOuterAngle());
                AL10.alSourcef(id, AL10.AL_CONE_OUTER_GAIN,
                        source.isDirectional() ? 1.0f : 0.0f);
            }

            //!
            //! [CONCEPT_VELOCITY].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_VELOCITY)) {
                final Vector3f velocity = source.isPositional() ? ImmutableVector3f.ZERO : source.getVelocity();

                AL10.alSource3f(id, AL10.AL_VELOCITY,
                        velocity.getX(),
                        velocity.getY(),
                        velocity.getZ());
            }

            //!
            //! [CONCEPT_PITCH].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_PITCH)) {
                AL10.alSourcef(id, AL10.AL_PITCH, source.getPitch());
            }

            //!
            //! [CONCEPT_VOLUME].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_VOLUME)) {
                AL10.alSourcef(id, AL10.AL_GAIN, source.getVolume());
            }

            //!
            //! [CONCEPT_LOOPING].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_LOOPING)) {
                AL10.alSourcei(id, AL10.AL_LOOPING,
                        source.getAudio() instanceof FactoryAudioStatic && source.isLooping()
                                ? AL10.AL_TRUE
                                : AL10.AL_FALSE);
            }
            source.setUpdated();
        }
    }

    /**
     * <p>Handle when updating an streaming audio</p>
     *
     * @see #onUpdateData(FactoryAudioStreaming, int)
     */
    private void onUpdateStreaming(AudioSource source, FactoryAudioStreaming audio) {
        int processed = AL10.alGetSourcei(source.getHandle(), AL10.AL_BUFFERS_PROCESSED);

        //!
        //! While there is free buffer(s), fill them.
        //!
        while ((processed--) != 0) {
            final int free = AL10.alSourceUnqueueBuffers(source.getHandle());

            //!
            //! Fill the data in the buffer.
            //!
            final boolean isValid = onUpdateData(audio, free);

            //!
            //! Check whenever the audio request to loop.
            //!
            if (source.isLooping() && !isValid) {
                audio.reset();

                //!
                //! Fill the data once more.
                //!
                if (onUpdateData(audio, free)) {
                    AL10.alSourceQueueBuffers(source.getHandle(), free);
                }
            } else if (isValid) {
                AL10.alSourceQueueBuffers(source.getHandle(), free);
            }
        }
    }

    /**
     * <p>Handle when attaching an audio</p>
     *
     * @see #onAttachSource(AudioSource, FactoryAudioStatic)
     * @see #onAttachSource(AudioSource, FactoryAudioStreaming)
     */
    private void onAttachSource(AudioSource source, Audio audio) {
        if (audio.isStreaming()) {
            onAttachSource(source, (FactoryAudioStreaming) audio);
        } else {
            onAttachSource(source, (FactoryAudioStatic) audio);
        }
    }

    /**
     * <p>Handle when attaching a static audio</p>
     */
    private void onAttachSource(AudioSource source, FactoryAudioStatic audio) {
        if (audio.getHandle() == Manageable.INVALID_HANDLE) {
            audio.setHandle(AL10.alGenBuffers());

            //!
            //! Proceed to handle static data.
            //!
            AL10.alBufferData(audio.getHandle(), audio.getFormat().eType, audio.getData(), audio.getRate());

            //!
            //! Once uploaded, we don't need to upload it anymore.
            //!
            audio.getData().clear();
        }
        AL10.alSourcei(source.getHandle(), AL10.AL_BUFFER, audio.getHandle());
    }

    /**
     * <p>Handle when attaching a streaming audio</p>
     */
    private void onAttachSource(AudioSource source, FactoryAudioStreaming audio) {
        if (audio.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Poll an available buffer and query them in the target source.
            //!
            for (int i = 0; i < MAX_BUFFER_COUNT; i++) {
                final int id = mBuffers.poll();

                //!
                //! Fill the data in the buffer.
                //!
                if (onUpdateData(audio, id)) {
                    AL10.alSourceQueueBuffers(source.getHandle(), id);
                } else {
                    mBuffers.add(id);
                }
            }
        }
    }

    /**
     * <p>Handle when detaching an audio</p>
     *
     * @see #onDetachSource(AudioSource, FactoryAudioStatic)
     * @see #onDetachSource(AudioSource, FactoryAudioStreaming)
     */
    private void onDetachSource(AudioSource source, Audio audio) {
        if (audio.isStreaming()) {
            onDetachSource(source, (FactoryAudioStreaming) audio);
        } else {
            onDetachSource(source, (FactoryAudioStatic) audio);
        }
    }

    /**
     * <p>Handle when detaching a static audio</p>
     */
    private void onDetachSource(AudioSource source, FactoryAudioStatic audio) {
        AL10.alSourcei(source.getHandle(), AL10.AL_BUFFER, AL10.AL_NONE);
    }

    /**
     * <p>Handle when detaching a streaming audio</p>
     */
    private void onDetachSource(AudioSource source, FactoryAudioStreaming audio) {
        for (int i = 0; i < MAX_BUFFER_CAPACITY; ++i) {
            mBuffers.add(AL10.alSourceUnqueueBuffers(source.getHandle()));
        }
        audio.reset();
    }

    /**
     * <p>Handle when a source requires to be removed</p>
     */
    private void onRemoveSource(AudioSource source) {
        //!
        //! Detach the audio from the source.
        //!
        onDetachSource(source, source.getAudio());

        //!
        //! Remove the entry from the being use list and add it back to the free attachment(s) list.
        //!
        mAttachments[source.getHandle() - 1] = null;
        mSources.add(source.setHandle(Manageable.INVALID_HANDLE));
    }
}
