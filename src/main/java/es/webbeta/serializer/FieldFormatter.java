package es.webbeta.serializer;

import com.google.common.base.Ascii;
import com.google.common.base.CaseFormat;
import es.webbeta.serializer.base.IFieldFormatter;
import es.webbeta.serializer.type.FieldFormatterType;

public class FieldFormatter implements IFieldFormatter {

    private FieldFormatterType formatterType;

    public FieldFormatter(FieldFormatterType type) {
        formatterType = type;
    }

    public FieldFormatter(String type) {
        this(FieldFormatterType.fromString(type));
    }

    public String format(String name) {
        if (name.contains(" "))
            throw new IllegalArgumentException("A field cannot have empty spaces.");

        if (formatterType == FieldFormatterType.INHERITED)
            return name;
        else {
            Boolean hasDashOrUnderscore = name.contains("-") || name.contains("_");

            if (hasDashOrUnderscore) {
                name = name.replace("-", "_");
                name = name.toLowerCase();

                return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.valueOf(formatterType.getStringType().toUpperCase()), name);
            } else {
                name = String.valueOf(Ascii.toLowerCase(name.charAt(0))) + name.substring(1);

                return CaseFormat.LOWER_CAMEL.to(CaseFormat.valueOf(formatterType.getStringType().toUpperCase()), name);
            }
        }
    }

}
