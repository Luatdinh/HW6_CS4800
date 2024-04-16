import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchMessagesByUser implements Iterator<MessageMemento> {
    private Iterator<MessageMemento> iterator;
    private String ownerUsername;
    private String otherUsername;
    private MessageMemento nextItem;
    private boolean nextItemSet = false;

    public SearchMessagesByUser(List<MessageMemento> messages, String ownerUsername, String otherUsername) {
        this.iterator = messages.iterator();
        this.ownerUsername = ownerUsername;
        this.otherUsername = otherUsername;
    }

    @Override
    public boolean hasNext() {
        if (nextItemSet) return true;
        while (iterator.hasNext()) {
            MessageMemento current = iterator.next();
            if (current.getContent().startsWith(ownerUsername + " to " + otherUsername + ":") ||
                    current.getContent().startsWith(otherUsername + " to " + ownerUsername + ":")) {
                nextItem = current;
                nextItemSet = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public MessageMemento next() {
        if (!nextItemSet && !hasNext()) {
            throw new NoSuchElementException();
        }
        nextItemSet = false;
        return nextItem;
    }
}
