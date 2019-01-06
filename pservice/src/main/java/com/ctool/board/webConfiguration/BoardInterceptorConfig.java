package com.ctool.board.webConfiguration;

import com.ctool.board.interceptor.LoginInterceptor;
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
public class BoardInterceptorConfig implements WebMvcConfigurer {

    //远程用户服务
    @Autowired
    LoginInterceptor loginInterceptor;

    private final String boardPath = "/board/*";

    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(loginInterceptor).addPathPatterns(userPath);
        registry.addInterceptor(loginInterceptor).addPathPatterns(boardPath);
    }
}
