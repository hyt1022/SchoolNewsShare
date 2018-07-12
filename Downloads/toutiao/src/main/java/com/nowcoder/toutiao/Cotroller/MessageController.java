package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.DAO.MessageDAO;
import com.nowcoder.toutiao.Service.MessageService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.model.ViewObject;
import com.nowcoder.toutiao.util.Toutiao;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @RequestMapping(value= "/msg/addMessage" , method = {RequestMethod.GET,RequestMethod.POST})
    public String addMessage(
                          @RequestParam("toId") int toId,
                          @RequestParam("content") String content){
        try{
            int fromId = hostHolder.getUser().getId();
            Message message = new Message();
            message.setContent(content);
            message.setFromId(fromId);
            message.setToId(toId);
            message.setCreatedDate(new Date());
            message.setConversationId(fromId < toId ? String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
            messageDAO.addMessage(message);
            return "redirect:/user/" + String.valueOf(toId);
        }catch(Exception e){
            logger.error("添加评论失败"+ e.getMessage());
            //return Toutiao.getJSONString(1,"发送消息失败");
            return "redirect:/user/" + String.valueOf(toId);
        }
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model,
                                     @RequestParam(value = "page" ,defaultValue = "1") int page) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, (page-1)*10, page*10);
            List<Message> conversationall = messageService.getConversationList(localUserId, 0, 10000);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.setMessage(msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();//如果信息的发送方是当前用户，说明target是toId
                User user = userService.getUser(targetId);
                vo.setUser(user);
                vo.setUnreadCount(messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            model.addAttribute("page",page);
            model.addAttribute("size",(conversationall.size()/10)+1);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId,
                                     @RequestParam(value = "page" ,defaultValue = "1") int page) {
        try {
            List<Message> conversationList = messageService.getConversationDetails(conversationId, (page-1)*10, page*10);
            List<Message> conversationall = messageService.getConversationDetails(conversationId, 0, 10000);
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.setMessage(msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.setUser(user);
                messages.add(vo);
            }
            model.addAttribute("conversationId",conversationId);
            model.addAttribute("page",page);
            model.addAttribute("size",(conversationall.size()/10)+1);
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value= "/msg/deleteMessage" , method = {RequestMethod.GET,RequestMethod.POST})
    public String deleteMessage(@RequestParam("msgId") int msgId){
        try{
            messageService.deleteMessage(msgId);
            //return Toutiao.getJSONString(0,"删除成功");
        }catch (Exception e){
            logger.error("删除失败"+e.getMessage());
            //return Toutiao.getJSONString(1,"删除失败");
        }
        return "redirect:/msg/detail";
    }


}
