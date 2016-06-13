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
 * Represent an immutable {@linkplain Quaternionf}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class ImmutableQuaternionf extends Quaternionf {
    /**
     * <p>Default constructor</p>
     */
    public ImmutableQuaternionf(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    /**
     * <p></p>Creates a new quaternion where each component value is 0.0f</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf zero() {
        return new ImmutableQuaternionf(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion where each component value is 1.0f</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf one() {
        return new ImmutableQuaternionf(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the x-axis</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf unitX() {
        return new ImmutableQuaternionf(1.0f, 0.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the y-axis</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf unitY() {
        return new ImmutableQuaternionf(0.0f, 1.0f, 0.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the z-axis</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf unitZ() {
        return new ImmutableQuaternionf(0.0f, 0.0f, 1.0f, 0.0f);
    }

    /**
     * <p>Creates a new quaternion of length one in the w-axis</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf unitW() {
        return new ImmutableQuaternionf(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#NaN}</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf nan() {
        return new ImmutableQuaternionf(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#POSITIVE_INFINITY}</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf positiveInfinity() {
        return new ImmutableQuaternionf(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                Float.POSITIVE_INFINITY);
    }

    /**
     * <p>Creates a new quaternion where each component value is {@link Float#NEGATIVE_INFINITY}</p>
     *
     * @return a new quaternion
     */
    public static ImmutableQuaternionf negativeInfinity() {
        return new ImmutableQuaternionf(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
                , Float.NEGATIVE_INFINITY);
    }
}