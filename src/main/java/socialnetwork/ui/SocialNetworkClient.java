package socialnetwork.ui;

import socialnetwork.service.SocialNetworkService;

import java.util.*;

public interface SocialNetworkClient {
    Scanner scanner = new Scanner(System.in);

    /**
     * Runs the application
     */
    public abstract void run(String[] args);
}
