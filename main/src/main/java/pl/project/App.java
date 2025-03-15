package pl.project;

import pl.project.threads.LibraryUser;
import pl.project.threads.Reader;
import pl.project.threads.Writer;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class, that performs running readers and writers threads.
 */
public class App 
{

    static int numberOfReaders = 10;
    static int numberOfWriters = 3;

    public static void main( String... args )
    {
        // reading data from input
        try {
            numberOfReaders = Integer.parseInt(args[0]);
            numberOfWriters = Integer.parseInt(args[1]);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }


        Library library = new Library();
        List<LibraryUser> users = new ArrayList<>();

        for (int i = 0; i < numberOfReaders; i++) {
            users.add(new Reader(library, "R"+ (i+1)));
        }

        for (int i = numberOfReaders; i < numberOfReaders + numberOfWriters; i++) {
            users.add(new Writer(library, "W" + (i+1- numberOfReaders)));
        }

        for (LibraryUser user : users) {
            user.start();
        }

    }
}
