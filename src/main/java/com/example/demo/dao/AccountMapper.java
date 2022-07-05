package com.example.demo.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.demo.constant.DataSourceName;
import com.example.demo.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountMapper {

    @DS(DataSourceName.READ_ONLY)
    @Select("<script>" +
            "select JKOSID " +
            "from account " +
            "where JKOSID in " +
            "<foreach collection = 'jkosIdList' item = 'jkosId' index = 'i' open = '(' close = ')' separator = ',' >" +
            " #{jkosId} </foreach>" +
            "</script>")
    List<String> listJkosIdByJkosListHashTag(List<String> jkosIdList);

    @DS(DataSourceName.READ_ONLY)
    @Select("<script>" +
            "select JKOSID " +
            "from account " +
            "where JKOSID in " +
            "<foreach collection = 'jkosIdList' item = 'jkosId' index = 'i' open = '(' close = ')' separator = ',' >" +
            " '${jkosId}' </foreach>" +
            "</script>")
    List<String> listJkosIdByJkosListDollarSign(List<String> jkosIdList);

    @Insert("<script>" +
            "insert into Account(JKOSAccount, JKOSID, ID1, ID2, ID3, ID4, ID5 , ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, " +
            "ID16)" +
            "values" +
            "<foreach collection = 'accountList' item = 'account' index = 'i' separator = ','>" +
            "(#{account.jkosAccount}, #{account.jkosId}, #{account.id1}, #{account.id2}, #{account.id3}, #{account.id4}, " +
            "#{account.id5}, #{account.id6}, #{account.id7}, #{account.id8}, #{account.id9}, #{account" +
            ".id10}, #{account.id11}, #{account.id12}, #{account.id13}, #{account.id14}, #{account.id15}, " +
            "#{account.id16})" +
            "</foreach>" +
            "</script>")
    void hashtagBatchInsert(List<Account> accountList);

    @Insert("<script>" +
            "insert into Account(JKOSAccount, JKOSID, ID1, ID2, ID3, ID4, ID5 , ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, " +
            "ID16)" +
            "values" +
            "<foreach collection = 'accountList' item = 'account' index = 'i' separator = ','>" +
            "('${account.jkosAccount}', '${account.jkosId}', '${account.id1}', '${account.id2}', '${account.id3}', '${account.id4}', " +
            "'${account.id5}', '${account.id6}', '${account.id7}', '${account.id8}', '${account.id9}', '${account" +
            ".id10}', '${account.id11}', '${account.id12}', '${account.id13}', '${account.id14}', '${account.id15}', " +
            "'${account.id16}')" +
            "</foreach>" +
            "</script>")
    void dollarSignBatchInsert(List<Account> accountList);

    @Insert("insert into Account" +
            "(JKOSAccount, JKOSID, ID1, ID2, ID3, ID4, ID5 , ID6, ID7, ID8" +
            ", ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16) values" +
            "(#{account.jkosAccount}, #{account.jkosId}, #{account.id1}, #{account.id2}, #{account.id3}, #{account.id4}, " +
            "#{account.id5}, #{account.id6}, #{account.id7}, #{account.id8}, #{account.id9}, #{account" +
            ".id10}, #{account.id11}, #{account.id12}, #{account.id13}, #{account.id14}, #{account.id15}, " +
            "#{account.id16})")
    void hashtagInsert(@Param("account") Account account);

    @Insert("insert into Account" +
            "(JKOSAccount, JKOSID, ID1, ID2, ID3, ID4, ID5 , ID6, ID7, ID8" +
            ", ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16) values" +
            "('${account.jkosAccount}', '${account.jkosId}', '${account.id1}', '${account.id2}', '${account.id3}', '${account.id4}', " +
            "'${account.id5}', '${account.id6}', '${account.id7}', '${account.id8}', '${account.id9}', '${account.id10}', " +
            "'${account.id11}', '${account.id12}', '${account.id13}', '${account.id14}', '${account.id15}', " +
            "'${account.id16}')")
    void dollarSignInsert(@Param("account") Account account);

}