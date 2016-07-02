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
package ar.com.quark.mathematic;

/**
 * <code>Vector3f</code> encapsulate a 3 element vector that is represented by single precision floating.
 */
public abstract class Vector3f {
    protected float mX;
    protected float mY;
    protected float mZ;

    /**
     * <p>Default constructor</p>
     */
    public Vector3f(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
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
     * <p>Get the z component of the vector</p>
     *
     * @return the z component of the vector
     */
    public final float getZ() {
        return mZ;
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
     * <p>Get the floored value of the z component</p>
     *
     * @return the floored value of the z component
     */
    public final int getFloorZ() {
        return (int) Math.floor(mZ);
    }

    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T add(float scalar, T result) {
        result.mX = mX + scalar;
        result.mY = mY + scalar;
        result.mZ = mZ + scalar;
        return result;
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x      the value to add to the x component
     * @param y      the value to add to the y component
     * @param z      the value to add to the z component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T add(float x, float y, float z, T result) {
        result.mX = mX + x;
        result.mY = mY + y;
        result.mZ = mZ + z;
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
    public final <T extends Vector3f> T add(Vector3f vector, T result) {
        result.mX = mX + vector.mX;
        result.mY = mY + vector.mY;
        result.mZ = mZ + vector.mZ;
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
    public final <T extends Vector3f> T sub(float scalar, T result) {
        result.mX = mX - scalar;
        result.mY = mY - scalar;
        result.mZ = mZ - scalar;
        return result;
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x      the value to subtract from the x component
     * @param y      the value to subtract from the y component
     * @param z      the value to subtract from the z component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T sub(float x, float y, float z, T result) {
        result.mX = mX - x;
        result.mY = mY - y;
        result.mZ = mZ - z;
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
    public final <T extends Vector3f> T sub(Vector3f vector, T result) {
        result.mX = mX - vector.mX;
        result.mY = mY - vector.mY;
        result.mZ = mZ - vector.mZ;
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
    public final <T extends Vector3f> T mul(float scalar, T result) {
        result.mX = mX * scalar;
        result.mY = mY * scalar;
        result.mZ = mZ * scalar;
        return result;
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x      the value to multiply with the x component
     * @param y      the value to multiply with the y component
     * @param z      the value to multiply with the z component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T mul(float x, float y, float z, T result) {
        result.mX = mX * x;
        result.mY = mY * y;
        result.mZ = mZ * z;
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
    public final <T extends Vector3f> T mul(Vector3f vector, T result) {
        result.mX = mX * vector.mX;
        result.mY = mY * vector.mY;
        result.mZ = mZ * vector.mZ;
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
    public final <T extends Vector3f> T div(float scalar, T result) {
        result.mX = mX / scalar;
        result.mY = mY / scalar;
        result.mZ = mZ / scalar;
        return result;
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x      the value to divide with the x component
     * @param y      the value to divide with the y component
     * @param z      the value to divide with the z component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T div(float x, float y, float z, T result) {
        result.mX = mX / x;
        result.mY = mY / y;
        result.mZ = mZ / z;
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
    public final <T extends Vector3f> T div(Vector3f vector, T result) {
        result.mX = mX / vector.mX;
        result.mY = mY / vector.mY;
        result.mZ = mZ / vector.mZ;
        return result;
    }

    /**
     * <p>Calculates the distance between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance between the two vectors
     */
    public final float distance(Vector3f vector) {
        return (float) Math.sqrt(distanceSquared(vector));
    }

    /**
     * <p>Calculates the distanceSquared between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance squared between the two vectors
     */
    public final float distanceSquared(Vector3f vector) {
        final float dx = mX - vector.mX;
        final float dy = mY - vector.mY;
        final float dz = mZ - vector.mZ;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * <p>Calculates the dot product of the two vectors</p>
     *
     * @param vector the vector to dot with this vector
     *
     * @return a dot product of the two vectors
     */
    public final float dot(Vector3f vector) {
        return (mX * vector.mX) + (mY * vector.mY) + (mZ * vector.mZ);
    }

    /**
     * <p>Calculates the cross product of this vector with a provided vector</p>
     *
     * @param vector the vector to cross product with this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T cross(Vector3f vector, T result) {
        final float vX1 = mX;
        final float vY1 = mY;
        final float vZ1 = mZ;
        final float vX2 = vector.mX;
        final float vY2 = vector.mY;
        final float vZ2 = vector.mZ;

        result.mX = vY1 * vZ2 - vZ1 * vY2;
        result.mY = vZ1 * vX2 - vX1 * vZ2;
        result.mZ = vX1 * vY2 - vY1 * vX2;
        return result;
    }

    /**
     * <p>Normalises the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T normalise(T result) {
        return mul(1.0f / length(), result);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T negate(T result) {
        result.mX = -mX;
        result.mY = -mY;
        result.mZ = -mZ;
        return result;
    }

    /**
     * <p>Calculates the magnitude of the vector</p>
     *
     * @return the magnitude of the vector
     */
    public final float length() {
        return (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
    }

    /**
     * <p>Calculates the squared value of the magnitude of this vector</p>
     *
     * @return the squared value of the magnitude of the vector
     */
    public final float lengthSquared() {
        return mX * mX + mY * mY + mZ * mZ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 3;
        hash = 59 * hash + Float.floatToIntBits(mX);
        hash = 59 * hash + Float.floatToIntBits(mY);
        hash = 59 * hash + Float.floatToIntBits(mZ);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object vector) {
        if (!(vector instanceof Vector3f)) {
            return false;
        }
        final Vector3f other = (Vector3f) vector;
        return Float.compare(mX, other.mX) == 0 && Float.compare(mY, other.mY) == 0 && Float.compare(mZ, other.mZ) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + mX + ", " + mY + ", " + mZ + "]";
    }
}
