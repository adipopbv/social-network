package socialnetwork.ui;

import socialnetwork.domain.Friendship;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.UserService;

import java.util.Scanner;

public class Client {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Scanner scanner = new Scanner(System.in);

    public Client(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
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
                        return;
                    case "1":
                        showAllUsers();
                        break;
                    case "2":
                        showAllFriendships();
                        break;
                    case "3":
                        addUser();
                        break;
                    case "4":
                        removeUser();
                        break;
                    case "5":
                        addFriendship();
                        break;
                    case "6":
                        removeFriendship();
                        break;
                    default:
                        System.out.println("\ncommand not recognised!");
                        break;
                }
            }
            catch (RuntimeException exception) {
                System.out.println("\nError: " + exception);
            }
        }
    }

    private void showMenu() {
        System.out.println("\n--- Social Network ---");
        System.out.println("Commands:");
        System.out.println("    [0]: Exit");
        System.out.println("    [1]: Show all users");
        System.out.println("    [2]: Show all friendships");
        System.out.println("    [3]: Add user");
        System.out.println("    [4]: Remove user");
        System.out.println("    [5]: Add friendship");
        System.out.println("    [6]: Remove friendship");
        System.out.println("----------------------");
    }

    private void showAllUsers() {
        userService.getAll().forEach(System.out::println);
    }

    private void showAllFriendships() {
        friendshipService.getAll().forEach(System.out::println);
    }

    private void addUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        userService.addUser(firstName, lastName);
    }

    private void removeUser() {
        System.out.print("User id: ");
        String id = scanner.nextLine();

        userService.removeUser(Long.parseLong(id));
        friendshipService.removeFriendships(Long.parseLong(id));
    }

    private void addFriendship() {
        System.out.print("Friend 1 id: ");
        String id1 = scanner.nextLine();
        System.out.print("Friend 2 id: ");
        String id2 = scanner.nextLine();

        friendshipService.addFriendship(Long.parseLong(id1), Long.parseLong(id2));
        userService.addFriend(Long.parseLong(id1), Long.parseLong(id2));
        userService.addFriend(Long.parseLong(id2), Long.parseLong(id1));
    }

    private void removeFriendship() {
        System.out.print("Friendship id: ");
        String id = scanner.nextLine();

        Friendship friendship = friendshipService.removeFriendship(Long.parseLong(id));
        userService.removeFriend(friendship.getFriend1(), friendship.getFriend2());
        userService.removeFriend(friendship.getFriend2(), friendship.getFriend1());
    }
}
