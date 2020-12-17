package socialnetwork.repository.database;

import socialnetwork.domain.Conversation;
import socialnetwork.domain.Id;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.repository.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConversationDatabaseRepository extends AbstractDatabaseRepository<Conversation> {
    protected Repository<User> userRepository;
    protected Repository<Message> messageRepository;

    public ConversationDatabaseRepository(String dbUrl, String user, String password, Repository<User> userRepository, Repository<Message> messageRepository) {
        super(dbUrl, user, password);
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from conversations;");
            while (data.next()) {
                Id id = new Id(data.getInt("conversation_id"));
                List<User> participants = new ArrayList<>();
                for (Id userId: idStringToIdList(data.getString("participants_ids")))
                    participants.add(userRepository.findOne(userId));
                List<Message> messages = new ArrayList<>();
                for (Id messageId: idStringToIdList(data.getString("messages_ids")))
                    messages.add(messageRepository.findOne(messageId));

                Conversation conversation = new Conversation(participants, messages);
                conversation.setId(id);

                entities.putIfAbsent(conversation.getId(), conversation);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load conversations database");
        }
    }

    @Override
    protected void addToDatabase(Conversation entity) {
        try {
            statement.executeUpdate("insert into conversations(conversation_id, participants_ids, messages_ids) " +
                    "values (" + entity.getId() +
                    ", '" + idListToIdString(entity.getParticipantsIds()) +
                    "', '" + idListToIdString(entity.getMessagesIds()) +
                    "');");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Id id) {
        try {
            statement.executeUpdate("delete from conversations where conversation_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateInDatabase(Conversation entity) {
        try {
            statement.executeUpdate("update conversations " +
                    "set " +
                    "participants_ids = '" + idListToIdString(entity.getParticipantsIds()) +
                    "', messages_ids = '" + idListToIdString(entity.getMessagesIds()) +
                    "' where conversation_id = " + entity.getId() + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from conversations");
            while (data.next()) {
                Id id = new Id(data.getInt("conversation_id"));
                List<Id> participantsList = idStringToIdList(data.getString("participants_ids"));
                List<Id> messagesList = idStringToIdList(data.getString("messages_ids"));
                if (!participantsList.equals(entities.get(id).getParticipantsIds()) ||
                        !messagesList.equals(entities.get(id).getMessagesIds())) {
                    statement.executeUpdate("update conversations set participants_ids = '" + idListToIdString(entities.get(id).getParticipantsIds()) + "' where conversation_id = " + id + ";");
                    statement.executeUpdate("update conversations set messages_ids = '" + idListToIdString(entities.get(id).getMessagesIds()) + "' where conversation_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
