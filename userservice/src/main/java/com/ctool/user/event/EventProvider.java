package com.ctool.user.event;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/10 17:16
 * @Email: Kylinrix@outlook.com
 * @Description: 事件发送者
 */
@Service
public class EventProvider {
    private static final Logger logger = LoggerFactory.getLogger(EventProvider.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * @Author: Kylinrix
     * @param: [topic, message]
     * @return: boolean
     * @Date: 2019/1/10
     * @Email: Kylinrix@outlook.com
     * @Description: 发送事件，由Kafka消费者解决。
     */
    public boolean fireEvent(String topic,String message){

        try {
            kafkaTemplate.send(topic, message);
            logger.info("发送事件: topic=" + topic + " ,message=" + message.toString() + ".");
            return true;
        }
        catch (Exception e){
            logger.warn("事件发送失败:"+e);
        }
        return false;
    }

}
