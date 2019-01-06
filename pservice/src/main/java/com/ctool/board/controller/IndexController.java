package com.ctool.board.controller;

import com.ctool.board.service.ActionService;
import com.ctool.board.service.BoardService;
import com.ctool.model.ViewObject;
import com.ctool.model.board.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.soap.Addressing;
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

    @Autowired
    BoardService boardService;

    @Autowired
    RedisTemplate redisTemplate;

    //加入boardId与userId
    //一般情况下，实现了Session共享的服务可以直接从session中得到userId。
    @RequestMapping(path={"/board/{boardId}"},method = {RequestMethod.GET})
    public String index(HttpServletResponse response,
                        HttpServletRequest request, Model model,@PathVariable("boardId") int boardId){
        model.addAttribute("boardId",boardId);
        int userId = (int)request.getSession().getAttribute("userId");
        if(redisTemplate.opsForSet().isMember(boardId,userId)){
            model.addAttribute("userId",userId);
            return "index";
        }
        else {
            model.addAttribute("msg","错误，你不是该看板的成员。");
            return "error";
        }
    }

    /**
     * @Auther: Kylinrix
     * @param: [response, request, model]
     * @return: java.lang.String
     * @Date: 2019/1/6
     * @Email: Kylinrix@outlook.com
     * @Description:加载用户所拥有的看板，后期可以增加用户关联的看板。
     */
    @RequestMapping(path = {"/board"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userBoard(HttpServletResponse response,
                        HttpServletRequest request, Model model){
        List<ViewObject> info = new ArrayList<ViewObject>();
        if(request.getSession().getAttribute("userId")!=null) {
            int userId = (int)request.getSession().getAttribute("userId");
            List<Board> blist = boardService.getBoardByUserid(userId);
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

    @ResponseBody
    @RequestMapping(path={"/test"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){

        System.out.println(request.getSession().getAttribute("userId"));
        return "8002:"+ request.getSession().getId();
    }

}
