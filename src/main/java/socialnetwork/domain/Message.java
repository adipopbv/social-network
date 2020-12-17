package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Message extends Entity {
    private final User from;
    private final List<User> to;
    private final String text;
    private final LocalDateTime date;
    private Message original;
    private Message response;

    public Message(User from, List<User> to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = LocalDateTime.now();
        this.response = null;
        this.original = null;
    }

    public Message(User from, List<User> to, String text, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = date;
        this.response = null;
        this.original = null;
    }

    public Message(User from, List<User> to, String text, LocalDateTime date, Message original, Message response) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.date = date;
        this.original = original;
        this.response = response;
    }

    public User getFrom() {
        return from;
    }

    public Id getFromId() {
        return getFrom().getId();
    }

    public List<User> getTo() {
        return to;
    }

    public List<Id> getToIds() {
        return to.stream().map(Entity::getId).collect(Collectors.toList());
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getOriginal() {
        return original;
    }

    public void setOriginal(Message original) {
        this.original = original;
    }

    public Id getOriginalId() {
        return original.getId();
    }

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    public Id getResponseId() {
        return getResponse().getId();
    }

    @Override
    public String toString() {
        return "Message{" +
                "\n id = " + getId() +
                ",\n from = " + getFromId() +
                ",\n to = " + getToIds() +
                ",\n text = '" + getText() + '\'' +
                ",\n date = " + getDate() +
                ",\n original = " + getOriginalId() +
                ",\n response = " + getResponseId() +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return getFromId().equals(message1.getFromId()) &&
                getToIds().equals(message1.getToIds()) &&
                getText().equals(message1.getText()) &&
                getDate().equals(message1.getDate()) &&
                getOriginalId().equals(message1.getOriginalId()) &&
                getResponseId().equals(message1.getResponseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromId(), getToIds(), getText(), getDate(), getOriginalId(), getResponseId());
    }
}
