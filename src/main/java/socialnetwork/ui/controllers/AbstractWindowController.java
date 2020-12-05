package socialnetwork.ui.controllers;

import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;

public abstract class AbstractWindowController {
    protected SocialNetworkService service;
    protected User loggedUser;

    public void init(SocialNetworkService service, User loggedUser) {
        this.service = service;
        this.loggedUser = loggedUser;
    }
}
