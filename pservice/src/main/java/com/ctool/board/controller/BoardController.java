package com.ctool.board.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.service.BoardService;
import com.ctool.board.service.CardMemberService;
import com.ctool.model.ViewObject;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.ctool.util.String2IntUtil.string2IntId;

/**
 * @program: Ctool
 * @description: 这里的方法都缺少试验。
 * @author: Kylinrix
 * @create: 2019-01-03 15:56
 **/
@Controller
public class BoardController {


    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);


    @Reference
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CardMemberService cardMemberService;


    /**
     * @Author: Kylinrix
     * @param: [response, request, model, boardId]
     * @return: java.lang.String
     * @Date: 2019/1/15
     * @Email: Kylinrix@outlook.com
     * @Description: 返回页面
     */
    @RequestMapping(path={"/board/{boardId}"},method = {RequestMethod.GET})
    public String index(HttpServletResponse response,
                        HttpServletRequest request, Model model,@PathVariable("boardId") String boardId){
        int userId = (int)request.getSession().getAttribute("userId");

        if(!boardService.checkBoardExist(string2IntId(boardId))){
            model.addAttribute("msg","错误，看板不存在。");
            return "error";
        }
        if(boardService.checkIfBoardAuthorized(string2IntId(boardId),userId)!=0){
            model.addAttribute("msg","错误，权限不足。您不能访问该看板。");
            return "error";
        }
        else{
            return "board";
        }
    }

    //这里可以加入缓存
    @ResponseBody
    @RequestMapping(path={"/board/{boardId}"}, method = {RequestMethod.POST})
    public String getAllMsg(HttpServletResponse response,
                        HttpServletRequest request, Model model,@PathVariable("boardId") String boardId){

        List<Lane> subLanes = boardService.getLanesByBoardId(string2IntId(boardId));

        JSONObject res = new JSONObject();

        JSONObject jsonObject = new JSONObject();

        List<JSONObject> lanes = new ArrayList<>();

        for(Lane lane :subLanes){

            jsonObject.put("lane",lane);

            List <Panel> subPanels =boardService.getPanelByLaneId(lane.getId());
            List<JSONObject> panels = new ArrayList<>();

            JSONObject jsonObject2 = new JSONObject();

            for (Panel panel :subPanels){

                jsonObject2.put("panel",panel);

                List<JSONObject> cards = new ArrayList<>();
                List<Card> subCards = boardService.getCardByPanelId(panel.getId());

                JSONObject jsonObject3 = new JSONObject();

                for(Card card :subCards){

                    jsonObject3.put("card",card);

                    List<JSONObject> members = new ArrayList<>();
                    List<User> subMembers =cardMemberService.getCardMemberByJSON(card.getId());

                    JSONObject jsonObject4 = new JSONObject();

                    for (User user :subMembers){
                        System.out.println("user :"+user.getId());
                        jsonObject4.put("member",user);
                        members.add((JSONObject) jsonObject4.clone());
                    }
                    jsonObject3.put("members",members);
                    cards.add((JSONObject) jsonObject3.clone());
                }
                jsonObject2.put("cards",cards);
                panels.add((JSONObject) jsonObject2.clone());
            }
            jsonObject.put("panels",panels);
            lanes.add((JSONObject) jsonObject.clone());
        }
        res.put("lanes",lanes);

        return res.toJSONString();
    }



    /**
     * @Author: Kylinrix
     * @param: [response, request, model]
     * @return: java.lang.String
     * @Date: 2019/1/6
     * @Email: Kylinrix@outlook.com
     * @Description:加载用户所关联的看板,待测试！
     */
    @ResponseBody
    @RequestMapping(path = {"/board"},method = {RequestMethod.GET})
    public String userBoard(HttpServletResponse response,
                        HttpServletRequest request, Model model){

        //设置
        //request.getServletContext().getSessionCookieConfig().setDomain();
        //request.getSession()
        List<ViewObject> info = new ArrayList<ViewObject>();

        if(request.getSession().getAttribute("userId")!=null) {
            int userId = (int)request.getSession().getAttribute("userId");
            //List<JSONObject> subList = new ArrayList<>();
            List<Board> blist = boardService.getBoardsByUserid(userId);
//            for (Board  b:blist) {
//                JSONObject jsonObject= new JSONObject();
//                jsonObject.put("board",b);
//                subList.add(jsonObject);
//            }
            JSONObject res = new JSONObject();
            res.put("boards",blist);
            return JsonUtil.getJSONString(0,res);
        }
        else
            return JsonUtil.getJSONString(1,"失败","获得看板列表失败");

    }

    /**
     * @Author: Kylinrix
     * @param: [model, response, request, code, boardId]
     *          以下是code的三种值。
     *           BORAD_AUTHORIZATION_MEMBER =0;
     *           BORAD_AUTHORIZATION_ONLY_OWNER =1;
     *           BORAD_AUTHORIZATION_PUBLIC =2;
     * @return: java.lang.String
     * @Date: 2019/1/8
     * @Email: Kylinrix@outlook.com
     * @Description:
     */
    @ResponseBody
    @RequestMapping(path={"/changeAuthorization"},method = {RequestMethod.POST})
    public String changeAuthorization (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request,@RequestParam("authorization") int code,
                                       @RequestParam("boardId") String boardId){


        int userId = (int)request.getSession().getAttribute("userId");
        Board board =boardService.getBoard(string2IntId(boardId));
        if(board.getOwnUserId() == userId){
            boardService.updateBoardAuthorization(board.getId(),code);
            return JsonUtil.getJSONString(0);
        }
        else return JsonUtil.getJSONString(1,"fail","用户没有权限更改看板授权。");
    }

    @ResponseBody
    @RequestMapping(path={"board/addboard"},method = {RequestMethod.POST})
    public String addboard (Model model,
                                       HttpServletResponse response,
                                       HttpServletRequest request,
                                       @RequestParam("user_id") String userId ,
                                       @RequestParam("board_name") String boardName,
                                       @RequestParam("description") String description,
                                       @RequestParam("authorization") String authorization){
        try {
            Board board = boardService.addBoard(boardName, string2IntId(userId), description);
            boardService.updateBoardAuthorization(board.getId(),Integer.parseInt(authorization));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("board",board);
            return JsonUtil.getJSONString(0,jsonObject);
        }
        catch (Exception e){
            logger.warn("添加看板失败");
            return JsonUtil.getJSONString(1,"fail","添加看板失败");
        }
    }







    @ResponseBody
    @RequestMapping(path={"/test"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        

        System.out.println(request.getSession().getAttribute("userId"));
        return "8002:"+ request.getSession().getId();
    }

}
