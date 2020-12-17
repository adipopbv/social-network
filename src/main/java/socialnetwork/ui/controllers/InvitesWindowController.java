package socialnetwork.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.Invite;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.ui.controllers.table_obj.TableInvite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvitesWindowController extends AbstractWindowController {
    ObservableList<TableInvite> toUsers = FXCollections.observableArrayList();
    ObservableList<TableInvite> fromUsers = FXCollections.observableArrayList();

    @FXML
    public TableView<TableInvite> toUsersTableView;
    @FXML
    public TableColumn<TableInvite, String> firstNameColumn;
    @FXML
    public TableColumn<TableInvite, String> lastNameColumn;
    @FXML
    public TableColumn<TableInvite, String> statusColumn;
    @FXML
    public TableColumn<TableInvite, String> dateColumn;
    @FXML
    public TableView<TableInvite> fromUsersTableView;
    @FXML
    public TableColumn<TableInvite, String> firstNameColumn1;
    @FXML
    public TableColumn<TableInvite, String> lastNameColumn1;
    @FXML
    public TableColumn<TableInvite, String> statusColumn1;
    @FXML
    public TableColumn<TableInvite, String> dateColumn1;
    @FXML
    public Button acceptInviteButton;
    @FXML
    public Button rejectInviteButton;

    @FXML
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("lastName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("date"));
        firstNameColumn1.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("firstName"));
        lastNameColumn1.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("lastName"));
        statusColumn1.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("status"));
        dateColumn1.setCellValueFactory(new PropertyValueFactory<TableInvite, String>("date"));
        toUsersTableView.setItems(toUsers);
        fromUsersTableView.setItems(fromUsers);
    }

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
    }

    @Override
    public void update() {
        List<Invite> invites = service.getInvites(loggedUser.getId());
        List<Invite> receivedInvites = invites.stream().filter(invite -> invite.getTo().equals(loggedUser)).collect(Collectors.toList());
        List<TableInvite> fromUsersList = new ArrayList<>();
        for (Invite invite : receivedInvites) {
            fromUsersList.add(new TableInvite(service.getUser(invite.getFrom().getId()), invite));
        }
        List<Invite> sentInvites = invites.stream().filter(invite -> invite.getFrom().equals(loggedUser)).collect(Collectors.toList());
        List<TableInvite> toUsersList = new ArrayList<>();
        for (Invite invite : sentInvites) {
            toUsersList.add(new TableInvite(service.getUser(invite.getTo().getId()), invite));
        }
        fromUsers.setAll(fromUsersList);
        toUsers.setAll(toUsersList);
    }

    public void acceptInvite() {
        try {
            if (fromUsersTableView.getSelectionModel().getSelectedItem() != null) {
                service.acceptInvite(loggedUser.getId(), fromUsersTableView.getSelectionModel().getSelectedItem().getInvite().getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation successful!");
                alert.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void rejectInvite() {
        try {
            if (fromUsersTableView.getSelectionModel().getSelectedItem() != null) {
                service.rejectInvite(loggedUser.getId(), fromUsersTableView.getSelectionModel().getSelectedItem().getInvite().getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation successful!");
                alert.show();
            } else if (toUsersTableView.getSelectionModel().getSelectedItem() != null) {
                service.rejectInvite(loggedUser.getId(), toUsersTableView.getSelectionModel().getSelectedItem().getInvite().getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Operation successful!");
                alert.show();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }
}
