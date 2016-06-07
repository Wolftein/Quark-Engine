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
package org.quark_engine.audio;

import org.quark_engine.mathematic.*;

/**
 * <code>AudioListener</code> encapsulate the listener of the sound.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class AudioListener {
    private final MutableVector3f mPosition = MutableVector3f.zero();
    private final MutableVector3f mVelocity = MutableVector3f.zero();
    private final MutableVector3f mUp = MutableVector3f.unitY();
    private final MutableVector3f mDirection = MutableVector3f.unitZ();
    private float mVolume = 1.0f;

    /**
     * <p>Change the position of the listener</p>
     *
     * @param position the vector that contain(s) the coordinate(s) of the position
     *
     * @see #setPosition(float, float, float)
     */
    public void setPosition(Vector3i position) {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    /**
     * <p>Change the position of the listener</p>
     *
     * @param position the vector that contain(s) the coordinate(s) of the position
     *
     * @see #setPosition(float, float, float)
     */
    public void setPosition(Vector3f position) {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    /**
     * <p>Change the position of the listener</p>
     *
     * @param x the new x coordinate of the position
     * @param y the new y coordinate of the position
     * @param z the new z coordinate of the position
     *
     * @see #setPosition(Vector3f)
     * @see #setPosition(Vector3i)
     */
    public void setPosition(float x, float y, float z) {
        mPosition.setXYZ(x, y, z);
    }

    /**
     * <p>Change the velocity of the listener</p>
     *
     * @param velocity the vector that contain(s) the coordinate(s) of the velocity
     *
     * @see #setPosition(float, float, float)
     */
    public void setVelocity(Vector3i velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    /**
     * <p>Change the velocity of the listener</p>
     *
     * @param velocity the vector that contain(s) the coordinate(s) of the velocity
     *
     * @see #setPosition(float, float, float)
     */
    public void setVelocity(Vector3f velocity) {
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    /**
     * <p>Change the velocity of the listener</p>
     *
     * @param x the new x coordinate of the velocity
     * @param y the new y coordinate of the velocity
     * @param z the new z coordinate of the velocity
     *
     * @see #setVelocity(Vector3f)
     * @see #setVelocity(Vector3i)
     */
    public void setVelocity(float x, float y, float z) {
        mVelocity.setXYZ(x, y, z);
    }

    /**
     * <p>Change the rotation of the listener</p>
     *
     * @param up        the up (Y Unit) of the rotation
     * @param direction the direction (Z Unit) of the rotation
     */
    public void setRotation(Vector3f up, Vector3f direction) {
        mUp.setXYZ(up.getX(), up.getY(), up.getZ());
        mDirection.setXYZ(direction.getX(), direction.getY(), direction.getZ());
    }

    /**
     * <p>Change the volume of the listener</p>
     *
     * @param volume the new volume of the listener (normalised)
     *
     * @see AudioSource#MIN_VOLUME
     * @see AudioSource#MAX_VOLUME
     */
    public void setVolume(float volume) {
        mVolume = Math.max(Math.min(volume, AudioSource.MIN_VOLUME), AudioSource.MAX_VOLUME);
    }

    /**
     * <p>Get the position of the listener</p>
     *
     * @return the position of the listener
     *
     * @see #setPosition(Vector3i)
     * @see #setPosition(Vector3f)
     * @see #setPosition(float, float, float)
     */
    public Vector3f getPosition() {
        return mPosition;
    }

    /**
     * <p>Get the velocity of the listener</p>
     *
     * @return the velocity of the listener
     *
     * @see #setVelocity(Vector3i)
     * @see #setVelocity(Vector3f)
     * @see #setVelocity(float, float, float)
     */
    public Vector3f getVelocity() {
        return mVelocity;
    }

    /**
     * <p>Get the Y-Unit (rotation) of the listener</p>
     *
     * @return the Y-Unit (rotation) of the listener
     *
     * @see #setRotation(Vector3f, Vector3f)
     */
    public Vector3f getUp() {
        return mUp;
    }

    /**
     * <p>Get the Z-Unit (rotation) of the listener</p>
     *
     * @return the Z-Unit (rotation) of the listener
     *
     * @see #setRotation(Vector3f, Vector3f)
     */
    public Vector3f getDirection() {
        return mDirection;
    }

    /**
     * <p>Get the volume of the listener</p>
     *
     * @return the volume of the listener
     *
     * @see #setVolume(float)
     */
    public float getVolume() {
        return mVolume;
    }
}
