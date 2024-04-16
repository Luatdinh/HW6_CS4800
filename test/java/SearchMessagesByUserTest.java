import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class SearchMessagesByUserTest {
    private List<MessageMemento> messages;
    private SearchMessagesByUser iterator;

    @BeforeEach
    void setUp() {
        // Setting up a few sample messages
        LocalDateTime timestamp = LocalDateTime.now();
        messages = Arrays.asList(
                new MessageMemento("Paul to Michael: Hello", timestamp),
                new MessageMemento("Michael to Paul: Hi", timestamp),
                new MessageMemento("Paul to Alice: Greetings", timestamp)
        );
        iterator = new SearchMessagesByUser(messages, "Paul", "Michael");
    }

    @Test
    void testHasNextTrue() {
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
    }

    @Test
    void testNextReturnsCorrectElement() {
        assertEquals("Paul to Michael: Hello", iterator.next().getContent(), "Should return the correct message.");
    }

}
