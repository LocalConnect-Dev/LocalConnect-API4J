package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ga.local_connect.api.util.JsonTimestampSerializer;

import java.sql.Timestamp;

public class Group {
    private String id;
    private Region region;
    private String name;
    private Timestamp createdAt;

    public Group(String id, Region region, String name, Timestamp createdAt) {
        this.id = id;
        this.region = region;
        this.name = name;
        this.createdAt = createdAt;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public Region getRegion() {
        return region;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty("updated_at")
    @JsonSerialize(using = JsonTimestampSerializer.class)
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
