package com.rabbit.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class HelloListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloListener.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = "my_queue")
    public void service(Message message) {
        String messageId = message.getMessageProperties().getMessageId();
        Boolean hasKey = redisTemplate.hasKey(messageId);
        Assert.notNull(hasKey, "消息缓存不能为空");
        if (hasKey) {
            String value = Objects.requireNonNull(redisTemplate.opsForValue().get(messageId)).toString();
            if (messageId.equals(value)) {
                LOGGER.info("消息已经被消费,消息id:{}", messageId);
                return;
            }
        } else {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(messageId, messageId);
            Assert.notNull(result, "消息缓存不能为空");
            if (!result) {
                LOGGER.info("消息缓存失败,其他消费者已消费,消息id:{}", messageId);
                return;
            }
        }
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("监听消息队列的消息:{}", msg);
    }
}
