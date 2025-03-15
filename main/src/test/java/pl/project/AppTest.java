package pl.project;

import org.junit.jupiter.api.Test;
import pl.project.threads.LibraryUser;
import pl.project.threads.Reader;
import pl.project.threads.Writer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    void testNumberOfReadersAndWritersParsedCorrectly() {
        // Prepare
        String[] args = {"5", "2"};

        // Act
        App.main(args);

        // Assert
        assertEquals(5, App.numberOfReaders, "Number of readers should be parsed correctly");
        assertEquals(2, App.numberOfWriters, "Number of writers should be parsed correctly");
    }

    @Test
    void testDefaultNumberOfReadersAndWritersOnInvalidInput() {
        // Prepare
        String[] args = {"invalid", "input"};

        // Act
        App.main(args);

        // Assert
        assertEquals(10, App.numberOfReaders, "Default number of readers should be used on invalid input");
        assertEquals(3, App.numberOfWriters, "Default number of writers should be used on invalid input");
    }

    @Test
    void testUsersInitialization() {
        // Prepare
        App.numberOfReaders = 3; // reset for testing purposes
        App.numberOfWriters = 2;

        // Act
        Library library = new Library();
        List<LibraryUser> users = new ArrayList<>();

        for (int i = 0; i < App.numberOfReaders; i++) {
            users.add(new Reader(library, "R" + (i + 1)));
        }
        for (int i = App.numberOfReaders; i < App.numberOfReaders + App.numberOfWriters; i++) {
            users.add(new Writer(library, "W" + (i + 1 - App.numberOfReaders)));
        }

        // Assert
        assertEquals(5, users.size(), "Number of users should match readers + writers");
        assertTrue(users.get(0) instanceof Reader, "First user should be a Reader");
        assertTrue(users.get(3) instanceof Writer, "Fourth user should be a Writer");
    }
}
