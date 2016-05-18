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
package org.quark.engine.media.openal.audio;

import org.quark.engine.mathematic.MutableVector3f;
import org.quark.engine.mathematic.Vector3f;
import org.quark.engine.mathematic.Vector3i;
import org.quark.engine.media.openal.ALComponent;

/**
 * <code>AudioSource</code> represent an audio source that can be manipulated.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AudioSource extends ALComponent {
    public final static int CONCEPT_POSITION = (1 << 1);
    public final static int CONCEPT_DIRECTION = (1 << 2);
    public final static int CONCEPT_VELOCITY = (1 << 3);
    public final static int CONCEPT_PITCH = (1 << 4);
    public final static int CONCEPT_VOLUME = (1 << 5);
    public final static int CONCEPT_LOOPING = (1 << 6);

    /**
     * Represent the minimum possible value of the pitch value.
     */
    public final static float MIN_PITCH = 0.5f;

    /**
     * Represent the maximum possible value of the pitch value.
     */
    public final static float MAX_PITCH = 2.0f;

    /**
     * Represent the minimum possible value of the volume value.
     */
    public final static float MIN_VOLUME = 0.0f;

    /**
     * Represent the maximum possible value of the volume value.
     */
    public final static float MAX_VOLUME = 1.0f;

    private final MutableVector3f mPosition = MutableVector3f.zero();
    private final MutableVector3f mDirection = MutableVector3f.zero();
    private final MutableVector3f mVelocity = MutableVector3f.zero();
    private float mPitch;
    private float mVolume;
    private boolean mLooping;
    private boolean mDirectional;
    private boolean mPositional;

    /**
     * <p>Change the position of the audio</p>
     *
     * @param position the vector that contain(s) the coordinate(s) of the position
     *
     * @see #setPosition(float, float, float)
     */
    public void setPosition(Vector3i position) {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    /**
     * <p>Change the position of the audio</p>
     *
     * @param position the vector that contain(s) the coordinate(s) of the position
     *
     * @see #setPosition(float, float, float)
     */
    public void setPosition(Vector3f position) {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    /**
     * <p>Change the position of the audio</p>
     *
     * @param x the new x coordinate of the position
     * @param y the new y coordinate of the position
     * @param z the new z coordinate of the position
     *
     * @see #setPosition(Vector3f)
     * @see #setPosition(Vector3i)
     */
    public void setPosition(float x, float y, float z) {
        if (mPosition.getX() != x || mPosition.getY() != y || mPosition.getZ() != z) {
            mPosition.setXYZ(x, y, z);
            setUpdate(CONCEPT_POSITION);
        }
    }

    /**
     * <p>Change the direction of the audio</p>
     *
     * @param direction the vector that contain(s) the coordinate(s) of the direction
     *
     * @see #setDirection(float, float, float)
     */
    public void setDirection(Vector3i direction) {
        setDirection(direction.getX(), direction.getY(), direction.getZ());
    }

    /**
     * <p>Change the direction of the audio</p>
     *
     * @param direction the vector that contain(s) the coordinate(s) of the direction
     *
     * @see #setDirection(float, float, float)
     */
    public void setDirection(Vector3f direction) {
        setPosition(direction.getX(), direction.getY(), direction.getZ());
    }

    /**
     * <p>Change the direction of the audio</p>
     *
     * @param x the new x coordinate of the direction
     * @param y the new y coordinate of the direction
     * @param z the new z coordinate of the direction
     *
     * @see #setDirection(Vector3f)
     * @see #setDirection(Vector3i)
     */
    public void setDirection(float x, float y, float z) {
        if (mDirection.getX() != x || mDirection.getY() != y || mDirection.getZ() != z) {
            mDirection.setXYZ(x, y, z);
            setUpdate(CONCEPT_DIRECTION);
        }
    }

    /**
     * <p>Change the velocity of the audio</p>
     *
     * @param velocity the vector that contain(s) the coordinate(s) of the velocity
     *
     * @see #setPosition(float, float, float)
     */
    public void setVelocity(Vector3i velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    /**
     * <p>Change the velocity of the audio</p>
     *
     * @param velocity the vector that contain(s) the coordinate(s) of the velocity
     *
     * @see #setPosition(float, float, float)
     */
    public void setVelocity(Vector3f velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    /**
     * <p>Change the velocity of the audio</p>
     *
     * @param x the new x coordinate of the velocity
     * @param y the new y coordinate of the velocity
     * @param z the new z coordinate of the velocity
     *
     * @see #setVelocity(Vector3f)
     * @see #setVelocity(Vector3i)
     */
    public void setVelocity(float x, float y, float z) {
        if (mVelocity.getX() != x || mVelocity.getY() != y || mVelocity.getZ() != z) {
            mVelocity.setXYZ(x, y, z);
            setUpdate(CONCEPT_VELOCITY);
        }
    }

    /**
     * <p>Change the pitch of the audio</p>
     *
     * @param pitch the new pitch value of the audio
     *
     * @see #MIN_PITCH
     * @see #MAX_PITCH
     */
    public void setPitch(float pitch) {
        if (mPitch != pitch && pitch >= MIN_PITCH && pitch <= MAX_PITCH) {
            mPitch = pitch;
            setUpdate(CONCEPT_PITCH);
        }
    }

    /**
     * <p>Change the volume of the audio</p>
     *
     * @param volume the new volume of the audio (normalised)
     *
     * @see #MIN_VOLUME
     * @see #MAX_VOLUME
     */
    public void setVolume(float volume) {
        if (mVolume != volume && volume >= MIN_VOLUME && volume <= MAX_VOLUME) {
            mVolume = volume;
            setUpdate(CONCEPT_VOLUME);
        }
    }

    /**
     * <p>Change if the audio should use position</p>
     *
     * @param isPositional <code>true</code> if the audio should use position, <code>false</code> otherwise
     *
     * @see #setPosition(Vector3i)
     * @see #setPosition(Vector3f)
     * @see #setPosition(float, float, float)
     */
    public void setPositional(boolean isPositional) {
        if (mPositional != isPositional) {
            mPositional = isPositional;
            setUpdate(CONCEPT_POSITION);
        }
    }

    /**
     * <p>Change if the audio should use direction</p>
     *
     * @param isDirectional <code>true</code> if the audio should use direction, <code>false</code> otherwise
     *
     * @see #setDirection(Vector3i)
     * @see #setDirection(Vector3f)
     * @see #setDirection(float, float, float)
     */
    public void setDirectional(boolean isDirectional) {
        if (mDirectional != isDirectional) {
            mDirectional = isDirectional;
            setUpdate(CONCEPT_DIRECTION);
        }
    }

    /**
     * <p>Change if the audio should loop</p>
     *
     * @param isLooping <code>true</code> if the audio should loop, <code>false</code> otherwise
     */
    public void setLooping(boolean isLooping) {
        if (mLooping != isLooping) {
            mLooping = isLooping;
            setUpdate(CONCEPT_LOOPING);
        }
    }

    /**
     * <p>Get the position of the audio</p>
     *
     * @return the position of the audio
     *
     * @see #setPosition(Vector3i)
     * @see #setPosition(Vector3f)
     * @see #setPosition(float, float, float)
     * @see #setPositional(boolean)
     */
    public Vector3f getPosition() {
        return mPosition;
    }

    /**
     * <p>Get the direction of the audio</p>
     *
     * @return the direction of the audio
     *
     * @see #setDirection(Vector3i)
     * @see #setDirection(Vector3f)
     * @see #setDirection(float, float, float)
     * @see #setDirectional(boolean)
     */
    public Vector3f getDirection() {
        return mDirection;
    }

    /**
     * <p>Get the velocity of the audio</p>
     *
     * @return the velocity of the audio
     *
     * @see #setVelocity(Vector3i)
     * @see #setVelocity(Vector3f)
     * @see #setVelocity(float, float, float)
     */
    public Vector3f getVelocity() {
        return mVelocity;
    }

    /**
     * <p>Get the pitch of the audio</p>
     *
     * @return the pitch of the audio
     *
     * @see #setPitch(float)
     */
    public float getPitch() {
        return mPitch;
    }

    /**
     * <p>Get the volume of the audio</p>
     *
     * @return the volume of the audio
     *
     * @see #setVolume(float)
     */
    public float getVolume() {
        return mVolume;
    }

    /**
     * <p>Check if the audio is looping</p>
     *
     * @return <code>true</code> if the audio is looping, <code>false</code> otherwise
     */
    public boolean isLooping() {
        return mLooping;
    }

    /**
     * <p>Check if the audio is directional</p>
     *
     * @return <code>true</code> if the audio is directional, <code>false</code> otherwise
     */
    public boolean isDirectional() {
        return mDirectional;
    }

    /**
     * <p>Check if the audio is positional</p>
     *
     * @return <code>true</code> if the audio is positional, <code>false</code> otherwise
     */
    public boolean isPositional() {
        return mPositional;
    }
}