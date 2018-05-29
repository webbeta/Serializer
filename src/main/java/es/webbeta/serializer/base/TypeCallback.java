package es.webbeta.serializer.base;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public interface TypeCallback {
    void itsByte(Byte value) throws IOException;
    void itsShort(Short value) throws IOException;
    void itsInteger(Integer value) throws IOException;
    void itsBigInteger(BigInteger value) throws IOException;
    void itsLong(Long value) throws IOException;
    void itsFloat(Float value) throws IOException;
    void itsDouble(Double value) throws IOException;
    void itsBigDecimal(BigDecimal value) throws IOException;
    void itsNumeric(Object value) throws IOException;
    void itsString(String value) throws IOException;
    void itsStringParseable(Object value) throws IOException;
    void itsBoolean(Boolean value) throws IOException;
    void itsDate(Date value) throws IOException;
    void itsSerializableObject(Object value) throws IOException;
    void itsUnserializableObject(Object value) throws IOException;
    void itsIterable(Iterable<?> value) throws IOException;
    void itsMap(Map<?, ?> value) throws IOException;
}
