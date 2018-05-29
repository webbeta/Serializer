package es.webbeta;

import es.webbeta.serializer.*;
import es.webbeta.serializer.base.Cache;
import es.webbeta.serializer.base.ConfigurationProvider;
import es.webbeta.serializer.base.Environment;
import es.webbeta.serializer.base.Logger;

public class SerializerBuilder {

    private ConfigurationProvider configurationProvider;
    private Environment environment;
    private Cache cache;
    private Logger logger;

    public static SerializerBuilder build() {
        return new SerializerBuilder();
    }

    public SerializerBuilder withConfigurationProvider(final ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;

        return this;
    }

    public SerializerBuilder withEnvironment(final Environment environment) {
        this.environment = environment;

        return this;
    }

    public SerializerBuilder withCache(final Cache cache) {
        this.cache = cache;

        return this;
    }

    public SerializerBuilder withLogger(final Logger logger) {
        this.logger = logger;

        return this;
    }

    public Serializer get() {
        if (configurationProvider == null || environment == null || cache == null)
            throw new IllegalArgumentException("All services must be settled.");

        ConfigurationManager configurationManager =
                new ConfigurationManager(configurationProvider, environment, cache);

        Serializer serializer = new Serializer(configurationManager);
        serializer.setLogger(logger);

        return serializer;
    }

}
