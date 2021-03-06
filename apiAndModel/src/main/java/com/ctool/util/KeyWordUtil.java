package com.ctool.util;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 16:47
 * @Email: Kylinrix@outlook.com
 * @Description: 存放一些常量
 */
public class KeyWordUtil {
    public static final int ORDINARY_USER = 0;
    public static final int ADMIN_USER = 1;


    public static final int LOGINUSER_TIMEOUT = 1800;//seconds
    public static final int SESSION_TIMEOUT = 1800;//seconds


    //redis 索引前缀
    public static final String LOGIN_USER_PREFIX = "LOGINUSER:";
    public static final String LOGIN_USER_TOEKN_PREFIX = "LOGINUSERTOEKN:";
    public static final String CARD_MEMBER_PREFIX = "CARD_MEMBER:";


    public static final String LOGIN_PAGE = "http://localhost:8001/signIn";

    public static final int ID_PREFIX_INDEX = 2;

    //看板权限，默认是成员可写
    public static final int BORAD_AUTHORIZATION_MEMBER =0;
    public static final int BORAD_AUTHORIZATION_ONLY_OWNER =1;
    public static final int BORAD_AUTHORIZATION_PUBLIC =2;


    //看板权限，默认是成员可写
    public static final int BORAD_USER_ROLE_MEMBER =0;
    public static final int BORAD_USER_ROLE_OWNER =1;
    public static final int BORAD_USER_ROLE_BLACKLIST =2;


    //kafka服务Topic
    //发送消息时，自动配置
    public static final String KAFKA_MAIL_TOPIC = "VerifyMail";

}
