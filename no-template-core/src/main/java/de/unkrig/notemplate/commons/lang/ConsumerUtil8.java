
/*
 * No-Template - an extremely light-weight templating framework
 *
 * Copyright (c) 2016, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote products derived from this software without
 *       specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.unkrig.notemplate.commons.lang;

import java.util.function.Consumer;

import de.unkrig.commons.lang.protocol.ConsumerUtil;
import de.unkrig.commons.lang.protocol.ConsumerWhichThrows;
import de.unkrig.commons.nullanalysis.Nullable;

/**
 * A Java 8-specific variant of {@link ConsumerUtil}.
 */
public final
class ConsumerUtil8 {

    private ConsumerUtil8() {}

    /**
     * Converts a {@link de.unkrig.commons.lang.protocol.Consumer} into a (Java 8) {@link java.util.function.Consumer}.
     */
    public static <T> java.util.function.Consumer<T>
    asJavaUtil(de.unkrig.commons.lang.protocol.Consumer<T> delegate) {
        return (@Nullable T subject) -> {
            assert subject != null;
            delegate.consume(subject);
        };
    }

    /**
     * Same as {@link ConsumerUtil#ignoreExceptions(Class, ConsumerWhichThrows)}, but returns a (Java 8) {@link
     * de.unkrig.commons.lang.protocol.Consumer }.
     */
    public static <EX extends Throwable, T> Consumer<T>
    ignoreExceptions(Class<EX> exceptionClass, ConsumerWhichThrows<T, EX> delegate) {
        return ConsumerUtil8.asJavaUtil(ConsumerUtil.ignoreExceptions(exceptionClass, delegate));
    }
}
