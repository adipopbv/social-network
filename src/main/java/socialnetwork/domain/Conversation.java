package socialnetwork.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Conversation extends Entity {
    private final List<User> participants;
    private final List<Message> messages;

    public Conversation(List<User> participants, List<Message> messages) {
        this.participants = participants;
        this.messages = messages;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<Id> getParticipantsIds() {
        return getParticipants().stream().map(Entity::getId).collect(Collectors.toList());
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Id> getMessagesIds() {
        return getMessages().stream().map(Entity::getId).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return (getParticipantsIds().equals(that.getParticipantsIds()) &&
                getMessagesIds().equals(that.getMessagesIds()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParticipantsIds(), getMessagesIds());
    }
}
