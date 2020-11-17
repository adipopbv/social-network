package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private final Long from;
    private final List<Long> to;
    private final String message;
    private final LocalDateTime date;
    private final Message replyTo;

    public Message(Long from, List<Long> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
        this.replyTo = null;
    }

    public Message(Long from, List<Long> to, String message, Message replyTo) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
        this.replyTo = replyTo;
    }

    public Message(Long from, List<Long> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.replyTo = null;
    }

    public Message(Long from, List<Long> to, String message, LocalDateTime date, Message replyTo) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.replyTo = replyTo;
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "\n from=" + from +
                "\n , to=" + to +
                "\n , message='" + message + '\'' +
                "\n , date=" + date +
                "\n , replyTo=" + replyTo.getId() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return getFrom().equals(message1.getFrom()) &&
                getTo().equals(message1.getTo()) &&
                getMessage().equals(message1.getMessage()) &&
                getDate().equals(message1.getDate()) &&
                getReplyTo().equals(message1.getReplyTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getMessage(), getDate(), getMessage());
    }
}
