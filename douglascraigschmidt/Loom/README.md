This director contains examples of Java's structured concurrency
features.  They require recent versions of the JDK, such as JDK 19 (or
later), as well as gradle 7.6 (or later).

Here's an overview of what's current included in these examples:

. ex1 - This example demonstrates how to create, start, and use
         virtual and platform Thread objects in Java 19, which
         contains an implementation of lightweight user-mode threads
         (virtual threads).

. ex2 - This example demonstrates Java 19 structured concurrency
        features, which enable a main task to split into several
        concurrent sub-tasks that run concurrently to completion
        before the main task can complete.  Java 19 supports
        structured concurrency by enhancing ExecutorService to support
        AutoCloseable and updating Executors to define new static
        factory methods that support usage in a structured manner.

. ex3 - This example demonstrates Java 19 Java structured concurrency
        features, which enables a main task to split into several
        concurrent sub-tasks that run concurrently to completion
        before the main task can complete.  Java 19 supports
        structured concurrency via the StructuredTaskScope class,
        which supports AutoCloseable and defines several nested
        classes (such as StructuredTaskScope.ShutdownOnFailure and
        StructuredTaskScope.ShutdownOnSuccess) that supports
        structured concurrency.

. ex4 - This example compares and contrasts the programming models and
        performance results of Java parallel streams, completable
        futures, Project Reactor, RxJava, and Java structured
        concurrency when applied to download, transform, and store
        many images from a remote web server.

. ex5 - This example demonstrates how to create a custom
        StructuredTaskScope that's used to capture the result of the
        first subtask to complete successfully (i.e., identify a prime
        number without returning a null).

. ex6 - This example shows how to reduce and/or multiply BigFraction
        objects via the Java completable futures framework.  It also
        shows how to customize the Java completable futures framework
        to use arbitrary Executor objects, including the new
        Executors.newVirtualThreadPerTaskExecutor() provided in Java
        19.
