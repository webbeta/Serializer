package util.serializer;

@SuppressWarnings(value = "all")
public class ClassWithGetters2 {

    private Integer id;
    private String foo;
    private Boolean bar;
    private String truncatedNameField = "Hello world!";

    public ClassWithGetters2() {
        id = 200;
        foo = "Hello world2!";
        bar = false;
    }

    public ClassWithGetters2(Integer id) {
        id = id;
        foo = "Hello world2!";
        bar = false;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public Boolean isBar() {
        return bar;
    }

    public void setBar(Boolean bar) {
        this.bar = bar;
    }

    public String getTruncatedNameField() {
        return truncatedNameField;
    }

    public void setTruncatedNameField(String truncatedNameField) {
        this.truncatedNameField = truncatedNameField;
    }
}
