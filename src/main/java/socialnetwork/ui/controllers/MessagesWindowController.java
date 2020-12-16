package socialnetwork.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

import java.util.ArrayList;
import java.util.List;

public class MessagesWindowController extends AbstractWindowController {
    ObservableList<User> conversations = FXCollections.observableArrayList();
    ObservableList<Message> currentMessages = FXCollections.observableArrayList();
    List<Label> messages = new ArrayList<>();

    @FXML
    public TableView<User> conversationsTableView;
    @FXML
    public TableColumn<User, String> firstNameColumn;
    @FXML
    public TableColumn<User, String> lastNameColumn;
    @FXML
    public Button newConversationButton;
    @FXML
    public TextField messageTextBox;
    @FXML
    public Button sendReplyButton;
    @FXML
    public VBox chatBox;
    @FXML
    public ScrollPane chatContainer;

    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        conversationsTableView.setItems(conversations);
    }

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
    }

    public void openAllUsersMessageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/allUsersMessageWindow.fxml"));
            AnchorPane root = loader.load();

            AllUsersMessageWindowController controller = loader.getController();
            controller.init(service, loggedUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 396, 482));
            stage.setTitle("New conversation");
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void sendReply() {
    }

    @Override
    public void update() {
        List<Message> conversationsList = service.getConversations(loggedUser.getId());
        conversationsList.forEach(System.out::println);
        List<User> users = new ArrayList<>();
        for (Message message : conversationsList) {
            List<Long> allMessageUsersIds = new ArrayList<>(message.getTo());
            allMessageUsersIds.add(message.getFrom());
            List<User> allMessageUsers = new ArrayList<>();
            for (Long id : allMessageUsersIds) {
                allMessageUsers.add(service.getUser(id));
            }
            for (User user : allMessageUsers) {
                if (user != loggedUser)
                    users.add(user);
            }
        }
        conversations.setAll(users);

//        List<Message> messagesList = service.getConversation(loggedUser.getId(), )
        messages.add(new Label("I'm a message"));

        messages.get(0).setAlignment(Pos.CENTER_LEFT);
        messages.get(0).setStyle("-fx-border-color:grey; -fx-background-color:white;");
        messages.get(0).setPadding(new Insets(5,5,5,5));

        chatBox.getChildren().add(messages.get(0));
    }
}
