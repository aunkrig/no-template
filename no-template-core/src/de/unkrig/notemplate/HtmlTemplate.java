
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

package de.unkrig.notemplate;

import de.unkrig.commons.lang.AssertionUtil;
import de.unkrig.notemplate.util.Entities;

/**
 * Adds HTML-specific functionality to {@link NoTemplate}.
 */
public abstract
class HtmlTemplate extends NoTemplate {

    private boolean replaceNonAsciiCharactersWithHtmlCharacterReferences = true;

    static { AssertionUtil.enableAssertionsForThisClass(); }

    /**
     * Configures whether all non-ASCII characters (code point 128 and above) that will be written should be silently
     * converted into "HTML character entity references", or, if an HTML character entity reference does not exist for
     * the code point, into a numeric character reference.
     * <p>
     *   Examples:
     * </dl>
     * <table>
     *   <tr><td>"{@code \}{@code u00E4}" or "{@code \u00E4}":</td><td>&rarr;</td><td>"{@code &auml;}"</td></tr>
     *   <tr><td>"{@code \}{@code u1234}"</td><td>&rarr;</td><td>"{@code &#4660;}"</td></tr>
     * </table>
     * <p>
     *   Notice that the five HTML special characters "{@code "&*<>}" are <i>not</i> converted into character entities;
     *   to do that, use {@link #esc(String)}.
     * </p>
     * <p>
     *   By default, this conversion is <i>on</i>.
     * </p>
     */
    public void
    setReplaceNonAsciiCharactersWithHtmlCharacterReferences(boolean value) {
        this.replaceNonAsciiCharactersWithHtmlCharacterReferences = value;
    }

    @Override public void
    l(String line) {

        super.l(
            this.replaceNonAsciiCharactersWithHtmlCharacterReferences
            ? Entities.replaceNonAsciiCharactersWithHtmlCharacterReferences(line)
            : line
        );
    }


    @Override public void
    l(String... lines) {

        if (this.replaceNonAsciiCharactersWithHtmlCharacterReferences) {

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String s    = Entities.replaceNonAsciiCharactersWithHtmlCharacterReferences(line);
                if (!s.equals(line)) {
                    String[] tmp = new String[lines.length];
                    System.arraycopy(lines, 0, tmp, 0, i);
                    tmp[i++] = s;
                    for (; i < lines.length; i++) {
                        tmp[i] = Entities.replaceNonAsciiCharactersWithHtmlCharacterReferences(lines[i]);
                    }
                    lines = tmp;
                    break;
                }
            }
        }

        super.l(lines);
    }

    @Override public void
    p(String text) {

        super.p(
            this.replaceNonAsciiCharactersWithHtmlCharacterReferences
            ? Entities.replaceNonAsciiCharactersWithHtmlCharacterReferences(text)
            : text
        );
    }

    /**
     * Replaces the five HTML special characters with "character entities".
     * <p>
     *   Notice that non-ASCII characters (like "{@code \u00E4}") need not (and should not) be printed as character
     *   entities by the template, because they are automatically converted (see {@link
     *   #setReplaceNonAsciiCharactersWithHtmlCharacterReferences(boolean)}).
     * </p>
     * <table border="1">
     *   <caption>Predefined entities in XML</caption>
     *   <tr><th>Name</th><th>Character</th><th>Unicode code point (decimal)</th><th>Description</th></tr>
     *   <tr><td>quot</td><td>&quot;</td><td>U+0022 (34)</td><td>double quotation mark</td></tr>
     *   <tr><td>amp</td><td>&amp;</td><td>U+0026 (38)</td><td>ampersand</td></tr>
     *   <tr><td>apos</td><td>&apos;</td><td>U+0027 (39)</td><td>apostrophe (apostrophe-quote)</td></tr>
     *   <tr><td>lt</td><td>&lt;</td><td>U+003C (60)</td><td>less-than sign</td></tr>
     *   <tr><td>gt</td><td>&gt;</td><td>U+003E (62)</td><td>greater-than sign</td></tr>
     * </table>
     *
     * @see #setReplaceNonAsciiCharactersWithHtmlCharacterReferences(boolean)
     */
    public static String
    esc(String rawText) { return Entities.replaceXmlSpecialCharactersWithPredefinedEntities(rawText); }
}
