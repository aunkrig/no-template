
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

import java.util.ArrayList;
import java.util.List;

import de.unkrig.commons.lang.protocol.Producer;
import de.unkrig.commons.lang.protocol.ProducerUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.javadocish.Options;

/**
 * Base class for rendering HTML documents similar to JAVADOC's "summary" pages.
 * <p>
 *   Examples:
 * </p>
 * <ul>
 *   <li><a href="http://docs.oracle.com/javase/7/docs/api/overview-summary.html">overview-summary.html</a></li>
 *   <li>
 *     <a href="http://docs.oracle.com/javase/7/docs/api/java/awt/dnd/package-summary.html">package-summary.html</a>
 *   </li>
 *   <li><a href="docs.oracle.com/javase/7/docs/api/deprecated-list.html">deprecated-list.html</a></li>
 * </ul>
 */
public
class AbstractSummaryHtml extends AbstractRightFrameHtml {

    /**
     * Representation of a section on the "summary page". E.g. on the JAVADOC package summary page, the sections are
     * "Interface Summary", "Class Summary", "Enum Summary", "Exception Summary", "Error Summary", "Annotation Type
     * Summary".
     */
    public static
    class Section {

        @Nullable public final String  anchor;
        public final String            title;
        @Nullable public final String  summary;
        public final String            firstColumnHeading;
        public final List<SectionItem> items = new ArrayList<>();

        public
        Section(@Nullable String anchor, String title, @Nullable String summary, String firstColumnHeading) {
            this.anchor             = anchor;
            this.title              = title;
            this.summary            = summary;
            this.firstColumnHeading = firstColumnHeading;
        }
    }

    /**
     * Representation of an item in a {@link Section}. E.g. on the JAVADOC package summary page, the items of the
     * "Interface Summary" section are the individual interfaces declared in the package.
     */
    public static
    class SectionItem {

        public final String           link;
        @Nullable public final String name;
        public final String           summary;

        public
        SectionItem(String link, @Nullable String name, String summary) {
            this.link    = link;
            this.name    = name;
            this.summary = summary;

        }
    }

    /**
     * @param prolog Renders the content of the {@code <div class="header">} section
     * @param epilog Renders the "description" at the bottom of the page
     *
     * @see AbstractRightFrameHtml#rRightFrameHtml(String, Options, String[], String[], String[], String[], String[],
     *      String[], String[], Runnable)
     */
    protected void
    rSummary(
        String             windowTitle,
        Options            options,
        @Nullable String[] stylesheetLinks,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        Runnable[]         headers,
        @Nullable Runnable epilog,
        List<Section>      sections
    ) {

        super.rRightFrameHtml(
            windowTitle,     // windowTitle
            options,         // options
            stylesheetLinks, // stylesheetLinks
            nav1,            // nav1
            nav2,            // nav2
            nav3,            // nav3
            nav4,            // nav4
            null,            // nav5
            null,            // nav6
            () -> {          // renderBody

                for (Runnable header : headers) {
                    if (header == null) continue;

                    this.l(
"    <div class=\"header\">"
                    );
                    header.run();
                    this.l(
"    </div>"
                    );
                }

                this.l(
"    <div class=\"contentContainer\">",
"      <ul class=\"blockList\">"
                );
                for (Section section : sections) {

                    if (section.items.isEmpty()) continue;

                    if (section.anchor != null) {
                        this.l(
"        <a name=\"" + section.anchor + "\">",
"          <!--   -->",
"        </a>"
                        );
                    }
                    this.l(
"        <li class=\"blockList\">",
"          <table class=\"overviewSummary\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\" summary=\"" + section.summary + "\">",
"            <caption><span>" + section.title + "</span><span class=\"tabEnd\">&nbsp;</span></caption>",
"            <tr>",
"              <th class=\"colFirst\" scope=\"col\">" + section.firstColumnHeading + "</th>",
"              <th class=\"colLast\" scope=\"col\">Description</th>",
"            </tr>",
"            <tbody>"
                    );
                    Producer<String> trClass = ProducerUtil.alternate("altColor", "rowColor");
                    for (SectionItem item : section.items) {
                        this.l(
"              <tr class=\"" + trClass.produce() + "\">",
"                <td class=\"colFirst\"><a href=\"" + item.link + "\">" + item.name + "</a></td>"
                        );
                        if (item.summary.isEmpty()) {
                            this.l(
"                <td class=\"colLast\">&nbsp;</td>"
                            );
                        } else {
                            this.l(
"                <td class=\"colLast\">",
"                  <div class=\"block\">" + item.summary + "</div>",
"                </td>"
                            );
                        }
                        this.l(
"              </tr>"
                        );
                    }
                    this.l(
"            </tbody>",
"          </table>",
"        </li>"
                    );
                }
                this.l(
"      </ul>"
                );

                if (epilog != null) epilog.run();

                this.l(
"    </div>"
                );
            }
        );
    }
}
