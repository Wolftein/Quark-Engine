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
package ar.com.quark.audio;

import ar.com.quark.audio.factory.FactoryStaticAudio;
import ar.com.quark.audio.factory.FactoryStreamingAudio;
import ar.com.quark.mathematic.ImmutableVector3f;
import ar.com.quark.mathematic.Vector3f;
import ar.com.quark.utility.Manageable;
import ar.com.quark.utility.buffer.BufferFactory;
import ar.com.quark.utility.buffer.Float32Buffer;
import ar.com.quark.utility.buffer.Int8Buffer;
import org.eclipse.collections.api.stack.primitive.MutableIntStack;
import org.eclipse.collections.impl.factory.primitive.IntStacks;

/**
 * Implementation for {@link AudioManager}.
 */
public final class AudioManagerImpl implements AudioManager {
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
     * Hold the context for {@link ALES10}.
     */
    private ALES10 mAL;

    /**
     * Hold all source(s).
     */
    private final AudioSource[] mAttachments = new AudioSource[MAX_SOURCE];

    /**
     * Hold all source(s) being available.
     */
    private final MutableIntStack mSources = IntStacks.mutable.empty();

    /**
     * Hold all streaming buffer(s).
     */
    private final MutableIntStack mBuffers = IntStacks.mutable.empty();

    /**
     * Hold temporally buffer for streaming.
     */
    private final byte[] mTemp = new byte[MAX_BUFFER_CAPACITY];

    /**
     * Hold temporally buffer for the buffer.
     */
    private final Int8Buffer mTempArray = BufferFactory.allocateInt8(MAX_BUFFER_CAPACITY);

    /**
     * Hold temporally buffer for the listener.
     */
    private final Float32Buffer mTempFloat32Array = BufferFactory.allocateFloat32(6);

    /**
     * <p>Handle when the module initialise</p>
     *
     * @param al the audio implementation
     */
    public void onModuleCreate(ALES10 al) {
        mAL = al;

        if (mAL.alcCreateContext()) {
            //!
            //! Pre-generate all source(s).
            //!
            //! NOTE: All source(s) will be reusable.
            //!
            for (int i = 0; i < MAX_SOURCE; ++i) {
                mSources.push(mAL.alGenSources());
            }

            //!
            //! Pre-generate all buffer(s).
            //!
            //! NOTE: All buffer(s) will be reusable.
            //!
            for (int i = 0; i < MAX_BUFFER_STREAMING; ++i) {
                mBuffers.push(mAL.alGenBuffers());
            }
        }
    }

    /**
     * <p>Handle when the module destroy</p>
     */
    public void onModuleDestroy() {
        //!
        //! Stop all attachment(s).
        //!
        for (final AudioSource attachment : mAttachments) {
            //!
            //! Only proceed with valid attachment(s)
            //!
            if (attachment != null) {
                onRemoveSource(attachment);
            }
        }

        //!
        //! Clear all source(s) and buffer(s).
        //!
        while (mSources.notEmpty()) {
            mAL.alDeleteSources(mSources.pop());
        }
        while (mBuffers.notEmpty()) {
            mAL.alDeleteBuffers(mBuffers.pop());
        }

        //!
        //! Destroy the context.
        //!
        mAL.alcDestroyContext();
    }

