package com.ctool.board.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.util.JsonUtil;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

import static com.ctool.util.String2IntUtil.string2IntId;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/16 16:38
 * @Email: Kylinrix@outlook.com
 * @Description: update方法需要优化
 */

@Service
public class ActionNettyService {

    private static final Logger logger = LoggerFactory.getLogger(ActionNettyService.class);

    private final static String DEFAULT_CARD_NAME = "卡片";
    private final static String DEFAULT_LANE_NAME = "泳道";
    private final static String DEFAULT_BOARD_NAME = "看板";
    private final static String DEFAULT_PANEL_NAME = "release";


    private final static String DEFAULT_BOARD_CONTENT = "";
    private final static String DEFAULT_CARD_CONTENT = "";


    private final static String DEFAULT_CARD_DESCRIPTION = "This is a Card";
    private final static String DEFAULT_LANE_DESCRIPTION = "This is a Lane";
    private final static String DEFAULT_PANEL_DESCRIPTION = "This is a Panel";

    @Autowired
    BoardService boardService;

    @Autowired
    CardMemberService cardMemberService;

    @Reference
    UserService userService;

   /**
    * 以下所有方法都标明了需要的字段。
    * 使用action字段指明调用的方法。
    * 有些方法的返回JSON中带有目标Bean。
    * 前端所有ID都以单词小写首字母加下划线做前缀。即
    * Card -> c_xxxx
    * Panel -> p_xxxx
    * Lane -> l_xxxx
    * Board ->b_xxxx
    * User -> u_xxxx
    * CardMember -> cm_xxxx
    * BoardMember ->bm_xxxx
    *
    * 后端所有的ID都为int。传回前端的ID也是int形式的，前端需要自己提取后，加入前缀。
    */
   public String handlerJsonCode(String code)  {

       //解析JSON
       JSONObject jsonObject = JSON.parseObject(code);
       String action = jsonObject.getString("action");

       switch (action){
           //心跳检测
           case "heart_beat":return JsonUtil.getJSONString(999,"heart_beat_reply");
           //卡片
           case "add_card":return addCard(jsonObject);
           case "update_card": return updateCard(jsonObject);
           case "delete_card":return deleteCard(jsonObject);

           //卡片成员
           case "add_member_to_card":return addMemberToCard(jsonObject);
           case "remove_member_from_card":return removeMemberFromCard(jsonObject);

           //panel
           case "add_panel":return addPanel(jsonObject);
           case "update_panel":return updatePanel(jsonObject);
           case "delete_panel":return deletePanel(jsonObject);

           //泳道
           case "add_lane":return addLane(jsonObject);
           case "delete_lane":return deleteLane(jsonObject);

           //看板成员
           case "add_member_to_board":return addMemberToBoard(jsonObject);
           case "remove_member_from_board":return removeMemberFromBoard(jsonObject);

           default:return JsonUtil.getJSONString(1, "Action Exception", "没有指定动作！");
       }

   }


    /**
     * 卡片
     *
     */
    private String addCard(JSONObject jsonObject){
        //必需字段
        String userId,panelId;
        //非必需字段
        String content,description;

        //获取字段
        userId = jsonObject.getString("user_id");
        panelId = jsonObject.getString("panel_id");
        content = jsonObject.getString("content");
        description = jsonObject.getString("description");

        //检查
        if(userId==null) {
            logger.warn("addCard 缺少user_id");
            return JsonUtil.getJSONString(1, "添加卡片失败", "没有用户！");
        }
        if(panelId==null){
            logger.warn("addCard 缺少panel_id");
            return JsonUtil.getJSONString(1, "添加卡片失败", "没有指定Panel！");
        }
        if(content==null||content.equals(""))content =DEFAULT_CARD_CONTENT;
        if(description == null||description.equals("")) description=DEFAULT_CARD_DESCRIPTION;

        //操作
        Card card = boardService.addCard(
                string2IntId(panelId),
                DEFAULT_CARD_NAME,
                string2IntId(userId),
                content,
                description);

        //添加结果
        //这里返回的结果是int，前端需要加上ID前缀
        jsonObject.put("code",0);
        jsonObject.put("action", "add_card");
        jsonObject.put("laneId", boardService.getLane(boardService.getPanel(card.getId()).getLaneId()));
        jsonObject.put("card",card);
        return jsonObject.toJSONString();
    }

