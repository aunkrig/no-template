
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
 * Renders the typical "class frame" (the frame that covers the right 80% of the frame set) page.
 */
public abstract
class AbstractRightFrameHtml extends NoTemplate {

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
     *   For the structure of the top and bottom navigation bars, and the meaning of the <var>nav*</var> parameters,
     *   see here:
     * </p>
     * <dl>
     *   <dd>{@link #rTopNavBar(Options, String[], String[], String[], String, String[], String[])}</dd>
     *   <dd>{@link #rBottomNavBar(Options, String[], String[], String[], String, String[], String[])}</dd>
     * </dl>
     * <p>
     *   The HTML structure of the body should look like this:
     * </p>
     * <pre>
     * {@code
     * <div class="header">
     *   <div class="subTitle">com.acme.util</div>
     *   <h2 title="Class LirumLarum" class="title">Class LirumLarum&lt;T&gt;</h2>
     * </div>
     * <div class="contentContainer">
     * <ul class="inheritance">
     *   <li>java.lang.Object</li>
     *   <ul class="inheritance">
     *     <li>LirumLarum</li>
     *   </ul>
     * </ul>
     * <div class="description">
     *   <ul class="blockList">
     *     <li class="blockList">
     *       <dl>
     *         <dt><span class="strong">Type Parameters:</span></dt>
     *         <dd><code>T</code> - The element type</dd>
     *       </dl>
     *       <dl>
     *         <dt>All Implemented Interfaces:</dt>
     *         <dd>
     *           Interface1, Interface2
     *         </dd>
     *       </dl>
     *       <dl>
     *         <dt>Enclosing class:</dt>
     *         <dd>EnclosingClass</dd>
     *       </dl>
     *       <dl>
     *         <dt>All Known Subclasses:</dt>
     *         <dd><a href="...">MySuperClass</a></dd>
     *       </dl>
     *       <hr>
     *       <br>
     *       <pre>public static abstract class <span class="strong">LirumLarum</span> extends BaseClass</pre>
     *       <div class="block">This is the first sentence. And this is the rest of the description.</div>"
     *       <dl>
     *         <dt><span class="strong">See Also:</span></dt>
     *         <dd><a href="..."><code>SomeOtherClass</code></a> The label of this tag</dd>
     *       </dl>
     *     </li>
     *   </ul>
     * </div>
     * <div class="summary">
     *   <ul class="blockList">
     *     <li class="blockList">
     *       <ul class="blockList">
     *         <li class="blockList">
     *           <a name="field_summary"><!--   --></a>
     *           <h3>Field Summary</h3>
     *           <table class="overviewSummary" border="0" cellpadding="3" cellspacing="0" summary="Field Summary table, listing fields, and an explanation">
     *             <caption><span>Fields</span><span class="tabEnd">&nbsp;</span></caption>
     *             <tr>
     *               <th class="colFirst" scope="col">Modifier and Type</th>
     *               <th class="colLast" scope="col">Field and Description</th>
     *             </tr>
     *             <tr class="altColor">
     *               <td class="colFirst"><code>static int a</code></td>
     *               <td class="colLast">
     *                 <code><strong><a href="...">a</a></strong></code>
     *                 <div class="block">First sentence of field description:</div>
     *               </td>
     *             </tr>
     *           </table>
     *         </li>
     *       </ul>
     *
     *       <!-- Same for nested classes, constructors, methods. -->
     *
     *     </li>
     *   </ul>
     * </div>
     * <div class="details">
     *   <ul class="blockList">
     *     <li class="blockList">
     *       <ul class="blockList">
     *         <li class="blockList">
     *           <a name="field_detail"><!-- --></a>
     *           <h3>Field Detail</h3>
     *           <a name="field2"><!-- --></a>
     *           <ul class="blockListLast">
     *             <li class="blockList">
     *               <h4>field2</h4>
     *               <pre>static int field2</pre>
     *               <div class="block">First sentence of field description. And more sentences.</div>
     *               <dl>
     *                 <dt><span class="strong">See Also:</span></dt>"
     *                 <dd><a href="..."><code>SomeOtherElement</code></a> Tag label</dd>
     *               </dl>
     *             </li>
     *           </ul>
     *         </li>
     *       </ul>
     *
     *       <!-- Same for constructors and methods detail. -->
     *
     *     </li>
     *   </ul>
     * </div>
     * }</pre>
     *
     * @param windowTitle     The window title (optionally augmented with {@link Options#windowTitle}
     * @param options         Container for the various command line options
     * @param stylesheetLinks The (optional) external stylesheet for this page
     */
    protected void
    rClassFrameHtml(
        String             windowTitle,
        Options            options,
        @Nullable String[] stylesheetLinks,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        @Nullable String[] nav5,
        @Nullable String[] nav6,
        Runnable           renderBody
    ) {

        this.include(TopHtml.class).render(windowTitle, options, stylesheetLinks);

        String wt = options.windowTitle == null ? "" : " (" + options.windowTitle + ")";
        this.l(
"<script type=\"text/javascript\"><!--",
"    if (location.href.indexOf('is-external=true') == -1) {",
"        parent.document.title=\"" + windowTitle + wt + "\";",
"    }",
"//-->",
"</script>",
"<noscript>",
"<div>JavaScript is disabled on your browser.</div>",
"</noscript>"
        );

        this.rTopNavBar(options, nav1, nav2, nav3, nav4, nav5, nav6);

        renderBody.run();

        this.rBottomNavBar(options, nav1, nav2, nav3, nav4, nav5, nav6);

        this.include(BottomHtml.class).render();
    }

    /**
     * Renders a "top navigation bar".
     * <p>
     *   The structure of the "top navigation bar" is as follows:
     * </p>
     * <pre>
     *                                               (options.top)
     *   +----------------------------------------------------------+
     *   | (nav1)                                  (options.header) |
     *   +----------------------------------------------------------+
     *   | (nav2)  (nav3)  (nav4)                                   |
     *   +----------------------------------------------------------+
     *   | Summary: (nav5)   Detail: (nav6)                         |
     *   +----------------------------------------------------------+
     * </pre>
     *
     * @see #rNavBar(String, String, String[], String[], String[], String, String[], String[])
     */
    private void
    rTopNavBar(
        Options            options,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        @Nullable String[] nav5,
        @Nullable String[] nav6
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

        this.rNavBar("top", options.header, nav1, nav2, nav3, nav4, nav5, nav6);

        this.l(
"<!-- ========= END OF TOP NAVBAR ========= -->"
        );
    }

    /**
     * Renders a "bottom navigation bar".
     * <p>
     *   The structure of the "top navigation bar" is as follows:
     * </p>
     * <pre>
     *   +----------------------------------------------------------+
     *   | (nav1)                                  (options.footer) |
     *   +----------------------------------------------------------+
     *   | (nav2)  (nav3)  (nav4)                                   |
     *   +----------------------------------------------------------+
     *   | Summary: (nav5)   Detail: (nav6)                         |
     *   +----------------------------------------------------------+
     *                                               (options.bottom)
     * </pre>
     *
     * @see #rNavBar(String, String, String[], String[], String[], String, String[], String[])
     */
    private void
    rBottomNavBar(
        Options            options,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        @Nullable String[] nav5,
        @Nullable String[] nav6
    ) {

        this.l(
"<!-- ======= START OF BOTTOM NAVBAR ====== -->"
        );

        this.rNavBar("bottom", options.footer, nav1, nav2, nav3, nav4, nav5, nav6);

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

    /**
     * Renders a "top" or "bottom" navigation bar.
     * <p>
     *   The structure of the "top", resp. "bottom" navigation bar is as follows:
     * </p>
     * <pre>
     *   +----------------------------------------------------------+
     *   | (nav1)                             (header resp. footer) |
     *   +----------------------------------------------------------+
     *   | (nav2)  (nav3)  (nav4)                                   |
     *   +----------------------------------------------------------+
     *   | Summary: (nav5)   Detail: (nav6)                         |
     *   +----------------------------------------------------------+
     * </pre>
     * <p>
     *   {@code nav1}, {@code nav3}, {@code nav4}, {@code nav5} and {@code nav6} are label-link pairs. Entries with a
     *   {@code null} label are ignored. Links can have the following values:
     * </p>
     * <p>
     *   {@code nav2} is an array of HTML fragments. {@code null} entries are ignored.
     * </p>
     * <dl>
     *   <dt>{@link #DISABLED}</dt>
     *   <dd>
     *     The label is displayed without a link. This indicates that the function is not available in this context,
     *     but may be in a different context.
     *   </dd>
     *   <dt>{@link #HIGHLIT}</dt>
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
     * @param nav1 Typically {@code [ "Overview", x, "Package", x, "Class", x, "Tree", x, "Deprecated", x, "Index", x,
     *             "Help", x ]}, or {@code null} to suppress navigation bar 1
     * @param nav2 Typically {@code [ "<a href=\"...\">Prev Class</a>", "Next Class" ]}, or {@code null} to suppress
     *             navigation bar 2
     * @param nav3 Typically {@code [ "Frames", x, "No Frames", x, "All Classes", x ]}, or {@code null} to suppress
     *             navigation bar 3
     * @param nav4 Typically {@code [ "All Classes", x ]}, and is automagically hidden iff the page resides in a frame
     *             (opposed to the "top" browser window")
     * @param nav5 Typically {@code [ "Nested", x, "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress
     *             navigation bar 5
     * @param nav6 Typically {@code [ "Field", x, "Constr", x, "Method", x ]}, or {@code null} to suppress navigation
     *             bar 6
     */
    private void
    rNavBar(
        String             kind,
        @Nullable String   headerFooter,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        @Nullable String[] nav5,
        @Nullable String[] nav6
    ) {

        // Render "nav1".
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

                if (link == AbstractRightFrameHtml.HIGHLIT) {
                    this.l(
"<li class=\"navBarCell1Rev\">" + labelHtml + "</li>"
                    );
                } else
                if (link == AbstractRightFrameHtml.DISABLED) {
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

            // Render "nav2".
            if (nav2 != null) {

                this.l(
"<ul class=\"navList\">"
                );
                for (int i = 0; i < nav2.length;) {
                    String html = nav2[i++];

                    if (html == null) continue;

                    this.l(
"<li>" + html + "</li>"
                    );
                }
                this.l(
"</ul>"
                );
            }

            // Render "nav3".
            if (nav3 != null) {
                assert nav3.length % 2 == 0;

                this.l(
"<ul class=\"navList\">"
                );
                for (int i = 0; i < nav3.length;) {
                    String labelHtml = nav3[i++];
                    String link      = nav3[i++];

                    this.l(
"<li><a href=\"" + link + "\" target=\"_top\">" + labelHtml + "</a></li>"
                    );
                }

                this.l(
"</ul>"
                );
            }
        }

        // Render "nav4".
        if (nav4 != null) {
            assert nav4.length % 2 == 0;

            this.l(
"<ul class=\"navList\" id=\"allclasses_navbar_" + kind + "\">"
            );
            for (int i = 0; i < nav4.length;) {
                String labelHtml = nav4[i++];
                String link      = nav4[i++];

                this.l(
"<li><a href=\"" + link + "\">" + labelHtml + "</a></li>"
                );
            }
            this.l(
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

        if (nav5 != null || nav6 != null) {

            // Render "nav5".
            this.l(
"<div>"
            );

            if (nav5 != null) {
                assert nav5.length % 2 == 0;

                this.l(
"<ul class=\"subNavList\">",
"<li>Summary:&nbsp;</li>"
                );
                for (int i = 0; i < nav5.length;) {
                    String labelHtml = nav5[i++];
                    String link      = nav5[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    this.p("<li>");
                    if (link == AbstractRightFrameHtml.DISABLED) {
                        this.p(labelHtml);
                    } else {
                        this.p("<a href=\"" + link + "\">" + labelHtml + "</a>");
                    }
                    if (i != nav5.length) this.p("&nbsp;|&nbsp;");
                    this.l("</li>");
                }
                this.l(
"</ul>"
                );
            }

            // Render "nav6".
            if (nav6 != null) {
                assert nav6.length % 2 == 0;

                this.l(
"<ul class=\"subNavList\">",
"<li>Detail:&nbsp;</li>"
                );
                for (int i = 0; i < nav6.length;) {
                    String labelHtml = nav6[i++];
                    String link      = nav6[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    this.p("<li>");
                    if (link == AbstractRightFrameHtml.DISABLED) {
                        this.p(labelHtml);
                    } else {
                        this.p("<a href=\"" + link + "\">" + labelHtml + "</a>");
                    }
                    if (i != nav6.length) this.p("&nbsp;|&nbsp;");
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
