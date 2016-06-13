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
 * Represent an immutable {@linkplain Vector3f}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class ImmutableVector3f extends Vector3f {
    public final static ImmutableVector3f ZERO = zero();

    /**
     * <p>Default constructor</p>
     */
    public ImmutableVector3f(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * <p>Creates a new vector where each component value is 0.0f</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f zero() {
        return new ImmutableVector3f(0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector where each component value is 1.0f</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f one() {
        return new ImmutableVector3f(1.0f, 1.0f, 1.0f);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f unitX() {
        return new ImmutableVector3f(1.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f unitY() {
        return new ImmutableVector3f(0.0f, 1.0f, 0.0f);
    }

    /**
     * <p>Creates a new vector of length one in the z-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f unitZ() {
        return new ImmutableVector3f(0.0f, 0.0f, 1.0f);
    }

    /**
     * <p>Creates a new vector of length minus one in the z-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f unitNegativeZ() {
        return new ImmutableVector3f(0.0f, 0.0f, -1.0f);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#NaN}</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f nan() {
        return new ImmutableVector3f(Float.NaN, Float.NaN, Float.NaN);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#POSITIVE_INFINITY}</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f positiveInfinity() {
        return new ImmutableVector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    }

    /**
     * <p>Creates a new vector where each component value is {@link Float#NEGATIVE_INFINITY}</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3f negativeInfinity() {
        return new ImmutableVector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }
}