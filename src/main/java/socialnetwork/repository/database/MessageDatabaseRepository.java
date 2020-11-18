package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Long, Message> {
    public MessageDatabaseRepository(String url, String user, String password, Validator<Message> validator) {
        super(url, user, password, validator);
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from messages;");
            while (data.next()) {
                long id = data.getInt("message_id");
                long from = data.getInt("from_id");
                List<Long> to = dbStringToList(data.getString("to_ids"));
                String messageValue = data.getString("message_value");
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();
                long response = (data.getInt("response_id") != 0) ? ((long) (data.getInt("response_id"))) : 0;
                boolean isReply = data.getBoolean("is_reply");

                Message message = new Message(from, to, messageValue, date, response, isReply);
                message.setId(id);

                entities.putIfAbsent(message.getId(), message);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load database");
        }
    }

    @Override
    protected void addToDatabase(Message entity) {
        try {
            statement.executeUpdate("insert into messages(message_id, from_id, to_ids, message_value, date, response_id, is_reply) " +
                    "values (" + entity.getId() +
                    ", " + entity.getFrom() +
                    ", '" + listToDbString(entity.getTo()) +
                    "', '" + entity.getMessage() +
                    "', '" + Timestamp.valueOf(entity.getDate()) +
                    "', " + ((entity.getResponse() == 0) ? "null" : entity.getResponse()) +
                    ", " + entity.isReply() +
                    ");");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Long id) {
        try {
            statement.executeUpdate("delete from messages where message_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from messages");
            while (data.next()) {
                long id = data.getInt("message_id");
                List<Long> toList = dbStringToList(data.getString("to_ids"));
                if (data.getInt("from_id") != entities.get(id).getFrom() ||
                        !toList.equals(entities.get(id).getTo()) ||
                        !data.getString("message_value").equals(entities.get(id).getMessage()) ||
                        !data.getTimestamp("date").toLocalDateTime().equals(entities.get(id).getDate()) ||
                        data.getInt("response_id") != ((entities.get(id).getResponse() == 0) ? 0 : entities.get(id).getResponse()) ||
                        data.getBoolean("is_reply") != entities.get(id).isReply()) {
                    statement.executeUpdate("update messages set from_id = " + entities.get(id).getFrom() + " where message_id = " + id + ";");
                    statement.executeUpdate("update messages set to_ids = '" + listToDbString(entities.get(id).getTo()) + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set message_value = '" + entities.get(id).getMessage() + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set date = '" + Timestamp.valueOf(entities.get(id).getDate()) + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set response_id = " + ((entities.get(id).getResponse() == 0) ? "null" : entities.get(id).getResponse()) + " where message_id = " + id + ";");
                    statement.executeUpdate("update messages set is_reply = " + entities.get(id).isReply() + " where message_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
