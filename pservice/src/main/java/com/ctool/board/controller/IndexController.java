package com.ctool.board.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ctool.board.service.BoardService;
import com.ctool.model.ViewObject;
import com.ctool.model.board.Board;
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

/**
 * @program: Ctool
 * @description: 这里的方法都缺少试验。
 * @author: Kylinrix
 * @create: 2019-01-03 15:56
 **/
@Controller
public class IndexController {


    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    @Reference
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * @Author: Kylinrix
     * @param: [response, request, model, boardId]
     * @return: java.lang.String
     * @Date: 2019/1/15
     * @Email: Kylinrix@outlook.com
     * @Description: 返回的页面需要有Board的所有数据 ，包括lanes、panels、cards、members，
     *                  这里使用模板还是JSON？
     */
    @RequestMapping(path={"/board/{boardId}"},method = {RequestMethod.GET})
    public String index(HttpServletResponse response,
                        HttpServletRequest request, Model model,@PathVariable("boardId") String boardId){
        model.addAttribute("boardId",boardId);
        int userId = (int)request.getSession().getAttribute("userId");


        if(boardService.checkIfBoardAuthorized(Integer.parseInt(boardId.substring(2)),userId)!=0){
            model.addAttribute("msg","错误，权限不足。您不能访问该看板。");
            return "error";
        }
        else{
            model.addAttribute("userId",userId);
            return "index";
        }
    }

    /**
     * @Author: Kylinrix
     * @param: [response, request, model]
     * @return: java.lang.String
     * @Date: 2019/1/6
     * @Email: Kylinrix@outlook.com
     * @Description:加载用户所关联的看板
     */
    @RequestMapping(path = {"/board"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userBoard(HttpServletResponse response,
                        HttpServletRequest request, Model model){

        //设置关于
        //request.getServletContext().getSessionCookieConfig().setDomain();
        //request.getSession()
        List<ViewObject> info = new ArrayList<ViewObject>();

        if(request.getSession().getAttribute("userId")!=null) {
            int userId = (int)request.getSession().getAttribute("userId");
            List<Board> blist = boardService.getBoardsByUserid(userId);
            for (Board  b:blist) {
                ViewObject vo = new ViewObject();
                vo.set("boardId","b_"+String.valueOf(b.getId()));
                vo.set("boardName",b.getBoardName());
                vo.set("createDate",b.getCreatedDate());
                vo.set("description",b.getDescription());
                info.add(vo);
            }
            model.addAttribute("boards",info);
            return "boards";
        }
        return "error";
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
    @RequestMapping(path={"/changeAuthorization"},method = {RequestMethod.GET,RequestMethod.POST})
    public String changeAuthorization (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request,@RequestParam("authorization") int code,
                                       @RequestParam("boardId") String boardId){


        int userId = (int)request.getSession().getAttribute("userId");
        Board board =boardService.getBoard(Integer.parseInt(boardId.substring(2)));
        if(board.getOwnUserId() == userId){
            boardService.updateBoardAuthorization(board.getId(),code);
            return JsonUtil.getJSONString(0);
        }
        else return JsonUtil.getJSONString(1,"fail","用户没有权限更改看板授权。");
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
