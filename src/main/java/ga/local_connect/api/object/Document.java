package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class Document {
    private String id;
    private User author;
    private String title;
    private String content;
    private Timestamp updatedAt;

    public Document(String id, User author, String title, String content, Timestamp updatedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
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
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty("updated_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
