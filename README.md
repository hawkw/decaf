decaf
=====

[![Build Status](https://magnum.travis-ci.com/hawkw/decaf.svg?token=CZmphEJzXbknGmxGsxbT&branch=master)](https://magnum.travis-ci.com/hawkw/decaf)

Decaf is an alleged programming language. It's kinda like Java but a lot less so.

Decaf was implemented by Hawk Weisman (@hawkw) and Max Clive (@ArcticLight) for Professor Janyl Jumadinova's CMPSC420 at Allegheny College. We apologize in advance.

This Scala implementation of the Decaf compiler (`dcc`) is based on the C++/Flex/Bison reference implementation provided by Professor Jumadinova.

Rave Reviews
-----------

> ... [I]mmense sadness that the first thought that ran through my mind after typing "^this" was "*** this used in incorrect context", demonstrating that clearly I have spent FAR too much time on this project.

~ Max Clive



> I've seen other languages with support for recursive descent parsing, for instance, but for the shear, nightmare glory of an LALR parser, nothing more than superficially more elegant (sic) than Flex/Bison.

~ Andrew Thall, Ph.D


> You're making a compiler named after coffee? That's so dorky.

~ Cara Brosius

> ```scala
inherited <- classState.parent.get.table.get(thing) if !classState.table.get(thing).get.matches(inherited)
```

~ the Decaf codebase

Using Decaf
-----------

#### Programming in Decaf

Decaf is a simple object-oriented language with a C-like syntax that produces Java bytecode. Essentially, it is a watered-down version of Java (hence the name). 

If you'd like to actually write programs in Decaf, the file [`decaf.pdf`](decaf.pdf) contains a brief specification of the Decaf language, written by Julie Zelenski and updated by Janyl Jumadinova. Our implementation deviates from the specification in a few ways, documented in the next section. 

Additionally, the [`src/test/resources`](src/test/resources) contains a number of sample Decaf programs which are used by our test suite for various phases of the compiler. These could be very useful to get an understanding of Decaf's syntax. Of particular interest are the [samples](src/test/resources/lab3-samples) used by the parser test suite, which contain whole working Decaf programs.

##### Differences from Decaf Specification

Coming soon.

#### Using the Decaf compiler

You can build the Decaf compiler, `dcc`, using our Gradle build script. Simply type the command `./gradlew dccJar`. This will build Decaf, run our ScalaTest test suite, and then generate the runnable `dcc` jar file. The jar file is output to `build/libs/dcc.jar` relative to the root of the repository, and can be run with `java -jar dcc.jar path/to/source/code/file.decaf`. The Decaf compiler currently takes one argument, a Decaf source code file to compile. If you pass more or less arguments, at this point, the compiler will do nothing and issue a warning.

Decaf's default backend generates Java bytecode using the [Jasmin](http://jasmin.sourceforge.net) assembly language. Invoking the Decaf compiler (`dcc`) on a Decaf source code file will produce Jasmin assembly files with the file extension `.j`. In order to produce executable `.class` files, the Jasmin assembler must be invoked on those `.j` files. You can download an executable Jasmin jarfile [here](http://sourceforge.net/projects/jasmin/files/).

##### Running Tests 

Running our test suites is very easy - all you have to do is navigate to this repository's root directory and type `./gradlew test`. 

This will trigger the Gradle wrapper to download the correct version of Gradle (if it is not already present), install all of Decaf's dependencies (currently the Scala parser-combinators library and ScalaTest), build Decaf, and then run our test cases. Note that the Gradle wrapper will install Gradle and all dependencies locally, so you don't need root access to the machine you wish to test Decaf on. If you have a local copy of Gradle installed, you can feel free to use that to run our test task, as well, but the Gradle wrapper will ensure the correct version of Gradle is used.

##### Generating Documentation

If you're interested in reading the ScalaDoc documentation we've written (and you might be, as it documents some of the decisions we've made regarding our interpretation of the Decaf spec), you can generate this documentation using the `./gradlew scaladoc` command. The documentation will be output to the `build/docs/scaladoc` directory, and you can read it by opening the `index.html` file in that directory.
