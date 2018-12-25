package com.ctool.user.model;

import org.springframework.stereotype.Component;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 14:23
 * @Email: Kylinrix@outlook.com
 * @Description:用于在Session有效期间中快速访问User信息。
 */
@Component
public class UserHolder {
    private static ThreadLocal<User> userholder = new ThreadLocal<User>();

    public User getUser(){
        return userholder.get();
    }
    public void  setUser(User user){
        userholder.set(user);
    }
    public void remove (){
        userholder.remove();
    }
}
