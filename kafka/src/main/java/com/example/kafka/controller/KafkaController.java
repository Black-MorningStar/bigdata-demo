package com.example.kafka.controller;

import com.example.kafka.produce.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private ProduceService produceService;

    @PostMapping("/produce")
    public void produce(String key, String val) throws ExecutionException, InterruptedException {
        produceService.produce(key,val);
    }
}
