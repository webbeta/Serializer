package util.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Foo {

    private Integer id = 654;
    public String fooField = "Random string";
    public Bar bar = new Bar();
    private String truncatedNameField = "Hello world!";

    public List<String> list;
    public List<Bar> listOfBean;

    public String nullField = null;

    public PrivateFoo privateFoo = new PrivateFoo();

    private Map<String, Double> map;

    private BeanWithWrongDefinedMetadata beanWithWrongMetadata;

    private List<Foo> recursiveList;

    private Map<String, Foo> recursiveMap;

    public Foo() {
        list = new ArrayList<>();
        list.add("A");
        list.add("B");

        Bar bean1 = new Bar();
        bean1.id = 111;

        Bar bean2 = new Bar();
        bean2.id = 112;

        listOfBean = new ArrayList<>();
        listOfBean.add(bean1);
        listOfBean.add(bean2);

        map = new HashMap<>();
        map.put("foo", 50.1);
        map.put("bar", 60.12);

        beanWithWrongMetadata = new BeanWithWrongDefinedMetadata();

        recursiveList = new ArrayList<>();
        recursiveMap = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addRecursiveListItem(Foo foo) {
        recursiveList.add(foo);
    }

    public void addRecursiveMapItem(String key, Foo foo) {
        recursiveMap.put(key, foo);
    }

}
