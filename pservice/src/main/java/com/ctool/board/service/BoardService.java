package com.ctool.board.service;

import com.ctool.board.dao.*;
import com.ctool.board.webSocket.BoardNettyServer;
import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import com.ctool.model.user.User;
import com.ctool.util.KeyWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: Ctool
 * @description:
 * @author: Kylinrix
 * @create: 2019-01-03 21:38
 **/
@Service
public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    @Autowired(required = false)
    PanelDAO panelDAO;

    @Autowired(required = false)
    CardDAO cardDAO;


    @Autowired(required = false)
    BoardUserRelationDAO boardUserRelationDAO;

    //board
    @Transactional
    public Board addBoard(@NotNull String boardName, @NotNull int userId, String description){

        Board b = new Board();

        //HTML过滤
        boardName = HtmlUtils.htmlEscape(boardName);
        description = HtmlUtils.htmlEscape(description);

        //默认为成员可见
        b.setAuthorization(KeyWordUtil.BORAD_AUTHORIZATION_MEMBER);
        b.setBoardName(boardName);
        //日期编码默认为SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        // "yyyy-MM-dd HH:mm:ss"
        b.setCreatedDate(new Date());
        b.setOwnUserId(userId);
        b.setDescription(description);

        //mybatis Insert会把导入的对象加上自增的ID。
        boardDAO.insertBoard(b);
        //增加关系表
        BoardUserRelation boardUserRelation = new BoardUserRelation();
        boardUserRelation.setBoardId(b.getId());
        boardUserRelation.setUserId(b.getOwnUserId());
        boardUserRelation.setUserRole(KeyWordUtil.BORAD_USER_ROLE_OWNER);
        boardUserRelation.setCreatedDate(new Date());
        boardUserRelationDAO.insertBoardUserRelation(boardUserRelation);
        return b;
    }

    //外键约束 ON DELETE CASCADE 保证联结的主表被删除时，子表也被删除。
    @Transactional
    public boolean deleteBoard(@NotNull int boardId){
        if(boardDAO.deleteById(boardId)>0)return true;
        return false;
    }
    public boolean  updateBoard(@NotNull int boardId,String name,String description){
        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        description = HtmlUtils.htmlEscape(description);

        if(boardDAO.updateBoardProfile(boardId,name,description)>0)return true;
        else return false;
    }

    /**
     * @Author: Kylinrix
     * @param: [boardId, code]
     * @return: boolean
     * @Date: 2019/1/15
     * @Email: Kylinrix@outlook.com
     * @Description: 改变看板属性，int 0:成员共享  int 1:私有   int 2:公开
     */
    public boolean updateBoardAuthorization(int boardId,int code){

        if(boardDAO.updateAuthorization(boardId,code)>0) return true;
        else return false;
    }




    //lane
    public Lane addLane(@NotNull int boardId,String name,String description){
        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        description = HtmlUtils.htmlEscape(description);


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



    //panel
    public Panel addPanel(@NotNull int laneId,String name,String description){
        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        description = HtmlUtils.htmlEscape(description);


        Panel panel = new Panel();
        panel.setLaneId(laneId);
        panel.setPanelName(name);
        panel.setCreatedDate(new Date());
        panel.setDescription(description);
        panelDAO.insertPanel(panel);
        return panel;
    }

    public boolean deletePanel(@NotNull int panelId){
        if(panelDAO.deleteById(panelId)>0)return true;
        return false;
    }
    public boolean  updatePanel(@NotNull int cardId,String name,String description){

        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        description = HtmlUtils.htmlEscape(description);
        if(panelDAO.updateCard(cardId,name,description)>0)return true;
        else return false;
    }


    //card
    public Card addCard(@NotNull int laneId,String name,int userId,String cardContent,String description) {


        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        cardContent =HtmlUtils.htmlEscape(cardContent);
        description = HtmlUtils.htmlEscape(description);


        Card card = new Card();
        card.setCardName(name);
        card.setLaneId(laneId);
        card.setCreatedDate(new Date());
        card.setLastChanger(userId);
        card.setCardContent(cardContent);
        card.setDescription(description);
        cardDAO.insertCard(card);
        return card;
    }

    public boolean deleteCard(@NotNull int cardId){
        if(cardDAO.deleteById(cardId)>0)return true;
        return false;
    }


    public boolean  updateCard(@NotNull int cardId,String name,int userId,String cardContent,String description){

        //HTML过滤
        name = HtmlUtils.htmlEscape(name);
        cardContent =HtmlUtils.htmlEscape(cardContent);
        description = HtmlUtils.htmlEscape(description);
        if(cardDAO.updateCard(cardId,name,userId,cardContent,description)>0)return true;
        else return false;
    }

    //getter
    public Card getCard(int id){
        return cardDAO.selectById(id);
    }
    public Lane getLane(int id){
        return laneDAO.selectById(id);
    }
    public Board getBoard (int id){
        return boardDAO.selectById(id);
    }
    public Panel getPanel (int id){return panelDAO.selectById(id);}


    //relation
    public List<Board> getBoardsByUserid (int userId){
        return boardUserRelationDAO.selectBoardsByUserId(userId);
    }

    public List<User> getMemberByBoardId (int boardId){
        return boardUserRelationDAO.selectUsersByBoardId(boardId);
    }




    /**
     * @Author: Kylinrix
     * @param: [boardId, userId]
     * @return: boolean
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description: 添加看板成员
     */

    public BoardUserRelation addBoardMember(int boardId,int userId){
        BoardUserRelation boardUserRelation = new BoardUserRelation();
        boardUserRelation.setBoardId(boardId);
        boardUserRelation.setUserId(userId);
        boardUserRelation.setUserRole(KeyWordUtil.BORAD_USER_ROLE_MEMBER);
        boardUserRelation.setCreatedDate(new Date());
        boardUserRelationDAO.insertBoardUserRelation(boardUserRelation);
        return boardUserRelation;
    }

    /**
     * @Author: Kylinrix
     * @param: [boardId, userId]
     * @return: boolean
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description: 移除看板成员
     */
    public boolean removeBoardMember(int boardId,int userId){
        if(boardUserRelationDAO.deleteBoardAndUserRelation(boardId,userId)>0)return true;
        return false;
    }




    /**
     * @Author: lky
     * @param: [boardId, userId]
     * @return: int
     * 返回值 0 : 权限通过
     *       1 : 非成员
     *       2 : 黑名单
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description: 检查用户是否可以进入看板页
     *
     */
    public int checkIfBoardAuthorized(int boardId ,int userId){
        Board board =boardDAO.selectById(boardId);
        if(board.getAuthorization()==KeyWordUtil.BORAD_AUTHORIZATION_ONLY_OWNER){
            if(board.getOwnUserId()!=userId)return -1;
            else return 0;
        }
        BoardUserRelation boardUserRelation = boardUserRelationDAO.selectBoardAndUserRelation(boardId,userId);
        if(board.getAuthorization()==KeyWordUtil.BORAD_AUTHORIZATION_PUBLIC){
            if(boardUserRelation!=null){ if(boardUserRelation.getUserRole()==KeyWordUtil.BORAD_USER_ROLE_BLACKLIST) return 2; }
            return 0;
        }
        if(board.getAuthorization()==KeyWordUtil.BORAD_AUTHORIZATION_MEMBER){
            if(boardUserRelation!=null){ if(boardUserRelation.getUserRole()==KeyWordUtil.BORAD_USER_ROLE_BLACKLIST) return 2; }
            else if(boardUserRelation==null) return 1;
            else return 0;
        }
        return 0;
    }

    /**
     * @Author: Kylinrix
     * @param: [boardId, userId]
     * @return: boolean
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description: 检查是否为看板成员
     */
    public boolean checkIsBoardMember(int boardId ,int userId){
        BoardUserRelation boardUserRelation =  boardUserRelationDAO.selectBoardAndUserRelation(boardId,userId);
        if(boardUserRelation==null)return false;
        else return true;
    }


}
