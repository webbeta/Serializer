package es.webbeta.serializer.metadata;

import es.webbeta.serializer.type.FieldAccessType;

import java.util.Collections;
import java.util.List;

public class MetadataProperty implements IMetadataProperty {

    private String propertyName;
    protected FieldAccessType accessType;
    protected MetadataPropertyAccessor accessor;
    private String serializedName;
    private List<String> groups;

    MetadataProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Boolean hasAccessType() {
        return accessType != null;
    }

    @Override
    public FieldAccessType getAccessType() {
        return accessType;
    }

    @Override
    public void setAccessType(FieldAccessType accessType) {
        this.accessType = accessType;
    }

    @Override
    public MetadataPropertyAccessor getAccessor() {
        return accessor;
    }

    @Override
    public void setAccessor(MetadataPropertyAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Boolean hasSerializedName() {
        return serializedName != null;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    @Override
    public void setSerializedName(String serializedName) {
        this.serializedName = serializedName;
    }

    @Override
    public Boolean hasGroups() {
        return groups.size() > 0;
    }

    @Override
    public Boolean appliesToGroups(List<String> wantedGroups) {
        return !Collections.disjoint(groups, wantedGroups);
    }

    @Override
    public List<String> getGroups() {
        return groups;
    }

    @Override
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
