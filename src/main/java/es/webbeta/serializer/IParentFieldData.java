package es.webbeta.serializer;

public interface IParentFieldData {
    Class<?> getKlass();
    String getFieldName();
    String[] getGroups();
    Boolean isRecursive(Class<?> klass, String fieldName);
}