    //三个SQL需要优化!
    private String updateCard(JSONObject jsonObject){
        //必需字段
        String userId,panelId,cardId;
        //非必需字段
        String content,description;

        //获取字段
        userId = jsonObject.getString("user_id");
        panelId = jsonObject.getString("panel_id");
        cardId = jsonObject.getString("card_id");
        content = jsonObject.getString("content");
        description = jsonObject.getString("description");

        //检查
        if(userId==null) {
            logger.warn("updateCard 缺少user_id");
            return JsonUtil.getJSONString(1, "更新卡片失败", "没有用户！");
        }
        if(panelId==null){
            logger.warn("updateCard 缺少panel_id");
            return JsonUtil.getJSONString(1, "更新卡片失败", "没有指定Panel！");
        }
        if(cardId==null){
            logger.warn("updateCard 缺少card_id");
            return JsonUtil.getJSONString(1, "更新卡片失败", "没有指定卡片！");
        }
        Card card = boardService.getCard(string2IntId(cardId));
        if(content==null)content =card.getCardContent();
        if(description == null) description=card.getDescription();


        //操作
        boardService.updateCard(
                string2IntId(cardId),
                card.getCardName(),
                string2IntId(userId),
                content,
                description);

        //返回结果
        jsonObject.put("code",0);
        jsonObject.put("action", "update_card");
        jsonObject.put("card",boardService.getCard(string2IntId(cardId)));
        return jsonObject.toJSONString();
    }

    private String deleteCard(JSONObject jsonObject){
        //必需字段
        String cardId;

        //获取字段
        cardId = jsonObject.getString("card_id");

        //检查
        if(cardId==null) {
            logger.warn("deleteCard 缺少card_id");
            return JsonUtil.getJSONString(1, "删除卡片失败", "没有指定卡片！");
        }

        //操作
        boardService.deleteCard(string2IntId(cardId));

        //添加结果
        jsonObject.put("code",0);
        jsonObject.put("action", "delete_card");
        return jsonObject.toJSONString();
    }



    /**
     * 卡片成员
     *
     */
    private String addMemberToCard(JSONObject jsonObject){
        //必需字段
        String cardId,userId,memberId;

        //获得字段
        cardId = jsonObject.getString("card_id");
        userId = jsonObject.getString("user_id");
        memberId = jsonObject.getString("member_id");


        //检查
        if(cardId==null) {
            logger.warn("addMemberToCard 缺少card_id");
            return JsonUtil.getJSONString(1, "添加卡片成员失败", "没有指定卡片！");
        }
        if(userId==null) {
            logger.warn("addMemberToCard 缺少user_id");
            return JsonUtil.getJSONString(1, "添加卡片成员失败", "没有指定用户！");
        }
        if(memberId==null) {
            logger.warn("removeMemberFromCard 缺少member_id");
            return JsonUtil.getJSONString(1, "删除卡片成员失败", "没有指定成员！");
        }

        List<User> members ;
        //操作
        try {
            members =cardMemberService.addCardMemberByJSON(string2IntId(cardId), string2IntId(memberId));
        }catch (Exception e){
            logger.warn("添加卡片成员失败"+e);
            return JsonUtil.getJSONString(1, "添加卡片成员失败","添加卡片成员操作失败");
        }

        //添加返回字段，新增的卡片成员。
        jsonObject.put("code",0);
        jsonObject.put("action", "add_member_to_card");
        jsonObject.put("members",members);
        return jsonObject.toJSONString();
    }

