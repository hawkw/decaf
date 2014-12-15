decaf
=====

[![Build Status](https://travis-ci.org/hawkw/decaf.svg)](https://travis-ci.org/hawkw/decaf)

Decaf is an alleged programming language. It's kinda like Java but a lot less so.

Decaf was implemented by Hawk Weisman (@hawkw) and Max Clive (@ArcticLight) for Professor Janyl Jumadinova's CMPSC420 at Allegheny College. We apologize in advance.

This Scala implementation of the Decaf compiler (`dcc`) is based on the C++/Flex/Bison reference implementation provided by Professor Jumadinova.

![An image describing how great Decaf is](http://4.bp.blogspot.com/_QonjXrwiEbY/SfYav8aEppI/AAAAAAAAJz8/qphPXYzKWEc/s400/decaf-coffee.jpg)

Rave Reviews
-----------

> ... [I]mmense sadness that the first thought that ran through my mind after typing "^this" was "*** this used in incorrect context", demonstrating that clearly I have spent FAR too much time on this project.

~ Max Clive



> I've seen other languages with support for recursive descent parsing, for instance, but for the shear, nightmare glory of an LALR parser, nothing more than superficially more elegant (sic) than Flex/Bison.

~ Andrew Thall, Ph.D


> You're making a compiler named after coffee? That's so dorky.

~ Cara Brosius

> `No errors and 468 warnings detected.`

~ IntelliJ IDEA

Using Decaf
-----------

##### Running the Decaf compiler

You can build the Decaf compiler, `dcc`, using our Gradle build script. Simply type the command `./gradlew dccJar`. This will build Decaf, run our ScalaTest test suite, and then generate the runnable `dcc` jar file. The jar file is output to `build/libs/dcc.jar` relative to the root of the repository, and can be run with `java -jar dcc.jar path/to/source/code/file.decaf`. The Decaf compiler currently takes one argument, a Decaf source code file to compile. If you pass more or less arguments, at this point, the compiler will do nothing and issue a warning.

Decaf's default backend generates Java bytecode using the [Jasmin](http://jasmin.sourceforge.net) assembly language. Invoking the Decaf compiler (`dcc`) on a Decaf source code file will produce Jasmin assembly files with the file extension `.j`. In order to produce executable `.class` files, the Jasmin assembler must be invoked on those `.j` files. You can download an executable Jasmin jarfile [here](http://sourceforge.net/projects/jasmin/files/).
##### Running Tests 

Running our test suites is very easy - all you have to do is navigate to this repository's root directory and type `./gradlew test`. 

This will trigger the Gradle wrapper to download the correct version of Gradle (if it is not already present), install all of Decaf's dependencies (currently the Scala parser-combinators library and ScalaTest), build Decaf, and then run our test cases. Note that the Gradle wrapper will install Gradle and all dependencies locally, so you don't need root access to the machine you wish to test Decaf on. If you have a local copy of Gradle installed, you can feel free to use that to run our test task, as well, but the Gradle wrapper will ensure the correct version of Gradle is used.

##### Generating Documentation

If you're interested in reading the ScalaDoc documentation we've written (and you might be, as it documents some of the decisions we've made regarding our interpretation of the Decaf spec), you can generate this documentation using the `./gradlew scaladoc` command. The documentation will be output to the `build/docs/scaladoc` directory, and you can read it by opening the `index.html` file in that directory.
