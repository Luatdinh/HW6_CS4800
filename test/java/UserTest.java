import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.time.LocalDateTime;

class UserTest {
    private ChatServer server;
    private User paul;
    private User michael;

    @BeforeEach
    void setUp() {
        server = new ChatServer();
        paul = new User("Paul", server);
        michael = new User("Michael", server);
    }

    @Test
    void testSendMessage() {
        paul.sendMessage("Hello Michael", Arrays.asList("Michael"));
        assertFalse(michael.getHistory().getMessages().isEmpty(), "Michael should have received a message.");
    }

    @Test
    void testUndoMessage() {
        paul.sendMessage("Hello Michael", Arrays.asList("Michael"));
        paul.undoLastMessageSent();
        assertTrue(michael.getHistory().getMessages().isEmpty(), "Michael's history should be empty after undo.");
    }

    @Test
    void testReceiveMessage() {
        Message message = new Message("Paul", Arrays.asList("Michael"), LocalDateTime.now(), "Hello, Michael");
        michael.receiveMessage(message);
        assertFalse(michael.getHistory().getMessages().isEmpty(), "Michael should have messages in his history after receiving one.");
        assertEquals("Paul to Michael: Hello, Michael", michael.getHistory().getMessages().get(0).getContent(), "Check the content of the received message.");
    }

    @Test
    void testViewChatHistory() {
        paul.sendMessage("Hello Michael", Arrays.asList("Michael"));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        michael.viewChatHistory();
        String expectedOutput = "Michael's Chat History:\n- Paul to Michael: Hello Michael at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n";
        assertTrue(outContent.toString().contains(expectedOutput), "Output should contain the formatted chat history.");
    }

    @Test
    void testGetHistory() {
        assertNotNull(paul.getHistory(), "Should return a non-null ChatHistory object.");
        assertEquals(ChatHistory.class, paul.getHistory().getClass(), "Should return an instance of ChatHistory.");
    }

    @Test
    void testGetUsername() {
        assertEquals("Paul", paul.getUsername(), "Should return the username 'Paul'.");
        assertEquals("Michael", michael.getUsername(), "Should return the username 'Michael'.");
    }

    @Test
    void testIterator() {
        paul.sendMessage("Hi Michael", Arrays.asList("Michael"));
        paul.sendMessage("Hello World", Arrays.asList("Alice"));
        michael.sendMessage("Hi Paul", Arrays.asList("Paul"));
        Iterator<MessageMemento> it = paul.iterator(michael);
        assertTrue(it.hasNext(), "There should be a message from Paul to Michael.");
        assertEquals("Paul to Michael: Hi Michael", it.next().getContent(), "The message content should match.");
        assertTrue(it.hasNext(), "There should be a message from Michael to Paul.");
        assertEquals("Michael to Paul: Hi Paul", it.next().getContent(), "The message content should match.");
        assertFalse(it.hasNext(), "No more messages should be left.");
    }


}
