package ru.itis.javalab.pdfer.utils;

import com.rabbitmq.client.BasicProperties;

import java.util.UUID;

public class Rabbit {
//    public Long fuck() {
//        String correlationId = UUID.randomUUID().toString();
//        try {
//            // формируем свойства, которые мы направим вместе с задачей (сообщением)
//            BasicProperties properties = new BasicProperties.Builder()
//                    .correlationId(correlationId) // id сообщения
//                    .replyTo(replyQueueName) // очередь конкретного клиента
//                    .build();
//            // публикуем задачу
//            channel.basicPublish("", REQUEST_QUEUE_NAME, properties, fileUrl.getBytes());
//
//            // класс, позволяющий отложить задачу в поток
//            CompletableFuture<Long> result = new CompletableFuture<>();
//
//            String consumerTag = channel.basicConsume(replyQueueName, true, (tag, message) -> {
//                // смотрим, этот ответ был конкретно по этой задаче?
//                if (message.getProperties().getCorrelationId().equals(correlationId)) {
//                    // кладем результат (размер файла в result) и идем дальше
//                    result.complete(Long.parseLong(new String(message.getBody())));
//                }
//            }, tag -> {});
//            // вы не получите размер файла, пока у result не будет вызван complete
//            Long value = result.get();
//            // если получили ответ - больше ничего не делаем
//            channel.basicCancel(consumerTag);
//            return value;
//        } catch (IOException | InterruptedException | ExecutionException e) {
//            throw new IllegalArgumentException(e);
//        }
//
//    }
}
