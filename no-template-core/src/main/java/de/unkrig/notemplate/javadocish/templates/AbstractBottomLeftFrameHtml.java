
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

package de.unkrig.notemplate.javadocish.templates;

import de.unkrig.commons.lang.AssertionUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.javadocish.Options;

/**
 * Base class for the bottom left frame (which typically display class names).
 */
public abstract
class AbstractBottomLeftFrameHtml extends AbstractHtml {

    static { AssertionUtil.enableAssertionsForThisClass(); }

    /**
     * <pre>
     * [###heading###]      <= Blue bar
     * index-header         <= Optional; e.g. the "All Classes" link
     * index-container      <= The rest of the page
     * </pre>
     * @param options         Container for the various command line options
     * @param styleSheetLinks The (optional) external stylesheets for this page
     * @param heading         The heading of this page, and also the window title (optionally augmented with {@link
     *                        Options#windowTitle}
     * @param renderIndexHeader Renders the section between the
     */
    protected void
    rBottomLeftFrameHtml(
        String             windowTitle,
        Options            options,
        String[]           styleSheetLinks,
        @Nullable String   heading,
        @Nullable String   headingLink,
        @Nullable Runnable renderIndexHeader,
        Runnable           renderIndexContainer
    ) {

        this.rHtml(windowTitle, options, styleSheetLinks, () -> {

            if (heading != null) {
                String hwl = (
                    headingLink == null
                    ? heading
                    : "<a href=\"" + headingLink + "\" target=\"classFrame\">" + heading + "</a>"
                );
                this.l(
"    <h1 title=\"" + heading + "\" class=\"bar\">" + hwl + "</h1>"
                );
            }

            if (renderIndexHeader != null) {
                this.l(
"    <div class=\"indexHeader\">"
                );
                renderIndexHeader.run();
                this.l(
"    </div>"
                );
            }

            this.l(
"    <div class=\"indexContainer\">"
            );
            renderIndexContainer.run();
            this.l(
"    </div>"
            );
        });
    }
}
