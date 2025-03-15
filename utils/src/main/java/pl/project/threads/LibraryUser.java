package pl.project.threads;

import lombok.AllArgsConstructor;
import pl.project.Library;

/**
 * Abstract class after which Readers and Writers inherit, so they have common ancestor.
 */
@AllArgsConstructor
public abstract class LibraryUser extends Thread {

    Library library;
    String id;

    /**
     * Method which executes thread commands.
     */
    @Override
    public void run() {}
}
