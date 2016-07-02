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
 * <code>Matrix4f</code> encapsulate a single precision floating point 4 by 4 matrix.
 */
public abstract class Matrix4f {
    protected float m00, m01, m02, m03;
    protected float m10, m11, m12, m13;
    protected float m20, m21, m22, m23;
    protected float m30, m31, m32, m33;

    /**
     * <p>Constructor</p>
     */
    public Matrix4f(Matrix4f matrix) {
        this(
                matrix.m00, matrix.m01, matrix.m02, matrix.m03,
                matrix.m10, matrix.m11, matrix.m12, matrix.m13,
                matrix.m20, matrix.m21, matrix.m22, matrix.m23,
                matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }

    /**
     * <p>Constructor</p>
     */
    public Matrix4f(
            float p00, float p01, float p02, float p03,
            float p10, float p11, float p12, float p13,
            float p20, float p21, float p22, float p23,
            float p30, float p31, float p32, float p33) {
        this.m00 = p00;
        this.m01 = p01;
        this.m02 = p02;
        this.m03 = p03;
        this.m10 = p10;
        this.m11 = p11;
        this.m12 = p12;
        this.m13 = p13;
        this.m20 = p20;
        this.m21 = p21;
        this.m22 = p22;
        this.m23 = p23;
        this.m30 = p30;
        this.m31 = p31;
        this.m32 = p32;
        this.m33 = p33;
    }

    /**
     * <p>Put the matrix into a {@link Float32Array}</p>
     *
     * @param buffer the buffer
     *
     * @return a reference to <code>buffer</code>
     */
    public final Float32Array store(Float32Array buffer) {
        return store(buffer.position(), buffer);
    }

    /**
     * <p>Put the matrix into a {@link Float32Array}</p>
     *
     * @param index  the buffer's offset
     * @param buffer the buffer
     *
     * @return a reference to <code>buffer</code>
     */
    public final Float32Array store(int index, Float32Array buffer) {
        buffer.write(index, m00);
        buffer.write(index + 1, m10);
        buffer.write(index + 2, m20);
        buffer.write(index + 3, m30);
        buffer.write(index + 4, m01);
        buffer.write(index + 5, m11);
        buffer.write(index + 6, m21);
        buffer.write(index + 7, m31);
        buffer.write(index + 8, m02);
        buffer.write(index + 9, m12);
        buffer.write(index + 10, m22);
        buffer.write(index + 11, m32);
        buffer.write(index + 12, m03);
        buffer.write(index + 13, m13);
        buffer.write(index + 14, m23);
        buffer.write(index + 15, m33);
        return buffer;
    }

    /**
     * <p>Adds a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T add(Matrix4f matrix, T result) {
        result.m00 = m00 + matrix.m00;
        result.m01 = m01 + matrix.m01;
        result.m02 = m02 + matrix.m02;
        result.m03 = m03 + matrix.m03;
        result.m10 = m10 + matrix.m10;
        result.m11 = m11 + matrix.m11;
        result.m12 = m12 + matrix.m12;
        result.m13 = m13 + matrix.m13;
        result.m20 = m20 + matrix.m20;
        result.m21 = m21 + matrix.m21;
        result.m22 = m22 + matrix.m22;
        result.m23 = m22 + matrix.m23;
        result.m30 = m30 + matrix.m30;
        result.m31 = m31 + matrix.m31;
        result.m32 = m32 + matrix.m32;
        result.m33 = m32 + matrix.m33;
        return result;
    }

    /**
     * <p>Subtracts a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T sub(Matrix4f matrix, T result) {
        result.m00 = m00 - matrix.m00;
        result.m01 = m01 - matrix.m01;
        result.m02 = m02 - matrix.m02;
        result.m03 = m03 - matrix.m03;
        result.m10 = m10 - matrix.m10;
        result.m11 = m11 - matrix.m11;
        result.m12 = m12 - matrix.m12;
        result.m13 = m13 - matrix.m13;
        result.m20 = m20 - matrix.m20;
        result.m21 = m21 - matrix.m21;
        result.m22 = m22 - matrix.m22;
        result.m23 = m22 - matrix.m23;
        result.m30 = m30 - matrix.m30;
        result.m31 = m31 - matrix.m31;
        result.m32 = m32 - matrix.m32;
        result.m33 = m32 - matrix.m33;
        return result;
    }

    /**
     * <p>Multiplies a provided matrix to this matrix</p>
     *
     * @param matrix the matrix
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T mul(Matrix4f matrix, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v03 = m03;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v13 = m13;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m22;
        final float v23 = m23;
        final float v30 = m30;
        final float v31 = m31;
        final float v32 = m32;
        final float v33 = m33;

        result.m00 = v00 * matrix.m00 + v01 * matrix.m10 + v02 * matrix.m20 + v03 * matrix.m30;
        result.m01 = v00 * matrix.m01 + v01 * matrix.m11 + v02 * matrix.m21 + v03 * matrix.m31;
        result.m02 = v00 * matrix.m02 + v01 * matrix.m12 + v02 * matrix.m22 + v03 * matrix.m32;
        result.m03 = v00 * matrix.m03 + v01 * matrix.m13 + v02 * matrix.m23 + v03 * matrix.m33;
        result.m10 = v10 * matrix.m00 + v11 * matrix.m10 + v12 * matrix.m20 + v13 * matrix.m30;
        result.m11 = v10 * matrix.m01 + v11 * matrix.m11 + v12 * matrix.m21 + v13 * matrix.m31;
        result.m12 = v10 * matrix.m02 + v11 * matrix.m12 + v12 * matrix.m22 + v13 * matrix.m32;
        result.m13 = v10 * matrix.m03 + v11 * matrix.m13 + v12 * matrix.m23 + v13 * matrix.m33;
        result.m20 = v20 * matrix.m00 + v21 * matrix.m10 + v22 * matrix.m20 + v23 * matrix.m30;
        result.m21 = v20 * matrix.m01 + v21 * matrix.m11 + v22 * matrix.m21 + v23 * matrix.m31;
        result.m22 = v20 * matrix.m02 + v21 * matrix.m12 + v22 * matrix.m22 + v23 * matrix.m32;
        result.m23 = v20 * matrix.m03 + v21 * matrix.m13 + v22 * matrix.m23 + v23 * matrix.m33;
        result.m30 = v30 * matrix.m00 + v31 * matrix.m10 + v32 * matrix.m20 + v33 * matrix.m30;
        result.m31 = v30 * matrix.m01 + v31 * matrix.m11 + v32 * matrix.m21 + v33 * matrix.m31;
        result.m32 = v30 * matrix.m02 + v31 * matrix.m12 + v32 * matrix.m22 + v33 * matrix.m32;
        result.m33 = v30 * matrix.m03 + v31 * matrix.m13 + v32 * matrix.m23 + v33 * matrix.m33;
        return result;
    }

    /**
     * <p>Multiplies a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to multiply
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T mul(float scalar, T result) {
        result.m00 = m00 * scalar;
        result.m01 = m01 * scalar;
        result.m02 = m02 * scalar;
        result.m03 = m03 * scalar;
        result.m10 = m10 * scalar;
        result.m11 = m11 * scalar;
        result.m12 = m12 * scalar;
        result.m13 = m13 * scalar;
        result.m20 = m20 * scalar;
        result.m21 = m21 * scalar;
        result.m22 = m22 * scalar;
        result.m23 = m22 * scalar;
        result.m30 = m30 * scalar;
        result.m31 = m31 * scalar;
        result.m32 = m32 * scalar;
        result.m33 = m32 * scalar;
        return result;
    }

    /**
     * <p>Divides a scalar value with this matrix</p>
     *
     * @param scalar the matrix's scalar to divide
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T div(float scalar, T result) {
        final float vScalar = scalar * 0.5f;

        result.m00 = m00 * vScalar;
        result.m01 = m01 * vScalar;
        result.m02 = m02 * vScalar;
        result.m03 = m03 * vScalar;
        result.m10 = m10 * vScalar;
        result.m11 = m11 * vScalar;
        result.m12 = m12 * vScalar;
        result.m13 = m13 * vScalar;
        result.m20 = m20 * vScalar;
        result.m21 = m21 * vScalar;
        result.m22 = m22 * vScalar;
        result.m23 = m22 * vScalar;
        result.m30 = m30 * vScalar;
        result.m31 = m31 * vScalar;
        result.m32 = m32 * vScalar;
        result.m33 = m32 * vScalar;
        return result;
    }

    /**
     * <p>Translate the provided values to this matrix</p>
     *
     * @param x      the value to translate the x component
     * @param y      the value to translate the y component
     * @param z      the value to translate the z component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T translate(float x, float y, float z, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v03 = m03;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v13 = m13;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m22;
        final float v23 = m23;
        final float v30 = m30;
        final float v31 = m31;
        final float v32 = m32;
        final float v33 = m33;

        result.m00 = v00;
        result.m01 = v01;
        result.m02 = v02;
        result.m03 = v00 * x + v01 * y + v02 * z + v03;
        result.m10 = v10;
        result.m11 = v11;
        result.m12 = v12;
        result.m13 = v10 * x + v11 * y + v12 * z + v13;
        result.m20 = v20;
        result.m21 = v21;
        result.m22 = v22;
        result.m23 = v20 * x + v21 * y + v22 * z + v23;
        result.m30 = v30;
        result.m31 = v31;
        result.m32 = v32;
        result.m33 = v30 * x + v31 * y + v32 * z + v33;
        return result;
    }

    /**
     * <p>Translate a provided vector to this matrix</p>
     *
     * @param translation the matrix's translation
     * @param result      the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T translate(Vector3f translation, T result) {
        return translate(translation.mX, translation.mY, translation.mZ, result);
    }

    /**
     * <p>Scale the provided scalar to this matrix</p>
     *
     * @param scalar the matrix's scalar to scale
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T scale(float scalar, T result) {
        return scale(scalar, scalar, scalar, scalar, result);
    }

    /**
     * <p>Scale the provided values to this matrix</p>
     *
     * @param x      the value to scale the x component
     * @param y      the value to scale the y component
     * @param z      the value to scale the z component
     * @param w      the value to scale the w component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T scale(float x, float y, float z, float w, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v03 = m03;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v13 = m13;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m22;
        final float v23 = m23;
        final float v30 = m30;
        final float v31 = m31;
        final float v32 = m32;
        final float v33 = m33;

        result.m00 = v00 * x;
        result.m01 = v01 * y;
        result.m02 = v02 * z;
        result.m03 = v03;
        result.m10 = v10 * x;
        result.m11 = v11 * y;
        result.m12 = v12 * z;
        result.m13 = v13;
        result.m20 = v20 * x;
        result.m21 = v21 * y;
        result.m22 = v22;
        result.m23 = v23;
        result.m30 = v30 * x;
        result.m31 = v31 * y;
        result.m32 = v32 * z;
        result.m33 = v33;
        return result;
    }

    /**
     * <p>Scale a provided vector to this matrix</p>
     *
     * @param scale  the matrix's scale
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T scale(Vector4f scale, T result) {
        return scale(scale.mX, scale.mY, scale.mZ, scale.mW, result);
    }

    /**
     * <p>Rotates a provided quaternion to this matrix</p>
     *
     * @param rotation the matrix's rotation
     * @param result   the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T rotate(Quaternionf rotation, T result) {
        final float dqx = rotation.mX + rotation.mX;
        final float dqmY = rotation.mY + rotation.mY;
        final float dqmZ = rotation.mZ + rotation.mZ;

        final float q00 = dqx * rotation.mX;
        final float q11 = dqmY * rotation.mY;
        final float q22 = dqmZ * rotation.mZ;
        final float q01 = dqx * rotation.mY;
        final float q02 = dqx * rotation.mZ;
        final float q03 = dqx * rotation.mW;
        final float q12 = dqmY * rotation.mZ;
        final float q13 = dqmY * rotation.mW;
        final float q23 = dqmZ * rotation.mW;

        final float rm00 = 1.0f - q11 - q22;
        final float rm01 = q01 + q23;
        final float rm02 = q02 - q13;
        final float rm10 = q01 - q23;
        final float rm11 = 1.0f - q22 - q00;
        final float rm12 = q12 + q03;
        final float rm20 = q02 + q13;
        final float rm21 = q12 - q03;
        final float rm22 = 1.0f - q11 - q00;

        final float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
        final float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
        final float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
        final float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
        final float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
        final float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
        final float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
        final float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;

        result.m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
        result.m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
        result.m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
        result.m23 = m03 * rm20 + m13 * rm21 + m23 * rm22;
        result.m00 = nm00;
        result.m01 = nm01;
        result.m02 = nm02;
        result.m03 = nm03;
        result.m10 = nm10;
        result.m11 = nm11;
        result.m12 = nm12;
        result.m13 = nm13;
        result.m30 = m30;
        result.m31 = m31;
        result.m32 = m32;
        result.m33 = m33;
        return result;
    }

    /**
     * <p>Transform the provided values to this matrix</p>
     *
     * @param x      the value to transform the x component
     * @param y      the value to transform the y component
     * @param z      the value to transform the z component
     * @param w      the value to transform the w component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4f> T transform(float x, float y, float z, float w, T result) {
        result.mX = m00 * x + m01 * y + m02 * z + m03 * w;
        result.mY = m10 * x + m11 * y + m12 * z + m13 * w;
        result.mZ = m20 * x + m21 * y + m22 * z + m23 * w;
        result.mW = m30 * x + m31 * y + m32 * z + m33 * w;
        return result;
    }

    /**
     * <p>Transform the provided vector to this matrix</p>
     *
     * @param transformation the matrix's transformation
     * @param result         the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector4f> T transform(Vector4f transformation, T result) {
        return transform(transformation.mX, transformation.mY, transformation.mZ, transformation.mW, result);
    }

    /**
     * <p>Calculates the transpose of the matrix</p>
     *
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T transpose(T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v03 = m03;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v13 = m13;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m22;
        final float v23 = m23;
        final float v30 = m30;
        final float v31 = m31;
        final float v32 = m32;
        final float v33 = m33;

        result.m00 = v00;
        result.m01 = v10;
        result.m02 = v20;
        result.m03 = v30;
        result.m10 = v01;
        result.m11 = v11;
        result.m12 = v21;
        result.m13 = v31;
        result.m20 = v02;
        result.m21 = v12;
        result.m22 = v22;
        result.m23 = v32;
        result.m30 = v03;
        result.m31 = v13;
        result.m32 = v23;
        result.m33 = v33;
        return result;
    }

    /**
     * <p>Calculates the invert of the matrix</p>
     *
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix4f> T invert(T result) {
        final float determinant = determinant();

        if (determinant != 0.0f) {
            final float v00 = m00;
            final float v01 = m01;
            final float v02 = m02;
            final float v03 = m03;
            final float v10 = m10;
            final float v11 = m11;
            final float v12 = m12;
            final float v13 = m13;
            final float v20 = m20;
            final float v21 = m21;
            final float v22 = m22;
            final float v23 = m23;
            final float v30 = m30;
            final float v31 = m31;
            final float v32 = m32;
            final float v33 = m33;
            final float inverse = 1.0f / determinant;


            result.m00 = v11 * (v22 * v33 - v23 * v32) + v12 * (v23 * v31 - v21 * v33) + v13 * (v21 * v32 - v22 * v31);
            result.m00 *= inverse;
            result.m01 = v21 * (v02 * v33 - v03 * v32) + v22 * (v03 * v31 - v01 * v33) + v23 * (v01 * v32 - v02 * v31);
            result.m01 *= inverse;
            result.m02 = v31 * (v02 * v13 - v03 * v12) + v32 * (v03 * v11 - v01 * v13) + v33 * (v01 * v12 - v02 * v11);
            result.m02 *= inverse;
            result.m03 = v01 * (v13 * v22 - v12 * v23) + v02 * (v11 * v23 - v13 * v21) + v03 * (v12 * v21 - v11 * v22);
            result.m03 *= inverse;
            result.m10 = v12 * (v20 * v33 - v23 * v30) + v13 * (v22 * v30 - v20 * v32) + v10 * (v23 * v32 - v22 * v33);
            result.m10 *= inverse;
            result.m11 = v22 * (v00 * v33 - v03 * v30) + v23 * (v02 * v30 - v00 * v32) + v20 * (v03 * v32 - v02 * v33);
            result.m11 *= inverse;
            result.m12 = v32 * (v00 * v13 - v03 * v10) + v33 * (v02 * v10 - v00 * v12) + v30 * (v03 * v12 - v02 * v13);
            result.m12 *= inverse;
            result.m13 = v02 * (v13 * v20 - v10 * v23) + v03 * (v10 * v22 - v12 * v20) + v00 * (v12 * v23 - v13 * v22);
            result.m13 *= inverse;
            result.m20 = v13 * (v20 * v31 - v21 * v30) + v10 * (v21 * v33 - v23 * v31) + v11 * (v23 * v30 - v20 * v33);
            result.m20 *= inverse;
            result.m21 = v23 * (v00 * v31 - v01 * v30) + v20 * (v01 * v33 - v03 * v31) + v21 * (v03 * v30 - v00 * v33);
            result.m21 *= inverse;
            result.m22 = v33 * (v00 * v11 - v01 * v10) + v30 * (v01 * v13 - v03 * v11) + v31 * (v03 * v10 - v00 * v13);
            result.m22 *= inverse;
            result.m23 = v03 * (v11 * v20 - v10 * v21) + v00 * (v13 * v21 - v11 * v23) + v01 * (v10 * v23 - v13 * v20);
            result.m23 *= inverse;
            result.m30 = v10 * (v22 * v31 - v21 * v32) + v11 * (v20 * v32 - v22 * v30) + v12 * (v21 * v30 - v20 * v31);
            result.m30 *= inverse;
            result.m31 = v20 * (v02 * v31 - v01 * v32) + v21 * (v00 * v32 - v02 * v30) + v22 * (v01 * v30 - v00 * v31);
            result.m31 *= inverse;
            result.m32 = v30 * (v02 * v11 - v01 * v12) + v31 * (v00 * v12 - v02 * v10) + v32 * (v01 * v10 - v00 * v11);
            result.m32 *= inverse;
            result.m33 = v00 * (v11 * v22 - v12 * v21) + v01 * (v12 * v20 - v10 * v22) + v02 * (v10 * v21 - v11 * v20);
            result.m33 *= inverse;
        }
        return result;
    }

    /**
     * <p>Calculates the trace of the matrix</p>
     *
     * @return the trace of the matrix
     */
    public final float trace() {
        return m00 + m11 + m22 + m33;
    }

    /**
     * <p>Calculates the determinant of the matrix</p>
     *
     * @return the determinant of the matrix
     */
    public final float determinant() {
        return m00 * (m11 * m22 * m33 + m21 * m32 * m13 + m31 * m12 * m23
                - m31 * m22 * m13 - m11 * m32 * m23 - m21 * m12 * m33)
                - m10 * (m01 * m22 * m33 + m21 * m32 * m03 + m31 * m02 * m23
                - m31 * m22 * m03 - m01 * m32 * m23 - m21 * m02 * m33)
                + m20 * (m01 * m12 * m33 + m11 * m32 * m03 + m31 * m02 * m13
                - m31 * m12 * m03 - m01 * m32 * m13 - m11 * m02 * m33)
                - m30 * (m01 * m12 * m23 + m11 * m22 * m03 + m21 * m02 * m13
                - m21 * m12 * m03 - m01 * m22 * m13 - m11 * m02 * m23);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return Float.floatToIntBits(m00) ^
                Float.floatToIntBits(m01) ^
                Float.floatToIntBits(m02) ^
                Float.floatToIntBits(m03) ^
                Float.floatToIntBits(m10) ^
                Float.floatToIntBits(m11) ^
                Float.floatToIntBits(m12) ^
                Float.floatToIntBits(m13) ^
                Float.floatToIntBits(m20) ^
                Float.floatToIntBits(m21) ^
                Float.floatToIntBits(m22) ^
                Float.floatToIntBits(m23) ^
                Float.floatToIntBits(m30) ^
                Float.floatToIntBits(m31) ^
                Float.floatToIntBits(m32) ^
                Float.floatToIntBits(m33);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object other) {
        if (!(other instanceof Matrix4f)) {
            return false;
        }
        final Matrix4f matrix = Matrix4f.class.cast(other);
        return m00 == matrix.m00
                && m01 == matrix.m01
                && m02 == matrix.m02
                && m03 == matrix.m03
                && m10 == matrix.m10
                && m11 == matrix.m11
                && m12 == matrix.m12
                && m13 == matrix.m13
                && m20 == matrix.m20
                && m21 == matrix.m21
                && m22 == matrix.m22
                && m23 == matrix.m23
                && m30 == matrix.m30
                && m31 == matrix.m31
                && m32 == matrix.m32
                && m33 == matrix.m33;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + "\n" +
                "  [" + m00 + "\t" + m01 + "\t" + m02 + "\t" + m03 + "]" + "\n" +
                "  [" + m10 + "\t" + m11 + "\t" + m12 + "\t" + m13 + "]" + "\n" +
                "  [" + m20 + "\t" + m21 + "\t" + m22 + "\t" + m23 + "]" + "\n" +
                "  [" + m30 + "\t" + m31 + "\t" + m32 + "\t" + m33 + "]" + "\n" + "]";
    }
}
