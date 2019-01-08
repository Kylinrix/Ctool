package com.ctool.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/8 16:38
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class BoardUserRelation implements Serializable {
    private int id;
    private int boardId;
    private int userId;
    /**
     * int 0 member
     * int 1 owner
     * int 2 blackList
     */
    private int userRole;
    private Date createdDate;



    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
