package es.webbeta;

import es.webbeta.serializer.*;
import es.webbeta.serializer.base.ICache;
import es.webbeta.serializer.base.IConfigurationProvider;
import es.webbeta.serializer.base.IEnvironment;
import es.webbeta.serializer.base.ILogger;

public class SerializerBuilder {

    private IConfigurationProvider configurationProvider;
    private IEnvironment environment;
    private ICache cache;
    private ILogger logger;

    public static SerializerBuilder build() {
        return new SerializerBuilder();
    }

    public SerializerBuilder withConfigurationProvider(final IConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;

        return this;
    }

    public SerializerBuilder withEnvironment(final IEnvironment environment) {
        this.environment = environment;

        return this;
    }

    public SerializerBuilder withCache(final ICache cache) {
        this.cache = cache;

        return this;
    }

    public SerializerBuilder withLogger(final ILogger logger) {
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
