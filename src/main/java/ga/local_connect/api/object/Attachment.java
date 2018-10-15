package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attachment {
    private String id;
    private String type;
    private Attachable object;

    public Attachment(String id, String type, Attachable object) {
        this.id = id;
        this.type = type;
        this.object = object;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public Attachable getObject() {
        return object;
    }
}
