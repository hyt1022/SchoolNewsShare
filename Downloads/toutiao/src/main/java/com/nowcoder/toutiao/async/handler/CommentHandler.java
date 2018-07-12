package com.nowcoder.toutiao.async.handler;


import com.nowcoder.toutiao.Service.CommentService;
import com.nowcoder.toutiao.Service.MessageService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Comment;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CommentHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(3);
        User user = userService.getUser(model.getActorId());
        message.setContent(user.getUsername()+ "评论了你的资讯，http://localhost:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        int toId = model.getEntityOwnerId();
        message.setToId(toId);
        message.setConversationId(3 < toId ? String.format("%d_%d",3,toId):String.format("%d_%d",toId,3));
        messageService.addMessage(message);

        System.out.println("commented");


    }

    @Override
    public List<EventType> getSupportsEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
