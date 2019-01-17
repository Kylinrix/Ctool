package com.ctool.util;


/**
 * @Author: Kylinrix
 * @Date: 2019/1/16 15:03
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class String2IntUtil {

    private static final int ID_PREFIX_INDEX =2;

    public static int string2IntId(String id){
        return Integer.parseInt(id.substring(ID_PREFIX_INDEX));
    }
}
