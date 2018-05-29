package es.webbeta.serializer;

import es.webbeta.serializer.base.*;
import es.webbeta.serializer.type.DateFormatType;
import es.webbeta.serializer.type.FieldAccessType;
import es.webbeta.serializer.type.FieldFormatterType;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationManager {

    public static final String METADATA_DIR_KEY = "serializer.metadata.dir";
    public static final String INCLUDE_NULL_VALUES_KEY = "serializer.include_null_values";
    public static final String FIELD_FORMATTING_METHOD_KEY = "serializer.field_formatting_method";
    public static final String FIELD_ACCESS_TYPE_KEY = "serializer.access_type";
    public static final String DATE_FORMAT_KEY = "serializer.date_format";

    private Path metadataPath;
    private Boolean includeNullValues;
    private FieldFormatterType formatterType;
    private FieldAccessType accessType;
    private DateFormatType dateFormatType;

    private IMetadataAccessor metadataAccessor;

    public ConfigurationManager(
            IConfigurationProvider conf,
            IEnvironment environment,
            ICache cache
    ) {

        metadataPath = Paths.get(conf.getString(METADATA_DIR_KEY, "conf/serializer/"));
        includeNullValues = conf.getBoolean(INCLUDE_NULL_VALUES_KEY, false);
        formatterType = FieldFormatterType.fromString(conf.getString(FIELD_FORMATTING_METHOD_KEY, FieldFormatterType.LOWER_UNDERSCORE.toString()));
        accessType = FieldAccessType.fromString(conf.getString(FIELD_ACCESS_TYPE_KEY, FieldAccessType.PROPERTY.toString()));
        dateFormatType = DateFormatType.fromString(conf.getString(DATE_FORMAT_KEY, DateFormatType.ISO8601.toString()));

        if (environment.isProd()) {
            metadataAccessor = new CacheMetadataAccessor(cache);
        } else
            metadataAccessor = new FileMetadataAccessor();

        metadataAccessor.setMetadataPath(metadataPath);
    }

    public Path getMetadataPath() {
        return metadataPath;
    }

    public Boolean getIncludeNullValues() {
        return includeNullValues;
    }

    public IMetadataAccessor getMetadataAccessor() {
        return metadataAccessor;
    }

    public ISerializerMetadataProvider newMetadataProvider() {
        return new SerializerYamlMetadataProvider(metadataAccessor);
    }

    public IFieldFormatter getFieldFormatter() {
        return new FieldFormatter(formatterType);
    }

    public FieldAccessType getAccessType() {
        return accessType;
    }

    public DateFormatType getDateFormatType() {
        return dateFormatType;
    }

}
