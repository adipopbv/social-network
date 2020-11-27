package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.DuplicateException;
import socialnetwork.domain.exceptions.NotFoundException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.graphs.UndirectedGraph;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.InviteValidator;
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
    private final Repository<Long, Invite> inviteRepository;
    private final UserValidator userValidator;
    private final FriendshipValidator friendshipValidator;
    private final MessageValidator messageValidator;
    private final InviteValidator inviteValidator;

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, Message> messageRepository, Repository<Long, Invite> inviteRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.inviteRepository = inviteRepository;
        userValidator = new UserValidator();
        friendshipValidator = new FriendshipValidator();
        messageValidator = new MessageValidator();
        inviteValidator = new InviteValidator();
    }

    /**
     * Gets all users from the repo
     * @return the list of users
     */
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets all friendships from the repo
     * @return the list of friendships
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    /**
     * Gets all messages from the repo
     * @return the list of messages
     */
    public Iterable<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Adds a new user to the repo
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return the newly created user if adding was successful
     */
    public User addUser(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        userValidator.validate(user);
        Random random = new Random();
        do {
            user.setId((long) (random.nextInt(9000) + 1000));
        } while (userRepository.save(user) != null);
        return user;
    }

    /**
     * Removes a user from the repo
     * @param id the id of the user to be removed
     * @return the removed user if operation successful
     */
    public User removeUser(long id) {
        User user = userRepository.delete(id);

        Collection<Friendship> toBeErased = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll())
            if (user.getId() == friendship.getFriend1() || user.getId() == friendship.getFriend2())
                toBeErased.add(friendship);
        for (Friendship friendship : toBeErased)
            friendshipRepository.delete(friendship.getId());
        for (User userSearch : userRepository.findAll())
            userSearch.getFriends().remove(id);

        return user;
    }

    /**
     * Adds a new friendship to the repo
     * @param id1 the id of the first user from the friendship
     * @param id2 the id of the second user from the friendship
     * @return the newly created friendship if the operation was successful
     */
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

    /**
     * Removes a friendship from the repo
     * @param id the id of the friendship to be removed
     * @return the friendship that has been removed if the operation was successful
     */
    public Friendship removeFriendship(long id) {
        Friendship friendship = friendshipRepository.delete(id);

        User user1 = userRepository.findOne(friendship.getFriend1()), user2 = userRepository.findOne(friendship.getFriend2());
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());

        return friendship;
    }

    /**
     * Gets the number of communities with more than one user
     * @return the number of communities
     */
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

    /**
     * Gets the community with most users
     * @return the users of the most sociable community
     */
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

    /**
     * Gets all the friends of a user and the dates of the friendships
     * @param id the id of the user whose friends will be returned
     * @return the friends and friendship dates of the user
     */
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

    /**
     * Gets all the friends of a user and the dates of the friendships
     * @param id the id of the user whose friends will be returned
     * @return the friends and friendship dates of the user
     */
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

    /**
     * Adds a message to the repo
     * @param id the id of the user that sends the message
     * @param to the ids of teh users that receive the message
     * @param messageValue the message body
     * @return the newly created message if the operation was successful
     */
    public Message sendMessage(long id, List<Long> to, String messageValue) {
        Message message = new Message(id, to, messageValue);
        message.setReply(false);

        messageValidator.validate(message);
        boolean okTo = true;
        for (Long toId : to)
            if (userRepository.findOne(toId) == null) {
                okTo = false;
                break;
            }
        if (userRepository.findOne(id) == null ||
                !okTo)
            throw new ValidationException("invalid users");

        Random random = new Random();
        do {
            message.setId((long) (random.nextInt(9000) + 1000));
        } while (messageRepository.save(message) != null);
        return message;
    }

    /**
     * Gets all conversations that implicate a user
     * @param userId the id of the user that takes part in the conversations
     * @return a list composed of the first message of each conversation
     */
    public Iterable<Message> getConversations(long userId) {
        if (userRepository.findOne(userId) == null)
            throw new NotFoundException("nonexistent user");

        Collection<Message> conversations = new ArrayList<>();
        for (Message message : messageRepository.findAll())
            conversations.add(message);
        conversations = conversations.stream()
                .filter(conversation ->
                        (conversation.getFrom().equals(userId) ||
                                conversation.getTo().contains(userId)) &&
                                !conversation.isReply())
                .collect(Collectors.toCollection(ArrayList::new));

        return conversations;
    }

    /**
     * Gets the messages of a conversation
     * @param userId the id of the user that is taking part of the conversation
     * @param conversationId the id of the first message of the conversation
     * @return the list of messages of the conversation
     */
    public Iterable<Message> getConversation(long userId, long conversationId) {
        if (userRepository.findOne(userId) == null)
            throw new ValidationException("invalid user");
        if (messageRepository.findOne(conversationId) == null ||
                (messageRepository.findOne(conversationId).getFrom() != userId &&
                        !messageRepository.findOne(conversationId).getTo().contains(userId)))
            throw new ValidationException("invalid message");

        List<Message> conversation = new ArrayList<>();
        Message message = messageRepository.findOne(conversationId);
        conversation.add(message);
        while (message.getResponse() != 0) {
            message = messageRepository.findOne(message.getResponse());
            conversation.add(message);
        }
        return conversation;
    }

    /**
     * Adds a message ro the repo as a reply to a message or another reply
     * @param userId the id of the user taking part to the conversation
     * @param replyToId the id of the message that is replying to
     * @param messageValue the message body
     * @return the newly created reply if the operation was successful
     */
    public Message replyToMessage(long userId, long replyToId, String messageValue) {
        if (userRepository.findOne(userId) == null)
            throw new ValidationException("invalid user");
        if (messageRepository.findOne(replyToId) == null ||
                messageRepository.findOne(replyToId).getFrom() == userId ||
                !messageRepository.findOne(replyToId).getTo().contains(userId))
            throw new ValidationException("invalid message");

        Message original = messageRepository.findOne(replyToId);
        Message message = new Message(userId, new ArrayList<>(Arrays.asList(original.getFrom())), messageValue);

        messageValidator.validate(message);

        Random random = new Random();
        do {
            message.setId((long) (random.nextInt(9000) + 1000));
        } while (messageRepository.save(message) != null);
        original.setResponse(message.getId());
        return message;
    }

    /**
     * Adds a new friendship invite to the repo
     * @param from the id of the user inviting
     * @param to the id of the user being invited
     * @return the newly created invite if the operation was successful
     */
    public Invite sendInvite(long from, long to) {
        if (userRepository.findOne(from) == null ||
                userRepository.findOne(to) == null ||
                from == to)
            throw new ValidationException("invalid users ids");
        if (userRepository.findOne(to).getFriends().contains(from))
            throw new DuplicateException("users already friends");
        for (Invite invite : inviteRepository.findAll())
            if (((invite.getFrom() == from && invite.getTo() == to) ||
                    (invite.getFrom() == to && invite.getTo() == from)) &&
                    invite.getStatus() != InviteStatus.REJECTED)
                throw new DuplicateException("users already trying to connect");

        Invite invite = new Invite(from, to);
        inviteValidator.validate(invite);
        Random random = new Random();
        do {
            invite.setId((long) (random.nextInt(9000) + 1000));
        } while (inviteRepository.save(invite) != null);
        return invite;
    }

    /**
     * Gets the invites implicating a user
     * @param userId the id of the user taking part in the invite
     * @return the list of invites
     */
    public Iterable<Invite> getInvites(long userId) {
        Collection<Invite> invites = new ArrayList<>();
        for (Invite invite : inviteRepository.findAll())
            if (invite.getFrom() == userId || invite.getTo() == userId)
                invites.add(invite);
        return invites;
    }

    /**
     * Accepts an invite and adds a friendship between the two users of the invite
     * @param userId the id of the user being invited
     * @param inviteId the id of the invite being accepted
     * @return the newly created friendship after accepting the invite if the operation was successful
     */
    public Friendship acceptInvite(long userId, long inviteId) {
        if (userRepository.findOne(userId) == null)
            throw new NotFoundException("nonexistent user");
        Invite invite = inviteRepository.findOne(inviteId);
        if (invite == null ||
                invite.getFrom() == userId ||
                invite.getTo() != userId ||
                invite.getStatus() != InviteStatus.PENDING)
            throw new ValidationException("invalid invite");

        invite.setStatus(InviteStatus.APPROVED);
        return addFriendship(invite.getFrom(), invite.getTo());
    }

    /**
     * Rejects a invite
     * @param userId the id of the user rejecting the invite
     * @param inviteId the id of the invite being rejected
     * @return the invite after being rejected if the operation was successful
     */
    public Invite rejectInvite(long userId, long inviteId) {
        if (userRepository.findOne(userId) == null)
            throw new NotFoundException("nonexistent user");
        Invite invite = inviteRepository.findOne(inviteId);
        if (invite == null ||
                (invite.getFrom() != userId &&
                        invite.getTo() != userId) ||
                invite.getStatus() != InviteStatus.PENDING)
            throw new ValidationException("invalid invite");

        invite.setStatus(InviteStatus.REJECTED);
        return invite;
    }

    /**
     * Closes the repositories. Acts like a destructor
     */
    public void close() {
        userRepository.close();
        friendshipRepository.close();
        messageRepository.close();
        inviteRepository.close();
    }
}
