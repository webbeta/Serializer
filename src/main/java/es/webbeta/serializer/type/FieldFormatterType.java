package es.webbeta.serializer.type;

public enum FieldFormatterType {
    INHERITED ("inherited"),
    LOWER_HYPHEN ("lower_hyphen"),
    LOWER_UNDERSCORE ("lower_underscore"),
    LOWER_CAMEL ("lower_camel"),
    UPPER_CAMEL ("upper_camel"),
    UPPER_UNDERSCORE ("upper_underscore");

    private String stringType;

    FieldFormatterType(String stringType) {
        this.stringType = stringType;
    }

    public String getStringType() {
        return this.stringType;
    }

    public static FieldFormatterType fromString(String text) {
        for (FieldFormatterType type : FieldFormatterType.values()) {
            if (type.stringType.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No formatter with text " + text + " found.");
    }
}