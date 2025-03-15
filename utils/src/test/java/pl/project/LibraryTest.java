package pl.project;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LibraryTest {


    @Test
    void testSingleReader() throws InterruptedException {
        Library library = new Library();

        library.startReading("Reader1");
        assertEquals(1, library.getReadersCounter());

        library.stopReading("Reader1");
        assertEquals(0, library.getReadersCounter());
    }

    @Test
    void testSingleWriter() throws InterruptedException {
        Library library = new Library();

        library.startWriting("Writer1");
        assertEquals(1, library.getWritersCounter());

        library.stopWriting("Writer1");
        assertEquals(0, library.getWritersCounter());
    }



    @Test
    void testReaderWriterExclusion() throws InterruptedException {
        Library library = new Library();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            try {
                library.startReading("Reader1");
                library.stopReading("Reader1");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {
            try {
                library.startWriting("Writer1");
                library.stopWriting("Writer1");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(0, library.getReadersCounter());
        assertEquals(0, library.getWritersCounter());
    }

}