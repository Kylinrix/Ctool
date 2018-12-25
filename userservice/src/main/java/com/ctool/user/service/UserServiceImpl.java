package com.ctool.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ctool.user.dao.UserDAO;
import com.ctool.user.model.User;
import com.ctool.user.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.ctool.user.util.StatusUtil.ORDINARY_USER;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/24 17:49
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Service //DubboService
@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired(required = false)
    UserDAO userDAO;

    @Override
    public User getUser(int id) { return userDAO.selectById(id); }

    @Override
    public User getUser(String name) { return userDAO.selectByName(name); }

    @Override
    public int addUser(User user) { return userDAO.addUser(user); }

    @Override
    public Map<String, Object> register(String name, String pwd, String email, String HeadUrl) {
        Map<String,Object> context = new HashMap<String,Object>();
        if (StringUtils.isEmpty(name)||StringUtils.containsWhitespace(name)){
            context.put("msg","用户名不能包含空格");
            return context;
        }
        if (StringUtils.isEmpty(pwd)){
            context.put("msg","密码不能为空");
            return context;
        }
        User user = userDAO.selectByName(name);
        if (user!=null){
            context.put("msg","用户名已存在");
            return context;
        }

        User regUser = new User();
        regUser.setName(name);
        regUser.setSalt(UUID.randomUUID().toString().substring(0, 5).toLowerCase());
        regUser.setPassword(MD5Util.MD5(pwd+regUser.getSalt()));
        regUser.setStatus(ORDINARY_USER);
        userDAO.addUser(regUser);
        context.put("user",regUser);
        return context;
    }

    @Override
    public Map<String, Object> login(String name, String pwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(name)||StringUtils.containsWhitespace(name)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(pwd)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(name);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!(MD5Util.MD5(pwd+user.getSalt()).equals(user.getPassword()))) {
            map.put("msg", "密码不正确");
            return map;
        }

        map.put("user", user);
        return map;
    }


}
