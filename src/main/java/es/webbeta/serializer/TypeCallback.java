package es.webbeta.serializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public abstract class TypeCallback implements es.webbeta.serializer.base.TypeCallback {

    @Override
    public void itsByte(Byte value) throws IOException {}

    @Override
    public void itsShort(Short value) throws IOException {}

    @Override
    public void itsInteger(Integer value) throws IOException {}

    @Override
    public void itsBigInteger(BigInteger value) throws IOException {}

    @Override
    public void itsLong(Long value) throws IOException {}

    @Override
    public void itsFloat(Float value) throws IOException {}

    @Override
    public void itsDouble(Double value) throws IOException {}

    @Override
    public void itsBigDecimal(BigDecimal value) throws IOException {}

    @Override
    public void itsNumeric(Object value) throws IOException {}

    @Override
    public void itsString(String value) throws IOException {}

    @Override
    public void itsStringParseable(Object value) throws IOException {}

    @Override
    public void itsBoolean(Boolean value) throws IOException {}

    @Override
    public void itsDate(Date value) throws IOException {}

    @Override
    public void itsSerializableObject(Object value) throws IOException {}

    @Override
    public void itsUnserializableObject(Object value) throws IOException {}

    @Override
    public void itsIterable(Iterable<?> value) throws IOException {}

    @Override
    public void itsMap(Map<?, ?> value) throws IOException {}

}
