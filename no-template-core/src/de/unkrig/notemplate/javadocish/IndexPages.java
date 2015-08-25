
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

package de.unkrig.notemplate.javadocish;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import de.unkrig.commons.lang.AssertionUtil;
import de.unkrig.commons.lang.protocol.Consumer;
import de.unkrig.commons.lang.protocol.ConsumerWhichThrows;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.commons.util.collections.IterableUtil;
import de.unkrig.commons.util.collections.IterableUtil.ElementWithContext;
import de.unkrig.notemplate.NoTemplate;
import de.unkrig.notemplate.javadocish.templates.AbstractRightFrameHtml;

/**
 * Creates index pages ("{@code ./index-pages/index-1.html}", ...), or a single index page ("{@code
 * ./index-all.html}").
 */
public
class IndexPages {

    static { AssertionUtil.enableAssertionsForThisClass(); }

    /**
     * Representation of an entry in the generated index.
     */
    public
    interface IndexEntry {

        /**
         * The key to sort by; is not displayed.
         */
        String getKey();

        /**
         * The (base-directory-relative) link to the element's description, e.g. "{@code
         * java/awt/PageAttributes.MediaType.html#field}".
         */
        String getLink();

        /**
         * E.g. "{@code Static variable in class java.awt.<a href="../java/awt/PageAttributes.MediaType.html"
         * title="class in java.awt">PageAttributes.MediaType</a>}".
         */
        String getExplanation();

        /**
         * E.g. "{@code The MediaType instance for Engineering A, 8 1/2 x 11 in.}".
         */
        String getShortDescription();
    }

    /**
     *
     * @param key            See {@link IndexEntry#getKey()}
     * @param link      See {@link IndexEntry#getHtmlTerm1()}
     * @param explanation      See {@link IndexEntry#getHtmlTerm2()}
     * @param shortDescription See {@link IndexEntry#getHtmlDefinition()}
     * @see IndexEntry
     */
    public static IndexEntry
    indexEntry(String key, String link, String explanation, String shortDescription) {

        return new IndexEntry() {
            @Override public String getKey()              { return key;              }
            @Override public String getLink()             { return link;             }
            @Override public String getExplanation()      { return explanation;      }
            @Override public String getShortDescription() { return shortDescription; }
        };
    }

    public static void
    createSingleIndex(
        File                   outputFile,
        Collection<IndexEntry> indexEntries,
        Options                options,
        @Nullable String[]     nav1
    ) throws IOException {

        // Group all entries by their initial.
        SortedMap<Character, Collection<IndexEntry>>
        entriesByInitial = new TreeMap<Character, Collection<IndexEntry>>();

        for (IndexEntry entry : indexEntries) {

            char initial = Character.toUpperCase(entry.getKey().charAt(0));

            Collection<IndexEntry> entriesOfInitial = entriesByInitial.get(initial);
            if (entriesOfInitial == null) {
                entriesOfInitial = new ArrayList<IndexEntry>();
                entriesByInitial.put(initial, entriesOfInitial);
            }

            entriesOfInitial.add(entry);
        }

        // Create the index file.
        IndexPages.createIndexFile(
            outputFile,                        // outputFile
            "Index",                           // windowTitle
            options,                           // options
            new String[] { "stylesheet.css" }, // stylesheetLinks
            nav1,                              // nav1
            new String[] {                     // nav2
                "Prev Letter", null,
                "Next Letter", null,
            },
            noTemplate -> {                    // indexNavigation
                for (Character initial : entriesByInitial.keySet()) {
                    noTemplate.l(
"<a href=\"#" + (int) initial + "\">" + initial + "</a>"
                    );
                }
            },
            entriesByInitial                   // entriesByInitial
        );
    }

