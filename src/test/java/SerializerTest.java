import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.webbeta.Serializer;
import es.webbeta.serializer.*;
import es.webbeta.serializer.type.DateFormatType;
import es.webbeta.serializer.type.FieldAccessType;
import es.webbeta.serializer.type.FieldFormatterType;
import org.junit.Test;
import util.serializer.*;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

public class SerializerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static Serializer getSerializerAs(FieldFormatterType type, FieldAccessType accessType, DateFormatType dateFormatType, Boolean withNulls) {
        Map<String, Object> config = new HashMap<>();

        config.put(ConfigurationManager.METADATA_DIR_KEY, "src/test/resources");
        config.put(ConfigurationManager.INCLUDE_NULL_VALUES_KEY, withNulls);
        config.put(ConfigurationManager.FIELD_FORMATTING_METHOD_KEY, type.toString());
        config.put(ConfigurationManager.FIELD_ACCESS_TYPE_KEY, accessType.toString());
        config.put(ConfigurationManager.DATE_FORMAT_KEY, dateFormatType.toString());

        IConfigurationProvider configurationProvider = new IConfigurationProvider() {
            @Override
            public boolean getBoolean(String key, boolean defaultValue) {
                return config.get(key) == null ? defaultValue : Boolean.valueOf(config.get(key).toString());
            }

            @Override
            public String getString(String key, String defaultValue) {
                return config.get(key) == null ? defaultValue : config.get(key).toString();
            }
        };

        IEnvironment environment = () -> false;

        FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor();
        fileMetadataAccessor.setMetadataPath(Paths.get((String) config.get(ConfigurationManager.METADATA_DIR_KEY)));

        ICache cache = new ICache() {
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

        return new Serializer(configurationManager);
    }

    @Test
    public void test_bean_without_metadata_returns_empty_object() {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new BeanWithoutMetadata(), "a");

