package virtual_threads.part2;

import org.junit.jupiter.api.Test;
import virtual_threads.AbstractTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class Rule2WriteBlockingSynchronousCodeTest extends AbstractTest {

    @Test
    public void useSynchronousCodeTest() throws InterruptedException, ExecutionException {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            long startMillis = System.currentTimeMillis();

            Future<Integer> priceInEur = executorService.submit(this::readPriceInEur);
            Future<Float> exchangeRateEurToUsd = executorService.submit(this::readExchangeRateEurToUsd);
            float netAmountInUsd = priceInEur.get() * exchangeRateEurToUsd.get();

            Future<Float> tax = executorService.submit(() -> readTax(netAmountInUsd));
            float grossAmountInUsd = netAmountInUsd * (1 + tax.get());
            assertEquals(132, grossAmountInUsd);

            long durationMillis = System.currentTimeMillis() - startMillis;
            assertEquals(durationMillis, 8000, 100);
        }
    }

    @Test
    public void useAsynchronousCodeTest() throws InterruptedException, ExecutionException {
        long startMillis = System.currentTimeMillis();

        CompletableFuture.supplyAsync(this::readPriceInEur)
            .thenCombine(CompletableFuture.supplyAsync(this::readExchangeRateEurToUsd), (price, exchangeRate) -> price * exchangeRate)
            .thenCompose(amount -> CompletableFuture.supplyAsync(() -> amount * (1 + readTax(amount))))
            .whenComplete((grossAmountInUsd, t) -> {
                if (t == null) {
                    assertEquals(132, grossAmountInUsd);
                } else {
                    fail(t);
                }
            })
            .get();

        long durationMillis = System.currentTimeMillis() - startMillis;
        assertEquals(durationMillis, 8000, 100);
    }

    private int readPriceInEur() {
        return sleepAndGet(2000, 100);
    }

    private float readExchangeRateEurToUsd() {
        return sleepAndGet(3000, 1.1f);
    }

    private float readTax(float amount) {
        return sleepAndGet(5000, 0.2f);
    }
}
