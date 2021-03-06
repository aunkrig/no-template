
/*
 * No-Template - an extremely light-weight templating framework
 *
 * Copyright (c) 2015, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.unkrig.notemplate.javadocish;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;

import de.unkrig.commons.nullanalysis.Nullable;

/**
 * Container for the command line options of the JAVADOC doclet.
 */
public
class Options {

    /** The argument of the "-d" command line option. */
    public File destination = new File(".");

    /** The argument of the "-windowtitle" command line option. */
    @Nullable public String windowTitle;

    /** The argument of the "-doctitle" command line option. */
    @Nullable public String docTitle;

    /** The argument of the "-header" command line option. */
    @Nullable public String header;

    /** The argument of the "-footer" command line option. */
    @Nullable public String footer;

    /** Whether the "-quiet" command line option is given. */
    public boolean quiet;

    /** The argument of the "-top" command line option. */
    @Nullable public String top;

    /** The argument of the "-bottom" command line option. */
    @Nullable public String bottom;

    /** Whether the "-notimestamp" command line option is given. */
    public boolean noTimestamp;

    /** The "generation" date that is rendered into the head of all documents. */
    public final Date generationDate = new Date();

    /** The name of the "generator" as it will appear in an HTML comment near the top of each document. */
    public String generator = "javadoc";

    /** Whether to split the index by initial. */
    public boolean splitIndex;

    /**
     * Inserts the following line in the head of every generated page:
     * <p>
     *    {@code <META http-equiv="Content-Type" content="text/html; charset=}<var>htmlCharset</var>{@code ">}
     * </p>
     *
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html#charset">The "{@code
     *      -charset}" command line option of the JAVADOC tool</a>
     */
    @Nullable public String htmlCharset;

    /**
     * The charset of the generated HTML files.
     *
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html#docencoding">The {@code
     *      -docencoding}" command line option of the JAVADOC tool</a>
     */
    public Charset documentCharset = Charset.defaultCharset();
}
