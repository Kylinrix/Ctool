package com.ctool.user.controller;

import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.user.model.UserHolder;
import com.ctool.user.util.JsonUtil;
import com.ctool.user.util.KeyWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    UserHolder userHolder;
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 注册
     * @param model
     * @param response
     * @param request
     * @param username
     * @param password
     * @param email
     * @param next
     * @param headUrl
     * @return
     * @Description 约定 userid 的存在视为登录登出的凭证
     */
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
            if(map.containsKey("user"))
                return JsonUtil.getJSONString(0,map);
            else if (map.get("msg")!=null)
                return JsonUtil.getJSONString(1, map);
            else
                return JsonUtil.getJSONString(2, map);
        }
        catch (Exception e){
            logger.error("注册异常： " + e.getMessage());
            return JsonUtil.getJSONString(-1,"后台注册异常");
        }
    }

    /**
     * 登录
     * @param model
     * @param response
     * @param request
     * @param username
     * @param password
     * @param next
     * @return
     */
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

            if(map.containsKey("user")) {
                //因为使用了redis-session共享，这样其他微服务就可以通过session获取当前登录的用户了。
                User user = (User) map.get("user");
                //request.getSession().setAttribute("online",1);

                //约定 userid的存在视为登录登出的凭证
                request.getSession().setAttribute("userid", user.getId());
                request.getSession().setAttribute("username", user.getName());

                //使用redis作为登录限制 {key->LOGINUSER:userid , value->sessionid}
                redisTemplate.opsForValue().set("LOGINUSER:"+String.valueOf(user.getId()),
                                                request.getSession().getId(),
                                                KeyWordUtil.LOGINUSER_TIMEOUT,
                                                TimeUnit.SECONDS);
                //页面重定向可以在前端使用
                //if (!(StringUtils.isEmpty(next))) {return "redirect:" + next;}

                return JsonUtil.getJSONString(0, map);
            }
            else if (map.get("msg")!=null)
                return JsonUtil.getJSONString(1, map);
            else
                return JsonUtil.getJSONString(2, map);
        }
        catch (Exception e){
            logger.error("登录异常： " + e.getMessage());
            return JsonUtil.getJSONString(-1,"后台登录异常");
        }
    }


    @ResponseBody
    @RequestMapping(path={"/logout"},method = {RequestMethod.POST,RequestMethod.GET})
    public String logout (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        HttpSession session = request.getSession();

        if(session.getAttribute("userid")!=null) {

            int userid = (int)session.getAttribute("userid");
            //request.getSession().setAttribute("online",0);
            request.getSession().removeAttribute("userid");
            request.getSession().removeAttribute("username");

            //如何快速过期。
            request.getSession().setMaxInactiveInterval(1);
            //request.getSession().invalidate();


            redisTemplate.delete(KeyWordUtil.LOGIN_USER_PREFIX+ String.valueOf(userid));
            return JsonUtil.getJSONString(0);

        }
        else
            return JsonUtil.getJSONString(1,"登出异常");
    }
    @ResponseBody
    @RequestMapping(path={"/test"},method = {RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        return "8001:"+ request.getSession().getId();
    }

}
