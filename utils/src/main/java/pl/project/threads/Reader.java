package pl.project.threads;

import pl.project.Library;

/**
 * Class {@code Reader} representing Readers thread, wanting to read in Library.
 */
public class Reader extends LibraryUser {

    /**
     * Simple constructor
     * @param library used in run() method
     * @param id unique ID used in printing information about moves
     */
    public Reader(Library library, String id) {
        super(library, id);
    }

    /**
     * Method which executes thread commands.
     */
    @Override
    public void run() {

        while (true) {
            try{

                // Wywołanie że chce zacząć czytać
                library.startReading(id);

                // czyta przez 2 sekundy
                sleep(2000);
                library.stopReading(id);

                // odpoczywa przez 4 sekundy
                sleep(1000);


            } catch (InterruptedException e){
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
