Explain Gradle DSL for building tasks
=====================================

Consider the following build.gradle:

    task helloWorld {
      println "Hello World"
    }

which prints "Hello World" when you invoke `gradle helloWorld`.
I'd like to understand what happens on the Groovy side when it
evaluates this script.

According to Gradle documentation, writing `task name` creates
a `Task` object named `name` and makes it available as a property
of the current delegate object, which is a `Project` instance.

I can't understand what `name` is from the Groovy perspective.
It's not a string, and I think it's a method call:

    helloWorld ( { println "Hello World!" } )

The current `Project` delegate may use `methodMissing` to add
a task with the given name, but how can it differentiate between
these two:

   // Ok, task definition
   task foo { println "Foo" }

   // Throws an exception, method not found
   bar { println "Bar" }

In fact, my current implementation lacks with respect to this
sort of error detection. I'd like to build tasks with the Gradle
syntax `task foo`, but want an exception to be thrown when one
doesn't use the `task()` method to define a task.

See the source on [GitHub][1]

[1]: https://github.com/RaffaeleSgarro/gradle-like-dsl
