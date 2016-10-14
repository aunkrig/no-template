
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

        public
        Section(
            String                            anchor,
            String                            navigationLinkLabel,
            String                            summaryTitle1,
            @Nullable String                  summaryTitle2,
            @Nullable String[]                summaryTableHeadings,
            @Nullable String                  detailTitle,
            @Nullable String                  detailDescription,
            @Nullable Comparator<SectionItem> summaryItemComparator
        ) {
            this.anchor                = anchor;
            this.navigationLinkLabel   = navigationLinkLabel;
            this.summaryTitle1         = summaryTitle1;
            this.summaryTitle2         = summaryTitle2;
            this.summaryTableHeadings  = summaryTableHeadings;
            this.detailTitle           = detailTitle;
            this.detailDescription     = detailDescription;
            this.summaryItemComparator = summaryItemComparator;
        }

        /** The anchor that links to the section summary and the section detail, e.g. {@code "constructor"}. */
        public final String anchor;

        /** E.g. "Constr". */
        public final String navigationLinkLabel;

        /** E.g. "Constructor Summary". */
        public final String summaryTitle1;

        /** E.g. "Constructors". */
        @Nullable public final String summaryTitle2;

        /** E.g. <code>{ "Modifier and Type", "Method and Description" }</code>. */
        @Nullable public final String[] summaryTableHeadings;

        /**
         * E.g. {@code "Constructor Detail"}. {@code null} iff this section has a summary, but not a detail.
         */
        @Nullable public final String detailTitle;

        /** E.g. {@code "Default values appear <u>underlined</u>"}. */
        @Nullable public final String detailDescription;

        /** E.g. the enum constants. */
        public final List<SectionItem> items = new ArrayList<SectionItem>();

        /** E.g. "Methods inherited from class java.lang.Enum..." */
        public final List<SectionAddendum> addenda = new ArrayList<SectionAddendum>();

        /**
         * For sorting the items in the summary. Iff {@code null}, then the items are sorted by {@link
         * SectionItem#detailTitle}.
         */
        @Nullable public final Comparator<SectionItem> summaryItemComparator;
    }

    /**
     * Representation of an item in a {@link Section}. E.g. for the JAVADOC class detail page, the items of the "Field"
     * section are the individual fields of the class.
     */
    public static
    class SectionItem {

        public
        SectionItem(
            @Nullable String anchor,
            String[]         summaryTableCells,
            String           detailTitle,
            Runnable         printDetailContent
        ) {
            this.anchor             = anchor;
            this.summaryTableCells  = summaryTableCells;
            this.detailTitle        = detailTitle;
            this.printDetailContent = printDetailContent;
        }

        /** The anchor that links from the item summary to the item detail. */
        @Nullable public final String anchor;

        /** The contents of the cells of the item's row in the summary. */
        public final String[] summaryTableCells;

        /**
         * The title of the item in the detail section, e.g. "MyClass" for a constructor on the "MyClass" detail page.
         */
        public final String detailTitle;

        /**
         * Renders the item's detail content, e.g. the description of constructor "MyClass()" in the "Constructor
         * detail" section.
         */
        public final Runnable printDetailContent;
    }

    /**
     * Representation of the optional "addendum" block(s) at the bottom of each section in the summary. E.g. for the
     * JAVADOC class detail page, the addenda for the method summary are "Methods inherited from ...".
     */
    public static
    class SectionAddendum {

        public
        SectionAddendum(String title, String content, @Nullable String anchor) {
            this.title   = title;
            this.content = content;
            this.anchor  = anchor;
        }
        /**
         * E.g. {@code "Methods inherited from class&nbsp;java.lang.<a href=\"../../../java/lang/Enum.html\"
         * title=\"class in java.lang\">Enum</a>"}.
         */
        public final String title;

        /**
         * E.g. {@code "<code><a href=\"../../../java/lang/Enum.html#clone()\">clone</a>, <a
         * href=\"../../../java/lang/Enum.html#compareTo(E)\">compareTo</a></code>"}.
         */
        public final String content;

        /** E.g. {@code "methods_inherited_from_class_java.lang.Enum"}. */
        @Nullable public final String anchor;
    }

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
     *   | (heading)                |
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
     * @param heading  E.g. {@code "Enum CertPathValidatorException.BasicReason"}
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
        String             heading,
        String             headingTitle,
        Runnable           prolog,
        List<Section>      sections
    ) {

        List<String> nav5 = new ArrayList<String>();
        List<String> nav6 = new ArrayList<String>();
        for (Section section : sections) {

            nav5.add(section.navigationLinkLabel);
            nav5.add(
                section.items.isEmpty() && section.addenda.isEmpty()
                ? AbstractRightFrameHtml.DISABLED
                : '#' + section.anchor + "_summary"
            );

            if (section.detailTitle != null) {
                nav6.add(section.navigationLinkLabel);
                nav6.add(section.items.isEmpty() ? AbstractRightFrameHtml.DISABLED :  '#' + section.anchor + "_detail");
            }
        }

        this.rRightFrameHtml(
            windowTitle,                           // windowTitle
            options,                               // options
            stylesheetLinks,                       // stylesheetLinks
            nav1,                                  // nav1
            nav2,                                  // nav2
            nav3,                                  // nav3
            nav4,                                  // nav4
            nav5.toArray(new String[nav5.size()]), // nav5
            nav6.toArray(new String[nav6.size()]), // nav6
            () -> {                                // renderBody
                this.l(
"    <div class=\"header\">"
                );
                if (subtitle != null) {
                    this.l(
"      <div class=\"subTitle\">" + subtitle + "</div>"
                    );
                }
                this.l(
"      <h2 title=\"" + headingTitle + "\" class=\"title\">" + heading + "</h2>",
"    </div>",
"    <div class=\"contentContainer\">"
                );
                prolog.run();

                // Render the section summaries.
                this.l(
"      <div class=\"summary\">",
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">"
                );
                for (Section section : sections) {

                    if (section.items.isEmpty() && section.addenda.isEmpty()) continue;

                    this.l(
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">",
"                <a name=\"" + section.anchor + "_summary\">",
"                  <!--   -->",
"                </a>",
"                <h3>" + section.summaryTitle1 + "</h3>"
                    );
                    if (!section.items.isEmpty()) {
                        this.l(
"                <table class=\"overviewSummary\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">",
"                  <caption><span>" + section.summaryTitle2 + "</span><span class=\"tabEnd\">&nbsp;</span></caption>",
"                  <tr>"
                        );
                        String[] sths = section.summaryTableHeadings;
                        if (sths != null) {
                            Once first = NoTemplate.once();
                            for (String sth : sths) {
                                this.l(
"                    <th class=\"" + (first.once() ? "colOne" : "colLast") + "\" scope=\"col\">" + sth + "</th>"
                                );
                            }
                        }
                        this.l(
"                  </tr>"
                        );

                        List<SectionItem> sortedItems = new ArrayList<>(section.items);
                        Collections.sort(
                            sortedItems,
                            (
                                section.summaryItemComparator != null
                                ? section.summaryItemComparator
                                : new Comparator<SectionItem>() {

                                    @Override public int
                                    compare(@Nullable SectionItem si1, @Nullable SectionItem si2) {
                                        assert si1 != null;
                                        assert si2 != null;
                                        return si1.detailTitle.compareTo(si2.detailTitle);
                                    }
                                }
                            )
                        );

                        final Producer<String> trClass = ProducerUtil.alternate("altColor", "rowColor");
                        for (SectionItem item : sortedItems) {

                            if (item.summaryTableCells == null) continue;

                            this.l(
"                  <tr class=\"" + trClass.produce() + "\">"
                            );
                            Once first = NoTemplate.once();
                            for (String stc : item.summaryTableCells) {
                                boolean f = first.once();
                                if (f) {
                                    stc = "<a href=\"#" + item.anchor + "_detail\">" + stc + "</a>";
                                }
                                this.l(
"                    <td class=\"" + (f ? "colOne" : "colLast") + "\">",
"                      " + stc,
"                    </td>"
                                );
                            }
                            this.l(
"                  </tr>"
                            );
                        }

                        this.l(
"                </table>"
                        );
                    }

                    if (section.addenda != null) {
                        for (SectionAddendum addendum : section.addenda) {
                            this.l(
"                <ul class=\"blockList\">",
"                  <li class=\"blockList\">"
                            );
                            if (addendum.anchor != null) {
                                this.l(
"                    <a name=\"" + addendum.anchor + "\">",
"                      <!--   -->",
"                    </a>"
                                );
                            }
                            this.l(
"                    <h3>" + addendum.title + "</h3>",
"                    " + addendum.content,
"                  </li>",
"                </ul>"
                            );
                        }
                    }

                    this.l(
"              </li>",
"            </ul>"
                    );
                }
                this.l(
"          </li>",
"        </ul>",
"      </div>",
"      <div class=\"details\">",
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">"
                );

                // Render the section details.
                for (Section section : sections) {

                    if (section.detailTitle == null || section.items.isEmpty()) continue;

                    this.l(
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">",
"                <a name=\"" + section.anchor + "_detail\">",
"                  <!--   -->",
"                </a>",
"                <h3>" + section.detailTitle + "</h3>"
                    );
                    if (section.detailDescription != null) {
                        this.l(
"                <p>",
"                  " + section.detailDescription,
"                </p>"
                        );
                    }
                    for (SectionItem item : section.items) {
                        this.l(
"                <a name=\"" + item.anchor + "_detail\">",
"                  <!--   -->",
"                </a>",
"                <ul class=\"blockList\">",
"                  <li class=\"blockList\">",
"                    <h4>" + item.detailTitle + "</h4>"
                        );
                        item.printDetailContent.run();
                        this.l(
"                  </li>",
"                </ul>"
                        );
                    }

                    this.l(
"              </li>",
"            </ul>"
                    );
                }
                this.l(
"          </li>",
"        </ul>",
"      </div>",
"    </div>"
                );
            }
        );
    }
}
