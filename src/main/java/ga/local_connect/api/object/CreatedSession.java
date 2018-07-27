package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class CreatedSession extends Session {
    private String secret;

    public CreatedSession(String id, User user, String secret, Timestamp createdAt) {
        super(id, user, createdAt);
        this.secret = secret;
    }

    @JsonProperty
    public String getSecret() {
        return secret;
    }
}
