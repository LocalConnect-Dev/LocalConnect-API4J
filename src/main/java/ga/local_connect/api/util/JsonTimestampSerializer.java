package ga.local_connect.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class JsonTimestampSerializer extends JsonSerializer<Timestamp> {
    @Override
    public void serialize(Timestamp timestamp, JsonGenerator generator,
                          SerializerProvider provider) throws IOException {
        var seconds = timestamp.getTime() / 1000;
        generator.writeNumber(seconds);
    }
}
