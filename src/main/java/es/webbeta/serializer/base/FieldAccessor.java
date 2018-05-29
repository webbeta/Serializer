package es.webbeta.serializer.base;

public interface FieldAccessor {
    Boolean exists();
    <T> T get();
}