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

import ar.com.quark.mathematic.geometry.Rectangle;

/**
 * <code>Camera</code> encapsulate a camera with position and rotation.
 */
public class Camera {
    private static final ImmutableVector3f UNIT_X = ImmutableVector3f.unitX();
    private static final ImmutableVector3f UNIT_Y = ImmutableVector3f.unitY();
    private static final ImmutableVector3f UNIT_Z = ImmutableVector3f.unitNegativeZ();

    /**
     * Hold the projection of the camera.
     */
    protected MutableMatrix4f mProjectionMatrix;

    /**
     * Hold the view of the camera.
     */
    protected MutableMatrix4f mViewMatrix = MutableMatrix4f.createIdentity();

    /**
     * Hold the projection-view of the camera.
     */
    protected MutableMatrix4f mCombination = MutableMatrix4f.createIdentity();

    /**
     * Hold the inverse projection-view of the camera.
     */
    protected MutableMatrix4f mInverse = MutableMatrix4f.createIdentity();

    /**
     * Hold the position of the camera.
     */
    protected MutableVector3f mPosition = MutableVector3f.zero();

    /**
     * Hold the rotation of the camera.
     */
    protected MutableQuaternionf mRotation = MutableQuaternionf.unitW();

    /**
     * Hold the dirty flag of the camera, to prevent updating it all the time (saves CPU).
     */
    protected boolean mDirty = true;

    /**
     * Hold temporally matrix for calculation.
     */
    private final MutableMatrix4f mTemp0 = MutableMatrix4f.createIdentity();
    private final MutableMatrix4f mTemp1 = MutableMatrix4f.createIdentity();
    private final MutableVector3f mTemp2 = MutableVector3f.nan();
    private final MutableQuaternionf mTemp3 = MutableQuaternionf.nan();

    /**
     * <p>Constructor</p>
     */
    public Camera(Matrix4f projection) {
        mProjectionMatrix = new MutableMatrix4f(projection);
    }

    /**
     * <p>Get the projection matrix of the camera</p>
     *
     * @return a reference to the projection matrix
     */
    public Matrix4f getProjection() {
        return mProjectionMatrix;
    }

    /**
     * <p>Get the view matrix of the camera</p>
     *
     * @return a reference to the view matrix
     */
    public Matrix4f getView() {
        if (mDirty) {
            mPosition.negate(mTemp2);
            mRotation.invert(mTemp3);

            //!
            //! Calculate the view.
            //!
            mViewMatrix = mTemp0.setRotation(mTemp3).mul(mTemp1.setTranslation(mTemp2));

            //!
            //! Calculate the projection-view.
            //!
            mCombination.set(mProjectionMatrix).mul(mViewMatrix);

            //!
            //! Calculate the inverse projection-view.
            //!
            mInverse.set(mCombination).invert();

            mDirty = false;
        }
        return mViewMatrix;
    }

    /**
     * <p>Get the projection-view combined matrix of the camera</p>
     *
     * @return a reference to the projection-view combined matrix
     */
    public Matrix4f getViewProjection() {
        if (mDirty) {
            getView();
        }
        return mCombination;
    }

    /**
     * <p>Get the inverse projection-view combined matrix of the camera</p>
     *
     * @return a reference to the inverse projection-view combined matrix
     */
    public Matrix4f getInverseViewProjection() {
        return mInverse;
    }

    /**
     * <p>Get the position of the view matrix of the camera</p>
     *
     * @return a reference the position of the view matrix
     */
    public final Vector3f getPosition() {
        return mPosition;
    }

    /**
     * <p>Get the rotation of the view matrix of the camera</p>
     *
     * @return a reference the rotation of the view matrix
     */
    public final Quaternionf getRotation() {
        return mRotation;
    }

    /**
     * <p>Get the right direction of the rotation of the camera</p>
     *
     * @return a reference the right direction of the rotation
     */
    public final Vector3f getRight() {
        return mRotation.transform(UNIT_X, ImmutableVector3f.nan());
    }

    /**
     * <p>Get the up direction of the rotation of the camera</p>
     *
     * @return a reference the up direction of the rotation
     */
    public final Vector3f getUp() {
        return mRotation.transform(UNIT_Y, ImmutableVector3f.nan());
    }

    /**
     * <p>Get the forward direction of the rotation of the camera</p>
     *
     * @return a reference the forward direction of the rotation
     */
    public final Vector3f getForward() {
        return mRotation.transform(UNIT_Z, ImmutableVector3f.nan());
    }

    /**
     * <p>Changes the camera's projection</p>
     *
     * @param projection the new projection of the camera
     */
    public final void setProjection(Matrix4f projection) {
        mProjectionMatrix.set(projection);
        mDirty = true;
    }

