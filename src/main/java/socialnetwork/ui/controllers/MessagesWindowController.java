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
import socialnetwork.domain.Conversation;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.ui.controllers.table_obj.LabelMessage;
import socialnetwork.ui.controllers.table_obj.TableConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesWindowController extends AbstractWindowController {
    ObservableList<TableConversation> conversations = FXCollections.observableArrayList();
    TableConversation selectedConversation = null;

    @FXML
    public TableView<TableConversation> conversationsTableView;
    @FXML
    public TableColumn<TableConversation, String> participantsColumn;
    @FXML
    public Button newConversationButton;
    @FXML
    public TextField messageTextField;
    @FXML
    public Button sendMessageButton;
    @FXML
    public VBox chatBox;
    @FXML
    public ScrollPane chatContainer;

    public void initialize() {
        participantsColumn.setCellValueFactory(new PropertyValueFactory<TableConversation, String>("participantsNames"));
        conversationsTableView.setItems(conversations);
        conversationsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedConversation = newSelection;
                updateChat();
            }
        });
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

    public void sendMessage() {
        try {
            if (selectedConversation != null)
                service.sendMessage(selectedConversation.getConversation().getId(), loggedUser.getId(), messageTextField.getText());
            updateChat();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
        messageTextField.clear();
    }

    @Override
    public void update() {
        List<TableConversation> conversationsList = new ArrayList<>();
        for (Conversation conversation : service.getConversations(loggedUser.getId()))
            conversationsList.add(new TableConversation(conversation));
        conversations.setAll(conversationsList);
    }

    private void updateChat() {
        chatBox.getChildren().clear();
        if (selectedConversation != null) {
            List<Message> conversationMessages = new ArrayList<>(service.getConversationMessages(selectedConversation.getConversation().getId()));
            for (Message message : conversationMessages) {
                LabelMessage messageLabel = new LabelMessage(message);
                if (message.getFrom().equals(loggedUser))
                    messageLabel.setAlignment(Pos.CENTER_LEFT);
                else
                    messageLabel.setAlignment(Pos.CENTER_RIGHT);
//                messageLabel.setOnMouseClicked((event -> {
//
//                }));

                chatBox.getChildren().add(messageLabel);
            }
        }
    }
}
