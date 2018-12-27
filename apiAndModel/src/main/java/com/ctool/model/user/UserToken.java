package com.ctool.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/26 14:11
 * @Email: Kylinrix@outlook.com
 * @Description:unused，这里计划使用Cookie与Redis 保存
 */
public class UserToken implements Serializable {
    private int id ;
    private int userId;
    private Date expired;
    private int status;
    private String tokenKey;

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String key) {
        this.tokenKey = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
