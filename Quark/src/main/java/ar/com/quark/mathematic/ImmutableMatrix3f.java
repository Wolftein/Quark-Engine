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
 * Represent an immutable {@link Matrix3f}.
 */
public final class ImmutableMatrix3f extends Matrix3f {
    /**
     * <p>Constructor</p>
     */
    public ImmutableMatrix3f(Matrix3f matrix) {
        super(matrix);
    }

    /**
     * <p>Constructor</p>
     */
    public ImmutableMatrix3f(
            float p00, float p01, float p02,
            float p10, float p11, float p12,
            float p20, float p21, float p22) {
        super(p00, p01, p02, p10, p11, p12, p20, p21, p22);
    }

    /**
     * <p>Creates an identity {@link Matrix3f}</p>
     *
     * @return a new matrix
     */
    public static ImmutableMatrix3f createIdentity() {
        return new ImmutableMatrix3f(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1);
    }

    /**
     * <p>Creates a scaling {@link Matrix3f}</p>
     *
     * @param scalar the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix3f createScale(float scalar) {
        return createScale(scalar, scalar, scalar);
    }

    /**
     * <p>Creates a scaling {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix3f createScale(Vector3f vector) {
        return createScale(vector.mX, vector.mY, vector.mZ);
    }

    /**
     * <p>Creates a scaling {@link Matrix3f}</p>
     *
     * @param x the value of the x component of the scaling matrix
     * @param y the value of the y component of the scaling matrix
     * @param z the value of the z component of the scaling matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix3f createScale(float x, float y, float z) {
        return new ImmutableMatrix3f(
                x, 0, 0,
                0, y, 0,
                0, 0, z);
    }

    /**
     * <p>Creates a translation {@link Matrix3f}</p>
     *
     * @param scalar the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix3f createTranslation(float scalar) {
        return createTranslation(scalar, scalar);
    }

    /**
     * <p>Creates a translation {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix3f createTranslation(Vector2f vector) {
        return createTranslation(vector.mX, vector.mY);
    }

    /**
     * <p>Creates a translation {@link Matrix3f}</p>
     *
     * @param x the value of the x component of the translation matrix
     * @param y the value of the y component of the translation matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix3f createTranslation(float x, float y) {
        return new ImmutableMatrix3f(
                1, 0, x,
                0, 1, y,
                0, 0, 1);
    }

    /**
     * <p>Creates a rotation {@link Matrix3f}</p>
     *
     * @param quaternion the quaternion for the rotation matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix3f createRotation(Quaternionf quaternion) {
        return new ImmutableMatrix3f(
                1 - 2 * quaternion.mY * quaternion.mY - 2 * quaternion.mZ * quaternion.mZ,
                2 * quaternion.mX * quaternion.mY - 2 * quaternion.mW * quaternion.mZ,
                2 * quaternion.mX * quaternion.mZ + 2 * quaternion.mW * quaternion.mY,
                2 * quaternion.mX * quaternion.mY + 2 * quaternion.mW * quaternion.mZ,
                1 - 2 * quaternion.mX * quaternion.mX - 2 * quaternion.mZ * quaternion.mZ,
                2 * quaternion.mY * quaternion.mZ - 2 * quaternion.mW * quaternion.mX,
                2 * quaternion.mX * quaternion.mZ - 2 * quaternion.mW * quaternion.mY,
                2 * quaternion.mY * quaternion.mZ + 2 * quaternion.mX * quaternion.mW,
                1 - 2 * quaternion.mX * quaternion.mX - 2 * quaternion.mY * quaternion.mY);
    }
}