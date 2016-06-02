
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
import de.unkrig.notemplate.javadocish.Options;

/**
 * Renders the typical "class frame" (the frame that covers the right 80% of the frame set) page.
 * <p>
 *   The following building blocks appear in more than one document:
 * </p>
 * <dl>
 *   <dt>Heading:</dt>
 *   <dd>{@code <h1 class="title">}</dd>
 *   <dt>Summary table:</dt>
 *   <dd>
 *     <pre>
 * heading1
 * +-----------------+
 * | heading2        |
 * +-----------------+---+---------------------+---------------------+
 * | th1                 | th2                 | th3...              |
 * +---------------------+---------------------+---------------------+
 * | text1               | text2               | text3...            |
 * | text1               | text2               | text3...            | &lt- slightly darker background
 * | text1               | text2               | text3...            |
 * | text1               | text2               | text3...            | &lt- slightly darker background
 * | ...                 | ...                 | ...                 |
 * +---------------------+---------------------+---------------------+
 *
 * +-----------------------------------------------------------------+
 * | addendum                                                        |
 * +-----------------------------------------------------------------+
 * | ...                                                             |
 * +-----------------------------------------------------------------+
 *     </pre>
 *   </dd>
 *   <dt>Section summary:</dt>
 *   <dd>
 *     <pre>
 * +---------------------------------------------------------------------+
 * | <i>heading1</i>                                                            |
 * | +-----------------+                                                 |
 * | | heading2        |                                                 |
 * | +-----------------+---+---------------------+---------------------+ |
 * | | th1                 | th2                 | th3...              | |
 * | +---------------------+---------------------+---------------------+ |
 * | | text1               | text2               | text3...            | |
 * | | text1               | text2               | text3...            | | &lt- slightly darker background
 * | | text1               | text2               | text3...            | |
 * | | text1               | text2               | text3...            | | &lt- slightly darker background
 * | | ...                 | ...                 | ...                 | |
 * | +---------------------+---------------------+---------------------+ |
 * +---------------------------------------------------------------------+
 *     </pre>
 *   </dd>
 *   <dt>Section detail:</dt>
 *   <dd>
 *     <pre>
 * +----------------------------------+
 * | heading1                         |
 * | +------------------------------+ |
 * | | heading2                     | |
 * | +------------------------------+ |
 * | | (Free text.)                 | |
 * | +------------------------------+ |
 * | ...                              |
 * +----------------------------------+
 *     </pre>
 *   </dd>
 * </dl>
 * <p>
 *   There are nine kinds of documents that are composed from these building blocks:
 * </p>
 * <dl>
 *   <dt>{@code constant-values.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>"Constant field values"</dd>
 *       <dt>Free text:</dt>
 *       <dd>Contents ({@code <h2 title="Contents">})<br />com.acme.pkg1.*<br />com.acme.pkg2.*</dd>
 *       <dt>Summary table:</dt>
 *       <dd>
 *         <dl>
 *           <dt>heading1:</dt><dd><var>package-name</var></dd>
 *           <dt>heading2:</dt><dd><var>class-name</var></dd>
 *           <dt>th1:</dt>     <dd>"Modifier and Type"</dd>
 *           <dt>th2:</dt>     <dd>"Constant Field"</dd>
 *           <dt>th3:</dt>     <dd>"Value"</dd>
 *         </dl>
 *       </dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code deprecated-list.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>"Deprecated API"</dd>
 *       <dt>Free text:</dt>
 *       <dd>Contents ({@code <h2 title="Contents">})<br />Deprecated Fields</dd>
 *       <dt>Summary table:</dt>
 *       <dd>
 *         <dl>
 *           <dt>heading1:</dt><dd>(none)</dd>
 *           <dt>heading2:</dt><dd>"Deprecated Fields"</dd>
 *           <dt>th1:</dt>     <dd>"Field and Description"</dd>
 *         </dl>
 *       </dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code help-doc.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>"How This API Document Is Organized"</dd>
 *       <dt>Free text:</dt>
 *       <dd>This API document...</dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code index-*.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>(none)</dd>
 *       <dt>Free text:</dt>
 *       <dd>A B C D E<br />A<br />MyClass - Class in pkg.pkg<br />...</dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code overview-summary.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>-doctitle</dd>
 *       <dt>Free text:</dt>
 *       <dd>(First sentence from "overview.html")<br />See: Description</dd>
 *       <dt>Summary table:</dt>
 *       <dd>
 *         <dl>
 *           <dt>heading1:</dt><dd>(none)</dd>
 *           <dt>heading2:</dt><dd>"Packages"</dd>
 *           <dt>th1:</dt>     <dd>"Package"</dd>
 *           <dt>th2:</dt>     <dd>"Description"</dd>
 *         </dl>
 *       </dd>
 *       <dt>Free text:</dt>
 *       <dd>(Content of "overview.html".)</dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code overview-tree.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>Hierarchy For All Packages</dd>
 *       <dt>Free text:</dt>
 *       <dd>
 *         Package Hierarchies:
 *         <br />
 *         pkg.pkg1, pkg.pkg2, ...
 *         <br />
 *         Class Hierarchy
 *         <br />
 *         <ul>
 *           <li>java.lang.Object</li>
 *           <li>...</li>
 *         </ul>
 *         <br />
 *         Interface Hierarchy<br />
 *         <ul>
 *           <li>pkg.pkg.MyInterface</li>
 *           <li>...</li>
 *         </ul>
 *       </dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code pkg/pkg/MyClass.Inner.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Free text:</dt>
 *       <dd>
 *         (package name)
 *         <h2>Class (simple class name)</h2>
 *         java.lang.Object
 *         <dl><dd>
 *           pkg.pkg.Foo
 *           <dl><dd>
 *             (qualified class name)
 *           </dd></dl>
 *         </dd></dl>
 *         <hr />
 *         public abstract class Foo
 *         <br />
 *         extends (simple superclass name)
 *         <br />
 *         (first sentence of description)
 *       </dd>
 *       <dt>Section summary:</dt>
 *       <dd>
 *         <dl>
 *           <dt>Summary table:</dt>
 *           <dd>
 *             <dl>
 *               <dt>heading1:</dt><dd>"Nested Class Summary"</dd>
 *               <dt>heading2:</dt><dd>"Nested classes" ("Nested Interfaces", ...)</dd>
 *               <dt>th1:</dt>     <dd>"Modifier and Type"</dd>
 *               <dt>th2:</dt>     <dd>"Class and Description"</dd>
 *             </dl>
 *           <dt>Addendum:</dt>
 *           <dd>"Nested classes/interfaces inherited from (superclass)"</dd>
 *         </dl>
 *       </dd>
 *       <dt>Section summary:</dt>
 *       <dd>
 *         <dl>
 *           <dt>Summary table:</dt>
 *           <dd>
 *             <dl>
 *               <dt>heading1:</dt><dd>"Constructor Summary"</dd>
 *               <dt>heading2:</dt><dd>"Constructors"</dd>
 *               <dt>th1:</dt>     <dd>"Constructor and Description"</dd>
 *             </dl>
 *         </dl>
 *       </dd>
 *       <dt>Section summary:</dt>
 *       <dd>
 *         <dl>
 *           <dt>Summary table:</dt>
 *           <dd>
 *             <dl>
 *               <dt>heading1:</dt><dd>"Method Summary"</dd>
 *               <dt>heading2:</dt><dd>"Methods"</dd>
 *               <dt>th1:</dt>     <dd>"Modifier and Type"</dd>
 *               <dt>th2:</dt>     <dd>"Method and Description"</dd>
 *             </dl>
 *           </dd>
 *           <dt>Addendum:</dt>
 *           <dd>"Methods inherited from class (immediate superclass)"</dd>
 *           <dt>Addendum:</dt>
 *           <dd>"Methods inherited from class (super-superclass)"</dd>
 *         </dl>
 *       </dd>
 *       <dt>Section detail:</dt>
 *       <dd>
 *         <dl>
 *           <dt>heading1:</dt><dd>"Constructor Detail" ("Method Detail", ...)</dd>
 *           <dt>heading2:</dt><dd>(Constructor signature)</dd>
 *         </dl>
 *       </dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code pkg/pkg/package-summary.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>Package (package-name)</dd>
 *       <dt>Free text:</dt>
 *       <dd>(First sentence of package description)<br />See: Description</dd>
 *       <dt>Summary table:</dt>
 *       <dd>
 *         <dl>
 *           <dt>heading2:</dt><dd>"Interface Summary" ("Class Summary", ...)</dd>
 *           <dt>th1:</dt>     <dd>"Interface"</dd>
 *           <dt>th2:</dt>     <dd>"Description"</dd>
 *         </dl>
 *       </dd>
 *     </dl>
 *   </dd>
 *   <dt>{@code pkg/pkg/package-tree.html}</dt>
 *   <dd>
 *     <dl>
 *       <dt>Heading:</dt>
 *       <dd>Hierarchy For Package pkg.pkg</dd>
 *       <dt>Free text:</dt>
 *         Package Hierarchies:
 *         <br />
 *         All Packages
 *         <br />
 *         Class Hierarchy
 *         <br />
 *         <ul>
 *           <li>
 *             java.lang.Object
 *             <ul>
 *               <li>...</li>
 *             </ul>
 *           </li>
 *         </ul>
 *         <br />
 *         Interface Hierarchy<br />
 *         <ul>
 *           <li>
 *             pkg.pkg.MyInterface
 *             <ul>
 *               <li>...</li>
 *             </ul>
 *           </li>
 *         </ul>
 *     </dl>
 *   </dd>
 * </dl>
 */
