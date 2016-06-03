# no-template
A super-small Java library for templating, i.e. generating text files (HTML, XML, whatever) from a "template" text file and dynamic data.

  <p>
    It is based on the concept that the templates are bare Java&trade; classes.
  </p>

  <h2>Motivation</h2>

  <p>
    Other templating engines like <a href="http://freemarker.org">Freemarker</a> or Java ServerPages (a.k.a. "JSP")
    use template files written their special markup language, optimized for the particular purpose. 
  </p>
  <p>
    This has the following problems:
  </p>
  <ul>
    <li style="padding:3px">
      Each framework has its own syntax and semantics for implementing the dynamic part of a document (variables,
      control structures, in-source documentation, ...).
    </li>
    <li style="padding:3px">
      Because the nesting of control structures and the indentation of the static content are generally not identical,
      it is very hard to write easily-readable code.
    </li>
    <li style="padding:3px">
      Most templating frameworks have no static typing, i.e. you must always be careful when using (or worse: re-using)
      variables, because there is no "compile-time checking" of types.
    </li>
    <li style="padding:3px">
      Powerful debuggers are not available in many cases, so you typcially revert to looking at log files.
    </li>
    <li style="padding:3px">
      Because there are so many different templating engines, you favorite IDE possibly offers no advanced editor with
      code completion, refactoring, and the like.
    </li>
    <li style="padding:3px">
      If the templating engine involves some kind of an "translation" step, then that must be executed on each and
      every code change - at worst manually.
    </li>
  </ul>

  <h2>Quick Start</h2>

  <p>
    The approach of no-template is as simple as it could be:
  <p>
```java
// By convention, you place all your template classes in packages named "*.templates[.*]":
package com.acme.myproject.templates;

import de.unkrig.notemplate.*;

// Your template class extends the "NoTemplate" class, either directly, as here, or indirectly to get extra
// convenience functionality useful for a particular purpose, e.g. by extending "HtmlTemplate".
//
// The name of the template class is typically chosen to reflect the "name" of the generated document (whatever
// "name" means in the specific context). For example, the name of this template class indicates that the name
// of its product is "index.html".
public
class IndexHtml extends NoTemplate {

    // Your template class has only a zero-arg constructor (or no constructor at all).
    // It (typically) declares no fields.

    // You declare a method (typically, but necessarily) named "render" with all the parameters necessary for
    // the dynamic data that it needs.
    public void
    render(String firstName, String lastName, int age) {

        // The method "NoTemplate.l(String...)" writes the given strings to the output, each followed by a line
        // break.
        //
        // By putting each string on a separate line, and aligning these lines in column one, the indentation in
        // the generated document is exactly reflected here in the source code (apart from the enclosing quotes).
        l(
"<!DOCTYPE html>",
"<html>",
"  <head>",
"    <title>My first template</title>",
"  </head>",
"  <body>",
"    This is my very first template.",
"    <br />",
"    Hello " + firstName + " " + lastName + ",", // Notice how the method parameters are used.
"    you must be " + age + " years old!",
"  </body>",
"</html>"
        );
    }
```

```java
// Examples of how to use the template class declared above:

// Render to STDOUT:
NoTemplate.newTemplate(IndexHtml.class, System.out).render("John", "Doe", 99);

// Render to a file:
NoTemplate.render(IndexHtml.class, new File("index.html"), (IndexHtml t) -> {
  t.render("John", "Doe", 99);
});
```

  <p>
    No-Template addresses most of the above-mentioned problems:
  </p>
  <ul>
    <li style="padding:3px">
      You don't need to learn yet another syntax - it's all Java! Honestly, HTML, JavaScript, CSS, ... is already
      enough to confuse me.
    </li>
    <li style="padding:3px">
      The "content" lines are quite easy to spot in the source code, since they appear in column one and start with
      a quote.
    </li>
    <li style="padding:3px">
      You benefit from the static type model of Java, and get compiler warnings and errors when other frameworks
      produce only <i>runtime</i> errors.
    </li>
    <li style="padding:3px">
      Debugging of No-Templates integrates seamlessly with the "normal" Java classes that make up your program.
    </li>
    <li style="padding:3px">
      All the nifty Java IDE features like syntax highlighting, automatic indentation, autocompletion, code checking
      as-you-type, code refactoring and so forth work just the same for No-Template classes.
    </li>
    <li style="padding:3px">
      Last but not least: There is no "translation" step whatsoever that you need to integrate with your build
      process and continuous integration - it's all plain Java.
    </li>
  </ul>
  <p>
    However, where is light, there must also be shadow:
  </p>
  <ul>
    <li style="padding:3px">
      Literal text (which maps to Java string literals) must be enclosed in quotes, and some characters must
      (as you all know) be escaped with backslashes. This is particularly annoying for tag attributes
