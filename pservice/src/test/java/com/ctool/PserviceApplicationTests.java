package com.ctool;

import com.ctool.pservice.Testyuan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PserviceApplicationTests {

    @Autowired
    Testyuan testyuan;
    @Test
    public void contextLoads() {

        testyuan.getyuancheng();
    }

}

