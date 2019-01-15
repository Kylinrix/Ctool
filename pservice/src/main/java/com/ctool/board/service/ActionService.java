package com.ctool.board.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import com.ctool.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/4 16:50
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@org.springframework.stereotype.Service
public class ActionService {
    @Autowired
    BoardService boardService;



    private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

    private final static String REQUEST_INSERT = "insert";
    private final static String REQUEST_UPDATE = "update";
    private final static String REQUEST_DELETE = "delete";

    private final static String ENTITY_TYPE_CARD = "card";
    private final static String ENTITY_TYPE_LANE = "lane";
    private final static String ENTITY_TYPE_BOARD = "board";
    private final static String ENTITY_TYPE_PANEL = "panel";

    private final static String DEFAULT_CARD_NAME = "卡片";
    private final static String DEFAULT_LANE_NAME = "泳道";
    private final static String DEFAULT_BOARD_NAME = "看板";
    private final static String DEFAULT_PANEL_NAME = "release";


    private final static String DEFAULT_BOARD_CONTENT = "";
    private final static String DEFAULT_CARD_CONTENT = "";

    private final static String DEFAULT_BOARD_DESCRIPTION = "This is a Board";
    private final static String DEFAULT_CARD_DESCRIPTION = "This is a Card";
    private final static String DEFAULT_LANE_DESCRIPTION = "This is a Lane";
    private final static String DEFAULT_PANEL_DESCRIPTION = "This is a Panel";
    private final static String NONE_ENTITY = "x_-1";
    /*
    约定格式如下：
    board_id    b_(int)
    user_id     (int)
    action（动作）      String ["update", "delete", "insert"]
    entity(指定类型)     ["card","lane","board"...]
    card_id   String (c_xxxx)
    lane_id   String (l_xxxx)
    content     (string)
    description     (String )
     */
    public String handlerJsonCode(String code) throws SQLException{

        //解析JSON
        JSONObject jsonObject = JSON.parseObject(code);
        String boardId = jsonObject.getString("board_id");
        String userId = jsonObject.getString("user_id");
        String action = jsonObject.getString("action");
        String entity = jsonObject.getString("entity");

        String cardId=null,laneId=null,panelId=null,content=null,description=null;
        if(jsonObject.containsKey("lane_id")) laneId = jsonObject.getString("lane_id");
        else laneId =NONE_ENTITY;
        if(jsonObject.containsKey("panel_id")) laneId = jsonObject.getString("panel_id");
        else panelId =NONE_ENTITY;
        if(jsonObject.containsKey("card_id"))cardId= jsonObject.getString("card_id");
        else cardId = NONE_ENTITY;

        if(jsonObject.containsKey("content"))
            content = jsonObject.getString("content");
        if(jsonObject.containsKey("description"))
            description = jsonObject.getString("description");


        //分配处理
        if (entity.length() != 0 && entity.equals(ENTITY_TYPE_CARD)) {
            Card card = dohandlerCard(Integer.parseInt(cardId.substring(2)), Integer.parseInt(userId), Integer.parseInt(panelId.substring(2)), action, content, description);
            if(card!=null) return encodeJson(card,jsonObject);
        }else if (entity.equals(ENTITY_TYPE_LANE)) {
            Lane lane= doHandlerLane(Integer.parseInt(laneId.substring(2)), Integer.parseInt(userId), Integer.parseInt(boardId.substring(2)), action, content, description);
            if(lane!=null) return encodeJson(lane,jsonObject);

        }else if (entity.equals(ENTITY_TYPE_BOARD)) {
            Board board= doHandlerBoard(Integer.parseInt(boardId.substring(2)), Integer.parseInt(userId), action, description);
            if(board!=null)return encodeJson(board,jsonObject);
        }
        else if (entity.equals(ENTITY_TYPE_PANEL)) {
            Panel panel= dohandlerPanel(Integer.parseInt(boardId.substring(2)), Integer.parseInt(laneId.substring(2)),action, description);
            if(panel!=null)return encodeJson(panel,jsonObject);
        }
        return null;
    }




