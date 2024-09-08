package com.example.kafka.produce;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ProduceService {

    @Autowired
    private KafkaProducer<String,String> producer;

    public void produce(String key, String message) throws ExecutionException, InterruptedException {
        ProducerRecord record = new ProducerRecord("testTopicOne",key, message);
        Future send = producer.send(record);
        send.get();
        System.out.println("发送消息Key:"+key+",Value:"+message);
    }
}
