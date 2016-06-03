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
package org.quark_engine.mathematic;

/**
 * <code>Vector3i</code> encapsulate a 3 element vector that is represented by integer.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public abstract class Vector3i {
    protected int mX;
    protected int mY;
    protected int mZ;

    /**
     * <p>Default constructor</p>
     */
    public Vector3i(int x, int y, int z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    /**
     * <p>Get the x component of the vector</p>
     *
     * @return the x component of the vector
     */
    public final int getX() {
        return mX;
    }

    /**
     * <p>Get the y component of the vector</p>
     *
     * @return the y component of the vector
     */
    public final int getY() {
        return mY;
    }

    /**
     * <p>Get the z component of the vector</p>
     *
     * @return the z component of the vector
     */
    public final int getZ() {
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
    public final <T extends Vector3i> T add(int scalar, T result) {
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
    public final <T extends Vector3i> T add(int x, int y, int z, T result) {
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
    public final <T extends Vector3i> T add(Vector3i vector, T result) {
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
    public final <T extends Vector3i> T sub(int scalar, T result) {
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
    public final <T extends Vector3i> T sub(int x, int y, int z, T result) {
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
    public final <T extends Vector3i> T sub(Vector3i vector, T result) {
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
    public final <T extends Vector3i> T mul(int scalar, T result) {
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
    public final <T extends Vector3i> T mul(int x, int y, int z, T result) {
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
    public final <T extends Vector3i> T mul(Vector3i vector, T result) {
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
    public final <T extends Vector3i> T div(int scalar, T result) {
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
    public final <T extends Vector3i> T div(int x, int y, int z, T result) {
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
    public final <T extends Vector3i> T div(Vector3i vector, T result) {
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
    public final int distance(Vector3i vector) {
        return (int) Math.sqrt(distanceSquared(vector));
    }

    /**
     * <p>Calculates the distanceSquared between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance squared between the two vectors
     */
    public final int distanceSquared(Vector3i vector) {
        final int dx = mX - vector.mX;
        final int dy = mY - vector.mY;
        final int dz = mZ - vector.mZ;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * <p>Calculates the dot product of the two vectors</p>
     *
     * @param vector the vector to dot with this vector
     *
     * @return a dot product of the two vectors
     */
    public final int dot(Vector3i vector) {
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
    public final <T extends Vector3i> T cross(Vector3i vector, T result) {
        final int vX1 = mX;
        final int vY1 = mY;
        final int vZ1 = mZ;
        final int vX2 = vector.mX;
        final int vY2 = vector.mY;
        final int vZ2 = vector.mZ;

        result.mX = vY1 * vZ2 - vZ1 * vY2;
        result.mY = vZ1 * vX2 - vX1 * vZ2;
        result.mZ = vX1 * vY2 - vY1 * vX2;
        return result;
    }

    /**
     * <p>Negates the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3i> T negate(T result) {
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
    public final int length() {
        return (int) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
    }

    /**
     * <p>Calculates the squared value of the magnitude of this vector</p>
     *
     * @return the squared value of the magnitude of the vector
     */
    public final int lengthSquared() {
        return mX * mX + mY * mY + mZ * mZ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 3;
        hash = 59 * hash + mX;
        hash = 59 * hash + mY;
        hash = 59 * hash + mZ;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object vector) {
        if (!(vector instanceof Vector3i)) {
            return false;
        }
        final Vector3i other = (Vector3i) vector;
        return Float.compare(mX, other.mX) == 0 && Float.compare(mY, other.mY) == 0 && Float.compare(mZ, other.mZ) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + mX + ", " + mY + ", " + mZ + "]";
    }
}
