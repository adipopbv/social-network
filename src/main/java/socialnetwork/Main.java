package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Invite;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.InviteValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDatabaseRepository;
import socialnetwork.repository.database.InviteDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;
import socialnetwork.repository.database.UserDatabaseRepository;
import socialnetwork.service.Service;
import socialnetwork.ui.Client;

public class Main {
    public static void main(String[] args) {
        Repository<Long, User> userRepository = new UserDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        Repository<Long, Message> messageRepository = new MessageDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        Repository<Long, Invite> inviteRepository = new InviteDatabaseRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "postgres");
        Service service = new Service(userRepository, friendshipRepository, messageRepository, inviteRepository);
        Client client = new Client(service);

        client.run();
    }
}
