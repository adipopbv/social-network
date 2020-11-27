package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Invite;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.service.Service;
import socialnetwork.ui.*;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/SocialNetwork";
        Repository<Long, User> userRepository = new UserDatabaseRepository(dbUrl, "postgres", "postgres");
        Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(dbUrl, "postgres", "postgres");
        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(dbUrl, "postgres", "postgres");
        Repository<Long, Invite> inviteRepository = new InviteDatabaseRepository(dbUrl, "postgres", "postgres");
        Service service = new Service(userRepository, friendshipRepository, messageRepository, inviteRepository);
        Client client = new TuiClient(service);

        client.run();
    }
}
