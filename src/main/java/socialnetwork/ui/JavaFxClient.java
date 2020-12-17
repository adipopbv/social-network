package socialnetwork.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Invite;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDatabaseRepository;
import socialnetwork.repository.database.InviteDatabaseRepository;
import socialnetwork.repository.database.MessageDatabaseRepository;
import socialnetwork.repository.database.UserDatabaseRepository;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.ui.controllers.LogInWindowController;

public class JavaFxClient extends Application implements SocialNetworkClient{
    final SocialNetworkService service;

    public JavaFxClient() {
        String dbUrl = "jdbc:postgresql://localhost:5432/socialnetwork";
        String user = "adipopbv", password = "adipopbv";
        Repository<User> userRepository = new UserDatabaseRepository(dbUrl, user, password);
        Repository<Friendship> friendshipRepository = new FriendshipDatabaseRepository(dbUrl, user, password);
        Repository<Message> messageRepository = new MessageDatabaseRepository(dbUrl, user, password);
        Repository<Invite> inviteRepository = new InviteDatabaseRepository(dbUrl, user, password);
        service = new SocialNetworkService(userRepository, friendshipRepository, messageRepository, inviteRepository);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/logInWindow.fxml"));
        AnchorPane root = loader.load();

        LogInWindowController controller = loader.getController();
        controller.init(service, null);

        primaryStage.setScene(new Scene(root, 270, 355));
        primaryStage.setTitle("Log In!");
        primaryStage.show();
    }

    @Override
    public void run(String[] args) {
        main(args);
    }

   public static void main(String[] args) {
        launch(args);
    }
}
