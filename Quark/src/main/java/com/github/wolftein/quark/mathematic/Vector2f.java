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
package com.github.wolftein.quark.mathematic;

/**
 * <code>Vector2f</code> encapsulate a 2 element vector that is represented by single precision floating.
 */
public abstract class Vector2f {
    protected float mX;
    protected float mY;

    /**
     * <p>Default constructor</p>
     */
    public Vector2f(float x, float y) {
        mX = x;
        mY = y;
    }

    /**
     * <p>Get the x component of the vector</p>
     *
     * @return the x component of the vector
     */
    public final float getX() {
        return mX;
    }

    /**
     * <p>Get the y component of the vector</p>
     *
     * @return the y component of the vector
     */
    public final float getY() {
        return mY;
    }

    /**
     * <p>Get the floored value of the x component</p>
     *
     * @return the floored value of the x component
     */
    public final int getFloorX() {
        return (int) Math.floor(mX);
    }

    /**
     * <p>Get the floored value of the y component</p>
     *
     * @return the floored value of the y component
     */
    public final int getFloorY() {
        return (int) Math.floor(mY);
    }

    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T add(float scalar, T result) {
        result.mX = mX + scalar;
        result.mY = mY + scalar;
        return result;
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x      the value to add to the x component
     * @param y      the value to add to the y component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T add(float x, float y, T result) {
        result.mX = mX + x;
        result.mY = mY + y;
        return result;
    }

    /**
     * <p>Adds a provided vector to this vector</p>
     *
     * @param vector the vector to add to this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T add(Vector2f vector, T result) {
        result.mX = mX + vector.mX;
        result.mY = mY + vector.mY;
        return result;
    }

    /**
     * <p>Subtracts a scalar value from this vector</p>
     *
     * @param scalar the value to subtract from this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T sub(float scalar, T result) {
        result.mX = mX - scalar;
        result.mY = mY - scalar;
        return result;
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x      the value to subtract from the x component
     * @param y      the value to subtract from the y component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T sub(float x, float y, T result) {
        result.mX = mX - x;
        result.mY = mY - y;
        return result;
    }

    /**
     * <p>Subtracts the provided vector from this vector</p>
     *
     * @param vector the vector to sub to this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T sub(Vector2f vector, T result) {
        result.mX = mX - vector.mX;
        result.mY = mY - vector.mY;
        return result;
    }

    /**
     * <p>Multiplies a scalar value with this vector</p>
     *
     * @param scalar the value to multiply with this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T mul(float scalar, T result) {
        result.mX = mX * scalar;
        result.mY = mY * scalar;
        return result;
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x      the value to multiply with the x component
     * @param y      the value to multiply with the y component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T mul(float x, float y, T result) {
        result.mX = mX * x;
        result.mY = mY * y;
        return result;
    }

    /**
     * <p>Multiplies a provided vector with this vector</p>
     *
     * @param vector the vector to multiply with this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T mul(Vector2f vector, T result) {
        result.mX = mX * vector.mX;
        result.mY = mY * vector.mY;
        return result;
    }

    /**
     * <p>Divides a scalar value with this vector</p>
     *
     * @param scalar the value to divide with this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T div(float scalar, T result) {
        result.mX = mX / scalar;
        result.mY = mY / scalar;
        return result;
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x      the value to divide with the x component
     * @param y      the value to divide with the y component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T div(float x, float y, T result) {
        result.mX = mX / x;
        result.mY = mY / y;
        return result;
    }

    /**
     * <p>Divides a provided vector with this vector</p>
     *
     * @param vector the vector to divide with this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T div(Vector2f vector, T result) {
        result.mX = mX / vector.mX;
        result.mY = mY / vector.mY;
        return result;
    }

    /**
     * <p>Calculates the distance between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance between the two vectors
     */
    public final float distance(Vector2f vector) {
        return (float) Math.sqrt(distanceSquared(vector));
    }

    /**
     * <p>Calculates the distanceSquared between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance squared between the two vectors
     */
    public final float distanceSquared(Vector2f vector) {
        final float dx = mX - vector.mX;
        final float dy = mY - vector.mY;
        return dx * dx + dy * dy;
    }

    /**
     * <p>Calculates the dot product of the two vectors</p>
     *
     * @param vector the vector to dot with this vector
     *
     * @return a dot product of the two vectors
     */
    public final float dot(Vector2f vector) {
        return (mX * vector.mX) + (mY * vector.mY);
    }

    /**
     * <p>Normalises the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T normalise(T result) {
        return mul(1.0f / length(), result);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector2f> T negate(T result) {
        result.mX = -mX;
        result.mY = -mY;
        return result;
    }

    /**
     * <p>Calculates the magnitude of the vector</p>
     *
     * @return the magnitude of the vector
     */
    public final float length() {
        return (float) Math.sqrt(mX * mX + mY * mY);
    }

    /**
     * <p>Calculates the squared value of the magnitude of this vector</p>
     *
     * @return the squared value of the magnitude of the vector
     */
    public final float lengthSquared() {
        return mX * mX + mY * mY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 29 * hash + Float.floatToIntBits(mX);
        hash = 29 * hash + Float.floatToIntBits(mY);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object vector) {
        if (!(vector instanceof Vector2f)) {
            return false;
        }
        final Vector2f other = (Vector2f) vector;
        return Float.compare(mX, other.mX) == 0 && Float.compare(mY, other.mY) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + mX + ", " + mY + "]";
    }
}
