package com.ctool.user.controller;

import com.ctool.user.service.UserService;
import com.ctool.user.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 14:48
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @ResponseBody
    @RequestMapping(path={"/register"},method = {RequestMethod.POST})
    public String register (Model model,
                            HttpServletResponse response,
                            HttpServletRequest request,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam(value = "email",required = false) String email,
                            @RequestParam(value = "next",required = false) String next,
                            @RequestParam(value = "headUrl",required = false) String headUrl){

        try{
            Map<String, Object> map = userService.register(username, password,email,headUrl);


            return JsonUtil.getJSONString(0,map);
        }
        catch (Exception e){
            logger.error("注册异常： " + e.getMessage());
            return JsonUtil.getJSONString(1,"后台异常");
        }
    }

    @ResponseBody
    @RequestMapping(path={"/login"},method = {RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam(value = "next",required = false) String next){
        try{
            Map<String, Object> map = userService.login(username, password);

            //这里可以使用sessionID更改redis中的user信息，这样其他微服务就可以通过redis和sessionId获取当前登录的用户了。

            //页面重定向可以在前端使用
            //if (!(StringUtils.isEmpty(next))) {return "redirect:" + next;}

            return JsonUtil.getJSONString(0,map);
        }
        catch (Exception e){
            logger.error("登录异常： " + e.getMessage());
            return JsonUtil.getJSONString(1,"后台异常");
        }
    }
}
