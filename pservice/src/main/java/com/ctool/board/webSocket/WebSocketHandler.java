package com.ctool.board.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctool.board.service.ActionService;
import com.ctool.board.service.BoardService;
import com.ctool.util.JsonUtil;
import com.ctool.util.KeyWordUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @program: Ctool
 * @description: Websocket进入页处理，缺少心跳检测。
 * @author: KyLee
 * @create: 2019-01-03 14:51
 **/
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ActionService actionService;

    //一个 ChannelGroup 代表一个直播频道
    private  static Map<Integer, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    private static Long ActiveUser = 0L;




    private int boardId = -1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ActiveUser++;
        System.out.println("Channel接入！总计"+ ActiveUser+"。");


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //移除Channel
        channelGroupMap.get(this.boardId).remove(ctx.channel());
        ActiveUser--;
        System.out.println("Channel退出！总计"+ ActiveUser+"。");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //打印出channel唯一值，asLongText方法是channel的id的全名
       // System.out.println("handlerAdded："+ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
      //  System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {


        //msg为Json字符串。
        //获取前端发送的命令
        System.out.println("收到消息："+msg.text());
        JSONObject jsonObject = JSON.parseObject(msg.text());
        String boardId = jsonObject.getString("board_id");
        String userId = jsonObject.getString("user_id");

        //更新用户session超时时间
        //仍然需要拿到sessionId。。。。
        Object loginUserSessionId = redisTemplate.opsForValue().get(userId);
        redisTemplate.opsForValue().set(userId,
                loginUserSessionId,
                KeyWordUtil.LOGINUSER_TIMEOUT,
                TimeUnit.SECONDS);




        this.boardId=Integer.parseInt(boardId.substring(2));
        //加入频道
        if(this.boardId>0&&channelGroupMap.containsKey(this.boardId)){
            if(channelGroupMap.get(this.boardId).find(ctx.channel().id())==null) { channelGroupMap.get(boardId).add(ctx.channel()); }
        }
        else {
            channelGroupMap.put(this.boardId, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            channelGroupMap.get(this.boardId).add(ctx.channel());
        }

        //业务逻辑
        //处理数据库逻辑
        String resJson =null;
        try {
            resJson = actionService.handlerJsonCode(msg.text());
        }
        catch (Exception e){
            logger.warn("命令执行错误: "+e.toString());
        }


        //广播,带有两种失败原因
        //1、更新失败。
        //2、看板号异常。
        if (channelGroupMap.containsKey(boardId)) {
            if(resJson!=null){
                channelGroupMap.get(boardId).writeAndFlush(new TextWebSocketFrame(resJson));
            }
            else{
                resJson = JsonUtil.getJSONString(1,"fail","Board update failed");
                channelGroupMap.get(boardId).writeAndFlush(new TextWebSocketFrame(resJson));
            }
        }
        else{
            resJson = JsonUtil.getJSONString(1,"fail","The board number is wrong or not existed.");
            ctx.channel().writeAndFlush(resJson);
        }

        //System.out.println("通道数量："+channelGroupMap.get(boardId).size());

    }



}

