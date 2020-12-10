package socialnetwork.repository.database;

import socialnetwork.domain.Invite;
import socialnetwork.domain.InviteStatus;
import socialnetwork.domain.exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InviteDatabaseRepository extends AbstractDatabaseRepository<Long, Invite> {
    public InviteDatabaseRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from invites;");
            while (data.next()) {
                long id = data.getInt("invite_id");
                long from = data.getInt("from_id");
                long to = data.getInt("to_id");
                InviteStatus status = stringToStatus(data.getString("status"));
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();
                Invite invite = new Invite(from, to, status, date);
                invite.setId(id);

                entities.putIfAbsent(invite.getId(), invite);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load database");
        }
    }

    @Override
    protected void addToDatabase(Invite entity) {
        try {
            statement.executeUpdate("insert into invites(invite_id, from_id, to_id, status, date) " +
                    "values (" + entity.getId() + ", " + entity.getFrom() + ", " + entity.getTo() + ", '" + statusToString(entity.getStatus()) + "', '" + Timestamp.valueOf(entity.getDate()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Long id) {
        try {
            statement.executeUpdate("delete from invites where invite_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateInDatabase(Invite entity) {
        try {
            statement.executeUpdate("update invites " +
                    "set from_id = '" + entity.getFrom() + "', to_id = '" + entity.getTo() + "', status = '" + statusToString(entity.getStatus()) + "' " +
                    "where invite_id = " + entity.getId() + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from invites");
            while (data.next()) {
                long id = data.getInt("invite_id");
                InviteStatus status = stringToStatus(data.getString("status"));
                if (data.getInt("from_id") != entities.get(id).getFrom() ||
                        data.getInt("to_id") != entities.get(id).getTo() ||
                        !status.equals(entities.get(id).getStatus())) {
                    statement.executeUpdate("update invites set from_id = " + entities.get(id).getFrom() + " where invite_id = " + id + ";");
                    statement.executeUpdate("update invites set to_id = " + entities.get(id).getTo() + " where invite_id = " + id + ";");
                    statement.executeUpdate("update invites set status = '" + statusToString(entities.get(id).getStatus()) + "' where invite_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }

    private InviteStatus stringToStatus(String statusStr) {
        InviteStatus status;
        switch (statusStr) {
            case "APPROVED": status = InviteStatus.APPROVED; break;
            case "REJECTED": status = InviteStatus.REJECTED; break;
            default: status = InviteStatus.PENDING;
        }
        return status;
    }

    private String statusToString(InviteStatus status) {
        String statusStr;
        switch (status) {
            case APPROVED: statusStr = "APPROVED"; break;
            case REJECTED: statusStr = "REJECTED"; break;
            default: statusStr = "PENDING";
        }
        return statusStr;
    }
}
