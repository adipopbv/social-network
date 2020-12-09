package socialnetwork.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;

public class SignUpWindowController extends AbstractWindowController {

    @FXML
    public TextField signUpFirstNameField;
    @FXML
    public TextField signUpLastNameField;
    @FXML
    public Button signUpButton;

    public void signUp() {
        try {
            String firstName = signUpFirstNameField.getText();
            String lastName = signUpLastNameField.getText();

            User user = service.addUser(firstName, lastName);
            service.logInUser(user.getId());

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/userWindow.fxml"));
            AnchorPane root = loader.load();

            UserWindowController controller = loader.getController();
            controller.init(service, user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 700, 500));
            stage.setTitle(user.getFirstName() + " " + user.getLastName() + "'s home window");
            stage.show();
            ((Stage) signUpButton.getScene().getWindow()).close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

    }

    @Override
    public void update() {

    }
}
