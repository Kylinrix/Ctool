package com.ctool;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.dao.BoardDAO;
import com.ctool.board.dao.BoardUserRelationDAO;
import com.ctool.board.dao.CardDAO;
import com.ctool.board.dao.LaneDAO;
import com.ctool.board.service.ActionNettyService;
import com.ctool.board.service.BoardService;
import com.ctool.board.service.CardMemberService;
import com.ctool.model.BoardUserRelation;
import com.ctool.model.board.Board;
import com.ctool.model.board.Card;
import com.ctool.model.board.Lane;
import com.ctool.model.board.Panel;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.util.JsonUtil;
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

import static com.ctool.util.String2IntUtil.string2IntId;

//这是直接mysql-dump。测试使用，注意会删除表之后再新建表。
//@Sql("Board-init.sql")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PserviceApplicationTests {

    @Reference
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired(required = false)
    BoardUserRelationDAO boardUserRelationDAO;

    @Autowired(required = false)
    BoardDAO boardDAO;

    @Autowired(required = false)
    LaneDAO laneDAO;

    @Autowired(required = false)
    CardDAO cardDAO;

    @Autowired
    CardMemberService cardMemberService;


    @Autowired
    ActionNettyService actionNettyService;

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

    //@Rollback(false)
    @Test
    public void relationDAOTest(){



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
        b.setOwnUserId(4);
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


        boardService.addBoard("test-json",1,"sd");

        boardService.addBoardMember(1,1);

        boardService.addLane(1,"qwe","asdffa");

        boardService.addPanel(1,"fda","adfdsaf");

        boardService.addPanel(1,"fda","adfdsaf");

        boardService.addCard(1,"123",1,"d","dfas");
        boardService.addCard(1,"123",1,"d","dfas");
        boardService.addCard(1,"123",1,"d","dfas");



        System.out.println("------------JSON Test----------");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action","add_card");
        jsonObject.put("user_id","u_1");
        jsonObject.put("panel_id","p_1");
        jsonObject.put("content","this is a test");

        System.out.println(actionNettyService.handlerJsonCode(jsonObject.toJSONString()));

    }

    @Test
    public void getAllMsgTest(){


        System.out.println("------------getAllMsg Test----------");
        boardService.addBoard("test-json",1,"sd");

        boardService.addBoardMember(1,1);

        boardService.addLane(1,"qwe","asdffa");

        boardService.addLane(1,"空","asdffa");

        boardService.addPanel(1,"1p","adfdsaf");

        boardService.addPanel(1,"2p","adfdsaf");

        Card c =boardService.addCard(1,"1p-1c",1,"d","dfas");
         boardService.addCard(1,"1p-2c",1,"d","dfas");
        boardService.addCard(1,"1p-3c",1,"d","dfas");


        boardService.addCard(2,"2p-1c",1,"d","dfas");
        boardService.addCard(2,"2p-2c",1,"d","dfas");
        boardService.addCard(2,"2p-3c",1,"d","dfas");

        cardMemberService.addCardMemberByJSON(c.getId(),1);
        cardMemberService.addCardMemberByJSON(c.getId(),2);

        int boardId = 1;
        List<Lane> subLanes = boardService.getLanesByBoardId(boardId);

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

        System.out.println(res);
    }


}

