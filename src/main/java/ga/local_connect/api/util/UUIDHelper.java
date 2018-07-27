package ga.local_connect.api.util;

import java.util.UUID;

public class UUIDHelper {
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
