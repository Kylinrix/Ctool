package com.ctool.model.board;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/27 14:42
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class Card implements Serializable {

    private int id;
    //private long id;

    private int laneId;

    private String cardName;

    private String cardContent;

    private String lastChanger;

    //按照日期排列。
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardContent() {
        return cardContent;
    }

    public void setCardContent(String cardContent) {
        this.cardContent = cardContent;
    }

    public String getLastChanger() {
        return lastChanger;
    }

    public void setLastChanger(String latChanger) {
        this.lastChanger = latChanger;
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
