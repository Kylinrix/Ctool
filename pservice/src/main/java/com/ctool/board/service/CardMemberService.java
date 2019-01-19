package com.ctool.board.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctool.model.user.User;
import com.ctool.remoteService.UserService;
import com.ctool.util.KeyWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/16 14:13
 * @Email: Kylinrix@outlook.com
 * @Description: 分配卡片给用户
 */
@Service
public class CardMemberService {
    @Autowired
    RedisTemplate redisTemplate;

    @Reference
    UserService userService;

     private final static  int  limit = 3;


     //数据库存取
    public List<String> getCardMissionUserIds(int cardId){
        //暂时不清楚返回的是int还是String.
        return redisTemplate.opsForList().range(KeyWordUtil.CARD_MEMBER_PREFIX+String.valueOf(cardId),0,-1);
    }

    public void setCardMissionUserId(int cardId,int userId){
        redisTemplate.opsForList().leftPush(KeyWordUtil.CARD_MEMBER_PREFIX+String.valueOf(cardId),userId);
    }

    //这里从数据库取三次太慢，可以考虑Redis直接保存User的JSON
    public List<User> getCardMissionUsers(int cardId) {
        List<String> listIds = getCardMissionUserIds(cardId);
        List<User> res = new ArrayList<>();
        for (int i = 0; i < listIds.size(); i++) {
            User user = userService.getUserById(Integer.parseInt(listIds.get(i)));
            res.add(user);
        }
        return res;
    }

    //Redis JSON 方式
    public List<User> getCardMemberByJSON(int cardId){

        List<String> userList= redisTemplate.opsForList().range(KeyWordUtil.CARD_MEMBER_PREFIX+String.valueOf(cardId),0,-1);
        List<User> res = new ArrayList<>();
        for(int i= 0;i<userList.size();i++){
            //System.out.println("jsonObject: "+userList.get(i));
            User user = (JSONObject.parseObject(userList.get(i),User.class));

            //User user = JSON.parseObject(userList.get(0),User.class);
            res.add(user);
        }
        return res;
    }

    public List<User> addCardMemberByJSON(int cardId,int userId){
        User user = userService.getUserById(userId);
        String userJson = JSONObject.toJSONString(user);

        //这里需要一次检查是否已存在！
        redisTemplate.opsForList().leftPush(KeyWordUtil.CARD_MEMBER_PREFIX+String.valueOf(cardId),userJson);
        return getCardMemberByJSON(cardId);
    }


    public boolean removeCardMemberByJSON(int cardId,int userId){
        User user = userService.getUserById(userId);
        String userJson = JSONObject.toJSONString(user);
        if(redisTemplate.opsForList().remove(KeyWordUtil.CARD_MEMBER_PREFIX+String.valueOf(cardId),1,userJson)>0)
            return true;
        else return false;
    }

}
