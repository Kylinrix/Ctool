package com.ctool.user.webConfiguration;

import com.ctool.user.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 14:49
 * @Email: Kylinrix@outlook.com
 * @Description:
 */

@Component
public class UserInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    private final String userPath = "/login/*";

    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(loginInterceptor).addPathPatterns(userPath);

        registry.addInterceptor(loginInterceptor).addPathPatterns(userPath);
    }
}
