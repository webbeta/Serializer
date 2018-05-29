package serializer;

import es.webbeta.serializer.*;
import es.webbeta.serializer.base.Cache;
import es.webbeta.serializer.base.ConfigurationProvider;
import es.webbeta.serializer.base.Environment;
import es.webbeta.serializer.base.SerializerMetadataProvider;
import org.junit.Test;
import util.serializer.Bar;
import util.serializer.BeanWithWrongDefinedMetadata;
import util.serializer.Foo;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class SerializerYamlMetadataProviderTest {

    public static ConfigurationManager buildAs(Boolean isProd) {
        Map<String, String> config = new HashMap<>();

        config.put(ConfigurationManager.METADATA_DIR_KEY, "src/test/resources/provider_metadatas");

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

        Environment environment = () -> isProd;

        FileMetadataAccessor fileMetadataAccessor = new FileMetadataAccessor();
        fileMetadataAccessor.setMetadataPath(Paths.get((String) config.get(ConfigurationManager.METADATA_DIR_KEY)));

        Cache cache = new Cache() {
            @Override
            public String get(String key) {
                if (key.equalsIgnoreCase(Foo.class.getCanonicalName()))
                    return fileMetadataAccessor.getMetadataContent(Foo.class);
                else if (key.equalsIgnoreCase(Bar.class.getCanonicalName()))
                    return fileMetadataAccessor.getMetadataContent(Bar.class);
                else if (key.equalsIgnoreCase(BeanWithWrongDefinedMetadata.class.getCanonicalName()))
                    return fileMetadataAccessor.getMetadataContent(BeanWithWrongDefinedMetadata.class);
                else
                    return null;
            }

            @Override
            public void set(String key, String content) {
            }

            @Override
            public void remove(String key) {
            }
        };

        return new ConfigurationManager(configurationProvider, environment, cache);
    }

    @Test
    public void test_can_provide_metadata_for_bean_defined_by_yaml() {
        ConfigurationManager configurationManager = buildAs(false);
        SerializerMetadataProvider provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof FileMetadataAccessor).isTrue();
        assertThat(provider.canProvide(Foo.class)).isTrue();

        configurationManager = buildAs(true);
        provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof CacheMetadataAccessor).isTrue();
        assertThat(provider.canProvide(Foo.class)).isTrue();
    }

    @Test
    public void test_can_provide_metadata_for_bean_defined_by_yml() {
        ConfigurationManager configurationManager = buildAs(false);
        SerializerMetadataProvider provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof FileMetadataAccessor).isTrue();
        assertThat(provider.canProvide(Bar.class)).isTrue();

        configurationManager = buildAs(true);
        provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof CacheMetadataAccessor).isTrue();
        assertThat(provider.canProvide(Bar.class)).isTrue();
    }

    @Test
    public void test_can_provide_metadata_for_bean_defined_with_correct_filename_but_bad_declaration() {
        ConfigurationManager configurationManager = buildAs(false);
        SerializerMetadataProvider provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof FileMetadataAccessor).isTrue();
        assertThat(provider.canProvide(BeanWithWrongDefinedMetadata.class)).isFalse();

        configurationManager = buildAs(true);
        provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof CacheMetadataAccessor).isTrue();
        assertThat(provider.canProvide(BeanWithWrongDefinedMetadata.class)).isFalse();
    }

    @Test
    public void test_provide_correct_properties_by_groups_for_metadata() {
        ConfigurationManager configurationManager = buildAs(false);
        SerializerMetadataProvider provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof FileMetadataAccessor).isTrue();
        assertThat(provider.getPropertiesByGroup(Foo.class, null, "grupo")).isEqualTo(new String[]{"id", "fooField"});

        configurationManager = buildAs(true);
        provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof CacheMetadataAccessor).isTrue();
        assertThat(provider.getPropertiesByGroup(Foo.class, null, "grupo")).isEqualTo(new String[]{"id", "fooField"});
    }

    @Test
    public void test_provide_correct_properties_by_groups_for_wrong_metadata() {
        ConfigurationManager configurationManager = buildAs(false);
        SerializerMetadataProvider provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof FileMetadataAccessor).isTrue();
        assertThat(provider.getPropertiesByGroup(BeanWithWrongDefinedMetadata.class, null, "grupo")).isEqualTo(new String[0]);

        configurationManager = buildAs(true);
        provider = configurationManager.newMetadataProvider();

        assertThat(configurationManager.getMetadataAccessor() instanceof CacheMetadataAccessor).isTrue();
        assertThat(provider.getPropertiesByGroup(BeanWithWrongDefinedMetadata.class, null, "grupo")).isEqualTo(new String[0]);
    }

}
