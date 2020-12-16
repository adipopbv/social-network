package socialnetwork.ui.controllers.table_obj;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import socialnetwork.domain.Message;

public class LabelMessage extends Label {
    private final Message message;

    public LabelMessage(Message message) {
//        if (message.isReply())
//            super("Replying to: " + message.getOriginal().getMessage() + "\n\n" + message.getMessage());
        super(message.getMessage());
        this.message = message;
        setStyle("-fx-border-color:grey; -fx-background-color:white;");
        setPadding(new Insets(5, 5, 5, 5));
    }
}
