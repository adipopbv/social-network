package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

public class UserService {
    private Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    public User addUtilizator(User messageTask) {
        User task = repo.save(messageTask);
        return task;
    }

    public Iterable<User> getAll(){
        return repo.findAll();
    }

    ///TO DO: add other methods
}