    public Card dohandlerCard(int cardId , int userId, int laneId,String action,String content,String description)throws SQLException {
        switch (action){
            case REQUEST_INSERT:
                if(content==null)content ="";
                if(description == null) description=DEFAULT_CARD_DESCRIPTION;
                return boardService.addCard(laneId,DEFAULT_CARD_NAME,userId,content,description);
            case REQUEST_UPDATE:
                Card card = boardService.getCard(cardId);
                if(description==null) description =card.getDescription();
                boardService.updateCard(cardId,card.getCardName(),userId,content,description);
                return boardService.getCard(cardId);
            case REQUEST_DELETE:
                Card card2 = boardService.getCard(cardId);
                boardService.deleteCard(cardId);
                return card2;
            default:
                return null;
        }
    }

    //Lane的处理应该也有UserID的处理，或者用于一个动作表
    public Lane doHandlerLane(int laneId , int userId, int boardId, String action, String content, String description)throws SQLException{
        switch (action){
            case REQUEST_INSERT:
                if(description == null||description=="") description=DEFAULT_LANE_DESCRIPTION;
                return boardService.addLane(boardId,DEFAULT_LANE_NAME,description);
            case REQUEST_DELETE:
                Lane lane = boardService.getLane(laneId);
                boardService.deleteLane(laneId);
                return lane;
            default:
                return null;
        }
    }


    public Panel dohandlerPanel(int panelId, int laneId,String action,String description)throws SQLException {
        switch (action){
            case REQUEST_INSERT:
                if(description == null) description=DEFAULT_PANEL_DESCRIPTION;
                return boardService.addPanel(laneId,DEFAULT_PANEL_NAME,description);
            case REQUEST_UPDATE:
                Panel panel = boardService.getPanel(panelId);
                if(description==null||description=="") description =panel.getDescription();
                boardService.updatePanel(panelId,panel.getPanelName(),description);
                return panel;
            case REQUEST_DELETE:
                Panel panel2 = boardService.getPanel(panelId);
                boardService.deletePanel(panelId);
                return panel2;
            default:
                return null;
        }
    }


    public Board doHandlerBoard(int boardId , int userId, String action, String description)throws SQLException{
        switch (action){
            case REQUEST_INSERT:
                if(description == null||description=="") description=DEFAULT_BOARD_DESCRIPTION;
                return boardService.addBoard(DEFAULT_BOARD_NAME,userId,description);
            case REQUEST_DELETE:
                Board board =boardService.getBoard(boardId);
                boardService.deleteBoard(boardId);
                return board;
            default:
                return null;
        }
    }

    /**
     * @Auther: Kylinrix
     * @param: [entity, jsonObject]
     * @return: java.lang.String
     * @Date: 2019/1/5
     * @Email: Kylinrix@outlook.com
     * @Description:重构返回前端的JSONString，当前只能返回成功与失败（null）后期可以增加提醒的错误种类。
     * @Version: 1.0
     */
    private String encodeJson(Object entity,JSONObject jsonObject){

        String action = jsonObject.getString("action");
        String entityType = jsonObject.getString("entity");

        //添加insert后，添加对应的类型Id，前端需要得到ID才能做交互。
        if(action.equals(REQUEST_INSERT) ){
            if(entityType.equals(ENTITY_TYPE_CARD))jsonObject.put("card_id","c_"+String.valueOf(((Card)entity).getId()));
            if(entityType.equals(ENTITY_TYPE_LANE))jsonObject.put("lane_id","l_"+String.valueOf(((Lane)entity).getId()));
            if(entityType.equals(ENTITY_TYPE_BOARD))jsonObject.put("board_id","b_"+String.valueOf(((Board)entity).getId()));
            if(entityType.equals(ENTITY_TYPE_PANEL))jsonObject.put("panel_id","p_"+String.valueOf(((Panel)entity).getId()));
        }
        jsonObject.put("msg","success");
        jsonObject.put("code",0);
        return jsonObject.toJSONString();
    }

}
