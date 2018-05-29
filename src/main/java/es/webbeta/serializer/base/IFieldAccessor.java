package es.webbeta.serializer.base;

public interface IFieldAccessor {
    Boolean exists();
    <T> T get();
}