package com.ctool.user;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import com.alibaba.fastjson.JSONObject;
import com.ctool.user.event.EventProvider;
import com.ctool.util.KeyWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableDubbo //Dubbo 在Spring boot2.0后需要加这个注解启动。

//@PropertySource("classpath:application.properties")
public class UserserviceApplication {




    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);

    }

}

