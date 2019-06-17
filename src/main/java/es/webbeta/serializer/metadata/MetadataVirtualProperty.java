package es.webbeta.serializer.metadata;

import es.webbeta.serializer.base.MetadataProperty;
import es.webbeta.serializer.type.FieldAccessType;

public class MetadataVirtualProperty extends es.webbeta.serializer.metadata.MetadataProperty implements MetadataProperty {

    MetadataVirtualProperty(String virtualPropertyName) {
        super(virtualPropertyName);
        this.accessType = FieldAccessType.PUBLIC_METHOD;
        this.accessor = new MetadataPropertyAccessor(virtualPropertyName);
    }

    @Override
    public void setAccessType(FieldAccessType accessType) {
        throw new IllegalArgumentException("Virtual properties have preset access type as PUBLIC_METHOD.");
    }

    @Override
    public void setAccessor(MetadataPropertyAccessor accessor) {
        throw new IllegalArgumentException("Virtual properties have preset accessor as its name.");
    }

}