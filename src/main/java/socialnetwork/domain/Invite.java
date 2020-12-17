package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Invite extends Entity {
    private final User from;
    private final User to;
    private InviteStatus status;
    private final LocalDateTime date;

    public Invite(User from, User to) {
        this.from = from;
        this.to = to;
        this.status = InviteStatus.PENDING;
        date = LocalDateTime.now();
    }

    public Invite(User from, User to, InviteStatus status, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public Id getFromId() {
        return from.getId();
    }

    public User getTo() {
        return to;
    }

    public Id getToId() {
        return to.getId();
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

    public static String statusToString(InviteStatus status) {
        switch(status) {
            case PENDING: return "PENDING";
            case APPROVED: return "APPROVED";
            case REJECTED: return "REJECTED";
            case CANCELLED: return "CANCELLED";
            default: return null;
        }
    }

    public static InviteStatus stringToStatus(String status) {
        switch (status) {
            case "PENDING": return InviteStatus.PENDING;
            case "APPROVED": return InviteStatus.APPROVED;
            case "REJECTED": return InviteStatus.REJECTED;
            case "CANCELLED": return InviteStatus.CANCELLED;
            default: return null;
        }
    }

    @Override
    public String toString() {
        return "Invite{" +
                "\n from=" + from +
                ",\n status=" + status +
                ",\n date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invite invite = (Invite) o;
        return from.equals(invite.from) &&
                to.equals(invite.to) &&
                status == invite.status &&
                date.equals(invite.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, status, date);
    }
}
