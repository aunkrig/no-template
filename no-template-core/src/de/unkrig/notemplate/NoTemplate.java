
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;

import de.unkrig.commons.io.IoUtil;
import de.unkrig.commons.lang.AssertionUtil;
import de.unkrig.commons.lang.protocol.ConsumerWhichThrows;
import de.unkrig.commons.nullanalysis.Nullable;

/**
 * The heart of the No-Template library: Renders the templates that you define as Java&trade; classes
 * ("template classes").
 * <p>
 *   The lifecycle of a template is as follows:
 * </p>
 * <ul>
 *   <li>
 *     You create an instance of your template class by calling {@link #newTemplate(Class, Writer)} (or one of its
 *     brethren).
 *   </li>
 *   <li>
 *     You invoke the render method that you declared in your template class, passing all the dynamic data as
 *     arguments.
 *   </li>
 *   <li>
 *     The render method of your class generates output by calling {@link #l(String...)}, {@link #p(String)} (and
 *     their brethren). Optionally, it includes other templates by calling {@link #include(Class)}.
 *   </li>
 *   <li>
 *     The render method of your class returns, and document generation is complete. Voila!
 *   </li>
 * </ul>
 */
public abstract
class NoTemplate {

    static { AssertionUtil.enableAssertionsForThisClass(); }

    @Nullable private PrintWriter pw;

    /**
     * Renders the given no-template (<var>templateClass</var>) to the given <var>file</var>. Silently creates any
     * missing parent directories.
     *
     * @param <T>        The template class
     * @param outputFile The file to write to (in UTF-8 encoding)
     * @param renderer   Prints the text to its <var>subject</var> {@link PrintWriter}
     */
    public static final <T extends NoTemplate, EX extends Exception> void
    render(final Class<T> templateClass, File outputFile, final ConsumerWhichThrows<? super T, EX> renderer)
    throws IOException, EX {
        NoTemplate.render(templateClass, outputFile, renderer, true);
    }

    /**
     * Renders the given no-template (<var>templateClass</var>) to the given <var>file</var> with the "UTF-8" charset.
     *
     * @param <T>                            The template class
     * @param outputFile                     The file to write to (in UTF-8 encoding)
     * @param renderer                       Prints the text to its <var>subject</var> {@link PrintWriter}
     * @param createMissingParentDirectories Whether to create any missing parent directories for the <var>file</var>
     */
    public static final <T extends NoTemplate, EX extends Exception> void
    render(
        final Class<T>                           templateClass,
        File                                     outputFile,
        final ConsumerWhichThrows<? super T, EX> renderer,
        boolean                                  createMissingParentDirectories
    ) throws IOException, EX {
        NoTemplate.render(templateClass, outputFile, renderer, createMissingParentDirectories, Charset.forName("UTF-8"));
    }

    /**
     * Renders the given no-template (<var>templateClass</var>) to the given <var>file</var>.
     *
     * @param <T>                            The template class
     * @param outputFile                     The file to write to (in UTF-8 encoding)
     * @param renderer                       Prints the text to its <var>subject</var> {@link PrintWriter}
     * @param createMissingParentDirectories Whether to create any missing parent directories for the <var>file</var>
     * @param charset                        The charset to use
     */
    public static final <T extends NoTemplate, EX extends Exception> void
    render(
        final Class<T>                           templateClass,
        File                                     outputFile,
        final ConsumerWhichThrows<? super T, EX> renderer,
        boolean                                  createMissingParentDirectories,
        Charset                                  charset
    ) throws IOException, EX {

        System.out.println("Generating " + outputFile + "...");

        IoUtil.outputFilePrintWriter(
            outputFile,
            charset,
            new ConsumerWhichThrows<PrintWriter, EX>() {

                @Override public void
                consume(PrintWriter pw) throws EX {
                    renderer.consume(NoTemplate.newTemplate(templateClass, pw));
                }
            },
            createMissingParentDirectories
        );
    }

