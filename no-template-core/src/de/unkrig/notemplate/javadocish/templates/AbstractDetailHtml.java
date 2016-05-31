
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unkrig.commons.lang.protocol.Producer;
import de.unkrig.commons.lang.protocol.ProducerUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.NoTemplate;
import de.unkrig.notemplate.javadocish.Options;

/**
 * Base class for all "detail" pages, i.e. those that are very similar to a JAVADOC class detail page.
 */
public
class AbstractDetailHtml extends AbstractRightFrameHtml {

    /**
     * A "section" is a piece of documentation that appears twice on the JAVADOC detail page: First in the top half of
     * the page as a "summary", then, in the bottom half of the page, as a "detail".
     * <p>
     *   For example, the sections of a "class" detail page are "Nested", "Field", "Constr" and "Method".
     * </p>
     * <p>
     *   Each section contains a list of "items". E.g. the "Method" section contains one item for each documented
     *   method.
     * </p>
     */
    public static
    class Section {

        /** The anchor that links to the section summary and the section detail, e.g. {@code "constructor"}. */
        public String anchor;

        /** E.g. "Constr". */
        public String navigationLinkLabel;

        /** E.g. "Constructor Summary". */
        public String summaryTitle1;

        /** E.g. "Constructors". */
        public String summaryTitle2;

        /** E.g. <code>{ "Modifier and Type", "Method and Description" }</code>. */
        public String[] summaryTableHeadings;

        /** E.g. {@code "Constructor Detail"}. */
        public String detailTitle;

        /** E.g. {@code "Default values appear <u>underlined</u>"}. */
        public String detailDescription;

        /** E.g. the enum constants. */
        public final List<SectionItem> items = new ArrayList<SectionItem>();

        /** E.g. "Methods inherited from class java.lang.Enum..." */
        public final List<SectionAddendum> addenda = new ArrayList<SectionAddendum>();
    }

    /**
     * Representation of an item in a {@link Section}. E.g. for the JAVADOC class detail page, the items of the "Field"
     * section are the individual fields of the class.
     */
    public static
    class SectionItem {

        /** The anchor that links from the item summary to the item detail. */
        public String anchor;

        /** The contents of the cells of the item's row in the summary. */
        public String[] summaryTableCells;

        /**
         * The title of the item in the detail section, e.g. "MyClass" for a constructor on the "MyClass" detail page.
         */
        public String detailTitle;

        /**
         * Renders the item's detail content, e.g. the description of constructor "MyClass()" in the "Constructor
         * detail" section.
         */
        public Runnable printDetailContent;
    }

    /**
     * Representation of the optional "addendum" block(s) at the bottom of each section in the summary. E.g. for the
     * JAVADOC class detail page, the addenda for the method summary are "Methods inherited from ...".
     */
    public static
    class SectionAddendum {

        /**
         * E.g. {@code "Methods inherited from class&nbsp;java.lang.<a href=\"../../../java/lang/Enum.html\"
         * title=\"class in java.lang\">Enum</a>"}.
         */
        public String title;

        /**
         * E.g. {@code "<code><a href=\"../../../java/lang/Enum.html#clone()\">clone</a>, <a
         * href=\"../../../java/lang/Enum.html#compareTo(E)\">compareTo</a></code>"}.
         */
        public String content;

        /** E.g. {@code "methods_inherited_from_class_java.lang.Enum"}. */
        public String anchor;
    }

    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * Renders a "detail page".
     * <p>
     *   The layout is as follows:
     * </p>
     * <pre>
     *   +--------------------------+
     *   |   (top navigation bar)   |
     *   +--------------------------+
     *   | (subtitle)               |
     *   | (title)                  |
     *   | (prolog)                 |
     *   |+------------------------+|
     *   || (section 1 summary)    ||
     *   |+------------------------+|
     *   |...                       |
     *   |+------------------------+|
     *   || (section 1 detail)     ||
     *   |+------------------------+|
     *   |...                       |
     *   +--------------------------+
     *   | (bottom navigation bar)  |
     *   +--------------------------+
     * </pre>