    public static void
    createSplitIndex(
        File                   baseDirectory,
        Collection<IndexEntry> indexEntries,
        Options                options,
        @Nullable String[]     nav1
    ) throws IOException {

        // Group all entries by their initial.
        SortedMap<Character, Collection<IndexEntry>>
        entriesByInitial = new TreeMap<Character, Collection<IndexEntry>>();

        for (IndexEntry entry : indexEntries) {

            char initial = Character.toUpperCase(entry.getKey().charAt(0));

            Collection<IndexEntry> entriesOfInitial = entriesByInitial.get(initial);
            if (entriesOfInitial == null) {
                entriesOfInitial = new ArrayList<IndexEntry>();
                entriesByInitial.put(initial, entriesOfInitial);
            }

            entriesOfInitial.add(entry);
        }

        // Create a file for each initial.
        int idx = 1;
        for (
            ElementWithContext<Entry<Character, Collection<IndexEntry>>> e
            :
            IterableUtil.iterableWithContext(entriesByInitial.entrySet())
        ) {
            Entry<Character, Collection<IndexEntry>> previousIndexPage = e.previous();
            Entry<Character, Collection<IndexEntry>> indexEntry        = e.current();
            Entry<Character, Collection<IndexEntry>> nextIndexPage     = e.next();

            Character              initial          = indexEntry.getKey();
            Collection<IndexEntry> entriesOfInitial = indexEntry.getValue();

            IndexPages.createIndexFile(
                new File(baseDirectory, "index-" + idx + ".html"),  // outputFile
                initial + "-Index",                                 // windowTitle
                options,                                            // options
                new String[] { "stylesheet.css" },                  // stylesheetLinks
                nav1,                                               // nav1
                new String[] {                                      // nav2
                    "Prev Letter", previousIndexPage == null ? null : "index-" + (idx - 1) + ".html",
                    "Next Letter", nextIndexPage     == null ? null : "index-" + (idx + 1) + ".html",
                },
                noTemplate -> {                                     // indexNavigation
                    int idx2 = 1;
                    for (Character initial2 : entriesByInitial.keySet()) {
                        noTemplate.l(
"<a href=\"index-" + idx2 + ".html\">" + initial2 + "</a>"
                        );
                    }
                },
                Collections.singletonMap(initial, entriesOfInitial) // entriesByInitial
            );

            idx++;
        }
    }

    /**
     * @param nav2 Typically <code>{ "Prev Letter", "Next Letter" }</code>
     */
    private static void
    createIndexFile(
        File                                   outputFile,
        String                                 windowTitle,
        Options                                options,
        String[]                               stylesheetLinks,
        @Nullable String[]                     nav1,
        @Nullable String[]                     nav2,
        Consumer<NoTemplate>                   indexNavigation,
        Map<Character, Collection<IndexEntry>> entriesByInitial
    ) throws IOException {

        String windowTitle2 = (
            options.windowTitle == null
            ? windowTitle
            : windowTitle +" (" + options.windowTitle + ")"
        );

        NoTemplate.render(
            AbstractRightFrameHtml.class,
            outputFile,
            new ConsumerWhichThrows<AbstractRightFrameHtml, RuntimeException>() {

                @Override public void
                consume(AbstractRightFrameHtml arfh) {

                    arfh.rRightFrameHtml(
                        windowTitle2,    // windowTitle
                        options,         // options
                        stylesheetLinks, // stylesheetLinks
                        nav1,            // nav1
                        nav2,            // nav2
                        new String[] {   // nav3
                            "Frames",    "index.html?" + outputFile.getName(),
                            "No Frames", outputFile.getName(),
                        },
                        new String[] {   // nav4
                            "All Classes", "allclasses-noframe.html",
                        },
                        null,            // nav5
                        null,            // nav6
                        () -> {

                            // Top index navigation.
                            indexNavigation.consume(arfh);

                            for (Entry<Character, Collection<IndexEntry>> e : entriesByInitial.entrySet()) {
                                Character              initial          = e.getKey();
                                Collection<IndexEntry> entriesOfInitial = e.getValue();

                                arfh.l(
"<a name=\"" + (int) initial + "\" />",
"<h2 class=\"title\">" + initial + "</h2>",
"<dl>"
                                );

                                for (IndexEntry entry : entriesOfInitial) {
                                    arfh.l(
"  <dt><span class=\"strong\"><a href=\"" + entry.getLink() + "\">" + entry.getKey() + "</a></span> - " + entry.getExplanation() + "</dt>",
"  <dd><div class=\"block\">" + entry.getShortDescription() + "</div></dd>"
                                    );
                                }

                            arfh.l(
"</dl>"
                                );
                            }

                            // Bottom index navigation.
                            indexNavigation.consume(arfh);
                        }
                    );
                }
            }
        );
    }
}
