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
package ar.com.quark.render.storage;

/**
 * <code>StorageType</code> enumerate {@link Storage} type(s).
 */
public enum StorageType {
    /**
     * The buffer of the storage is located in the client-side (never disposed, until the storage is being removed).
     */
    CLIENT,

    /**
     * The buffer of the storage is located in the server-side (and be disposed once it's uploaded).
     */
    SERVER,

    /**
     * The buffer of the storage is located in the server-side (and access though mapping).
     */
    SERVER_MAPPED
}
