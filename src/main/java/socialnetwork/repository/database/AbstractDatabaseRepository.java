package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.exceptions.DatabaseException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.MemoryRepository;

import java.sql.*;

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
