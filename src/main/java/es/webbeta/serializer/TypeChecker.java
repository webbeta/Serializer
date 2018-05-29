package es.webbeta.serializer;

import es.webbeta.serializer.base.ISerializerMetadataProvider;
import es.webbeta.serializer.base.ITypeCallback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class TypeChecker {

    private ISerializerMetadataProvider provider;

    public TypeChecker(ISerializerMetadataProvider provider) {
        this.provider = provider;
    }

    public <T> Boolean isByte(T value) {
        return value instanceof Byte;
    }

    public <T> Boolean isShort(T value) {
        return value instanceof Short;
    }

    public <T> Boolean isInteger(T value) {
        return value instanceof Integer;
    }

    public <T> Boolean isBigInteger(T value) {
        return value instanceof BigInteger;
    }

    public <T> Boolean isLong(T value) {
        return value instanceof Long;
    }

    public <T> Boolean isFloat(T value) {
        return value instanceof Float;
    }

    public <T> Boolean isDouble(T value) {
        return value instanceof Double;
    }

    public <T> Boolean isBigDecimal(T value) {
        return value instanceof BigDecimal;
    }

    public <T> Boolean isNumeric(T value) {
        return isByte(value) ||
                isShort(value) ||
                isInteger(value) ||
                isBigInteger(value) ||
                isLong(value) ||
                isFloat(value) ||
                isDouble(value) ||
                isBigDecimal(value);
    }

    public <T> Boolean isString(T value) {
        return value instanceof String;
    }

    public <T> Boolean isStringParseable(T value) {
        return value instanceof Enum<?> || value instanceof UUID;
    }

    public <T> Boolean isBoolean(T value) {
        return value instanceof Boolean;
    }

    public <T> Boolean isDate(T value) {
        return value instanceof Date;
    }

    public <T> Boolean isSerializableObject(T value) {
        return !(value instanceof Iterable) && provider.canProvide(value.getClass());
    }

    public <T> Boolean isUnserializableObject(T value) {
        return !isNumeric(value) &&
                !isString(value) &&
                !isStringParseable(value) &&
                !isBoolean(value) &&
                !isDate(value) &&
                !isIterable(value) &&
                !isMap(value) &&
                !provider.canProvide(value.getClass());
    }

    public <T> Boolean isIterable(T value) {
        return value instanceof Iterable;
    }

    public <T> Boolean isMap(T value) {
        return value instanceof Map;
    }

    public <T> void check(T value, ITypeCallback callback) throws IOException {
        if (isByte(value)) {
            callback.itsByte((Byte) value);
            callback.itsNumeric(value);
        } else if (isShort(value)) {
            callback.itsShort((Short) value);
            callback.itsNumeric(value);
        } else if (isInteger(value)) {
            callback.itsInteger((Integer) value);
            callback.itsNumeric(value);
        } else if (isBigInteger(value)) {
            callback.itsBigInteger((BigInteger) value);
            callback.itsNumeric(value);
        } else if (isLong(value)) {
            callback.itsLong((Long) value);
            callback.itsNumeric(value);
        } else if (isFloat(value)) {
            callback.itsFloat((Float) value);
            callback.itsNumeric(value);
        } else if (isDouble(value)) {
            callback.itsDouble((Double) value);
            callback.itsNumeric(value);
        } else if (isBigDecimal(value)) {
            callback.itsBigDecimal((BigDecimal) value);
            callback.itsNumeric(value);
        } else if (isString(value)) {
            callback.itsString((String) value);
        } else if (isStringParseable(value))
            callback.itsStringParseable(value);
        else if (isBoolean(value))
            callback.itsBoolean((Boolean) value);
        else if (isDate(value))
            callback.itsDate((Date) value);
        else if (isUnserializableObject(value))
            callback.itsUnserializableObject(value);
        else if (isSerializableObject(value))
            callback.itsSerializableObject(value);
        else if (isIterable(value))
            callback.itsIterable((Iterable<?>) value);
        else if (isMap(value))
            callback.itsMap((Map<?, ?>) value);
    }

}
