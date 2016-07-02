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

import ar.com.quark.system.utility.array.Float32Array;

/**
 * Represent a mutable {@link Matrix3f}.
 */
public final class MutableMatrix3f extends Matrix3f {
    /**
     * <p>Constructor</p>
     */
    public MutableMatrix3f(Matrix3f matrix) {
        super(matrix);
    }

    /**
     * <p>Constructor</p>
     */
    public MutableMatrix3f(
            float p00, float p01, float p02,
            float p10, float p11, float p12,
            float p20, float p21, float p22) {
        super(p00, p01, p02, p10, p11, p12, p20, p21, p22);
    }

    /**
     * <p>Set the values of the matrix from a {@link Matrix3f}</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f set(Matrix3f matrix) {
        m00 = matrix.m00;
        m01 = matrix.m01;
        m02 = matrix.m02;
        m10 = matrix.m10;
        m11 = matrix.m11;
        m12 = matrix.m12;
        m20 = matrix.m20;
        m21 = matrix.m21;
        m22 = matrix.m22;
        return this;
    }

    /**
     * <p>Set the values of the matrix from a {@link Float32Array}</p>
     *
     * @param buffer the buffer
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f set(Float32Array buffer) {
        final int offset = buffer.position();

        m00 = buffer.read(offset);
        m01 = buffer.read(offset + 1);
        m02 = buffer.read(offset + 2);
        m10 = buffer.read(offset + 3);
        m11 = buffer.read(offset + 4);
        m12 = buffer.read(offset + 5);
        m20 = buffer.read(offset + 6);
        m21 = buffer.read(offset + 7);
        m22 = buffer.read(offset + 8);
        return this;
    }

    /**
     * <p>Adds a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f add(Matrix3f matrix) {
        return add(matrix, this);
    }

    /**
     * <p>Subtracts a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f sub(Matrix3f matrix) {
        return sub(matrix, this);
    }

    /**
     * <p>Multiplies a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f mul(Matrix3f matrix) {
        return mul(matrix, this);
    }

    /**
     * <p>Multiplies a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to multiply
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f mul(float scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Divides a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to divide
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f div(float scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Calculates the transpose of the matrix</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f transpose() {
        return transpose(this);
    }

    /**
     * <p>Calculates the invert of the matrix</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix3f invert() {
        return invert(this);
    }

    /**
     * <p>Set this matrix as an identity {@link Matrix3f}</p>
     *
     * @return a new matrix
     */
    public MutableMatrix3f setIdentity() {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        return this;
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix3f}</p>
     *
     * @param scalar the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public MutableMatrix3f setToScale(float scalar) {
        return setScale(scalar, scalar, scalar);
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public MutableMatrix3f setScale(Vector3f vector) {
        return setScale(vector.mX, vector.mY, vector.mZ);
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix3f}</p>
     *
     * @param x the value of the x component of the scaling matrix
     * @param y the value of the y component of the scaling matrix
     * @param z the value of the z component of the scaling matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix3f setScale(float x, float y, float z) {
        m00 = x;
        m01 = 0.0f;
        m02 = 0.0f;
        m10 = 0.0f;
        m11 = y;
        m12 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = z;
        return this;
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix3f}</p>
     *
     * @param scalar the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix3f setTranslation(float scalar) {
        return setTranslation(scalar, scalar);
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix3f setTranslation(Vector2f vector) {
        return setTranslation(vector.mX, vector.mY);
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix3f}</p>
     *
     * @param x the value of the x component of the translation matrix
     * @param y the value of the y component of the translation matrix
     *
     * @return a new matrix
     */
    public MutableMatrix3f setTranslation(float x, float y) {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = x;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = y;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        return this;
    }

    /**
     * <p>Set this matrix as a rotation {@link Matrix3f}</p>
     *
     * @param quaternion the quaternion for the rotation matrix
     *
     * @return a new matrix
     */
    public MutableMatrix3f setRotation(Quaternionf quaternion) {
        m00 = 1.0f - 2.0f * quaternion.mY * quaternion.mY - 2.0f * quaternion.mZ * quaternion.mZ;
        m01 = 2.0f * quaternion.mX * quaternion.mY - 2.0f * quaternion.mW * quaternion.mZ;
        m02 = 2.0f * quaternion.mX * quaternion.mZ + 2.0f * quaternion.mW * quaternion.mY;
        m10 = 2.0f * quaternion.mX * quaternion.mY + 2.0f * quaternion.mW * quaternion.mZ;
        m11 = 1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mZ * quaternion.mZ;
        m12 = 2.0f * quaternion.mY * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mX;
        m20 = 2.0f * quaternion.mX * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mY;
        m21 = 2.0f * quaternion.mY * quaternion.mZ + 2.0f * quaternion.mX * quaternion.mW;
        m22 = 1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mY * quaternion.mY;
        return this;
    }

    /**
     * <p>Creates an identity {@link Matrix3f}</p>
     *
     * @return a new matrix
     */
    public static MutableMatrix3f createIdentity() {
        return new MutableMatrix3f(
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
    public static MutableMatrix3f createScale(float scalar) {
        return createScale(scalar, scalar, scalar);
    }

    /**
     * <p>Creates a scaling {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static MutableMatrix3f createScale(Vector3f vector) {
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
    public static MutableMatrix3f createScale(float x, float y, float z) {
        return new MutableMatrix3f(
                x, 0, 0,
                0, y, 0,
                0, 0, z);
    }

    /**
     * <p>Creates a translation {@link Matrix3f}</p>
     *
     * @param scalar the value of all component(s) of the translation matrix.
     *
     * @return a new matrix.
     */
    public static MutableMatrix3f createTranslation(float scalar) {
        return createTranslation(scalar, scalar);
    }

    /**
     * <p>Creates a translation {@link Matrix3f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static MutableMatrix3f createTranslation(Vector2f vector) {
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
    public static MutableMatrix3f createTranslation(float x, float y) {
        return new MutableMatrix3f(
                1, 0, x,
                0, 1, y,
                0, 0, 1);
    }

    /**
     * <p>Creates a rotation {@link Matrix3f}</p>
     *
     * @param quaternion the quaternion for the rotation matrix.
     *
     * @return a new matrix
     */
    public static MutableMatrix3f createRotation(Quaternionf quaternion) {
        return new MutableMatrix3f(
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
