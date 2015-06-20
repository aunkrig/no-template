
/*
 * no-template - an extremely light-weight templating framework
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

package de.unkrig.notemplate.javadocish;

import java.io.File;
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
}
