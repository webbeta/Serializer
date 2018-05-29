package es.webbeta.serializer.base;

public interface IConfigurationProvider {

    boolean getBoolean(String key, boolean defaultValue);
    String getString(String key, String defaultValue);

}
