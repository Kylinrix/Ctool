package com.ctool;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ctool.remoteService.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PserviceApplicationTests {

    @Reference(check = false)
    UserService userService;

    @Test
    public void contextLoads() {

        System.out.println(userService.getUser("1"));
        userService.checkAndUpdateIfUserExpired(1,"1234-1234");
    }

}

