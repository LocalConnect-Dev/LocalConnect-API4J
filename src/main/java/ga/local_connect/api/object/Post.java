package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class Post {
    private String id;
    private User author;
    private Document document;
    private Timestamp updatedAt;

    public Post(String id, User author, Document document, Timestamp updatedAt) {
        this.id = id;
        this.author = author;
        this.document = document;
        this.updatedAt = updatedAt;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public User getAuthor() {
        return author;
    }

    @JsonProperty
    public Document getDocument() {
        return document;
    }

    @JsonProperty("updated_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
