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
 * Represent a mutable {@linkplain Vector2i}.
 */
public final class MutableVector2i extends Vector2i {
    /**
     * <p>Default constructor</p>
     */
    public MutableVector2i(int x, int y) {
        super(x, y);
    }

    /**
     * <p>Change the components of this vector</p>
     *
     * @param vector the vector
     */
    public void set(Vector2i vector) {
        mX = vector.mX;
        mY = vector.mY;
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
     * <p>Change the x and y components of this vector</p>
     *
     * @param x the new x component
     * @param y the new y component
     */
    public void setXY(int x, int y) {
        mX = x;
        mY = y;
    }

    /**
     * <p>Adds a scalar value to this vector</p>
     *
     * @param scalar the value to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i add(int scalar) {
        return add(scalar, this);
    }

    /**
     * <p>Adds the provided values to this vector</p>
     *
     * @param x the value to add to the x component
     * @param y the value to add to the y component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i add(int x, int y) {
        return add(x, y, this);
    }

    /**
     * <p>Adds a provided vector to this vector</p>
     *
     * @param vector the vector to add to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i add(MutableVector2i vector) {
        return add(vector, this);
    }

    /**
     * <p>Subtracts a scalar value from this vector</p>
     *
     * @param scalar the value to subtract from this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i sub(int scalar) {
        return sub(scalar, this);
    }

    /**
     * <p>Subtracts the provided values from this vector</p>
     *
     * @param x the value to subtract from the x component
     * @param y the value to subtract from the y component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i sub(int x, int y) {
        return sub(x, y, this);
    }

    /**
     * <p>Subtracts the provided vector from this vector</p>
     *
     * @param vector the vector to sub to this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i sub(MutableVector2i vector) {
        return sub(vector, this);
    }

    /**
     * <p>Multiplies a scalar value with this vector</p>
     *
     * @param scalar the value to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i mul(int scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Multiplies the provided values with this vector</p>
     *
     * @param x the value to multiply with the x component
     * @param y the value to multiply with the y component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i mul(int x, int y) {
        return mul(x, y, this);
    }

    /**
     * <p>Multiplies a provided vector with this vector</p>
     *
     * @param vector the vector to multiply with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i mul(MutableVector2i vector) {
        return mul(vector, this);
    }

    /**
     * <p>Divides a scalar value with this vector</p>
     *
     * @param scalar the value to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i div(int scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Divides the provided values with this vector</p>
     *
     * @param x the value to divide with the x component
     * @param y the value to divide with the y component
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i div(int x, int y) {
        return div(x, y, this);
    }

    /**
     * <p>Divides a provided vector with this vector</p>
     *
     * @param vector the vector to divide with this vector
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i div(MutableVector2i vector) {
        return div(vector, this);
    }

    /**
     * <p>Negates the vector</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableVector2i negate() {
        return negate(this);
    }

    /**
     * <p>Creates a new vector where each component value is 0</p>
     *
     * @return a new vector
     */
    public static MutableVector2i zero() {
        return new MutableVector2i(0, 0);
    }

    /**
     * <p>Creates a new vector where each component value is 1</p>
     *
     * @return a new vector
     */
    public static MutableVector2i one() {
        return new MutableVector2i(1, 1);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector2i unitX() {
        return new MutableVector2i(1, 0);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static MutableVector2i unitY() {
        return new MutableVector2i(0, 1);
    }
}
