package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity {
    private final LocalDateTime date;
    private final User firstFriend;
    private final User secondFriend;

    public Friendship(User firstFriend, User secondFriend) {
        date = LocalDateTime.now();
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
    }

    public Friendship(LocalDateTime date, User firstFriend, User secondFriend) {
        this.date = date;
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
    }

    public User getFirstFriend() {
        return firstFriend;
    }

    public Id getFirstFriendId() {
        return getFirstFriend().getId();
    }

    public User getSecondFriend() {
        return secondFriend;
    }

    public Id getSecondFriendId() {
        return getSecondFriend().getId();
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + getId() + '\'' +
                ", date='" + getDate() + '\'' +
                ", firstFriend='" + getFirstFriendId() + '\'' +
                ", secondFriend='" + getSecondFriendId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return getDate().equals(that.getDate()) &&
                getFirstFriendId() == that.getFirstFriendId() &&
                getSecondFriendId() == that.getSecondFriendId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getFirstFriendId(), getSecondFriendId());
    }
}