public
class AbstractRightFrameHtml extends AbstractHtml {

    /**
     * The label is displayed without a link. This indicates that the function is not available in this context, but
     * may be in a different context.
     */
    public static final String DISABLED = new String("You should never see this text");

    /**
     * The label is highlighted. This indicates that the document for this function is currently displayed.
     */
    public static final String HIGHLIT  = new String("You should never see this text");

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
     *   <dd>{@link #rTopNavBar(Options, String[], String[], String[], String[], String[], String[])}</dd>
     *   <dd>{@link #rBottomNavBar(Options, String[], String[], String[], String[], String[], String[])}</dd>
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
     *           <table class="overviewSummary" border="0" cellpadding="3" cellspacing="0" summary="Field Summary tab
     *le, listing fields, and an explanation">
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
     * @param windowTitle     The window title (will be augmented with the optional {@link Options#windowTitle})
     * @param options         Container for the various command line options
     * @param stylesheetLinks The (optional) external stylesheet for this page
     * @param nav1            See above
     * @param nav2            See above
     * @param nav3            See above
     * @param nav4            See above
     * @param nav5            See above
     * @param nav6            See above
     */
    public void
    rRightFrameHtml(
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

        this.rHtml(windowTitle, options, stylesheetLinks, () -> {

            String wt = options.windowTitle == null ? "" : " (" + options.windowTitle + ")";
            this.l(
"    <script type=\"text/javascript\"><!--",
"if (location.href.indexOf('is-external=true') == -1) {",
"  parent.document.title=\"" + windowTitle + wt + "\";",
"}",
"    //-->",
"    </script>",
"    <noscript>",
"      <div>JavaScript is disabled on your browser.</div>",
"    </noscript>"
            );

            this.rTopNavBar(options, nav1, nav2, nav3, nav4, nav5, nav6);

            renderBody.run();

            this.rBottomNavBar(options, nav1, nav2, nav3, nav4, nav5, nav6);
        });
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
"    <!-- ========= START OF TOP NAVBAR ======= -->"
        );

        this.rNavBar("top", options.header, nav1, nav2, nav3, nav4, nav5, nav6);

        this.l(
"    <!-- ========= END OF TOP NAVBAR ========= -->"
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
"    <!-- ======= START OF BOTTOM NAVBAR ====== -->"
        );

        this.rNavBar("bottom", options.footer, nav1, nav2, nav3, nav4, nav5, nav6);

        this.l(
"    <!-- ======== END OF BOTTOM NAVBAR ======= -->"
        );

        // "-bottom" command line option.
        if (options.bottom != null) {
            this.l(
"    <p class=\"legalCopy\"><small>" + options.bottom + "</small></p>"
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
     *   {@code nav2} is an array of HTML fragments. {@code null} entries are ignored.
     * </p>
     * <p>
     *   Example: The "Package" function is typically {@link #DISABLED} on the "overview packge", highlit on a "package
     *   page", and, on a "class page", displayed with a link to the class's package page.
     * </p>
     *
     * @param kind {@code "top"} or {@code "bottom"}
     * @param nav1 Typically {@code [ "Overview", x, "Package", x, "Class", x, "Tree", x, "Deprecated", x, "Index", x,
     *             "Help", x ]}, or {@code null} to suppress navigation bar 1
     * @param nav2 Typically {@code [ "<a href=\"...\">Prev Class</a>", ... ]}, or {@code null} to suppress navigation
     *             bar 2
     * @param nav3 Typically {@code [ "Frames", x, "No Frames", x ]}, or {@code null} to suppress navigation bar 3
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
"    <div class=\"" + kind + "Nav\">",
"      <a name=\"navbar_" + kind + "\">",
"        <!--   -->",
"      </a>",
"      <a href=\"#skip-navbar_" + kind + "\" title=\"Skip navigation links\"></a>",
"      <a name=\"navbar_" + kind + "_firstrow\">",
"        <!--   -->",
"      </a>",
"      <ul class=\"navList\" title=\"Navigation\">"
            );

            for (int i = 0; i < nav1.length;) {
                String labelHtml = nav1[i++];
                String link      = nav1[i++];

                if (labelHtml == null) continue;

                assert link != null;

                if (link == AbstractRightFrameHtml.HIGHLIT) {
                    this.l(
"        <li class=\"navBarCell1Rev\">" + labelHtml + "</li>"
                    );
                } else
                if (link == AbstractRightFrameHtml.DISABLED) {
                    this.l(
"        <li>" + labelHtml + "</li>"
                    );
                } else
                {
                    this.l(
"        <li><a href=\"" + link + "\">" + labelHtml + "</a></li>"
                    );
                }
            }

            this.l(
"      </ul>"
            );

            // "-header", resp. "-footer" command line option.
            if (headerFooter != null) {
                this.l(
"      <div class=\"aboutLanguage\"><em>" + headerFooter + "</em></div>"
                );
            }
            this.l(
"    </div>"
            );
        }

        this.l(
"    <div class=\"subNav\">"
        );

        if (nav2 != null || nav3 != null) {

            // Render "nav2".
            if (nav2 != null) {

                this.l(
"      <ul class=\"navList\">"
                );
                for (int i = 0; i < nav2.length;) {
                    String html = nav2[i++];

                    if (html == null) continue;

                    this.l(
"        <li>" + html + "</li>"
                    );
                }
                this.l(
"      </ul>"
                );
            }

            // Render "nav3".
            if (nav3 != null) {
                assert nav3.length % 2 == 0;

                this.l(
"      <ul class=\"navList\">"
                );
                for (int i = 0; i < nav3.length;) {
                    String labelHtml = nav3[i++];
                    String link      = nav3[i++];

                    this.l(
"        <li><a href=\"" + link + "\" target=\"_top\">" + labelHtml + "</a></li>"
                    );
                }

                this.l(
"      </ul>"
                );
            }
        }

        // Render "nav4".
        if (nav4 != null) {
            assert nav4.length % 2 == 0;

            this.l(
"      <ul class=\"navList\" id=\"allclasses_navbar_" + kind + "\">"
            );
            for (int i = 0; i < nav4.length;) {
                String labelHtml = nav4[i++];
                String link      = nav4[i++];

                this.l(
"        <li><a href=\"" + link + "\">" + labelHtml + "</a></li>"
                );
            }
            this.l(
"      </ul>",
"      <div>",
"        <script type=\"text/javascript\"><!--",
"allClassesLink = document.getElementById(\"allclasses_navbar_" + kind + "\");",
"if (window == top) {",
"  allClassesLink.style.display = \"block\";",
"} else {",
"  allClassesLink.style.display = \"none\";",
"}",
"//-->",
"        </script>",
"      </div>"
            );
        }

        if (nav5 != null || nav6 != null) {

            // Render "nav5".
            this.l(
"      <div>"
            );

            if (nav5 != null) {
                assert nav5.length % 2 == 0;

                this.l(
"        <ul class=\"subNavList\">",
"          <li>Summary:&nbsp;</li>"
                );
                for (int i = 0; i < nav5.length;) {
                    String labelHtml = nav5[i++];
                    String link      = nav5[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    String s;
                    if (link == AbstractRightFrameHtml.DISABLED) {
                        s = labelHtml;
                    } else {
                        s = "<a href=\"" + link + "\">" + labelHtml + "</a>";
                    }
                    if (i != nav5.length) s += "&nbsp;|&nbsp;";
                    this.l(
"          <li>" + s + "</li>"
                    );
                }
                this.l(
"        </ul>"
                );
            }

            // Render "nav6".
            if (nav6 != null) {
                assert nav6.length % 2 == 0;

                this.l(
"        <ul class=\"subNavList\">",
"          <li>Detail:&nbsp;</li>"
                );
                for (int i = 0; i < nav6.length;) {
                    String labelHtml = nav6[i++];
                    String link      = nav6[i++];

                    if (labelHtml == null) continue;
                    assert link != null;

                    String s;
                    if (link == AbstractRightFrameHtml.DISABLED) {
                        s = labelHtml;
                    } else {
                        s = "<a href=\"" + link + "\">" + labelHtml + "</a>";
                    }
                    if (i != nav6.length) s += "&nbsp;|&nbsp;";
                    this.l(
"          <li>" + s + "</li>"
                    );
                }
                this.l(
"        </ul>"
                );
            }
            this.l(
"      </div>"
            );
        }

        this.l(
"      <a name=\"skip-navbar_" + kind + "\">",
"        <!--   -->",
"      </a>",
"    </div>"
        );
    }
}
