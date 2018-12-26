package com.ctool.model;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/26 14:53
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class User {
    private int id ;
    private String name;
    private String password;
    private String salt;
    private String email;
    private String headUrl;
    /**
     * status 用户状态：普通0，管理员1
     */
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
