package ga.local_connect.api.util;

public class EnumHelper {
    public static <T extends Enum<?>> T valueOf(Class<T> enumeration, String search) {
        for (var value : enumeration.getEnumConstants()) {
            if (value.name().compareToIgnoreCase(search) == 0) {
                return value;
            }
        }

        return null;
    }
}
