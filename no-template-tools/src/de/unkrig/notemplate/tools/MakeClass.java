
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

package de.unkrig.notemplate.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import de.unkrig.commons.io.IoUtil;
import de.unkrig.commons.lang.protocol.ConsumerWhichThrows;
import de.unkrig.commons.nullanalysis.Nullable;
import de.unkrig.notemplate.NoTemplate;

/**
 * Generates source code for Java classes (which extend {@link NoTemplate}) from text files.
 * @author Arno
 *
 */
public abstract
class MakeClass {

    /**
     * <h2>Usage:</h2>
     * <dl>
     *   <dt>{@code MakeClass -help}</dt>
     *   <dd>
     *     Print this text.
     *   </dd>
     *   <dt>{@code MakeClass} [ <var>option</var> ] ... <var>text-file</var> ...</dt>
     *   <dd>
     *     Generates one Java compilation unit (".java" file) for each <var>text-file</var>.
     *   </dd>
     * </dl>
     *
     * <h2>Options:</h2>
     *
     * <dl>
     *   <dt>{@code -d} <var>dir</var></dt>
     *   <dd>
     *     Where to create the compilation units (".java" files). Defaults to "{@code .}".
     *   </dd>
     *   <dt>{@code -package} <var>package-name</var></dt>
     *   <dd>
     *     The Java package to create the class in. The default is to create the classes in the root package.
     *   </dd>
     * </dl>
     */
    public static void
    main(String[] args) throws IOException {

        int idx = 0;

        File   destination = new File(".");
        String packagE     = null;
        for (; idx < args.length;) {

            String arg = args[idx];
            if (!arg.startsWith("-")) break;

            idx++;

            if ("-help".equals(arg)) {
                IoUtil.copyResource(
                    MakeClass.class.getClassLoader(),                                     // classLoader
                    MakeClass.class.getName().replace('.',  '/') + ".main(String[]).txt", // resourceName
                    System.out,                                                           // outputStream
                    false                                                                 // closeOutputStream
                );
                return;
            } else
            if ("-d".equals(arg)) {
                destination = new File(args[idx++]);
            } else
            if ("-package".equals(arg)) {
                packagE = args[idx++];
            } else
            {
                System.err.println("Unrecognized command line option \"" + arg + "\"; try \"-help\".");
                System.exit(1);
            }
        }

        for (; idx < args.length; idx++) {

            File textFile = new File(args[idx]);

            String className;
            {
                StringBuilder sb = new StringBuilder();
                for (StringTokenizer st = new StringTokenizer(textFile.getName(), "-."); st.hasMoreTokens();) {
                    String t = st.nextToken();
                    sb.append(Character.toUpperCase(t.charAt(0)));
                    sb.append(t.substring(1).toLowerCase());
                }
                className = sb.toString();
            }

            MakeClass.generate(
                textFile,       // textFile
                new File(       // sourceFile
                    destination,
                    (packagE == null ? className : packagE.replace('.', '/') + '/' + className) + ".java"
                ),
                packagE,        // packagE
                className       // className
            );
        }
    }

    private static void
    generate(File textFile, File sourceFile, @Nullable final String packagE, final String className)
    throws IOException {

        final BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(textFile), Charset.forName("UTF-8"))
        );
        try {

            IoUtil.outputFilePrintWriter(
                sourceFile,
                Charset.forName("UTF-8"),
                new ConsumerWhichThrows<PrintWriter, IOException>() {

                    @Override public void
                    consume(PrintWriter pw) throws IOException {
                        pw.println();
                        if (packagE != null) {
                            pw.println("package " + packagE + ";");
                            pw.println();
                        }
                        pw.println("import de.unkrig.notemplate.NoTemplate;");
                        pw.println();
                        pw.println("public");
                        pw.println("class " + className + " extends NoTemplate {");
                        pw.println();
                        pw.println("    public void");
                        pw.println("    render() {");
                        pw.println("        this.l(");
                        for (;;) {
                            String line = br.readLine();
                            if (line == null) break;

                            line = line.replaceAll("\\\\", "\\\\\\\\");
                            line = line.replaceAll("\"", "\\\\\"");

                            pw.print("            \"");
                            pw.print(line);
                            pw.println("\",");
                        }
                        pw.println("        );");
                        pw.println("    }");
                        pw.println("}");
                    }
                }
            );

            br.close();
        } finally {
            try { br.close(); } catch (Exception e) {}
        }
    }
}
