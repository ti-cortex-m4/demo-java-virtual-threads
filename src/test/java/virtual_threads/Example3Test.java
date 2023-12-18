package virtual_threads;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Example3Test {

    @Test
    public void platformThreadBuilderTest() throws InterruptedException, ExecutionException {
        Thread.Builder builder = Thread.ofPlatform()
            .group(Thread.currentThread().getThreadGroup())
            .daemon(false)
            .priority(10)
            .stackSize(1024)
            .name("MyThread")
            .inheritInheritableThreadLocals(false)
            .uncaughtExceptionHandler((t, e) -> System.out.printf("Thread %s failed with exception %s", t, e));
        Thread thread = builder.start(() -> System.out.println("run"));

        assertEquals("main", thread.getThreadGroup().getName());
        assertFalse(thread.isDaemon());
        assertEquals(10, thread.getPriority());

        thread.join();
    }

    @Test
    public void virtualThreadBuilderTest() throws InterruptedException, ExecutionException {
        Thread.Builder builder = Thread.ofVirtual()
            .name("MyThread")
            .inheritInheritableThreadLocals(false)
            .uncaughtExceptionHandler((t, e) -> System.out.printf("Thread %s failed with exception %s", t, e));
        Thread thread = builder.start(() -> System.out.println("run"));

        assertEquals("VirtualThreads", thread.getThreadGroup().getName());
        assertTrue(thread.isDaemon());
        assertEquals(5, thread.getPriority());

        thread.join();
    }
}