        assertThat(parsed).isEqualTo("{}");
    }

    @Test
    public void test_bean_with_wrong_defined_metadata_returns_empty_object() {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new BeanWithWrongDefinedMetadata(), "a");

        assertThat(parsed).isEqualTo("{}");
    }

    @Test
    public void test_scalar_fields_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "a");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("id", 654);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_bean_fields_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new util.serializer.Foo(), "c");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("fooField", "Random string");

        ObjectNode mockSubMap = mapper.createObjectNode();
        mockSubMap.put("barField", "Random string");

        mockMap.set("bar", mockSubMap);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_bean_fields_prints_correct_serialization_excluding_child_beans_with_wrong_metadata() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "only_correct_metadata");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("id", 654);

        ObjectNode mockSubMap = mapper.createObjectNode();
        mockSubMap.put("barField", "Random string");

        mockMap.set("bar", mockSubMap);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_bean_fields_prints_correct_serialization_including_null_values() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "c", "null_group");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("fooField", "Random string");

        ObjectNode mockSubMap = mapper.createObjectNode();
        mockSubMap.put("barField", "Random string");
        mockSubMap.putNull("nullField");

        mockMap.set("bar", mockSubMap);

        mockMap.putNull("nullField");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_bean_fields_prints_correct_serialization_including_null_values_only_if_field_exists() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "c", "null_group_unexistent");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("fooField", "Random string");

        ObjectNode mockSubMap = mapper.createObjectNode();
        mockSubMap.put("barField", "Random string");

        mockMap.set("bar", mockSubMap);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_bean_fields_prints_correct_serialization_NOT_including_null_values() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, false);

        String parsed = serializer.serialize(new Foo(), "c", "null_group");

        ObjectNode mockMap = mapper.createObjectNode();
        mockMap.put("fooField", "Random string");

        ObjectNode mockSubMap = mapper.createObjectNode();
        mockSubMap.put("barField", "Random string");

        mockMap.set("bar", mockSubMap);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_list_of_scalar_fields_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "list");

        ObjectNode mockMap = mapper.createObjectNode();
        ArrayNode mockArray = mapper.createArrayNode();
        mockArray.add("A");
        mockArray.add("B");
        mockMap.putArray("list").addAll(mockArray);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_list_of_bean_fields_prints_correct_serialization_with_upper_underscore() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.UPPER_UNDERSCORE, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "list_of_bean", "b");

        ObjectNode mockMap = mapper.createObjectNode();

        mockMap.put("ID", 654);
        mockMap.put("FOO_FIELD", "Random string");

        ArrayNode mockArray = mapper.createArrayNode();

        ObjectNode mockSubMap1 = mapper.createObjectNode();
        mockSubMap1.put("ID", 111);
        mockArray.add(mockSubMap1);

        ObjectNode mockSubMap2 = mapper.createObjectNode();
        mockSubMap2.put("ID", 112);
        mockArray.add(mockSubMap2);

        mockMap.putArray("LIST_OF_BEAN").addAll(mockArray);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_list_of_scalar_at_top_using_beans_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        List<Foo> listOfBeans = new ArrayList<>();

        listOfBeans.add(new Foo());
        Foo foo = new Foo();
        foo.setId(800);
        listOfBeans.add(foo);

        String parsed = serializer.serialize(listOfBeans, "a");

        ArrayNode mockArray = mapper.createArrayNode();

        ObjectNode mockMap1 = mapper.createObjectNode();
        mockMap1.put("id", 654);

        mockArray.add(mockMap1);

        ObjectNode mockMap2 = mapper.createObjectNode();
        mockMap2.put("id", 800);

        mockArray.add(mockMap2);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockArray));
    }

    @Test
    public void test_list_of_scalar_at_top_using_beans_with_folding_prints_correct_serialization_with_lower_hyphen() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.LOWER_HYPHEN, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        List<Foo> listOfBeans = new ArrayList<>();

        listOfBeans.add(new Foo());
        Foo foo = new Foo();
        foo.fooField = "Another string";
        listOfBeans.add(foo);

        String parsed = serializer.serialize(listOfBeans, "c");

        ArrayNode mockArray = mapper.createArrayNode();

        ObjectNode mockMap1 = mapper.createObjectNode();
        mockMap1.put("foo-field", "Random string");

        ObjectNode mockSubMap1 = mapper.createObjectNode();
        mockSubMap1.put("bar-field", "Random string");

        mockMap1.set("bar", mockSubMap1);

        mockArray.add(mockMap1);

        ObjectNode mockMap2 = mapper.createObjectNode();
        mockMap2.put("foo-field", "Another string");

        ObjectNode mockSubMap2 = mapper.createObjectNode();
        mockSubMap2.put("bar-field", "Random string");

        mockMap2.set("bar", mockSubMap2);

        mockArray.add(mockMap2);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockArray));
    }

    @Test
    public void test_list_of_scalar_at_top_using_scalar_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        List<String> listOfScalar = new ArrayList<>();

        listOfScalar.add("A");
        listOfScalar.add("B");

        String parsed = serializer.serialize(listOfScalar);

        ArrayNode mockArray = mapper.createArrayNode();
        mockArray.add("A");
        mockArray.add("B");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockArray));
    }

    @Test
    public void test_map_of_scalar_fields_prints_correct_serialization() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        String parsed = serializer.serialize(new Foo(), "map");

        ObjectNode mockMap = mapper.createObjectNode();
        ObjectNode mockMapValue = mapper.createObjectNode();
        mockMapValue.put("bar", 60.12);
        mockMapValue.put("foo", 50.1);
        mockMap.set("map", mockMapValue);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mockMap));
    }

    @Test
    public void test_simple_serialization_using_getters() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PUBLIC_METHOD, DateFormatType.ISO8601, true);

        ClassWithGetters foo = new ClassWithGetters();

        String parsed = serializer.serialize(foo, "a");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("foo", "Hello world!");
        mock.put("bar", true);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_PROPERTY_accessor_at_global_config_but_GETTERS_in_metadata_head_config() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters2 foo = new ClassWithGetters2();

        String parsed = serializer.serialize(foo, "a");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("foo", "Hello world2!");
        mock.put("bar", false);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_PROPERTY_accessor_at_global_config_but_GETTERS_in_metadata_head_config_and_PROPERTY_over_property_config() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters3 foo = new ClassWithGetters3();

        String parsed = serializer.serialize(foo, "a");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 200);
        mock.put("foo", "Hello world2!");
        mock.put("bar", false);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_PROPERTY_accessor_at_global_config_but_GETTERS_in_property_config() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters foo = new ClassWithGetters();

        String parsed = serializer.serialize(foo, "a", "b");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 500);
        mock.put("foo", "Hello world!");
        mock.put("bar", true);
        mock.put("barMethod", "Foo Bar");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_PROPERTY_accessor_at_global_config_but_GETTERS_in_property_config_with_custom_accessor() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters foo = new ClassWithGetters();

        String parsed = serializer.serialize(foo, "a", "c");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 500);
        mock.put("foo", "Hello world!");
        mock.put("bar", true);
        mock.put("barMethod2", "Foo Bar Foo Bar");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_the_SERIALIZED_NAME_property_modifier_over_a_field() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        Foo foo = new Foo();

        String parsed = serializer.serialize(foo, "a", "truncated_name");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 654);
        mock.put("anotherName", "Hello world!");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_the_SERIALIZED_NAME_property_modifier_over_a_method() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters2 foo = new ClassWithGetters2();

        String parsed = serializer.serialize(foo, "a", "truncated_name");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("foo", "Hello world2!");
        mock.put("bar", false);
        mock.put("anotherName", "Hello world!");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_virtual_properties() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters foo = new ClassWithGetters();

        String parsed = serializer.serialize(foo, "fake_group");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 500);
        mock.put("fake", "I'm fake!");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_simple_serialization_using_virtual_properties_without_serialized_name_will_get_method_name_as_key() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        ClassWithGetters foo = new ClassWithGetters();

        String parsed = serializer.serialize(foo, "fake_group2");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("getGetterWithoutProperty2", "I'm fake!");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_object_with_list_of_same_object_serializes_children_using_parent_groups() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        Foo foo = new Foo();

        Foo childFoo1 = new Foo();
        childFoo1.setId(1);

        foo.addRecursiveListItem(childFoo1);

        Foo childFoo2 = new Foo();
        childFoo2.setId(2);

        foo.addRecursiveListItem(childFoo2);

        String parsed = serializer.serialize(foo, "a", "recursive_foo");

        ArrayNode childsJson = mapper.createArrayNode();

        ObjectNode childMock1 = mapper.createObjectNode();
        childMock1.put("fooField", "Random string");

        childsJson.add(childMock1);

        ObjectNode childMock2 = mapper.createObjectNode();
        childMock2.put("fooField", "Random string");

        childsJson.add(childMock2);

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 654);
        mock.put("fooField", "Random string");
        mock.set("recursiveList", childsJson);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_object_with_list_of_same_object_serializes_children_map_using_parent_groups() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        Foo foo = new Foo();

        Foo childFoo1 = new Foo();
        childFoo1.setId(1);

        foo.addRecursiveMapItem("random_key_1", childFoo1);

        Foo childFoo2 = new Foo();
        childFoo2.setId(2);

        foo.addRecursiveMapItem("random_key_2", childFoo2);

        String parsed = serializer.serialize(foo, "a", "recursive_foo_map");

        ObjectNode childsJson = mapper.createObjectNode();

        ObjectNode childMock2 = mapper.createObjectNode();
        childMock2.put("fooField", "Random string");

        childsJson.set("random_key_2", childMock2);

        ObjectNode childMock1 = mapper.createObjectNode();
        childMock1.put("fooField", "Random string");

        childsJson.set("random_key_1", childMock1);

        ObjectNode mock = mapper.createObjectNode();
        mock.put("id", 654);
        mock.put("fooField", "Random string");
        mock.set("recursiveMap", childsJson);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_check_numeric_primitive_types() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        TypeChecks foo = new TypeChecks();

        String parsed = serializer.serialize(foo, "numerics");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("primitiveByte", 0);
        mock.put("objectByte", 1);
        mock.put("primitiveShort", 50);
        mock.put("objectShort", 50);
        mock.put("primitiveInt", 500);
        mock.put("objectInt", 500);
        mock.put("bigInt", 500);
        mock.put("primitiveLong", 500L);
        mock.put("objectLong", 500L);
        mock.put("primitiveDouble", 500.1);
        mock.put("objectDouble", 500.1);
        mock.put("primitiveFloat", 500.1F);
        mock.put("objectFloat", 500.1F);
        mock.put("bigDecimal", 500.1F);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_check_string_primitive_types() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        TypeChecks foo = new TypeChecks();

        String parsed = serializer.serialize(foo, "string");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("fooString", "Foo");
        mock.put("fooEnum", "A");
        mock.put("fooUUID", "8c748766-df38-421b-9eb2-0f0d2ffa2299");

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_check_boolean_primitive_types() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        TypeChecks foo = new TypeChecks();

        String parsed = serializer.serialize(foo, "boolean");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("primitiveBoolean", false);
        mock.put("objectBoolean", true);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_check_date_primitive_types_USING_ISO_DATE_FORMAT() throws JsonProcessingException {
        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.ISO8601, true);

        TypeChecks foo = new TypeChecks();

        String parsed = serializer.serialize(foo, "date");

        ObjectNode mock = mapper.createObjectNode();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mock.put("fooDate", dateFormat.format(foo.getFooDate()));
        mock.put("fooTimestamp", dateFormat.format(foo.getFooTimestamp()));

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));
    }

    @Test
    public void test_check_date_primitive_types_USING_UNIX_TIMESTAMP_DATE_FORMAT() throws JsonProcessingException {
        TimeZone sysTimezone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Serializer serializer = getSerializerAs(FieldFormatterType.INHERITED, FieldAccessType.PROPERTY, DateFormatType.UNIX_TIMESTAMP, true);

        TypeChecks foo = new TypeChecks();

        String parsed = serializer.serialize(foo, "date");

        ObjectNode mock = mapper.createObjectNode();
        mock.put("fooDate", 1506075072112L);
        mock.put("fooTimestamp", 1506075072112L);

        assertThat(parsed).isNotEmpty();
        assertThat(parsed).isEqualTo(mapper.writeValueAsString(mock));

        TimeZone.setDefault(sysTimezone);
    }

}
