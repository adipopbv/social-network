package socialnetwork.domain;

import java.util.Objects;

public class Invite extends Entity<Long> {
    private final Long from;
    private final Long to;
    private InviteStatus status = InviteStatus.PENDING;

    public Invite(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    public Invite(Long from, Long to, InviteStatus status) {
        this.from = from;
        this.to = to;
        this.status = status;
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
