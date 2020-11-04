package socialnetwork.ui;

import org.w3c.dom.ls.LSOutput;
import socialnetwork.domain.exceptions.SocialNetworkException;
import socialnetwork.service.Service;

import java.util.Scanner;

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
                    case "7":
                        getCommunitiesCount();
                        break;
                    case "8":
                        getMostSociableCommunity();
                        break;
                    default:
                        System.out.println("\ncommand not recognised!");
                        break;
                }
            }
            catch (SocialNetworkException exception) {
                System.out.println(exception.toString());
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
        System.out.println("    [7]: Communities count");
        System.out.println("    [8]: Most sociable community");
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
        }
        catch (Exception e) {
            id1 = id2 = "-1";
        }

        service.addFriendship(Long.parseLong(id1), Long.parseLong(id2));
    }

    private void removeFriendship() {
        System.out.print("Friendship id: ");
        String id = scanner.nextLine();
        try {
            Long.parseLong(id);
        }
        catch (Exception e) {
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
}
