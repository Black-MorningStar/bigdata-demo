package com.example.mongo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    /**
     * 创建mongoDB客户端
     */
    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/?minPoolSize=4&maxPoolSize=20");
        return mongoClient;
    }

    /**
     * 创建MongoDB数据库
     */
    @Bean
    public MongoDatabase mongoDataBase(MongoClient mongoClient) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(pojoCodecProvider));
        MongoDatabase database = mongoClient.getDatabase("demo").withCodecRegistry(pojoCodecRegistry);
        return database;
    }
}
