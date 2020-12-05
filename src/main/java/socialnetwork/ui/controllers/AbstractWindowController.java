package socialnetwork.ui.controllers;

import socialnetwork.service.SocialNetworkService;

public abstract class AbstractWindowController {
    protected SocialNetworkService service;

    public void setService(SocialNetworkService service) {
        this.service = service;
    }
}
