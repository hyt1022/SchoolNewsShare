package com.nowcoder.toutiao;

import com.nowcoder.toutiao.DAO.CommentDAO;
import com.nowcoder.toutiao.DAO.MessageDAO;
import com.nowcoder.toutiao.DAO.NewsDAO;
import com.nowcoder.toutiao.DAO.UserDAO;
import com.nowcoder.toutiao.model.Comment;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToutiaoApplicationTests {

	@Autowired
	UserDAO userDAO;

	@Autowired
	NewsDAO newsDAO;

	@Autowired
	CommentDAO commentDAO;

	@Autowired
	MessageDAO messageDAO;

	@Test
	public void initializeUserDAO() {
		User user = new User();
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		user.setName("test");
		user.setPassword("10086");
		user.setSalt("asdf");
		user.setUsername("testuser");
		userDAO.addUser(user);
	}

	@Test
	public void initializeNewsDAO(){
		for(int i = 0;i<4;i++){
			News news = new News();
			news.setUserId(i);
			news.setCreatedDate(new Date());
			news.setLink(String.format("testlink%d",i));
			news.setTitle(String.format("testtitle%d",i));
			news.setImage("a");
			newsDAO.addNews(news);
		}
	}

	@Test
	public void initializeCommentDAO(){
		for(int i= 0;i<10;i++){
			Comment comment = new Comment();
			comment.setContent(String.format("Hello my friend%d",i));
			comment.setCreatedDate(new Date());
			comment.setEntityId(i+10);
			comment.setEntityType(1);
			comment.setUserId(i+25);
			comment.setStatus(0);
			commentDAO.addComment(comment);
		}
	}

	@Test
	public void addNewsDAO(){

		for(int i = 25;i<34;i++){
			News news = new News();
			news.setCommentCount(i-15);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(100)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}", i));
			news.setLink(String.format("http://localhost/news/%d", news.getId()));
			newsDAO.addNews(news);
		}

	}

	@Test
	public void testMessageDAO(){
		List<Message> list = messageDAO.getConversationList(12,0,10);
		for(Message m : list){
			System.out.println(m.getContent());
		}
	}
}
