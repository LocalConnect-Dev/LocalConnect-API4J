package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;
import java.util.List;

public class Post {
    private String id;
    private User author;
    private Document document;
    private List<PostLike> likes;
    private Timestamp createdAt;

    public Post(String id, User author, Document document, List<PostLike> likes, Timestamp createdAt) {
        this.id = id;
        this.author = author;
        this.document = document;
        this.likes = likes;
        this.createdAt = createdAt;
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

    @JsonProperty
    public List<PostLike> getLikes() {
        return likes;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
