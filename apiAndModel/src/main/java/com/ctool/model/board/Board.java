package com.ctool.model.board;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/27 14:49
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class Board implements Serializable {

    private int id;

    private int ownUserId;

    private String boardName;

    private Date createdDate;

    /**
     * int 0 成员共享
     * int 1 私有
     * int 2 公开
     */
    private int authorization;

    private String description;

//    private List<Lane> lanes;
//
//    public List<Lane> getLanes() {
//        return lanes;
//    }
//
//    public void setLanes(List<Lane> lanes) {
//        this.lanes = lanes;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(int ownUserId) {
        this.ownUserId = ownUserId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getAuthorization() {
        return authorization;
    }

    public void setAuthorization(int authorization) {
        this.authorization = authorization;
    }
}
