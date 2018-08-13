package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class Profile {
    private String id;
    private User user;
    private String hobbies;
    private String favorites;
    private String mottoes;
    private Timestamp createdAt;

    public Profile(String id, User user, String hobbies, String favorites, String mottoes, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.hobbies = hobbies;
        this.favorites = favorites;
        this.mottoes = mottoes;
        this.createdAt = createdAt;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public User getUser() {
        return user;
    }

    @JsonProperty
    public String getHobbies() {
        return hobbies;
    }

    @JsonProperty
    public String getFavorites() {
        return favorites;
    }

    @JsonProperty
    public String getMottoes() {
        return mottoes;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
