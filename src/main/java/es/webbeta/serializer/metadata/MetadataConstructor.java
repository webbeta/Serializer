package es.webbeta.serializer.metadata;

import es.webbeta.serializer.base.MetadataProperty;
import es.webbeta.serializer.type.FieldAccessType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MetadataConstructor {

    private static final String KEY_VIRTUAL_PROPERTIES = "virtual_properties";
    private static final String KEY_PROPERTIES = "properties";
    private static final String KEY_ACCESS_TYPE = "access_type";
    private static final String KEY_ACCESSOR = "accessor";
    private static final String KEY_SERIALIZED_NAME = "serialized_name";

    private static final String KEY_GROUPS = "groups";
    private static final String KEY_GETTER = "getter";

    public static Metadata build(Map<?, ?> map) {
        if (map == null) return null;
        Map.Entry<String, Map<String, Object>> root =
                (Map.Entry<String, Map<String, Object>>) map.entrySet().iterator().next();

        String canonicalName = root.getKey();
        Map<String, Object> modifiers = root.getValue();

        Metadata metadata = new Metadata(canonicalName);

        for (Map.Entry<String, Object> modifierEntry : modifiers.entrySet()) {
            String key = modifierEntry.getKey();

            switch (key) {
                case KEY_PROPERTIES: {
                    List<es.webbeta.serializer.metadata.MetadataProperty> properties =
                            buildProperties((Map<String, Map<String, Object>>) modifierEntry.getValue(), false);
                    metadata.setProperties(properties);
                    break;
                }
                case KEY_ACCESS_TYPE:
                    metadata.setAccessType(FieldAccessType.fromString((String) modifierEntry.getValue()));
                    break;
                case KEY_VIRTUAL_PROPERTIES: {
                    List<MetadataVirtualProperty> properties =
                            buildProperties((Map<String, Map<String, Object>>) modifierEntry.getValue(), true);
                    metadata.setVirtualProperties(properties);
                    break;
                }
            }
        }

        return metadata;
    }

    private static <T extends MetadataProperty> List<T> buildProperties(Map<String, Map<String, Object>> map, Boolean asVirtualProperties) {
        List<MetadataProperty> properties = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> propertyEntry : map.entrySet()) {
            String propertyName = propertyEntry.getKey();
            Map<String, Object> args = propertyEntry.getValue();

            MetadataProperty metadataProperty = asVirtualProperties ?
                    new MetadataVirtualProperty(propertyName) :
                    new es.webbeta.serializer.metadata.MetadataProperty(propertyName);

            for (Map.Entry<String, Object> argEntry : args.entrySet()) {
                String key = argEntry.getKey();

                switch (key) {
                    case KEY_GROUPS:
                        List<String> groups = new ArrayList<>();
                        for (Object rawGroup : (List<?>) argEntry.getValue()) {
                            String group = (String) rawGroup;
                            groups.add(group);
                        }
                        metadataProperty.setGroups(groups);
                        break;
                    case KEY_SERIALIZED_NAME:
                        metadataProperty.setSerializedName((String) argEntry.getValue());
                        break;
                }

                if (!asVirtualProperties) {
                    switch (key) {
                        case KEY_ACCESS_TYPE:
                            metadataProperty.setAccessType(FieldAccessType.fromString((String) argEntry.getValue()));
                            break;
                        case KEY_ACCESSOR:
                            MetadataPropertyAccessor accessor =
                                    buildPropertyAccessor(metadataProperty, (Map<String, String>) argEntry.getValue());
                            metadataProperty.setAccessor(accessor);
                            break;
                    }
                }
            }

            properties.add(metadataProperty);
        }

        return (List<T>) properties;
    }

    private static MetadataPropertyAccessor buildPropertyAccessor(MetadataProperty metadataProperty, Map<String, String> map) {
        if (metadataProperty.getAccessType() != FieldAccessType.PUBLIC_METHOD) return null;

        String getter = null;
        for (Map.Entry<String, String> argEntry : map.entrySet()) {
            String key = argEntry.getKey();

            if (key.equals(KEY_GETTER))
                getter = argEntry.getValue();
        }
        return (getter == null) ?
                null :
                new MetadataPropertyAccessor(getter);
    }

}
