package com.ctool.pservice;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ctool.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/24 18:03
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Service
public class Testyuan {

    @Reference(check = false) //dubbo Reference
    UserService userService;

    public void getyuancheng(){
        System.out.println(userService.login());
    }

}