    /**
     * <p>Handle when the module update</p>
     */
    public void onModuleUpdate() {
        for (final AudioSource attachment : mAttachments) {
            if (attachment != null) {
                onUpdateSource(attachment);
            }
        }
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
            source.setHandle(mSources.size() > 0 ? mSources.pop() : Manageable.INVALID_HANDLE);

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
            mAL.alSourceStop(source.getHandle());

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
        mAL.alSourcePlay(source.getHandle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && mAL.alGetSourcei(source.getHandle(), ALES10.AL_SOURCE_STATE) == ALES10.AL_PLAYING) {
            mAL.alSourcePause(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        for (final AudioSource attachment : mAttachments) {
            //!
            //! Only proceed with valid attachment(s)
            //!
            if (attachment != null) {
                pause(attachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && mAL.alGetSourcei(source.getHandle(), ALES10.AL_SOURCE_STATE) == ALES10.AL_PAUSED) {
            mAL.alSourcePlay(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        for (final AudioSource attachment : mAttachments) {
            //!
            //! Only proceed with valid attachment(s)
            //!
            if (attachment != null) {
                resume(attachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(AudioSource source) {
        if (source.getHandle() != Manageable.INVALID_HANDLE
                && mAL.alGetSourcei(source.getHandle(), ALES10.AL_SOURCE_STATE) != ALES10.AL_STOPPED) {
            mAL.alSourceStop(source.getHandle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        for (final AudioSource attachment : mAttachments) {
            //!
            //! Only proceed with valid attachment(s)
            //!
            if (attachment != null) {
                stop(attachment);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AudioListener listener) {
        final Vector3f position = listener.getPosition();
        mAL.alListenerf(ALES10.AL_POSITION, position.getX(), position.getY(), position.getZ());

        final Vector3f velocity = listener.getVelocity();
        mAL.alListenerf(ALES10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());

        final Vector3f up = listener.getUp();
        final Vector3f direction = listener.getDirection();

        mTempFloat32Array.rewind();
        mTempFloat32Array.write(direction.getX()).write(direction.getY()).write(direction.getZ());
        mTempFloat32Array.write(up.getX()).write(up.getY()).write(up.getZ());

        mAL.alListenerf(ALES10.AL_ORIENTATION, mTempFloat32Array);

        //!
        //! Set the listener's gain.
        //!
        mAL.alListenerf(ALES10.AL_GAIN, listener.getVolume());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Audio audio) {
        if (audio.getHandle() != Manageable.INVALID_HANDLE) {
            mAL.alDeleteBuffers(audio.setHandle(Manageable.INVALID_HANDLE));
        }
    }

    /**
     * <p>Handle when updating a source</p>
     */
    private synchronized void onUpdateSource(AudioSource source) {
        if (mAL.alGetSourcei(source.getHandle(), ALES10.AL_SOURCE_STATE) == ALES10.AL_STOPPED) {
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
                //!
                //! Proceed to update the streaming.
                //!
                onUpdateStreaming(source, (FactoryStreamingAudio) source.getAudio());
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

                mAL.alSourcei(id, ALES10.AL_SOURCE_RELATIVE, source.isPositional()
                        ? ALES10.AL_FALSE
                        : ALES10.AL_TRUE);
                mAL.alSourcef(id, ALES10.AL_POSITION, position.getX(), position.getY(), position.getZ());
                mAL.alSourcef(id, ALES10.AL_REFERENCE_DISTANCE, source.getDistance());
                mAL.alSourcef(id, ALES10.AL_MAX_DISTANCE, source.getMaxDistance());
            }

            //!
            //! [CONCEPT_DIRECTION].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_DIRECTION)) {
                final Vector3f direction = source.isDirectional() ? ImmutableVector3f.ZERO : source.getDirection();

                mAL.alSourcef(id, ALES10.AL_DIRECTION, direction.getX(), direction.getY(), direction.getZ());

                mAL.alSourcef(id, ALES10.AL_CONE_INNER_ANGLE, source.getInnerAngle());
                mAL.alSourcef(id, ALES10.AL_CONE_OUTER_ANGLE, source.getOuterAngle());
                mAL.alSourcef(id, ALES10.AL_CONE_OUTER_GAIN, source.isDirectional() ? 1.0f : 0.0f);
            }

            //!
            //! [CONCEPT_VELOCITY].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_VELOCITY)) {
                final Vector3f velocity = source.isPositional() ? ImmutableVector3f.ZERO : source.getVelocity();

                mAL.alSourcef(id, ALES10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
            }

            //!
            //! [CONCEPT_PITCH].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_PITCH)) {
                mAL.alSourcef(id, ALES10.AL_PITCH, source.getPitch());
            }

            //!
            //! [CONCEPT_VOLUME].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_VOLUME)) {
                mAL.alSourcef(id, ALES10.AL_GAIN, source.getVolume());
            }

            //!
            //! [CONCEPT_LOOPING].
            //!
            if (force || source.hasUpdate(AudioSource.CONCEPT_LOOPING)) {
                mAL.alSourcei(id, ALES10.AL_LOOPING,
                        !source.getAudio().isStreaming() && source.isLooping()
                                ? ALES10.AL_TRUE
                                : ALES10.AL_FALSE);
            }
            source.setUpdated();
        }
    }

    /**
     * <p>Handle when updating an streaming audio data</p>
     *
     * @see #onUpdateStreaming(AudioSource, FactoryStreamingAudio)
     */
    private boolean onUpdateData(FactoryStreamingAudio audio, int buffer) {
        final int length = audio.read(mTemp, 0, mTemp.length);

        if (length > 0) {
            mTempArray.clear();
            mTempArray.write(mTemp, 0, length);
            mTempArray.flip();
            mAL.alBufferData(buffer, audio.getFormat().eType, mTempArray, audio.getRate());
        }
        return (length > 0);
    }

    /**
     * <p>Handle when updating an streaming audio</p>
     *
     * @see #onUpdateData(FactoryStreamingAudio, int)
     */
    private void onUpdateStreaming(AudioSource source, FactoryStreamingAudio audio) {
        int processed = mAL.alGetSourcei(source.getHandle(), ALES10.AL_BUFFERS_PROCESSED);

        //!
        //! While there is free buffer(s), fill them.
        //!
        while ((processed--) != 0) {
            final int free = mAL.alSourceUnqueueBuffers(source.getHandle());

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
                    mAL.alSourceQueueBuffers(source.getHandle(), free);
                }
            } else if (isValid) {
                mAL.alSourceQueueBuffers(source.getHandle(), free);
            }
        }
    }

    /**
     * <p>Handle when attaching an audio</p>
     *
     * @see #onAttachSource(AudioSource, FactoryStaticAudio)
     * @see #onAttachSource(AudioSource, FactoryStreamingAudio)
     */
    private void onAttachSource(AudioSource source, Audio audio) {
        if (audio.isStreaming()) {
            onAttachSource(source, (FactoryStreamingAudio) audio);
        } else {
            onAttachSource(source, (FactoryStaticAudio) audio);
        }
    }

    /**
     * <p>Handle when attaching a static audio</p>
     */
    private void onAttachSource(AudioSource source, FactoryStaticAudio audio) {
        if (audio.getHandle() == Manageable.INVALID_HANDLE) {
            audio.setHandle(mAL.alGenBuffers());

            //!
            //! Proceed to handle static data.
            //!
            mAL.alBufferData(audio.getHandle(), audio.getFormat().eType, audio.getData(), audio.getRate());

            //!
            //! Once uploaded, we don't need to upload it anymore.
            //!
            BufferFactory.deallocate(audio.getData());
        }
        mAL.alSourcei(source.getHandle(), ALES10.AL_BUFFER, audio.getHandle());
    }

    /**
     * <p>Handle when attaching a streaming audio</p>
     */
    private void onAttachSource(AudioSource source, FactoryStreamingAudio audio) {
        if (audio.getHandle() == Manageable.INVALID_HANDLE) {
            //!
            //! Poll an available buffer and query them in the target source.
            //!
            for (int i = 0; i < MAX_BUFFER_COUNT; i++) {
                final int id = mBuffers.pop();

                //!
                //! Fill the data in the buffer.
                //!
                if (onUpdateData(audio, id)) {
                    mAL.alSourceQueueBuffers(source.getHandle(), id);
                } else {
                    mBuffers.push(id);
                }
            }
        }
    }

    /**
     * <p>Handle when detaching an audio</p>
     *
     * @see #onDetachSource(AudioSource, FactoryStaticAudio)
     * @see #onDetachSource(AudioSource, FactoryStreamingAudio)
     */
    private void onDetachSource(AudioSource source, Audio audio) {
        if (audio.isStreaming()) {
            onDetachSource(source, (FactoryStreamingAudio) audio);
        } else {
            onDetachSource(source, (FactoryStaticAudio) audio);
        }
    }

    /**
     * <p>Handle when detaching a {@link FactoryStaticAudio}</p>
     */
    private void onDetachSource(AudioSource source, FactoryStaticAudio audio) {
        mAL.alSourcei(source.getHandle(), ALES10.AL_BUFFER, ALES10.AL_NONE);
    }

    /**
     * <p>Handle when detaching a {@link FactoryStreamingAudio}</p>
     */
    private void onDetachSource(AudioSource source, FactoryStreamingAudio audio) {
        for (int i = 0; i < MAX_BUFFER_CAPACITY; ++i) {
            mBuffers.push(mAL.alSourceUnqueueBuffers(source.getHandle()));
        }
        audio.reset();
    }

    /**
     * <p>Handle when an {@link AudioSource} requires to be removed</p>
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
        mSources.push(source.setHandle(Manageable.INVALID_HANDLE));
    }
}
