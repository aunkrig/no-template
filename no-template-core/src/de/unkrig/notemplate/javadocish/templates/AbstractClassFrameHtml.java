
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
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.NoTemplate;
import de.unkrig.notemplate.javadocish.Options;
import de.unkrig.notemplate.javadocish.templates.include.BottomHtml;
import de.unkrig.notemplate.javadocish.templates.include.TopHtml;

/**
 * Does a big part of the work if you plan to generate "JAVADOC-like" documentation, i.e. the frameset and the three
 * frames.
 */
public abstract
class AbstractClassFrameHtml extends NoTemplate {

    /**
     * The label is displayed without a link. This indicates that the function is not available in this context, but
     * may be in a different context.
     */
    protected static final String DISABLED = new String("You should never see this text");

    /**
     * The label is highlighted. This indicates that the document for this function is currently displayed.
     */
    protected static final String HIGHLIT  = new String("You should never see this text");

    static { AssertionUtil.enableAssertionsForThisClass(); }

    /**
     * Renders the "body" section of the page.
     *
     * @see #rClassFrameHtml(String, Options, String, String[], String[], String[], String, String[], String[])
     */
    protected abstract void
    rClassFrameBody();

    /**
     * Renders a page for the "class frame", i.e. the frame that covers the right 80% of the JAVADOC frame set.
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
     *   The structure of the top an bottom navigation bare is identical:
     * </p>
     * <pre>
     *   +----------------------------------------------------------+
     *   | (nav1)                             (header resp. footer) |
     *   +----------------------------------------------------------+
     *   | (nav2)  (nav3)  (allclasses)                             |
     *   +----------------------------------------------------------+
     *   | Summary: (nav4)   Detail: (nav5)                         |
     *   +----------------------------------------------------------+
     * </pre>
     * <p>
     *   All of {@code nav1}, {@code nav2}, {@code nav3}, {@code nav4} and {@code nav5} label-link pairs. Entries
     *   with a {@code null} label are ignored. Links can have the following values:
     * </p>
     * <dl>
     *   <dt>{@link #DISABLED}</dt>
     *   <dd>
     *     The label is displayed without a link. This indicates that the function is not available in this context,
     *     but may be in a different context.
     *   </dd>
     *   <dt>{@#link #HIGHLIT}</dt>
     *   <dd>
     *     The label is highlighted. This indicates that the document for this function is currently displayed.
     *   </dd>
     *   <dt>(Any other value)</dt>
     *   <dd>
     *     The label is displayed with a link.
     *   </dd>
     * </dl>
     * <p>
     *   Example: The "Package" function is typically {@link #DISABLED} on the "overview packge", highlit on a "package
     *   page", and, on a "class page", displayed with a link to the class's package page.
     * </p>
     *
     * @param title          The window title (optionally augmented with {@link Options#windowTitle}
     * @param options        Container for the various command line options
     * @param stylesheetLink The (optional) external stylesheet for this page
     * @param allClassesLink Appears with a label "All Classes", and is automagically hidden iff the page resides in
     *                       a frame (opposed to the "top" browser window")
     */
    protected void
    rClassFrameHtml(
        String             title,
        Options            options,
        @Nullable String   stylesheetLink,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String   allClassesLink,
        @Nullable String[] nav4,
        @Nullable String[] nav5
    ) {

        this.include(TopHtml.class).render(title, options, stylesheetLink);

        String wt = options.windowTitle == null ? "" : " (" + options.windowTitle + ")";
        this.l(
"<script type=\"text/javascript\"><!--",
"    if (location.href.indexOf('is-external=true') == -1) {",
"        parent.document.title=\"" + title + wt + "\";",
"    }",
"//-->",
"</script>",
"<noscript>",
"<div>JavaScript is disabled on your browser.</div>",
"</noscript>"
        );

        this.rTopNavBar(options, nav1, nav2, nav3, allClassesLink, nav4, nav5);

        this.rClassFrameBody();

        this.rBottomNavBar(options, nav1, nav2, nav3, allClassesLink, nav4, nav5);

        this.include(BottomHtml.class).render();
    }

