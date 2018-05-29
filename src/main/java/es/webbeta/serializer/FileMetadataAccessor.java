package es.webbeta.serializer;

import es.webbeta.serializer.base.FileUtils;
import es.webbeta.serializer.base.IMetadataAccessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileMetadataAccessor implements IMetadataAccessor {

    private static final String YAML_EXT = ".yaml";
    private static final String YML_EXT = ".yml";

    protected Path metadataPath = null;

    private Path buildPath(Class klass, String extension) {
        return Paths.get(metadataPath.toString(), klass.getCanonicalName() + extension);
    }

    private Boolean hasYmlMetadata(Class klass) {
        return Files.exists(buildPath(klass, YML_EXT));
    }

    private Boolean hasYamlMetadata(Class klass) {
        return Files.exists(buildPath(klass, YAML_EXT));
    }

    @Override
    public void setMetadataPath(Path path) {
        metadataPath = path;
    }

    @Override
    public Boolean hasMetadata(Class klass) {
        return hasYmlMetadata(klass) ||
                hasYamlMetadata(klass);
    }

    @Override
    public String getMetadataContent(Class klass) {
        String contents = null;

        try {
            if (hasYmlMetadata(klass))
                contents = FileUtils.getContent(buildPath(klass, YML_EXT));
            else if(hasYamlMetadata(klass))
                contents = FileUtils.getContent(buildPath(klass, YAML_EXT));
        } catch (IOException ignored) {}

        return contents;
    }

}
