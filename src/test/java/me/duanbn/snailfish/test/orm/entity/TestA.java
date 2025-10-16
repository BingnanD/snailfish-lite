package me.duanbn.snailfish.test.orm.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.duanbn.snailfish.core.domain.DomainE;
import me.duanbn.snailfish.core.orm.rdb.annotations.Field;
import me.duanbn.snailfish.core.orm.rdb.annotations.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(value = "t_test_a", alias = "a")
public class TestA extends DomainE {

    @Field
    private boolean b1;
    @Field
    private Boolean B2;
    @Field
    private Boolean B2_NULL;

    @Field
    private short sh3;
    @Field
    private Short Sh4;
    @Field
    private Short Sh4_NULL;

    @Field
    private int i5;
    @Field
    private Integer I6;
    @Field
    private Integer I6_NULL;

    @Field
    private float f7;
    @Field
    private Float F8;
    @Field
    private Float F8_NULL;

    @Field
    private double d9;
    @Field
    private Double D10;
    @Field
    private Double D10_NULL;

    @Field
    private String s11;
    @Field
    private String s11_NULL;

    @Field
    private byte[] by12;
    @Field
    private byte[] by12_NULL;

    @Field
    private byte byte13;
    @Field
    private Byte Byte14;
    @Field
    private Byte Byte14_NULL;

    @Field("test_enum")
    private TestEnum testEnum;
    @Field
    private TestEnum testEnum_NULL;

    @Field
    private Map<String, Object> map;
    @Field
    private Map<String, Object> map_NULL;
    @Field
    private List<String> list;
    @Field
    private List<String> list_NULL;
    @Field
    private Set<String> set;
    @Field
    private Set<String> set_NULL;

    @Field(jsonField = true)
    private TestB jsonField;
    @Field(jsonField = true)
    private TestB jsonField_NULL;

    @Override
    public boolean isAggregationRoot() {
        return true;
    }

}
