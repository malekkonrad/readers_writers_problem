package pl.project.threads;


import org.junit.jupiter.api.Test;
import pl.project.Library;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReaderTest {

    @Test
    void testReaderThreadBehavior() throws InterruptedException {
        Library library = new Library();
        Reader reader = new Reader(library, "Reader1");

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                // Run the thread for a limited time to test behavior
                Thread thread = new Thread(reader);
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
        assertEquals(0, library.getReadersCounter());
    }

    @Test
    void testMultipleReadersWithLibrary() throws InterruptedException {
        Library library = new Library();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 5; i++) {
            Reader reader = new Reader(library, "Reader" + i);
            executor.submit(() -> {
                Thread thread = new Thread(reader);
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
        assertEquals(0, library.getReadersCounter());
    }
}