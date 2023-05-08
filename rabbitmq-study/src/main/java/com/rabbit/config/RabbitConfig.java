package com.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("my_queue");
    }

    @Bean
    public Exchange myExchange() {
        return new DirectExchange("my_exchange", false, false, null);
    }

    @Bean
    public Binding myBinding() {
        return new Binding("my_queue", Binding.DestinationType.QUEUE, "my_exchange",
                "direct.biz.ex", null);
    }
}
