package socialnetwork.domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
//    private static final long serialVersionUID = 7331115341259248461L;
    private Id id;

    public Id getId() {
        return id;
    }
    public void setId(Id id) {
        this.id = id;
    }
}