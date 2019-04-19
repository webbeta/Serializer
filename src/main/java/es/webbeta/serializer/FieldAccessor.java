package es.webbeta.serializer;

import com.google.common.collect.Lists;
import es.webbeta.serializer.base.Logger;
import es.webbeta.serializer.type.FieldAccessType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class FieldAccessor implements es.webbeta.serializer.base.FieldAccessor {

    private Boolean initialized = false;

    private Class<?> klass;
    private List<Class<?>> klassTree;
    private Object ob;
    private String fieldName;
    private FieldAccessType accessType;

    private Logger logger;

    private Boolean exists = false;
    private Object value = null;

    private Boolean ensureFieldExists = true;
    private String customGetterName;

    public FieldAccessor(
            Object ob,
            String fieldName,
            FieldAccessType accessType
    ) {
        this.ob = ob;
        this.fieldName = fieldName;
        this.accessType = accessType;

        klass = ob.getClass();
        klassTree = new ArrayList<>();
        klassTree.add(klass);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private String[] buildGetterNames(String fieldName) {
        List<String> names = new ArrayList<>();

        String firstLetter = fieldName.substring(0, 1);

        names.add("get" + firstLetter.toUpperCase() + fieldName.substring(1));
        names.add("is" + firstLetter.toUpperCase() + fieldName.substring(1));

        return names.toArray(new String[0]);
    }

    private void init() {
        if (initialized) return;

        Class<?> superKlass = klass;
        while (superKlass != null) {
            superKlass = superKlass.getSuperclass();
            if (superKlass.getCanonicalName().equals(Object.class.getCanonicalName()))
                break;

            klassTree.add(superKlass);
        }
        klassTree = Lists.reverse(klassTree);

        if (accessType == FieldAccessType.PROPERTY)
            buildAsProperty();
        else if (accessType == FieldAccessType.PUBLIC_METHOD)
            buildAsMethod();

        initialized = true;
    }

    @SuppressWarnings("all")
    private void buildAsProperty() {
        for (Class<?> klass : klassTree) {
            try {
                Field field = klass.getDeclaredField(fieldName);
                if (!field.isAccessible())
                    field.setAccessible(true);
                value = field.get(ob);
                exists = true;
            } catch (IllegalAccessException e) {
                if (logger != null) {
                    logger.error(String.format("Serializer cannot serialize field '%s.%s', because it is not public.",
                            klass.getCanonicalName(),
                            fieldName
                    ));
                }
            } catch (NoSuchFieldException ignored) {}
        }
    }

    private void buildAsMethod() {
        if (!Modifier.isPublic(klass.getModifiers()))
            throw new IllegalAccessError("Serializer cannot access \"" + klass.getCanonicalName() + "\" class. It can be caused because it has non public access.");

        String[] names;
        if (customGetterName == null)
            names = buildGetterNames(fieldName);
        else
            names = new String[]{customGetterName};

        for (String name : names) {
            try {
                Method method = klass.getMethod(name);
                value = method.invoke(ob);
                exists = true;
                break;
            } catch (NoSuchMethodException ignored) {
                exists = false;
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (logger != null) {
                    logger.error(String.format("Serializer cannot serialize method '%s.%s', because it %s",
                            klass.getCanonicalName(), name,
                            e instanceof IllegalAccessException ? "is not public." :
                                    "throw an exception when was called."
                    ));
                }
            }
        }

        if (ensureFieldExists) {
            FieldAccessor accessorAsField =
                    new FieldAccessor(ob, fieldName, FieldAccessType.PROPERTY);

            exists = exists && accessorAsField.exists();

            if (!exists)
                value = null;
        }
    }

    public void setEnsureFieldExists(Boolean ensureFieldExists) {
        this.ensureFieldExists = ensureFieldExists;
    }

    public void setCustomGetterName(String customGetterName) {
        this.customGetterName = customGetterName;
    }

    @Override
    public Boolean exists() {
        init();
        return exists;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get() {
        init();
        return (T) value;
    }

}
