package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class User {
    private String id;
    private Group group;
    private UserType type;
    private String name;
    private Image avatar;
    private Timestamp createdAt;

    public User(String id, Group group, UserType type, String name, Image avatar, Timestamp createdAt) {
        this.id = id;
        this.group = group;
        this.type = type;
        this.name = name;
        this.avatar = avatar;
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
    public UserType getType() {
        return type;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public Image getAvatar() {
        return avatar;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
