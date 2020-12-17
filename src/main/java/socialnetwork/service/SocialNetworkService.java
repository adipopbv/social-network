package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.DuplicateException;
import socialnetwork.domain.exceptions.NotFoundException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.graphs.UndirectedGraph;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.utils.observers.Observable;
import socialnetwork.utils.observers.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SocialNetworkService implements Observable {
    private final Repository<User> userRepository;
    private final List<User> loggedInUsers = new ArrayList<>();
    private final Repository<Friendship> friendshipRepository;
    private final Repository<Message> messageRepository;
    private final Repository<Invite> inviteRepository;
    private final Repository<Conversation> conversationRepository;
    private final UserValidator userValidator;
    private final FriendshipValidator friendshipValidator;
    private final MessageValidator messageValidator;
    private final InviteValidator inviteValidator;
    private final ConversationValidator conversationValidator;

    public SocialNetworkService(Repository<User> userRepository, Repository<Friendship> friendshipRepository, Repository<Message> messageRepository, Repository<Invite> inviteRepository, Repository<Conversation> conversationRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.inviteRepository = inviteRepository;
        this.conversationRepository = conversationRepository;
        userValidator = new UserValidator();
        friendshipValidator = new FriendshipValidator();
        messageValidator = new MessageValidator();
        inviteValidator = new InviteValidator();
        conversationValidator = new ConversationValidator();
    }

    /**
     * Logs in a user
     * @param userId the id of the user to be logged in
     * @return the user that logged in
     */
    public User logInUser(Id userId) {
        User user = userRepository.findOne(userId);
        if (loggedInUsers.contains(user))
            throw new DuplicateException("user already logged in");

        loggedInUsers.add(user);
        return user;
    }

    /**
     * Logs out a user
     * @param userId the id of the user to be logged out
     * @return the user that logged out
     */
    public User logOutUser(Id userId) {
        User user = userRepository.findOne(userId);
        if (!loggedInUsers.contains(user))
            throw new NotFoundException("user not logged in");

        loggedInUsers.remove(user);
        return user;
    }

    /**
     * Gets all users from the repo
     * @return the list of users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets a user from the repo
     * @return the user
     */
    public User getUser(Id userId) {
        return userRepository.findOne(userId);
    }

    /**
     * Gets all friendships from the repo
     * @return the list of friendships
     */
    public List<Friendship> getAllFriendships() {
        return friendshipRepository.findAll();
    }

    /**
     * Gets all messages from the repo
     * @return the list of messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Gets all invites from the repo
     * @return the list of invites
     */
    public List<Invite> getAllInvites() {
        return inviteRepository.findAll();
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
        do {
            user.setId(new Id());
        } while (userRepository.save(user) != null);

        notifyObservers();
        return user;
    }

    /**
     * Removes a user from the repo
     * @param id the id of the user to be removed
     * @return the removed user if operation successful
     */
    public User removeUser(Id id) {
        User user = userRepository.delete(id);

        Collection<Friendship> toBeErased = new ArrayList<>();
        for (Friendship friendship : friendshipRepository.findAll())
            if (user.getId() == friendship.getFirstFriendId() || user.getId() == friendship.getSecondFriendId())
                toBeErased.add(friendship);
        for (Friendship friendship : toBeErased)
            friendshipRepository.delete(friendship.getId());
        for (User userSearch : userRepository.findAll())
            userSearch.getFriends().remove(user);

        notifyObservers();
        return user;
    }

    /**
     * Adds a new friendship to the repo
     * @param firstFriendId the id of the first user from the friendship
     * @param secondFriendId the id of the second user from the friendship
     * @return the newly created friendship if the operation was successful
     */
    public Friendship addFriendship(Id firstFriendId, Id secondFriendId) {
        User firstFriend = userRepository.findOne(firstFriendId);
        User secondFriend = userRepository.findOne(secondFriendId);
        if (firstFriend == null || secondFriend == null)
            throw new NotFoundException("nonexistent users");

        Friendship friendship = new Friendship(firstFriend, secondFriend);
        friendshipValidator.validate(friendship);
        do {
            friendship.setId(new Id());
        } while (friendshipRepository.save(friendship) != null);

        firstFriend.getFriends().add(secondFriend);
        secondFriend.getFriends().add(firstFriend);
        userRepository.update(firstFriend);
        userRepository.update(secondFriend);

        notifyObservers();
        return friendship;
    }

    /**
     * Removes a friendship from the repo
     * @param userId the id of the user removing the friendship
     * @param friendshipId the id of the friendship to be removed
     * @return the friendship that has been removed if the operation was successful
     */
    public Friendship removeFriendship(Id userId, Id friendshipId) {
        User user = userRepository.findOne(userId);
        Friendship friendship = friendshipRepository.findOne(friendshipId);
        if (user == null)
            throw new ValidationException("invalid user");
        if (friendship == null ||
                (friendship.getFirstFriend() != user &&
                friendship.getSecondFriend() != user))
            throw new ValidationException("invalid friendship");

        friendshipRepository.delete(friendship.getId());

        User user1 = userRepository.findOne(friendship.getFirstFriendId()), user2 = userRepository.findOne(friendship.getSecondFriendId());
        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);
        userRepository.update(user1);
        userRepository.update(user2);

        notifyObservers();
        return friendship;
    }

    /**
     * Gets the number of communities with more than one user
     * @return the number of communities
     */
    public int getCommunitiesCount() {
        Map<Id, HashSet<Id>> adjMap = new HashMap<>();
        for (User user : getAllUsers()) {
            for (Id friendId : user.getFriendsIds()) {
                Id userId = user.getId();
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
        Map<Id, HashSet<Id>> adjMap = new HashMap<>();
        for (User user : getAllUsers()) {
            for (Id friendId : user.getFriendsIds()) {
                Id userId = user.getId();
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
        Iterable<Id> connectedComponent = graph.getConnectedComponentWithLongestRoad();
        for (Id vertex : connectedComponent) {
            mostSociableNetwork.add(userRepository.findOne(vertex));
        }
        return mostSociableNetwork;
    }

    /**
     * Gets all the friends of a user and the dates of the friendships
     * @param id the id of the user whose friends will be returned
     * @return the friends and friendship dates of the user
     */
    public Map<User, LocalDateTime> getUserFriendships(Id id) {
        Map<User, LocalDateTime> friends = new HashMap<>();
        Collection<Friendship> friendships = new ArrayList<>(friendshipRepository.findAll());

        friendships = friendships.stream()
                .filter(fr -> fr.getFirstFriendId() == id || fr.getSecondFriendId() == id)
                .collect(Collectors.toCollection(ArrayList::new));
        friendships.forEach(friendship -> {
            Id friendId = (id == friendship.getFirstFriendId()) ? friendship.getSecondFriendId() : friendship.getFirstFriendId();
            friends.put(userRepository.findOne(friendId), friendship.getDate());
        });

        return friends;
    }

    /**
     * Gets all the friends of a user and the dates of the friendships
     * @param id the id of the user whose friends will be returned
     * @return the friends and friendship dates of the user
     */
    public Map<User, LocalDateTime> getUserFriendshipsInMonth(Id id, int month) {
        Map<User, LocalDateTime> friends = new HashMap<>();
        Collection<Friendship> friendships = new ArrayList<>(friendshipRepository.findAll());

        friendships = friendships.stream()
                .filter(fr -> (fr.getFirstFriendId() == id || fr.getSecondFriendId() == id) && fr.getDate().getMonth().getValue() == month)
                .collect(Collectors.toCollection(ArrayList::new));
        friendships.forEach(friendship -> {
            Id friendId = (id == friendship.getFirstFriendId()) ? friendship.getSecondFriendId() : friendship.getFirstFriendId();
            friends.put(userRepository.findOne(friendId), friendship.getDate());
        });

        return friends;
    }

    /**
     * Gets all conversations that implicate a user
     * @param userId the id of the user that takes part in the conversations
     * @return a list composed of the conversations
     */
    public List<Conversation> getConversations(Id userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new NotFoundException("nonexistent user");

        List<Conversation> conversations = new ArrayList<>(conversationRepository.findAll());
        conversations = conversations.stream()
                .filter(conversation -> conversation.getParticipants().contains(user))
                .collect(Collectors.toList());

        return conversations;
    }

    /**
     * Gets the messages of a conversation
     * @param conversationId the id of the conversation
     * @return the list of messages of the conversation
     */
    public List<Message> getConversationMessages(Id conversationId) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        if (conversation == null)
            throw new ValidationException("invalid conversation");

        return conversation.getMessages();
    }

    /**
     * Adds a message to the repo
     * @param conversationId the id of the conversation
     * @param fromId the id of the user that sends the message
     * @param text the message body
     * @return the newly created message if the operation was successful
     */
    public Message sendMessage(Id conversationId, Id fromId, String text) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        User from = userRepository.findOne(fromId);
        if (from == null || !conversation.getParticipants().contains(from))
            throw new ValidationException("invalid user and conversation");

        List<User> to = new ArrayList<>(conversation.getParticipants());
        to.remove(from);
        Message message = new Message(from, to, text);

        messageValidator.validate(message);
        do {
            message.setId(new Id());
        } while (messageRepository.save(message) != null);
        conversation.getMessages().add(message);

        notifyObservers();
        return message;
    }

    /**
     * Adds a message ro the repo as a reply to a message or another reply
     * @param conversationId the id of the conversation
     * @param fromId the id of the user taking part to the conversation
     * @param originalId the id of the message that is replying to
     * @param text the message body
     * @return the newly created reply if the operation was successful
     */
    public Message sendReply(Id conversationId, Id fromId, Id originalId, String text) {
        Conversation conversation = conversationRepository.findOne(conversationId);
        User from = userRepository.findOne(fromId);
        Message original = messageRepository.findOne(originalId);
        if (from == null || !conversation.getParticipants().contains(from))
            throw new ValidationException("invalid user and conversation");

        List<User> to = new ArrayList<>(Collections.singletonList(original.getFrom()));
        Message response = new Message(from, to, text);

        messageValidator.validate(response);
        response.setOriginal(original);
        original.setResponse(response);
        do {
            response.setId(new Id());
        } while (messageRepository.save(response) != null);
        conversation.getMessages().add(response);
        messageRepository.update(original);

        notifyObservers();
        return response;
    }

    /**
     * Adds a new friendship invite to the repo
     * @param fromId the id of the user inviting
     * @param toId the id of the user being invited
     * @return the newly created invite if the operation was successful
     */
    public Invite sendInvite(Id fromId, Id toId) {
        User from = userRepository.findOne(fromId);
        User to = userRepository.findOne(toId);
        if (from == null ||
                to == null ||
                from == to)
            throw new ValidationException("invalid users");
        if (to.getFriends().contains(from))
            throw new DuplicateException("users already friends");
        for (Invite invite : inviteRepository.findAll())
            if (((invite.getFrom() == from && invite.getTo() == to) ||
                    (invite.getFrom() == to && invite.getTo() == from)) &&
                    invite.getStatus() == InviteStatus.PENDING)
                throw new DuplicateException("users already trying to connect");

        Invite invite = new Invite(from, to);
        inviteValidator.validate(invite);
        do {
            invite.setId(new Id());
        } while (inviteRepository.save(invite) != null);

        notifyObservers();
        return invite;
    }

    /**
     * Gets the invites implicating a user
     * @param userId the id of the user taking part in the invite
     * @return the list of invites
     */
    public List<Invite> getInvites(Id userId) {
        User user = userRepository.findOne(userId);
        List<Invite> invites = new ArrayList<>();
        for (Invite invite : inviteRepository.findAll())
            if (invite.getFrom() == user || invite.getTo() == user)
                invites.add(invite);
        return invites;
    }

    /**
     * Accepts an invite and adds a friendship between the two users of the invite
     * @param userId the id of the user being invited
     * @param inviteId the id of the invite being accepted
     * @return the newly created friendship after accepting the invite if the operation was successful
     */
    public Friendship acceptInvite(Id userId, Id inviteId) {
        User user = userRepository.findOne(userId);
        Invite invite = inviteRepository.findOne(inviteId);
        if (userRepository.findOne(user.getId()) == null)
            throw new NotFoundException("nonexistent user");
        if (inviteRepository.findOne(invite.getId()) == null ||
                invite.getFrom() == user ||
                invite.getTo() != user ||
                invite.getStatus() != InviteStatus.PENDING)
            throw new ValidationException("invalid invite");

        invite.setStatus(InviteStatus.APPROVED);
        inviteRepository.update(invite);

        notifyObservers();
        return addFriendship(invite.getFrom().getId(), invite.getTo().getId());
    }

    /**
     * Rejects a invite
     * @param userId the id of the user rejecting the invite
     * @param inviteId the id of the invite being rejected
     * @return the invite after being rejected if the operation was successful
     */
    public Invite rejectInvite(Id userId, Id inviteId) {
        User user = userRepository.findOne(userId);
        Invite invite = inviteRepository.findOne(inviteId);
        if (userRepository.findOne(user.getId()) == null)
            throw new NotFoundException("nonexistent user");
        if (inviteRepository.findOne(invite.getId()) == null ||
                (invite.getFrom() != user &&
                        invite.getTo() != user) ||
                invite.getStatus() != InviteStatus.PENDING)
            throw new ValidationException("invalid invite");

        invite.setStatus(InviteStatus.REJECTED);
        inviteRepository.update(invite);

        notifyObservers();
        return invite;
    }

    /**
     * Removes a friend
     * @param userId the id of the user removing the friend
     * @param friendId the id of the friend being removed
     */
    public void removeFriend(Id userId, Id friendId) {
        User user = userRepository.findOne(userId);
        User friend = userRepository.findOne(friendId);
        if (user == null ||
                friend == null)
            throw new NotFoundException("nonexistent user");

        for (Friendship friendship : friendshipRepository.findAll()) {
            if ((friendship.getFirstFriend() == user && friendship.getSecondFriend() == friend) ||
                    (friendship.getFirstFriend() == friend && friendship.getSecondFriend() == user)) {
                removeFriendship(user.getId(), friendship.getId());
                break;
            }
        }

        notifyObservers();
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
