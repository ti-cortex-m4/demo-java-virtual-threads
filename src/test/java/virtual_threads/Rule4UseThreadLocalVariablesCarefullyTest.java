package virtual_threads;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class Rule4UseThreadLocalVariablesCarefullyTest {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    @Test
    public void threadLocalVariablesTest() throws InterruptedException {
        assertNull(THREAD_LOCAL.get());

        THREAD_LOCAL.set("zero");
        assertEquals("zero", THREAD_LOCAL.get()); // unconstrained mutability

        THREAD_LOCAL.set("one");
        assertEquals("one", THREAD_LOCAL.get()); // unbounded lifetime

        Thread childThread = new Thread(() -> {
            assertEquals("one", THREAD_LOCAL.get()); // expensive inheritance
        });
        childThread.join();

        THREAD_LOCAL.remove();
        assertNull(THREAD_LOCAL.get());
    }

    private static final ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();

    @Test
    public void scopedValuesTest() {
        assertThrows(NoSuchElementException.class,
            () -> {
                assertNull(SCOPED_VALUE.get());
            });

        ScopedValue.where(SCOPED_VALUE, "zero").run(
            () -> {
                assertEquals("zero", SCOPED_VALUE.get());
                ScopedValue.where(SCOPED_VALUE, "one").run(
                    () -> {
                        assertEquals("one", SCOPED_VALUE.get());
                    }
                );
                assertEquals("zero", SCOPED_VALUE.get());

                try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                    Supplier<String> value1 = scope.fork(() -> {
                            assertEquals("zero", SCOPED_VALUE.get());
                            return "a";
                        }
                    );
                    Supplier<String> value2 = scope.fork(() -> {
                            assertEquals("zero", SCOPED_VALUE.get());
                            return "z";
                        }
                    );

                    scope.join().throwIfFailed();
                    assertEquals("a", value1.get());
                    assertEquals("z", value2.get());
                } catch (Exception e) {
                    fail(e);
                }
            });

        assertThrows(NoSuchElementException.class,
            () -> {
                assertNull(SCOPED_VALUE.get());
            });
    }
}
