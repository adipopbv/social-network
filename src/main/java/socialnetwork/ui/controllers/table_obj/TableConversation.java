package socialnetwork.ui.controllers.table_obj;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class TableConversation {
    private final List<Long> participantsIds;
    private String participants;
    private final Long conversationId;

    public List<Long> getParticipantsIds() {
        return participantsIds;
    }

    public String getParticipants() {
        return participants;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public TableConversation(List<User> participants, Message firstMessage) {
        this.participantsIds = participants.stream().map(Entity::getId).collect(Collectors.toList());
        this.participants = participants.get(0).getFirstName() + " " + participants.get(0).getLastName();
        for (int index = 1; index < participants.size(); index++)
            this.participants += ", " + participants.get(index).getFirstName() + " " + participants.get(index).getLastName();
        this.conversationId = firstMessage.getId();
    }
}
