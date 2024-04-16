import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class ChatServerTest {
    private ChatServer server;
    private User user1;
    private User user2;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        server = new ChatServer();
        user1 = new User("Paul", server);
        user2 = new User("Michael", server);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testSendMessage() {
        server.sendMessage("Paul", Arrays.asList("Michael"), "Hello, Michael!");
        String expected = "Paul sends message to Michael: Hello, Michael!\nMichael received message from Paul: Hello, Michael!\n";
        assertEquals(expected, outContent.toString(), "Output should match expected send message sequence.");
    }

    @Test
    void testSendToUnregisteredUser() {
        server.sendMessage("Paul", Arrays.asList("Timothee"), "Hello, Timothee!");
        String expectedOutput = "Paul tried to send a message to Timothee, but Timothee is not registered on the server\nNo valid recipients provided for this message.\n";
        assertEquals(expectedOutput, outContent.toString(), "Output should indicate that Timothee is not registered.");
    }

    @Test
    void testBlockUser() {
        server.blockMessage("Michael", "Paul");
        assertTrue(server.isBlocked("Michael", "Paul"), "Paul should be blocked from sending messages to Michael.");
    }

    @Test
    void testRegisterAndUnregisterUser() {
        assertTrue(server.getUsers().containsKey("Paul"), "Paul should be registered.");
        server.unregisterUser("Paul");
        assertFalse(server.getUsers().containsKey("Paul"), "Paul should be unregistered.");
    }

    @Test
    void testSendMessageNoRecipients() {
        server.sendMessage("Paul", Arrays.asList(), "Hello, anyone!");
        String expectedOutput = "No recipients provided for this message.\n";
        assertEquals(expectedOutput, outContent.toString(), "Output should match expected for no recipients.");
    }


    @Test
    void testBlockedUserCannotSendMessage() {
        outContent.reset();
        server.blockMessage("Michael", "Paul");
        server.sendMessage("Paul", Arrays.asList("Michael"), "Can you hear me, Michael?");
        String expectedOutput = "Michael has blocked Paul\n";
        assertEquals(expectedOutput, outContent.toString(), "Output should confirm that the message is blocked.");
    }


    @Test
    void testNotifyUndo() {
        server.registerUser(new User("John", server));
        outContent.reset();
        String messageContent = "Paul to John: Hello, John!";
        server.sendMessage("Paul", Arrays.asList("John"), messageContent);
        server.notifyUndo("Paul", messageContent);
        assertTrue(user1.getHistory().getMessages().isEmpty(), "John's chat history should be empty after undo.");
    }


    @Test
    void testEmptyServer() {
        ChatServer emptyServer = new ChatServer();
        assertTrue(emptyServer.getUsers().isEmpty(), "No users should be registered in a new server.");
        assertTrue(emptyServer.getBlockedUsers().isEmpty(), "No users should be blocked in a new server.");
    }

}