    /**
     * @param nav1 Typically {@code [ "Overview", x, "Package", x, "Class", x, "Tree", x, "Deprecated", x, "Index", x,
     *             "Help", x ]}, or {@code null} to suppress navigation bar 1
     * @param nav2 Typically {@code [ "Prev Class", x, "Next Class", x ]}, or {@code null} to suppress navigation bar 2
     * @param nav3 Typically {@code [ "Frames", x, "No Frames", x, "All Classes", x ]}, or {@code null} to suppress
     *             navigation bar 3
     * @param nav4 Typically {@code [ "Nested", x, "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress
     *             navigation bar 4
     * @param nav5 Typically {@code [ "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress navigation
     *             bar 5
     */
    private void
    rTopNavBar(
        Options            options,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String   allClassesLink,
        @Nullable String[] nav4,
        @Nullable String[] nav5
    ) {

        // "-top" command line option.
        if (options.top != null) {
            this.l(
options.top
            );
        }

        this.l(
"<!-- ========= START OF TOP NAVBAR ======= -->"
        );

        this.rNavBar("top", options.header, nav1, nav2, nav3, allClassesLink, nav4, nav5);

        this.l(
"<!-- ========= END OF TOP NAVBAR ========= -->"
        );
    }

    /**
     * @param nav1 Typically {@code [ "Overview", x, "Package", x, "Class", x, "Tree", x, "Deprecated", x, "Index", x,
     *             "Help", x ]}, or {@code null} to suppress navigation bar 1
     * @param nav2 Typically {@code [ "Prev Class", x, "Next Class", x ]}, or {@code null} to suppress navigation bar 2
     * @param nav3 Typically {@code [ "Frames", x, "No Frames", x, "All Classes", x ]}, or {@code null} to suppress
     *             navigation bar 3
     * @param nav4 Typically {@code [ "Nested", x, "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress
     *             navigation bar 4
     * @param nav5 Typically {@code [ "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress navigation
     *             bar 5
     */
    private void
    rBottomNavBar(
        Options            options,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String   allClassesLink,
        @Nullable String[] nav4,
        @Nullable String[] nav5
    ) {

        this.l(
"<!-- ======= START OF BOTTOM NAVBAR ====== -->"
        );

        this.rNavBar("top", options.header, nav1, nav2, nav3, allClassesLink, nav4, nav5);

        this.l(
"<!-- ======== END OF BOTTOM NAVBAR ======= -->"
        );

        // "-bottom" command line option.
        if (options.bottom != null) {
            this.l(
"<p class=\"legalCopy\"><small>" + options.bottom + "</small></p>"
            );
        }
    }

