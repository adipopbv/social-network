package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Long, Friendship>  {
    public FriendshipDatabaseRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from friendships;");
            while (data.next()) {
                long id = data.getInt("friendship_id");
                long id1 = data.getInt("friend_1_id");
                long id2 = data.getInt("friend_2_id");
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();
                Friendship friendship = new Friendship(date, id1, id2);
                friendship.setId(id);

                entities.putIfAbsent(friendship.getId(), friendship);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load database");
        }
    }

    @Override
    protected void addToDatabase(Friendship entity) {
        try {
            statement.executeUpdate("insert into friendships(friendship_id, friend_1_id, friend_2_id, date) " +
                    "values (" + entity.getId() + ", '" + entity.getFriend1() + "', '" + entity.getFriend2() + "', '" + Timestamp.valueOf(entity.getDate()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Long id) {
        try {
            statement.executeUpdate("delete from friendships where friendship_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }
}
