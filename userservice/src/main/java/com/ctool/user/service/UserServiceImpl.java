package com.ctool.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.user.dao.UserDAO;
import com.ctool.util.KeyWordUtil;
import com.ctool.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ctool.util.KeyWordUtil.ORDINARY_USER;


/**
 * @Auther: Kylinrix
 * @Date: 2018/12/24 17:49
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@org.springframework.stereotype.Service
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired(required = false)
    UserDAO userDAO;

    @Autowired
    RedisTemplate redisTemplate;

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

    //其他微服务查看登录用户是否过期
    @Override
    public boolean checkAndUpdateIfUserExpired(int userid, String sessionId) {

        String key = KeyWordUtil.LOGIN_USER_PREFIX+String.valueOf(userid);
        Object loginUserSessionId = redisTemplate.opsForValue().get(key);

        //二次迭代时，为了实现长期无密码登录，这里可以考虑直接用一个token POJO类取代sessionID，结合过期时间和Cookie，存在Redis中做用户验证

        //单点登录限制
        //如果当前sessionID 与 userid 所绑定的sessionID 相同，则重置过期时间
        if(loginUserSessionId!=null && loginUserSessionId.toString().equals(sessionId)){

            //更新过期时间
            redisTemplate.opsForValue().set(key,
                    sessionId,
                    KeyWordUtil.LOGINUSER_TIMEOUT,
                    TimeUnit.SECONDS);
            logger.info("重新更新Redis LOGINUSER:"+String.valueOf(userid)+" "+sessionId+" 过期时间");
        }
        else if(loginUserSessionId==null){
            logger.warn("SessionID 与 绑定的 userid 已过期，请重新登录 ！");
            return false;
        }
        else{
            logger.warn("SessionID 与 绑定的 userid 不符 ，已在其它地方登录，请重新登录 ！");
            return false;
        }
        return true;
    }


}
