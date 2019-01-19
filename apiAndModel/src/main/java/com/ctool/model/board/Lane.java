package com.ctool.model.board;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

//    private List<Panel> panels;
//
//
//    public List<Panel> getPanels() {
//        return panels;
//    }
//
//    public void setPanels(List<Panel> panels) {
//        this.panels = panels;
//    }



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
