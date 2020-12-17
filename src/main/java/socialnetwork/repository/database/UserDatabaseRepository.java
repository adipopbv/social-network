package socialnetwork.repository.database;

import socialnetwork.domain.Id;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseRepository extends AbstractDatabaseRepository<User> {
    public UserDatabaseRepository(String url, String user, String password) {
        super(url, user, password);
        loadData();
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from users;");
            while (data.next()) {
                Id id = new Id(data.getInt("user_id"));
                String firstName = data.getString("first_name");
                String lastName = data.getString("last_name");
                User user = new User(firstName, lastName);
                user.setId(id);

                entities.putIfAbsent(user.getId(), user);
            }
            data.close();
            data = statement.executeQuery("select * from users;");
            while (data.next()) {
                Id id = new Id(data.getInt("user_id"));
                String friendsStr = data.getString("friends");

                List<User> friendsList = new ArrayList<>();
                for (Id userId : idStringToIdList(friendsStr))
                    friendsList.add(findOne(userId));
                findOne(id).setFriends(friendsList);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load users database");
        }
    }

    @Override
    protected void addToDatabase(User entity) {
        try {
            statement.executeUpdate("insert into users(user_id, first_name, last_name, friends) " +
                    "values (" + entity.getId() + ", '" + entity.getFirstName() + "', '" + entity.getLastName() + "', '" + idListToIdString(entity.getFriendsIds()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Id id) {
        try {
            statement.executeUpdate("delete from users where user_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateInDatabase(User entity) {
        try {
            statement.executeUpdate("update users " +
                    "set first_name = '" + entity.getFirstName() + "', last_name = '" + entity.getLastName() + "', friends = '" + idListToIdString(entity.getFriendsIds()) + "' " +
                    "where user_id = " + entity.getId() + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from users");
            while (data.next()) {
                Id id = new Id(data.getInt("user_id"));
                List<Id> friendsList = idStringToIdList(data.getString("friends"));
                if (!data.getString("first_name").equals(entities.get(id).getFirstName()) ||
                        !data.getString("last_name").equals(entities.get(id).getLastName()) ||
                        !friendsList.equals(entities.get(id).getFriendsIds())) {
                    statement.executeUpdate("update users set first_name = '" + entities.get(id).getFirstName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set last_name = '" + entities.get(id).getLastName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set friends = '" + idListToIdString(entities.get(id).getFriendsIds()) + "' where user_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
