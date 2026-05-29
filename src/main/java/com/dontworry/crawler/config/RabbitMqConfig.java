package com.dontworry.crawler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String CRAWLER_EXCHANGE = "crawler.exchange";

    public static final String PLACE_QUEUE = "crawler.place.queue";
    public static final String PLACE_ROUTING_KEY = "crawler.place";

    @Bean
    public Declarables crawlerDeclarables() {
        TopicExchange crawlerExchange = new TopicExchange(CRAWLER_EXCHANGE);

        Queue placeQueue = new Queue(PLACE_QUEUE, true);

        Binding placeBinding = BindingBuilder
                .bind(placeQueue)
                .to(crawlerExchange)
                .with(PLACE_ROUTING_KEY);


        return new Declarables(
                crawlerExchange,
                placeQueue,
                placeBinding
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
