package es.webbeta.serializer.base;

public interface Cache {

    String get(String key);
    void set(String key, String content);
    void remove(String key);

}
