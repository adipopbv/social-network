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

    public TableConversation(Conversation conversation, User loggedUser) {
        this.conversation = conversation;
        if (loggedUser != conversation.getParticipants().get(0)) {
            this.participantsNames = conversation.getParticipants().get(0).getFirstName() + " " + conversation.getParticipants().get(0).getLastName();
            for (int index = 1; index < conversation.getParticipants().size(); index++)
                if (loggedUser != conversation.getParticipants().get(index))
                    this.participantsNames += ", " + conversation.getParticipants().get(index).getFirstName() + " " + conversation.getParticipants().get(index).getLastName();
        } else {
            this.participantsNames = conversation.getParticipants().get(1).getFirstName() + " " + conversation.getParticipants().get(1).getLastName();
            for (int index = 2; index < conversation.getParticipants().size(); index++)
                this.participantsNames += ", " + conversation.getParticipants().get(index).getFirstName() + " " + conversation.getParticipants().get(index).getLastName();
        }
    }
}
