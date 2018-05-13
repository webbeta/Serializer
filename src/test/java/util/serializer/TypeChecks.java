package util.serializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class TypeChecks {

    enum FooEnum {
        A,
        B,
        C;
    }

    public TypeChecks() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 12);
        cal.set(Calendar.MILLISECOND, 112);

        fooDate = cal.getTime();
        fooTimestamp = new Timestamp(fooDate.getTime());
    }

    private byte primitiveByte = 0;
    private Byte objectByte = 1;
    private short primitiveShort = 50;
    private Short objectShort = 50;
    private int primitiveInt = 500;
    private Integer objectInt = 500;
    private BigInteger bigInt = BigInteger.valueOf(500);
    private long primitiveLong = 500L;
    private Long objectLong = 500L;
    private double primitiveDouble = 500.1;
    private Double objectDouble = 500.1;
    private float primitiveFloat = 500.1F;
    private Float objectFloat = 500.1F;
    private BigDecimal bigDecimal = BigDecimal.valueOf(500.1);

    private String fooString = "Foo";
    private FooEnum fooEnum = FooEnum.A;
    private UUID fooUUID = UUID.fromString("8c748766-df38-421b-9eb2-0f0d2ffa2299");

    private boolean primitiveBoolean = false;
    private Boolean objectBoolean = true;

    private Date fooDate;
    private Timestamp fooTimestamp;

    public Date getFooDate() {
        return fooDate;
    }

    public Timestamp getFooTimestamp() {
        return fooTimestamp;
    }
}
