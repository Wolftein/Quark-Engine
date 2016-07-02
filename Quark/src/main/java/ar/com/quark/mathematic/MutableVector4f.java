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
 * Represent a mutable {@linkplain Vector4f}.
 */
public final class MutableVector4f extends Vector4f {
    /**
     * <p>Default constructor</p>
     */
    public MutableVector4f(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    /**
     * <p>Change the components of this vector</p>
     *
     * @param vector the vector
     */
    public void set(Vector4f vector) {
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
    public void setX(float x) {
        mX = x;
    }

    /**
     * <p>Change the y component of this vector</p>
     *
     * @param y the new y component
     */
    public void setY(float y) {
        mY = y;
    }

    /**
     * <p>Change the z component of this vector</p>
     *
     * @param z the new z component
     */
    public void setZ(float z) {
        mZ = z;
    }

    /**
     * <p>Change the w component of this vector</p>
     *
     * @param w the new w component
     */
    public void setW(float w) {
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
    public void setXYZW(float x, float y, float z, float w) {
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
    public MutableVector4f add(float scalar) {
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
    public MutableVector4f add(float x, float y, float z, float w) {
        return add(x, y, z, w, this);
    }

    /**
     * <p>Adds a provided vector to this vector</p>
     *
     * @param vector the vector to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f add(MutableVector4f vector) {
        return add(vector, this);
    }

    /**
     * <p>Subtracts a scalar value from this vector</p>
     *
     * @param scalar the value to subtract from this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f sub(float scalar) {
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
    public MutableVector4f sub(float x, float y, float z, float w) {
        return sub(x, y, z, w, this);
    }

    /**
     * <p>Subtracts the provided vector from this vector</p>
     *
     * @param vector the vector to sub to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f sub(MutableVector4f vector) {
        return sub(vector, this);
    }

    /**
     * <p>Multiplies a scalar value with this vector</p>
     *
     * @param scalar the value to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f mul(float scalar) {
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
    public MutableVector4f mul(float x, float y, float z, float w) {
        return mul(x, y, z, w, this);
    }

    /**
     * <p>Multiplies a provided vector with this vector</p>
     *
     * @param vector the vector to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f mul(MutableVector4f vector) {
        return mul(vector, this);
    }

    /**
     * <p>Divides a scalar value with this vector</p>
     *
     * @param scalar the value to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f div(float scalar) {
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
    public MutableVector4f div(float x, float y, float z, float w) {
        return div(x, y, z, w, this);
    }

    /**
     * <p>Divides a provided vector with this vector</p>
     *
     * @param vector the vector to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f div(MutableVector4f vector) {
        return div(vector, this);
    }

    /**
     * <p>Normalises the vector</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f normalise() {
        return normalise(this);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector4f negate() {
        return negate(this);
    }

    /**
     * <p>Creates a new vector where each component value is 0.0f</p>
     *
     * @return a new vector
     */
    public static MutableVector4f zero() {
        return new MutableVector4f(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector where each component value is 1.0f</p>
     *
     * @return a new vector
     */
    public static MutableVector4f one() {
        return new MutableVector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4f unitX() {
        return new MutableVector4f(1.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4f unitY() {
        return new MutableVector4f(0.0f, 1.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector of length one in the z-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4f unitZ() {
        return new MutableVector4f(0.0f, 0.0f, 1.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector of length one in the w-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector4f unitW() {
        return new MutableVector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#NaN}</p>
     *
     * @return a new vector
     */
    public static MutableVector4f nan() {
        return new MutableVector4f(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#POSITIVE_INFINITY}</p>
     *
     * @return a new vector
     */
    public static MutableVector4f positiveInfinity() {
        return new MutableVector4f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                Float.POSITIVE_INFINITY);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#NEGATIVE_INFINITY}</p>
     *
     * @return a new vector
     */
    public static MutableVector4f negativeInfinity() {
        return new MutableVector4f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
                , Float.NEGATIVE_INFINITY);
    }
}
