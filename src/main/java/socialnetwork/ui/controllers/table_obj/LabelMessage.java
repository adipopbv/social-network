package socialnetwork.ui.controllers.table_obj;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;

public class LabelMessage extends Label {
    private final Message message;
    public String style;

    public Message getMessage() {
        return message;
    }

    public LabelMessage(Message message, User loggedUser) {
        this.message = message;

        if (message.getOriginal() != null)
            setText("[" + message.getOriginal().getText() + "] " + message.getText());
        else
            setText(message.getText());

        if (message.getFrom().equals(loggedUser)) {
            setAlignment(Pos.CENTER_RIGHT);
            style = "-fx-border-color: grey; -fx-background-color: #adadad; -fx-pref-width: 250px;";
        }
        else {
            setAlignment(Pos.CENTER_LEFT);
            style = "-fx-border-color: grey; -fx-background-color: white; -fx-pref-width: 250px;";
        }

        setStyle(style);
        setPadding(new Insets(5, 5, 5, 5));
        setWrapText(true);
    }
}
