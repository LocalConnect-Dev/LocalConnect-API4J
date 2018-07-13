package ga.local_connect.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    public static String serialize(Object obj) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
