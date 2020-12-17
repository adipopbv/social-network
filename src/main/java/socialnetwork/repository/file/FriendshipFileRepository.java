package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Id;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.DuplicateException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Friendship> {
    private Repository<User> userRepository;

    public FriendshipFileRepository(String fileName) {
        super(fileName);
    }

    public FriendshipFileRepository(String fileName, Repository<User> userRepository) {
        super(fileName);
        this.userRepository = userRepository;
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(
                LocalDateTime.parse(attributes.get(1)),
                userRepository.findOne(new Id(attributes.get(2))),
                userRepository.findOne(new Id(attributes.get(3)))
        );
        friendship.setId(new Id(attributes.get(0)));
        return friendship;
    }

    @Override
    public Friendship save(Friendship entity) {
        for (Friendship f : entities.values())
            if ((f.getFirstFriend() == entity.getFirstFriend() && f.getSecondFriend() == entity.getSecondFriend()) ||
                    f.getFirstFriend() == entity.getSecondFriend() && f.getSecondFriend() == entity.getFirstFriend())
                throw new DuplicateException("friendship already existent");
        return super.save(entity);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId() + ";" + entity.getDate() + ";" + entity.getFirstFriendId() + ";" + entity.getSecondFriendId();
    }
}