```java
      l(
"    Look &lt;a href=\"" + href + "\">here&lt;/a>".
      );
```
and for embedded JavaScript code.
      <br />
      One possible workaround is to use single quotes instead of double quotes, which both HTML and JavaScript
      permit. (And single quotes need not be backslash-escaped in Java string literals.)
      <br />
      Another is to use the <a href="master/no-template-tools/src/de/unkrig/notemplate/tools/MakeClass.main(String%5B%5D).txt"><code>MakeClass</code></a> command line utility, which
      converts an "example file" into a template class with one big "<code>l()</code>" call.
    </li>
    <li style="padding:3px">
      Embedding variables is not as compact as in other templating languages, where you can typically write
      "<code>${myvar}</code>".
    </li>
    <li style="padding:3px">
      My favorite IDE (of course) supports syntax highlighting, code checking and autocompletion in the <i>Java
      code</i>, but not (yet) in the HTML code in the string literals.
    </li>
    <li style="padding:3px">
      The number of string literals per Java class is limited to 64k, and consequently the size of a
      template class. As a last resort, one can split the code generation into multiple templates which include
      each other.
    </li>
    <li style="padding:3px">
      Code checkers may complain about the "wrong" indentation of the arguments to the "<code>l()</code>" method. You need
      to suppress these message, or, even better, verify that the arguments of "<code>l()</code>" indeed start in coloumn
      one.
    </li>
  </ul>

  <h2>More possibilities</h2>

  <p>
    Other concepts that are not demostrated in the above example are:
  </p>
  <ul>
    <li style="padding:3px">
      Within the renderer, you can use local variables, control structures, constants, etc., as in any Java&trade;
      method.
      <br />
    </li>
    <li style="padding:3px">
      You can declare methods ("subrenderers") that are invoked from the renderer, which also render text.
      By convention, the name of a subrenderer method starts with a small "r", followed by an upper-case letter.
      By moving code from the renderer into subrenderers, you can implement a logical structure for your template,
      to any level you want.
    </li>
    <li style="padding:3px">
      To encapsulate code that is re-used by multiple templates, you'd move it into "subtemplates" and include it
      from the top-level template with
      <pre>   this.include(MySubtemplateHtml.class).render( /* ... */ );</pre>
      Again, the subtemplate declares a method named "render" with all the parameters that it requires to do the
      job.
      <br />
      To keep the (top-level) template classes and the subtemplate classes separate, you'd put them in a package
      "...templates.include" (or a subpackage thereof, if you feel the urge to add even more structure.
    </li>
  </ul>
  
  <h2>Example Code</h2>
  
  <p>
    The "<a href="http://doclet.unkrig.de">JAVADOC doclet</a>" (an open-source reimplementation of the standard JDK
    JAVADOC doclet) makes heavy use of No-Template and is a good source for inspiration.
  </p>
      
  <h2>Download</h2>
      
  <p>
    No-Template is available for download <a href="../download">here</a>; there is also a <a
    href="../CHANGELOG.txt">change log</a>.
  </p>
      
  <h2>Documentation</h2>
      
  <p>
    <a href="index.html">API documentation</a>
  </p>
  <p>
    <a href="de/unkrig/notemplate/tools/MakeClass.html#main-java.lang.String:A-">The "<code>MakeClass</code>" command line
    utility</a>
  </p>
    
  <h2>License</h2>
    
  <p>
    No-Template is distributed under the <a href="../new_bsd_license.txt">New BSD License</a>.
  </p></div>
