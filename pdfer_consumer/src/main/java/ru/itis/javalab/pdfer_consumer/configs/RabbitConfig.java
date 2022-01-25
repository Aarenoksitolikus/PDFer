package ru.itis.javalab.pdfer_consumer.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${rabbit.key}")
    private String key;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable().withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .autoDelete().build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(key);
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> containerFactory(ConnectionFactory rabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        factory.setPrefetchCount(10);
        factory.setConcurrentConsumers(5);
        return factory;
    }
}
