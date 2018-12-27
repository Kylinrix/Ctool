package com.ctool.user.interceptor;

import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.user.model.UserHolder;
import com.ctool.user.util.KeyWordUtil;
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
 * @Description:访问之前，检查是否登录，userHolder是否已经加入。
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
        if (session.getAttribute("userid")==null) {
            //强制返回登录页面
            response.sendRedirect("http://localhost:8001/login?next=" + request.getRequestURI());
            return false;
        }
        else {

            int userid = (int)session.getAttribute("userid");
            String key = KeyWordUtil.LOGIN_USER_PREFIX+String.valueOf(userid);
            Object loginUserSessionId = redisTemplate.opsForValue().get(key);

            //二次迭代时，为了实现长期无密码登录，这里可以考虑直接用一个token POJO类取代sessionID，结合过期时间和Cookie，存在Redis中做用户验证

            //单点登录限制
            //如果当前sessionID 与 userid 所绑定的sessionID 相同，则重置过期时间
            if(loginUserSessionId!=null && loginUserSessionId.toString().equals(session.getId())){

                //更新过期时间
                redisTemplate.opsForValue().set(key,
                                                session.getId(),
                                                KeyWordUtil.LOGINUSER_TIMEOUT,
                                                TimeUnit.SECONDS);
                logger.info("重新更新Redis LOGINUSER:"+String.valueOf(userid)+" "+session.getId()+" 过期时间");
            }
            else if(loginUserSessionId==null){
                //如果不同，说明已在其它地方重新登录，即userid与新的sessionid绑定，则用户需登录。
                logger.warn("SessionID 与 绑定的 userid 已过期，请重新登录 ！");
                response.sendRedirect("http://localhost:8001/login?next=" + request.getRequestURI());
                return false;
            }
            else{
                redisTemplate.delete(key);
                logger.warn("SessionID 与 绑定的 userid 不符 ，已在其它地方登录，请重新登录 ！");
                response.sendRedirect("http://localhost:8001/login?next=" + request.getRequestURI());
                return false;
            }

            User user = userService.getUser((int)session.getAttribute("userid"));
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
