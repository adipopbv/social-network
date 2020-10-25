package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.exceptions.NotFoundException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    protected Validator<E> validator;
    protected Map<ID,E> entities;

    public MemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("id must not be null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must not be null");
        validator.validate(entity);

        if(entities.get(entity.getId()) != null)
            return entity;
        else
            entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
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
        validator.validate(entity);

        entities.put(entity.getId(),entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            return null;
        }
        return entity;
    }

    @Override
    public void close() {

    }
}
