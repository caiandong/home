package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;

@Configuration
public class mybeans {
    @Bean
    Converter mm(){
        return new myconvert();
    }


}
