package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Service {
    private String description;

    public Service(String description) {
        this.description = description;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
