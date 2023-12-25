package virtual_threads;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Example5ThreadFactoryTest {

    @Test
    public void virtualThreadFactoryTest() throws InterruptedException {
        Thread.Builder builder = Thread.ofVirtual()
            .name("a virtual thread");
        ThreadFactory threadFactory = builder.factory();
        Thread thread = threadFactory.newThread(() -> System.out.println("run"));

        assertEquals("a virtual thread", thread.getName());
        assertEquals(Thread.State.NEW, thread.getState());
    }
}
