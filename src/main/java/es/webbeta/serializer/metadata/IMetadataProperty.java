package es.webbeta.serializer.metadata;

import es.webbeta.serializer.type.FieldAccessType;

import java.util.List;

public interface IMetadataProperty {
    String getPropertyName();
    Boolean hasAccessType();
    FieldAccessType getAccessType();
    void setAccessType(FieldAccessType accessType);
    MetadataPropertyAccessor getAccessor();
    void setAccessor(MetadataPropertyAccessor accessor);
    Boolean hasSerializedName();
    String getSerializedName();
    void setSerializedName(String serializedName);
    Boolean hasGroups();
    Boolean appliesToGroups(List<String> wantedGroups);
    List<String> getGroups();
    void setGroups(List<String> groups);
}
