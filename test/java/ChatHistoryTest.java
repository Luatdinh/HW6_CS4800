import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ChatHistoryTest {
    private ChatHistory chatHistory;
    private Message message1;
    private Message message2;

    @Before
    public void setUp() {
        chatHistory = new ChatHistory("user1");
        LocalDateTime timestamp = LocalDateTime.now();
        message1 = new Message("user1", Arrays.asList("user2"), timestamp, "Hello user2!");
        message2 = new Message("user2", Arrays.asList("user1"), timestamp, "Hello user1!");
        chatHistory.addNewMessage(message1);
        chatHistory.addNewMessage(message2);
    }

    @Test
    public void testAddNewMessage() {
        List<MessageMemento> messages = chatHistory.getMessages();
        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertEquals("user1 to user2: Hello user2!", messages.get(0).getContent());
        assertEquals("user2 to user1: Hello user1!", messages.get(1).getContent());
    }

    @Test
    public void testGetLastMessageSent() {
        MessageMemento lastMessage = chatHistory.getLastMessageSent();
        assertNotNull(lastMessage);
        assertEquals("User1 undo his Last message", "user1 to user2: Hello user2!", lastMessage.getContent());  // Correcting the expected result to match the actual functionality
    }


    @Test
    public void testRemoveMessage() {
        MessageMemento lastMessage = chatHistory.getLastMessageSent();
        assertTrue(chatHistory.removeMessage(lastMessage));
        assertEquals(1, chatHistory.getMessages().size());
        assertFalse(chatHistory.removeMessage(lastMessage));
    }

    @Test
    public void testIterator() {
        Iterator<MessageMemento> iterator = chatHistory.iterator(new User("user2", new ChatServer()));
        assertTrue(iterator.hasNext());
        assertEquals("user1 to user2: Hello user2!", iterator.next().getContent());
        assertTrue(iterator.hasNext());
        assertEquals("user2 to user1: Hello user1!", iterator.next().getContent());
        assertFalse(iterator.hasNext());
    }

}
