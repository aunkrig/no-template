
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

package de.unkrig.notemplate.javadocish.templates;

import de.unkrig.commons.lang.AssertionUtil;
import de.unkrig.notemplate.NoTemplate;
import de.unkrig.notemplate.javadocish.Options;
import de.unkrig.notemplate.javadocish.templates.include.BottomHtml;
import de.unkrig.notemplate.javadocish.templates.include.TopHtml;

public abstract
class AbstractPackageFrameHtml extends NoTemplate {

    static { AssertionUtil.enableAssertionsForThisClass(); }

//    /**
//     * The label is displayed without a link. This indicates that the function is not available in this context, but
//     * may be in a different context.
//     */
//    protected static final String DISABLED = new String("You should never see this text");
//
//    /**
//     * The label is highlighted. This indicates that the document for this function is currently displayed.
//     */
//    protected static final String HIGHLIT  = new String("You should never see this text");

    /**
     * Renders a page for the "package frame", i.e. the frame that covers the left 20% of the JAVADOC frame set.
     * <p>
     *   The layout is as follows:
     * </p>
     * <pre>
     *   +--------------------------+
     *   |   (top navigation bar)   |
     *   +--------------------------+
     *   |                          |
     *   |                          |
     *   |          (body)          |
     *   |                          |
     *   |                          |
     *   +--------------------------+
     *   | (bottom navigation bar)  |
     *   +--------------------------+
     * </pre>
     * <p>
     *   For the structure of the top and bottom navigation bars, and the meaning of the <var>nav*</var> parameters,
     *   see here:
     * </p>
     * <dl>
     *   <dd>{{@link #rTopNavBar(Options, String[], String[], String[], String, String[], String[])}</dd>
     *   <dd>{{@link #rBottomNavBar(Options, String[], String[], String[], String, String[], String[])}</dd>
     * </dl>
     *
     * @param title          The window title (optionally augmented with {@link Options#windowTitle}
     * @param options        Container for the various command line options
     * @param stylesheetLink The (optional) external stylesheet for this page
     */
    protected void
    rPackageFrameHtml(
        String   heading,
        String   headingLink,
        Options  options,
        String   styleSheetLink,
        Runnable renderBody
    ) {

        this.include(TopHtml.class).render(heading, options, styleSheetLink);

        this.l(
"<h1 class=\"bar\"><a href=\"" + headingLink + "\" target=\"classFrame\">" + heading + "</a></h1>",
"<div class=\"indexContainer\">"
        );

        renderBody.run();

        this.l(
"</div>"
        );

        this.include(BottomHtml.class).render();
    }
}
