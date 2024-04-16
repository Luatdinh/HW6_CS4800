import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatHistory implements IterableByUser {
    private List<MessageMemento> messages;
    private String ownerUsername;

    public ChatHistory(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        this.messages = new ArrayList<>();
    }

    public void addNewMessage(Message message) {
        for (String recipient : message.getRecipients()) {
            if (recipient.equals(ownerUsername) || message.getSender().equals(ownerUsername)) {
                String modifiedContent = message.getSender() + " to " + recipient + ": " + message.getContent();
                messages.add(new MessageMemento(modifiedContent, message.getTimestamp()));
            }
        }
    }

    public MessageMemento getLastMessageSent() {
        for (int i = messages.size() - 1; i >= 0; i--) {
            MessageMemento memento = messages.get(i);
            if (memento.getContent().startsWith(ownerUsername)) {
                return memento;
            }
        }
        return null;
    }

    public boolean removeMessage(MessageMemento memento) {
        return messages.removeIf(m -> m.getContent().equals(memento.getContent()));
    }

    public List<MessageMemento> getMessages() {
        return messages;
    }

    @Override
    public Iterator<MessageMemento> iterator(User userToSearchWith) {
        return new SearchMessagesByUser(messages, ownerUsername, userToSearchWith.getUsername());
    }
}