package com.nowcoder.toutiao.Service;

import com.nowcoder.toutiao.DAO.LoginTicketDAO;
import com.nowcoder.toutiao.DAO.UserDAO;
import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.util.Toutiao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;



    public Map<String,Object> register(String username, String password, String nickname, String school,int age,int sex){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }

        User user  = userDAO.selectByName(username);
        if(user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(Toutiao.MD5(password+user.getSalt()));
        user.setHeadUrl("");
        user.setUsername(nickname);
        user.setSchool(school);
        user.setAge(age);
        user.setSex(sex);

        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }

        if(StringUtils.isEmpty(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!Toutiao.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void updateFollowCount(int userId,int followCount){
        userDAO.updateFollowCount(userId,followCount);
    }

    public void updateFansCount(int userId,int fansCount){
        userDAO.updateFansCount(userId,fansCount);
    }

    public void updateHeadUrl(int userId, String headUrl){
        userDAO.updateHeadUrl(headUrl,userId);
    }

    public void updateUserInfo(int userId,String username,int sex,int age, String intro,String school){
        userDAO.updateUserInfo(userId,username,sex,age,intro,school);
    }
}
