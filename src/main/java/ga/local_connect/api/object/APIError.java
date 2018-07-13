package ga.local_connect.api.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import ga.local_connect.api.enumeration.APIErrorType;

public class APIError {
    private final APIErrorType error;

    public APIError(APIErrorType error) {
        this.error = error;
    }

    @JsonProperty
    public APIErrorType getError() {
        return error;
    }
}
