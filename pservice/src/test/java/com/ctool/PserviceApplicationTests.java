package com.ctool;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.dao.BoardDAO;
import com.ctool.board.dao.CardDAO;
import com.ctool.board.dao.LaneDAO;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.remoteService.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

//这是直接mysql-dump。测试使用，注意会删除表之后再新建表。
@Sql("/Board-init.sql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PserviceApplicationTests {

    @Reference(check = false)
    UserService userService;

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    @Autowired(required = false)
    CardDAO cardDAO;

    @Test
    public void BoardSqlTest() {
        Board b = new Board();
        b.setBoardName("b1");
        b.setCreatedDate(new Date());
        b.setOwnUserId(5);
        b.setDescription("testb");

        Lane lane = new Lane();
        lane.setBoardId(1);
        lane.setCreatedDate(new Date());
        lane.setLaneName("l1");
        lane.setDescription("testl");

        Card card1 = new Card();
        card1.setCardName("c1");
        card1.setLaneId(1);
        card1.setCreatedDate(new Date());
        card1.setLastChanger(1);
        card1.setCardContent("this is card1");

        Card card2 = new Card();
        card2.setCardName("c2");
        card2.setLaneId(1);
        card2.setCreatedDate(new Date());
        card2.setLastChanger(1);
        card2.setCardContent("this is card2");

        int t = boardDAO.insertBoard(b);
        System.out.println(t);
        t=laneDAO.insertLane(lane);
        System.out.println(t);
         t=cardDAO.insertCard(card1);
        System.out.println(t);
        t=cardDAO.insertCard(card2);
        System.out.println(card2.getId());
        t=cardDAO.insertCard(card2);
        System.out.println("should be 3 :"+card2.getId());

        t = boardDAO.deleteById(1);
        System.out.println(t);

        List<Card> list = cardDAO.selectByLaneId(1);

        for (int i= 0;i<list.size();i++){
            System.out.println(list.get(i).getCardName());
        }

    }
    @Test
    public void remoteTest(){
        //Redis 与 远程Dubbo测试
//        System.out.println(userService.getUser("1"));
//        userService.checkAndUpdateIfUserExpired(1,"1234-1234");
    }

    @Test
    public void jsonHandlerTest(){

        JSONObject j =new JSONObject();
        j.put("code",1);
        j.put("msg","this is a msg");
        System.out.println(j.toJSONString());
        System.out.println(j.toString());
        System.out.println(j.toJSONString().equals(j.toString()));
    }



}

