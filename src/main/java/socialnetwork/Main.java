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
        String dbUrl = "jdbc:postgresql://localhost:5432/social_network";
        String user = "adipopbv", password = "adipopbv";
        Repository<Long, User> userRepository = new UserDatabaseRepository(dbUrl, user, password);
        Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(dbUrl, user, password);
        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(dbUrl, user, password);
        Repository<Long, Invite> inviteRepository = new InviteDatabaseRepository(dbUrl, user, password);
        Service service = new Service(userRepository, friendshipRepository, messageRepository, inviteRepository);
        Client client = new TuiClient(service);

        client.run();
    }
}
