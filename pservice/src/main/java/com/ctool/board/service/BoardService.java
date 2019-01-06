package com.ctool.board.service;

import com.ctool.board.dao.BoardDAO;
import com.ctool.board.dao.CardDAO;
import com.ctool.board.dao.LaneDAO;
import com.ctool.board.webSocket.BoardNettyServer;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    CardDAO cardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    public Board addBoard(@NotNull String boardName, @NotNull int userId, String description){

        Board b = new Board();
        b.setBoardName(boardName);
        //日期编码默认为SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        // "yyyy-MM-dd HH:mm:ss"
        b.setCreatedDate(new Date());
        b.setOwnUserId(userId);
        b.setDescription(description);

        //mybatis Insert会把导入的对象加上自增的ID。
        boardDAO.insertBoard(b);
        return b;
    }

    //外键约束 ON DELETE CASCADE 保证联结的主表被删除时，子表也被删除。
    @Transactional
    public boolean deleteBoard(@NotNull int boardId){
        if(boardDAO.deleteById(boardId)>0)return true;
        return false;
    }


    public Lane addLane(@NotNull int boardId,String name,String description){
        Lane lane = new Lane();
        lane.setBoardId(boardId);
        lane.setCreatedDate(new Date());
        lane.setLaneName(name);
        lane.setDescription(description);
        laneDAO.insertLane(lane);
        return lane;
    }

    public boolean deleteLane(@NotNull int laneId){
        if(laneDAO.deleteById(laneId)>0)return true;
        return false;
    }

    public Card addCard(@NotNull int laneId,String name,int userId,String CardContent,String description) {
        Card card = new Card();
        card.setCardName(name);
        card.setLaneId(laneId);
        card.setCreatedDate(new Date());
        card.setLastChanger(userId);
        card.setCardContent(CardContent);
        card.setDescription(description);
        cardDAO.insertCard(card);
        return card;
    }

    public boolean deleteCard(@NotNull int cardId){
        if(cardDAO.deleteById(cardId)>0)return true;
        return false;
    }

    public boolean  updateCard(@NotNull int cardId,String name,int userId,String cardContent,String description){
      if(cardDAO.updateCard(cardId,name,userId,cardContent,description)>0)return true;
      else return false;
    }

    public Card getCard(int id){
        return cardDAO.selectById(id);
    }

    public Lane getLane(int id){
        return laneDAO.selectById(id);
    }
    public Board getBoard (int id){
        return boardDAO.selectById(id);
    }


    public List<Board> getBoardByUserid (int userId){
        return boardDAO.selectByUserId(userId);
    }

}
