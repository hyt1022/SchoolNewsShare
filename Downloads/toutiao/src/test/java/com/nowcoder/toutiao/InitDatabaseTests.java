package com.nowcoder.toutiao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nowcoder.toutiao.DAO.CommentDAO;
import com.nowcoder.toutiao.DAO.LoginTicketDAO;
import com.nowcoder.toutiao.DAO.NewsDAO;
import com.nowcoder.toutiao.DAO.UserDAO;
import com.nowcoder.toutiao.model.*;
import com.nowcoder.toutiao.Service.CommentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void initData() {
        Random random = new Random();


    }

}
