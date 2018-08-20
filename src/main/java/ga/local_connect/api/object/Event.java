package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;
import java.util.List;

public class Event {
    private String id;
    private User author;
    private Document document;
    private Timestamp date;
    private List<EventAttendance> attendances;
    private Timestamp createdAt;

    public Event(String id, User author, Document document, Timestamp date, List<EventAttendance> attendances, Timestamp updatedAt) {
        this.id = id;
        this.author = author;
        this.document = document;
        this.date = date;
        this.attendances = attendances;
        this.createdAt = updatedAt;
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
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getDate() {
        return date;
    }

    @JsonProperty
    public List<EventAttendance> getAttendances() {
        return attendances;
    }

    @JsonProperty("created_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
