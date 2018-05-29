package es.webbeta.serializer;

import java.nio.file.Path;

public interface IMetadataAccessor {

    void setMetadataPath(Path path);
    Boolean hasMetadata(Class klass);
    String getMetadataContent(Class klass);

}
