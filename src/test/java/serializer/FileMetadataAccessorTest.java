package serializer;

import es.webbeta.serializer.FileMetadataAccessor;
import es.webbeta.serializer.base.IMetadataAccessor;
import org.junit.BeforeClass;
import org.junit.Test;
import util.serializer.Bar;
import util.serializer.BeanWithoutMetadata;
import util.serializer.Foo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;


public class FileMetadataAccessorTest {

    private static IMetadataAccessor accessor;

    private static Path metadataPath = null;

    @BeforeClass
    public static void init() {
        metadataPath = Paths.get("./src/test/resources/accessor_metadatas");

        accessor = new FileMetadataAccessor();
        accessor.setMetadataPath(metadataPath);
    }

    @Test
    public void test_has_metadata_for_bean_defined_by_yaml() {
        assertThat(accessor.hasMetadata(Foo.class)).isTrue();
    }

    @Test
    public void test_has_metadata_for_bean_defined_by_yml() {
        assertThat(accessor.hasMetadata(Bar.class)).isTrue();
    }

    @Test
    public void test_has_not_metadata_for_undefined_bean() {
        assertThat(accessor.hasMetadata(BeanWithoutMetadata.class)).isFalse();
    }

    @Test
    public void test_get_correct_contents_for_yaml() throws IOException {
        byte[] encoded = Files.readAllBytes(metadataPath.resolve(Foo.class.getCanonicalName() + ".yaml"));
        String content = new String(encoded, Charset.forName("UTF-8"));

        assertThat(accessor.getMetadataContent(Foo.class)).isEqualTo(content);
    }

    @Test
    public void test_get_correct_contents_for_yml() throws IOException {
        byte[] encoded = Files.readAllBytes(metadataPath.resolve(Bar.class.getCanonicalName() + ".yml"));
        String content = new String(encoded, Charset.forName("UTF-8"));

        assertThat(accessor.getMetadataContent(Bar.class)).isEqualTo(content);
    }

}