    /**
     * <p>Changes the camera's position</p>
     *
     * @param x the new position of the camera on the x coordinate
     * @param y the new position of the camera on the y coordinate
     * @param z the new position of the camera on the z coordinate
     */
    public final void setPosition(float x, float y, float z) {
        mPosition.setXYZ(x, y, z);
        mDirty = true;
    }

    /**
     * <p>Changes the camera's position</p>
     *
     * @param vector the vector that contain(s) position(s) on x, y and z coordinate(s)
     */
    public final void setPosition(Vector3f vector) {
        setPosition(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * <p>Changes the camera's position</p>
     *
     * @param vector the vector that contain(s) position(s) on x and y coordinate(s)
     */
    public final void setPosition(Vector2f vector) {
        setPosition(vector.getX(), vector.getY(), 0.0f);
    }

    /**
     * <p>Translate the camera's position</p>
     *
     * @param x the new translation of the camera on the x coordinate
     * @param y the new translation of the camera on the y coordinate
     * @param z the new translation of the camera on the z coordinate
     */
    public final void translate(float x, float y, float z) {
        mPosition.add(x, y, z);
        mDirty = true;
    }

    /**
     * <p>Translate the camera's position</p>
     *
     * @param vector the vector that contain(s) translation(s) on x, y and z coordinate(s)
     */
    public final void translate(Vector3f vector) {
        translate(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * <p>Translate the camera's position</p>
     *
     * @param vector the vector that contain(s) translation(s) on x and y coordinate(s)
     */
    public final void translate(Vector2f vector) {
        translate(vector.getX(), vector.getY(), 0.0f);
    }

    /**
     * <p>Rotates the direction and up vector of the camera by the given angle (in radians) around the given axis</p>
     *
     * @param angle the new angle of the rotation
     * @param axis  the vector that contain(s) rotation(s) on x, y and z coordinate(s)
     */
    public final void rotate(float angle, Vector3f axis) {
        rotate(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    /**
     * <p>Rotates the direction and up vector of the camera by the given angle (in radians) around the given axis</p>
     *
     * @param angle the new angle of the rotation
     * @param x     the new rotation of the camera on the x coordinate
     * @param y     the new rotation of the camera on the y coordinate
     * @param z     the new rotation of the camera on the z coordinate
     */
    public final void rotate(float angle, float x, float y, float z) {
        mRotation.rotateAxis(angle, x, y, z);
        mDirty = true;
    }

    /**
     * <p>Rotates the direction and up vector of the camera by the given angle (in degrees) around the given axis</p>
     *
     * @param angle the new angle of the rotation
     * @param axis  the vector that contain(s) rotation(s) on x, y and z coordinate(s)
     */
    public final void rotateByDegrees(float angle, Vector3f axis) {
        rotateByDegrees(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    /**
     * <p>Rotates the direction and up vector of the camera by the given angle (in degrees) around the given axis</p>
     *
     * @param angle the new angle of the rotation
     * @param x     the new rotation of the camera on the x coordinate
     * @param y     the new rotation of the camera on the y coordinate
     * @param z     the new rotation of the camera on the z coordinate
     */
    public final void rotateByDegrees(float angle, float x, float y, float z) {
        mRotation.rotateAxisByDegrees(angle, x, y, z);
        mDirty = true;
    }

    /**
     * <p>Get the world coordinates from the given screen coordinates</p>
     *
     * @param coordinates the screen coordinates
     * @param viewport    the screen viewport
     * @param result      the vector that will contain the result
     *
     * @return a reference to <code>result</code>
     */
    public final Vector3f toWorldCoordinates(Vector3f coordinates, Rectangle viewport, MutableVector3f result) {
        final float x = coordinates.mX - viewport.getX();
        final float y = viewport.getHeight() - coordinates.mY - 1 - viewport.getY();

        result.mX = (2.0F * x) / viewport.getWidth() - 1.0F;
        result.mY = (2.0F * y) / viewport.getHeight() - 1.0F;
        result.mZ = (2.0F * coordinates.getZ()) - 1.0F;
        mInverse.transform(result, result);

        return result;
    }

    /**
     * <p>Get the screen coordinates from the given world coordinates</p>
     *
     * @param coordinates the world coordinates
     * @param viewport    the screen viewport
     * @param result      the vector that will contain the result
     *
     * @return a reference to <code>result</code>
     */
    public final Vector3f toScreenCoordinates(Vector3f coordinates, Rectangle viewport, MutableVector3f result) {
        mCombination.transform(coordinates, result);

        result.mX = (viewport.getWidth() * (result.mX + 1.0F)) * 0.5F + viewport.getX();
        result.mY = (viewport.getHeight() * (result.mY + 1.0F)) * 0.5F + viewport.getY();
        result.mZ = (result.mZ + 1.0F) * 0.5F;

        return result;
    }
} 