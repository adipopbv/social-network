package socialnetwork.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

import java.util.stream.Collectors;

public class UserWindowController extends AbstractWindowController {
    ObservableList<User> userFriends = FXCollections.observableArrayList();

    @FXML
    public Text userWindowTitleText;
    @FXML
    public Button logOutUserButton;
    @FXML
    public TableView<User> userFriendsTableView;
    @FXML
    public TableColumn<User, String> firstNameColumn;
    @FXML
    public TableColumn<User, String> lastNameColumn;
    @FXML
    public Button listAllUsersButton;
    @FXML
    public Button removeFriendButton;
    @FXML
    public Button listInvitesButton;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        userFriendsTableView.setItems(userFriends);
    }

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
        userWindowTitleText.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName() + ", welcome!");
    }

    public void logOut() {
        service.logOutUser(loggedUser.getId());
        ((Stage) logOutUserButton.getScene().getWindow()).close();
    }

    public void openAllUsersWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/allUsersWindow.fxml"));
            AnchorPane root = loader.load();

            AllUsersWindowController controller = loader.getController();
            controller.init(service, loggedUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 396, 482));
            stage.setTitle("All social network users");
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void removeFriend() {
        if (userFriendsTableView.getSelectionModel().getSelectedItem() != null) {
            service.removeFriend(loggedUser.getId(), userFriendsTableView.getSelectionModel().getSelectedItem().getId());
        }
    }

    public void openInvitesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/invitesWindow.fxml"));
            AnchorPane root = loader.load();

            InvitesWindowController controller = loader.getController();
            controller.init(service, loggedUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 592, 560));
            stage.setTitle("All user invites");
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    @Override
    public void update() {
        userFriends.setAll(service.getFriends(loggedUser.getId()));
    }
}
