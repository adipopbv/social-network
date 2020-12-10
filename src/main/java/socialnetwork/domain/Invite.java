package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Invite extends Entity<Long> {
    private final Long from;
    private final Long to;
    private InviteStatus status = InviteStatus.PENDING;
    private final LocalDateTime date;

    public Invite(Long from, Long to) {
        this.from = from;
        this.to = to;
        date = LocalDateTime.now();
    }

    public Invite(Long from, Long to, InviteStatus status, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "\n from=" + from +
                ",\n status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invite invite = (Invite) o;
        return from.equals(invite.from) &&
                to.equals(invite.to) &&
                status == invite.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, status);
    }
}
