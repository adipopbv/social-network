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

public class LogInWindowController extends AbstractWindowController {

    @FXML
    public Button logInAcceptButton;
    @FXML
    public TextField logInUserIdField;
    @FXML
    public Button logInToSignUpButton;

    public void logIn() {
        try {
            String userIdStr = logInUserIdField.getText();
            long userId = Long.parseLong(userIdStr);
            User user = service.logInUser(userId);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/userWindow.fxml"));
            AnchorPane root = loader.load();

            UserWindowController controller = loader.getController();
            controller.init(service, user);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 471, 444));
            stage.setTitle(user.getFirstName() + " " + user.getLastName() + "'s home window");
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void toSignUp() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/signUpWindow.fxml"));
        AnchorPane root = loader.load();

        SignUpWindowController controller = loader.getController();
        controller.init(service, null);

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 270, 355));
        stage.setTitle("Sign Up!");
        stage.show();
    }

    @Override
    public void update() {

    }
}
