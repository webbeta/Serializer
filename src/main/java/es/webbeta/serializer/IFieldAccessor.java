package es.webbeta.serializer;

public interface IFieldAccessor {
    Boolean exists();
    <T> T get();
}