package com.ctool.user.interceptor;

import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.user.model.UserHolder;
import com.ctool.util.KeyWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 14:24
 * @Email: Kylinrix@outlook.com
 * @Description:访问之前，检查是否登录，即单点登录，userHolder是否已经加入。
 *              待测试强制重定义对session的影响。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    UserService userService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("userId")==null) {
            //强制返回登录页面
            response.sendRedirect("http://localhost:8001/login?next=" + request.getRequestURI());
            return false;
        }
        else {

            int userid = (int)session.getAttribute("userId");
            //检查用户的session情况
            if(!userService.checkAndUpdateIfUserExpired(userid,session.getId())){
                response.sendRedirect("http://localhost:8001/login?next=" + request.getRequestURI());
                return false;
            }
            User user = userService.getUser((int)session.getAttribute("userId"));
            if(user!=null) userHolder.setUser(user);
        }
        return true;

    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        User user =userHolder.getUser();
        if(modelAndView!=null && user!=null){
            modelAndView.addObject("user",user);
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if(userHolder.getUser()!=null) userHolder.remove();
    }


}
