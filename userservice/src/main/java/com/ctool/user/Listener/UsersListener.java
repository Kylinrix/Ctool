package com.ctool.user.Listener;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 20:49
 * @Email: Kylinrix@outlook.com
 * @Description:unused
 */

@Component
public class UsersListener implements HttpSessionBindingListener {
    @Override
    public  void valueBound(HttpSessionBindingEvent event) {
    }


    @Override
    public  void valueUnbound(HttpSessionBindingEvent event) {
    }

}
