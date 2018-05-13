package serializer;

import es.webbeta.serializer.FieldAccessor;
import es.webbeta.serializer.type.FieldAccessType;
import org.junit.Test;
import util.serializer.Child;
import util.serializer.ClassWithGetters;
import util.serializer.Foo;

import static org.fest.assertions.Assertions.assertThat;

public class FieldAccessorTest {

    @Test
    public void test_field_accessor_access_property_with_value_correctly() {
        ClassWithGetters cls = new ClassWithGetters();

        FieldAccessor accessor = new FieldAccessor(cls, "id", FieldAccessType.PROPERTY);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof Integer).isTrue();
        assertThat((Integer) accessor.get()).isEqualTo(500);
    }

    @Test
    public void test_field_accessor_access_property_with_NULL_value_correctly() {
        ClassWithGetters cls = new ClassWithGetters(null);

        FieldAccessor accessor = new FieldAccessor(cls, "id", FieldAccessType.PROPERTY);

        assertThat(accessor.exists()).isTrue();
        assertThat((Integer) accessor.get()).isEqualTo(null);
    }

    @Test
    public void test_field_accessor_access_getter_with_value_correctly() {
        ClassWithGetters cls = new ClassWithGetters();

        FieldAccessor accessor = new FieldAccessor(cls, "foo", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello world!");

        FieldAccessor accessor2 = new FieldAccessor(cls, "bar", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor2.exists()).isTrue();
        assertThat(accessor2.get() instanceof Boolean).isTrue();
        assertThat((Boolean) accessor2.get()).isTrue();
    }

    @Test
    public void test_field_accessor_access_getter_with_value_correctly_ensuring_that_field_exists() {
        ClassWithGetters cls = new ClassWithGetters();

        FieldAccessor accessor = new FieldAccessor(cls, "getterWithoutProperty", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isFalse();
        assertThat((String) accessor.get()).isEqualTo(null);
    }

    @Test
    public void test_field_accessor_access_property_with_value_correctly_using_a_custom_getter() {
        ClassWithGetters cls = new ClassWithGetters();

        FieldAccessor accessor = new FieldAccessor(cls, "barMethod2", FieldAccessType.PUBLIC_METHOD);
        accessor.setCustomGetterName("getBarMethodBis");

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Foo Bar Foo Bar");
    }

    @Test
    public void test_field_accessor_access_property_with_value_correctly_using_a_custom_getter_as_field_has_no_effect() {
        ClassWithGetters cls = new ClassWithGetters();

        FieldAccessor accessor = new FieldAccessor(cls, "barMethod3", FieldAccessType.PROPERTY);
        accessor.setCustomGetterName("getBarMethod3Bis");

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Foo");
    }

    @Test(expected = IllegalAccessError.class)
    public void test_field_accessor_as_public_method_will_throw_exception_when_class_has_not_public_modifier() {
        Foo cls = new Foo();

        FieldAccessor accessor = new FieldAccessor(cls.privateFoo, "id", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof Integer).isTrue();
        assertThat((Integer) accessor.get()).isEqualTo(654);
    }

    @Test
    public void test_field_accessor_can_access_property_over_class_parents() {
        Child cls = new Child();

        FieldAccessor accessor = new FieldAccessor(cls, "childField", FieldAccessType.PROPERTY);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello child!");

        accessor = new FieldAccessor(cls, "parentField", FieldAccessType.PROPERTY);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello parent!");

        accessor = new FieldAccessor(cls, "grandparentField", FieldAccessType.PROPERTY);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello grandparent!");
    }

    @Test
    public void test_field_accessor_can_access_getter_over_class_parents() {
        Child cls = new Child();

        FieldAccessor accessor = new FieldAccessor(cls, "childField", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello child!");

        accessor = new FieldAccessor(cls, "parentField", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello parent!");

        accessor = new FieldAccessor(cls, "grandparentField", FieldAccessType.PUBLIC_METHOD);

        assertThat(accessor.exists()).isTrue();
        assertThat(accessor.get() instanceof String).isTrue();
        assertThat((String) accessor.get()).isEqualTo("Hello grandparent!");
    }

}
