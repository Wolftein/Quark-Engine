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
 * Represent a mutable {@linkplain Vector4i}.
 */
public final class MutableVector4i extends Vector4i {
    /**
     * <p>Default constructor</p>
     */
    public MutableVector4i(int x, int y, int z, int w) {
        super(x, y, z, w);
    }

    /**
     * <p>Change the components of this vector</p>
     *
     * @param vector the vector
     */
    public void set(Vector4i vector) {
        mX = vector.mX;
        mY = vector.mY;
        mZ = vector.mZ;
        mW = vector.mW;
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
     * <p>Change the w component of this vector</p>
     *
     * @param w the new w component
     */
    public void setW(int w) {
        mW = w;
    }

    /**
     * <p>Change the x and y components of this vector</p>
     *
     * @param x the new x component
     * @param y the new y component
     * @param z the new z component
     * @param w the new w component
     */
    public void setXYZW(int x, int y, int z, int w) {
        mX = x;
        mY = y;
        mZ = z;
        mW = w;
    }


    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i add(int scalar) {
        return add(scalar, this);
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x the value to add to the x component
     * @param y the value to add to the y component
     * @param z the value to add to the z component
     * @param w the value to add to the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i add(int x, int y, int z, int w) {
        return add(x, y, z, w, this);
    }

    /**
     * <p>Adds a provided vector to this vector</p>
     *
     * @param vector the vector to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i add(MutableVector4i vector) {
        return add(vector, this);
    }

    /**
     * <p>Subtracts a scalar value from this vector</p>
     *
     * @param scalar the value to subtract from this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i sub(int scalar) {
        return sub(scalar, this);
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x the value to subtract from the x component
     * @param y the value to subtract from the y component
     * @param z the value to subtract from the z component
     * @param w the value to subtract from the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i sub(int x, int y, int z, int w) {
        return sub(x, y, z, w, this);
    }

    /**
     * <p>Subtracts the provided vector from this vector</p>
     *
     * @param vector the vector to sub to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i sub(MutableVector4i vector) {
        return sub(vector, this);
    }

    /**
     * <p>Multiplies a scalar value with this vector</p>
     *
     * @param scalar the value to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i mul(int scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x the value to multiply with the x component
     * @param y the value to multiply with the y component
     * @param z the value to multiply with the z component
     * @param w the value to multiply with the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i mul(int x, int y, int z, int w) {
        return mul(x, y, z, w, this);
    }

    /**
     * <p>Multiplies a provided vector with this vector</p>
     *
     * @param vector the vector to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i mul(MutableVector4i vector) {
        return mul(vector, this);
    }

    /**
     * <p>Divides a scalar value with this vector</p>
     *
     * @param scalar the value to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i div(int scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x the value to divide with the x component
     * @param y the value to divide with the y component
     * @param z the value to divide with the z component
     * @param w the value to divide with the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i div(int x, int y, int z, int w) {
        return div(x, y, z, w, this);
    }

    /**
     * <p>Divides a provided vector with this vector</p>
     *
     * @param vector the vector to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i div(MutableVector4i vector) {
        return div(vector, this);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4i negate() {
        return negate(this);
    }

    /**
     * <p>Creates a new vector where each component value is 0</p>
     *
     * @return a new vector
     */
    public static MutableVector4i zero() {
        return new MutableVector4i(0, 0, 0, 0);
    }

    /**
     * <p>Creates a new vector where each component value is 1</p>
     *
     * @return a new vector
     */
    public static MutableVector4i one() {
        return new MutableVector4i(1, 1, 1, 1);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4i unitX() {
        return new MutableVector4i(1, 0, 0, 0);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4i unitY() {
        return new MutableVector4i(0, 1, 0, 0);
    }

    /**
     * <p>Creates a new vector of length one in the z-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4i unitZ() {
        return new MutableVector4i(0, 0, 1, 0);
    }

    /**
     * <p>Creates a new vector of length one in the w-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4i unitW() {
        return new MutableVector4i(0, 0, 0, 1);
    }
}
