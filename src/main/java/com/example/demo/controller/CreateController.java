package com.example.demo.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.demo.constant.DataSourceName;
import com.example.demo.service.CreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/create")
@DS(DataSourceName.PRIMARY)
public class CreateController {

    private final CreateService createService;

    @PostMapping("/foreachHashtag")
    public ResponseEntity<Object> foreachHashtag(@RequestParam int runTimes, @RequestParam int batchSize) {
        createService.insertByForeachHashtag(runTimes, batchSize);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/foreachDollarSign")
    public ResponseEntity<Object> foreachDollarSign(@RequestParam int runTimes, @RequestParam int batchSize) {
        createService.insertByForeachDollarSign(runTimes, batchSize);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/batchHashtag")
    public ResponseEntity<Object> batchHashtag(@RequestParam int runTimes, @RequestParam int batchSize) {
        createService.insertByBatchHashTag(runTimes, batchSize);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/batchDollarSign")
    public ResponseEntity<Object> batchDollarSign(@RequestParam int runTimes, @RequestParam int batchSize) {
        createService.insertByBatchDollarSign(runTimes, batchSize);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
