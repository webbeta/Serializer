package util.serializer;

public class ClassWithGetters {

    private Integer id;
    private String foo;
    private Boolean bar;

    private String barMethod;
    private String barMethod2;
    private String barMethod3;

    public ClassWithGetters() {
        id = 500;
        foo = "Hello world!";
        bar = true;
        barMethod = "Foo";
        barMethod2 = "Foo";
        barMethod3 = "Foo";
    }

    public ClassWithGetters(Integer id) {
        id = id;
        foo = "Hello world!";
        bar = true;
        barMethod = "Foo";
        barMethod2 = "Foo";
        barMethod3 = "Foo";
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

    public String getBarMethod() {
        return barMethod + " Bar";
    }

    public void setBarMethod(String barMethod) {
        this.barMethod = barMethod;
    }

    public String getBarMethod2() {
        return barMethod2;
    }

    public void setBarMethod2(String barMethod2) {
        this.barMethod2 = barMethod2;
    }

    public String getBarMethodBis() {
        return barMethod2 + " Bar Foo Bar";
    }

    public String getBarMethod3() {
        return barMethod3;
    }

    public void setBarMethod3(String barMethod3) {
        this.barMethod3 = barMethod3;
    }

    public String getBarMethod3Bis() {
        return barMethod3 + " Bar Foo Bar";
    }

    public String getGetterWithoutProperty() {
        return "I'm fake!";
    }

    public String getGetterWithoutProperty2() {
        return "I'm fake!";
    }

}
