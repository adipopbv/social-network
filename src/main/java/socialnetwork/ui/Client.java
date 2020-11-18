package socialnetwork.ui;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.exceptions.SocialNetworkException;
import socialnetwork.service.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Client {
    private final Service service;
    private final Scanner scanner = new Scanner(System.in);

    public Client(Service service) {
        this.service = service;
    }

    public void run() {
        while (true) {
            try {
                showMenu();
                System.out.println("\n> ");
                String option = scanner.nextLine();
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
                        addFriendship();
                        break;
                    case "6":
                        removeFriendship();
                        break;
                    case "7":
                        getCommunitiesCount();
                        break;
                    case "8":
                        getMostSociableCommunity();
                        break;
                    case "9":
                        getUserFriendships();
                        break;
                    case "10":
                        getUserFriendshipsInMonth();
                        break;
                    case "11":
                        sendMessage();
                        break;
                    case "12":
                        listConversations();
                        break;
                    case "13":
                        viewConversation();
                        break;
                    case "14":
                        replyToMessage();
                        break;
                    default:
                        System.out.println("\ncommand not recognised!");
                        break;
                }
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
        System.out.println("    [5]: Add friendship");
        System.out.println("    [6]: Remove friendship");
        System.out.println("--Communities---------");
        System.out.println("    [7]: Communities count");
        System.out.println("    [8]: Most sociable community");
        System.out.println("--Friends-------------");
        System.out.println("    [9]: User friendships");
        System.out.println("    [10]: User friendships in month");
        System.out.println("--Messages------------");
        System.out.println("    [11]: Send message");
        System.out.println("    [12]: List conversations");
        System.out.println("    [13]: View conversation");
        System.out.println("    [14]: Reply to message");
        System.out.println("----------------------");
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

    private void addFriendship() {
        System.out.print("Friend 1 id: ");
        String id1 = scanner.nextLine();
        System.out.print("Friend 2 id: ");
        String id2 = scanner.nextLine();
        try {
            Long.parseLong(id1);
            Long.parseLong(id2);
        } catch (Exception e) {
            id1 = id2 = "-1";
        }

        service.addFriendship(Long.parseLong(id1), Long.parseLong(id2));
    }

    private void removeFriendship() {
        System.out.print("Friendship id: ");
        String id = scanner.nextLine();
        try {
            Long.parseLong(id);
        } catch (Exception e) {
            id = "-1";
        }

        service.removeFriendship(Long.parseLong(id));
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

        Map<User, LocalDateTime> friends = service.getUserFriendshipsInMonth(Long.parseLong(id), Integer.parseInt(month));
        for (User user : friends.keySet()) {
            System.out.println(user.getLastName() + "|" + user.getFirstName() + "|" + friends.get(user));
        }
    }

    private void sendMessage() {
        System.out.print("User id: ");
        long id = Long.parseLong(scanner.nextLine());
        System.out.print("Send to: ");
        List<Long> to = Arrays.stream(scanner.nextLine().split(",")).map(Long::parseLong).collect(Collectors.toCollection(Vector::new));
        System.out.print("Message: ");
        String message = scanner.nextLine();

        service.sendMessage(id, to, message);
    }

    private void listConversations() {
        Iterable<Message> conversations = service.getConversations();
        conversations.forEach(System.out::println);
    }

    private void viewConversation() {

    }

    private void replyToMessage() {

    }
}
