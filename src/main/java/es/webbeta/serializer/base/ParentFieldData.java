package es.webbeta.serializer.base;

public interface ParentFieldData {
    Class<?> getKlass();
    String getFieldName();
    String[] getGroups();
    Boolean isRecursive(Class<?> klass, String fieldName);
}
