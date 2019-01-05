package com.ctool.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/25 15:19
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class JsonUtil {
    public static String getJSONString(int code, String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toString();
    }
    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toString();
    }
    public static String getJSONString(int code, Map<String ,Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }
}
