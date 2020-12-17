package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Id;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.repository.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Friendship>  {
    protected Repository<User> userRepository;

    public FriendshipDatabaseRepository(String url, String user, String password, Repository<User> userRepository) {
        super(url, user, password);
        this.userRepository = userRepository;
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from friendships;");
            while (data.next()) {
                Id id = new Id(data.getInt("friendship_id"));
                User firstFriend = userRepository.findOne(new Id(data.getInt("first_friend_id")));
                User secondFriend = userRepository.findOne(new Id(data.getInt("second_friend_id")));
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();
                Friendship friendship = new Friendship(date, firstFriend, secondFriend);
                friendship.setId(id);

                entities.putIfAbsent(friendship.getId(), friendship);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load friendships database");
        }
    }

    @Override
    protected void addToDatabase(Friendship entity) {
        try {
            statement.executeUpdate("insert into friendships(friendship_id, first_friend_id, second_friend_id, date) " +
                    "values (" + entity.getId() + ", '" + entity.getFirstFriendId() + "', '" + entity.getSecondFriendId() + "', '" + Timestamp.valueOf(entity.getDate()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Id id) {
        try {
            statement.executeUpdate("delete from friendships where friendship_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }
}
