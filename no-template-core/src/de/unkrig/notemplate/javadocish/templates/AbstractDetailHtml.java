
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
import java.util.List;

import de.unkrig.commons.lang.protocol.Producer;
import de.unkrig.commons.lang.protocol.ProducerUtil;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.javadocish.Options;


public
class AbstractDetailHtml extends AbstractRightFrameHtml {
    /** One. Two. Three. */
    @Deprecated public static final int CONST = 8;

    public static
    class Section {

        /** E.g. {@code "Enum Constant"}. */
        public String nameSingular;

        /** E.g. {@code "Enum Constants"}. */
        public String namePlural;

        /** E.g. {@code "enum_constant"}. */
        public String anchor;

        /** E.g. {@code "Enum Constant Summary table, listing enum constants, and an explanation"}. */
        public String summary;

        /** E.g. {@code "Enum Constant and Description"}. */
        public String summaryLeftColumnTitle;

        /** E.g. the enum constants. */
        public List<SectionItem> items;

        /** E.g. "Methods inherited from class java.lang.Enum..." */
        @Nullable public List<SectionAddendum> addenda;
    }

    public static
    class SectionItem {

        public String anchor;
        public String name;
        public String shortDescription;

        /**
         * E.g.
         * <br />
         * {@code <pre>public static final&nbsp;<a href=\"../../../java/security/cert/CertPathValidatorException.BasicReason.html\" title=\"enum in java.security.cert\">CertPathValidatorException.BasicReason</a> UNSPECIFIED</pre>}
         * <br />
         * {@code <div class=\"block\">Unspecified reason.</div>}
         * <br />
         * <p>Or:</p>
         * {@code <pre>public static&nbsp;<a href=\"../../../java/security/cert/CertPathValidatorException.BasicReason.html\" title=\"enum in java.security.cert\">CertPathValidatorException.BasicReason</a>[]&nbsp;values()</pre>}
         * <br />
         * {@code <div class=\"block\">Returns an array containing the constants of this enum type, in}
         * <br />
         * {@code the order they are declared.  This method may be used to iterate}
         * <br />
         * {@code over the constants as follows:}
         * <br />
         * {@code <pre>}
         * <br />
         * {@code for (CertPathValidatorException.BasicReason c : CertPathValidatorException.BasicReason.values())}
         * <br />
         * {@code &nbsp;   System.out.println(c);}
         * <br />
         * {@code </pre></div>}
         * <br />
         * {@code <dl><dt><span class=\"strong\">Returns:</span></dt><dd>an array containing the constants of this enum type, in}
         * <br />
         * {@code the order they are declared</dd></dl>}
         * <br />
         */
        public String content;
    }

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
     * @param subtitle E.g. the name of the enclosing package, e.g. {@code "java.security.cert"}
     * @param title    E.g. {@code "Enum CertPathValidatorException.BasicReason"}
     * @param prolog   Would typically render "{@code <ul class="inheritance"> ... <div class="description"> ...}"
     * @param sections
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
            nav5.add(section.namePlural);
            nav5.add('#' + section.anchor + "_summary");
            nav6.add(section.namePlural);
            nav6.add('#' + section.anchor + "_detail");
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

                this.l(
"  <div class=\"summary\">",
"    <ul class=\"blockList\">",
"      <li class=\"blockList\">"
                );
                for (Section section : sections) {
                    this.l(
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">",
"            <a name=\"" + section.anchor + "_summary\">",
"              <!--   -->",
"            </a>",
"            <h3>" + section.nameSingular + " Summary</h3>",
"            <table class=\"overviewSummary\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\" summary=\"" + section.summary + "\">",
"              <caption><span>" + section.namePlural + "</span><span class=\"tabEnd\">&nbsp;</span></caption>",
"              <tr>",
"                <th class=\"colOne\" scope=\"col\">" + section.summaryLeftColumnTitle + "</th>",
"              </tr>"
                    );

                    Producer<String> trClass = ProducerUtil.alternate("altColor", "rowColor");
                    for (SectionItem item : section.items) {
                        this.l(
"              <tr class=\"" + trClass.produce() + "\">",
"                <td class=\"colOne\"><code><strong><a href=\"#" + item.anchor + "\">" + item.name + "</a></strong></code>",
"                  <div class=\"block\">" + item.shortDescription + "</div>",
"                </td>",
"              </tr>"
                        );
                    }

                    this.l(
"            </table>"
                    );

                    if (section.addenda != null) {
                        for (SectionAddendum addendum : section.addenda) {
                            this.l(
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">",
"                <a name=\"" + addendum.anchor + "\">",
"                  <!--   -->",
"                </a>",
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
                for (Section section : sections) {
                    this.l(
"        <ul class=\"blockList\">",
"          <li class=\"blockList\">",
"            <a name=\"" + section.anchor + "_detail\">",
"              <!--   -->",
"            </a>",
"            <h3>" + section.nameSingular + " Detail</h3>"
                    );
                    for (SectionItem item : section.items) {
                        this.l(
"            <a name=\"" + item.anchor + "\">",
"              <!--   -->",
"            </a>",
"            <ul class=\"blockList\">",
"              <li class=\"blockList\">",
"                <h4>" + item.name + "</h4>"
                        );
                        this.p(item.content);
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
