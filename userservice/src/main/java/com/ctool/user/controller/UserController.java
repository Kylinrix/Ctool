package com.ctool.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.user.event.EventProvider;
import com.ctool.user.model.UserHolder;
import com.ctool.util.JsonUtil;
import com.ctool.util.KeyWordUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    EventProvider eventProvider;

    @RequestMapping(path={"/signIn"},method = {RequestMethod.GET,RequestMethod.POST})
    public String register (Model model,@RequestParam(value = "next", required = false) String next){

        model.addAttribute("next",next);
        return "signIn";
    }

    /**
     * @Author: Kylinrix
     * @param: [model, response, request, username, password, email, next, headUrl]
     * @return: java.lang.String
     * @Date: 2019/1/11
     * @Email: Kylinrix@outlook.com
     * @Description:注册，若成功则发送事件。
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
            if(map.containsKey("user")) {
                User user = (User)map.get("user");
                //如果存在注册邮箱，则发送确认事件
                if(user.getEmail()!=null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", user.getId());
                    jsonObject.put("name", user.getName());
                    jsonObject.put("email",user.getEmail());
                    eventProvider.fireEvent(KeyWordUtil.KAFKA_MAIL_TOPIC,jsonObject.toJSONString());
                }
                return JsonUtil.getJSONString(0, map);
            }
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
     * @Author: Kylinrix
     * @param: [model, response, request, username, password, next]
     * @return: java.lang.String
     * @Date: 2019/1/11
     * @Email: Kylinrix@outlook.com
     * @Description:登录
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
                request.getSession().setAttribute("userId", user.getId());
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


    /**
     * @Author: Kylinrix
     * @param: [model, response, request]
     * @return: java.lang.String
     * @Date: 2019/1/11
     * @Email: Kylinrix@outlook.com
     * @Description: 登出，移除session的userId与userName, 移除Redis登录状态。
     */
    @ResponseBody
    @RequestMapping(path={"/logout"},method = {RequestMethod.POST,RequestMethod.GET})
    public String logout (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){

        HttpSession session = request.getSession();

        if(session.getAttribute("userId")!=null) {

            int userid = (int)session.getAttribute("userId");
            //request.getSession().setAttribute("online",0);
            request.getSession().removeAttribute("userId");
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


    /**
     * @Author: Kylinrix
     * @param: [model, userId]
     * @return: java.lang.String
     * @Date: 2019/1/11
     * @Email: Kylinrix@outlook.com
     * @Description: 邮箱验证链接，更新用户状态，重定向至主页。
     */
    @RequestMapping(path={"/mailLinkToVerify/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String mailStatus (Model model, @PathVariable("userId") int userId){

        userService.updateUserStatus(userId,1);
        model.addAttribute("user",userService.getUserById(userId));
        return "index";
    }



    //session共享测试
    @ResponseBody
    @RequestMapping(path={"/testsession"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        System.out.println(request.getSession().getAttribute("userId"));
        return "8001:"+ request.getSession().getId();
    }

    //Kafka消息+邮箱测试
    @ResponseBody
    @RequestMapping(path={"/testmail"},method = {RequestMethod.GET,RequestMethod.POST})
    public String mailTest (Model model){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",122345);
        jsonObject.put("name","testMY");

        //email 输入自己的邮箱进行测试
        String email = "1248378280@qq.com";
        jsonObject.put("email",email);
        try {
            eventProvider.fireEvent(KeyWordUtil.KAFKA_MAIL_TOPIC, jsonObject.toJSONString());
            return JsonUtil.getJSONString(0);
        }
        catch (Exception e){
            logger.info("测试失败"+e);
        }
        return JsonUtil.getJSONString(1,"fail","发送失败");
    }

}
