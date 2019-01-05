package com.ctool.board.controller;

import com.ctool.board.service.ActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: Ctool
 * @description:
 * @author: KyLee
 * @create: 2019-01-03 15:56
 **/
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


    //加入boardId与userId
    //一般情况下，实现了Session共享的服务可以直接从session中得到userId。
    @RequestMapping("/board/{boardId}")
    public String index(HttpServletResponse response,
                        HttpServletRequest request, Model model,@PathVariable("boardId") int boardId){
        model.addAttribute("boardId",boardId);
        int userId = (int)request.getSession().getAttribute("userId");
        model.addAttribute("userId",userId);
        return "index";
    }
}
