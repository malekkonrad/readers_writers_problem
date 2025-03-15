package pl.project.threads;

import org.junit.jupiter.api.Test;
import pl.project.Library;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class WriterTest {

    @Test
    void testReaderThreadBehavior() throws InterruptedException {
        Library library = new Library();
        var writer = new Writer(library, "Writer1");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                // Run the thread for a limited time to test behavior
                Thread thread = new Thread(writer);
                thread.start();

                thread.interrupt(); // Stop the thread
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // Assert that the reader interacted with the library correctly
        // Check the counters after execution
        assertEquals(0, library.getWritersCounter());
    }

    @Test
    void testMultipleReadersWithLibrary() throws InterruptedException {
        Library library = new Library();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 5; i++) {
            var writer = new Writer(library, "Writer" + i);
            executor.submit(() -> {
                Thread thread = new Thread(writer);
                thread.start();

                try {

                    thread.interrupt(); // Stop the thread after testing

                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // Ensure the library has no active readers left
        assertEquals(0, library.getWritersCounter());
    }
}