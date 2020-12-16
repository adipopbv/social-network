package socialnetwork.ui.controllers.table_obj;

import socialnetwork.domain.Invite;
import socialnetwork.domain.User;

public class TableInvite {
    private final String firstName;
    private final String lastName;
    private final String status;
    private final String date;
    private final Invite invite;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public Invite getInvite() {
        return invite;
    }

    public TableInvite(User user, Invite invite) {
        this.invite = invite;
        firstName = user.getFirstName();
        lastName = user.getLastName();
        switch (invite.getStatus()) {
            case PENDING: status = "PENDING"; break;
            case APPROVED: status = "APPROVED"; break;
            case REJECTED: status = "REJECTED"; break;
            default: status = null; break;
        }
        date = invite.getDate().toString();
    }
}
