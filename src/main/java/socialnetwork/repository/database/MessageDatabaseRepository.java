package socialnetwork.repository.database;

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

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Message> {
    protected Repository<User> userRepository;

    public MessageDatabaseRepository(String url, String user, String password, Repository<User> userRepository) {
        super(url, user, password);
        this.userRepository = userRepository;
        loadData();
    }

    @Override
    protected void loadData() {
        try {
            ResultSet data = statement.executeQuery("select * from messages;");
            while (data.next()) {
                Id id = new Id(data.getInt("message_id"));
                User from = userRepository.findOne(new Id(data.getInt("from_id")));
                List<User> to = new ArrayList<>();
                for (Id userId: idStringToIdList(data.getString("to_ids")))
                    to.add(userRepository.findOne(userId));
                String text = data.getString("text");
                LocalDateTime date = data.getTimestamp("date").toLocalDateTime();

                Message message = new Message(from, to, text, date);
                message.setId(id);

                entities.putIfAbsent(message.getId(), message);
            }
            data.close();
            data = statement.executeQuery("select * from messages;");
            while (data.next()) {
                Id id = new Id(data.getInt("message_id"));
                Message original = (data.getInt("original_id") != 0) ? (findOne(new Id(data.getInt("original_id")))) : null;
                Message response = (data.getInt("response_id") != 0) ? (findOne(new Id(data.getInt("response_id")))) : null;

                findOne(id).setOriginal(original);
                findOne(id).setResponse(response);
            }
            data.close();
        } catch (Exception exception) {
            throw new DatabaseException("could not load messages database");
        }
    }

    @Override
    protected void addToDatabase(Message entity) {
        try {
            statement.executeUpdate("insert into messages(message_id, from_id, to_ids, text, date, original_id, response_id) " +
                    "values (" + entity.getId() +
                    ", " + entity.getFromId() +
                    ", '" + idListToIdString(entity.getToIds()) +
                    "', '" + entity.getText() +
                    "', '" + Timestamp.valueOf(entity.getDate()) +
                    "', " + ((entity.getOriginal() == null) ? "null" : entity.getOriginalId()) +
                    ", " + ((entity.getResponse() == null) ? "null" : entity.getResponseId()) +
                    ");");
        } catch (Exception exception) {
            throw new DatabaseException("could not add to database");
        }
    }

    @Override
    protected void removeFromDatabase(Id id) {
        try {
            statement.executeUpdate("delete from messages where message_id = " + id + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not remove from database");
        }
    }

    @Override
    protected void updateInDatabase(Message entity) {
        try {
            statement.executeUpdate("update messages " +
                    "set " +
                    "from_id = " + entity.getFromId() +
                    ", to_ids = '" + idListToIdString(entity.getToIds()) +
                    "', text = '" + entity.getText() +
                    "', date = '" + Timestamp.valueOf(entity.getDate()) +
                    "', original_id = " + ((entity.getOriginal() == null) ? "null" : entity.getOriginalId().getValue()) +
                    ", response_id = " + ((entity.getResponse() == null) ? "null" : entity.getResponseId().getValue()) +
                    " where message_id = " + entity.getId() + ";");
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }

    @Override
    protected void updateDatabase() {
        try {
            ResultSet data = statement.executeQuery("select * from messages");
            while (data.next()) {
                Id id = new Id(data.getInt("message_id"));
                List<Id> toList = idStringToIdList(data.getString("to_ids"));
                if (data.getInt("from_id") != entities.get(id).getFromId().getValue() ||
                        !toList.equals(entities.get(id).getToIds()) ||
                        !data.getString("text").equals(entities.get(id).getText()) ||
                        !data.getTimestamp("date").toLocalDateTime().equals(entities.get(id).getDate()) ||
                        data.getInt("original_id") != ((entities.get(id).getOriginal() == null) ? 0 : entities.get(id).getOriginalId().getValue()) ||
                        data.getInt("response_id") != ((entities.get(id).getResponse() == null) ? 0 : entities.get(id).getResponseId().getValue())) {
                    statement.executeUpdate("update messages set from_id = " + entities.get(id).getFromId() + " where message_id = " + id + ";");
                    statement.executeUpdate("update messages set to_ids = '" + idListToIdString(entities.get(id).getToIds()) + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set text = '" + entities.get(id).getText() + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set date = '" + Timestamp.valueOf(entities.get(id).getDate()) + "' where message_id = " + id + ";");
                    statement.executeUpdate("update messages set original_id = " + ((entities.get(id).getOriginal() == null) ? "null" : entities.get(id).getOriginalId().getValue()) + " where message_id = " + id + ";");
                    statement.executeUpdate("update messages set response_id = " + ((entities.get(id).getResponse() == null) ? "null" : entities.get(id).getResponseId().getValue()) + " where message_id = " + id + ";");
                }
            }
        } catch (Exception exception) {
            throw new DatabaseException("could not update database");
        }
    }
}
