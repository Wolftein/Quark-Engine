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
import org.teavm.jso.webaudio.*;

import java.util.*;

/**
 * Implementation for {@link AudioManager.ALES10}.
 */
public final class WebOpenALES10 implements AudioManager.ALES10 {
    public static AudioContext mDevice;

    /**
     * Hold all factories for all WebAL component(s).
     */
    protected final Map<Integer, WebALBuffer> mBufferFactory = new HashMap<>();
    protected final Map<Integer, WebALSource> mSourceFactory = new HashMap<>();
    protected final WebALListener mListener = new WebALListener();

    /**
     * Hold all unique identifier(s) for all WebAL component(s).
     */
    protected int mBuffer = 1;
    protected int mSource = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean alcCreateContext() {
        final boolean isValid = (mDevice = AudioContext.create()) != null;

        if (isValid) {
            mListener.initialise();
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alcDestroyContext() {
        if (mDevice != null) {
            mDevice.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenBuffers() {
        final int id = mBuffer++;

        mBufferFactory.put(id, new WebALBuffer());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGenSources() {
        final int id = mSource++;

        mSourceFactory.put(id, new WebALSource());

        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteBuffers(int name) {
        mBufferFactory.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alDeleteSources(int name) {
        mSourceFactory.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePause(int name) {
        mSourceFactory.get(name).pause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcePlay(int name) {
        mSourceFactory.get(name).play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceStop(int name) {
        mSourceFactory.get(name).stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcei(int name, int type, int value) {
        final WebALSource source = mSourceFactory.get(name);

        switch (type) {
            case WebOpenALES10.AL_BUFFER:
                source.mBuffer = value;
                break;
            case WebOpenALES10.AL_SOURCE_RELATIVE:
                source.mRelative = value;
                break;
            case WebOpenALES10.AL_LOOPING:
                if (source.mAudio != null) {
                    source.mAudio.setLoop(value == AudioManager.ALES10.AL_TRUE);
                }
                source.mLooping = value;
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value) {
        final WebALSource source = mSourceFactory.get(name);

        switch (type) {
            case WebOpenALES10.AL_REFERENCE_DISTANCE:
                source.mPanner.setRefDistance(value);
                break;
            case WebOpenALES10.AL_MAX_DISTANCE:
                source.mPanner.setMaxDistance(value);
                break;
            case WebOpenALES10.AL_CONE_INNER_ANGLE:
                source.mPanner.setConeInnerAngle(value);
                break;
            case WebOpenALES10.AL_CONE_OUTER_ANGLE:
                source.mPanner.setConeOuterAngle(value);
                break;
            case WebOpenALES10.AL_CONE_OUTER_GAIN:
                source.mPanner.setConeOuterGain(value);
                break;
            case WebOpenALES10.AL_PITCH:
                if (source.mAudio != null) {
                    source.mAudio.getPlaybackRate().setValue(value);
                }
                source.mPitch = value;
                break;
            case WebOpenALES10.AL_GAIN:
                source.mGain.getGain().setValue(value);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourcef(int name, int type, float value1, float value2, float value3) {
        final WebALSource source = mSourceFactory.get(name);

        switch (type) {
            case WebOpenALES10.AL_POSITION:
                if (source.mRelative == AudioManager.ALES10.AL_TRUE) {
                    source.mPanner.setPosition(
                            value1 - mListener.mPositionX,
                            value2 - mListener.mPositionY,
                            value3 - mListener.mPositionZ);
                } else {
                    source.mPanner.setPosition(value1, value2, value3);
                }
                break;
            case WebOpenALES10.AL_DIRECTION:
                source.mPanner.setOrientation(value1, value2, value3);
                break;
            case WebOpenALES10.AL_VELOCITY:
                source.mPanner.setVelocity(value1, value2, value3);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value) {
        switch (type) {
            case WebOpenALES10.AL_GAIN:
                mListener.mGain.getGain().setValue(value);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, float value1, float value2, float value3) {
        switch (type) {
            case WebOpenALES10.AL_POSITION:
                mListener.mAuthority.setPosition(value1, value2, value3);
                mListener.mPositionX = value1;
                mListener.mPositionY = value2;
                mListener.mPositionZ = value3;
                break;
            case WebOpenALES10.AL_VELOCITY:
                mListener.mAuthority.setVelocity(value1, value2, value3);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alListenerf(int type, Float32Array value) {
        switch (type) {
            case WebOpenALES10.AL_ORIENTATION:
                mListener.mAuthority.setOrientation(
                        value.read(), value.read(), value.read(),
                        value.read(), value.read(), value.read());
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alGetSourcei(int name, int type) {
        final WebALSource source = mSourceFactory.get(name);

        switch (type) {
            case WebOpenALES10.AL_SOURCE_STATE:
                return source.mState;
            case WebOpenALES10.AL_BUFFERS_PROCESSED:
                return source.mBufferProcessed.size();
        }
        return WebOpenALES10.AL_NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int alSourceUnqueueBuffers(int name) {
        final WebALSource source = mSourceFactory.get(name);

        //!
        //! Enqueue a buffer from the processed queue.
        //!
        return source.mBufferProcessed.isEmpty() ? AL_NONE : source.mBufferProcessed.poll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alSourceQueueBuffers(int name, int id) {
        final WebALSource source = mSourceFactory.get(name);

        //!
        //! Queue a buffer into the enqueue queue.
        //!
        source.mBufferEnqueue.add(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void alBufferData(int name, int format, Int8Array data, int rate) {
        final int alLength;
        final int alChannel;

        //!
        //! Calculate the length and channel of the format.
        //!
        switch (format) {
            case AudioManager.ALES10.AL_FORMAT_MONO8:
                alLength = 0x01;
                alChannel = 0x01;
                break;
            case AudioManager.ALES10.AL_FORMAT_MONO16:
                alLength = 0x02;
                alChannel = 0x01;
                break;
            case AudioManager.ALESExtension.AL_FORMAT_MONO_FLOAT32:
                alLength = 0x04;
                alChannel = 0x01;
                break;
            case AudioManager.ALES10.AL_FORMAT_STEREO8:
                alLength = 0x01;
                alChannel = 0x02;
                break;
            case AudioManager.ALES10.AL_FORMAT_STEREO16:
                alLength = 0x02;
                alChannel = 0x02;
                break;
            case AudioManager.ALESExtension.AL_FORMAT_STEREO_FLOAT32:
                alLength = 0x04;
                alChannel = 0x02;
                break;
            default:
                throw new IllegalArgumentException("Format " + format + " is not supported by <WebOpenALES10>");
        }

        //!
        //! Create the audio buffer that will hold all the byte(s).
        //!
        final AudioBuffer alBuffer = mDevice.createBuffer(
                alChannel, data.capacity() / (alLength * alChannel), rate);

        //!
        //! Transform all byte(s) into format byte(s).
        //!
        final List<org.teavm.jso.typedarrays.Float32Array> array = new ArrayList<>(alChannel);
        for (int i = 0; i < alChannel; i++) {
            array.add(alBuffer.getChannelData(i));
        }

        for (int i = 0, j = data.capacity() / (alLength * alChannel); i < j; i++) {
            for (int k = 0; k < alChannel; k++) {
                switch (alLength) {
                    case 1:
                        array.get(k).set(i, -1.0F * data.read() * (2.0F / 256.0F));
                        break;

                    case 2:
                        array.get(k).set(i, data.readInt16() / 32768.0F);
                        break;

                    case 4:
                        array.get(k).set(i, data.readFloat32());
                        break;
                }
            }
        }
        mBufferFactory.get(name).mBuffer = alBuffer;
    }

    /**
     * <code>WebALBuffer</code> represent an <b>OpenAL</b> buffer.
     */
    private final class WebALBuffer {
        /**
         * Hold the {@link AudioBuffer} attached to the buffer.
         */
        public AudioBuffer mBuffer;

        /**
         * <p>Check if the buffer is valid</p>
         *
         * @return <code>true</code> if the buffer is valid, <code>false</code> otherwise
         */
        public boolean isValid() {
            return mBuffer != null;
        }
    }

    /**
     * <code>WebALSource</code> represent an <b>OpenAL</b> source.
     */
    private final class WebALSource {
        /**
         * Hold the {@link PannerNode} of the source.
         */
        public final PannerNode mPanner;

        /**
         * Hold the {@link GainNode} of the source.
         */
        public final GainNode mGain;

        /**
         * Hold the {@link AudioBufferSourceNode} of the source.
         */
        public AudioBufferSourceNode mAudio;

        /**
         * Hold the unique identifier of the attached buffer.
         */
        public int mBuffer = AudioManager.ALES10.AL_NONE;

        /**
         * Hold the AL_LOOPING flag of the source.
         */
        public int mLooping = AudioManager.ALES10.AL_FALSE;

        /**
         * Hold the AL_SOURCE_RELATIVE flag of the source.
         */
        public int mRelative = AudioManager.ALES10.AL_TRUE;

        /**
         * Hold the state (AL_PLAYING, AL_STOPPED, AL_PAUSED) of the source.
         */
        public int mState = AudioManager.ALES10.AL_STOPPED;

        /**
         * Hold the pitch of the source.
         */
        public float mPitch;

        /**
         * Hold the timing of the source.
         */
        public double mStartTime = 0.0D, mBufferTime = 0.0D;

        /**
         * Hold all {@link WebALBuffer} enqueue for processing.
         */
        public final Queue<Integer> mBufferEnqueue = new LinkedList<>();

        /**
         * Hold all {@link WebALBuffer} processed.
         */
        public final Queue<Integer> mBufferProcessed = new LinkedList<>();

        /**
         * <p>Constructor</p>
         */
        public WebALSource() {
            mPanner = mDevice.createPanner();
            mPanner.setPanningModel(PannerNode.MODEL_HRTF);
            mPanner.setDistanceModel(PannerNode.DISTANCE_MODEL_INVERSE);
            mPanner.connect(mGain = mDevice.createGain());

            //!
            //! Attach the output node to the context.
            //!
            mGain.connect(mDevice.getDestination());
        }

        /**
         * <p>Change the source state to {@link WebOpenALES10#AL_PAUSED}</p>
         */
        public void pause() {
            //!
            //! Set the time when the source has been pause.
            //!
            mBufferTime = mDevice.getCurrentTime() - mStartTime;

            //!
            //! Stop the node and remove it.
            //!
            mAudio.stop();
            mAudio.disconnect();
            mAudio = null;

            //!
            //! Update the new state of the source.
            //!
            mState = AudioManager.ALES10.AL_PAUSED;
        }

        /**
         * <p>Change the source state to {@link WebOpenALES10#AL_PLAYING}</p>
         */
        public void play() {
            if (mState != AL_PAUSED) {
                //!
                //! Mark every buffer as processed.
                //!
                while (!mBufferEnqueue.isEmpty()) {
                    mBufferProcessed.add(mBufferEnqueue.poll());
                }
                mBufferTime = 0.0D;
            }

            mAudio = mDevice.createBufferSource();
            mAudio.setOnEnded(event -> {
                if (isStreaming()) {
                    //!
                    //! Add the buffer to the processed queue.
                    //!
                    mBufferProcessed.add(mBuffer);

                    //!
                    //! Check if we can still stream.
                    //!
                    if (!mBufferEnqueue.isEmpty()) {
                        play();
                    } else {
                        mBuffer = AudioManager.ALES10.AL_NONE;

                        stop();
                    }
                } else if (mLooping == AudioManager.ALES10.AL_FALSE) {
                    stop();
                }
            });
            mAudio.connect(mPanner);

            //!
            //! Set the looping and pitch feature.
            //!
            mAudio.setLoop(mLooping == AudioManager.ALES10.AL_TRUE);
            mAudio.getPlaybackRate().setValue(mPitch);

            //!
            //! Check if the source is streaming data.
            //!
            if (isStreaming()) {
                mBuffer = mBufferEnqueue.poll();
            }

            //!
            //! Grab the current buffer and attach it to the node.
            //!
            final WebALBuffer buffer = mBufferFactory.get(mBuffer);
            if (buffer != null && buffer.isValid()) {
                mAudio.setBuffer(buffer.mBuffer);

                //!
                //! Check if it should resume or play.
                //!
                mStartTime = mDevice.getCurrentTime();

                if (mState == AL_PAUSED) {
                    mAudio.start(0, mBufferTime % (mBuffer == AL_NONE ? 0 : buffer.mBuffer.getDuration()));
                } else {
                    mAudio.start(0, 0);
                }

                //!
                //! Update the new state of the source.
                //!
                mState = AudioManager.ALES10.AL_PLAYING;
            }
        }

        /**
         * <p>Change the source state to {@link WebOpenALES10#AL_STOPPED}</p>
         */
        public void stop() {
            //!
            //! Stop the node and remove it.
            //!
            mAudio.stop();
            mAudio.disconnect();
            mAudio = null;

            //!
            //! Mark every buffer as processed.
            //!
            if (mBuffer != AudioManager.ALES10.AL_NONE) {
                mBufferProcessed.add(mBuffer);
            }
            while (!mBufferEnqueue.isEmpty()) {
                mBufferProcessed.add(mBufferEnqueue.poll());
            }
            mBufferTime = 0.0D;

            //!
            //! Update the new state of the source.
            //!
            mState = AudioManager.ALES10.AL_STOPPED;
        }

        /**
         * <p>Check if the source is streaming</p>
         *
         * @return <code>true</code> if the source is streaming, <code>false</code> otherwise
         */
        public boolean isStreaming() {
            return mBufferEnqueue.size() > 0;
        }
    }

    /**
     * <code>WebALListener</code> represent an <b>OpenAL</b> listener.
     */
    private final class WebALListener {
        /**
         * Hold the {@link PannerNode} of the listener.
         */
        public PannerNode mPanner;

        /**
         * Hold the {@link GainNode} of the listener.
         */
        public GainNode mGain;

        /**
         * Hold the {@link AudioListener} of the listener.
         */
        public AudioListener mAuthority;

        /**
         * Hold the position of the listener.
         */
        public float mPositionX = 0.0F, mPositionY = 0.0F, mPositionZ = 0.0F;

        /**
         * <p>Initialise the component</p>
         */
        public void initialise() {
            //!
            //! Use the context listener.
            //!
            mAuthority = mDevice.getListener();

            //!
            //! Create the panner and gain node.
            //!
            mPanner = mDevice.createPanner();
            //mPanner.setDistanceModel(PannerNode.DISTANCE_MODEL_INVERSE);
            mPanner.connect(mGain = mDevice.createGain());

            //!
            //! Attach the output node to the context.
            //!
            mGain.connect(mDevice.getDestination());
        }
    }
}
