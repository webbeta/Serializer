package es.webbeta.serializer.base;

public interface IParentFieldData {
    Class<?> getKlass();
    String getFieldName();
    String[] getGroups();
    Boolean isRecursive(Class<?> klass, String fieldName);
}
