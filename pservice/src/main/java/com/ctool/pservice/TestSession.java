package com.ctool.pservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Kylinrix
 * @Date: 2018/12/26 16:25
 * @Email: Kylinrix@outlook.com
 * @Description:
 */
@Controller
public class TestSession {
    @ResponseBody
    @RequestMapping(path={"/test"},method = {RequestMethod.POST})
    public String login (Model model,
                         HttpServletResponse response,
                         HttpServletRequest request){
        return "8002:"+ request.getSession().getId();
    }

}
