package es.webbeta.serializer.base;

public interface ICache {

    String get(String key);
    void set(String key, String content);
    void remove(String key);

}
