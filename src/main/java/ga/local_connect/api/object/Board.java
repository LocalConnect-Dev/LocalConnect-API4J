package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class Board {
    private String id;
    private Group group;
    private Document document;
    private Timestamp createdAt;

    public Board(String id, Group group, Document document, Timestamp updatedAt) {
        this.id = id;
        this.group = group;
        this.document = document;
        this.createdAt = updatedAt;
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
    public Document getDocument() {
        return document;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
