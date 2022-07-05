package com.example.demo.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.demo.AsyncResult;
import com.example.demo.constant.DataSourceName;
import com.example.demo.dao.AccountMapper;
import com.example.demo.util.TestDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.example.demo.util.GsonUtils.toJson;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueryService {

    private final AccountMapper mapper;
    private final SqlSessionFactory sqlSessionFactory;
    @Qualifier("taskExecutor")
    private final TaskExecutor taskExecutor;

    @DS(DataSourceName.READ_ONLY)
    public void queryByForeachHashtag(int runTimes) {
        List<String> testList = TestDataUtils.getTestList();
        AtomicInteger executeCount = new AtomicInteger(0);
        AtomicLong timeCount = new AtomicLong(0);
        CountDownLatch countDownLatch = new CountDownLatch(runTimes);
        for (int i = 0; i < runTimes; i++) {
            CompletableFuture.supplyAsync(() -> {
                int index = executeCount.getAndIncrement();
                LocalDateTime startAt = LocalDateTime.now();
                List<String> queryResult = mapper.listJkosIdByJkosListHashTag(testList);
                LocalDateTime endAt = LocalDateTime.now();
                AsyncResult<List<String>> result = new AsyncResult(index, queryResult, startAt, endAt);
                return result;
            }, taskExecutor).whenCompleteAsync((result, ex) -> {
                List<String> queryResult = result.getResult();
                log.info("第 {} 次查詢結束, 查詢資料筆數 : {}, 耗時 : {} ms", result.getIndex(), queryResult.size(), Duration.between(result.getStartAt(),
                        result.getEndAt()).toMillis());
                timeCount.getAndAdd(Duration.between(result.getStartAt(), result.getEndAt()).toMillis());
                countDownLatch.countDown();
            }, taskExecutor);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("run : {} times, avg hashTag cost : {} ms", runTimes, timeCount.get() / runTimes);
    }

    @DS(DataSourceName.READ_ONLY)
    public void queryByForeachDollarSign(int runTimes) {
        List<String> testList = TestDataUtils.getTestList();
        List<String> resultList = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < runTimes; i++) {
            resultList = mapper.listJkosIdByJkosListDollarSign(testList);
            log.info("已執行 {} 次", i + 1);
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("resultList : {}", toJson(resultList));
        log.info("run : {} times, avg dollarSign cost : {} ms", runTimes, (Duration.between(start, end).toMillis() / runTimes));
    }

    @DS(DataSourceName.READ_ONLY)
    public void queryByPreparedStatement(int runTimes) {
        List<String> testList = TestDataUtils.getTestList();
        String sqlFormat = "select JKOSID from Account where JKOSID in (%s)";
        List<String> resultList = new ArrayList<>(testList.size());
        String replaceSymbol = testList.stream().map(o -> "?").collect(Collectors.joining(","));
        String sql = String.format(sqlFormat, replaceSymbol);

        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < runTimes; i++) {
            resultList = executePreparedStatementQuery(testList, sql);
        }
        LocalDateTime end = LocalDateTime.now();

        log.info("resultList : {}", toJson(resultList));
        log.info("run : {} times, avg preparedStatement cost : {} ms", runTimes, (Duration.between(start, end).toMillis() / runTimes));
    }

    private List<String> executePreparedStatementQuery(List<String> testList, String sql) {
        List<String> resultList = new ArrayList<>(testList.size());
        try (Connection connection = sqlSessionFactory.openSession().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                log.info("preparedStatement : {} parameter set start", sql);
                for (int i = 0; i < testList.size(); i++) {
                    preparedStatement.setString(i + 1, testList.get(i));
                }
                log.info("preparedStatement : {} parameter set completed", sql);
                log.info("preparedStatement : {} execute start", sql);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    log.info("preparedStatement : {} execute completed", sql);
                    while (resultSet.next()) {
                        resultList.add(resultSet.getString(1));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultList;
    }

    @DS(DataSourceName.READ_ONLY)
    public void queryByStatement(int runTimes) {
        List<String> testList = TestDataUtils.getTestList();
        String sqlFormat = "select JKOSID from Account where JKOSID in (%s)";
        List<String> resultList = new ArrayList<>(testList.size());
        String queryValues = testList.stream().map(o -> "'" + o + "'").collect(Collectors.joining(","));
        String sql = String.format(sqlFormat, queryValues);

        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < runTimes; i++) {
            resultList = executeStatementQuery(sql);
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("resultList : {}", toJson(resultList));
        log.info("run : {} times, avg statement cost : {} ms", runTimes, (Duration.between(start, end).toMillis() / runTimes));
    }

    private List<String> executeStatementQuery(String sql) {
        List<String> resultList = new ArrayList<>();
        log.info("statement : {} execute start", sql);
        try (Connection connection = sqlSessionFactory.openSession().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            log.info("statement : {} execute completed", sql);
            while (resultSet.next()) {
                resultList.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultList;
    }

}
