package socialnetwork.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

public class LogInWindowController extends AbstractWindowController {

    @FXML
    public Button loginAcceptButton;
    @FXML
    public TextField loginUserIdField;
    @FXML
    public Button loginToSignUpButton;

    public void logIn(ActionEvent actionEvent) throws Exception {
        try {
            String userIdStr = loginUserIdField.getText();
            long userId = Long.parseLong(userIdStr);
            User user = service.logInUser(userId);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/userWindow.fxml"));
            AnchorPane root = loader.load();

            UserWindowController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 700, 500));
            stage.setTitle(user.getFirstName() + " " + user.getLastName() + "'s home window");
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void toSignUp(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/signUpWindow.fxml"));
        AnchorPane root = loader.load();

        SignUpWindowController controller = loader.getController();
        controller.setService(service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Sign Up!");
        stage.show();
    }
}
