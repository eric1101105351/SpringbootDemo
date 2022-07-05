package com.example.demo.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.demo.constant.DataSourceName;
import com.example.demo.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/query")
@DS(DataSourceName.READ_ONLY)
public class QueryController {

    private final QueryService queryService;
    private final Semaphore concurrentLimit = new Semaphore(1);

    @GetMapping("/hashtag")
    public ResponseEntity<Object> hashtagQuery(@RequestParam int runTimes) {
        try {
            concurrentLimit.acquire();
            queryService.queryByForeachHashtag(runTimes);
            concurrentLimit.release();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/dollarSign")
    public ResponseEntity<Object> dollarSignQuery(@RequestParam int runTimes) {
        queryService.queryByForeachDollarSign(runTimes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/preparedStatement")
    public ResponseEntity<Object> preparedStatementQuery(@RequestParam int runTimes) {
        queryService.queryByPreparedStatement(runTimes);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/statement")
    public ResponseEntity<Object> statementQuery(@RequestParam int runTimes) {
        queryService.queryByStatement(runTimes);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
