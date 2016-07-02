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
package ar.com.quark.mathematic.geometry;

import ar.com.quark.mathematic.MutableVector2f;
import ar.com.quark.mathematic.Vector2f;

/**
 * A representation of a rectangle.
 */
public final class Rectangle {
    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;

    /**
     * <p>Constructor from an origin</p>
     *
     * @param width  the width of the rectangle.
     * @param height the height of the rectangle.
     */
    public Rectangle(float width, float height) {
        this(0, 0, width, height);
    }

    /**
     * <p>Constructor given a position and a size</p>
     *
     * @param position the vector that contains the rectangle position.
     * @param size     the vector that contains the rectangle size.
     */
    public Rectangle(Vector2f position, Vector2f size) {
        this(position.getFloorX(), position.getFloorY(), size.getFloorX(), size.getFloorY());
    }

    /**
     * <p>Constructor</p>
     *
     * @param x      the x position of the rectangle.
     * @param y      the y position of the rectangle.
     * @param width  the width of the rectangle.
     * @param height the height of the rectangle.
     */
    public Rectangle(float x, float y, float width, float height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    public void set(float x, float y, float width, float height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    /**
     * <p>Get the x position of the rectangle</p>
     *
     * @return the x position of the rectangle
     */
    public float getX() {
        return mX;
    }

    /**
     * <p>Get the y position of the rectangle</p>
     *
     * @return the y position of the rectangle
     */
    public float getY() {
        return mY;
    }

    /**
     * <p>Get the width of the rectangle</p>
     *
     * @return the width of the rectangle
     */
    public float getWidth() {
        return mWidth;
    }

    /**
     * <p>Get the height of the rectangle</p>
     *
     * @return the height of the rectangle
     */
    public float getHeight() {
        return mHeight;
    }

    /**
     * <p>Get the position of the rectangle</p>
     *
     * @param vector the vector that will contain(s) the position
     *
     * @return the <code>vector</code>
     */
    public MutableVector2f getPosition(MutableVector2f vector) {
        vector.setXY(mX, mY);

        return vector;
    }

    /**
     * <p>Get the dimension of the rectangle</p>
     *
     * @param vector the vector that will contain(s) the dimension
     *
     * @return the <code>vector</code>
     */
    public MutableVector2f getDimension(MutableVector2f vector) {
        vector.setXY(mWidth, mHeight);

        return vector;
    }

    /**
     * <p>Get the area of the rectangle</p>
     *
     * @return the area of the rectangle (which is width * height)
     */
    public float getArea() {
        return mWidth * mHeight;
    }

    /**
     * <p>Get the aspect ratio of the rectangle</p>
     *
     * @return the aspect ratio of the rectangle (which is width / height)
     */
    public float getAspectRatio() {
        return mWidth / mHeight;
    }

    /**
     * <p>Get the center of the rectangle</p>
     *
     * @param vector the vector that will contain(s) the center
     *
     * @return the <code>vector</code>
     */
    public MutableVector2f getCenter(MutableVector2f vector) {
        vector.setXY(mX + mWidth * 0.5f, mY + mHeight * 0.5f);

        return vector;
    }

    /**
     * <p>Check if the given rectangle is within this rectangle</p>
     *
     * @param other the other rectangle
     *
     * @return <code>true</code> if the given rectangle is within, <code>false</code> otherwise
     */
    public boolean contain(Rectangle other) {
        return contain(other.mX, other.mY, other.mWidth, other.mHeight);
    }

    /**
     * <p>Check if the given rectangle is within this rectangle</p>
     *
     * @param x      the x coordinate of the rectangle
     * @param y      the y coordinate of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     *
     * @return <code>true</code> if the given rectangle is within, <code>false</code> otherwise
     */
    public boolean contain(float x, float y, float width, float height) {
        final float f1 = mX + mWidth;
        final float f2 = x + width;
        final float f3 = mY + mHeight;
        final float f4 = y + height;

        return (x > mX) && (x < f1)
                && (f2 > mX)
                && (f2 < f1)
                && (y > mY)
                && (y < f3)
                && (f4 > mY)
                && (f4 < f3);
    }

    /**
     * <p>Check if the given point is within this rectangle<p>
     *
     * @param vector a vector that contain(s) the point
     *
     * @return <code>true</code> if the given point is within, <code>false</code> otherwise
     */
    public boolean contain(Vector2f vector) {
        return contain(vector.getX(), vector.getY());
    }

    /**
     * <p>Check if the given point is within this rectangle<p>
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     *
     * @return <code>true</code> if the given point is within, <code>false</code> otherwise
     */
    public boolean contain(float x, float y) {
        return (mX <= x) && (mX + mWidth >= x) && (mY <= y) && (mY + mHeight >= y);
    }

    /**
     * <p>Check if the given rectangle overlap this rectangle<p>
     *
     * @param other the other rectangle
     *
     * @return <code>true</code> if the given rectangle overlap, <code>false</code> otherwise
     */
    public boolean overlap(Rectangle other) {
        return (mX < other.mX + other.mWidth)
                && (mX + mWidth > other.mX)
                && (mY < other.mY + other.mHeight)
                && (mY + mHeight > other.mY);
    }

    /**
     * <p>Merge this rectangle with other rectangle</p>
     *
     * @param other the other rectangle
     *
     * @return <code>this</code> rectangle
     */
    public Rectangle merge(Rectangle other) {
        return merge(this, other);
    }

    /**
     * <p>Merge this rectangle with other rectangle</p>
     *
     * @param destination the destination rectangle
     * @param other       the other rectangle
     *
     * @return <code>destination</code> rectangle
     */
    public Rectangle merge(Rectangle destination, Rectangle other) {
        final float f1 = Math.min(mX, other.mX);
        final float f2 = Math.max(mX + mWidth, other.mX + mWidth);
        final float f3 = Math.min(mY, other.mY);
        final float f4 = Math.max(mY + mHeight, other.mY + mHeight);

        destination.mX = f1;
        destination.mY = f3;
        destination.mWidth = f2 - f1;
        destination.mHeight = f4 - f3;

        return destination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash = 37 * hash + Float.floatToIntBits(mX);
        hash = 37 * hash + Float.floatToIntBits(mY);
        hash = 37 * hash + Float.floatToIntBits(mWidth);
        hash = 37 * hash + Float.floatToIntBits(mHeight);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rectangle)) {
            return false;
        }
        final Rectangle other = (Rectangle) o;
        return Float.compare(mX, other.mX) != 0
                && Float.compare(mY, other.mY) != 0
                && Float.compare(mWidth, other.mWidth) != 0
                && Float.compare(mHeight, other.mHeight) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[" + mX + '/' + mY + ", " + mWidth + '/' + mHeight + "]";
    }
}
