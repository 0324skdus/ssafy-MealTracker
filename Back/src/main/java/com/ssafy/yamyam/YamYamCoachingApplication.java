package com.ssafy.yamyam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ssafy.yamyam.dao")
public class YamYamCoachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(YamYamCoachingApplication.class, args);
    }

}


