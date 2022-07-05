package com.example.demo.service;

import com.example.demo.AsyncResult;
import com.example.demo.dao.AccountMapper;
import com.example.demo.entity.Account;
import com.example.demo.util.TestDataUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateService {

    private final AccountMapper mapper;
    private final SqlSessionFactory sqlSessionFactory;
    @Qualifier("taskExecutor")
    private final TaskExecutor taskExecutor;

    public void insertByForeachHashtag(int runTimes, int batchSize) {
        List<Account> accountList = TestDataUtils.getAccountList();
        List<List<Account>> partition = Lists.partition(accountList, batchSize);
        log.info("partition size : {}", partition.size());
        LocalDateTime start = LocalDateTime.now();
        AtomicInteger executeCount = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(runTimes * partition.size());
        for (int i = 0; i < runTimes; i++) {
            for (int j = 0; j < partition.size(); j++) {
                int partitionIndex = j;
                CompletableFuture.supplyAsync(() -> {
                    int index = executeCount.getAndIncrement();
                    LocalDateTime startAt = LocalDateTime.now();
                    mapper.hashtagBatchInsert(partition.get(partitionIndex));
                    LocalDateTime endAt = LocalDateTime.now();
                    countDownLatch.countDown();
                    return new AsyncResult(index, null, startAt, endAt);
                }, taskExecutor).whenCompleteAsync((result, ex) -> {
                    log.info("第 {} 次 insert 結束, 耗時 : {} ms", result.getIndex(), Duration.between(result.getStartAt(), result.getEndAt()).toMillis());
                }, taskExecutor);
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void insertByForeachDollarSign(int runTimes, int batchSize) {
        List<Account> accountList = TestDataUtils.getAccountList();
        List<List<Account>> partition = Lists.partition(accountList, batchSize);
        log.info("partition size : {}", partition.size());
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < runTimes; i++) {
            for (int j = 0; j < partition.size(); j++) {
                mapper.dollarSignBatchInsert(partition.get(j));
            }
            log.info("insert complete : {}", i + 1);
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("run : {} times, avg foreach dollarSign cost : {} ms", runTimes, Duration.between(start, end).toMillis() / runTimes);
    }

    public void insertByBatchHashTag(int runTimes, int batchSize) {
        List<Account> accountList = TestDataUtils.getAccountList();
        List<List<Account>> partition = Lists.partition(accountList, batchSize);
        log.info("partition size : {}", partition.size());
        LocalDateTime start = LocalDateTime.now();
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            AccountMapper batchInsertMapper = session.getMapper(AccountMapper.class);
            for (int i = 0; i < runTimes; i++) {
                for (int j = 0; j < partition.size(); j++) {
                    List<Account> thisPartition = partition.get(j);
                    for (int k = 0; k < thisPartition.size(); k++) {
                        batchInsertMapper.hashtagInsert(thisPartition.get(k));
                    }
                    session.commit();
                }
                log.info("insert complete : {}", i + 1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("run : {} times, avg batch hashtag cost : {} ms", runTimes, Duration.between(start, end).toMillis() / runTimes);
    }

    public void insertByBatchDollarSign(int runTimes, int batchSize) {
        List<Account> accountList = TestDataUtils.getAccountList();
        List<List<Account>> partition = Lists.partition(accountList, batchSize);
        log.info("partition size : {}", partition.size());
        LocalDateTime start = LocalDateTime.now();
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            AccountMapper batchInsertMapper = session.getMapper(AccountMapper.class);
            for (int i = 0; i < runTimes; i++) {
                for (int j = 0; j < partition.size(); j++) {
                    List<Account> thisPartition = partition.get(j);
                    for (int k = 0; k < thisPartition.size(); k++) {
                        batchInsertMapper.dollarSignInsert(thisPartition.get(k));
                    }
                    session.commit();
                }
                log.info("insert complete : {}", i + 1);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("run : {} times, avg batch dollarSign cost : {} ms", runTimes, Duration.between(start, end).toMillis() / runTimes);
    }

}
