package com.ctool.user;

import com.alibaba.fastjson.JSONObject;
import com.ctool.remoteService.UserService;
import com.ctool.user.event.EventConsumer;
import com.ctool.user.event.EventProvider;
import com.ctool.user.service.MailService;
import com.ctool.util.KeyWordUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserserviceApplicationTests {


    @Autowired(required = false)
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    EventProvider eventProvider;

    @Autowired
    MailService mailService;

    @Test
    public void contextLoads() {
//        User user = new User();
//        user.setName("testt");
//        user.setPassword("23432");
//        user.setSalt("234312333");
//        userService.addUser(user);

        //userService.register("1","1","qq.com","");

        //mailService.sendVerifyMail(1,"testMy","1248378280@qq.com");
    }

    @Test
    public void MailTest() {
        System.out.println("-------------------- 邮件服务测试 -------------------------");
        mailService.sendVerifyMail(1,"testMy","1248378280@qq.com");
    }

}

