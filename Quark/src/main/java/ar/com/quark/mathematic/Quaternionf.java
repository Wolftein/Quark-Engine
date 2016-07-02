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
 * <code>Quaternionf</code> extend a rotation in three dimensions to a rotation in four dimensions.
 * <p>
 * <code>Quaternionf</code> are defined by four floating point component: {x y z w}.
 */
public abstract class Quaternionf {
    protected float mX;
    protected float mY;
    protected float mZ;
    protected float mW;

    /**
     * <p>Default constructor</p>
     */
    public Quaternionf(float x, float y, float z, float w) {
        mX = x;
        mY = y;
        mZ = z;
        mW = w;
    }

    /**
     * <p>Get the x component of the quaternion</p>
     *
     * @return the x component of the quaternion
     */
    public final float getX() {
        return mX;
    }

    /**
     * <p>Get the y component of the quaternion</p>
     *
     * @return the y component of the quaternion
     */
    public final float getY() {
        return mY;
    }

    /**
     * <p>Get the z component of the quaternion</p>
     *
     * @return the z component of the quaternion
     */
    public final float getZ() {
        return mZ;
    }

    /**
     * <p>Get the w component of the quaternion</p>
     *
     * @return the w component of the quaternion
     */
    public final float getW() {
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
     * <p>Adds a scalar value to this quaternion</p>
     *
     * @param scalar the value to add to this quaternion
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T add(float scalar, T result) {
        result.mX = mX + scalar;
        result.mY = mY + scalar;
        result.mZ = mZ + scalar;
        result.mW = mW + scalar;
        return result;
    }

    /**
     * <p>Adds the provided values to this quaternion</p>
     *
     * @param x      the value to add to the x component
     * @param y      the value to add to the y component
     * @param z      the value to add to the z component
     * @param w      the value to add to the w component
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T add(float x, float y, float z, float w, T result) {
        result.mX = mX + x;
        result.mY = mY + y;
        result.mZ = mZ + z;
        result.mW = mW + w;
        return result;
    }

    /**
     * <p>Adds a provided quaternion to this quaternion</p>
     *
     * @param quaternion the quaternion to add to this quaternion
     * @param result     the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T add(Quaternionf quaternion, T result) {
        result.mX = mX + quaternion.mX;
        result.mY = mY + quaternion.mY;
        result.mZ = mZ + quaternion.mZ;
        result.mW = mW + quaternion.mW;
        return result;
    }

    /**
     * <p>Subtracts a scalar value from this quaternion</p>
     *
     * @param scalar the value to subtract from this quaternion
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T sub(float scalar, T result) {
        result.mX = mX - scalar;
        result.mY = mY - scalar;
        result.mZ = mZ - scalar;
        result.mW = mW - scalar;
        return result;
    }

    /**
     * <p>Subtracts the provided values from this quaternion</p>
     *
     * @param x      the value to subtract from the x component
     * @param y      the value to subtract from the y component
     * @param z      the value to subtract from the z component
     * @param w      the value to subtract from the w component
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T sub(float x, float y, float z, float w, T result) {
        result.mX = mX - x;
        result.mY = mY - y;
        result.mZ = mZ - z;
        result.mW = mW - w;
        return result;
    }

    /**
     * <p>Subtracts the provided quaternion from this quaternion</p>
     *
     * @param quaternion the quaternion to sub to this quaternion
     * @param result     the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T sub(Quaternionf quaternion, T result) {
        result.mX = mX - quaternion.mX;
        result.mY = mY - quaternion.mY;
        result.mZ = mZ - quaternion.mZ;
        result.mW = mW - quaternion.mW;
        return result;
    }

    /**
     * <p>Multiplies a scalar value with this quaternion</p>
     *
     * @param scalar the value to multiply with this quaternion
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T mul(float scalar, T result) {
        result.mX = mX * scalar;
        result.mY = mY * scalar;
        result.mZ = mZ * scalar;
        result.mW = mW * scalar;
        return result;
    }

    /**
     * <p>Multiplies the provided values with this quaternion</p>
     *
     * @param x      the value to multiply with the x component
     * @param y      the value to multiply with the y component
     * @param z      the value to multiply with the z component
     * @param w      the value to multiply with the w component
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T mul(float x, float y, float z, float w, T result) {
        result.mX = mX * x;
        result.mY = mY * y;
        result.mZ = mZ * z;
        result.mW = mW * w;
        return result;
    }

    /**
     * <p>Multiplies a provided quaternion with this quaternion</p>
     *
     * @param quaternion the quaternion to multiply with this quaternion
     * @param result     the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T mul(Quaternionf quaternion, T result) {
        final float vx = mX;
        final float vy = mY;
        final float vz = mZ;
        final float vw = mW;

        final float x = quaternion.mX;
        final float y = quaternion.mY;
        final float z = quaternion.mZ;
        final float w = quaternion.mW;

        result.mX = vw * x + vx * w + vy * z - vz * y;
        result.mY = vw * y + vy * w + vz * x - vx * z;
        result.mZ = vw * z + vz * w + vx * y - vy * x;
        result.mW = vw * w - vx * x - vy * y - vz * z;
        return result;
    }

    /**
     * <p>Multiplies a provided vector with this quaternion</p>
     *
     * @param vector the vector to multiply with this quaternion
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T mul(Vector3f vector, T result) {
        final float vx = mX;
        final float vy = mY;
        final float vz = mZ;
        final float vw = mW;

        final float x = vector.mX;
        final float y = vector.mY;
        final float z = vector.mZ;

        result.mX = vw * x + vz * z - vz * y;
        result.mY = vw * y + vz * x - vx * z;
        result.mZ = vw * z + vx * y - vy * x;
        result.mW = -vx * x - vy * y - vz * z;
        return result;
    }

    /**
     * <p>Divides a scalar value with this quaternion</p>
     *
     * @param scalar the value to divide with this quaternion
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T div(float scalar, T result) {
        result.mX = mX / scalar;
        result.mY = mY / scalar;
        result.mZ = mZ / scalar;
        result.mW = mW / scalar;
        return result;
    }

    /**
     * <p>Divides the provided values with this quaternion</p>
     *
     * @param x      the value to divide with the x component
     * @param y      the value to divide with the y component
     * @param z      the value to divide with the z component
     * @param w      the value to divide with the w component
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T div(float x, float y, float z, float w, T result) {
        result.mX = mX / x;
        result.mY = mY / y;
        result.mZ = mZ / z;
        result.mW = mW / w;
        return result;
    }

    /**
     * <p>Divides a provided quaternion with this quaternion</p>
     *
     * @param quaternion the quaternion to divide with this quaternion
     * @param result     the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T div(Quaternionf quaternion, T result) {
        result.mX = mX / quaternion.mX;
        result.mY = mY / quaternion.mY;
        result.mZ = mZ / quaternion.mZ;
        result.mW = mW / quaternion.mW;
        return result;
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given radians about the specified axis</p>
     *
     * @param angle  the angle in radians to rotate about the specified axis
     * @param x      the x coordinate of the rotation axis
     * @param y      the y coordinate of the rotation axis
     * @param z      the z coordinate of the rotation axis
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T rotateAxis(float angle, float x, float y, float z, T result) {
        final float
                halfAngle = angle * 0.5f,
                halfAngleInverse = (float) (1.0 / Math.sqrt(x * x + y * y + z * z)) * (float) Math.sin(halfAngle);

        final float vx = mX;
        final float vy = mY;
        final float vz = mZ;
        final float vw = mW;

        final float vx1 = x * halfAngleInverse;
        final float vy1 = y * halfAngleInverse;
        final float vz1 = z * halfAngleInverse;
        final float vw1 = (float) Math.cos(halfAngle);

        result.mX = vw * vx1 + vx * vw1 + vy * vz1 - vz * vy1;
        result.mY = vw * vy1 - vx * vz1 + vy * vw1 + vz * vx1;
        result.mZ = vw * vz1 + vx * vy1 - vy * vx1 + vz * vw1;
        result.mW = vw * vw1 - vx * vx1 - vy * vy1 - vz * vz1;
        return result;
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given radians about the specified axis</p>
     *
     * @param angle  the angle in radians to rotate about the specified axis
     * @param axis   the rotation axis
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T rotateAxis(float angle, Vector3f axis, T result) {
        return rotateAxis(angle, axis.mX, axis.mY, axis.mZ, result);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given degrees about the specified axis</p>
     *
     * @param angle  the angle in degrees to rotate about the specified axis
     * @param x      the x coordinate of the rotation axis
     * @param y      the y coordinate of the rotation axis
     * @param z      the z coordinate of the rotation axis
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T rotateAxisByDegrees(float angle, float x, float y, float z, T result) {
        return rotateAxis((float) Math.toRadians(angle), x, y, z, result);
    }

    /**
     * <p>Apply a rotation to the quaternion rotating the given degrees about the specified axis</p>
     *
     * @param angle  the angle in degrees to rotate about the specified axis
     * @param axis   the rotation axis
     * @param result the quaternion's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T rotateAxisByDegrees(float angle, Vector3f axis, T result) {
        return rotateAxisByDegrees(angle, axis.mX, axis.mY, axis.mZ, result);
    }

    /**
     * <p>Calculates the distance between this quaternion and the given one</p>
     *
     * @param quaternion the quaternion to check the distance from
     *
     * @return the distance between the two quaternions
     */
    public final float distance(Quaternionf quaternion) {
        return (float) Math.sqrt(distanceSquared(quaternion));
    }

    /**
     * <p>Calculates the distanceSquared between this quaternion and the given one</p>
     *
     * @param quaternion the quaternion to check the distance from
     *
     * @return the distance squared between the two quaternions
     */
    public final float distanceSquared(Quaternionf quaternion) {
        final float dx = mX - quaternion.mX;
        final float dy = mY - quaternion.mY;
        final float dz = mZ - quaternion.mZ;
        final float dw = mW - quaternion.mW;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    /**
     * <p>Calculates the dot product of the two quaternions</p>
     *
     * @param quaternion the quaternion to dot with this quaternion
     *
     * @return a dot product of the two quaternions
     */
    public final float dot(Quaternionf quaternion) {
        return (mX * quaternion.mX) + (mY * quaternion.mY) + (mZ * quaternion.mZ) + (mW * quaternion.mW);
    }

    /**
     * <p>Transform the given vector by this quaternion</p>
     *
     * @param vector the vector to transform
     * @param result the vector's result
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T transform(Vector3f vector, T result) {
        final float v01 = mX + mX;
        final float v02 = mY + mY;
        final float v03 = mZ + mZ;
        final float v04 = mX * v01;
        final float v05 = mY * v02;
        final float v06 = mZ * v03;
        final float v07 = mX * v02;
        final float v08 = mX * v03;
        final float v09 = mY * v03;
        final float v10 = mW * v01;
        final float v11 = mW * v02;
        final float v12 = mW * v03;

        result.mX = (1.0f - (v05 + v06)) * vector.mX + (v07 - v12) * vector.mY + (v08 + v11) * vector.mZ;
        result.mY = (v07 + v12) * vector.mX + (1.0f - (v04 + v06)) * vector.mY + (v09 - v10) * vector.mZ;
        result.mZ = (v08 - v11) * vector.mX + (v09 + v10) * vector.mY + (1.0f - (v04 + v05)) * vector.mZ;
        return result;
    }

    /**
     * <p>Transform the given vector by this quaternion</p>
     *
     * @param vector the vector to transform
     * @param result the vector's result
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4f> T transform(Vector4f vector, T result) {
        final float v01 = mX + mX;
        final float v02 = mY + mY;
        final float v03 = mZ + mZ;
        final float v04 = mX * v01;
        final float v05 = mY * v02;
        final float v06 = mZ * v03;
        final float v07 = mX * v02;
        final float v08 = mX * v03;
        final float v09 = mY * v03;
        final float v10 = mW * v01;
        final float v11 = mW * v02;
        final float v12 = mW * v03;

        result.mX = (1.0f - (v05 + v06)) * vector.mX + (v07 - v12) * vector.mY + (v08 + v11) * vector.mZ;
        result.mY = (v07 + v12) * vector.mX + (1.0f - (v04 + v06)) * vector.mY + (v09 - v10) * vector.mZ;
        result.mZ = (v08 - v11) * vector.mX + (v09 + v10) * vector.mY + (1.0f - (v04 + v05)) * vector.mZ;
        return result;
    }

    /**
     * <p>Normalises the quaternion</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T normalise(T result) {
        return mul(1.0f / length(), result);
    }

    /**
     * <p>Negates the quaternion</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T negate(T result) {
        result.mX = -mX;
        result.mY = -mY;
        result.mZ = -mZ;
        result.mW = -mW;
        return result;
    }

    /**
     * <p>Conjugates the quaternion</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T conjugate(T result) {
        result.mX = -mX;
        result.mY = -mY;
        result.mZ = -mZ;
        result.mW = mW;
        return result;
    }

    /**
     * <p>Invert the quaternion</p>
     *
     * @param result the vector's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Quaternionf> T invert(T result) {
        final float length = lengthSquared();

        if (length > 0.0f) {
            final float inverse = 1.0f / length;

            result.mX = -mX * inverse;
            result.mY = -mY * inverse;
            result.mZ = -mZ * inverse;
            result.mW = mW * inverse;
        }
        return result;
    }

    /**
     * <p>Calculates the magnitude of the quaternion</p>
     *
     * @return the magnitude of the quaternion
     */
    public final float length() {
        return (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ + mW * mW);
    }

    /**
     * <p>Calculates the squared value of the magnitude of this quaternion</p>
     *
     * @return the squared value of the magnitude of the quaternion
     */
    public final float lengthSquared() {
        return mX * mX + mY * mY + mZ * mZ + mW * mW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 37;
        hash = 37 * hash + Float.floatToIntBits(mX);
        hash = 37 * hash + Float.floatToIntBits(mY);
        hash = 37 * hash + Float.floatToIntBits(mZ);
        hash = 37 * hash + Float.floatToIntBits(mW);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object quaternion) {
        if (!(quaternion instanceof Quaternionf)) {
            return false;
        }
        final Quaternionf other = (Quaternionf) quaternion;
        return Float.compare(mX, other.mX) == 0 && Float.compare(mY, other.mY) == 0 &&
                Float.compare(mZ, other.mZ) == 0 && Float.compare(mW, other.mW) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + mX + ", " + mY + ", " + mZ + ", " + mW + "]";
    }
}
