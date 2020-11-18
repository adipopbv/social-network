package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.MemoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends MemoryRepository<ID, E> {
    Connection connection = null;
    Statement statement = null;

    public AbstractDatabaseRepository(String url, String user, String password, Validator<E> validator) {
        super(validator);
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (Exception exception) {
            throw new DatabaseException("could not connect to database");
        }
        loadData();
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
    public E delete(ID id) {
        E e = super.delete(id);
        if (e != null)
            removeFromDatabase(id);
        return e;
    }

    protected void addToDatabase (E entity) { }

    protected void removeFromDatabase (ID id) { }

    protected void updateDatabase() { }

    protected String listToDbString(List<Long> list) {
        StringBuilder dbString = new StringBuilder();
        if (!list.isEmpty())
            dbString.append(list.get(0).toString());
        for (int index = 1; index < list.size(); index++)
            dbString.append(",").append(list.get(index));
        return dbString.toString();
    }

    protected List<Long> dbStringToList(String dbString) {
        List<Long> list = new ArrayList<>();
        String[] stringArray = dbString.split(",");
        if (stringArray.length > 0 && !stringArray[0].equals(""))
            list = Arrays.stream(stringArray).map(Long::parseLong).collect(Collectors.toCollection(Vector::new));
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
