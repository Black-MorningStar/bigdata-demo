package com.example.mongo.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class User {

    private ObjectId id;

    private String userName;

    private Integer age;

    private String desc;

    private List<Province> provinceList;

    public static User build(ObjectId id, String name, Integer age, String desc) {
        User user = new User();
        user.setId(id);
        user.setUserName(name);
        user.setAge(age);
        user.setDesc(desc);
        return user;
    }

    public void addProvince(String pCode, String pName,
                            String cCode1, String cName1,
                            String cCode2, String cName2) {
        Province province = new Province();
        province.setCode(pCode);
        province.setName(pName);
        City city1 = new City();
        city1.setCode(cCode1);
        city1.setName(cName1);
        City city2 = new City();
        city2.setCode(cCode2);
        city2.setName(cName2);
        province.setCityList(Lists.newArrayList(city1,city2));
        this.setProvinceList(Lists.newArrayList(province));
    }
}
