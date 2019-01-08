//package com.ctool.remoteService;
//
//import com.sun.istack.internal.Nullable;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
///**
// * @Author: Kylinrix
// * @Date: 2019/1/6 14:50
// * @Email: Kylinrix@outlook.com
// * @Description: HTTPRequest 和HTTPResponse 没有序列化，dubbo不能使用
// */
//public interface LoginInterceptorService extends HandlerInterceptor {
//
//    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        return true;
//    }
//    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                            @Nullable ModelAndView modelAndView) throws Exception {
//    }
//    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
//                                 @Nullable Exception ex) throws Exception {
//    }
//}
