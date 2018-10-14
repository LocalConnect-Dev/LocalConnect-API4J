package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class BoardRead {
    private String id;
    private User user;
    private Board board;
    private Timestamp createdAt;

    public BoardRead(String id, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.board = board;
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
    public Board getBoard() {
        return board;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
