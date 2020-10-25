package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.repository.Repository;

import java.util.List;
import java.util.Random;

public class FriendshipService {
    private final Repository<Long, Friendship> repo;
    private final FriendshipValidator friendshipValidator;

    public FriendshipService(Repository<Long, Friendship> repo) {
        this.repo = repo;
        friendshipValidator = new FriendshipValidator();
    }

    public Friendship addFriendship(long id1, long id2) {
        Friendship friendship = new Friendship(id1, id2);
        friendshipValidator.validate(friendship);
        Random random = new Random();
        do {
            friendship.setId((long) (random.nextInt(9000) + 1000));
        } while (repo.save(friendship) != null);
        return friendship;
    }

    public Friendship removeFriendship(long id) {
        return repo.delete(id);
    }

    public Iterable<Friendship> removeFriendships(long friendId) {
        for (Friendship friendship : repo.findAll())
            if (friendId == friendship.getFriend1() || friendId == friendship.getFriend2())
                repo.delete(friendship.getId());
        return repo.findAll();
    }

    public Iterable<Friendship> getAll(){
        return repo.findAll();
    }
}
