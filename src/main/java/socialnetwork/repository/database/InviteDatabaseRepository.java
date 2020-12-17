package socialnetwork.repository.database;

import socialnetwork.domain.Id;
import socialnetwork.domain.Invite;
import socialnetwork.domain.InviteStatus;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.repository.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InviteDatabaseRepository extends AbstractDatabaseRepository<Invite> {
    protected Repository<User> userRepository;

    public InviteDatabaseRepository(String url, String user, String password, Repository<User> userRepository) {
        super(url, user, password);
        this.userRepository = userRepository;
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from invites;");
            while (data.next()) {
                Id id = new Id(data.getInt("invite_id"));
                User from = userRepository.findOne(new Id(data.getInt("from_id")));
                User to = userRepository.findOne(new Id(data.getInt("to_id")));
                InviteStatus status = Invite.stringToStatus(data.getString("status"));
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();
                Invite invite = new Invite(from, to, status, date);
                invite.setId(id);

                entities.putIfAbsent(invite.getId(), invite);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load invites database");
        }
    }

    @Override
    protected void addToDatabase(Invite entity) {
        try {
            statement.executeUpdate("insert into invites(invite_id, from_id, to_id, status, date) " +
                    "values (" + entity.getId() + ", " + entity.getFromId() + ", " + entity.getToId() + ", '" + Invite.statusToString(entity.getStatus()) + "', '" + Timestamp.valueOf(entity.getDate()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Id id) {
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
                    "set from_id = '" + entity.getFromId() + "', to_id = '" + entity.getToId() + "', status = '" + Invite.statusToString(entity.getStatus()) + "' " +
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
                Id id = new Id(data.getInt("invite_id"));
                InviteStatus status = Invite.stringToStatus(data.getString("status"));
                if (data.getInt("from_id") != entities.get(id).getFromId().getValue() ||
                        data.getInt("to_id") != entities.get(id).getToId().getValue() ||
                        !status.equals(entities.get(id).getStatus())) {
                    statement.executeUpdate("update invites set from_id = " + entities.get(id).getFromId() + " where invite_id = " + id + ";");
                    statement.executeUpdate("update invites set to_id = " + entities.get(id).getToId() + " where invite_id = " + id + ";");
                    statement.executeUpdate("update invites set status = '" + Invite.statusToString(entities.get(id).getStatus()) + "' where invite_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
