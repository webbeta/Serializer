package es.webbeta.serializer.base;

import java.nio.file.Path;

public interface MetadataAccessor {

    void setMetadataPath(Path path);
    Boolean hasMetadata(Class klass);
    String getMetadataContent(Class klass);

}
