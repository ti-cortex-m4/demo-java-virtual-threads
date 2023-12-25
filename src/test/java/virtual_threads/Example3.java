package virtual_threads;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Example3 {

    @Test
    public void test2() throws InterruptedException {
        Thread virtualThread = Thread.startVirtualThread(() -> { sleep(1000); System.out.println("run virtual thread"); });

        assertTrue(virtualThread.isVirtual());
        assertEquals("VirtualThreads", virtualThread.getThreadGroup().getName());
        assertTrue(virtualThread.isDaemon());
        assertEquals(5, virtualThread.getPriority());

        virtualThread.join();
    }

    private void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}