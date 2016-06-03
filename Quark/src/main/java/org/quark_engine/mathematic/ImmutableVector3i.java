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
package org.quark_engine.mathematic;

/**
 * Represent an immutable {@linkplain Vector3i}.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class ImmutableVector3i extends Vector3i {
    /**
     * <p>Default constructor</p>
     */
    public ImmutableVector3i(int x, int y, int z) {
        super(x, y, z);
    }

    /**
     * <p>Creates a new vector where each component value is 0</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i zero() {
        return new ImmutableVector3i(0, 0, 0);
    }

    /**
     * <p>Creates a new vector where each component value is 1</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i one() {
        return new ImmutableVector3i(1, 1, 1);
    }

    /**
     * <p>Creates a new vector of length one in the x-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i unitX() {
        return new ImmutableVector3i(1, 0, 0);
    }

    /**
     * <p>Creates a new vector of length one in the y-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i unitY() {
        return new ImmutableVector3i(0, 1, 0);
    }

    /**
     * <p>Creates a new vector of length one in the z-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i unitZ() {
        return new ImmutableVector3i(0, 0, 1);
    }

    /**
     * <p>Creates a new vector of length minus one in the z-axis</p>
     *
     * @return a new vector
     */
    public static ImmutableVector3i unitNegativeZ() {
        return new ImmutableVector3i(0, 0, -1);
    }
}