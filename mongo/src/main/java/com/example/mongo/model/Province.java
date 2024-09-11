package com.example.mongo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Province {

    private String code;

    private String name;

    private List<City> cityList;
}
