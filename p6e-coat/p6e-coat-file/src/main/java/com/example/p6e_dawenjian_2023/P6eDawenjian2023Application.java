package com.example.p6e_dawenjian_2023;

import com.example.p6e_dawenjian_2023.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P6eDawenjian2023Application {

    public static void main(String[] args) {
        SpringUtil.init(SpringApplication.run(P6eDawenjian2023Application.class, args));
    }

}
