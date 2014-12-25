
/**
 * =Decaf=
 * Decaf is an alleged programming language. It's kinda like Java but a lot less so. Decaf was implemented by
 * Hawk Weisman and Max Clive  for Professor Janyl Jumadinova's CMPSC420 at Allegheny College. We apologize in advance.
 *
 * === Programming in Decaf ===
 *
 * Decaf is a simple object-oriented language with a C-like syntax that produces Java bytecode. Essentially,
 * it is a watered-down version of Java (hence the name).
 *
 * If you'd like to actually write programs in Decaf, [[http://hawkw.github.io/decaf/decaf.pdf this pdf]] contains
 * a brief specification of the Decaf language, written by Julie Zelenski and updated by Janyl Jumadinova.
 * Our implementation deviates from the specification in a few ways, documented
 * [[https://github.com/hawkw/decaf#differences-from-decaf-specification here]] and elsewhere in the ScalaDoc.
 *
 * The Decaf compiler's [[decaf.backend.JasminBackend code generation component]] is currently a work in progress,
 * and a number of language features described in the specification are not yet implemented.
 * Currently, object-oriented features such as class and interface definitions are not yet implemented.
 */
package object decaf