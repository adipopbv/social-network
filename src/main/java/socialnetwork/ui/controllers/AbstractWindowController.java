package socialnetwork.ui.controllers;

import socialnetwork.domain.User;
import socialnetwork.service.SocialNetworkService;
import socialnetwork.utils.observers.Observer;

public abstract class AbstractWindowController implements Observer {
    protected SocialNetworkService service;
    protected User loggedUser;

    public void init(SocialNetworkService service, User loggedUser) {
        this.service = service;
        service.addObserver(this);
        this.loggedUser = loggedUser;
        update();
    }
}
