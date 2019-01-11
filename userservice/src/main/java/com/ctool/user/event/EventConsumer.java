package com.ctool.user.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctool.user.service.MailService;
import com.ctool.util.KeyWordUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Kylinrix
 * @Date: 2019/1/10 17:20
 * @Email: Kylinrix@outlook.com
 * @Description: kafka分区设置，增大并发量
 */

@Component
public class EventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    MailService mailService;

    //Topic = "mail",partition = 0
    @KafkaListener(topics = {KeyWordUtil.KAFKA_MAIL_TOPIC},groupId = "mailgroup",
            topicPartitions = {@TopicPartition(topic = KeyWordUtil.KAFKA_MAIL_TOPIC, partitions = { "0" })})
    public void receiveMailMessage_0(String message){
        logger.info("Topic="+KeyWordUtil.KAFKA_MAIL_TOPIC+" 的分区 0 消费者接收消息 " + message);
        JSONObject jsonObject = JSON.parseObject(message);
        int id =Integer.parseInt(jsonObject.getString("id"));
        String userName =jsonObject.getString("name");
        String email =jsonObject.getString("email");
        mailService.sendVerifyMail(id,userName,email);
    }

    //Topic = "mail",partition = 1
    @KafkaListener(topics = {KeyWordUtil.KAFKA_MAIL_TOPIC},groupId = "mailgroup",
            topicPartitions = {@TopicPartition(topic = KeyWordUtil.KAFKA_MAIL_TOPIC, partitions = { "1" })})
    public void receiveMailMessage_1(String message){
        logger.info("Topic="+KeyWordUtil.KAFKA_MAIL_TOPIC+" 的分区 1 消费者接收消息 " + message);
        JSONObject jsonObject = JSON.parseObject(message);
        int id =Integer.parseInt(jsonObject.getString("id"));
        String userName =jsonObject.getString("name");
        String email =jsonObject.getString("email");
        mailService.sendVerifyMail(id,userName,email);
    }


    @KafkaListener(id = "test-group", topicPartitions = { @TopicPartition(topic = "test", partitions = { "0" }) })
    public void listenPartition0(List<ConsumerRecord<?, ?>> records) {
        logger.info("Id0 Listener, Thread ID: " + Thread.currentThread().getId());
        logger.info("Id0 records size " +  records.size());
        for (ConsumerRecord<?, ?> record : records) {
            //NULL值处理
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            logger.info("Received: " + record);
            if (kafkaMessage.isPresent()) {
                Object message = record.value();
                String topic = record.topic();
                logger.info("p0 Received message={}",  message);
            }
        }
    }
}
