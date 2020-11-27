package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.util.List;

public class UserDatabaseRepository extends AbstractDatabaseRepository<Long, User> {
    public UserDatabaseRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from users;");
            while (data.next()) {
                long id = data.getInt("user_id");
                String firstName = data.getString("first_name");
                String lastName = data.getString("last_name");
                String friendsStr = data.getString("friends");
                User user = new User(firstName, lastName, dbStringToList(friendsStr));
                user.setId(id);

                entities.putIfAbsent(user.getId(), user);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load database");
        }
    }

    @Override
    protected void addToDatabase(User entity) {
        try {
            statement.executeUpdate("insert into users(user_id, first_name, last_name, friends) " +
                    "values (" + entity.getId() + ", '" + entity.getFirstName() + "', '" + entity.getLastName() + "', '" + listToDbString(entity.getFriends()) + "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Long id) {
        try {
            statement.executeUpdate("delete from users where user_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from users");
            while (data.next()) {
                long id = data.getInt("user_id");
                List<Long> friendsList = dbStringToList(data.getString("friends"));
                if (!data.getString("first_name").equals(entities.get(id).getFirstName()) ||
                        !data.getString("last_name").equals(entities.get(id).getLastName()) ||
                        !friendsList.equals(entities.get(id).getFriends())) {
                    statement.executeUpdate("update users set first_name = '" + entities.get(id).getFirstName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set last_name = '" + entities.get(id).getLastName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set friends = '" + listToDbString(entities.get(id).getFriends()) + "' where user_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
