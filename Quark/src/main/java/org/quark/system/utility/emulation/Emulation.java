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
package org.quark.system.utility.emulation;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <o>Emulation</o> encapsulate a helper to emulate multiple method(s) not being present on every platform.
 */
@SuppressWarnings("WhileLoopReplaceableByForEach")
public class Emulation {
    /**
     * (NOT PRESENT IN TeaVM)
     */
    public static <A, B extends Consumer<A>> void forEachArrayIfNotNull(A[] iterator, B consumer) {
        for (final A item : iterator) {
            if (item != null) {
                consumer.accept(item);
            }
        }
    }

    /**
     * (NOT PRESENT IN TeaVM)
     */
    public static <A, B extends Consumer<A>> void forEach(Iterable<A> iterator, B consumer) {
        final Iterator<A> it = iterator.iterator();

        while (it.hasNext()) {
            consumer.accept(it.next());
        }
    }

    /**
     * (NOT PRESENT IN TeaVM)
     */
    public static <A, B, C extends BiConsumer<A, B>> void forEach(Map<A, B> map, C consumer) {
        for (final Map.Entry<A, B> entry : map.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    /**
     * (NOT PRESENT IN TeaVM)
     */
    public static <A, B extends Function<A, Integer>> int forEachToMapInt(Iterable<A> iterator, B function) {
        int number = 0;

        final Iterator<A> it = iterator.iterator();

        while (it.hasNext()) {
            number += function.apply(it.next());
        }
        return number;
    }
}
