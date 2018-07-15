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
    private Timestamp updatedAt;

    public Profile(String id, User user, String hobbies, String favorites, String mottoes, Timestamp updatedAt) {
        this.id = id;
        this.user = user;
        this.hobbies = hobbies;
        this.favorites = favorites;
        this.mottoes = mottoes;
        this.updatedAt = updatedAt;
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

    @JsonProperty("updated_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
