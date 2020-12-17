package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Id;
import socialnetwork.domain.exceptions.NotFoundException;
import socialnetwork.repository.Repository;

import java.util.*;

public class MemoryRepository<E extends Entity> implements Repository<E> {
    protected Map<Id, E> entities;

    public MemoryRepository() {
        entities = new HashMap<>();
    }

    @Override
    public E findOne(Id id){
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        return entities.get(id);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");

        if(entities.get(entity.getId()) != null)
            return entity;
        else
            entities.put(entity.getId(),entity);

        return null;
    }

    @Override
    public E delete(Id id) {
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        if (entities.get(id) == null)
            throw new NotFoundException("entity not found in repo");

        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        entities.put(entity.getId(),entity);

        return entity;
    }

    @Override
    public void close() { }
}
