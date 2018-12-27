package com.ctool.remoteService;

import com.ctool.model.User;

import java.util.Map;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/24 17:47
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public interface UserService {

    public User getUser(int id);
    public User getUser(String name);
    public int addUser(User user);
    public Map<String,Object> register(String name, String pwd, String email, String HeadUrl);
    public Map<String,Object> login(String name, String pwd);
    public boolean checkAndUpdateIfUserExpired(int userid, String sessionId);
}
