package com.ctool.board.webConfiguration;

import com.ctool.board.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


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

    private final List<String> boardPath = new ArrayList<>();

    public void addInterceptors(InterceptorRegistry registry) {
        boardPath.add("/board");
        boardPath.add("/board/*");
        //registry.addInterceptor(loginInterceptor).addPathPatterns(userPath);
        registry.addInterceptor(loginInterceptor).addPathPatterns(boardPath);
    }
}
