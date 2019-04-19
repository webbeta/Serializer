package util.serializer;

@SuppressWarnings(value = "all")
public class ClassWithGetters3 {

    private Integer id;
    private String foo;
    private Boolean bar;

    public ClassWithGetters3() {
        id = 200;
        foo = "Hello world2!";
        bar = false;
    }

    public ClassWithGetters3(Integer id) {
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

}
