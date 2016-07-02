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
 * <code>Vector4i</code> encapsulate a 4 element vector that is represented by integer.
 */
public abstract class Vector4i {
    protected int mX;
    protected int mY;
    protected int mZ;
    protected int mW;

    /**
     * <p>Default constructor</p>
     */
    public Vector4i(int x, int y, int z, int w) {
        mX = x;
        mY = y;
        mZ = z;
        mW = w;
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
     * <p>Get the w component of the vector</p>
     *
     * @return the w component of the vector
     */
    public final int getW() {
        return mW;
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
     * <p>Get the floored value of the w component</p>
     *
     * @return the floored value of the w component
     */
    public final int getFloorW() {
        return (int) Math.floor(mW);
    }

    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T add(int scalar, T result) {
        result.mX = mX + scalar;
        result.mY = mY + scalar;
        result.mZ = mZ + scalar;
        result.mW = mW + scalar;
        return result;
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x      the value to add to the x component
     * @param y      the value to add to the y component
     * @param z      the value to add to the z component
     * @param w      the value to add to the w component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T add(int x, int y, int z, int w, T result) {
        result.mX = mX + x;
        result.mY = mY + y;
        result.mZ = mZ + z;
        result.mW = mW + w;
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
    public final <T extends Vector4i> T add(Vector4i vector, T result) {
        result.mX = mX + vector.mX;
        result.mY = mY + vector.mY;
        result.mZ = mZ + vector.mZ;
        result.mW = mW + vector.mW;
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
    public final <T extends Vector4i> T sub(int scalar, T result) {
        result.mX = mX - scalar;
        result.mY = mY - scalar;
        result.mZ = mZ - scalar;
        result.mW = mW - scalar;
        return result;
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x      the value to subtract from the x component
     * @param y      the value to subtract from the y component
     * @param z      the value to subtract from the z component
     * @param w      the value to subtract from the w component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T sub(int x, int y, int z, int w, T result) {
        result.mX = mX - x;
        result.mY = mY - y;
        result.mZ = mZ - z;
        result.mW = mW - w;
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
    public final <T extends Vector4i> T sub(Vector4i vector, T result) {
        result.mX = mX - vector.mX;
        result.mY = mY - vector.mY;
        result.mZ = mZ - vector.mZ;
        result.mW = mW - vector.mW;
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
    public final <T extends Vector4i> T mul(int scalar, T result) {
        result.mX = mX * scalar;
        result.mY = mY * scalar;
        result.mZ = mZ * scalar;
        result.mW = mW * scalar;
        return result;
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x      the value to multiply with the x component
     * @param y      the value to multiply with the y component
     * @param z      the value to multiply with the z component
     * @param w      the value to multiply with the w component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T mul(int x, int y, int z, int w, T result) {
        result.mX = mX * x;
        result.mY = mY * y;
        result.mZ = mZ * z;
        result.mW = mW * w;
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
    public final <T extends Vector4i> T mul(Vector4i vector, T result) {
        result.mX = mX * vector.mX;
        result.mY = mY * vector.mY;
        result.mZ = mZ * vector.mZ;
        result.mW = mW * vector.mW;
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
    public final <T extends Vector4i> T div(int scalar, T result) {
        result.mX = mX / scalar;
        result.mY = mY / scalar;
        result.mZ = mZ / scalar;
        result.mW = mW / scalar;
        return result;
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x      the value to divide with the x component
     * @param y      the value to divide with the y component
     * @param z      the value to divide with the z component
     * @param w      the value to divide with the w component
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T div(int x, int y, int z, int w, T result) {
        result.mX = mX / x;
        result.mY = mY / y;
        result.mZ = mZ / z;
        result.mW = mW / w;
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
    public final <T extends Vector4i> T div(Vector4i vector, T result) {
        result.mX = mX / vector.mX;
        result.mY = mY / vector.mY;
        result.mZ = mZ / vector.mZ;
        result.mW = mW / vector.mW;
        return result;
    }

    /**
     * <p>Calculates the distance between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance between the two vectors
     */
    public final int distance(Vector4i vector) {
        return (int) Math.sqrt(distanceSquared(vector));
    }

    /**
     * <p>Calculates the distanceSquared between this vector and the given one</p>
     *
     * @param vector the vector to check the distance from
     *
     * @return the distance squared between the two vectors
     */
    public final int distanceSquared(Vector4i vector) {
        final int dx = mX - vector.mX;
        final int dy = mY - vector.mY;
        final int dz = mZ - vector.mZ;
        final int dw = mW - vector.mW;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    /**
     * <p>Calculates the dot product of the two vectors</p>
     *
     * @param vector the vector to dot with this vector
     *
     * @return a dot product of the two vectors
     */
    public final int dot(Vector4i vector) {
        return (mX * vector.mX) + (mY * vector.mY) + (mZ * vector.mZ) + (mW * vector.mW);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4i> T negate(T result) {
        result.mX = -mX;
        result.mY = -mY;
        result.mZ = -mZ;
        result.mW = -mW;
        return result;
    }

    /**
     * <p>Calculates the magnitude of the vector</p>
     *
     * @return the magnitude of the vector
     */
    public final int length() {
        return (int) Math.sqrt(mX * mX + mY * mY + mZ * mZ + mW * mW);
    }

    /**
     * <p>Calculates the squared value of the magnitude of this vector</p>
     *
     * @return the squared value of the magnitude of the vector
     */
    public final int lengthSquared() {
        return mX * mX + mY * mY + mZ * mZ + mW * mW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 37;
        hash = 37 * hash + mX;
        hash = 37 * hash + mY;
        hash = 37 * hash + mZ;
        hash = 37 * hash + mW;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object vector) {
        if (!(vector instanceof Vector4i)) {
            return false;
        }
        final Vector4i other = (Vector4i) vector;
        return mX == other.mX && mY == other.mY && mZ == other.mZ && mW == other.mW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + mX + ", " + mY + ", " + mZ + ", " + mW + "]";
    }
}
