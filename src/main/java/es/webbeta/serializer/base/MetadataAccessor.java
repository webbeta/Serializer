package es.webbeta.serializer.base;

import java.nio.file.Path;

public interface IMetadataAccessor {

    void setMetadataPath(Path path);
    Boolean hasMetadata(Class klass);
    String getMetadataContent(Class klass);

}
