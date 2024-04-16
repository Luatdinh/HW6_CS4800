import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;


public class User implements IterableByUser {
    private String username;
    private ChatServer server;
    private ChatHistory history;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public User(String username, ChatServer server) {
        this.username = username;
        this.server = server;
        this.history = new ChatHistory(username);
        server.registerUser(this);
    }

    public void sendMessage(String content, List<String> recipients) {
        Message message = new Message(this.username, recipients, LocalDateTime.now(), content);
        history.addNewMessage(message);
        server.sendMessage(this.username, recipients, content);
    }

    public void receiveMessage(Message message) {
        System.out.println(this.username + " received message from " + message.getSender() + ": " + message.getContent());
        history.addNewMessage(message);
    }

    public void undoLastMessageSent() {
        MessageMemento lastMemento = history.getLastMessageSent();
        if (lastMemento != null && lastMemento.getContent().startsWith(username + " to")) {
            history.removeMessage(lastMemento);
            System.out.println(this.username + " undo his Last message sent.");
            server.notifyUndo(username, lastMemento.getContent());
        } else {
            System.out.println("No message to undo.");
        }
    }

    @Override
    public Iterator<MessageMemento> iterator(User userToSearchWith) {
        return history.iterator(userToSearchWith);
    }

    public void viewChatHistory() {
        System.out.println(this.username + "'s Chat History:");
        List<MessageMemento> messages = history.getMessages();
        for (MessageMemento memento : messages) {
            String formattedTimestamp = memento.getTimestamp().format(formatter);
            System.out.println("- " + memento.getContent() + " at " + formattedTimestamp);
        }
    }

    public ChatHistory getHistory() {
        return history;
    }

    public String getUserName() {
        return this.username;
    }
}
