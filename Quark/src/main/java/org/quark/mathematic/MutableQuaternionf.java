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
 * Represent a mutable {@linkplain Quaternionf}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class MutableQuaternionf extends Quaternionf {
    /**
     * <p>Default constructor</p>
     */
    public MutableQuaternionf(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    /**
     * <p>Change the x component of this quaternion</p>
     *
     * @param x the new x component
     */
    public final void setX(float x) {
        mX = x;
    }

    /**
     * <p>Change the y component of this quaternion</p>
     *
     * @param y the new y component
     */
    public final void setY(float y) {
        mY = y;
    }

    /**
     * <p>Change the z component of this quaternion</p>
     *
     * @param z the new z component
     */
    public final void setZ(float z) {
        mZ = z;
    }

    /**
     * <p>Change the w component of this quaternion</p>
     *
     * @param w the new w component
     */
    public final void setW(float w) {
        mW = w;
    }

    /**
     * <p>Change the x and y components of this quaternion</p>
     *
     * @param x the new x component
     * @param y the new y component
     * @param z the new z component
     * @param w the new w component
     */
    public final void setXYZW(float x, float y, float z, float w) {
        mX = x;
        mY = y;
        mZ = z;
        mW = w;
    }

    /**
     * <p>Adds a scalar value to this quaternion</p>
     *
     * @param scalar the value to add to this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf add(float scalar) {
        return add(scalar, this);
    }

    /**
     * <p>Adds the provided values to this quaternion</p>
     *
     * @param x the value to add to the x component
     * @param y the value to add to the y component
     * @param z the value to add to the z component
     * @param w the value to add to the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf add(float x, float y, float z, float w) {
        return add(x, y, z, w, this);
    }

    /**
     * <p>Adds a provided quaternion to this quaternion</p>
     *
     * @param quaternion the quaternion to add to this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf add(MutableQuaternionf quaternion) {
        return add(quaternion, this);
    }

    /**
     * <p>Subtracts a scalar value from this quaternion</p>
     *
     * @param scalar the value to subtract from this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf sub(float scalar) {
        return sub(scalar, this);
    }

    /**
     * <p>Subtracts the provided values from this quaternion</p>
     *
     * @param x the value to subtract from the x component
     * @param y the value to subtract from the y component
     * @param z the value to subtract from the z component
     * @param w the value to subtract from the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf sub(float x, float y, float z, float w) {
        return sub(x, y, z, w, this);
    }

    /**
     * <p>Subtracts the provided quaternion from this quaternion</p>
     *
     * @param quaternion the quaternion to sub to this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf sub(MutableQuaternionf quaternion) {
        return sub(quaternion, this);
    }

    /**
     * <p>Multiplies a scalar value with this quaternion</p>
     *
     * @param scalar the value to multiply with this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf mul(float scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Multiplies the provided values with this quaternion</p>
     *
     * @param x the value to multiply with the x component
     * @param y the value to multiply with the y component
     * @param z the value to multiply with the z component
     * @param w the value to multiply with the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf mul(float x, float y, float z, float w) {
        return mul(x, y, z, w, this);
    }

    /**
     * <p>Multiplies a provided quaternion with this quaternion</p>
     *
     * @param quaternion the quaternion to multiply with this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf mul(MutableQuaternionf quaternion) {
        return mul(quaternion, this);
    }

    /**
     * <p>Divides a scalar value with this quaternion</p>
     *
     * @param scalar the value to divide with this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf div(float scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Divides the provided values with this quaternion</p>
     *
     * @param x the value to divide with the x component
     * @param y the value to divide with the y component
     * @param z the value to divide with the z component
     * @param w the value to divide with the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf div(float x, float y, float z, float w) {
        return div(x, y, z, w, this);
    }

    /**
     * <p>Divides a provided quaternion with this quaternion</p>
     *
     * @param quaternion the quaternion to divide with this quaternion
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf div(MutableQuaternionf quaternion) {
        return div(quaternion, this);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given radians about the specified axis</p>
     *
     * @param angle the angle in radians to rotate about the specified axis
     * @param x     the x coordinate of the rotation axis
     * @param y     the y coordinate of the rotation axis
     * @param z     the z coordinate of the rotation axis
     *
     * @return a reference to <code>this</code>
     */
    public final MutableQuaternionf rotateAxis(float angle, float x, float y, float z) {
        return rotateAxis(angle, x, y, z, this);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given radians about the specified axis</p>
     *
     * @param angle the angle in radians to rotate about the specified axis
     * @param axis  the rotation axis
     *
     * @return a reference to <code>this</code>
     */
    public final MutableQuaternionf rotateAxis(float angle, Vector3f axis) {
        return rotateAxis(angle, axis, this);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given degrees about the specified axis</p>
     *
     * @param angle the angle in degrees to rotate about the specified axis
     * @param x     the x coordinate of the rotation axis
     * @param y     the y coordinate of the rotation axis
     * @param z     the z coordinate of the rotation axis
     *
     * @return a reference to <code>this</code>
     */
    public final MutableQuaternionf rotateAxisByDegrees(float angle, float x, float y, float z) {
        return rotateAxisByDegrees(angle, x, y, z, this);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given degrees about the specified axis</p>
     *
     * @param angle the angle in degrees to rotate about the specified axis
     * @param axis  the rotation axis
     *
     * @return a reference to <code>this</code>
     */
    public final MutableQuaternionf rotateAxisByDegrees(float angle, Vector3f axis) {
        return rotateAxisByDegrees(angle, axis, this);
    }

    /**
     * <p>Normalises the quaternion</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf normalise() {
        return normalise(this);
    }

    /**
     * <p>Negates the quaternion</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf negate() {
        return negate(this);
    }

    /**
     * <p>Conjugates the quaternion</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf conjugate() {
        return conjugate(this);
    }

    /**
     * <p>Invert the quaternion</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableQuaternionf invert() {
        return invert(this);
    }

    /**
     * <p></p>Creates a new quaternion where each component value is 0.0f</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf zero() {
        return new MutableQuaternionf(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion where each component value is 1.0f</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf one() {
        return new MutableQuaternionf(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the x-axis</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf unitX() {
        return new MutableQuaternionf(1.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the y-axis</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf unitY() {
        return new MutableQuaternionf(0.0f, 1.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the z-axis</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf unitZ() {
        return new MutableQuaternionf(0.0f, 0.0f, 1.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the w-axis</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf unitW() {
        return new MutableQuaternionf(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#NaN}</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf nan() {
        return new MutableQuaternionf(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#POSITIVE_INFINITY}</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf positiveInfinity() {
        return new MutableQuaternionf(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                Float.POSITIVE_INFINITY);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#NEGATIVE_INFINITY}</p>
     *
     * @return a new quaternion
     */
    public static MutableQuaternionf negativeInfinity() {
        return new MutableQuaternionf(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
                , Float.NEGATIVE_INFINITY);
    }
}
