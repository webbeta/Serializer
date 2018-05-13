package serializer;

import es.webbeta.serializer.FieldFormatter;
import es.webbeta.serializer.type.FieldFormatterType;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FieldFormatterTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_formatter_throws_exception_with_fields_with_whitespace() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.INHERITED);

        assertThat(formatter.format("Hola don pepito")).isEqualTo("Holadonpepito");
    }

    @Test
    public void test_formatter_format_correctly_to_inherited_from_upper_camel() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.INHERITED);

        assertThat(formatter.format("HolaDonPepito")).isEqualTo("HolaDonPepito");
    }

    @Test
    public void test_formatter_format_correctly_to_inherited_from_mixed_string() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.INHERITED);

        assertThat(formatter.format("Hola_donPepito")).isEqualTo("Hola_donPepito");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_camel_from_upper_camel() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.LOWER_CAMEL);

        assertThat(formatter.format("HolaDonPepito")).isEqualTo("holaDonPepito");
    }

    @Test
    public void test_formatter_format_correctly_to_upper_camel_from_lower_camel() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.UPPER_CAMEL);

        assertThat(formatter.format("holaDonPepito")).isEqualTo("HolaDonPepito");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_camel_from_all_upper() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.LOWER_CAMEL);

        assertThat(formatter.format("HOLADONPEPITO")).isEqualTo("hOLADONPEPITO");
    }

    @Test
    public void test_formatter_format_correctly_to_upper_camel_from_all_upper() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.UPPER_CAMEL);

        assertThat(formatter.format("HOLADONPEPITO")).isEqualTo("HOLADONPEPITO");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_underscore_from_upper_camel() {
        FieldFormatter formatter = new FieldFormatter("lower_underscore");

        assertThat(formatter.format("HolaDonPepito")).isEqualTo("hola_don_pepito");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_underscore_from_mixed_string() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.LOWER_UNDERSCORE);

        assertThat(formatter.format("Hola_don-Pepito")).isEqualTo("hola_don_pepito");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_hyphen_from_upper_camel() {
        FieldFormatter formatter = new FieldFormatter("LOWER_HYPHEN");

        assertThat(formatter.format("HolaDonPepito")).isEqualTo("hola-don-pepito");
    }

    @Test
    public void test_formatter_format_correctly_to_lower_hyphen_from_mixed_string() {
        FieldFormatter formatter = new FieldFormatter(FieldFormatterType.LOWER_HYPHEN);

        assertThat(formatter.format("Hola_don-Pepito")).isEqualTo("hola-don-pepito");
    }

    @Test
    public void test_formatter_format_correctly_to_upper_underscore_from_upper_camel() {
        FieldFormatter formatter = new FieldFormatter("UppER_UNDERSCORE");

        assertThat(formatter.format("HolaDonPepito")).isEqualTo("HOLA_DON_PEPITO");
    }

}