    private void
    rNavBar(
        String             kind,
        @Nullable String   headerFooter,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String   allClassesLink,
        @Nullable String[] nav4,
        @Nullable String[] nav5
    ) {
        if (nav1 != null) {
            assert nav1.length % 2 == 0;

            this.l(
"<div class=\"" + kind + "Nav\"><a name=\"navbar_" + kind + "\">",
"<!--   -->",
"</a><a href=\"#skip-navbar_" + kind + "\" title=\"Skip navigation links\"></a><a name=\"navbar_" + kind + "_firstrow\">", // SUPPRESS CHECKSTYLE LineLength
"<!--   -->",
"</a>",
"<ul class=\"navList\" title=\"Navigation\">"
            );

            for (int i = 0; i < nav1.length;) {
                String labelHtml = nav1[i++];
                String link      = nav1[i++];

                if (labelHtml == null) continue;

                assert link != null;

                if (link == AbstractClassFrameHtml.HIGHLIT) {
                    this.l(
"<li class=\"navBarCell1Rev\">" + labelHtml + "</li>"
                    );
                } else
                if (link == AbstractClassFrameHtml.DISABLED) {
                    this.l(
"<li>" + labelHtml + "</li>"
                    );
                } else
                {
                    this.l(
"<li><a href=\"" + link + "\">" + labelHtml + "</a></li>"
                    );
                }
            }

            this.l(
"</ul>"
            );

            // "-header", resp. "-footer" command line option.
            if (headerFooter != null) {
                this.l(
"<div class=\"aboutLanguage\"><em>" + headerFooter + "</em></div>"
                );
            }
            this.l(
"</div>"
            );
        }

        this.l(
"<div class=\"subNav\">"
        );

        if (nav2 != null || nav3 != null) {
            if (nav2 != null) {

                assert nav2.length % 2 == 0;

                this.l(
"<ul class=\"navList\">"
                );
                for (int i = 0; i < nav2.length;) {
                    String labelHtml = nav2[i++];
                    String link      = nav2[i++];

                    if (labelHtml == null) continue;

                    if (link == AbstractClassFrameHtml.DISABLED) {
                        this.l(
"<li>" + labelHtml + "</li>"
                        );
                    } else {
                        this.l(
"<li><a href=\"" + link + "\">" + labelHtml + "</a></li>"
                        );
                    }
                }
                this.l(
"</ul>"
                );
            }

            if (nav3 != null) {
                assert nav3.length % 2 == 0;

                this.l(
"<ul class=\"navList\">"
                );
                for (int i = 0; i < nav3.length;) {
                    String labelHtml = nav3[i++];
                    String link      = nav3[i++];

                    this.l(
"<li><a href=\"" + link + "\" target=\"_" + kind + "\">" + labelHtml + "</a></li>"
                    );
                }

                this.l(
"</ul>"
                );
            }
        }

        if (allClassesLink != null) {
            this.l(
"<ul class=\"navList\" id=\"allclasses_navbar_" + kind + "\">",
"<li><a href=\"" + allClassesLink + "\">All Classes</a></li>",
"</ul>",
"<div>",
"<script type=\"text/javascript\"><!--",
"  allClassesLink = document.getElementById(\"allclasses_navbar_" + kind + "\");",
"  if(window==top) {",
"    allClassesLink.style.display = \"block\";",
"  }",
"  else {",
"    allClassesLink.style.display = \"none\";",
"  }",
"  //-->",
"</script>",
"</div>"
            );
        }

        if (nav4 != null || nav5 != null) {
            this.l(
"<div>"
            );

            if (nav4 != null) {
                assert nav4.length % 2 == 0;

                this.l(
"<ul class=\"subNavList\">",
"<li>Summary:&nbsp;</li>"
                );
                final Once first = NoTemplate.once();
                for (int i = 0; i < nav4.length;) {
                    String labelHtml = nav4[i++];
                    String link      = nav4[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    this.p("<li>");
                    if (!first.once()) this.p("&nbsp;|&nbsp;");
                    if (link == AbstractClassFrameHtml.DISABLED) {
                        this.p(labelHtml);
                    } else {
                        this.p("<a href=\"" + link + "\">" + labelHtml + "</a>");
                    }
                    this.l("</li>");
                }
                this.l(
"</ul>"
                );
            }

            if (nav5 != null) {
                assert nav5.length % 2 == 0;

                this.l(
"<ul class=\"subNavList\">",
"<li>Detail:&nbsp;</li>"
                );
                final Once first = NoTemplate.once();
                for (int i = 0; i < nav5.length;) {
                    String labelHtml = nav5[i++];
                    String link      = nav5[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    this.p("<li>");
                    if (!first.once()) this.p("&nbsp;|&nbsp;");
                    if (link == AbstractClassFrameHtml.DISABLED) {
                        this.p(labelHtml);
                    } else {
                        this.p("<a href=\"" + link + "\">" + labelHtml + "</a>");
                    }
                    this.l("</li>");
                }
                this.l(
"</ul>"
                );
            }
            this.l(
"</div>"
            );
        }

        this.l(
"<a name=\"skip-navbar_" + kind + "\">",
"<!--   -->",
"</a></div>"
        );
    }
}
