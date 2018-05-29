package es.webbeta.serializer;

import es.webbeta.serializer.base.IParentFieldData;

import java.util.Objects;

public class ParentFieldData implements IParentFieldData {

    public ParentFieldData(Class<?> klass, String fieldName, String[] groups) {
        this.klass = klass;
        this.fieldName = fieldName;
        this.groups = groups;
    }

    private Class<?> klass;
    private String fieldName;
    private String[] groups;

    public Class<?> getKlass() {
        return klass;
    }

    public void setKlass(Class<?> klass) {
        this.klass = klass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    @Override
    public Boolean isRecursive(Class<?> klass, String fieldName) {
        return this.klass == klass && Objects.equals(this.fieldName, fieldName);
    }

}
