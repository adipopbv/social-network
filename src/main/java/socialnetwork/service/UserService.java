package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;

import java.util.Random;

public class UserService {
    private final Repository<Long, User> repo;
    private final UserValidator userValidator;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
        userValidator = new UserValidator();
    }

    public User addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        userValidator.validate(user);
        Random random = new Random();
        do {
            user.setId((long) (random.nextInt(9000) + 1000));
        } while (repo.save(user) != null);
        return user;
    }

    public User addFriend(long id1, long id2) {
        User user = repo.findOne(id1);
        user.getFriends().add(repo.findOne(id2));
        return user;
    }

    public User removeFriend(long id1, long id2) {
        User user = repo.findOne(id1);
        user.getFriends().remove(repo.findOne(id2));
        return user;
    }

    public User removeUser(long id) {
        return repo.delete(id);
    }

    public Iterable<User> getAll(){
        return repo.findAll();
    }
}
