package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.util.List;
import java.util.Vector;

public class UserFileRepository extends AbstractFileRepository<Long, User>{
    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        List<Long> list = new Vector<>();
        String[] strList = attributes.get(3).replace("[", "").replace("]", "").split(",");
        for (String s : strList)
            if (!s.equals(""))
                list.add(Long.parseLong(s));
        User user = new User(attributes.get(1), attributes.get(2), list);
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        StringBuilder strEntity = new StringBuilder(entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";[");
        if (entity.getFriends().size() > 0) {
            strEntity.append(entity.getFriends().get(0));
            for (int i = 1; i < entity.getFriends().size(); i++)
                strEntity.append(",").append(entity.getFriends().get(i));
        }
        strEntity.append("]");
        return strEntity.toString();
    }
}
