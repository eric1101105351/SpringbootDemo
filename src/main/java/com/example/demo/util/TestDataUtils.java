package com.example.demo.util;

import com.example.demo.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TestDataUtils {

    public static List<String> getTestList() {
        try {
            return FileUtils.readLines(ResourceUtils.getFile("classpath:testData.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public static List<Account> getAccountList() {
        List<String> testList = getTestList();
        List<Account> accountList = new ArrayList<>();
        for (int i = 0; i < testList.size(); i++) {
            Account account = new Account();
            account.setJkosAccount(UUID.randomUUID().toString().substring(0, 15));
            account.setJkosId(testList.get(i));
            account.setId1(UUID.randomUUID().toString().substring(0, 5));
            account.setId2(UUID.randomUUID().toString().substring(0, 5));
            account.setId3(UUID.randomUUID().toString().substring(0, 5));
            account.setId4(UUID.randomUUID().toString().substring(0, 5));
            account.setId5(UUID.randomUUID().toString().substring(0, 5));
            account.setId6(UUID.randomUUID().toString().substring(0, 5));
            account.setId7(UUID.randomUUID().toString().substring(0, 5));
            account.setId8(UUID.randomUUID().toString().substring(0, 5));
            account.setId9(UUID.randomUUID().toString().substring(0, 5));
            account.setId10(UUID.randomUUID().toString().substring(0, 5));
            account.setId11(UUID.randomUUID().toString().substring(0, 5));
            account.setId12(UUID.randomUUID().toString().substring(0, 5));
            account.setId13(UUID.randomUUID().toString().substring(0, 5));
            account.setId14(UUID.randomUUID().toString().substring(0, 5));
            account.setId15(UUID.randomUUID().toString().substring(0, 5));
            account.setId16(UUID.randomUUID().toString().substring(0, 5));
            accountList.add(account);
        }
        return accountList;
    }

}
