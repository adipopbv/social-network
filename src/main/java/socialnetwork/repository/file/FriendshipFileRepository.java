package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.exceptions.DuplicateException;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship> {
    public FriendshipFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(LocalDateTime.parse(attributes.get(1)), Long.parseLong(attributes.get(2)), Long.parseLong(attributes.get(3)));
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    public Friendship save(Friendship entity) {
        for (Friendship f : entities.values())
            if ((f.getFriend1() == entity.getFriend1() && f.getFriend2() == entity.getFriend2()) ||
                    f.getFriend1() == entity.getFriend2() && f.getFriend2() == entity.getFriend1())
                throw new DuplicateException("friendship already existent");
        return super.save(entity);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getDate() + ";" + entity.getFriend1() + ";" + entity.getFriend2();
    }
}
