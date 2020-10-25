package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    protected final LocalDateTime date;
    protected final long id1;
    protected final long id2;

    public Friendship(long id1, long id2) {
        date = LocalDateTime.now();
        this.id1 = id1;
        this.id2 = id2;
    }

    public Friendship(LocalDateTime date, long id1, long id2) {
        this.date = date;
        this.id1 = id1;
        this.id2 = id2;
    }

    public long getFriend1() {
        return id1;
    }

    public long getFriend2() {
        return id2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + getId() + '\'' +
                ", date='" + getDate() + '\'' +
                ", friend1='" + getFriend1() + '\'' +
                ", friend2='" + getFriend2() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return getDate().equals(that.getDate()) &&
                getFriend1() == that.getFriend1() &&
                getFriend2() == that.getFriend2();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getFriend1(), getFriend2());
    }
}
