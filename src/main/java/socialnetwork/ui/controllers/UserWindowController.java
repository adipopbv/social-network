package socialnetwork.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

public class UserWindowController extends AbstractWindowController {

    @FXML
    public Text userWindowTitleText;
    @FXML
    public Button logOutUserButton;

    @Override
    public void init(SocialNetworkService service, User loggedUser) {
        super.init(service, loggedUser);
        userWindowTitleText.setText(loggedUser.getFirstName() + " " + loggedUser.getLastName() + ", welcome!");
    }

    public void logOut() {
        service.logOutUser(loggedUser.getId());
        ((Stage) logOutUserButton.getScene().getWindow()).close();
    }
}
