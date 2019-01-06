package com.ctool.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/6 14:11
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
