
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

package de.unkrig.notemplate.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import de.unkrig.commons.lang.AssertionUtil;

/**
 * Utility functionality which deals with XML and HTML "entities".
 *
 * @see <a href="http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references">HTML and XML character
 *      entities</a>
 */
public final
class Entities {

    static { AssertionUtil.enableAssertionsForThisClass(); }

    private Entities() {} // Make the constructor of this utility class inaccessible.

    /**
     * Replaces the five XML "special characters" with the respective "predefined entities".
     * <table border="1">
     *   <caption>Predefined entities in XML</caption>
     *   <tr><th>Name</th><th>Character</th><th>Unicode code point (decimal)</th><th>Description</th></tr>
     *   <tr><td>quot</td><td>&quot;</td><td>U+0022 (34)</td><td>double quotation mark</td></tr>
     *   <tr><td>amp</td><td>&amp;</td><td>U+0026 (38)</td><td>ampersand</td></tr>
     *   <tr><td>apos</td><td>&apos;</td><td>U+0027 (39)</td><td>apostrophe (apostrophe-quote)</td></tr>
     *   <tr><td>lt</td><td>&lt;</td><td>U+003C (60)</td><td>less-than sign</td></tr>
     *   <tr><td>gt</td><td>&gt;</td><td>U+003E (62)</td><td>greater-than sign</td></tr>
     * </table>
     */
    public static String
    replaceXmlSpecialCharactersWithPredefinedEntities(String s) {
        int len = s.length();

        int idx;

        // Optimization for the special case when the string contains no characters that need to be replaced.
        NEEDS_ENCODING: {

            for (idx = 0; idx < len; idx++) {
                char c = s.charAt(idx);
                if (c == '"' || c == '&' || c == '\'' || c == '<' || c == '>') break NEEDS_ENCODING;
            }

            return s;
        }

        // At this point, "idx" is the index of the first character within "s" than needs to be escaped as an SGML
        // entity.

        StringBuilder sb = new StringBuilder(s.substring(0, idx));
        for (; idx < len; idx++) {
            char c = s.charAt(idx);
            switch (c) {
            case '"':  sb.append("&quot;"); break;
            case '&':  sb.append("&amp;");  break;
            case '\'': sb.append("&apos;"); break;
            case '<':  sb.append("&lt;");   break;
            case '>':  sb.append("&gt;");   break;
            default:   sb.append(c);        break;
            }
        }

        return sb.toString();
    }

    /**
     * Replaces all characters with unicode code point 128 and greater either with
     * <ul>
     *   <li>
     *     An "HTML character entity reference" (like "{@code &auml;}" for "&auml;"), if defined for the codepoint, or
     *   </li>
     *   <li>
     *     A "numeric character reference" (like "{@code &#12345;}" for the character for codepoint 12345).
     *   </li>
     * </ul>
     * <p>
     *   Particularly, double quotes, ampersands, single quotes, less-than signs and greater-than signs (also known as
     *   "XML special characters") are <i>not</i> replaced!
     * </p>
     *
     * @see #replaceXmlSpecialCharactersWithPredefinedEntities(String)
     */
    public static String
    replaceNonAsciiCharactersWithHtmlCharacterReferences(String s) {
        int len = s.length();

        // Optimization for the special case when the string contains no characters that need to be replaced.
        int    idx;
        NEEDS_ENCODING: {

            for (idx = 0; idx < len; idx++) {
                if (s.charAt(idx) >= 128) break NEEDS_ENCODING;
            }

            return s;
        }

        // At this point, "idx" is the index of the first character within "s" than needs to be replaced.

        StringBuilder sb = new StringBuilder(s.substring(0, idx));

        for (; idx < len; idx++) {
            char c = s.charAt(idx);

            if (c <= 127) {
                sb.append(c);
                continue;
            }

            String entity = Entities.HTML_CHARACTER_ENTITIES.get(c);
            if (entity != null) {
                sb.append('&').append(entity).append(';');
                continue;
            }

            sb.append("&#").append((int) c).append(';');
        }

        return sb.toString();
    }

    /**
     * Replaces all characters with unicode code point 128 and greater with "numeric character references" (like
     * "{@code &#12345;}" for the character for codepoint 12345).
     *
     * @see #replaceXmlSpecialCharactersWithPredefinedEntities(String)
     */
    public static String
    replaceNonAsciiCharactersWithNumericCharacterReferences(String s) {
        int len = s.length();

        // Optimization for the special case when the string contains no characters that need to be replaced.
        int idx;
        NEEDS_ENCODING: {

            for (idx = 0; idx < len; idx++) {
                if (s.charAt(idx) >= 128) break NEEDS_ENCODING;
            }

            return s;
        }

        // At this point, "idx" is the index of the first character within "s" than needs to be escaped as an SGML
        // entity.

        StringBuilder sb = new StringBuilder(s.substring(0, idx));

        for (; idx < len; idx++) {
            char c = s.charAt(idx);

            if (c <= 127) {
                sb.append(c);
            } else {
                sb.append("&#").append((int) c).append(';');
            }
        }

        return sb.toString();
    }

    private static final Map<Character, String> HTML_CHARACTER_ENTITIES;
    static {
        try {
            InputStream is = Entities.class.getResourceAsStream("html-character-entities.properties");
            assert is != null : "Resource \"html-character-entities.properties\" not found";

            Properties p = new Properties();
            p.load(is);

            is.close();

            Map<Character, String> m = new HashMap<Character, String>();
            for (Entry<Object, Object> e : p.entrySet()) {
                String key   = (String) e.getKey();
                String value = (String) e.getValue();

                assert key.length() == 1;

                m.put(key.charAt(0), value);
            }

            HTML_CHARACTER_ENTITIES = m;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
