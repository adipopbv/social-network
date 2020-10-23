package socialnetwork;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFileRepository;
import socialnetwork.service.UserService;
import socialnetwork.ui.UserClient;

public class Main {
    public static void main(String[] args) {
        Repository<Long, User> userRepository = new UserFileRepository("data/users.csv", new UserValidator());
        UserService userService = new UserService(userRepository);
        UserClient userClient = new UserClient(userService);

        userClient.run();
    }
}


