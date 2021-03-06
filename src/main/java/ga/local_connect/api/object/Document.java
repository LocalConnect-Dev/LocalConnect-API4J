package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;
import java.util.List;

public class Document {
    private String id;
    private User author;
    private String title;
    private String content;
    private List<Attachment> attachments;
    private Timestamp createdAt;

    public Document(String id, User author, String title, String content,
                    List<Attachment> attachments, Timestamp createdAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
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
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
