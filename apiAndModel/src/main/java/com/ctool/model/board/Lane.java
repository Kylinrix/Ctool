package com.ctool.model.board;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/27 14:48
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class Lane implements Serializable {

    private int id;

    private int boardId;

    private Date createdDate;

    private String laneName;

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



    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
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
}
