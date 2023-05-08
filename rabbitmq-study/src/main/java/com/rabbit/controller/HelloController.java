package com.rabbit.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
public class HelloController {

    @Resource
    private AmqpTemplate amqpTemplate;

    @GetMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) {
        String uuid = UUID.randomUUID().toString();
        //String uuid = "123"; 测试重复消费,发送多次消息id一样的消息
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setMessageId(uuid).build();
        amqpTemplate.convertAndSend("my_exchange", "direct.biz.ex", msg);
        return "ok";
    }
}
