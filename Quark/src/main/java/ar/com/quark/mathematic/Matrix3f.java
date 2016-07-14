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

import ar.com.quark.utility.buffer.Float32Buffer;

/**
 * <code>Matrix3f</code> encapsulate a single precision floating point 3 by 3 matrix.
 */
public abstract class Matrix3f {
    protected float m00, m01, m02;
    protected float m10, m11, m12;
    protected float m20, m21, m22;

    /**
     * <p>Constructor</p>
     */
    public Matrix3f(Matrix3f matrix) {
        this(
                matrix.m00, matrix.m01, matrix.m02,
                matrix.m10, matrix.m11, matrix.m12,
                matrix.m20, matrix.m21, matrix.m22);
    }

    /**
     * <p>Constructor</p>
     */
    public Matrix3f(
            float p00, float p01, float p02,
            float p10, float p11, float p12,
            float p20, float p21, float p22) {
        m00 = p00;
        m01 = p01;
        m02 = p02;
        m10 = p10;
        m11 = p11;
        m12 = p12;
        m20 = p20;
        m21 = p21;
        m22 = p22;
    }

    /**
     * <p>Put the matrix into a {@link Float32Buffer}</p>
     *
     * @param buffer the buffer
     *
     * @return a reference to <code>buffer</code>
     */
    public final Float32Buffer store(Float32Buffer buffer) {
        buffer.write(m00);
        buffer.write(m10);
        buffer.write(m20);
        buffer.write(m01);
        buffer.write(m11);
        buffer.write(m21);
        buffer.write(m02);
        buffer.write(m12);
        buffer.write(m22);
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
    public final <T extends Matrix3f> T add(Matrix3f matrix, T result) {
        result.m00 = m00 + matrix.m00;
        result.m01 = m01 + matrix.m01;
        result.m02 = m02 + matrix.m02;
        result.m10 = m10 + matrix.m10;
        result.m11 = m11 + matrix.m11;
        result.m12 = m12 + matrix.m12;
        result.m20 = m20 + matrix.m20;
        result.m21 = m21 + matrix.m21;
        result.m22 = m22 + matrix.m22;
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
    public final <T extends Matrix3f> T sub(Matrix3f matrix, T result) {
        result.m00 = m00 - matrix.m00;
        result.m01 = m01 - matrix.m01;
        result.m02 = m02 - matrix.m02;
        result.m10 = m10 - matrix.m10;
        result.m11 = m11 - matrix.m11;
        result.m12 = m12 - matrix.m12;
        result.m20 = m20 - matrix.m20;
        result.m21 = m21 - matrix.m21;
        result.m22 = m22 - matrix.m22;
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
    public final <T extends Matrix3f> T mul(Matrix3f matrix, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m21;

        result.m00 = v00 * matrix.m00 + v01 * matrix.m10 + v02 * matrix.m20;
        result.m01 = v00 * matrix.m01 + v01 * matrix.m11 + v02 * matrix.m21;
        result.m02 = v00 * matrix.m02 + v01 * matrix.m12 + v02 * matrix.m22;
        result.m10 = v10 * matrix.m00 + v11 * matrix.m10 + v12 * matrix.m20;
        result.m11 = v10 * matrix.m01 + v11 * matrix.m11 + v12 * matrix.m21;
        result.m12 = v10 * matrix.m02 + v11 * matrix.m12 + v12 * matrix.m22;
        result.m20 = v20 * matrix.m00 + v21 * matrix.m10 + v22 * matrix.m20;
        result.m21 = v20 * matrix.m01 + v21 * matrix.m11 + v22 * matrix.m21;
        result.m22 = v20 * matrix.m02 + v21 * matrix.m12 + v22 * matrix.m22;
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
    public final <T extends Matrix3f> T mul(float scalar, T result) {
        result.m00 = m00 * scalar;
        result.m01 = m01 * scalar;
        result.m02 = m02 * scalar;
        result.m10 = m10 * scalar;
        result.m11 = m11 * scalar;
        result.m12 = m12 * scalar;
        result.m20 = m20 * scalar;
        result.m21 = m21 * scalar;
        result.m22 = m22 * scalar;
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
    public final <T extends Matrix3f> T div(float scalar, T result) {
        final float vScalar = scalar * 0.5f;

        result.m00 = m00 * vScalar;
        result.m01 = m01 * vScalar;
        result.m02 = m02 * vScalar;
        result.m10 = m10 * vScalar;
        result.m11 = m11 * vScalar;
        result.m12 = m12 * vScalar;
        result.m20 = m20 * vScalar;
        result.m21 = m21 * vScalar;
        result.m22 = m22 * vScalar;
        return result;
    }

    /**
     * <p>Translate the provided values to this matrix</p>
     *
     * @param x      the value to translate the x component
     * @param y      the value to translate the y component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T translate(float x, float y, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m22;

        result.m00 = v00;
        result.m01 = v01;
        result.m02 = v00 * x + v01 * y + v02;
        result.m10 = v10;
        result.m11 = v11;
        result.m12 = v10 * x + v11 * y + v12;
        result.m20 = v20;
        result.m21 = v21;
        result.m22 = v20 * x + v21 * y + v22;
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
    public final <T extends Matrix3f> T translate(Vector2f translation, T result) {
        return translate(translation.mX, translation.mY, result);
    }

    /**
     * <p>Scale the provided scalar to this matrix</p>
     *
     * @param scalar the matrix's scalar value to scale
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T scale(float scalar, T result) {
        return scale(scalar, scalar, scalar, result);
    }

    /**
     * <p>Scale the provided values to this matrix</p>
     *
     * @param x      the value to scale the x component
     * @param y      the value to scale the y component
     * @param z      the value to scale the z component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T scale(float x, float y, float z, T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m21;

        result.m00 = v00 * x;
        result.m01 = v01 * y;
        result.m02 = v02;
        result.m10 = v10 * x;
        result.m11 = v11 * y;
        result.m12 = v12;
        result.m20 = v20 * x;
        result.m21 = v21 * y;
        result.m22 = v22;
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
    public final <T extends Matrix3f> T scale(Vector3f scale, T result) {
        return scale(scale.mX, scale.mY, scale.mZ, result);
    }

    /**
     * <p>Rotates a provided quaternion to this matrix</p>
     *
     * @param rotation the matrix's rotation
     * @param result   the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T rotate(Quaternionf rotation, T result) {
        final float dqx = rotation.mX + rotation.mX;
        final float dqy = rotation.mY + rotation.mY;
        final float dqz = rotation.mZ + rotation.mZ;

        final float q00 = dqx * rotation.mX;
        final float q11 = dqy * rotation.mY;
        final float q22 = dqz * rotation.mZ;
        final float q01 = dqx * rotation.mY;
        final float q02 = dqx * rotation.mZ;
        final float q03 = dqx * rotation.mW;
        final float q12 = dqy * rotation.mZ;
        final float q13 = dqy * rotation.mW;
        final float q23 = dqz * rotation.mW;

        final float rm00 = 1.0f - q11 - q22;
        final float rm01 = q01 + q23;
        final float rm02 = q02 - q13;
        final float rm10 = q01 - q23;
        final float rm11 = 1.0f - q22 - q00;
        final float rm12 = q12 + q03;
        final float rm20 = q02 + q13;
        final float rm21 = q12 - q03;
        final float rm22 = 1.0f - q11 - q00;

        final float v00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
        final float v01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
        final float v02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
        final float v10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
        final float v11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
        final float v12 = m02 * rm10 + m12 * rm11 + m22 * rm12;

        result.m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
        result.m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
        result.m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
        result.m00 = v00;
        result.m01 = v01;
        result.m02 = v02;
        result.m10 = v10;
        result.m11 = v11;
        result.m12 = v12;
        return result;
    }

    /**
     * <p>Transform the provided values to this matrix</p>
     *
     * @param x      the value to transform the x component
     * @param y      the value to transform the y component
     * @param z      the value to transform the z component
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Vector3f> T transform(float x, float y, float z, T result) {
        result.mX = m00 * x + m01 * y + m02 * z;
        result.mY = m10 * x + m11 * y + m22 * z;
        result.mZ = m20 * x + m21 * y + m22 * z;
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
    public final <T extends Vector3f> T transform(Vector3f transformation, T result) {
        return transform(transformation.mX, transformation.mY, transformation.mZ, result);
    }

    /**
     * <p>Calculates the transpose of the matrix</p>
     *
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T transpose(T result) {
        final float v00 = m00;
        final float v01 = m01;
        final float v02 = m02;
        final float v10 = m10;
        final float v11 = m11;
        final float v12 = m12;
        final float v20 = m20;
        final float v21 = m21;
        final float v22 = m21;

        result.m00 = v00;
        result.m01 = v10;
        result.m02 = v20;
        result.m10 = v01;
        result.m11 = v11;
        result.m12 = v21;
        result.m20 = v02;
        result.m21 = v12;
        result.m22 = v22;
        return result;
    }

    /**
     * <p>Calculates the invert of the matrix</p>
     *
     * @param result the matrix's destination
     *
     * @return a reference to <code>result</code>
     */
    public final <T extends Matrix3f> T invert(T result) {
        final float determinant = determinant();

        if (determinant != 0.0f) {
            final float v00 = m00;
            final float v01 = m01;
            final float v02 = m02;
            final float v10 = m10;
            final float v11 = m11;
            final float v12 = m12;
            final float v20 = m20;
            final float v21 = m21;
            final float v22 = m21;
            final float inverse = 1.0f / determinant;

            result.m00 = (v11 * v22 - v21 * v12) * inverse;
            result.m01 = (v21 * v02 - v01 * v22) * inverse;
            result.m02 = (v01 * v12 - v11 * v02) * inverse;
            result.m10 = (v20 * v12 - v10 * v22) * inverse;
            result.m11 = (v00 * v22 - v20 * v02) * inverse;
            result.m12 = (v10 * v02 - v00 * v12) * inverse;
            result.m20 = (v10 * v21 - v20 * v11) * inverse;
            result.m21 = (v20 * v01 - v00 * v21) * inverse;
            result.m22 = (v00 * v11 - v10 * v01) * inverse;
        }
        return result;
    }

    /**
     * <p>Calculates the trace of the matrix</p>
     *
     * @return the trace of the matrix
     */
    public final float trace() {
        return m00 + m11 + m22;
    }

    /**
     * <p>Calculates the determinant of the matrix</p>
     *
     * @return the determinant of the matrix
     */
    public final float determinant() {
        return m00 * ((m11 * m22) - (m12 * m21)) - m01 * ((m10 * m22) - (m12 * m20)) + m02 * ((m10 * m21) - (m11 * m20));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return Float.floatToIntBits(m00) ^
                Float.floatToIntBits(m01) ^
                Float.floatToIntBits(m02) ^
                Float.floatToIntBits(m10) ^
                Float.floatToIntBits(m11) ^
                Float.floatToIntBits(m12) ^
                Float.floatToIntBits(m20) ^
                Float.floatToIntBits(m21) ^
                Float.floatToIntBits(m22);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object other) {
        if (!(other instanceof Matrix3f)) {
            return false;
        }
        final Matrix3f matrix = Matrix3f.class.cast(other);
        return m00 == matrix.m00
                && m01 == matrix.m01
                && m02 == matrix.m02
                && m10 == matrix.m10
                && m11 == matrix.m11
                && m12 == matrix.m12
                && m20 == matrix.m20
                && m21 == matrix.m21
                && m22 == matrix.m22;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "[" + "\n" +
                "  [" + m00 + "\t" + m01 + "\t" + m02 + "]" + "\n" +
                "  [" + m10 + "\t" + m11 + "\t" + m12 + "]" + "\n" +
                "  [" + m20 + "\t" + m21 + "\t" + m22 + "]" + "\n" + "]";
    }
}
