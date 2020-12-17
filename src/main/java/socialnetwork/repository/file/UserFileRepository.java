package socialnetwork.repository.file;

import socialnetwork.domain.Id;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class UserFileRepository extends AbstractFileRepository<User> {
    public UserFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                User entity = extractEntity(Arrays.asList(line.split(";")));
                super.save(entity);
            });
            lines.forEach(line -> {
                List<String> attributes = Arrays.asList(line.split(";"));
                List<User> friendsList = new ArrayList<>();
                String[] strList = attributes.get(3).replace("[", "").replace("]", "").split(",");
                for (String stringId : strList)
                    if (!stringId.equals(""))
                        friendsList.add(findOne(new Id(stringId)));
                findOne(new Id(attributes.get(0))).setFriends(friendsList);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(new Id(attributes.get(0)));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        StringBuilder strEntity = new StringBuilder(entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";[");
        if (entity.getFriendsIds().size() > 0) {
            strEntity.append(entity.getFriendsIds().get(0));
            for (int i = 1; i < entity.getFriendsIds().size(); i++)
                strEntity.append(",").append(entity.getFriendsIds().get(i));
        }
        strEntity.append("]");
        return strEntity.toString();
    }
}
