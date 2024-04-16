import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private String sender;
    private List<String> recipients;
    private LocalDateTime timestamp;
    private String content;

    public Message(String sender, List<String> recipients, LocalDateTime timestamp, String content) {
        this.sender = sender;
        this.recipients = recipients;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}