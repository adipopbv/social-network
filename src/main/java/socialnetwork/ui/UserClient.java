package socialnetwork.ui;

import socialnetwork.service.UserService;

import java.util.Scanner;

public class UserClient {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    public UserClient(UserService userService) {
        this.userService = userService;
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
                        addUser();
                        break;
                    case "3":
                        removeUser();
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
        System.out.println("    [2]: Add user");
        System.out.println("    [3]: Remove user");
        System.out.println("----------------------");
    }

    private void showAllUsers() {
        userService.getAll().forEach(System.out::println);
    }

    private void addUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        userService.addUser(firstName, lastName);
    }

    private void removeUser() {
        System.out.print("id: ");
        String idStr = scanner.nextLine();
        long id = Long.parseLong(idStr);
        userService.removeUser(id);
    }
}
