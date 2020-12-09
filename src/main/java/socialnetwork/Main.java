package socialnetwork;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Invite;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.ui.*;

public class Main {
    public static void main(String[] args) {
//        String dbUrl = "jdbc:postgresql://localhost:5432/social_network";
//        String user = "adipopbv", password = "adipopbv";
//        Repository<Long, User> userRepository = new UserDatabaseRepository(dbUrl, user, password);
//        Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(dbUrl, user, password);
//        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(dbUrl, user, password);
//        Repository<Long, Invite> inviteRepository = new InviteDatabaseRepository(dbUrl, user, password);
//        SocialNetworkService service = new SocialNetworkService(userRepository, friendshipRepository, messageRepository, inviteRepository);
//        SocialNetworkClient client = new JavaFxClient(service);

//        client.run(args);

        JavaFxClient.main(args);
    }
}
