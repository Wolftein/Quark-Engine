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

import org.quark.system.utility.array.Float32Array;

import java.nio.FloatBuffer;

/**
 * Represent a mutable {@link Matrix4f}.
 */
public final class MutableMatrix4f extends Matrix4f {
    /**
     * <p>Constructor</p>
     */
    public MutableMatrix4f(Matrix4f matrix) {
        super(matrix);
    }

    /**
     * <p>Constructor</p>
     */
    public MutableMatrix4f(
            float p00, float p01, float p02, float p03,
            float p10, float p11, float p12, float p13,
            float p20, float p21, float p22, float p23,
            float p30, float p31, float p32, float p33) {
        super(
                p00, p01, p02, p03,
                p10, p11, p12, p13,
                p20, p21, p22, p23,
                p30, p31, p32, p33);
    }

    /**
     * <p>Set the values of the matrix from a {@link Matrix4f}</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f set(Matrix4f matrix) {
        m00 = matrix.m00;
        m01 = matrix.m01;
        m02 = matrix.m02;
        m03 = matrix.m03;
        m10 = matrix.m10;
        m11 = matrix.m11;
        m12 = matrix.m12;
        m13 = matrix.m13;
        m20 = matrix.m20;
        m21 = matrix.m21;
        m22 = matrix.m22;
        m23 = matrix.m23;
        m30 = matrix.m30;
        m31 = matrix.m31;
        m32 = matrix.m32;
        m33 = matrix.m33;
        return this;
    }

    /**
     * <p>Set the values of the matrix from a {@link FloatBuffer}</p>
     *
     * @param buffer the buffer
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f set(Float32Array buffer) {
        final int offset = buffer.position();

        m00 = buffer.read(offset);
        m01 = buffer.read(offset + 1);
        m02 = buffer.read(offset + 2);
        m03 = buffer.read(offset + 3);
        m10 = buffer.read(offset + 4);
        m11 = buffer.read(offset + 5);
        m12 = buffer.read(offset + 6);
        m13 = buffer.read(offset + 7);
        m20 = buffer.read(offset + 8);
        m21 = buffer.read(offset + 9);
        m22 = buffer.read(offset + 10);
        m23 = buffer.read(offset + 11);
        m30 = buffer.read(offset + 12);
        m31 = buffer.read(offset + 13);
        m32 = buffer.read(offset + 14);
        m33 = buffer.read(offset + 15);
        return this;
    }

    /**
     * <p>Adds a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f add(Matrix4f matrix) {
        return add(matrix, this);
    }

    /**
     * <p>Subtracts a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f sub(Matrix4f matrix) {
        return sub(matrix, this);
    }

    /**
     * <p>Multiplies a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f mul(Matrix4f matrix) {
        return mul(matrix, this);
    }

    /**
     * <p>Multiplies a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to multiply
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f mul(float scalar) {
        return mul(scalar, this);
    }

    /**
     * <p>Divides a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to divide
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f div(float scalar) {
        return div(scalar, this);
    }

    /**
     * <p>Translate the provided values to this matrix</p>
     *
     * @param x the value to translate the x component
     * @param y the value to translate the y component
     * @param z the value to translate the z component
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f translate(float x, float y, float z) {
        return translate(x, y, z, this);
    }

    /**
     * <p>Translate a provided vector to this matrix</p>
     *
     * @param translation the matrix's translation
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f translate(Vector3f translation) {
        return translate(translation, this);
    }

    /**
     * <p>Scale the provided scalar to this matrix</p>
     *
     * @param scalar the matrix's scalar to scale
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f scale(float scalar) {
        return scale(scalar, this);
    }

    /**
     * <p>Scale the provided values to this matrix</p>
     *
     * @param x the value to scale the x component
     * @param y the value to scale the y component
     * @param z the value to scale the z component
     * @param w the value to scale the w component
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f scale(float x, float y, float z, float w) {
        return scale(x, y, z, w, this);
    }

    /**
     * <p>Scale a provided vector to this matrix</p>
     *
     * @param scale the matrix's scale
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f scale(Vector4f scale) {
        return scale(scale, this);
    }

    /**
     * <p>Rotates a provided quaternion to this matrix</p>
     *
     * @param rotation the matrix's rotation
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f rotate(Quaternionf rotation) {
        return rotate(rotation, this);
    }

    /**
     * <p>Calculates the transpose of the matrix</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f transpose() {
        return transpose(this);
    }

    /**
     * <p>Calculates the invert of the matrix</p>
     *
     * @return a reference to <code>this</code>
     */
    public MutableMatrix4f invert() {
        return invert(this);
    }

