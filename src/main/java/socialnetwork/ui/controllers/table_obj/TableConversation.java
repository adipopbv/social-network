package socialnetwork.ui.controllers.table_obj;

import socialnetwork.domain.*;

import java.util.List;
import java.util.stream.Collectors;

public class TableConversation {
    private String participantsNames;
    private final Conversation conversation;

    public String getParticipantsNames() {
        return participantsNames;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public TableConversation(Conversation conversation) {
        this.conversation = conversation;
        this.participantsNames = conversation.getParticipants().get(0).getFirstName() + " " + conversation.getParticipants().get(0).getLastName();
        for (int index = 1; index < conversation.getParticipants().size(); index++)
            this.participantsNames += ", " + conversation.getParticipants().get(index).getFirstName() + " " + conversation.getParticipants().get(index).getLastName();
    }
}
