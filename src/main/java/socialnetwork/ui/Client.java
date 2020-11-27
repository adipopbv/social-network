package socialnetwork.ui;

import socialnetwork.service.Service;

import java.util.*;

public abstract class Client {
    protected final Service service;
    protected final Scanner scanner;

    public Client(Service service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Runs the application
     */
    public abstract void run();
}
