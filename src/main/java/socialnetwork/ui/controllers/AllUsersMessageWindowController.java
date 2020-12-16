package socialnetwork.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Entity;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

import java.util.List;
import java.util.stream.Collectors;

public class AllUsersMessageWindowController extends AbstractWindowController {
    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public TableView<User> usersTableView;
    @FXML
    public TableColumn<User, String> firstNameColumn;
    @FXML
    public TableColumn<User, String> lastNameColumn;
    @FXML
    public Button sendMessageButton;
    @FXML
    public TextField messageTextField;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        usersTableView.setItems(users);
        usersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
    }

    public void sendMessage() {
        try {
            if (usersTableView.getSelectionModel().getSelectedItems() != null) {
                List<Long> usersIds = usersTableView.getSelectionModel().getSelectedItems().stream().map(Entity::getId).collect(Collectors.toList());
                service.sendMessage(loggedUser.getId(), usersIds, messageTextField.getText());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
        ((Stage) sendMessageButton.getScene().getWindow()).close();
    }

    @Override
    public void update() {
        List<User> usersList = service.getAllUsers();
        usersList.remove(loggedUser);
        users.setAll(usersList);
    }
}
