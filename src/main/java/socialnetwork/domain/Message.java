package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity {
    private final User from;
    private final List<User> to;
    private final String text;
    private final LocalDateTime date;
    private final Message response;
    private final Message original;

    public Message(User from, List<User> to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = LocalDateTime.now();
        this.response = null;
        this.original = null;
    }

    public Message(User from, List<User> to, String text, Message response) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = LocalDateTime.now();
        this.response = response;
        this.original = null;
    }

    public Message(User from, List<User> to, String text, Message original) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = LocalDateTime.now();
        this.response = null;
        this.original = original;
    }

    public Message(User from, List<User> to, String text, LocalDateTime date, Message response, Message original) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = date;
        this.response = response;
        this.original = original;
    }

    public User getFrom() {
        return from;
    }

    public Long getFromId() {
        return getFrom().getId().getValue();
    }

    public List<User> getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getResponse() {
        return response;
    }

    public Long getResponseId() {
        return getResponse().getId();
    }

    public void setResponse(long response) {
        this.response = response;
    }

    public Message getOriginal() {
        return original;
    }

    public Long getOriginalId() {
        return original.getId();
    }

    public void setOriginal(Message original) {
        this.original = original;
    }

    public void setOriginalId(Long originalId) {
        this.getOriginal().setId(originalId);
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
