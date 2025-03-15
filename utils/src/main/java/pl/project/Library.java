package pl.project;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


/**
 * Class {@code Library} represents shared resource by Readers and Writers.
 * Provides methods {@code startReading}, {@code startWriting}, {@code stopReading}, {@code stopWriting}.
 * Only 5 Readers are allowed to enter Library at once, and only 1 Writer to be at library.
 */
@Getter
public class Library {

    static final int READERS_COUNT = 5;
    static final int WRITERS_COUNT = 1;

    Semaphore readingResource = new Semaphore(READERS_COUNT);
    Semaphore writingResource = new Semaphore(WRITERS_COUNT);
    Semaphore service = new Semaphore(1);

    int waitingReaders = 0;
    int waitingWriters = 0;
    int readersCounter = 0;
    int writersCounter = 0;

    List<String> queue = new ArrayList<>();
    List<String> inLibrary = new ArrayList<>();



    /**
     * Method which handles Reader request to start reading
     * @param readerId String representing selected reader
     * @throws InterruptedException when problems with using Semaphore.acquire() or Semaphore.release()
     */
    public void startReading(String readerId) throws InterruptedException {

        // my info
        synchronized (this) {
            System.out.println("<- Reader [" + readerId + "] tries to enter library and waits for permission to read...");
            waitingReaders++;
            queue.add(readerId);
            printCurrentInfo();
        }

        // prosi o wejście
        service.acquire();
        // jesli je dostaje - znaczy że pisarz przede mną o nie nie poprosił - zwalniam
        // zgłaszam że chcę dostęp do czytania
        readingResource.acquire();

        service.release();

        waitingReaders--;
        queue.remove(readerId);
        // pierwszy czytelnik blokuje dostęp pisarzom
        if (readersCounter==0){
            writingResource.acquire();
        }

        // zwiększam licznik czytających
        readersCounter++;
        inLibrary.add(readerId);
        System.out.println(  "-- Reader [" + readerId + "] started reading...");
    }

    /**
     * Method which handles Reader request to stop reading
     * @param readerId String representing selected reader
     * @throws InterruptedException when problems with using Semaphore.acquire() or Semaphore.release()
     */
    public void stopReading(String readerId) throws InterruptedException {
        synchronized (this) {
            System.out.println("-> Reader [" + readerId + "] stop reading and left library...");
            readersCounter--;
            inLibrary.remove(readerId);
            printCurrentInfo();
        }

        // Zwolnienie dostępu do zasoby pisarzy, jeśli to ostatni czytelnik
        if (readersCounter == 0) {
            writingResource.release();
        }

        // Zwolnienie dostępu do zasobu czytelników
        readingResource.release();

    }


    /**
     * Method which handles Writer request to start writing
     * @param writerId String representing selected reader
     * @throws InterruptedException when problems with using Semaphore.acquire() or Semaphore.release()
     */
    public void startWriting(String writerId) throws InterruptedException {
        synchronized (this) {
            System.out.println("<- Writer [" + writerId + "] tries to enter library and start writing...");
            waitingWriters++;
            queue.add(writerId);
            printCurrentInfo();
        }
        service.acquire();

        // Czekanie na dostęp do pisania
        writingResource.acquire();
        synchronized (this) {
            waitingWriters--;
            queue.remove(writerId);
            writersCounter++;
            inLibrary.add(writerId);
            System.out.println("-- Writer [" + writerId + "] started writing...");
            printCurrentInfo();
        }


    }

    /**
     * Method which handles Writer request to stop writing
     * @param writerId String representing selected reader
     * @throws InterruptedException when problems with using Semaphore.acquire() or Semaphore.release()
     */
    public void stopWriting(String writerId) throws InterruptedException {
        synchronized (this) {
            System.out.println("-> Writer [" + writerId + "] stop writing and left library...");
            writersCounter--;
            inLibrary.remove(writerId);
            printCurrentInfo();
        }

        writingResource.release();
        service.release();

    }


    /**
     * Method used in every public method to show current state
     * at Library
     */
    private synchronized void printCurrentInfo() {

        System.out.println(
                "IN LIBRARY: " + inLibrary.toString() + ", Readers: " + readersCounter + ", Writers: " + writersCounter +
                " \tIN QUEUE: " + queue.toString() + ", READERS: " + waitingReaders + ", WRITERS: " + waitingWriters
                );
    }

}
