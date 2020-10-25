package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFileRepository;
import socialnetwork.repository.file.UserFileRepository;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.UserService;
import socialnetwork.ui.Client;

public class Main {
    public static void main(String[] args) {
        Repository<Long, User> userRepository = new UserFileRepository("data/users.csv", new UserValidator());
        Repository<Long, Friendship> friendshipRepository = new FriendshipFileRepository("data/friendships.csv", new FriendshipValidator());
        UserService userService = new UserService(userRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);
        Client client = new Client(userService, friendshipService);

        client.run();
    }
}


