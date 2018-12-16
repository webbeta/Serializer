package es.webbeta.serializer;

import es.webbeta.serializer.base.MetadataAccessor;
import es.webbeta.serializer.base.ParentFieldData;
import es.webbeta.serializer.base.SerializerMetadataProvider;
import es.webbeta.serializer.metadata.*;
import es.webbeta.serializer.type.FieldAccessType;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class SerializerYamlMetadataProvider implements SerializerMetadataProvider {

    private MetadataAccessor metadataAccessor;

    private Map<String, Metadata> metadatas;

    public SerializerYamlMetadataProvider(MetadataAccessor metadataAccessor) {
        this.metadataAccessor = metadataAccessor;

        metadatas = new HashMap<>();
    }

    private void initializeMetadata(Class<?> klass) {
        if (metadatas.containsKey(klass.getCanonicalName()))
            return;

        Yaml yaml = new Yaml();

        if (!metadataAccessor.hasMetadata(klass)) {
            putNullMetadata(klass);
            return;
        }

        String yamlFileContents = metadataAccessor.getMetadataContent(klass);
        Metadata metadata = MetadataConstructor.build(yaml.loadAs(yamlFileContents, Map.class));

        if (metadata == null || !metadata.appliesTo(klass.getCanonicalName())) {
            putNullMetadata(klass);
            return;
        }

        this.metadatas.put(klass.getCanonicalName(), metadata);
    }

    private void putNullMetadata(Class<?> klass) {
        this.metadatas.put(klass.getCanonicalName(), null);
    }

    private Boolean containsMetadata(Class<?> klass) {
        return metadatas.containsKey(klass.getCanonicalName()) &&
                metadatas.get(klass.getCanonicalName()) != null;
    }

    private Metadata getMetadata(Class<?> klass) {
        return metadatas.get(klass.getCanonicalName());
    }

    @Override
    public Boolean canProvide(Class<?> klass) {
        initializeMetadata(klass);

        return metadataAccessor.hasMetadata(klass) &&
                containsMetadata(klass);
    }

    @Override
    public String[] getPropertiesByGroup(Class<?> klass, ParentFieldData parentData, String... group) {
        initializeMetadata(klass);

        if (!containsMetadata(klass))
            return new String[0];

        List<String> properties = new ArrayList<>();

        Metadata metadata = getMetadata(klass);

        if (metadata.hasProperties()) {
            for (MetadataProperty property : metadata.getProperties()) {
                if (parentData != null && parentData.isRecursive(klass, property.getPropertyName()))
                    continue;

                if (!property.hasGroups())
                    continue;

                if (property.appliesToGroups(Arrays.asList(group)))
                    properties.add(property.getPropertyName());
            }
        }

        if (metadata.hasVirtualProperties()) {
            for (MetadataVirtualProperty property : metadata.getVirtualProperties()) {
                if (parentData != null && parentData.isRecursive(klass, property.getPropertyName()))
                    continue;

                if (!property.hasGroups())
                    continue;

                if (property.appliesToGroups(Arrays.asList(group)))
                    properties.add(property.getPropertyName());
            }
        }

        return properties.toArray(new String[0]);
    }

    @Override
    public String[] getGroupsByFieldName(Class<?> klass, String fieldName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(fieldName);
        return property.getGroups().toArray(new String[0]);
    }

    @Override
    public Boolean hasAccessType(Class<?> klass) {
        Metadata metadata = getMetadata(klass);
        return metadata.hasAccessType();
    }

    @Override
    public FieldAccessType getAccessType(Class<?> klass) {
        Metadata metadata = getMetadata(klass);
        return metadata.getAccessType();
    }

    @Override
    public Boolean hasPropertyAccessType(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        return property.hasAccessType();
    }

    @Override
    public FieldAccessType getPropertyAccessType(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        return property.getAccessType();
    }

    @Override
    public String getPropertyCustomGetterName(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        MetadataPropertyAccessor accessor = property.getAccessor();
        if (accessor == null || !accessor.hasGetter())
            return null;
        else
            return accessor.getGetter();
    }

    @Override
    public Boolean hasPropertySerializedName(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        return property.hasSerializedName();
    }

    @Override
    public String getPropertySerializedName(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        return property.getSerializedName();
    }

    @Override
    public Boolean isVirtualProperty(Class<?> klass, String propertyName) {
        Metadata metadata = getMetadata(klass);
        IMetadataProperty property = metadata.getMixedProperty(propertyName);
        return property instanceof MetadataVirtualProperty;
    }

}