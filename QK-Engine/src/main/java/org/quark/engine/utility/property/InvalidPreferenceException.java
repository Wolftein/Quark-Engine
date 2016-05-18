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
package org.quark.engine.utility.property;

/**
 * <code>InvalidPreferenceException</code> represent an {@link Exception} when a {@link PropertyTree} is invalid.
 *
 * @author Agustin L. Alvarez (wolftein1@gmail.com)
 */
public final class InvalidPreferenceException extends Exception {
    /**
     * <p>Simple constructor</p>
     *
     * @param message the exception's message
     */
    public InvalidPreferenceException(String message) {
        super(message);
    }

    /**
     * <p>Complex constructor</p>
     *
     * @param message   the exception's message
     * @param throwable the exception's throwable
     */
    public InvalidPreferenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}