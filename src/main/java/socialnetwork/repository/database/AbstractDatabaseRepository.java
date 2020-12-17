package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Id;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.MemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class AbstractDatabaseRepository<E extends Entity> extends MemoryRepository<E> {
    protected Connection connection = null;
    protected Statement statement = null;

    public AbstractDatabaseRepository(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (Exception exception) {
            throw new DatabaseException("could not connect to database");
        }
    }

    protected void loadData() { }

    @Override
    public E save(E entity){
        E e = super.save(entity);
        if (e == null)
            addToDatabase(entity);
        return e;
    }

    @Override
    public E delete(Id id) {
        E e = super.delete(id);
        if (e != null)
            removeFromDatabase(id);
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if (e != null)
            updateInDatabase(entity);
        return e;
    }

    protected void addToDatabase (E entity) { }

    protected void removeFromDatabase (Id id) { }

    protected void updateInDatabase (E entity) { }

    protected void updateDatabase() { }

    protected String idListToIdString(List<Id> idList) {
        StringBuilder dbString = new StringBuilder();
        if (!idList.isEmpty())
            dbString.append(idList.get(0).toString());
        for (int index = 1; index < idList.size(); index++)
            dbString.append(",").append(idList.get(index));
        return dbString.toString();
    }

    protected List<Id> idStringToIdList(String idString) {
        List<Id> list = new ArrayList<>();
        String[] stringArray = idString.split(",");
        if (stringArray.length > 0 && !stringArray[0].equals(""))
            list = Arrays.stream(stringArray).map(Id::new).collect(Collectors.toList());
        return list;
    }

    @Override
    public void close() {
        try {
            updateDatabase();
            statement.close();
            connection.close();
        } catch (Exception exception) {
            throw new DatabaseException("failed to close connection to database");
        }
    }
}
