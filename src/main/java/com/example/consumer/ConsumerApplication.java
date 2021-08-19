package com.example.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SpringBootApplication
@RestController
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }


    @Autowired
    KafkaProducer kafkaProducer;

    @GetMapping("/t/{opt}")
    public Object t(@RequestBody JSONObject obj, @PathVariable(required = false) String opt) {
        if ("s".equalsIgnoreCase(opt)) {
            kafkaProducer.send(obj);
        } else {

        }
        return null;
    }

    @Component
    @Slf4j
    public static class KafkaConsumer {
        @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP1)
        public void topic_test(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

            Optional message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                Object msg = message.get();
                log.info("topic_test 消费了： Topic:" + topic + ",Message:" + msg);
                ack.acknowledge();
            }
        }

        @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP2)
        public void topic_test1(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

            Optional message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                Object msg = message.get();
                log.info("topic_test1 消费了： Topic:" + topic + ",Message:" + msg);
                ack.acknowledge();
            }
        }

        @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP2)
        public void topic_test2(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

            Optional message = Optional.ofNullable(record.value());
            if (message.isPresent()) {
                Object msg = message.get();
                log.info("topic_test2 消费了： Topic:" + topic + ",Message:" + msg);
                ack.acknowledge();
            }
        }
    }

    @Component
    @Slf4j
    public static class KafkaProducer {

        @Autowired
        private KafkaTemplate<String, Object> kafkaTemplate;

        //自定义topic
        public static final String TOPIC_TEST = "helloworld";

        //
        public static final String TOPIC_GROUP1 = "topic.group1";

        //
        public static final String TOPIC_GROUP2 = "topic.group2";

        public void send(Object obj) {
            String obj2String = JSON.toJSONString(obj);
            log.info("准备发送消息为：{}", obj2String);
            //发送消息
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC_TEST, obj2String);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //发送失败的处理
                    log.info(TOPIC_TEST + " - 生产者 发送消息失败：" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    //成功的处理
                    log.info(TOPIC_TEST + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                }
            });
        }
    }

}
