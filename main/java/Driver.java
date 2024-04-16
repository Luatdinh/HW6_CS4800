import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class Driver {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        User Paul = new User("Paul", server);
        User Michael = new User("Michael", server);
        User Timothee = new User("Timothee", server);
        System.out.println("---------Users send messages and receive messages---------");
        Paul.sendMessage("Hello Michael and Timothee!", List.of("Michael", "Timothee"));
        Michael.sendMessage("Hello Paul! How are you?", List.of("Paul"));
        Timothee.sendMessage("Hey everyone!", List.of("Paul", "Michael"));
        Paul.sendMessage("I am great, thanks for asking Michael!", List.of("Michael"));
        System.out.println("-------------------Paul undoes his last message--------------------");
        Paul.undoLastMessageSent();
        System.out.println("--------------------Michael blocks Timothee---------------------");
        server.blockMessage("Michael", "Timothee");
        System.out.println("Timothee tries to send a message to Michael after being blocked:");
        Timothee.sendMessage("Michael, can you see this message?", List.of("Michael"));
        System.out.println("-----------------Users view their chat history------------------");
        Paul.viewChatHistory();
        Michael.viewChatHistory();
        Timothee.viewChatHistory();
        System.out.println("----------Users view their chat history with specific user using iterator-----------");
        System.out.println("Iterating through a user's messages with another user");
        System.out.println("-Paul's message with Michael");
        Iterator<MessageMemento> PaulIteratorMichael = Paul.iterator(Michael);
        while (PaulIteratorMichael.hasNext()) {
            MessageMemento message = PaulIteratorMichael.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-Michael's message with Paul");
        Iterator<MessageMemento> MichaelIteratorPaul = Michael.iterator(Paul);
        while (MichaelIteratorPaul.hasNext()) {
            MessageMemento message = MichaelIteratorPaul.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-Paul's message with Timothee");
        Iterator<MessageMemento> PaulIteratorTimothee = Paul.iterator(Timothee);
        while (PaulIteratorTimothee.hasNext()) {
            MessageMemento message = PaulIteratorTimothee.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-Timothee's message with Paul");
        Iterator<MessageMemento> TimotheeIteratorPaul = Timothee.iterator(Paul);
        while (TimotheeIteratorPaul.hasNext()) {
            MessageMemento message = TimotheeIteratorPaul.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-Michael's message with Timothee");
        Iterator<MessageMemento> MichaelIteratorTimothee = Michael.iterator(Timothee);
        while (MichaelIteratorTimothee.hasNext()) {
            MessageMemento message = MichaelIteratorTimothee.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-Timothee's message with Michael");
        Iterator<MessageMemento> TimotheeIteratorMichael = Timothee.iterator(Michael);
        while (TimotheeIteratorMichael.hasNext()) {
            MessageMemento message = TimotheeIteratorMichael.next();
            System.out.println(message.getContent() + " (" + message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")");
        }
        System.out.println("-------------------Unregistering Michael-------------------");
        server.unregisterUser("Michael");
        Paul.sendMessage("Hello Michael, hope you are doing well", List.of("Michael"));

    }

}