package com.example.kafka.cosumer;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ConsumerOne implements InitializingBean {

    private static Properties properties = new Properties();
    static {
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"groupOne");
        properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG,"consumerOne");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,100);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092,localhost:9093");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CompletableFuture.runAsync(() -> {
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
            kafkaConsumer.subscribe(Lists.newArrayList("testTopicOne"));
            kafkaConsumer.poll(Duration.ofMillis(100)); //调用一次poll才会为consumer分配分区
            Set<TopicPartition> partitions = kafkaConsumer.assignment();
            partitions.forEach(it -> {
                System.out.println("consumerOne消费的分区:" + it.partition());
            });

            while(true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                Set<TopicPartition> recordPartitions = records.partitions();
                //按照分区的粒度进行消息处理
                for (TopicPartition partition : recordPartitions) {
                    List<ConsumerRecord<String, String>> recordList = records.records(partition);
                    for (ConsumerRecord<String, String> record : recordList) {
                        System.out.println("consumerOne消费到消息. Key:"+record.key() + ",Value:" + record.value() + ",Offset:" + record.offset());
                        //按照每条消息粒度提交偏移量
                        Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
                        map.put(partition,new OffsetAndMetadata(record.offset()));
                        kafkaConsumer.commitSync(map);
                    }
                    //按照单个分区粒度提交偏移量
                    /*Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
                    map.put(partition,new OffsetAndMetadata(recordList.get(recordList.size()-1).offset()));
                    kafkaConsumer.commitSync(map);*/
                }
                //按照poll一批的粒度提交偏移量
                //kafkaConsumer.commitSync();
            }
        });
    }
}