    private String removeMemberFromCard(JSONObject jsonObject){
        //必需字段
        String cardId,userId,memberId;

        //获得字段
        cardId = jsonObject.getString("card_id");
        userId = jsonObject.getString("user_id");
        memberId = jsonObject.getString("member_id");

        //检查
        if(cardId==null) {
            logger.warn("removeMemberFromCard 缺少card_id");
            return JsonUtil.getJSONString(1, "删除卡片成员失败", "没有指定卡片！");
        }
        if(userId==null) {
            logger.warn("removeMemberFromCard 缺少user_id");
            return JsonUtil.getJSONString(1, "删除卡片成员失败", "没有指定用户！");
        }
        if(memberId==null) {
            logger.warn("removeMemberFromCard 缺少member_id");
            return JsonUtil.getJSONString(1, "删除卡片成员失败", "没有指定成员！");
        }

        //操作
        if(!cardMemberService.removeCardMemberByJSON(string2IntId(cardId),string2IntId(memberId))){
            logger.warn("removeMemberFromCard 失败");
            return JsonUtil.getJSONString(1, "删除卡片成员失败", "删除卡片成员操作失败！");
        }

        //添加返回字段
        jsonObject.put("code",0);
        jsonObject.put("action", "remove_member_from_card");
        return jsonObject.toJSONString();
    }



    /**
     * panel
     *
     */
    private String addPanel(JSONObject jsonObject){
        //必需字段
        String userId,laneId;
        //非必需字段
        String description;

        //获取字段
        userId = jsonObject.getString("user_id");
        laneId = jsonObject.getString("panel_id");
        description = jsonObject.getString("description");

        //检查
        if(userId==null) {
            logger.warn("addPanel 缺少user_id");
            return JsonUtil.getJSONString(1, "添加Panel失败", "没有用户！");
        }
        if(laneId==null){
            logger.warn("addPanel 缺少panel_id");
            return JsonUtil.getJSONString(1, "添加Panel失败", "没有指定Lane！");
        }
        if(description == null||description.equals("")) description=DEFAULT_PANEL_DESCRIPTION;

        //操作
        Panel panel = boardService.addPanel(string2IntId(laneId),DEFAULT_PANEL_NAME,description);

        //添加结果
        jsonObject.put("code",0);
        jsonObject.put("action", "add_panel");
        jsonObject.put("panel",panel);
        return jsonObject.toJSONString();


    }

    private String updatePanel(JSONObject jsonObject){
        //必需字段
        String userId,panelId;
        //非必需字段
        String description,panelName;

        //获取字段
        userId = jsonObject.getString("user_id");
        panelId = jsonObject.getString("panel_id");
        description = jsonObject.getString("description");
        panelName = jsonObject.getString("panel_name");


        //检查
        if(userId==null) {
            logger.warn("updatePanel 缺少user_id");
            return JsonUtil.getJSONString(1, "更新卡片失败", "没有用户！");
        }
        if(panelId==null){
            logger.warn("updatePanel 缺少panel_id");
            return JsonUtil.getJSONString(1, "更新卡片失败", "没有指定Panel！");
        }

        Panel panel = boardService.getPanel(string2IntId(panelId));
        if(panelName == null) panelName =panel.getPanelName();
        if(description == null) description=panel.getDescription();


        //操作
        boardService.updatePanel(string2IntId(panelId),panelName,description);

        //返回结果
        jsonObject.put("code",0);
        jsonObject.put("action", "update_panel");
        jsonObject.put("panel",boardService.getPanel(string2IntId(panelId)));
        return jsonObject.toJSONString();
    }

    private String deletePanel(JSONObject jsonObject){
        //必需字段
        String panelId;

        //获取字段
        panelId = jsonObject.getString("panel_id");

        //检查
        if(panelId==null) {
            logger.warn("deletePanel 缺少card_id");
            return JsonUtil.getJSONString(1, "删除Panel失败", "没有指定panel！");
        }

        //操作
        boardService.deletePanel(string2IntId(panelId));

        //添加结果
        jsonObject.put("code",0);
        jsonObject.put("action", "delete_panel");
        return jsonObject.toJSONString();
    }



