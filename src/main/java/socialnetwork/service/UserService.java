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

    public Iterable<User> getAll(){
        return repo.findAll();
    }
}
