package com.ctool.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: Ctool
 * @description:
 * @author: KyLee
 * @create: 2019-01-03 15:56
 **/
@Controller
public class IndexController {
    @RequestMapping("/board")
    public String index(){
        return "index";
    }
}
