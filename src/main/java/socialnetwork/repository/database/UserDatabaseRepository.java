package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class UserDatabaseRepository extends AbstractDatabaseRepository<Long, User> {
    public UserDatabaseRepository(String url, String user, String password, Validator<User> validator) {
        super(url, user, password, validator);
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
                String[] friends = friendsStr.split(",");
                User user;
                if (friends.length > 0 && !friends[0].equals(""))
                    user = new User(firstName, lastName, Arrays.stream(friends).map(Long::parseLong).collect(Collectors.toCollection(Vector::new)));
                else
                    user = new User(firstName, lastName, new Vector<>());
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
            StringBuilder friends = new StringBuilder();
            if (!entity.getFriends().isEmpty())
                friends.append(entity.getFriends().get(0).toString());
            for (int index = 1; index < entity.getFriends().size(); index++)
                friends.append(",").append(entity.getFriends().get(index));
            statement.executeUpdate("insert into users(user_id, first_name, last_name, friends) " +
                    "values (" + entity.getId() + ", '" + entity.getFirstName() + "', '" + entity.getLastName() + "', '" + friends + "');");
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
                List<Long> friendsList;
                if (!data.getString("friends").split(",")[0].equals(""))
                    friendsList = Arrays.stream(data.getString("friends").split(",")).map(Long::parseLong).collect(Collectors.toCollection(Vector::new));
                else
                    friendsList = new Vector<>();
                if (!data.getString("first_name").equals(entities.get(id).getFirstName()) ||
                        !data.getString("last_name").equals(entities.get(id).getLastName()) ||
                        !friendsList.equals(entities.get(id).getFriends())) {
                    StringBuilder friends = new StringBuilder();
                    if (!entities.get(id).getFriends().isEmpty())
                        friends.append(entities.get(id).getFriends().get(0).toString());
                    for (int index = 1; index < entities.get(id).getFriends().size(); index++)
                        friends.append(",").append(entities.get(id).getFriends().get(index));
                    statement.executeUpdate("update users set first_name = '" + entities.get(id).getFirstName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set last_name = '" + entities.get(id).getLastName() + "' where user_id = " + id + ";");
                    statement.executeUpdate("update users set friends = '" + friends.toString() + "' where user_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
