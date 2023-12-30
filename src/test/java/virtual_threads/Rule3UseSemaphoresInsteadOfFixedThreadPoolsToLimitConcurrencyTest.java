package virtual_threads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class Rule3UseSemaphoresInsteadOfFixedThreadPoolsToLimitConcurrencyTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public String useFixedExecutorServiceToLimitConcurrency() throws ExecutionException, InterruptedException {
        Future<String> future = executorService.submit(() -> sharedResource());
        return future.get();
    }


    private final Semaphore semaphore = new Semaphore(8);

    public String useSemaphoreToLimitConcurrency() throws InterruptedException {
        semaphore.acquire();
        try {
            return sharedResource();
        } finally {
            semaphore.release();
        }
    }


    private String sharedResource() {
        return "";
    }
}
