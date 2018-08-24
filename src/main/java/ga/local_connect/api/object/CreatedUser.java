package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class CreatedUser extends User {
    private String token;

    public CreatedUser(String id, Group group, String name, String token, Timestamp createdAt) {
        super(id, group, name, null, createdAt);
        this.token = token;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }
}
