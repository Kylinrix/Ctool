package com.ctool.user;

import com.ctool.user.model.User;
import com.ctool.user.service.UserService;
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
    @Test
    public void contextLoads() {
//        User user = new User();
//        user.setName("testt");
//        user.setPassword("23432");
//        user.setSalt("234312333");
//        userService.addUser(user);

        //userService.register("1","1","qq.com","");
    }

}

