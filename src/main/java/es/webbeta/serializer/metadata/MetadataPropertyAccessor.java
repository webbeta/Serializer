package es.webbeta.serializer.metadata;

/**
 * It describes the customized getter method binded to a field.
 * <b>It requires that "access_type" config to be "public_method".</b>
 */
public class MetadataPropertyAccessor {

    private String getter;

    MetadataPropertyAccessor(String getter) {
        this.getter = getter;
    }

    public Boolean hasGetter() {
        return getter != null;
    }

    public String getGetter() {
        return getter;
    }

}