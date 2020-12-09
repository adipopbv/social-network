package socialnetwork.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

import java.util.List;

public class AllUsersWindowController extends AbstractWindowController {
    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public TableView<User> usersTableView;
    @FXML
    public TableColumn<User, String> firstNameColumn;
    @FXML
    public TableColumn<User, String> lastNameColumn;
    @FXML
    public Button sendInviteButton;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        usersTableView.setItems(users);
    }

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
    }

    public void sendInvite() {
        try {
            if (usersTableView.getSelectionModel().getSelectedItem() != null) {
                service.sendInvite(loggedUser.getId(), usersTableView.getSelectionModel().getSelectedItem().getId());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation successful!");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    @Override
    public void update() {
        List<User> usersList = service.getAllUsers();
        usersList.remove(loggedUser);
        users.setAll(usersList);
    }
}
