package com.nowcoder.toutiao.Cotroller;


import com.nowcoder.toutiao.Service.LikeService;
import com.nowcoder.toutiao.Service.NewsService;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventProducer;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Entitytype;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.util.Toutiao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value= "/like" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        try {
            int userId = hostHolder.getUser().getId();
            if(likeService.getLikeStatus(userId, Entitytype.Entity_NEWS,newsId) != 1){
                long likeCount = likeService.like(userId, Entitytype.Entity_NEWS, newsId);
                News news = newsService.getById(newsId);
                newsService.updateLikeCount(newsId, (int) likeCount);

                eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                        .setEntityType(Entitytype.Entity_NEWS).setEntityOwnerId(news.getUserId()));
                return Toutiao.getJSONString(0, String.valueOf(likeCount));
            }else{
                long likeCount = likeService.like(userId, Entitytype.Entity_NEWS, newsId);
                return Toutiao.getJSONString(0,String.valueOf(likeCount));
            }
        } catch (Exception e) {
            logger.error("点赞异常" + e.getMessage());
            return Toutiao.getJSONString(1, "点赞异常");
        }
    }

    @RequestMapping(value= "/dislike" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        try {
            int userId = hostHolder.getUser().getId();
            long likeCount = likeService.dislike(userId, Entitytype.Entity_NEWS, newsId);
            newsService.updateCommentCount(newsId, (int) likeCount);
            return Toutiao.getJSONString(0, String.valueOf(likeCount));
        }catch (Exception e) {
            logger.error("点赞异常" + e.getMessage());
            return Toutiao.getJSONString(1, "点赞异常");
        }
    }
}
