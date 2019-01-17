package com.ctool.board.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.service.BoardService;
import com.ctool.board.service.CardMemberService;
import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Board;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/8 17:45
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Controller
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Reference
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    CardMemberService cardMemberService;

    @ResponseBody
    @RequestMapping(path={"/addMemberToBoard"},method = {RequestMethod.GET,RequestMethod.POST})
    public String addMemberToBoard (Model model,
                                    HttpServletRequest request,
                                    @RequestBody String boardId,
                                    @RequestBody String memberId){
        BoardUserRelation boardUserRelation =boardService.addBoardMember(Integer.parseInt(boardId.substring(2)),Integer.parseInt(memberId));
        if(boardUserRelation.getId()>0)return JsonUtil.getJSONString(0);
        else return JsonUtil.getJSONString(1,"fail","看板添加成员失败");
    }

    @ResponseBody
    @RequestMapping(path={"/remoteMemberFromBoard"},method = {RequestMethod.GET,RequestMethod.POST})
    public String removeMemberFromBoard (Model model,
                                    HttpServletRequest request,
                                    @RequestBody String boardId,
                                    @RequestBody String memberId){
        if(boardService.removeBoardMember(Integer.parseInt(boardId.substring(2)),Integer.parseInt(memberId)))
            return JsonUtil.getJSONString(0);
        else return JsonUtil.getJSONString(1,"fail","看板删除成员失败");
    }



    @ResponseBody
    @RequestMapping(path={"/getMembers"},method = {RequestMethod.GET,RequestMethod.POST})
    public String getMembers (Model model,
                                    HttpServletRequest request,
                                    @RequestBody String boardId){
        List<User> ulist = boardService.getMemberByBoardId(Integer.parseInt(boardId.substring(2)));
        if(ulist.size()>0){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("members",ulist);
            return JsonUtil.getJSONString(0,jsonObject);
        }
        else{
            return JsonUtil.getJSONString(1,"fail","获取看板成员失败");

        }
    }



    @ResponseBody
    @RequestMapping(path={"/addCardMembers"},method = {RequestMethod.GET,RequestMethod.POST})
    public String addCardMissionUsers (Model model,
                                       HttpServletRequest request,
                                       @RequestBody String cardId,
                                       @RequestBody int userId){
        try {
            cardMemberService.addCardMemberByJSON(Integer.parseInt(cardId.substring(2)), userId);
            return JsonUtil.getJSONString(0);
        }catch (Exception e){
            logger.warn("添加卡片成员失败"+e);
            return JsonUtil.getJSONString(1, "失败","添加卡片成员失败");
        }
    }

    @ResponseBody
    @RequestMapping(path={"/getCardMembers"},method = {RequestMethod.GET,RequestMethod.POST})
    public String getCardMissionUsers (Model model,
                              HttpServletRequest request,
                              @RequestBody String cardId){

        List<User> ulist = cardMemberService.getCardMemberByJSON(Integer.parseInt(cardId.substring(2)));
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("cardMembers",ulist);
        return JsonUtil.getJSONString(0,jsonObject);
    }



    /**
     * @Auther: Kylinrix
     * @param: [model, request, boardId, searchEmail, searchName]
     * @return: java.lang.String
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description:查找用户，后续
     */
    @ResponseBody
    @RequestMapping(path={"/searchUser"},method = {RequestMethod.GET,RequestMethod.POST})
    public String addMemberToBoard (Model model,
                                    HttpServletRequest request,
                                    @RequestBody(required = true) String boardId,
                                    @RequestBody(required = false) String searchEmail,
                                    @RequestBody(required = true) String searchName){


        int userId = (int)request.getSession().getAttribute("userId");
        Board board =boardService.getBoard(Integer.parseInt(boardId.substring(2)));

        if(board.getOwnUserId() == userId) return JsonUtil.getJSONString(1,"fail","用户是该看板的创建者");

        if(boardService.checkIsBoardMember(board.getId(),userId)){
            return JsonUtil.getJSONString(1,"fail","该用户已为看板的成员,或为该看板的黑名单。");
        }

        User user = userService.getUserByName(searchName);
        if(user==null&&searchEmail!=null) user = userService.getUserByEmail(searchEmail);

        if(user!=null){
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("member",user);
            return JsonUtil.getJSONString(0,jsonObject);
        }
        else return JsonUtil.getJSONString(1,"fail","没有查找到该用户。");
    }
}
