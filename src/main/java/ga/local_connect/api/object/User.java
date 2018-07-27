package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class User {
    private String id;
    private Group group;
    private String name;
    private Timestamp createdAt;

    public User(String id, Group group, String name, Timestamp createdAt) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.createdAt = createdAt;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public Group getGroup() {
        return group;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