    /**
     * <p>Set this matrix as an identity {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public MutableMatrix4f setIdentity() {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix4f}</p>
     *
     * @param scalar the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public MutableMatrix4f setScale(float scalar) {
        return setScale(scalar, scalar, scalar, scalar);
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public MutableMatrix4f setScale(Vector4f vector) {
        return setScale(vector.mX, vector.mY, vector.mZ, vector.mW);
    }

    /**
     * <p>Set this matrix as a scaling {@link Matrix4f}</p>
     *
     * @param x the value of the x component of the scaling matrix
     * @param y the value of the y component of the scaling matrix
     * @param z the value of the z component of the scaling matrix
     * @param w the value of the w component of the scaling matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix4f setScale(float x, float y, float z, float w) {
        m00 = x;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = y;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = z;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = w;
        return this;
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix4f}</p>
     *
     * @param scalar the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix4f setTranslation(float scalar) {
        return setTranslation(scalar, scalar, scalar);
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public MutableMatrix4f setTranslation(Vector3f vector) {
        return setTranslation(vector.mX, vector.mY, vector.mZ);
    }

    /**
     * <p>Set this matrix as a translation {@link Matrix4f}</p>
     *
     * @param x the value of the x component of the translation matrix
     * @param y the value of the y component of the translation matrix
     * @param z the value of the z component of the translation matrix
     *
     * @return a new matrix
     */
    public MutableMatrix4f setTranslation(float x, float y, float z) {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = x;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = y;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = z;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    /**
     * <p>Set this matrix as a rotation {@link Matrix4f}</p>
     *
     * @param quaternion the quaternion for the rotation matrix
     *
     * @return a new matrix
     */
    public MutableMatrix4f setRotation(Quaternionf quaternion) {
        m00 = 1.0f - 2.0f * quaternion.mY * quaternion.mY - 2.0f * quaternion.mZ * quaternion.mZ;
        m01 = 2.0f * quaternion.mX * quaternion.mY - 2.0f * quaternion.mW * quaternion.mZ;
        m02 = 2.0f * quaternion.mX * quaternion.mZ + 2.0f * quaternion.mW * quaternion.mY;
        m03 = 0.0f;
        m10 = 2.0f * quaternion.mX * quaternion.mY + 2.0f * quaternion.mW * quaternion.mZ;
        m11 = 1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mZ * quaternion.mZ;
        m12 = 2.0f * quaternion.mY * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mX;
        m13 = 0.0f;
        m20 = 2.0f * quaternion.mX * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mY;
        m21 = 2.0f * quaternion.mY * quaternion.mZ + 2.0f * quaternion.mX * quaternion.mW;
        m22 = 1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mY * quaternion.mY;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
        return this;
    }

    /**
     * <p>Creates an identity {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static MutableMatrix4f createIdentity() {
        return new MutableMatrix4f(
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
    public static MutableMatrix4f createScale(float scalar) {
        return createScale(scalar, scalar, scalar, scalar);
    }

    /**
     * <p>Creates a scaling {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the scaling matrix
     *
     * @return a new matrix
     */
    public static MutableMatrix4f createScale(Vector4f vector) {
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
    public static MutableMatrix4f createScale(float x, float y, float z, float w) {
        return new MutableMatrix4f(
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
    public static MutableMatrix4f createTranslation(float scalar) {
        return createTranslation(scalar, scalar, scalar);
    }

    /**
     * <p>Creates a translation {@link Matrix4f}</p>
     *
     * @param vector the value of all component(s) of the translation matrix
     *
     * @return a new matrix.
     */
    public static MutableMatrix4f createTranslation(Vector3f vector) {
        return createTranslation(vector.mX, vector.mY, vector.mZ);
    }

    /**
     * <p>Creates a translation {@link Matrix4f}</p>
     *
     * @param x the value of the x component of the translation matrix
     * @param y the value of the y component of the translation matrix
     *
     * @return a new matrix
     */
    public static MutableMatrix4f createTranslation(float x, float y, float z) {
        return new MutableMatrix4f(
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
    public static MutableMatrix4f createRotation(Quaternionf quaternion) {
        return new MutableMatrix4f(
                1.0f - 2.0f * quaternion.mY * quaternion.mY - 2.0f * quaternion.mZ * quaternion.mZ,
                2.0f * quaternion.mX * quaternion.mY - 2.0f * quaternion.mW * quaternion.mZ,
                2.0f * quaternion.mX * quaternion.mZ + 2.0f * quaternion.mW * quaternion.mY,
                0.0f,
                2.0f * quaternion.mX * quaternion.mY + 2.0f * quaternion.mW * quaternion.mZ,
                1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mZ * quaternion.mZ,
                2.0f * quaternion.mY * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mX,
                0.0f,
                2.0f * quaternion.mX * quaternion.mZ - 2.0f * quaternion.mW * quaternion.mY,
                2.0f * quaternion.mY * quaternion.mZ + 2.0f * quaternion.mX * quaternion.mW,
                1.0f - 2.0f * quaternion.mX * quaternion.mX - 2.0f * quaternion.mY * quaternion.mY,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f);
    }

    /**
     * <p>Creates a perspective {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static MutableMatrix4f createPerspective(float fov, float aspect, float near, float far) {
        final float scale = (float) (Math.tan(Math.toRadians(fov) * 0.5f));

        return new MutableMatrix4f(
                1.0f / (scale * aspect),
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f / scale,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                (far + near) / (near - far),
                -1.0f,
                0.0f,
                0.0f,
                (far + far) * near / (near - far),
                0.0f);
    }

    /**
     * <p>Creates an orthographic {@link Matrix4f}</p>
     *
     * @return a new matrix
     */
    public static MutableMatrix4f createOrthographic(float left, float right, float bottom, float top,
            float near,
            float far) {
        return new MutableMatrix4f(
                2.0f / (right - left), 0.0f, 0.0f, -(right + left) / (right - left),
                0.0f, 2.0f / (top - bottom), 0.0f, -(top + bottom) / (top - bottom),
                0.0f, 0.0f, -2.0f / (far - near), -(far + near) / (far - near),
                0.0f, 0.0f, 0.0f, 1.0f);
    }
}
