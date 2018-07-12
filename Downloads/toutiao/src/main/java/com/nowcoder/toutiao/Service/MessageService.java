package com.nowcoder.toutiao.Service;


import com.nowcoder.toutiao.DAO.MessageDAO;
import com.nowcoder.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public List<Message> getConversationDetails(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetails(conversationId,offset,limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }

    public void deleteMessage(int messageId){
        messageDAO.deleteMessage(messageId,0);
    }
}