    /**
     * Instantiates the given template class and returns the instance. Everything that the template instance will
     * print (through {@link #l(String...)} and its brethren) will be written to the given writer.
     */
    public static final <T extends NoTemplate> T
    newTemplate(Class<T> templateClass, Writer out) {

        T template;
        try {
            template = templateClass.getConstructor().newInstance();
        } catch (IllegalArgumentException iae) {

            // Should not occur.
            throw iae;
        } catch (SecurityException se) {

            throw new IllegalArgumentException((
                "Template class \""
                + templateClass.getName()
                + "\" has no accessible zero-parameter constructor"
            ), se);
        } catch (InstantiationException ie) {

            throw new IllegalArgumentException((
                "Template class \""
                + templateClass.getName()
                + "\" is abstract"
            ), ie);
        } catch (IllegalAccessException iae) {

            throw new IllegalArgumentException((
                "Template class \""
                + templateClass.getName()
                + "\" enforces Java language access control and the underlying constructor is inaccessible"
            ), iae);
        } catch (InvocationTargetException ite) {

            throw new IllegalArgumentException((
                "The zero-parameter constructor of template class \""
                + templateClass.getName()
                + "\" threw an exception"
            ), ite);
        } catch (NoSuchMethodException nsme) {

            throw new IllegalArgumentException((
                "Template class \""
                + templateClass.getName()
                + "\" lacks the zero-parameter constructor"
            ), nsme);
        }

        if (!(out instanceof PrintWriter)) out = new PrintWriter(out, true);

        try {
            NoTemplate.class.getDeclaredField("pw").set(template, out);
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        return template;
    }

    /**
     * Instantiates the given template class and returns the instance. Everything that the template instance will
     * print (through {@link #l(String...)} and its brethren) will be written to the given output stream, encoded
     * according to the "platform default encoding".
     */
    public static final <T extends NoTemplate> T
    newTemplate(Class<T> templateClass, OutputStream os) {

        return NoTemplate.newTemplate(templateClass, new OutputStreamWriter(os));
    }

    /**
     * Instantiates the given template class and returns the instance. Everything that the template instance will
     * print (through {@link #l(String...)} and its brethren) will be written to the given output stream, encoded
     * according to the given charset.
     */
    public static final <T extends NoTemplate> T
    newTemplate(Class<T> templateClass, OutputStream os, Charset cs) {

        return NoTemplate.newTemplate(templateClass, new OutputStreamWriter(os, cs));
    }

    /**
     * Instantiates the given template class and returns the template instance. Everything that the template
     * instance will print, will be written to the same destination as for <i>this</i> template instance.
     */
    public <C extends NoTemplate> C
    include(Class<C> templateClass) {
        assert this.pw != null;
        return NoTemplate.newTemplate(templateClass, this.pw);
    }

    /**
     * @return The input string, but SGML-escaped
     */
    public static String
    html(String s) {
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        s = s.replace("\"", "&quot;");
        s = s.replace("'", "&apos;");
        return s;
    }

    /**
     * Terminates the current line by writing the line separator string.
     */
    public void
    l() {
        assert this.pw != null;
        this.pw.println();
    }

    /**
     * Writes a String and then terminates the line. Invoking this method is equivalent with
     * <p>
     *   Invoking this method is equivalent with
     * </p>
     * <pre>
     *     {@link #p(String) this.p(<var>text</var>)};
     *     {@link #l() this.l()};
     * </pre>
     */
    public void
    l(String line) {
        assert this.pw != null;
        this.pw.println(line);
    }

    /**
     * Writes the given <var>lines</var>, and a line separator after each line.
     * <p>
     *   Invoking this method is equivalent with:
     * </p>
     * <pre>
     *   for (String line : <var>lines</var>) {
     *       {@link #p(String) this.p(<var>line</var>)};
     *       {@link #l() this.l()};
     *   }
     * </pre>
     */
    public void
    l(String... lines) {

        assert this.pw != null;

        for (String line : lines) {
            this.pw.println(line);
        }
    }

    /**
     * Writes a string (but not a following line separator).
     */
    public void
    p(String text) {

        assert this.pw != null;
        this.pw.print(text);
    }

    /**
     * @see NoTemplate#once()
     */
    public
    interface Once {

        /**
         * @see NoTemplate#once()
         */
        boolean once();
    }

    /**
     * @return An object which produces the value {@code true} exactly once, and then always {@code false}
     */
    public static Once
    once() {
        return new Once() {

            boolean done;

            @Override public boolean
            once() {
                if (!this.done) {
                    this.done = true;
                    return true;
                }
                return false;
            }
        };
    }
}
