package socialnetwork.domain;

import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message {
    private Message replyTo;

    public ReplyMessage(User from, List<User> to, String message, Message replyTo) {
        super(from, to, message);
        this.replyTo = replyTo;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    @Override
    public String toString() {
        return "ReplyMessage{" +
                "replyTo=" + replyTo.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReplyMessage that = (ReplyMessage) o;
        return getReplyTo().equals(that.getReplyTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getReplyTo());
    }
}
