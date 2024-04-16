import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatServer {
    private Map<String, User> users;
    private Map<String, Set<String>> blockedUsers;

    public ChatServer() {
        users = new HashMap<>();
        blockedUsers = new HashMap<>();
    }

    public void registerUser(User user) {
        users.put(user.getUsername(), user);
        blockedUsers.put(user.getUsername(), new HashSet<>());
        System.out.println(user.getUsername() + " is registered in the chat server.");
    }

    public void unregisterUser(String username) {
        users.remove(username);
        blockedUsers.remove(username);
        System.out.println(username + " is removed from the chat server.");
    }

    public void sendMessage(String senderUsername, List<String> recipientUsernames, String messageContent) {
        if (recipientUsernames.isEmpty()) {
            System.out.println("No recipients provided for this message.");
            return;
        }

        StringBuilder recipients = new StringBuilder();
        boolean first = true;
        boolean allRecipientsUnregistered = true;

        for (String recipientUsername : recipientUsernames) {
            if (users.containsKey(recipientUsername)) {
                allRecipientsUnregistered = false;
                if (!isBlocked(recipientUsername, senderUsername)) {
                    if (first) {
                        first = false;
                    } else {
                        recipients.append(", ");
                    }
                    recipients.append(recipientUsername);
                }
            } else {
                System.out.print(senderUsername + " tried to send a message to " + recipientUsername + ", but ");
                System.out.println(recipientUsername + " is not registered on the server");
            }
        }

        if (!recipients.isEmpty()) {
            System.out.println(senderUsername + " sends message to " + recipients + ": " + messageContent);
            for (String recipientUsername : recipientUsernames) {
                if (users.containsKey(recipientUsername) && !isBlocked(recipientUsername, senderUsername)) {
                    User recipient = users.get(recipientUsername);
                    if (recipient != null) {
                        recipient.receiveMessage(new Message(senderUsername, recipientUsernames, LocalDateTime.now(), messageContent));
                    }
                }
            }
        } else if (allRecipientsUnregistered) {
            System.out.println("No valid recipients provided for this message.");
        }
    }

    public void blockMessage(String requesterUsername, String usernameToBlock) {
        blockedUsers.computeIfAbsent(requesterUsername, k -> new HashSet<>()).add(usernameToBlock);
        System.out.println(requesterUsername + " has blocked " + usernameToBlock);
    }


    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Set<String>> getBlockedUsers() {
        return blockedUsers;
    }

    public boolean isBlocked(String recipientUsername, String senderUsername) {
        Set<String> blocks = blockedUsers.get(recipientUsername);
        return blocks != null && blocks.contains(senderUsername);
    }

    public void notifyUndo(String senderUsername, String messageContent) {
        String[] parts = messageContent.split(" to ");
        String sender = parts[0];
        String messagePart = parts[1].substring(parts[1].indexOf(':') + 2);
        String[] recipientParts = parts[1].split(": ")[0].split(", ");

        for (String recipient : recipientParts) {
            User user = users.get(recipient.trim());
            if (user != null) {
                MessageMemento toRemove = new MessageMemento(sender + " to " + recipient + ": " + messagePart, LocalDateTime.now());
                user.getHistory().removeMessage(toRemove);
            }
        }

        User senderUser = users.get(sender);
        if (senderUser != null) {
            for (String recipient : recipientParts) {
                MessageMemento toRemove = new MessageMemento(sender + " to " + recipient + ": " + messagePart, LocalDateTime.now());
                senderUser.getHistory().removeMessage(toRemove);
            }
        }
    }

}
