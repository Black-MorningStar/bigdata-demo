package com.example.mongo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mongo.model.User;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private MongoDatabase mongoDatabase;

    @PostMapping("/insertOne")
    public void insertOne() {
        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        User user = User.build(null, "测试用户-01", 18, "这是一个测试的");
        user.addProvince("AH","安徽省","AQ","安庆市","QS","潜山市");
        InsertOneResult insertOneResult = collection.insertOne(user);
        String id = insertOneResult.getInsertedId().asObjectId().getValue().toString();
        boolean success = insertOneResult.wasAcknowledged();
        System.out.println("数据ID：" + id + ",结果：" + success);
    }

    @PostMapping("/insertMany")
    public void insertMany() {
        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        User user1 = User.build(null, "测试用户-02", 19, "这是一个测试的");
        user1.addProvince("HN","河南省","LY","洛阳市","KF","开封市");
        User user2 = User.build(null, "测试用户-03", 20, "这是一个测试的");
        user2.addProvince("SH","上海市","PT","普陀区","CN","长宁区");
        InsertManyResult result = collection.insertMany(Lists.newArrayList(user1, user2));
        boolean success = result.wasAcknowledged();
        result.getInsertedIds().values().forEach(it -> {
            String id = it.asObjectId().getValue().toString();
            System.out.println("数据ID：" + id + ",结果：" + success);
        });
    }

    @PostMapping("/updateOne")
    public void updateOne() {
        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        Bson filter = Filters.eq("userName", "测试用户-02");
        Bson update = Updates.set("age", 22);
        UpdateResult result = collection.updateOne(filter, update);
        long matchedCount = result.getMatchedCount();
        long modifiedCount = result.getModifiedCount();
        System.out.println("matchedCount: " + matchedCount + ", modifiedCount: " + modifiedCount);
    }

    @PostMapping("/updateMany")
    public void updateMany() {
        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        Bson filter = Filters.eq("userName", "测试用户-02");
        Bson update = Updates.set("age", 25);
        UpdateResult result = collection.updateOne(filter, update);
        long matchedCount = result.getMatchedCount();
        long modifiedCount = result.getModifiedCount();
        System.out.println("matchedCount: " + matchedCount + ", modifiedCount: " + modifiedCount);
    }

    @PostMapping("/query")
    public void query() {
        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        //
        Bson filter = Filters.and(Filters.gte("age", 20),Filters.regex("userName","用户"));
        FindIterable<User> result = collection.find(filter).sort(Sorts.ascending("age"));
        result.forEach(it -> {
            System.out.println("获得的结果为: " + JSONObject.toJSONString(it));
        });
    }

}
