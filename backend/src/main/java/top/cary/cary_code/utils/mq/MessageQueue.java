package top.cary.cary_code.utils.mq;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@SuppressWarnings("rawtypes")
public class MessageQueue {

    private final Map<String, ArrayDeque<Message>> topicToQueue = new HashMap<>();

    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public void offer(Message message) {
        String topic = message.getTopic();
        registerIfNew(topic);
        Queue<Message> queue = topicToQueue.get(topic);
        synchronized (queue) {
            queue.offer(message);
//            queue.notifyAll();
        }
    }

    public long offer(String topic, Object data) {
        Message message = Message.builder()
                .topic(topic)
                .data(data)
                .id(threadLocalRandom.nextLong())
                .time(LocalDateTime.now())
                .build();
        offer(message);
        return message.getId();
    }

    public Message pollByTopic(String topic) {
        Queue<Message> queue = topicToQueue.get(topic);
        if (queue == null) {
            return null;
        }
        synchronized (queue) {
//            while (queue.isEmpty()) {
//                try {
//                    queue.wait();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            return queue.poll();
        }
    }

    private void registerIfNew(String topic) {
        if (!topicToQueue.containsKey(topic)) {
            synchronized (topicToQueue) {
                if (!topicToQueue.containsKey(topic)) {
                    topicToQueue.put(topic,new ArrayDeque<>());
                }
            }
        }
    }
}
