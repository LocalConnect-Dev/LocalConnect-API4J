package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class EventAttendance {
    private String id;
    private User user;
    private Event event;
    private Timestamp createdAt;

    public EventAttendance(String id, User user, Event event, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.event = event;
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
    public Event getEvent() {
        return event;
    }

    @JsonProperty
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
