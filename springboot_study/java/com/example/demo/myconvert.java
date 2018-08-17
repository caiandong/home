package com.example.demo;

import org.springframework.core.convert.converter.Converter;

public class myconvert implements Converter<String,myobject> {
    @Override
    public myobject convert(String source) {
        String[] split = source.split(":");
        myobject myobject = new myobject();
        myobject.setId(Integer.valueOf(split[0]));
        myobject.setHaha(split[1]);
        return myobject;
    }
}
