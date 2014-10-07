decaf
=====

[![Build Status](https://magnum.travis-ci.com/hawkw/decaf.svg?token=CZmphEJzXbknGmxGsxbT&branch=master)](https://magnum.travis-ci.com/hawkw/decaf)

Decaf stands for "Compiler Language with No Pronouncable Acronym". It's kinda like Java but a lot less so.

Implemented by Hawk Weisman (@hawkw) and Max Clive (@ArcticLight) for Professor Janyl Jumadinova's CMPSC420 at Allegheny College.

This Scala implementation of the Decaf compiler (`dcc`) is based on the C++/Flex/Bison reference implementation provided by Professor Jumadinova.

Using Decaf
-----------

Right now, the Decaf compiler itself doesn't exist in a runnable state, as only some of the front-end components (the lexical and syntactical parsers) are currently implemented. However, both of these components have test suites (implemented in ScalaTest), so if you check out Decaf, you may run it against our specifications.

Running our test suites is very easy - all you have to do is navigate to this repository's root directory and type `./gradlew test`. 

This will trigger the Gradle wrapper to download the correct version of Gradle (if it is not already present), install all of Decaf's dependencies (currently the Scala parser-combinators library and ScalaTest), build Decaf, and then run our test cases. Note that the Gradle wrapper will install Gradle and all dependencies locally, so you don't need root access to the machine you wish to test Decaf on. If you have a local copy of Gradle installed, you can feel free to use that to run our test task, as well, but the Gradle wrapper will ensure the correct version of Gradle is used.

If you're interested in reading the ScalaDoc documentation we've written (and you might be, as it documents some of the decisions we've made regarding our interpretation of the Decaf spec), you can generate this documentation using the `./gradlew scaladoc` command. The documentation will be output to the `build/docs/scaladoc` directory, and you can read it by opening the `index.html` file in that directory.

Finally, you can build Decaf using `./gradlew build`. This will also run the whole test suite, failing the build attempt if the tests do not pass (though Decaf will still be built; Gradle will simply return a non-zero exit status and yell at you in red for a bit). The compiled Decaf codebase will be output to the `build/classes` directory. Currently, this will not output anything interactive or useful, although eventually we will produce a runnable `dcc` jarfile allowing you to actually invoke the Decaf compiler.