    /**
     * 泳道
     *
     */
    private String addLane(JSONObject jsonObject){
        //必需字段
        String userId,boardId;
        //非必需字段
        String description;

        //获取字段
        userId = jsonObject.getString("user_id");
        boardId = jsonObject.getString("board_id");
        description = jsonObject.getString("description");

        //检查
        if(userId==null) {
            logger.warn("addPanel 缺少user_id");
            return JsonUtil.getJSONString(1, "添加泳道失败", "没有用户！");
        }
        if(boardId==null){
            logger.warn("addPanel 缺少board_id");
            return JsonUtil.getJSONString(1, "添加泳道失败", "没有指定看板！");
        }

        if(description == null||description.equals("")) description=DEFAULT_LANE_DESCRIPTION;

        //操作
        Lane lane =boardService.addLane(string2IntId(boardId),DEFAULT_LANE_NAME,description);

        //添加结果
        //这里返回的结果是int，前端需要加上ID前缀
        jsonObject.put("code",0);
        jsonObject.put("action", "add_lane");
        jsonObject.put("lane",lane);
        return jsonObject.toJSONString();
    }

    private String deleteLane(JSONObject jsonObject){
        //必需字段
        String laneId;

        //获取字段
        laneId = jsonObject.getString("lane_id");

        //检查
        if(laneId==null) {
            logger.warn("deleteLane 缺少lane_id");
            return JsonUtil.getJSONString(1, "删除lane失败", "没有指定lane！");
        }

        //操作
        boardService.deleteLane(string2IntId(laneId));

        //添加结果
        jsonObject.put("code",0);
        jsonObject.put("action", "delete_lane");
        return jsonObject.toJSONString();
    }


    /**
     * 看板成员
     *
     */
    private String addMemberToBoard(JSONObject jsonObject){
        //必需字段
        String boardId,memberId;

        //获得字段
        boardId = jsonObject.getString("board_id");
        memberId = jsonObject.getString("member_id");

        //检查
        if(boardId==null) {
            logger.warn("addMemberToBoard 缺少board_id");
            return JsonUtil.getJSONString(1, "添加看板成员失败", "没有指定看板！");
        }
        if(memberId==null) {
            logger.warn("addMemberToBoard 缺少member_id");
            return JsonUtil.getJSONString(1, "添加看板成员失败", "没有指定成员！");
        }

        //操作
        BoardUserRelation boardUserRelation =boardService.addBoardMember(string2IntId(boardId),string2IntId(memberId));
        if(boardUserRelation.getId()<=0)
            return JsonUtil.getJSONString(1,"添加看板成员失败","操作失败");


        //返回
        jsonObject.put("code",0);
        jsonObject.put("action", "add_member_to_board");
        jsonObject.put("member",userService.getUserById(boardUserRelation.getUserId()));
        return jsonObject.toJSONString();

    }

    private String removeMemberFromBoard(JSONObject jsonObject){
        //必需字段
        String boardId,memberId;

        //获得字段
        boardId = jsonObject.getString("board_id");
        memberId = jsonObject.getString("member_id");

        //检查
        if(boardId==null) {
            logger.warn("removeMemberFromBoard 缺少board_id");
            return JsonUtil.getJSONString(1, "移除看板成员失败", "没有指定看板！");
        }
        if(memberId==null) {
            logger.warn("removeMemberFromBoard 缺少member_id");
            return JsonUtil.getJSONString(1, "移除看板成员失败", "没有指定成员！");
        }

        //操作
        if(!boardService.removeBoardMember(string2IntId(boardId),string2IntId(memberId)))
            return JsonUtil.getJSONString(1,"移除看板成员失败","操作失败");

        //返回
        jsonObject.put("code",0);
        jsonObject.put("action", "remove_member_from_board");
        return jsonObject.toJSONString();
    }



}
