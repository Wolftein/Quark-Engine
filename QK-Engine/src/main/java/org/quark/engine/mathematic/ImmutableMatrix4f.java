/*
 * This file is part of Quark Engine, licensed under the APACHE License.
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
package org.quark.engine.mathematic;

/**
 * Represent an immutable {@link Matrix4f}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class ImmutableMatrix4f extends Matrix4f {
    /**
     * <p>Constructor</p>
     */
    public ImmutableMatrix4f(Matrix4f matrix) {
        super(matrix);
    }

    /**
     * <p>Constructor</p>
     */
    public ImmutableMatrix4f(
            float p00, float p01, float p02, float p03,
            float p10, float p11, float p12, float p13,
            float p20, float p21, float p22, float p23,
            float p30, float p31, float p32, float p33) {
        super(p00, p01, p02, p03, p10, p11, p12, p13, p20, p21, p22, p23, p30, p31, p32, p33);
    }

    /**
     * <p>Creates an identity {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createIdentity() {
        return new ImmutableMatrix4f(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    /**
     * <p>Creates a scaling {@link Matrix4f}</p>
     *
     * @param scalar the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createScale(float scalar) {
        return createScale(scalar, scalar, scalar, scalar);
    }

    /**
     * <p>Creates a scaling {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createScale(Vector4f vector) {
        return createScale(vector.mX, vector.mY, vector.mZ, vector.mW);
    }

    /**
     * <p>Creates a scaling {@link Matrix4f}</p>
     *
     * @param x the value of the x component of the scaling matrix
     * @param y the value of the y component of the scaling matrix
     * @param z the value of the z component of the scaling matrix
     * @param w the value of the w component of the scaling matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix4f createScale(float x, float y, float z, float w) {
        return new ImmutableMatrix4f(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, w);
    }

    /**
     * <p>Creates a translation {@link Matrix4f}</p>
     *
     * @param scalar the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix4f createTranslation(float scalar) {
        return createTranslation(scalar, scalar, scalar);
    }

    /**
     * <p>Creates a translation {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static ImmutableMatrix4f createTranslation(Vector3f vector) {
        return createTranslation(vector.mX, vector.mY, vector.mZ);
    }

    /**
     * <p>Creates a translation {@link Matrix4f}</p>
     *
     * @param x the value of the x component of the translation matrix
     * @param y the value of the y component of the translation matrix
     * @param z the value of the z component of the translation matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createTranslation(float x, float y, float z) {
        return new ImmutableMatrix4f(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1);
    }

    /**
     * <p>Creates a rotation {@link Matrix4f}</p>
     *
     * @param quaternion the quaternion for the rotation matrix
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createRotation(Quaternionf quaternion) {

        return new ImmutableMatrix4f(
                1 - 2 * quaternion.mY * quaternion.mY - 2 * quaternion.mZ * quaternion.mZ,
                2 * quaternion.mX * quaternion.mY - 2 * quaternion.mW * quaternion.mZ,
                2 * quaternion.mX * quaternion.mZ + 2 * quaternion.mW * quaternion.mY,
                0,
                2 * quaternion.mX * quaternion.mY + 2 * quaternion.mW * quaternion.mZ,
                1 - 2 * quaternion.mX * quaternion.mX - 2 * quaternion.mZ * quaternion.mZ,
                2 * quaternion.mY * quaternion.mZ - 2 * quaternion.mW * quaternion.mX,
                0,
                2 * quaternion.mX * quaternion.mZ - 2 * quaternion.mW * quaternion.mY,
                2 * quaternion.mY * quaternion.mZ + 2 * quaternion.mX * quaternion.mW,
                1 - 2 * quaternion.mX * quaternion.mX - 2 * quaternion.mY * quaternion.mY,
                0,
                0,
                0,
                0,
                1);
    }

    /**
     * <p>Creates a perspective {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createPerspective(float fov, float aspect, float near, float far) {
        final float scale = (float) (1.0f / Math.tan(Math.toRadians(fov * 0.5f)));

        return new ImmutableMatrix4f(
                scale / aspect,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                scale,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                (far + near) / (near - far),
                (2.0f * far * near) / (near - far),
                0.0f,
                0.0f,
                -1.0f,
                0.0f);
    }

    /**
     * <p>Creates an orthographic {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static ImmutableMatrix4f createOrthographic(float left, float right, float bottom, float top,
                                                       float near,
                                                       float far) {
        return new ImmutableMatrix4f(
                2.0f / (right - left),
                0.0f,
                0.0f,
                -(right + left) / (right - left),
                0.0f,
                2.0f / (top - bottom),
                0.0f, -(top + bottom) / (top - bottom),
                0.0f,
                0.0f,
                -2.0f / (far - near),
                -(far + near) / (far - near),
                0.0f,
                0.0f,
                0.0f,
                1.0f);
    }
}