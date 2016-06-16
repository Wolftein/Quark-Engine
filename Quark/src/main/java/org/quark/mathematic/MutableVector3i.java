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
package org.quark.mathematic;

/**
 * Represent a mutable {@linkplain Vector3i}.
 */
public final class MutableVector3i extends Vector3i {
    /**
     * <p>Default constructor</p>
     */
    public MutableVector3i(int x, int y, int z) {
        super(x, y, z);
    }

    /**
     * <p>Change the components of this vector</p>
     *
     * @param vector the vector
     */
    public void set(Vector3i vector) {
        mX = vector.mX;
        mY = vector.mY;
        mZ = vector.mZ;
    }

    /**
     * <p>Change the x component of this vector</p>
     *
     * @param x the new x component
     */
    public void setX(int x) {
        mX = x;
    }

    /**
     * <p>Change the y component of this vector</p>
     *
     * @param y the new y component
     */
    public void setY(int y) {
        mY = y;
    }

    /**
     * <p>Change the z component of this vector</p>
     *
     * @param z the new z component
     */
    public void setZ(int z) {
        mZ = z;
    }

    /**
     * <p>Change the x and y components of this vector</p>
     *
     * @param x the new x component
     * @param y the new y component
     * @param z the new z component
     */
    public void setXYZ(int x, int y, int z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i add(int scalar) {
        return add(scalar, this);
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x the value to add to the x component
     * @param y the value to add to the y component
     * @param z the value to add to the z component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i add(int x, int y, int z) {
        return add(x, y, z, this);
    }

    /**
     * <p>Adds a provided vector to this vector</p>
     *
     * @param vector the vector to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i add(MutableVector3i vector) {
        return add(vector, this);
    }

    /**
     * <p>Subtracts a scalar value from this vector</p>
     *
     * @param scalar the value to subtract from this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i sub(int scalar) {
        return sub(scalar, this);
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x the value to subtract from the x component
     * @param y the value to subtract from the y component
     * @param z the value to subtract from the z component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i sub(int x, int y, int z) {
        return sub(x, y, z, this);
    }

    /**
     * <p>Subtracts the provided vector from this vector</p>
     *
     * @param vector the vector to sub to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i sub(MutableVector3i vector) {
        return sub(vector, this);
    }

    /**
     * <p>Multiplies a scalar value with this vector</p>
     *
     * @param scalar the value to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i mul(int scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x the value to multiply with the x component
     * @param y the value to multiply with the y component
     * @param z the value to multiply with the z component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i mul(int x, int y, int z) {
        return mul(x, y, z, this);
    }

    /**
     * <p>Multiplies a provided vector with this vector</p>
     *
     * @param vector the vector to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i mul(MutableVector3i vector) {
        return mul(vector, this);
    }

    /**
     * <p>Divides a scalar value with this vector</p>
     *
     * @param scalar the value to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i div(int scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x the value to divide with the x component
     * @param y the value to divide with the y component
     * @param z the value to divide with the z component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i div(int x, int y, int z) {
        return div(x, y, z, this);
    }

    /**
     * <p>Divides a provided vector with this vector</p>
     *
     * @param vector the vector to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i div(MutableVector3i vector) {
        return div(vector, this);
    }

    /**
     * <p>Calculates the cross product of this vector with a provided vector</p>
     *
     * @param vector the vector to cross product with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i cross(Vector3i vector) {
        return cross(vector, this);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector3i negate() {
        return negate(this);
    }

    /**
     * <p>Creates a new vector where each component value is 0</p>
     *
     * @return a new vector
     */
    public static MutableVector3i zero() {
        return new MutableVector3i(0, 0, 0);
    }

    /**
     * <p>Creates a new vector where each component value is 1</p>
     *
     * @return a new vector
     */
    public static MutableVector3i one() {
        return new MutableVector3i(1, 1, 1);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector3i unitX() {
        return new MutableVector3i(1, 0, 0);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector3i unitY() {
        return new MutableVector3i(0, 1, 0);
    }

    /**
     * <p>Creates a new vector of length one in the z-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector3i unitZ() {
        return new MutableVector3i(0, 0, 1);
    }

    /**
     * <p>Creates a new vector of length minus one in the z-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector3i unitNegativeZ() {
        return new MutableVector3i(0, 0, -1);
    }

}
