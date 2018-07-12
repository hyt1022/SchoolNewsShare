package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.DAO.UserDAO;
import com.nowcoder.toutiao.Service.RelationService;
import com.nowcoder.toutiao.Service.UserService;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventProducer;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Entitytype;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.Relation;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;

@Controller
public class RelationController {
    private static final Logger logger = LoggerFactory.getLogger(RelationController.class);

    @Autowired
    RelationService relationService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;


    @RequestMapping(value="/addRelation" , method = {RequestMethod.GET,RequestMethod.POST})
    public String addRelation(@RequestParam("toId") int toId){
        try{
            int localUserId = hostHolder.getUser().getId();
            long followCount = relationService.follow(localUserId,System.currentTimeMillis(),toId);

            eventProducer.fireEvent(new EventModel(EventType.FANS).setActorId(hostHolder.getUser().getId()).setEntityOwnerId(toId));

            //return Toutiao.getJSONString((int)followCount);
        }catch(Exception e){
            logger.error("关注异常" + e.getMessage());
            //return Toutiao.getJSONString(1,"关注异常");
        }
        return "redirect:/user/"+String.valueOf(toId);
    }

    @RequestMapping(value="/deleteRelation", method = {RequestMethod.GET,RequestMethod.POST})
    public String deleteRelation(@RequestParam("toId") int toId){
        try{
            int localUserId = hostHolder.getUser().getId();
            long followCount = relationService.disfollow(localUserId,toId);
            //return Toutiao.getJSONString((int)followCount);
        }catch(Exception e){
            logger.error("取消关注异常" + e.getMessage());
            //return Toutiao.getJSONString(1,"取消关注异常");
        }
        return "redirect:/user/"+String.valueOf(toId);
    }



    @RequestMapping("/oldAddRelation")
    @ResponseBody
    public String addRelation(@RequestParam("fromId") int fromId,
                              @RequestParam("toId") int toId){
        try{
            Relation relation = new Relation();
            relation.setFromId(fromId);
            relation.setToId(toId);
            relation.setRelationType(1);
            relationService.addRelation(relation);
            //更新用户的关注数和粉丝数
            int followCount = relationService.getUserFollowCount(fromId);
            userService.updateFollowCount(fromId,followCount);
            int fansCount = relationService.getUserFansCount(toId);
            userService.updateFansCount(toId,fansCount);
            return Toutiao.getJSONString(relation.getId());
        }catch (Exception e){
            logger.error("关注失败"+e.getMessage());
            return Toutiao.getJSONString(1,"添加关注失败");
        }
    }

    @RequestMapping("/oldDeleteRelation")
    @ResponseBody
    public String deleteRelation(@RequestParam("fromId") int fromId,
                                 @RequestParam("toId") int toId){
        try{
            relationService.deleteByFromAndToId(fromId,toId);
            //更新用户的关注数和粉丝数
            int followCount = relationService.getUserFollowCount(fromId);
            userService.updateFollowCount(fromId,followCount);
            int fansCount = relationService.getUserFansCount(toId);
            userService.updateFansCount(toId,fansCount);
            return Toutiao.getJSONString(0,"取消关注成功");
        }catch (Exception e){
            logger.error("取消关注失败"+e.getMessage());
            return Toutiao.getJSONString(1,"取消关注失败");
        }
    }




}