     * @param subtitle Displays in normal font  right above the <var>title</var>, e.g. the name of the enclosing
     *                 package
     * @param title    E.g. {@code "Enum CertPathValidatorException.BasicReason"}
     * @param prolog   Would typically render the "class description
     * @param sections The sections to render (e.g. "fields", "methods", "nested types")
     */
    public void
    rDetail(
        String             windowTitle,
        Options            options,
        @Nullable String[] stylesheetLinks,
        @Nullable String[] nav1,
        @Nullable String[] nav2,
        @Nullable String[] nav3,
        @Nullable String[] nav4,
        @Nullable String   subtitle,
        String             title,
        Runnable           prolog,
        List<Section>      sections
    ) {

        List<String> nav5 = new ArrayList<String>();
        List<String> nav6 = new ArrayList<String>();
        for (Section section : sections) {

            if (!section.items.isEmpty() || !section.addenda.isEmpty()) {
                nav5.add(section.navigationLinkLabel);
                nav5.add('#' + section.anchor + "_summary");
            }
            if (!section.items.isEmpty()) {
                nav6.add(section.navigationLinkLabel);
                nav6.add('#' + section.anchor + "_detail");
            }
        }

        this.rRightFrameHtml(
            windowTitle,
            options,
            stylesheetLinks,
            nav1,
            nav2,
            nav3,
            nav4,
            nav5.toArray(AbstractDetailHtml.EMPTY_STRING_ARRAY),
            nav6.toArray(AbstractDetailHtml.EMPTY_STRING_ARRAY),
            () -> {
                this.l(
"<div class=\"header\">"
                );
                if (subtitle != null) {
                    this.l(
"  <div class=\"subTitle\">" + subtitle + "</div>"
                    );
                }
                this.l(
"  <h2 title=\"" + title + "\" class=\"title\">" + title + "</h2>",
"</div>",
"<div class=\"contentContainer\">"
                );
                prolog.run();

                // Render the section summaries.
                this.l(
"  <div class=\"summary\">",
"    <ul class=\"blockList\">",
"      <li class=\"blockList\">"
                );
                for (Section section : sections) {

                    if (section.items.isEmpty() && section.addenda.isEmpty()) continue;

                    this.l(
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">",
"            <a name=\"" + section.anchor + "_summary\">",
"              <!--   -->",
"            </a>",
"            <h3>" + section.summaryTitle1 + "</h3>"
                    );
                    if (!section.items.isEmpty()) {
                        this.l(
"            <table class=\"overviewSummary\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">",
"              <caption><span>" + section.summaryTitle2 + "</span><span class=\"tabEnd\">&nbsp;</span></caption>",
"              <tr>"
                        );
                        if (section.summaryTableHeadings != null) {
                            Once first = NoTemplate.once();
                            for (String sth : section.summaryTableHeadings) {
                                this.l(
"                <th class=\"" + (first.once() ? "colOne" : "colLast") + "\" scope=\"col\">" + sth + "</th>"
                                );
                            }
                        }
                        this.l(
"              </tr>"
                        );

                        List<SectionItem> sortedItems = section.items;
                        Collections.sort(sortedItems, new Comparator<SectionItem>() {

                            @Override public int
                            compare(SectionItem si1, SectionItem si2) {
                                return si1.summaryTableCells[0].compareTo(si2.summaryTableCells[0]);
                            }
                        });

                        final Producer<String> trClass = ProducerUtil.alternate("altColor", "rowColor");
                        for (SectionItem item : sortedItems) {

                            if (item.summaryTableCells == null) continue;

                            this.l(
"              <tr class=\"" + trClass.produce() + "\">"
                            );
                            Once first = NoTemplate.once();
                            for (String stc : item.summaryTableCells) {
                                boolean f = first.once();
                                if (f) {
                                    stc = "<a href=\"#" + item.anchor + "_detail\">" + stc + "</a>";
                                }
                                this.l(
"                <td class=\"" + (f ? "colOne" : "colLast") + "\">",
"                  " + stc,
"                </td>"
                                );
                            }
                            this.l(
"              </tr>"
                            );
                        }

                        this.l(
"            </table>"
                        );
                    }

                    if (section.addenda != null) {
                        for (SectionAddendum addendum : section.addenda) {
                            this.l(
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">"
                            );
                            if (addendum.anchor != null) {
                                this.l(
"                <a name=\"" + addendum.anchor + "\">",
"                  <!--   -->",
"                </a>"
                                );
                            }
                            this.l(
"                <h3>" + addendum.title + "</h3>",
"                " + addendum.content,
"              </li>",
"            </ul>"
                            );
                        }
                    }

                    this.l(
"          </li>",
"        </ul>"
                    );
                }
                this.l(
"      </li>",
"    </ul>",
"  </div>",
"  <div class=\"details\">",
"    <ul class=\"blockList\">",
"      <li class=\"blockList\">"
                );

                // Render the section details.
                for (Section section : sections) {

                    if (section.items.isEmpty()) continue;

                    this.l(
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">",
"            <a name=\"" + section.anchor + "_detail\">",
"              <!--   -->",
"            </a>",
"            <h3>" + section.detailTitle + "</h3>"
                    );
                    if (section.detailDescription != null) {
                        this.l(
"            <p>",
"              " + section.detailDescription,
"            </p>"
                        );
                    }
                    for (SectionItem item : section.items) {
                        this.l(
"            <a name=\"" + item.anchor + "_detail\">",
"              <!--   -->",
"            </a>",
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">",
"                <h4>" + item.detailTitle + "</h4>"
                        );
                        item.printDetailContent.run();
                        this.l(
"              </li>",
"            </ul>"
                        );
                    }

                    this.l(
"          </li>",
"        </ul>"
                    );
                }
                this.l(
"      </li>",
"    </ul>",
"  </div>",
"</div>"
                );
            }
        );
    }
}
