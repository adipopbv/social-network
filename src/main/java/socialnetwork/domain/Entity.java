package socialnetwork.domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private Id id;

    public Id getId() {
        return id;
    }
    public void setId(Id id) {
        this.id = id;
    }
}