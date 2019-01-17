package com.ctool.model.board;

import com.ctool.model.user.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/27 14:42
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
public class Card implements Serializable {

    private int id;
    //private long id;

    private int panelId;

    private String cardName;

    private String cardContent;

    private int lastChanger;

    //按照日期排列。
    private Date createdDate;

    private String description;

//    private List<User> members;
//
//    public List<User> getMembers() {
//        return members;
//    }
//
//    public void setMembers(List<User> members) {
//        this.members = members;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPanelId() {
        return panelId;
    }

    public void setPanelId(int panelId) {
        this.panelId = panelId;
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

    public int getLastChanger() {
        return lastChanger;
    }

    public void setLastChanger(int latChanger) {
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
