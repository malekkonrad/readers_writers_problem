package pl.project.threads;

import pl.project.Library;

/**
 * Class {@code Writer} representing Writers thread, wanting to write in Library.
 */
public class Writer extends LibraryUser {

    /**
     * Simple constructor
     * @param library used in run() method
     * @param id unique ID used in printing information about moves
     */
    public Writer(Library library, String id) {
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
                library.startWriting(id);

                sleep(3000);

                library.stopWriting(id);

                // odpoczywa przez 4 sekundy
                sleep(1000);

            } catch (InterruptedException e){
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

}
