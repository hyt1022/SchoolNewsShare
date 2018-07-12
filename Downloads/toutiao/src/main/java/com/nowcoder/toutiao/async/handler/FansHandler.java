package com.nowcoder.toutiao.async.handler;


import com.nowcoder.toutiao.Service.MessageService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FansHandler implements EventHandler{
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(2);
        User user = userService.getUser(model.getActorId());
        message.setContent(user.getUsername()+ "成为了你的新粉丝");
        message.setCreatedDate(new Date());
        int toId = model.getEntityOwnerId();
        message.setToId(toId);
        message.setConversationId(2 < toId ? String.format("%d_%d",2,toId):String.format("%d_%d",toId,2));
        messageService.addMessage(message);

        System.out.println("fansed");
    }

    @Override
    public List<EventType> getSupportsEventTypes() {
        return Arrays.asList(EventType.FANS);
    }
}
