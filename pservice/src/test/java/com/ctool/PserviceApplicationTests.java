package com.ctool;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.dao.BoardDAO;
import com.ctool.board.dao.BoardUserRelationDAO;
import com.ctool.board.dao.CardDAO;
import com.ctool.board.dao.LaneDAO;
import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//这是直接mysql-dump。测试使用，注意会删除表之后再新建表。
@Sql("/Board-init.sql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PserviceApplicationTests {

    @Reference
    UserService userService;

    @Autowired(required = false)
    BoardUserRelationDAO boardUserRelationDAO;

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    @Autowired(required = false)
    CardDAO cardDAO;

    @Test
    public void BoardSqlTest() {

        System.out.println("------------BoardSqlTest Test----------");
//        Board b = new Board();
//        b.setBoardName("b1");
//        b.setCreatedDate(new Date());
//        b.setOwnUserId(2);
//        b.setDescription("testb");
//        b.setAuthorization(0);
//
//        Lane lane = new Lane();
//        lane.setBoardId(1);
//        lane.setCreatedDate(new Date());
//        lane.setLaneName("l1");
//        lane.setDescription("testl");
//
//        Card card1 = new Card();
//        card1.setCardName("c1");
//        card1.setLaneId(1);
//        card1.setCreatedDate(new Date());
//        card1.setLastChanger(1);
//        card1.setCardContent("this is card1");
//
//        Card card2 = new Card();
//        card2.setCardName("c2");
//        card2.setLaneId(1);
//        card2.setCreatedDate(new Date());
//        card2.setLastChanger(1);
//        card2.setCardContent("this is card2");
//
//        int t = boardDAO.insertBoard(b);
//        System.out.println(t);
//        t=laneDAO.insertLane(lane);
//        System.out.println(t);
//         t=cardDAO.insertCard(card1);
//        System.out.println(t);
//        t=cardDAO.insertCard(card2);
//        System.out.println(card2.getId());
//        t=cardDAO.insertCard(card2);
//        System.out.println("should be 3 :"+card2.getId());
//
//        //t = boardDAO.deleteById(1);
//        System.out.println(t);
//
//        List<Card> list = cardDAO.selectByLaneId(1);
//
//        for (int i= 0;i<list.size();i++){
//            System.out.println(list.get(i).getCardName());
//        }
//

    }

    @Test
    public void remoteTest(){
        //Redis 与 远程Dubbo测试
//        System.out.println(userService.getUser("1"));
//        userService.checkAndUpdateIfUserExpired(1,"1234-1234");
    }

    @Rollback(false)
    @Test
    public void relationDAOTest(){


        //System.out.println(boardDAO.selectByName("b1234"));

        System.out.println("------------relaitionDAO Test----------");

        User user =  new User();
        user.setName("12343");
        user.setPassword("23");
        user.setSalt("23432");
        user.setStatus(0);
        user.setEmail("23423");
        userService.addUser(user);

        Board b = new Board();
        b.setBoardName("b1234");
        b.setCreatedDate(new Date());
        b.setOwnUserId(5);
        b.setDescription("testb2");
        b.setAuthorization(0);
        boardDAO.insertBoard(b);

        BoardUserRelation boardUserRelation = new BoardUserRelation();
        boardUserRelation.setBoardId(1);
        boardUserRelation.setUserId(1);
        boardUserRelation.setCreatedDate(new Date());
        boardUserRelation.setUserRole(0);
        boardUserRelationDAO.insertBoardUserRelation(boardUserRelation);
        user= boardUserRelationDAO.selectUsersByBoardId(1).get(0);
        System.out.println("从关系表所得的看板ID为"+1+"的成员列表的第一个用户 的salt :"+user.getSalt());

        Board board = boardUserRelationDAO.selectBoardsByUserId(1).get(0);
        System.out.println("从关系表所得的用户ID为"+1+"的看板列表的第一个看板名字为 :"+board.getBoardName() +" ，权限为"+ board.getAuthorization());

        System.out.println("以下为删除关系表，返回的应该是1。");
        System.out.println(boardUserRelationDAO.deleteBoardAndUserRelation(1,1));


    }

    @Test
    public void jsonHandlerTest(){

        System.out.println("------------JSON Test----------");
        JSONObject j =new JSONObject();
        j.put("code",1);
        j.put("msg","this is a msg");
        User user = new User();
        user.setId(1);
        user.setName("sdfds");
        user.setEmail("12324234234@123123.com");
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user);
        j.put("users",list);
        System.out.println(j.toJSONString());

    }



}

