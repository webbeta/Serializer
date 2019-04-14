package es.webbeta;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import es.webbeta.serializer.*;
import es.webbeta.serializer.base.FieldFormatter;
import es.webbeta.serializer.base.Logger;
import es.webbeta.serializer.base.ParentFieldData;
import es.webbeta.serializer.base.SerializerMetadataProvider;
import es.webbeta.serializer.type.DateFormatType;
import es.webbeta.serializer.type.FieldAccessType;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Serializer {

    private final ConfigurationManager configurationManager;

    private SerializerMetadataProvider provider;
    private FieldFormatter formatter;
    private TypeChecker typeChecker;

    private JsonGenerator generator;
    private StringWriter writer;

    private Logger logger;

    Serializer(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
        formatter = configurationManager.getFieldFormatter();
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Inits serialization context
     *
     * @return True if it went ok
     */
    private Boolean initializeGenerator() {
        writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        provider = configurationManager.newMetadataProvider();
        typeChecker = new TypeChecker(provider);

        try {
            generator = factory.createGenerator(writer);
            return true;
        } catch (IOException e) {
            generator = null;
            return false;
        }
    }

    private FieldAccessor buildFieldAccessor(Object ob, String fieldName) {
        Class<?> klass = ob.getClass();

        FieldAccessType accessType = configurationManager.getAccessType();
        if (provider.hasAccessType(klass))
            accessType = provider.getAccessType(klass);
        if (provider.hasPropertyAccessType(klass, fieldName))
            accessType = provider.getPropertyAccessType(klass, fieldName);

        String customGetter = provider.getPropertyCustomGetterName(klass, fieldName);

        FieldAccessor fieldAccessor = new FieldAccessor(ob, fieldName, accessType);
        fieldAccessor.setCustomGetterName(customGetter);

        if (logger != null) {
            fieldAccessor.setLogger(logger);
        }

        if (provider.isVirtualProperty(klass, fieldName))
            fieldAccessor.setEnsureFieldExists(false);

        return fieldAccessor;
    }

    private String getSerializedName(Class<?> klass, String fieldName) {
        String finalFieldName = formatter.format(fieldName);
        if (provider.hasPropertySerializedName(klass, fieldName))
            finalFieldName = provider.getPropertySerializedName(klass, fieldName);

        return finalFieldName;
    }

    private void fillDateValue(JsonGenerator gen, final Date date) throws IOException {
        if (configurationManager.getDateFormatType() == DateFormatType.ISO8601) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            gen.writeString(format.format(date));
        } else if (configurationManager.getDateFormatType() == DateFormatType.UNIX_TIMESTAMP)
            gen.writeNumber(date.getTime());
    }

    private void fillRawValue(JsonGenerator gen, final ParentFieldData parentFieldData, final Object value, final String[] group) throws IOException {
        typeChecker.check(value, new TypeCallback() {

            @Override
            public void itsByte(Byte value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsShort(Short value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsInteger(Integer value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsBigInteger(BigInteger value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsLong(Long value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsFloat(Float value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsDouble(Double value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsBigDecimal(BigDecimal value) throws IOException {
                gen.writeNumber(value);
            }

            @Override
            public void itsString(String value) throws IOException {
                gen.writeString(value);
            }

            @Override
            public void itsStringParseable(Object value) throws IOException {
                gen.writeString(value.toString());
            }

            @Override
            public void itsBoolean(Boolean value) throws IOException {
                gen.writeBoolean(value);
            }

            @Override
            public void itsDate(Date value) throws IOException {
                fillDateValue(gen, value);
            }

            @Override
            public void itsSerializableObject(Object value) throws IOException {
                if (parentFieldData != null && parentFieldData.getKlass() == value.getClass()) {
                    fillWith(false, gen, parentFieldData, value, parentFieldData.getGroups());
                } else
                    fillWith(false, gen, parentFieldData, value, group);
            }

            @Override
            public void itsIterable(Iterable<?> value) throws IOException {
                gen.writeStartArray();
                for (Object arrValue : value) {
                    String[] parentGroups = provider.getGroupsByFieldName(parentFieldData.getKlass(), parentFieldData.getFieldName());
                    es.webbeta.serializer.ParentFieldData fieldData =
                            new es.webbeta.serializer.ParentFieldData(parentFieldData.getKlass(), parentFieldData.getFieldName(), parentGroups);

                    fillRawValue(gen, fieldData, arrValue, group);
                }
                gen.writeEndArray();
            }

            @Override
            public void itsMap(Map<?,?> value) throws IOException {
                gen.writeStartObject();
                for (Map.Entry<?, ?> entry : value.entrySet()) {
                    String[] parentGroups = provider.getGroupsByFieldName(parentFieldData.getKlass(), parentFieldData.getFieldName());
                    es.webbeta.serializer.ParentFieldData fieldData =
                            new es.webbeta.serializer.ParentFieldData(parentFieldData.getKlass(), parentFieldData.getFieldName(), parentGroups);

                    gen.writeFieldName(entry.getKey().toString());
                    fillRawValue(gen, fieldData, entry.getValue(), group);
                }
                gen.writeEndObject();
            }

        });
    }

    private void fillWith(Boolean asArray, JsonGenerator gen, final ParentFieldData parentData, final Object ob, final String[] group) throws IOException {
        Class<?> klass = ob.getClass();
        String[] fields = provider.getPropertiesByGroup(klass, parentData, group);
        if (asArray)
            gen.writeStartArray();
        else
            gen.writeStartObject();

        if (typeChecker.isSerializableObject(ob)) {
            for (String fieldName : fields) {
                FieldAccessor accessor = buildFieldAccessor(ob, fieldName);

                if (accessor.get() == null) {
                    if (accessor.exists() && configurationManager.getIncludeNullValues()) {
                        gen.writeFieldName(getSerializedName(klass, fieldName));
                        gen.writeNull();
                    }
                    continue;
                }

                if (!typeChecker.isUnserializableObject(accessor.get())) {
                    es.webbeta.serializer.ParentFieldData fieldData =
                            new es.webbeta.serializer.ParentFieldData(klass, fieldName, group);

                    gen.writeFieldName(getSerializedName(klass, fieldName));
                    fillRawValue(gen, fieldData, accessor.get(), group);
                }
            }
        } else if (typeChecker.isIterable(ob)) {
            for (Object arrValue : (Iterable<?>) ob) {
                es.webbeta.serializer.ParentFieldData fieldData =
                        new es.webbeta.serializer.ParentFieldData(arrValue.getClass(), null, group);

                fillRawValue(gen, fieldData, arrValue, group);
            }
        }

        if (asArray)
            gen.writeEndArray();
        else
            gen.writeEndObject();
    }

    /**
     * It serializes an object without specifing serialization groups
     *
     * @param ob Object to serialize
     * @return JSON format string representing the object
     */
    public <T> String serialize(T ob) {
        return serialize(ob, new String[0]);
    }

    /**
     * It serializes an object specifing serialization groups
     *
     * @param ob    Object to serialize
     * @param group Serialization group or groups
     * @return JSON format string representing the object
     */
    public <T> String serialize(T ob, String... group) {
        if (ob == null) return null;

        if (!initializeGenerator())
            return null;

        try {
            fillWith(typeChecker.isIterable(ob), generator, null, ob, group);

            generator.close();

            return writer.toString();
        } catch (IOException e) {
            return null;
        }
    }

}
