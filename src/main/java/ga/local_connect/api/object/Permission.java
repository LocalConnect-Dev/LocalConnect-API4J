package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permission {
    private String id;
    private String name;

    public Permission(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
