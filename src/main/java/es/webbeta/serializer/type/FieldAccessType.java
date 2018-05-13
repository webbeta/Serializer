package es.webbeta.serializer.type;

public enum FieldAccessType {
    PUBLIC_METHOD ("public_method"),
    PROPERTY ("property");

    private String stringType;

    FieldAccessType(String stringType) {
        this.stringType = stringType;
    }

    public String getStringType() {
        return this.stringType;
    }

    public static FieldAccessType fromString(String text) {
        for (FieldAccessType type : FieldAccessType.values()) {
            if (type.stringType.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No field access with text " + text + " found.");
    }
}