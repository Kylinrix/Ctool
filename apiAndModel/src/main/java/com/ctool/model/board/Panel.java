package com.ctool.model.board;

import java.util.Date;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/15 01:20
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class Panel {
    private int id;
    private int laneId;
    private String PanelName;
    private Date createdDate;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLaneId() {
        return laneId;
    }

    public void setLaneId(int laneId) {
        this.laneId = laneId;
    }

    public String getPanelName() {
        return PanelName;
    }

    public void setPanelName(String panelName) {
        PanelName = panelName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
