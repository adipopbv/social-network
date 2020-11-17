package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.graphs.UndirectedGraph;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, Message> messageRepository;
    private final UserValidator userValidator;
    private final FriendshipValidator friendshipValidator;
    private final MessageValidator messageValidator;

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, Message> messageRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        userValidator = new UserValidator();
        friendshipValidator = new FriendshipValidator();
        messageValidator = new MessageValidator();
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    public Iterable<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public User addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        userValidator.validate(user);
        Random random = new Random();
        do {
            user.setId((long) (random.nextInt(9000) + 1000));
        } while (userRepository.save(user) != null);
        return user;
    }

    public User removeUser(long id) {
        User user = userRepository.delete(id);

        Collection<Friendship> toBeErased = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll())
            if (user.getId() == friendship.getFriend1() || user.getId() == friendship.getFriend2())
                toBeErased.add(friendship);
        // faceam delete la friendship din lista din care faceam foreach loop in timpul iterarii
        for (Friendship friendship : toBeErased)
            friendshipRepository.delete(friendship.getId());
        for (User userSearch : userRepository.findAll())
            userSearch.getFriends().remove(id);

        return user;
    }

    public Friendship addFriendship(long id1, long id2) {
        Friendship friendship = new Friendship(id1, id2);
        friendshipValidator.validate(friendship);
        Random random = new Random();
        do {
            friendship.setId((long) (random.nextInt(9000) + 1000));
        } while (friendshipRepository.save(friendship) != null);

        User user1 = userRepository.findOne(id1), user2 = userRepository.findOne(id2);
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());

        return friendship;
    }

    public Friendship removeFriendship(long id) {
        Friendship friendship = friendshipRepository.delete(id);

        User user1 = userRepository.findOne(friendship.getFriend1()), user2 = userRepository.findOne(friendship.getFriend2());
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());

        return friendship;
    }

    public int getCommunitiesCount() {
        Map<Long, HashSet<Long>> adjMap = new HashMap<>();
        for (User user : getAllUsers()) {
            for (long friendId : user.getFriends()) {
                long userId = user.getId();
                adjMap.putIfAbsent(userId, new HashSet<>());
                adjMap.putIfAbsent(friendId, new HashSet<>());
                adjMap.get(userId).add(friendId);
                adjMap.get(userId).add(userId);
                adjMap.get(friendId).add(userId);
                adjMap.get(friendId).add(friendId);
            }
        }
        UndirectedGraph graph = new UndirectedGraph(adjMap);
        return graph.getConnectedComponentsCount();
    }

    public void close() {
        userRepository.close();
        friendshipRepository.close();
    }

    public Iterable<User> getMostSociableCommunity() {
        Map<Long, HashSet<Long>> adjMap = new HashMap<>();
        for (User user : getAllUsers()) {
            for (long friendId : user.getFriends()) {
                long userId = user.getId();
                adjMap.putIfAbsent(userId, new HashSet<>());
                adjMap.putIfAbsent(friendId, new HashSet<>());
                adjMap.get(userId).add(friendId);
                adjMap.get(userId).add(userId);
                adjMap.get(friendId).add(userId);
                adjMap.get(friendId).add(friendId);
            }
        }
        UndirectedGraph graph = new UndirectedGraph(adjMap);
        Collection<User> mostSociableNetwork = new ArrayList<>();
        Iterable<Long> connectedComponent = graph.getConnectedComponentWithLongestRoad();
        for (long vertex : connectedComponent) {
            mostSociableNetwork.add(userRepository.findOne(vertex));
        }
        return mostSociableNetwork;
    }

    public Map<User, LocalDateTime> getUserFriendships(long id) {
        Map<User, LocalDateTime> friends = new HashMap<>();
        Collection<Friendship> friendships = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll()) {
            friendships.add(friendship);
        }

        friendships = friendships.stream()
                .filter(fr -> fr.getFriend1() == id || fr.getFriend2() == id)
                .collect(Collectors.toCollection(ArrayList::new));
        friendships.forEach(friendship -> {
            long friendId = (id == friendship.getFriend1()) ? friendship.getFriend2() : friendship.getFriend1();
            friends.put(userRepository.findOne(friendId), friendship.getDate());
        });

        return friends;
    }

    public Map<User, LocalDateTime> getUserFriendshipsInMonth(long id, int month) {
        Map<User, LocalDateTime> friends = new HashMap<>();
        Collection<Friendship> friendships = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll()) {
            friendships.add(friendship);
        }

        friendships = friendships.stream()
                .filter(fr -> (fr.getFriend1() == id || fr.getFriend2() == id) && fr.getDate().getMonth().getValue() == month)
                .collect(Collectors.toCollection(ArrayList::new));
        friendships.forEach(friendship -> {
            long friendId = (id == friendship.getFriend1()) ? friendship.getFriend2() : friendship.getFriend1();
            friends.put(userRepository.findOne(friendId), friendship.getDate());
        });

        return friends;
    }
}
