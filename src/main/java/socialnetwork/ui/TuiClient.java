package socialnetwork.ui;

import socialnetwork.domain.Invite;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.SocialNetworkException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.service.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TuiClient extends Client {
    public TuiClient(Service service) {
        super(service);
    }

    @Override
    public void run() {
        while (true) {
            try {
                showMenu();
                showCommandPrompt();
                String option = getUserOption();
                executeOption(option);
            } catch (SocialNetworkException exception) {
                System.out.println(exception.toString());
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Social Network ---");
        System.out.println("Commands:");
        System.out.println("    [0]: Exit");
        System.out.println("--Users---------------");
        System.out.println("    [1]: Show all users");
        System.out.println("    [2]: Add user");
        System.out.println("    [3]: Remove user");
        System.out.println("--Friendships---------");
        System.out.println("    [4]: Show all friendships");
        System.out.println("    [5]: Send invite");
        System.out.println("    [6]: Show invites");
        System.out.println("    [7]: Accept invite");
        System.out.println("    [8]: Reject invite");
        System.out.println("    [9]: Remove friendship");
        System.out.println("--Communities---------");
        System.out.println("    [10]: Communities count");
        System.out.println("    [11]: Most sociable community");
        System.out.println("--Friends-------------");
        System.out.println("    [12]: User friendships");
        System.out.println("    [13]: User friendships in month");
        System.out.println("--Messages------------");
        System.out.println("    [14]: Send message");
        System.out.println("    [15]: List conversations");
        System.out.println("    [16]: View conversation");
        System.out.println("    [17]: Reply to message");
        System.out.println("----------------------");
    }

    private void showCommandPrompt() {
        System.out.println("\n> ");
    }

    private String getUserOption() {
        return scanner.nextLine();
    }

    private void executeOption(String option) {
        switch (option) {
            case "0":
                System.out.println("\nExiting app...");
                service.close();
                return;
            case "1":
                showAllUsers();
                break;
            case "2":
                addUser();
                break;
            case "3":
                removeUser();
                break;
            case "4":
                showAllFriendships();
                break;
            case "5":
                sendInvite();
                break;
            case "6":
                getInvites();
                break;
            case "7":
                acceptInvite();
                break;
            case "8":
                rejectInvite();
                break;
            case "9":
                removeFriendship();
                break;
            case "10":
                getCommunitiesCount();
                break;
            case "11":
                getMostSociableCommunity();
                break;
            case "12":
                getUserFriendships();
                break;
            case "13":
                getUserFriendshipsInMonth();
                break;
            case "14":
                sendMessage();
                break;
            case "15":
                listConversations();
                break;
            case "16":
                viewConversation();
                break;
            case "17":
                replyToMessage();
                break;
            default:
                System.out.println("\ncommand not recognised!");
                break;
        }
    }

    private void showAllUsers() {
        service.getAllUsers().forEach(System.out::println);
    }

    private void showAllFriendships() {
        service.getAllFriendships().forEach(System.out::println);
    }

    private void addUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        service.addUser(firstName, lastName);
    }

    private void removeUser() {
        System.out.print("User id: ");
        String id = scanner.nextLine();

        service.removeUser(Long.parseLong(id));
    }

    private void sendInvite() {
        System.out.print("User id: ");
        String fromId = scanner.nextLine();
        System.out.print("Send to id: ");
        String toId = scanner.nextLine();
        try {
            Long.parseLong(fromId);
            Long.parseLong(toId);
        } catch (Exception e) {
            throw new ValidationException("invalid ids");
        }

        service.sendInvite(Long.parseLong(fromId), Long.parseLong(toId));
    }

    private void getInvites() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            throw new ValidationException("invalid id");
        }

        Iterable<Invite> invites = service.getInvites(Long.parseLong(id));
        invites.forEach(invite -> System.out.println("Invite " + invite.getId() + " from " + invite.getFrom() + " to " + invite.getTo() + ". Status: " + invite.getStatus()));
    }

    private void acceptInvite() {
        System.out.print("User id: ");
        String userId = scanner.nextLine();
        System.out.print("Invite id: ");
        String inviteId = scanner.nextLine();
        try {
            Long.parseLong(userId);
            Long.parseLong(inviteId);
        } catch (Exception e) {
            throw new ValidationException("invalid ids");
        }

        service.acceptInvite(Long.parseLong(userId), Long.parseLong(inviteId));
    }

    private void rejectInvite() {
        System.out.print("User id: ");
        String userId = scanner.nextLine();
        System.out.print("Invite id: ");
        String inviteId = scanner.nextLine();
        try {
            Long.parseLong(userId);
            Long.parseLong(inviteId);
        } catch (Exception e) {
            throw new ValidationException("invalid ids");
        }

        service.rejectInvite(Long.parseLong(userId), Long.parseLong(inviteId));
    }

    private void removeFriendship() {
        System.out.print("User id: ");
        String userId = scanner.nextLine();
        System.out.print("Friendship id: ");
        String friendshipId = scanner.nextLine();
        try {
            Long.parseLong(userId);
            Long.parseLong(friendshipId);
        } catch (Exception e) {
            userId = "-1";
            friendshipId = "-1";
        }

        service.removeFriendship(Long.parseLong(userId), Long.parseLong(friendshipId));
    }

    private void getCommunitiesCount() {
        System.out.println("Communities count: " + service.getCommunitiesCount());
    }

    private void getMostSociableCommunity() {
        service.getMostSociableCommunity().forEach(System.out::println);
    }

    private void getUserFriendships() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            throw new ValidationException("invalid user id");
        }

        Map<User, LocalDateTime> friends = service.getUserFriendships(Long.parseLong(id));
        for (User user : friends.keySet()) {
            System.out.println(user.getLastName() + "|" + user.getFirstName() + "|" + friends.get(user));
        }
    }

    private void getUserFriendshipsInMonth() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        System.out.print("Month of friendship: ");
        String month = scanner.nextLine();
        try {
            Long.parseLong(id);
            Integer.parseInt(month);
        } catch (Exception e) {
            throw new ValidationException("invalid data");
        }

        Map<User, LocalDateTime> friends = service.getUserFriendshipsInMonth(Long.parseLong(id), Integer.parseInt(month));
        for (User user : friends.keySet()) {
            System.out.println(user.getLastName() + "|" + user.getFirstName() + "|" + friends.get(user));
        }
    }

    private void sendMessage() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        System.out.print("Send to: ");
        String toStr = scanner.nextLine();
        List<Long> to;
        System.out.print("Message: ");
        String message = scanner.nextLine();
        try {
            Long.parseLong(id);
            to = Arrays.stream(toStr.split(",")).map(Long::parseLong).collect(Collectors.toCollection(Vector::new));
        } catch (Exception e) {
            throw new ValidationException("invalid data");
        }

        service.sendMessage(Long.parseLong(id), to, message);
    }

    private void listConversations() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            throw new ValidationException("invalid id");
        }

        Iterable<Message> conversations = service.getConversations(Long.parseLong(id));
        conversations.forEach(conversation -> System.out.println("Conversation " + conversation.getId() + ": " + conversation.getMessage() + " | ..."));
    }

    private void viewConversation() {
        System.out.print("User id: ");
        String userId = scanner.nextLine();
        System.out.print("Conversation id: ");
        String conversationId = scanner.nextLine();
        try {
            Long.parseLong(userId);
            Long.parseLong(conversationId);
        } catch (Exception e) {
            throw new ValidationException("invalid data");
        }

        Iterable<Message> messages = service.getConversation(Long.parseLong(userId), Long.parseLong(conversationId));
        messages.forEach(message -> System.out.println("User " + message.getFrom() + " (id: " + message.getId() + ") : " + message.getMessage()));
    }

    private void replyToMessage() {
        System.out.print("User id: ");
        String id = scanner.nextLine();
        System.out.print("Reply to message: ");
        String replyToId = scanner.nextLine();
        System.out.print("Message: ");
        String messageValue = scanner.nextLine();
        try {
            Long.parseLong(id);
            Long.parseLong(replyToId);
        } catch (Exception e) {
            throw new ValidationException("invalid data");
        }

        service.replyToMessage(Long.parseLong(id), Long.parseLong(replyToId), messageValue);
    }
}
