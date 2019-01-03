package com.ctool.board.service;

import com.ctool.board.dao.BoardDAO;
import com.ctool.board.dao.CardDAO;
import com.ctool.board.dao.LaneDAO;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: Ctool
 * @description:
 * @author: KyLee
 * @create: 2019-01-03 21:38
 **/
@Service
public class BoardService {

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    CardDAO cardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    public boolean addBoard(@NotNull String boardName, @NotNull int userId, String description){

        Board b = new Board();
        b.setBoardName(boardName);
        //日期编码默认为SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        // "yyyy-MM-dd HH:mm:ss"
        b.setCreatedDate(new Date());
        b.setOwnUserId(userId);
        b.setDescription(description);

        //应该是成功返回行数，否则0，
        //但是成功一直是返回1。我不知道为啥。
        if(boardDAO.addBoard(b)>0)return true;
        return false;
    }

    //外键约束 ON DELETE CASCADE 保证联结的主表被删除时，子表也被删除。
    @Transactional
    public boolean deleteBoard(@NotNull int boardId){
        if(boardDAO.deleteById(boardId)>0)return true;
        return false;
    }


    public boolean addLane(@NotNull int boardId,String name,String description){
        Lane lane = new Lane();
        lane.setBoardId(boardId);
        lane.setCreatedDate(new Date());
        lane.setLaneName(name);
        lane.setDescription(description);
        if(laneDAO.addLane(lane)>0)return true;
        return false;
    }

    public boolean deleteLane(@NotNull int laneId){
        if(laneDAO.deleteById(laneId)>0)return true;
        return false;
    }

    public boolean addCard(@NotNull int cardId,@NotNull int laneId,String name,String userId,String CardContent) {
        Card card = new Card();
        card.setCardName(name);
        card.setLaneId(laneId);
        card.setCreatedDate(new Date());
        card.setLastChanger(userId);
        card.setCardContent(CardContent);
        if(cardDAO.addCard(card)>0)return true;
        return false;
    }

    public boolean deleteCard(@NotNull int cardId){
        if(cardDAO.deleteById(cardId)>0)return true;
        return false;
    }

    public void updateCard(@NotNull int cardId,String name,String userId,String cardContent,String description){
        cardDAO.updateCard(cardId,name,userId,cardContent,description);
    }


}
