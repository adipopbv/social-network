package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private final Long from;
    private final List<Long> to;
    private final String message;
    private final LocalDateTime date;
    private final long response;

    public Message(Long from, List<Long> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
        this.response = 0;
    }

    public Message(Long from, List<Long> to, String message, Long response) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
        this.response = response;
    }

    public Message(Long from, List<Long> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.response = 0;
    }

    public Message(Long from, List<Long> to, String message, LocalDateTime date, Long response) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.response = response;
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

    public Long getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "Message{" +
                "\n id = " + getId() +
                ",\n from = " + getFrom() +
                ",\n to = " + getTo() +
                ",\n message = '" + getMessage() + '\'' +
                ",\n date = " + getDate() +
                ",\n response = " + ((response == 0) ? "none" : response) +
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
                getResponse().equals(message1.getResponse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo(), getMessage(), getDate(), getMessage(), getResponse());
    }
}
