package es.webbeta.serializer.type;

public enum DateFormatType {
    UNIX_TIMESTAMP ("unix_timestamp"),
    ISO8601 ("iso8601");

    private String stringType;

    DateFormatType(String stringType) {
        this.stringType = stringType;
    }

    public String getStringType() {
        return this.stringType;
    }

    public static DateFormatType fromString(String text) {
        for (DateFormatType type : DateFormatType.values()) {
            if (type.stringType.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No date format with text " + text + " found.");
    }
}