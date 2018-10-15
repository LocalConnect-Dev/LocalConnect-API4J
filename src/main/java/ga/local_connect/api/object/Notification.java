package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
    private Object obj;

    public Notification(Object obj) {
        this.obj = obj;
    }

    @JsonProperty
    public String getType() {
        return obj.getClass().getSimpleName();
    }

    @JsonProperty
    public Object getObject() {
        return obj;
    }
}
