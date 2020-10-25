package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;

import javax.swing.*;
import java.util.Random;

public class Service {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final UserValidator userValidator;
    private final FriendshipValidator friendshipValidator;

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        userValidator = new UserValidator();
        friendshipValidator = new FriendshipValidator();
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepository.findAll();
    }

    public User addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        userValidator.validate(user);
        Random random = new Random();
        do {
            user.setId((long) (random.nextInt(9000) + 1000));
        } while (userRepository.save(user) != null);
        return user;
    }

    public User removeUser(long id) {
        User user = userRepository.delete(id);

        for (Friendship friendship : friendshipRepository.findAll())
            if (user.getId() == friendship.getFriend1() || user.getId() == friendship.getFriend2())
                friendshipRepository.delete(friendship.getId());
        for (User userSearch : userRepository.findAll())
            userSearch.getFriends().remove(id);

        return user;
    }

    public Friendship addFriendship(long id1, long id2) {
        Friendship friendship = new Friendship(id1, id2);
        friendshipValidator.validate(friendship);
        Random random = new Random();
        do {
            friendship.setId((long) (random.nextInt(9000) + 1000));
        } while (friendshipRepository.save(friendship) != null);

        User user1 = userRepository.findOne(id1), user2 = userRepository.findOne(id2);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());

        return friendship;
    }

    public Friendship removeFriendship(long id) {
        Friendship friendship = friendshipRepository.delete(id);

        User user1 = userRepository.findOne(friendship.getFriend1()), user2 = userRepository.findOne(friendship.getFriend2());
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());

        return friendship;
    }

    public void close() {
        userRepository.close();
        friendshipRepository.close();
    }
}
