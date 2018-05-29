package serializer;

import es.webbeta.serializer.*;
import es.webbeta.serializer.base.Cache;
import es.webbeta.serializer.base.ConfigurationProvider;
import es.webbeta.serializer.base.Environment;
import es.webbeta.serializer.base.SerializerMetadataProvider;
import org.junit.Before;
import org.junit.Test;
import util.serializer.BeanWithWrongDefinedMetadata;
import util.serializer.BeanWithoutMetadata;
import util.serializer.Foo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

public class TypeCheckerTest {

    private static SerializerMetadataProvider metadataProvider;

    enum Sample {
        READ,
        WRITE
    }

    @Before
    public void setUp() throws Exception {
        Map<String, String> config = new HashMap<>();

        config.put(ConfigurationManager.METADATA_DIR_KEY, "src/test/resources");

        ConfigurationProvider configurationProvider = new ConfigurationProvider() {
            @Override
            public boolean getBoolean(String key, boolean defaultValue) {
                return false;
            }

            @Override
            public String getString(String key, String defaultValue) {
                return config.get(key) == null ? defaultValue : config.get(key);
            }
        };

        Environment environment = () -> false;

        FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor();
        fileMetadataAccessor.setMetadataPath(Paths.get(config.get(ConfigurationManager.METADATA_DIR_KEY)));

        Cache cache = new Cache() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public void set(String key, String content) {
            }

            @Override
            public void remove(String key) {
            }
        };

        ConfigurationManager configurationManager = new ConfigurationManager(configurationProvider, environment, cache);
        metadataProvider = configurationManager.newMetadataProvider();
    }

    @Test
    public void test_it_detects_byte_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Byte type = 1;
        byte typePrimitive = 1;

        assertThat(typeChecker.isByte(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isByte(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_short_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Short type = 1;
        short typePrimitive = 1;

        assertThat(typeChecker.isShort(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isShort(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_integer_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Integer type = 1;
        int typePrimitive = 1;

        assertThat(typeChecker.isInteger(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isInteger(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_biginteger_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        SecureRandom random = new SecureRandom();
        BigInteger type = new BigInteger(130, random);

        assertThat(typeChecker.isBigInteger(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
    }

    @Test
    public void test_it_detects_long_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Long type = 1L;
        long typePrimitive = 1L;

        assertThat(typeChecker.isLong(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isLong(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_float_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Float type = 1F;
        float typePrimitive = 1F;

        assertThat(typeChecker.isFloat(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isFloat(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_double_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Double type = 1.0;
        double typePrimitive = 1.0;

        assertThat(typeChecker.isDouble(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
        assertThat(typeChecker.isDouble(typePrimitive)).isTrue();
        assertThat(typeChecker.isNumeric(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_bigdecimal_as_numeric() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        BigDecimal type = new BigDecimal(1.0);

        assertThat(typeChecker.isBigDecimal(type)).isTrue();
        assertThat(typeChecker.isNumeric(type)).isTrue();
    }

    @Test
    public void test_it_detects_string() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        String type = "foo";

        assertThat(typeChecker.isString(type)).isTrue();
    }

    @Test
    public void test_it_detects_string_parseable() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        UUID typeUUID = UUID.randomUUID();
        Sample typeEnum = Sample.READ;

        assertThat(typeChecker.isStringParseable(typeUUID)).isTrue();
        assertThat(typeChecker.isStringParseable(typeEnum)).isTrue();
    }

    @Test
    public void test_it_detects_boolean() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Boolean type = true;
        boolean typePrimitive = false;

        assertThat(typeChecker.isBoolean(type)).isTrue();
        assertThat(typeChecker.isBoolean(typePrimitive)).isTrue();
    }

    @Test
    public void test_it_detects_date() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Date type = new Date();

        assertThat(typeChecker.isDate(type)).isTrue();
    }

    @Test
    public void test_it_detects_serializable_object() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Foo type = new Foo();

        assertThat(typeChecker.isSerializableObject(type)).isTrue();
        assertThat(typeChecker.isUnserializableObject(type)).isFalse();
    }

    @Test
    public void test_it_detects_unserializable_object_with_not_defined_metadata_object() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        BeanWithoutMetadata type = new BeanWithoutMetadata();

        assertThat(typeChecker.isUnserializableObject(type)).isTrue();
        assertThat(typeChecker.isSerializableObject(type)).isFalse();
    }

    @Test
    public void test_it_detects_unserializable_object_with_wrong_defined_metadata_object() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        BeanWithWrongDefinedMetadata type = new BeanWithWrongDefinedMetadata();

        assertThat(typeChecker.isUnserializableObject(type)).isTrue();
        assertThat(typeChecker.isSerializableObject(type)).isFalse();
    }

    @Test
    public void test_it_detects_iterable() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        List<Integer> typeList = new ArrayList<>();
        String[] typeStringArr = new String[] {"a", "b"};

        assertThat(typeChecker.isIterable(typeList)).isTrue();
        assertThat(typeChecker.isIterable(typeStringArr)).isFalse();
    }

    @Test
    public void test_it_detects_map() {
        TypeChecker typeChecker = new TypeChecker(metadataProvider);

        Map<Integer, Integer> type = new HashMap<>();

        assertThat(typeChecker.isMap(type)).isTrue();
    }
}
