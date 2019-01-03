package com.ctool.board.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @program: Ctool
 * @description: Websocket进入页处理
 * @author: KyLee
 * @create: 2019-01-03 14:51
 **/
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //一个 ChannelGroup 代表一个直播频道
    private  static Map<Integer, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    private static Long ActiveUser = 0L;

    private final static String REQUEST_ADD = "add";
    private final static String REQUEST_UPDATE = "update";
    private final static String REQUEST_DELETE = "delete";

    private final static String ENTITY_TYPE_CARD = "card";
    private final static String ENTITY_TYPE_LANE = "lane";


    private int BoardId = -1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ActiveUser++;
        System.out.println("Channel接入！总计"+ ActiveUser+"。");


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelGroupMap.get(this.BoardId).remove(ctx.channel());
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
        JSONObject req = JSON.parseObject(msg.text());
        Integer boardId = Integer.parseInt(req.get("boardId").toString());
        //String code = req.get("code").toString();
        //String id = req.get("id").toString();
        if(req.containsKey("content"));
            String content = req.get("content").toString();

        this.BoardId=boardId;
        //加入频道
        if(channelGroupMap.containsKey(boardId)){
            if(channelGroupMap.get(boardId).find(ctx.channel().id())==null) { channelGroupMap.get(boardId).add(ctx.channel());
            }
        }
        else {
            channelGroupMap.put(boardId, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
            channelGroupMap.get(boardId).add(ctx.channel());
        }

        //业务逻辑
        //.......


        //广播
        if (channelGroupMap.containsKey(boardId)) {
            channelGroupMap.get(boardId).writeAndFlush(new TextWebSocketFrame("test服务时间："+ LocalDateTime.now()));
        }

        //System.out.println("通道数量："+channelGroupMap.get(boardId).size());

    }



}

